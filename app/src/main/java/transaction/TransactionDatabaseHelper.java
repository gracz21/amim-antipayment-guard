package transaction;

import android.content.Context;

import dbAdapter.DatabaseHelper;

/**
 * @author Kamil Walkowiak
 */
public class TransactionDatabaseHelper extends DatabaseHelper {
    public static final String TABLE_NAME = "transactions";

    public TransactionDatabaseHelper(Context context) {
        super(context);
    }
}
