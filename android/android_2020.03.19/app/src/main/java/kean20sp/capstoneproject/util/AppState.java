package kean20sp.capstoneproject.util;

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

}