package nour_b.projet.localDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_USER = "table_USER";
    public static final String COL_ID ="USER_id";
    public static final String COL_MAIL ="USER_mail";
    public static final String COL_PASSWORD ="USER_password";
    public static final String COL_NAME ="USER_name";
    public static final String COL_SURNAME ="USER_surname";
    public static final String COL_BIRTH ="USER_birth";
    public static final String COL_ADDRESS = "USER_address";
    public static final String COL_TEL1 = "USER_tel1";
    public static final String COL_TEL2 = "USER_tel2";
    public static final String COL_WEBSITE = "USER_website";
    public static final String COL_PHOTO = "USER_photo";



    private final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
                                            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                            + COL_MAIL + " TEXT UNIQUE, "
                                            + COL_PASSWORD + " TEXT NOT NULL, "
                                            + COL_NAME + " TEXT NOT NULL, "
                                            + COL_SURNAME + " TEXT NOT NULL,"
                                            + COL_BIRTH + " TEXT,"
                                            + COL_ADDRESS + " TEXT,"
                                            + COL_TEL1 + "  TEXT,"
                                            + COL_TEL2 + "  TEXT,"
                                            + COL_WEBSITE + "  TEXT,"
                                            + COL_PHOTO + "  TEXT)";


    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_USER + ";");
        onCreate(db);
    }

}
