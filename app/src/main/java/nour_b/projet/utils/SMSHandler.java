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
        Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        return contactIntent;
    }

    public static Card contactPicked(Intent data, ContentResolver cr) {
        Cursor cursor;
        Card card = new Card();
        try {
            Uri uri = data.getData();
            cursor = cr.query(uri, null, null, null, null);
            cursor.moveToFirst();
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            card.setName(name);

            String surname = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHONETIC_NAME));
            card.setSurname(surname);

            Cursor phone_cursor = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[] { id }, null);
            String telephones[] = new String[3];
            int i = 0;
            while (phone_cursor.moveToNext()) {
                String phone = phone_cursor.getString(phone_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                telephones[i] = phone;
                i++;
            }
            card.setTel1(telephones[0]);
            card.setTel2(telephones[1]);
            phone_cursor.close();

            Cursor email_cursor = cr.query( ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                            new String[] { id }, null);
            if(email_cursor.moveToNext()) {
                String email = email_cursor.getString(email_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                card.setMail(email);
            }
            email_cursor.close();

            Cursor address_cursor = cr.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null,
                                            ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
                                            new String[] { id }, null);

            if(address_cursor.moveToNext()) {
                String adr = address_cursor.getString(address_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                card.setAddress(adr);
            }
            address_cursor.close();

            String[] cols = { ContactsContract.CommonDataKinds.Website.URL };
            String filter = ContactsContract.Data.CONTACT_ID+" = ? " + " and " + ContactsContract.Data.MIMETYPE
                            + " = '" + ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE + "'";
            String[] params = { String.valueOf(id) };
            Cursor website_cursor = cr.query(ContactsContract.Data.CONTENT_URI, cols, filter, params, null);

            if(website_cursor.moveToNext()) {
                String val = website_cursor.getString(website_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
                card.setWebsite(val);
            }
            website_cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return card;
    }
}