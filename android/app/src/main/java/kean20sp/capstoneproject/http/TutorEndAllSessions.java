package kean20sp.capstoneproject.http;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class TutorEndAllSessions  extends HTTPConnectionHandler{
    private String host = null, filepath = null;

    public TutorEndAllSessions(String host, String filepath){
        this.host=host;
        this.filepath=filepath;
    }

    public TutorEndAllSessions(){
        host = "seesselm-project-page.com";
        filepath = "/Capstone/android/End_All_Sessions.php";
    }
    private String response;
    private boolean response_done = false;
    private List<NameValuePair> pairs;
    public String All_Session_End(String TutorID) { //why string?
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("user_role_id", TutorID));

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    response = makeRequest(host, filepath, pairs);
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    response_done = true;
                    System.out.println(response);
                }
            }
        });

        t.start();

        long tlim = 1000 * 2;
        long start = System.currentTimeMillis();
        while (!response_done & (System.currentTimeMillis() - start) <= tlim) {
        }
        response_done = false;

        return process(response);
    }

    private String process(String response) {
        if(response == null){
            return "Connection Failed";
        }
        if (response.equals("S01")) {
            return SESSION_END_SUCCESS;
        } else {
            return SESSION_END_FAILURE;
        }
    }
    public static final String SESSION_END_SUCCESS ="Tutor Session Ended";
    public static final String SESSION_END_FAILURE ="Rut Roh";
}
