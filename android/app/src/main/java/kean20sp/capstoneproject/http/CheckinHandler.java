package kean20sp.capstoneproject.http;

import android.util.Log;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import kean20sp.capstoneproject.util.AppState;

public class CheckinHandler extends HTTPConnectionHandler {
    private String host = null;
    private String filepath = null;
    private String session_id = AppState.Session.id;
    private ArrayList<NameValuePair> pairs;

    public CheckinHandler(){
        host = "seesselm-project-page.com";
        filepath = "/Capstone/android/android_student_checkin.php";
    }

    public String session_id(){
        return session_id;
    }

    private String response;
    private boolean response_done = false;

    public String checkin_qr(String tutor_role_id, String student_role_id, String qr_server_key, String course_id){
        //TODO: MAKE SURE THIS PASSES CORRECT EMAILS DEPENDING WHAT MODE WE IN
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("course_id", course_id));
        pairs.add(new BasicNameValuePair("student_role_id",student_role_id));
        pairs.add(new BasicNameValuePair("tutor_role_id",tutor_role_id));
        pairs.add(new BasicNameValuePair("qr_server_key",qr_server_key));
        pairs.add(new BasicNameValuePair("session_id",this.session_id));
        pairs.add(new BasicNameValuePair("checkin_type","qrcode"));
        pairs.add(new BasicNameValuePair("checkin_mode","student"));

        Thread t = new Thread(new Runnable(){
            public void run(){
                try{
                    response = makeRequest(host,filepath,pairs);
                }catch(Exception e){
                    System.out.println(e);
                }finally {
                    response_done = true;
                    System.out.println(response);
                }
            }
        });

        t.start();

        long tlim = 1000*2;
        long start = System.currentTimeMillis();
        while(!response_done & (System.currentTimeMillis() - start) <= tlim){}
        response_done = false;

        Log.d("qr_response", "response: "+ response);
        return process(response);
    }


    public String checkin_email(String tutor_id, String student, String checkin_mode){
        //TODO: MAKE SURE THIS PASSES CORRECT EMAILS DEPENDING WHAT MODE WE IN
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("tutor_role_id",tutor_id));
        pairs.add(new BasicNameValuePair("checkin_type","email"));
        pairs.add(new BasicNameValuePair("checkin_mode",checkin_mode));

        if (checkin_mode == "tutor"){
            pairs.add(new BasicNameValuePair("student_email",student));
        } else{
            pairs.add(new BasicNameValuePair("student_role_id",student));
        }


        Thread t = new Thread(new Runnable(){
            public void run(){
                try{
                    response = makeRequest(host,filepath,pairs);
                }catch(Exception e){
                    System.out.println(e);
                }finally {
                    response_done = true;
                    System.out.println(response);
                }
            }
        });

        t.start();

        long tlim = 1000*2;
        long start = System.currentTimeMillis();
        while(!response_done & (System.currentTimeMillis() - start) <= tlim){}
        response_done = false;

        return process(response);
    }

    private String process(String response) {
        if(response==null){
            return "null";
        }
        String[] response_split = response.split(";");
        String code = response_split[0];
        switch (code) {
            case "SC0":
            case "SCE0":
                return CHECKIN_SUCCESSFUL;
            case "FC0":
                return INVALID_SESSION;
            case "FC1":
                return TUTOR_SESSION_EXISTS;
            case "FQR1":
                return QRCODE_EXPIRED;
            case "FCE0":
                return COURSE_SELECT_FAILED;
            case "FCE1":
                return NO_VALID_SESSION;
            case "FCE2":
                return INVALID_STUDENT_EMAIL;
            default:
                return CHECKIN_FAILED;
        }
    }

    public static final String INVALID_EMAIL = "We didn't recognize that email.";
    public static final String INVALID_SESSION = "Login Session is invalid. Login Again.";
    public static final String TUTOR_SESSION_EXISTS = "Student already in an active session.";
    private static final String CHECKIN_FAILED = "Failed to check-in. Try again.";
    public static final String CHECKIN_SUCCESSFUL = "Check-in was successful.";
    private static final String QRCODE_EXPIRED = "QR-Code was expired.";
    private static final String COURSE_SELECT_FAILED = "Course selection failed.";
    private static final String NO_VALID_SESSION = "No valid tutoring session exists.";
    private static final String INVALID_STUDENT_EMAIL = "Invalid student email. Try again.";

}
