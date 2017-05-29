package nour_b.projet.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import nour_b.projet.model.Card;

public class DBRegister {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "card.db";

    private SQLiteDatabase db;
    private DatabaseHelper databaseHelper;

    public DBRegister(Context context) {
        databaseHelper = new DatabaseHelper(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open() {
        db = databaseHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }


    //////// CARD ///////

    public void storeCard (Card u) {
        open();
        if (u != null  ) {
            ContentValues store = new ContentValues();
            store.put(DatabaseHelper.COL_MAIL, u.getMail());
            store.put(DatabaseHelper.COL_PASSWORD, u.getPassword());
            store.put(DatabaseHelper.COL_NAME, u.getName());
            store.put(DatabaseHelper.COL_SURNAME, u.getSurname());
            store.put(DatabaseHelper.COL_ADDRESS, u.getAddress());
            store.put(DatabaseHelper.COL_TEL1, u.getTel1());
            store.put(DatabaseHelper.COL_TEL2, u.getTel2());
            store.put(DatabaseHelper.COL_WEBSITE, u.getWebsite());
            store.put(DatabaseHelper.COL_PHOTO, u.getPhoto());
            db.insert(DatabaseHelper.TABLE_CARD, null, store);
        }
        close();
    }

    public boolean login(String mail, String password){
        open();
        if (mail != null && password != null) {
            Cursor c = db.query (   DatabaseHelper.TABLE_CARD,
                                    new String[] { DatabaseHelper.COL_MAIL, DatabaseHelper.COL_PASSWORD},
                                    DatabaseHelper.COL_MAIL + " = ? AND " + DatabaseHelper.COL_PASSWORD +" = ?",
                                    new String[] {mail, password},
                                    null, null, null);
            if (c.getCount() > 0) {
                c.close();
                close();
                return true;
            }
            c.close();
        }
        close();
        return false;
    }

    public boolean exist(String mail){
        open();
        if (mail != null) {
            Cursor c = db.query (   DatabaseHelper.TABLE_CARD,
                    new String[] { DatabaseHelper.COL_MAIL},
                    DatabaseHelper.COL_MAIL + " = ?",
                    new String[] {mail},
                    null, null, null);
            if (c.getCount() > 0) {
                c.close();
                close();
                return true;
            }
            c.close();
        }
        close();
        return false;
    }

    public Card getCard (String mail) {
        open();
        Cursor c = db.query(    DatabaseHelper.TABLE_CARD,
                                new String[]{   DatabaseHelper.COL_PASSWORD, DatabaseHelper.COL_NAME,
                                                DatabaseHelper.COL_SURNAME, DatabaseHelper.COL_ADDRESS,
                                                DatabaseHelper.COL_TEL1, DatabaseHelper.COL_TEL2,
                                                DatabaseHelper.COL_WEBSITE, DatabaseHelper.COL_PHOTO},
                                DatabaseHelper.COL_MAIL + " = ?",
                                new String[] { mail },
                                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            Card u = new Card(mail, c.getString(0), c.getString(1), c.getString(2));
            u.setAddress(c.getString(3));
            u.setTel1(c.getString(4));
            u.setTel2(c.getString(5));
            u.setWebsite(c.getString(6));
            u.setPhoto(c.getString(7));
            c.close(); // close the cursor
            return u;
        }
        close();
        return null;
    }

    public void updateCard (Card u) {
        open();

        ContentValues updated = new ContentValues();
        updated.put(DatabaseHelper.COL_MAIL, u.getMail());
        updated.put(DatabaseHelper.COL_PASSWORD, u.getPassword());
        updated.put(DatabaseHelper.COL_NAME, u.getName());
        updated.put(DatabaseHelper.COL_SURNAME, u.getSurname());
        updated.put(DatabaseHelper.COL_ADDRESS, u.getAddress());
        updated.put(DatabaseHelper.COL_TEL1, u.getTel1());
        updated.put(DatabaseHelper.COL_TEL2, u.getTel2());
        updated.put(DatabaseHelper.COL_WEBSITE, u.getWebsite());
        if (u.getPhoto() != null) {
            updated.put(DatabaseHelper.COL_PHOTO, u.getPhoto());
        }

        db.update(DatabaseHelper.TABLE_CARD, updated, DatabaseHelper.COL_MAIL + " = '" + u.getMail() + "'", null);

        close();
    }
}