package nour_b.projet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class DataCardHandler {

    /////////////////////////////
    /// VALIDATION DES CHAMPS ///
    ////////////////////////////

    public static boolean textValidationMandatory(String s){
        if (s.length() > 2 && s.length() < 50) {
            return true;
        }
        return false;
    }

    public static boolean textValidation(String s){
        if (s.equals("") || ( s.length() > 2 && s.length() < 50 ) ) {
            return true;
        }
        return false;
    }

    public static boolean telValidation(String s){
        if(s.equals("")) {
            return true;
        }else {
            return Patterns.PHONE.matcher(s).matches();
        }
    }

    public static boolean mailValidation(String s1, String s2) {
        if(s1.equals("") && s1.equals(s2)) {
            return true;
        } else if(s1.equals(s2)) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(s1).matches();
        }
        return false;
    }


    public static boolean passwordValidation(String s1, String s2) {
        if( ( s1.equals("") && s1.equals(s2) ) || ( s1.length() >= 6 && s1.equals(s2) ) ) {
            return true;
        }
        return false;
    }

    public static boolean registerOk(boolean [] t) {
        for (int i = 0; i < 4 ; i++) {
            if(t[i] == false) {
                return false;
            }
        }
        return true;
    }

    /////////////////////////////
    /// GESTION DU EDIT      ///
    ////////////////////////////

    public static SharedPreferences sharedPreferences;
    private static final String PREFS = "PREFS";
    private static final String PREFS_ADDRESS = "PREFS_ADDRESS";
    private static final String PREFS_TEL1 = "PREFS_TEL1";
    private static final String PREFS_TEL2 = "PREFS_TEL2";
    private static final String PREFS_MAIL = "PREFS_MAIL";
    private static final String PREFS_SITE = "PREFS_SITE";

    public static void setStateCheckedBox(Context ctxt, CheckBox checkBox_address, CheckBox checkBox_tel1, CheckBox checkBox_tel2, CheckBox checkBox_mail, CheckBox checkBox_site) {
        sharedPreferences = ctxt.getSharedPreferences(PREFS, MODE_PRIVATE);
        if(sharedPreferences.contains(PREFS_ADDRESS)) {
            checkBox_address.setChecked(sharedPreferences.getBoolean(PREFS_ADDRESS, false));
        }
        if(sharedPreferences.contains(PREFS_TEL1)) {
            checkBox_tel1.setChecked(sharedPreferences.getBoolean(PREFS_TEL1, false));
        }
        if(sharedPreferences.contains(PREFS_TEL2)) {
            checkBox_tel2.setChecked(sharedPreferences.getBoolean(PREFS_TEL2, false));
        }
        if(sharedPreferences.contains(PREFS_MAIL)) {
            checkBox_mail.setChecked(sharedPreferences.getBoolean(PREFS_MAIL, false));
        }
        if(sharedPreferences.contains(PREFS_SITE)) {
            checkBox_site.setChecked(sharedPreferences.getBoolean(PREFS_SITE, false));
        }
    }

    public static void storeStateCheckedBox(Context ctxt, CheckBox checkBox_address, CheckBox checkBox_tel1, CheckBox checkBox_tel2, CheckBox checkBox_mail, CheckBox checkBox_site) {
        sharedPreferences = ctxt.getSharedPreferences(PREFS, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(PREFS_ADDRESS, checkBox_address.isChecked())
                                .putBoolean(PREFS_TEL1, checkBox_tel1.isChecked())
                                .putBoolean(PREFS_TEL2, checkBox_tel2.isChecked())
                                .putBoolean(PREFS_MAIL, checkBox_mail.isChecked())
                                .putBoolean(PREFS_SITE, checkBox_site.isChecked())
                                .apply();
    }

    public static void setTextViewPersonalCard(Context ctxt, TextView address, TextView tel1, TextView tel2, TextView mail, TextView site) {
        sharedPreferences = ctxt.getSharedPreferences(PREFS, MODE_PRIVATE);
        if(sharedPreferences.contains(PREFS_ADDRESS) && sharedPreferences.getBoolean(PREFS_ADDRESS, false) == false) {
            address.setVisibility(View.INVISIBLE);
        }
        if(sharedPreferences.contains(PREFS_TEL1) && sharedPreferences.getBoolean(PREFS_TEL1, false) == false) {
            tel1.setVisibility(View.INVISIBLE);
        }
        if(sharedPreferences.contains(PREFS_TEL2) && sharedPreferences.getBoolean(PREFS_TEL2, false) == false) {
            tel2.setVisibility(View.INVISIBLE);
        }
        if(sharedPreferences.contains(PREFS_MAIL) && sharedPreferences.getBoolean(PREFS_MAIL, false) == false) {
            mail.setVisibility(View.INVISIBLE);
        }
        if(sharedPreferences.contains(PREFS_SITE) && sharedPreferences.getBoolean(PREFS_SITE, false) == false) {
            site.setVisibility(View.INVISIBLE);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    /// PASSAGE D'UN STRING A UN TABLEAU DES ELEMENTS D'UNE CARTE ///
    //////////////////////////////////////////////////////////////////////////////////////

    public static String[] splitStringToCard(String s) {
        String [] array_str_split = s.split("\n");

        String [] array_card = new String[7];
        array_card[0] = array_str_split[1];
        array_card[1] = array_str_split[2];
        array_card[2] = array_str_split[3];
        String [] array_address = array_str_split[4].split(":");
        array_card[3] = array_address[1];
        String [] array_tel1 = array_str_split[5].split(":");
        array_card[4] = array_tel1[1];
        String [] array_tel2 = array_str_split[6].split(":");
        array_card[5] = array_tel2[1];
        String [] array_site = array_str_split[7].split(":");
        array_card[6] = array_site[1];

        return array_card;
    }
}
