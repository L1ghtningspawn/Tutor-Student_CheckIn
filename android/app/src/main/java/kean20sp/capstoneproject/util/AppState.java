package kean20sp.capstoneproject.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AppState {

    public static class Session {
        public static String id;
        public static String activity_id;
        public static boolean logout_flag;
    }

    public static class UserInfo {
        public static String email;
        public static String roles;
        public static String user_role_id;
        public static String student_role_id;
    }

    public static class Clock {
        public static String id;
        public static String in_datetime;
        public static String out_datetime;
    }

    public static class TutorSession {
        public static String tutor_id;
        public static String student_id;
        public static String qr_server_key;
        public static String student_email;
        public static String tutor_email;
        public static String in_datetime;
        public static String out_datetime;
        public static String course_id;
    }

    public static class TutorProfile {
        public static String first_name;
        public static String last_name;
        public static String[] courses;
        public static String[] course_ids;
    }


    public static class Debug {
        public static void log_Session(){
            Log.d("AppState.Session","id = "+Session.id);
            Log.d("AppState.Session", "activity_id = "+Session.activity_id);
            Log.d("AppState.Session", "logout_flag = "+Session.logout_flag);
        }
        public static void log_UserInfo(){
            Log.d("AppState.UserInfo", "email = "+UserInfo.email);
            Log.d("AppState.UserInfo","roles = "+UserInfo.roles);
            Log.d("AppState.UserInfo", "user_role_id = "+UserInfo.user_role_id);
        }
        public static void log_Clock(){
            Log.d("AppState.Clock","id = "+Clock.id);
            Log.d("AppState.Clock","in_datetime = "+Clock.in_datetime);
            Log.d("AppState.Clock","out_datetime = "+Clock.out_datetime);
        }
        public static void log_TutorSession(){
            Log.d("AppState.TutorSession","tutor_id = "+TutorSession.tutor_id);
            Log.d("AppState.TutorSession","student_id = "+TutorSession.student_id);
            Log.d("AppState.TutorSession","qr_server_key = "+TutorSession.qr_server_key);
            Log.d("AppState.TutorSession","student_email = "+TutorSession.student_email);
            Log.d("AppState.TutorSession","tutor_email = "+TutorSession.tutor_email);
            Log.d("AppState.TutorSession","in_datetime = "+TutorSession.in_datetime);
            Log.d("AppState.TutorSession","out_datetime = "+TutorSession.out_datetime);
            Log.d("AppState.TutorSession","course_id = "+TutorSession.course_id);
        }
        public static void log_TutorProfile(){
            Log.d("AppState.TutorProfile","first_name = "+TutorProfile.first_name);
            Log.d("AppState.TutorProfile","last_name = "+TutorProfile.last_name);
            Log.d("AppState.TutorProfile","courses = "+Arrays.toString(TutorProfile.courses));
            Log.d("AppState.TutorProfile","course_ids = "+Arrays.toString(TutorProfile.course_ids));
        }
        public static void log_All(){
            log_Session();
            log_UserInfo();
            log_Clock();
            log_TutorSession();
            log_TutorProfile();
        }
    }

    public static class IO {
        public static String config_file;

        public static boolean readFrom(Context c, String filename){
            try{
                FileInputStream fis = c.openFileInput(filename);
                InputStreamReader reader = new InputStreamReader(fis);
                StringBuilder builder = new StringBuilder();
                try{
                    BufferedReader breader = new BufferedReader(reader);
                    String line = breader.readLine();
                    while(line != null){
                        builder.append(line).append("\n");
                        line = breader.readLine();
                    }

                }catch(Exception e){
                    e.printStackTrace();
                    return false;
                } finally {
                    String contents = builder.toString();
                    JSONObject config = new JSONObject(contents);
                    setupConfig(config);
                    return true;
                }


            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
        }
        public static void writeTo(Context c, String filename){
            try{
                String contents = configJSON();
                try(FileOutputStream fos = c.openFileOutput(filename, Context.MODE_PRIVATE)){
                 fos.write(contents.getBytes());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public static void read(Context context){ readFrom(context,config_file); }
        public static void write(Context context){ writeTo(context,config_file); }


        private static void setupConfig(JSONObject config){
            try{
                JSONObject session = config.getJSONObject("Session");
                Session.activity_id = session.getString("activity_id");
                Session.id = session.getString("id");
                //Session.logout_flag = session.getBoolean("logout_flag");

                JSONObject userinfo = config.getJSONObject("UserInfo");
                UserInfo.email = userinfo.getString("email");
                UserInfo.roles = userinfo.getString("roles");
                UserInfo.user_role_id = userinfo.getString("user_role_id");
                UserInfo.student_role_id = userinfo.getString("student_role_id");

                JSONObject clock = config.getJSONObject("Clock");
                Clock.id = clock.getString("id");
                Clock.in_datetime = clock.getString("in_datetime");
                Clock.out_datetime = clock.getString("out_datetime");

                JSONObject tutorSession = config.getJSONObject("TutorSession");
                TutorSession.tutor_id = tutorSession.getString("tutor_id");
                TutorSession.student_id = tutorSession.getString("student_id");
                TutorSession.qr_server_key = tutorSession.getString("qr_server_key");
                TutorSession.student_email = tutorSession.getString("student_email");
                TutorSession.tutor_email = tutorSession.getString("tutor_email");
                TutorSession.in_datetime = tutorSession.getString("in_datetime");
                TutorSession.out_datetime = tutorSession.getString("out_datetime");
                TutorSession.course_id = tutorSession.getString("course_id");

                JSONObject tutorProfile = config.getJSONObject("TutorProfile");
                JSONArray coursesJSON = tutorProfile.getJSONArray("courses");
                JSONArray course_ids = tutorProfile.getJSONArray("course_ids");
                TutorProfile.first_name = tutorProfile.getString("first_name");
                TutorProfile.last_name = tutorProfile.getString("last_name");
                TutorProfile.courses = toStringArray(coursesJSON);
                TutorProfile.course_ids = toStringArray(course_ids);

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        private static String[] toStringArray(JSONArray json) throws Exception{
            if(json == null) return null;
            String[] array = new String[json.length()];
            for(int x = 0; x < json.length(); x++){
                array[x] = json.getString(x);
            }
            return array;
        }

        private static String configJSON() throws Exception{
            JSONObject config = new JSONObject();

            JSONObject session = new JSONObject();
            session.put("id",Session.id);
            session.put("activity_id",Session.activity_id);
            //session.put("logout_flag",Session.logout_flag);
            config.put("Session",session);

            JSONObject userinfo = new JSONObject();
            userinfo.put("email",UserInfo.email);
            userinfo.put("roles",UserInfo.roles);
            userinfo.put("user_role_id",UserInfo.user_role_id);
            userinfo.put("student_role_id",UserInfo.student_role_id);
            config.put("UserInfo",userinfo);

            JSONObject clock = new JSONObject();
            clock.put("id",Clock.id);
            clock.put("in_datetime",Clock.in_datetime);
            clock.put("out_datetime", Clock.out_datetime);
            config.put("Clock",clock);

            JSONObject tutorSession = new JSONObject();
            tutorSession.put("tutor_id",TutorSession.tutor_id);
            tutorSession.put("student_id",TutorSession.student_id);
            tutorSession.put("qr_server_key", TutorSession.qr_server_key);
            tutorSession.put("student_email",TutorSession.student_email);
            tutorSession.put("tutor_email", TutorSession.student_email);
            tutorSession.put("in_datetime", TutorSession.in_datetime);
            tutorSession.put("out_datetime", TutorSession.out_datetime);
            tutorSession.put("course_id", TutorSession.course_id);
            config.put("TutorSession",tutorSession);

            JSONObject tutorProfile = new JSONObject();
            tutorProfile.put("first_name",TutorProfile.first_name);
            tutorProfile.put("last_name",TutorProfile.last_name);
            tutorProfile.put("courses", toJSONArray(TutorProfile.courses));
            tutorProfile.put("course_ids", toJSONArray(TutorProfile.course_ids));
            config.put("TutorProfile",tutorProfile);

            return config.toString();
        }

        public static JSONArray toJSONArray(String[] array){
            if(array == null) return null;
            JSONArray json = new JSONArray();
            for(int x = 0; x < array.length; x++){
                json.put(array[x]);
            }
            return json;
        }
    }

}