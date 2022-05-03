package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.ExpenseManagerContract.TransactionEntry;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SQLiteTransactionDAO implements TransactionDAO {

    private DatabaseHelper databaseHelper;

    public SQLiteTransactionDAO() {
        this.databaseHelper = DatabaseHelper.getDatabaseHelperInstance();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues contentValues = mapTransactionIntoContent(date, accountNo, expenseType, amount);

        db.insert(TransactionEntry.TABLE_NAME, null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM transactions", null);

        return getTransactionList(cursor);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM transactions LIMIT " + limit, null);

        return getTransactionList(cursor);
    }

    private ContentValues mapTransactionIntoContent(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues content = new ContentValues();
        content.put(TransactionEntry.COLUMN_ACCOUNT_NO, accountNo);
        content.put(TransactionEntry.COLUMN_DATE, date.toString());
        content.put(TransactionEntry.COLUMN_EXPENSE_TYPE, expenseType.toString());
        content.put(TransactionEntry.COLUMN_AMOUNT, amount);
        return content;
    }

    private ArrayList<Transaction> getTransactionList(Cursor cursor) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        while(cursor.moveToNext()) {
            String accountNo = cursor.getString(cursor.getColumnIndex(TransactionEntry.COLUMN_ACCOUNT_NO));
            String date = cursor.getString(cursor.getColumnIndex(TransactionEntry.COLUMN_DATE));
            ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(TransactionEntry.COLUMN_EXPENSE_TYPE)));
            double amount = cursor.getDouble(cursor.getColumnIndex(TransactionEntry.COLUMN_AMOUNT));
            try {
                Date formattedDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(date);
                Transaction transaction = new Transaction(formattedDate, accountNo, expenseType, amount);
                transactions.add(transaction);
            }catch (ParseException ex) {
                System.out.println(ex);
            }
        }
        return transactions;
    }
}
