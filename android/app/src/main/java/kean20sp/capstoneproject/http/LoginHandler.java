package kean20sp.capstoneproject.http;

import android.net.Uri;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class LoginHandler extends HTTPConnectionHandler{
    private String host = null, filepath = null;
    private String session_id = null;

    public LoginHandler(String host, String filepath){
        this.host = host;
        this.filepath = filepath;
    }
    public LoginHandler(){
        host = "seesselm-project-page.com";
        filepath = "/Capstone/android/android_login.php";

    }

    public String session_id(){
        return session_id;
    }

    private String response;
    private boolean response_done = false;
    private List<NameValuePair> pairs;
    public String login(String email, String password){
        pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("login", "true"));
        pairs.add(new BasicNameValuePair("email", email));
        pairs.add(new BasicNameValuePair("password", password));

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
        if(response==null){
            return "null";
        }
        String code = response.substring(0,3);
        if(code.equals("SL0")){
            String session_id = response.substring(3,response.length());
            this.session_id = session_id;
            return "Login Successful";
        } else if(code.equals("FL0")){
            return "Email Not Found";
        } else if(code.equals("FL1")){
            return "Password Does Not Match";
        } else {
            return "Unknown Login Failure";
        }
    }

}
