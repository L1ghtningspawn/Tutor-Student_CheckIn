package kean20sp.capstoneproject.util;

import java.util.Random;

public class QRUtil {

    //ascii decimal 32-126
    public static String genkey(){
        String tutor_id = AppState.UserInfo.user_role_id;
        String server_id = "";

        Random random = new Random();
        for(int x = 0; x < 16; x++){
            int next = random.nextInt(126-32) + 32;
            server_id += (char)next;
        }

        return tutor_id+"#"+server_id;
    }
}
