package nour_b.projet.utils;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nour_b.projet.model.Card;

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

    public  static Card contactPicked(Intent data, ContentResolver cr) {

        Log.i("ee","contactPicked");
        Cursor cur;
        Intent smsIntent = null;
        Log.i("ContactPicked", "");
        Card card = new Card();
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
            card.setName(name);
            // column index of the contact name
            String surname = cur.getString(cur
                    .getColumnIndex(ContactsContract.Contacts.PHONETIC_NAME));
            Log.i("le nom===>", "" + name); // print data
            card.setSurname(surname);
            // column index of the phone number
            Cursor pCur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[] { id }, null);

            while (pCur.moveToNext()) {
                String phone = pCur
                        .getString(pCur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //  smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
                Log.i("le phone ===>", "" + phone); // print data
                card.setTel1(phone);
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
                card.setMail(email);
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
                Log.i("l'adresse  ===>", "" + adr); // print data
                card.setAddress(adr);
            }
            adrCur.close();
            // column index of the webSite
            String[] cols = { ContactsContract.CommonDataKinds.Website.URL };
            String filter = ContactsContract.Data.CONTACT_ID+" = ? " +
                    " and "+ContactsContract.Data.MIMETYPE
                    +" = '"+ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE+"'";
            String[] params = { String.valueOf(id) };
            Cursor website = cr.query(ContactsContract.Data.CONTENT_URI, cols, filter, params, null);

            while (website.moveToNext()) {
                String val = website.getString(website.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
              Log.i("le webSite est ==>",val);
                card.setWebsite(val);
            }

            Date date = null;
            String whereName = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.Event.TYPE + " = ?";
            String[] whereNameParams = new String[] { id, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, String.valueOf(ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY) };
            Cursor c = null;
            try {
                c = cr.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, null);
                if (c != null && c.moveToFirst()) {
                    int indexBirthday = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Event.START_DATE);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    date = format.parse(c.getString(indexBirthday));
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
            if(date != null) {
               // card.setBirth(date.toString());
            }
            if(c != null){
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return card;
    }
}
