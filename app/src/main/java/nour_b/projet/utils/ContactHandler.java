package nour_b.projet.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.content.BroadcastReceiver;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import nour_b.projet.PersonalCardActivity;
import nour_b.projet.model.Card;

public class ContactHandler extends BroadcastReceiver {

    public final static int CONTACT_PICKER = -1;
    public final static int PICK_CONTACT_REQUEST = -1;
    private PersonalCardActivity per;
    public ContactHandler(PersonalCardActivity per) {
        this.per = per;
    }
    public static Intent getContact() {
        Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        return contactIntent;
    }

    public static Card contactPicked(Context ctxt,Intent data) {
        Cursor cursor;
        Card card = new Card();
        try {
            Uri uri = data.getData();
            cursor =  ctxt.getContentResolver().query(uri, null, null, null, null);
            while(cursor.moveToNext()){

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                card.setName(name);
                String surname = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHONETIC_NAME));
                card.setSurname(surname);
                cursor.close();
                Cursor phone_cursor =  ctxt.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[] { id }, null);
                String telephones[] = new String[3];
                int i = 0;
                while (phone_cursor.moveToNext()) {
                    String phone = phone_cursor.getString(phone_cursor.getColumnIndex(ContactsContract.
                            CommonDataKinds.Phone.NUMBER));
                    String test = phone_cursor.getString(phone_cursor.getColumnIndex(ContactsContract.
                            CommonDataKinds.Phone.RAW_CONTACT_ID));
                    if( test.equals(id)) {
                        telephones[i] = phone;
                        i++;
                    }
                }
                card.setTel1(telephones[0]);
                card.setTel2(telephones[1]);
                phone_cursor.close();

                Cursor email_cursor =  ctxt.getContentResolver().query( ContactsContract.CommonDataKinds.Email.
                                CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[] { id }, null);
                if(email_cursor.moveToNext()) {
                    String email = email_cursor.getString(email_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.
                            ADDRESS));
                    card.setMail(email);
                }
                email_cursor.close();

                Cursor address_cursor =  ctxt.getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.
                                CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
                        new String[] { id }, null);

                if(address_cursor.moveToNext()) {
                    String adr = address_cursor.getString(address_cursor.getColumnIndex(ContactsContract.CommonDataKinds.
                            StructuredPostal.STREET));
                    card.setAddress(adr);
                }
                address_cursor.close();

                String[] cols = { ContactsContract.CommonDataKinds.Website.URL };
                String filter = ContactsContract.Data.CONTACT_ID+" = ? " + " and " + ContactsContract.Data.MIMETYPE
                        + " = '" + ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE + "'";
                String[] params = { String.valueOf(id) };
                Cursor website_cursor =  ctxt.getContentResolver().query(ContactsContract.Data.CONTENT_URI, cols,
                        filter, params, null);

                if(website_cursor.moveToNext()) {
                    String val = website_cursor.getString(website_cursor.getColumnIndex(ContactsContract.CommonDataKinds.
                            Website.URL));
                    card.setWebsite(val);
                }
                website_cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return card;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        assert(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
        SmsMessage [] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for(SmsMessage message : messages) {
            String body = message.getMessageBody();
            if (body == null || !body.startsWith(per.getExpectedPrefix())) {
                continue;
            }
            String from = message.getOriginatingAddress();
            if (! per.accept(from)) {
                continue;
            }
            per.sms(from, body);
        }

    }
}