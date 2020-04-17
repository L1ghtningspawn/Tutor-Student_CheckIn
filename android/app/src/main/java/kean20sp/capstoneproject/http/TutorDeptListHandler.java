package kean20sp.capstoneproject.http;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class TutorDeptListHandler extends HTTPConnectionHandler {
    private String host = null, filepath = null;
    public String[] departments = null;
    public String[] user_roles = null;

    public TutorDeptListHandler(String host, String filepath){
        this.host = host;
        this.filepath = filepath;
    }
    public TutorDeptListHandler(){
        host = "seesselm-project-page.com";
        filepath = "/Capstone/android/android_tutordeptlist.php";
    }

    public String getDept(int x){
        return departments[x];
    }
    public int size(){
        return departments.length;
    }
    public String getUserRole(int x) {
        return user_roles[x];
    }

    private String response;
    private boolean response_done = false;
    private List<NameValuePair> pairs;
    public String getTutorDeptList(String email, String session_id){
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("tutordeptlist", "true"));
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

        Log.d("hitler","response: "+response);
        return process(response);
    }


    private String process(String response) {
        String[] response_split = response.split(";");
        String code = response_split[0];
        if(code.equals("STu0")){
            String[] depts = response_split[2].split("\\^");
            System.out.println(Arrays.toString(depts));
            departments = depts;

            String[] uroles = response_split[1].split("\\^");
            System.out.println(Arrays.toString(uroles));
            user_roles = uroles;
            return GET_TUTOR_DEPT_LIST_SUCCESS;
        } else if(code.equals("FTu0")){
            return GET_TUTOR_DEPT_LIST_FAILURE;
        } else if(code.equals("STu1")){
            return SESSION_TIMED_OUT;
        }
        else {
            System.out.println("response: "+response);
            return UNKNOWN_FAILURE;
        }
    }

    public static final String GET_TUTOR_DEPT_LIST_SUCCESS = "Get Tutor Department Listings Was Successful";
    public static final String GET_TUTOR_DEPT_LIST_FAILURE = "Get Tutor Department Listings Failed";
    public static final String SESSION_TIMED_OUT = "Session Timed Out";
    public static final String UNKNOWN_FAILURE = "Unknown Failure";
}
