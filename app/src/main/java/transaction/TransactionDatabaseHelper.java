package transaction;

import android.content.Context;

import databaseHelper.DatabaseHelper;

/**
 * @author Kamil Walkowiak
 */
public class TransactionDatabaseHelper {
    private DatabaseHelper databaseHelper;

    public static final String TABLE_NAME = "transactions";

    public TransactionDatabaseHelper(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }
}
