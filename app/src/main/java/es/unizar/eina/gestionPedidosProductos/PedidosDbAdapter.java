package es.unizar.eina.gestionPedidosProductos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class PedidosDbAdapter {

    public static final String KEY_CLIENT = "client";
    public static final String KEY_NUMBER = "mobile_number";
    public static final String KEY_DATE = "date";
    public static final String KEY_ROWID = "_id";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_TABLE = "orders";

    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public PedidosDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public PedidosDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param client the client associated to the order
     * @param mobile_number the mobile_number of the client
     * @param date the date in which the order should be ready
     * @return rowId or -1 if failed
     */
    public long createOrder(String client, int mobile_number, String date) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CLIENT, client);
        initialValues.put(KEY_NUMBER, mobile_number);
        initialValues.put(KEY_DATE, date);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteOrder(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllOrders() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_CLIENT,
                KEY_NUMBER, KEY_DATE}, null, null, null, null, KEY_CLIENT);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchOrder(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_CLIENT, KEY_NUMBER, KEY_DATE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of note to update
     * @param client the client associated to the order
     * @param mobile_number the mobile_number of the client
     * @param date the date in which the order should be ready
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateOrder(long rowId, String client, int mobile_number, String date) {
        ContentValues args = new ContentValues();
        args.put(KEY_CLIENT, client);
        args.put(KEY_NUMBER, mobile_number);
        args.put(KEY_DATE, date);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
