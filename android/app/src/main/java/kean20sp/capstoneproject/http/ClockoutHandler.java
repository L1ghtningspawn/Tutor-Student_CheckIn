package kean20sp.capstoneproject.http;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class ClockoutHandler extends HTTPConnectionHandler {
    private String host = null, filepath = null;
    private String clockout_date;

    public ClockoutHandler(String host, String filepath){
        this.host = host;
        this.filepath = filepath;
    }
    public ClockoutHandler(){
        host = "seesselm-project-page.com";
        filepath = "/Capstone/android/android_clockout.php";
    }

    private String response;
    private boolean response_done = false;
    private List<NameValuePair> pairs;
    public String clockout(String email, String session_id, String clockin_id){
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("clockout", "true"));
        pairs.add(new BasicNameValuePair("email", email));
        pairs.add(new BasicNameValuePair("session_id", session_id));
        pairs.add(new BasicNameValuePair("clockin_id", clockin_id));

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
        if(response == null){
            return CONNECTION_FAILED;
        }
        String[] response_split = response.split(";");
        String code = response_split[0];
        if(code.equals("SCl0")){
            clockout_date = response_split[1];
            return CLOCKOUT_SUCCESS;
        } else if(code.equals("FCl0")){
            return CLOCKOUT_FAILURE;
        } else if(code.equals("SCl1")){
            return UNKNOWN_FAILURE;
        } else {
            return SESSION_EXPIRED;
        }
    }

    public String getClockoutDate(){
        return clockout_date;
    }

    public static final String CONNECTION_FAILED = "Connection Failed";
    public static final String CLOCKOUT_SUCCESS = "Clockout Was Successful";
    public static final String CLOCKOUT_FAILURE = "Clockout Failed";
    public static final String UNKNOWN_FAILURE = "Unknown Failure";
    public static final String SESSION_EXPIRED = "Session Expired";
}
