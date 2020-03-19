package kean20sp.capstoneproject.http;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class ClockinHandler extends HTTPConnectionHandler {
    private String host = null, filepath = null;
    private String clockin_date;
    private String clockin_id;

    public ClockinHandler(String host, String filepath){
        this.host = host;
        this.filepath = filepath;
    }
    public ClockinHandler(){
        host = "seesselm-project-page.com";
        filepath = "/Capstone/android/android_clockin.php";
    }

    private String response;
    private boolean response_done = false;
    private List<NameValuePair> pairs;
    public String clockin(String email, String session_id, String user_role_id){
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("clock_in", "true"));
        pairs.add(new BasicNameValuePair("email", email));
        pairs.add(new BasicNameValuePair("session_id", session_id));
        pairs.add(new BasicNameValuePair("user_role_id", user_role_id));

//        try {
//            response = makeRequest(host, filepath, table);
//        } catch (Exception e) {
//            System.out.println(e);
//        }


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
        String[] response_split = response.split(";");
        String code = response_split[0];
        if(code.equals("SC0")){
            clockin_id = response_split[1];
            clockin_date = response_split[2];
            return CLOCKIN_SUCCESS;
        } else if(code.equals("FC0")){
            return CLOCKIN_FAILURE;
        } else if(code.equals("SC1")){
            return UNKNOWN_FAILURE;
        } else {
            return SESSION_EXPIRED;
        }
    }

    public String getClockinDate(){
        return clockin_date;
    }
    public String getClockin_id(){
        return clockin_id;
    }

    public static final String CLOCKIN_SUCCESS = "Clockin Was Successful";
    public static final String CLOCKIN_FAILURE = "Clockin Failed";
    public static final String UNKNOWN_FAILURE = "Unknown Failure";
    public static final String SESSION_EXPIRED = "Session Expired";
}
