package kean20sp.capstoneproject.util;
import kean20sp.capstoneproject.http.HTTPConnectionHandler;

import java.util.ArrayList;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class GetTutorInfo extends HTTPConnectionHandler {
    private static String response;
    private static boolean response_done = false;

    private static String host = "seesselm-project-page.com";
    private static String filepath = "/Capstone/android/android_getuserinfo.php";

    public static String get_email(String email) {

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("get_type", "tutor_email"));
        pairs.add(new BasicNameValuePair("email", email));

        return process(pairs);
    }

    //TODO: Add more functions here for whatever retrieval is needed.. i hardcoded tutor_email above but could be dynamic if we need student_email as well.

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

            if(code.equals("SGE0")) {
                String tutor_email = response_split[1];
                return tutor_email;
            }
            else if(code.equals("FGE0")) {
                return NO_ACTIVE_SESSION;
            }
            else{
                return UNKNOWN_ERROR;
            }
        }

        private static final String RETRIEVAL_SUCCESSFUL = "Info retrieval was successful.";
        private static final String NO_ACTIVE_SESSION = "No active session for info retrieval.";
        private static final String UNKNOWN_ERROR = "Unknown Error. Try again.";
}