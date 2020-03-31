package kean20sp.capstoneproject.http;

import android.net.Uri;
import android.util.Log;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import kean20sp.capstoneproject.util.AppState;

public class LoginHandler extends HTTPConnectionHandler{
    private String host = null, filepath = null;
    private String session_id = null;
    private String available_roles = null;

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
    public String available_roles() { return available_roles; }

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
        Log.d("loginhandler response","response = "+response);
        if(response==null){
            return "null";
        }
        String[] response_split = response.split(";");
        String code = response_split[0];
        if(code.equals("SL0")){
            AppState.UserInfo.user_role_id = response_split[1]; //move later
            String available_roles = response_split[2];
            String session_id = response.replace(response_split[0]+';'+response_split[1]+';'+response_split[2]+';',"");
            this.session_id = session_id;
            this.available_roles = available_roles;
            return LOGIN_SUCCESSFUL;
        } else if(code.equals("FL0")){
            return EMAIL_NOT_FOUND;
        } else if(code.equals("FL1")){
            return PASSWORD_DOESNT_MATCH;
        } else {
            return UNKNOWN_LOGIN_FAILURE;
        }
    }

    public static final String LOGIN_SUCCESSFUL = "Login Successful";
    public static final String EMAIL_NOT_FOUND = "Email Not Found";
    public static final String PASSWORD_DOESNT_MATCH = "Password Does Not Match";
    public static final String UNKNOWN_LOGIN_FAILURE = "Unknown Login Failure";

}
