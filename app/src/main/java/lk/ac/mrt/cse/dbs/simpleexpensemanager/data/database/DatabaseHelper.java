package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.ExpenseManagerContract.AccountEntry;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.ExpenseManagerContract.TransactionEntry;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExpenseManager.db";
    private static DatabaseHelper databaseHelper;

    private DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void createDatabaseHelperInstance(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static DatabaseHelper getDatabaseHelperInstance() {
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + AccountEntry.TABLE_NAME +
                        " ( " + AccountEntry._ID + " INTEGER PRIMARY KEY, " +
                        AccountEntry.COLUMN_ACCOUNT_NO + " TEXT, " +
                        AccountEntry.COLUMN_ACCOUNT_HOLDER_NAME + " TEXT, " +
                        AccountEntry.COLUMN_BANK_NAME + " TEXT, " +
                        AccountEntry.COLUMN_BALANCE + " REAL);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TransactionEntry.TABLE_NAME +
                " ( " + TransactionEntry._ID + " INTEGER PRIMARY KEY, " +
                TransactionEntry.COLUMN_ACCOUNT_NO + " TEXT, " +
                TransactionEntry.COLUMN_DATE + " TEXT, " +
                TransactionEntry.COLUMN_EXPENSE_TYPE + " TEXT, " +
                TransactionEntry.COLUMN_AMOUNT + " REAL," +
                " FOREIGN KEY(" + TransactionEntry.COLUMN_ACCOUNT_NO + ") REFERENCES " + AccountEntry.TABLE_NAME + "(" + AccountEntry.COLUMN_ACCOUNT_NO + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AccountEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TransactionEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
