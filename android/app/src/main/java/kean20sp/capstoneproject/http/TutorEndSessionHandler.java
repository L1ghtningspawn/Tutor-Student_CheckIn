package kean20sp.capstoneproject.http;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class TutorEndSessionHandler extends HTTPConnectionHandler{
    private String host = null, filepath = null;

    //i think this is just for login?
    private String session_id = null;
    private String available_roles = null;
    //end


    public TutorEndSessionHandler(String host, String filepath){
        this.host=host;
        this.filepath=filepath;
    }

    public TutorEndSessionHandler(){
        host = "seesselm-project-page.com";
        filepath = "/Capstone/android/Tutor_Individual_checkout.php";
    }
    private String response;
    private boolean response_done = false;
    private List<NameValuePair> pairs;
    public String Individual_Session_End(String StudentID, String TutorID){ //why string?
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("user_role_id", TutorID));
        pairs.add(new BasicNameValuePair("student_id", StudentID));

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

        if(response.equals("S01")){
            return SESSION_END_SUCCESS;
        }
        else{
            return SESSION_END_FAILURE;
        }
    }
    public static final String SESSION_END_SUCCESS ="Tutor Session Ended";
    public static final String SESSION_END_FAILURE ="Rut Roh";
}
