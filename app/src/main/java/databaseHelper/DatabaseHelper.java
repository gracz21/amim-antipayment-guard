package databaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import payCard.PayCardDatabaseHelper;
import transaction.TransactionDatabaseHelper;

/**
 * @author Kamil Walkowiak
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "antiPaymentGuard.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_PAY_CARDS = "CREATE TABLE " + PayCardDatabaseHelper.TABLE_NAME + "(" +
            PayCardDatabaseHelper.COLUMN_ID + " INTEGER PRIMARY KEY, " +
            PayCardDatabaseHelper.COLUMN_NAME + " TEXT NOT NULL, " +
            PayCardDatabaseHelper.COLUMN_NO + " TEXT NOT NULL, " +
            PayCardDatabaseHelper.COLUMN_BALANCE + " FLOAT NOT NULL)";
    private static final String CREATE_TABLE_TRANSACTIONS = "";
    private static final String CREATE_TABLE_CONDITIONS = "";

    private static final String DROP_TABLE_PAY_CARDS = "DROP TABLE IF EXISTS " + PayCardDatabaseHelper.TABLE_NAME;
    private static final String DROP_TABLE_TRANSACTIONS = "DROP TABLE IF EXISTS " + TransactionDatabaseHelper.TABLE_NAME;
    //private static final String DROP_TABLE_CONDITIONS = "DROP TABLE IF EXISTS " + TABLE_CONDITIONS;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PAY_CARDS);
        //db.execSQL(CREATE_TABLE_TRANSACTIONS);
        //db.execSQL(CREATE_TABLE_CONDITIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL(DROP_TABLE_PAY_CARDS);
            //db.execSQL(DROP_TABLE_TRANSACTIONS);
            //db.execSQL(DROP_TABLE_CONDITIONS);
        }

        onCreate(db);
    }
}
