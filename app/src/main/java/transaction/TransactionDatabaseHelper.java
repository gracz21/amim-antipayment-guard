package transaction;

import android.content.Context;

import databaseHelper.DatabaseHelper;

/**
 * @author Kamil Walkowiak
 */
public class TransactionDatabaseHelper {
    private DatabaseHelper databaseHelper;

    public static final String TABLE_NAME = "transactions";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_PLACE = "place";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PAY_CARD_ID = "payCardId";

    public TransactionDatabaseHelper(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }
}
