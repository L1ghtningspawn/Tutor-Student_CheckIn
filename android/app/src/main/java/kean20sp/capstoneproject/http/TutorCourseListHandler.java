package kean20sp.capstoneproject.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class TutorCourseListHandler extends HTTPConnectionHandler {
    private String host = null, filepath = null;
    public String[] courses = null;
    public String[] course_ids = null;

    public TutorCourseListHandler(String host, String filepath){
        this.host = host;
        this.filepath = filepath;
    }
    public TutorCourseListHandler(){
        host = "seesselm-project-page.com";
        filepath = "/Capstone/android/android_tutorcourselist.php";
    }

    public String getCourse(int x){
        return courses[x];
    }
    public int size(){
        return courses.length;
    }
    public String getCourseID(int x) {
        return course_ids[x];
    }

    private String response;
    private boolean response_done = false;
    private List<NameValuePair> pairs;
    public String getTutorCourseList(String tutor_role_id){
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("t_ur_id",tutor_role_id));

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
                }
                response_done = true;
                System.out.println("response: "+response);
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
            return "NULL Response";
        }
        String[] response_split = response.split(";");
        String code = response_split[0];

        if(code.equals("STc0")){
            course_ids = response_split[1].split("\\^");
            courses = response_split[2].split("\\^");
            System.out.println(response);
            return (SUCCESSFUL);
        } else if(code.equals("STc1")){
            return (SESSION_EXPIRED);
        } else if(code.equals("FTc0")){
            return (DB_FAILURE);
        } else {
            return UK_FAILURE;
        }
    }

    public static final String SUCCESSFUL = "SUCCESS";
    public static final String SESSION_EXPIRED = "SESSION EXPIRED";
    public static final String DB_FAILURE = "DATABASE FAILURE";
    public static final String UK_FAILURE = "UNKNOWN FAILURE";
}
