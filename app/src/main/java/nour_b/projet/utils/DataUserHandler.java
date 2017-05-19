package nour_b.projet.utils;

import android.util.Patterns;

public class DataUserHandler {

    /////////////////////////////
    /// VALIDATION DES CHAMPS ///
    ////////////////////////////

    public static boolean textValidation(String s){
        if (s.length() > 2 && s.length() < 50) {
            return true;
        }
        return false;
    }

    public static boolean telValidation(String s){
        return Patterns.PHONE.matcher(s).matches();
    }

    public static boolean birthValidation(String s) {
        if (s.length() == 10) {
            System.out.println(s.charAt(2));
            System.out.println(s.charAt(5));
            boolean slash = Character.toString(s.charAt(2)).equals("/") && Character.toString(s.charAt(5)).equals("/");
            if(slash)
                return true;
        }
        return false;
    }

    public static boolean mailValidation(String s1, String s2) {
        if(s1.equals(s2)) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(s1).matches();
        }
        return false;
    }


    public static boolean passwordValidation(String s1, String s2) {
        if(s1.length() >= 6 && s1.equals(s2)) {
            return true;
        }
        return false;
    }

    public static boolean registerOk(boolean [] t) {
        for (int i = 0; i < t.length ; i++) {
            if(t[i] == false) {
                return false;
            }
        }
        return true;
    }

    /////////////////////////////
    /// GESTION DU EDIT      ///
    ////////////////////////////

    public static void storeCheckedBox() {

    }

}
