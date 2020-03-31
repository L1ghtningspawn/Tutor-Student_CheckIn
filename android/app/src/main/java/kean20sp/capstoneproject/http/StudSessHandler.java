package kean20sp.capstoneproject.http;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import kean20sp.capstoneproject.util.AppState;

public class StudSessHandler extends HTTPConnectionHandler{
    private String host = null, filepath = null;

    public JSONArray student_sessions;

    public StudSessHandler(String host, String filepath){
        this.host = host;
        this.filepath = filepath;
    }
    public StudSessHandler(){
        host = "seesselm-project-page.com";
        filepath = "/Capstone/android/Student_Session_history.php";

    }

    private String response;
    private boolean response_done = false;
    private List<NameValuePair> pairs;

    public String getStudentSessionHistory(String user_role_id){
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("user_role_id", user_role_id));


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
        try {
            System.out.println("asdfasdf: "+response);
            student_sessions = new JSONArray(response);
            return SUCCESS;
        }catch(Exception e){
            e.printStackTrace();
            return FAILURE;
        }
    }

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

}
