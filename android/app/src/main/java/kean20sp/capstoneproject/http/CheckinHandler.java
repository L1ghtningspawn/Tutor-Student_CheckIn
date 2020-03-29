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
//    public CheckinHandler(String host, String filepath){
//        this.host = host;
//        this.filepath = filepath;
//        this.session_id = session_id();
//    }

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


    public String checkin_email(String tutor_email, String student_email, String checkin_mode){
        //TODO: MAKE SURE THIS PASSES CORRECT EMAILS DEPENDING WHAT MODE WE IN
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("tutor_email",tutor_email));
        pairs.add(new BasicNameValuePair("student_email",student_email));
        pairs.add(new BasicNameValuePair("session_id",this.session_id));
        pairs.add(new BasicNameValuePair("checkin_type","email"));
        pairs.add(new BasicNameValuePair("checkin_mode",checkin_mode));

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
                return CHECKIN_SUCCESSFUL;
            case "FCO":
                return INVALID_SESSION;
            case "FC1":
                return TUTOR_SESSION_EXISTS;
            case "FQR1":
                return QRCODE_EXPIRED;
            default:
                return CHECKIN_FAILED;
        }
    }

    public static final String INVALID_SESSION = "Login Session is invalid. Login Again.";
    public static final String TUTOR_SESSION_EXISTS = "Student already in an active session.";
    private static final String CHECKIN_FAILED = "Failed to check-in. Try again.";
    public static final String CHECKIN_SUCCESSFUL = "Check-in was successful.";
    public static final String QRCODE_EXPIRED = "QR-Code was expired.";

}