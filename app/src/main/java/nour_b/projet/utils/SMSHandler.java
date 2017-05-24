package nour_b.projet.utils;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import nour_b.projet.model.User;

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

    public  static User contactPicked(Intent data, ContentResolver cr) {

        Log.i("ee","contactPicked");
        Cursor cur;
        Intent smsIntent = null;
        Log.i("ContactPicked", "");
        User user = new User();
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
            user.setName(name);
            while (pCur.moveToNext()) {
                String phone = pCur
                        .getString(pCur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //  smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
                Log.i("le phone ===>", "" + phone); // print data
                user.setTel1(phone);
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
                user.setMail(email);
            }
            emailCur.close();
            // column index of the adresse
            Cursor adrCur = cr.query(
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
                    new String[] { id }, null);
            while (adrCur.moveToNext()) {
                // This would allow you get several addresses
                // if the addresses were stored in an array
                String adr = adrCur
                        .getString(adrCur
                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                Log.i("le email ===>", "" + adr); // print data
                user.setAddress(adr);
            }
            adrCur.close();
            // column index of the webSite
            Cursor cursorWebsite = cr.query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Website.CONTACT_ID + " = ?",
                    new String[] { id },
                    null);
            if (cursorWebsite != null) {
                while (cursorWebsite.moveToNext()) {
                    int i = cursorWebsite.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL);
                    int type = cursorWebsite.getColumnIndex(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                    String s = cursorWebsite.getString(i);
                    switch (type) {
                        case ContactsContract.CommonDataKinds.Website.TYPE_HOME:
                            // s *** private url not working
                            break;
                        case ContactsContract.CommonDataKinds.Website.TYPE_WORK:
                            // s *** business url not working
                            break;
                    }
                }
            }
                cursorWebsite.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return   user;
    }
}
