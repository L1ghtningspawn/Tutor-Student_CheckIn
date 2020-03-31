package kean20sp.capstoneproject.util;

import java.util.regex.Pattern;

public class CheckUserInput {

    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^ *[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@kean.edu *";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email.toLowerCase()).matches();
    }

    public static boolean isValidPassword(String password){
        if(password.equals("")) return false;
        return true;
    }
}
