package payCard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import databaseHelper.DatabaseHelper;

/**
 * @author Kamil Walkowiak
 */
public class PayCardDatabaseHelper {
    private DatabaseHelper databaseHelper;

    public static final String TABLE_NAME = "payCards";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NO = "no";
    public static final String COLUMN_BALANCE = "balance";

    public PayCardDatabaseHelper(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public void createPayCard(PayCard payCard) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, payCard.getName());
        values.put(COLUMN_NO, payCard.getNo());
        values.put(COLUMN_BALANCE, payCard.getBalance());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<PayCard> getAllPayCards() {
        List<PayCard> payCards = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                PayCard payCard = new PayCard();
                payCard.setName(cursor.getString(1));
                payCard.setNo(cursor.getString(2));
                payCard.setBalance(Float.parseFloat(cursor.getString(3)));
                payCards.add(payCard);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return payCards;
    }
}
