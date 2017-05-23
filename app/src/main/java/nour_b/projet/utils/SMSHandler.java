package nour_b.projet.utils;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class SMSHandler {

    public final static int CONTACT_PICKER = -1;
    public final static int PICK_CONTACT_REQUEST = -1;

    public static Intent getContact() {
        Log.d("Send SMS", "");
        Intent contactIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        Log.i("ee","getContact");
        return contactIntent;
    }

    public static String contactPicked(Intent data, ContentResolver cr) {
        Log.i("ee","contactPicked");
        Cursor cur;
        Intent smsIntent = null;
        Log.i("ContactPicked", "");
        String phone = null;
        try {
            // getData() method will have the Content Uri of the selected
            // contact
            Uri uri = data.getData();
            Log.i("le data est ","==>"+uri.getPath());
            // Query the content uri
            cur = cr.query(uri, null, null, null, null);
           cur.moveToFirst();
            // column index of the contact ID
            String id = cur.getString(cur
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // column index of the contact name
            String name = cur.getString(cur
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Log.i("le nom===>", "" + name); // print data
            // column index of the phone number
            Cursor pCur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[] { id }, null);

            while (pCur.moveToNext()) {
                 phone = pCur
                        .getString(pCur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
              //  smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
                Log.i("le phone ===>", "" + phone); // print data
            }
            pCur.close();
            // column index of the email
            Cursor emailCur = cr.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[] { id }, null);

            while (emailCur.moveToNext()) {
                // This would allow you get several email addresses
                // if the email addresses were stored in an array
                String email = emailCur
                        .getString(emailCur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                Log.i("le email ===>", "" + email); // print data
            }
            emailCur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return   phone;
    }
}
