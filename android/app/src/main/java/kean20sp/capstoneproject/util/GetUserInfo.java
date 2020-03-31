package kean20sp.capstoneproject.util;
import kean20sp.capstoneproject.http.HTTPConnectionHandler;

import java.util.ArrayList;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class GetUserInfo extends HTTPConnectionHandler {
    private static String response;
    private static boolean response_done = false;

    private static String host = "seesselm-project-page.com";
    private static String filepath = "/Capstone/android/android_getuserinfo.php";

    public static String get_email(String user_role_id) {

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("get_type", "email"));
        pairs.add(new BasicNameValuePair("user_role_id", user_role_id));

        return process(pairs);
    }

    public static String get_name(String user_role_id) {

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("get_type", "name"));
        pairs.add(new BasicNameValuePair("user_role_id", user_role_id));

        return process(pairs);
    }

    public static String get_student_id(String student_email) {

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("get_type", "student_role_id"));
        pairs.add(new BasicNameValuePair("email", student_email));

        return process(pairs);
    }

        private static String process(final ArrayList<NameValuePair> pairs){

            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        response = makeRequest(host, filepath, pairs);
                    } catch (Exception e) {
                        System.out.println(e);
                    } finally {
                        response_done = true;
                        System.out.println(response);
                    }
                }
            });

            t.start();

            long tlim = 1000 * 2;
            long start = System.currentTimeMillis();
            while (!response_done & (System.currentTimeMillis() - start) <= tlim) {
            }
            response_done = false;

            if (response == null) {
                return "null";
            }
            String[] response_split = response.split(";");
            String code = response_split[0];

            switch (code) {
                case "SGE0":
                case "SGN0":
                case "SGID0":
                    return response_split[1];
                case "FGE0":
                    return EMAIL_RETRIEVAL_FAILED;
                case "FGN0":
                    return NAME_RETRIEVAL_FAILED;
                case "FGID0":
                    return UR_ID_RETRIEVAL_FAILED;
                default:
                    return UNKNOWN_ERROR;
            }
        }

        private static final String EMAIL_RETRIEVAL_FAILED = "Email retrieval was unsuccessful.";
        private static final String NAME_RETRIEVAL_FAILED = "Name retrieval was unsuccessful.";
        private static final String UR_ID_RETRIEVAL_FAILED= "User role id retrieval was unsuccessful.";
        private static final String UNKNOWN_ERROR = "Unknown Error. Try again.";
}