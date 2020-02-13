package kean20sp.capstoneproject.http;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Hashtable;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class SignupHandler extends HTTPConnectionHandler {
    private String host = null;
    private String filepath = null;
    private String session_id = null;
    private ArrayList<NameValuePair> pairs;

    public SignupHandler(){
        host = "seesselm-project-page.com";
        filepath = "/Capstone/android/android_signup.php";
    }
    public SignupHandler(String host, String filepath){
        this.host = host;
        this.filepath = filepath;
    }

    public String session_id(){
        return session_id;
    }

    private String response;
    private boolean response_done = false;
    public String signup(String email, String password, String fname, String lname, String year){

        pairs = new ArrayList<NameValuePair>();

        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("email",email));
        pairs.add(new BasicNameValuePair("pwd",password));
        pairs.add(new BasicNameValuePair("fname",fname));
        pairs.add(new BasicNameValuePair("lname",lname));
        pairs.add(new BasicNameValuePair("orgyear",year));
        pairs.add(new BasicNameValuePair("signup","true"));

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
        String code = response.substring(0,3);
        if(code.equals("SS0")){
            String session_id = response.substring(3,response.length());
            this.session_id = session_id;
            return "Signup Successful";
        } else if(code.equals("FSO")){
            return "Email Belongs to Existing User";
        } else if(code.equals("FS1")){
            return "Unexepected Database Failure";
        } else {
            return "Unknown Signup Failure";
        }
    }

}
