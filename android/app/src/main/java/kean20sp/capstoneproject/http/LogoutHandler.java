package kean20sp.capstoneproject.http;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class LogoutHandler extends HTTPConnectionHandler {
    private String host = null, filepath = null;

    public LogoutHandler(String host, String filepath){
        this.host = host;
        this.filepath = filepath;
    }
    public LogoutHandler(){
        host = "seesselm-project-page.com";
        filepath = "/Capstone/android/android_logout.php";
    }

    private String response;
    private boolean response_done = false;
    private List<NameValuePair> pairs;
    public String logout(String email, String session_id){
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("logout", "true"));
        pairs.add(new BasicNameValuePair("email", email));
        pairs.add(new BasicNameValuePair("session_id", session_id));

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
        if(response.equals("SLo0")){
            return LOGOUT_SUCCESS;
        } else if(response.equals("FLo0")){
            return LOGOUT_FAILURE;
        } else {
            return UNKNOWN_FAILURE;
        }
    }

    public static final String CONNECTION_FAILED = "Connection Failed";
    public static final String LOGOUT_SUCCESS = "Logout Was Successful";
    public static final String LOGOUT_FAILURE = "Logout Failed";
    public static final String UNKNOWN_FAILURE = "Unknown Failure";
}
