package es.unizar.eina.gestionPedidosProductos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 6;

    private static final String TAG = "DatabaseHelper";

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE_1 =
            "create table products (_id integer primary key autoincrement, " +
                    "name text not null, description text not null, price float not null, weight float not null);";
    private static final String DATABASE_CREATE_2 = "create table orders (_id integer primary key autoincrement, " +
                    "client text not null, mobile_number int not null, date Date not null);";

    /*private static final String DATABASE_CREATE_3 = "create table tiene (idProduct integer, idOrder integer," numUnidades*/

            //add another table for N:M relationship between products and orders

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE_1);
        db.execSQL(DATABASE_CREATE_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS orders");
        onCreate(db);
    }
}