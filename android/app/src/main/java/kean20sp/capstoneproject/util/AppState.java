package kean20sp.capstoneproject.util;

import android.util.Log;

import java.util.Arrays;

public class AppState {

    public static class Session {
        public static String id;
    }

    public static class UserInfo {
        public static String email;
        public static String roles;
        public static String user_role_id;
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

}