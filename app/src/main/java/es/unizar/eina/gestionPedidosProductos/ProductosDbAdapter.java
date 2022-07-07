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
public class ProductosDbAdapter {

    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PRICE = "price";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_ROWID = "_id";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_TABLE = "products";

    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public ProductosDbAdapter(Context ctx) {
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
    public ProductosDbAdapter open() throws SQLException {
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
     * @param name value to set note title to
     * @param description value to set note body to
     * @param price value to set note title to
     * @param weight value to set note title to
     * @return rowId or -1 if failed
     */
    public long createProduct(String name, String description, float price, float weight) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_PRICE, price);
        initialValues.put(KEY_WEIGHT, weight);


        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteProduct(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllProducts() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_DESCRIPTION, KEY_PRICE, KEY_WEIGHT}, null, null, null, null, KEY_NAME);
    }

    public Cursor fetchByPrice(){
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_DESCRIPTION, KEY_PRICE, KEY_WEIGHT}, null, null, null, null, KEY_PRICE);
    }

    public Cursor fetchByWeight(){
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_DESCRIPTION, KEY_PRICE, KEY_WEIGHT}, null, null, null, null, KEY_WEIGHT);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchProduct(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_DESCRIPTION, KEY_PRICE, KEY_WEIGHT}, KEY_ROWID + "=" + rowId, null,
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
     * @param name value to set note title to
     * @param description value to set note body to
     * @param price value to set note title to
     * @param weight value to set note title to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateProduct(long rowId, String name, String description, float price, float weight) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_DESCRIPTION, description);
        args.put(KEY_PRICE, price);
        args.put(KEY_WEIGHT, weight);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}