package pl.poznan.put.fc.antipaymentGuard.databaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.poznan.put.fc.antipaymentGuard.models.condition.AmountCondition;
import pl.poznan.put.fc.antipaymentGuard.models.condition.Condition;
import pl.poznan.put.fc.antipaymentGuard.models.condition.ConditionDatabaseHelper;
import pl.poznan.put.fc.antipaymentGuard.models.condition.NumberCondition;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;

/**
 * @author Kamil Walkowiak
 */
public class PayCardDatabaseHelper {
    private static final String LOG_TAG = PayCardDatabaseHelper.class.getSimpleName();

    private DatabaseHelper databaseHelper;

    public static final String TABLE_NAME = "payCards";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NO = "no";
    public static final String COLUMN_BANK_NAME = "bankName";
    public static final String COLUMN_BALANCE = "balance";
    public static final String COLUMN_EXPIRATION_DATE = "expirationDate";
    public static final String COLUMN_CONDITION_ID = "conditionId";

    public PayCardDatabaseHelper(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public void createPayCard(PayCard payCard) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy", Locale.US);

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, payCard.getName());
        values.put(COLUMN_NO, payCard.getNo());
        values.put(COLUMN_BANK_NAME, payCard.getBankName());
        values.put(COLUMN_BALANCE, payCard.getBalance());
        values.put(COLUMN_EXPIRATION_DATE, dateFormat.format(payCard.getExpirationDate()));
        values.put(COLUMN_CONDITION_ID, payCard.getCondition().getId());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<PayCard> getAllPayCards() {
        List<PayCard> payCards = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy", Locale.US);

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " a JOIN " + ConditionDatabaseHelper.TABLE_NAME + " b ON a." +
                COLUMN_CONDITION_ID + " = b." + ConditionDatabaseHelper.COLUMN_ID;
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String no = cursor.getString(2);
                String bankName = cursor.getString(3);
                Double balance = Double.parseDouble(cursor.getString(4));
                Date expirationDate = null;
                try {
                    expirationDate = dateFormat.parse(cursor.getString(5));
                } catch (ParseException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }

                Condition condition;
                if(cursor.getString(10).equals(AmountCondition.class.getSimpleName())) {
                    condition = new AmountCondition(cursor.getLong(7), cursor.getDouble(8));
                } else {
                    condition = new NumberCondition(cursor.getLong(7), cursor.getInt(9));
                }
                PayCard payCard = new PayCard(name, no, bankName, balance, expirationDate, condition);
                payCards.add(payCard);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return payCards;
    }

    public boolean deletePayCard(String no) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();

        return db.delete(TABLE_NAME, COLUMN_NO + "=?", new String[]{no}) > 0;
    }
}