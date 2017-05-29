package nour_b.projet.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;

import nour_b.projet.R;

public class ErrorMessages {

    public static HashMap<Integer, String> mapValidationMsg;

    public static void printErrorValidation(Context ctxt, boolean [] t){

        if(mapValidationMsg == null) {
            setMapValidationMsg(ctxt);
        }

        String msg = "";
        for (int i = 0; i < t.length; ++i) {
            if(!t[i]){
                msg += mapValidationMsg.get(i) + "\n";
            }
        }

        Toast.makeText(ctxt, msg, Toast.LENGTH_LONG).show();
    }

    public static void pbLogin(Context ctxt) {
        Toast.makeText(ctxt, R.string.pbLogin, Toast.LENGTH_LONG).show();//
    }

    public static void pbConnexion(Context ctxt) {
        Toast.makeText(ctxt, R.string.pbConnection, Toast.LENGTH_LONG).show();
    }

    public static void pbGeolocalisation(Context ctxt) {
        Toast.makeText(ctxt, R.string.pbGeolocalisation, Toast.LENGTH_LONG).show();
    }

    private static void setMapValidationMsg(Context ctxt){
        mapValidationMsg = new HashMap<>();
        mapValidationMsg.put(0, ctxt.getString(R.string.pbName));
        mapValidationMsg.put(1, ctxt.getString(R.string.pbSurname));
        mapValidationMsg.put(2, ctxt.getString(R.string.pbMail));
        mapValidationMsg.put(3, ctxt.getString(R.string.pbPassword));
        mapValidationMsg.put(4, ctxt.getString(R.string.pbAddress));
        mapValidationMsg.put(5, ctxt.getString(R.string.pbTel));
        mapValidationMsg.put(6, ctxt.getString(R.string.pbTel));
        mapValidationMsg.put(7, ctxt.getString(R.string.pbSite));
    }
}
