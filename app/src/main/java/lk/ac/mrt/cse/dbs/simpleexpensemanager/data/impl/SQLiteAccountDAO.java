package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.ExpenseManagerContract.AccountEntry;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class SQLiteAccountDAO implements AccountDAO {

    private DatabaseHelper databaseHelper;

    public SQLiteAccountDAO() {
        this.databaseHelper = DatabaseHelper.getDatabaseHelperInstance();
    }

    @Override
    public List<String> getAccountNumbersList() {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT account_no FROM accounts", null);
        ArrayList<String> accountIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            accountIds.add(cursor.getString(cursor.getColumnIndex("account_no")));
        }
        return accountIds;
    }

    @Override
    public List<Account> getAccountsList() {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM accounts", null);
        ArrayList<Account> accounts = new ArrayList<>();
        while(cursor.moveToNext()) {
            String accountNo = cursor.getString(cursor.getColumnIndex("account_no"));
            String bankName = cursor.getString(cursor.getColumnIndex("bank_name"));
            String accountHolderName = cursor.getString(cursor.getColumnIndex("account_holder_name"));
            Double balance = cursor.getDouble(cursor.getColumnIndex("balance"));
            Account account = new Account(accountNo, bankName, accountHolderName, balance);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM accounts WHERE account_no = '" + accountNo + "'", null);

        Account account = null;
        if(cursor.moveToFirst()) {
            String bankName = cursor.getString(cursor.getColumnIndex("bank_name"));
            String accountHolderName = cursor.getString(cursor.getColumnIndex("account_holder_name"));
            Double balance = cursor.getDouble(cursor.getColumnIndex("balance"));
            account = new Account(accountNo, bankName, accountHolderName, balance);
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues content = mapAccountIntoContent(account);

        db.insert(AccountEntry.TABLE_NAME, null, content);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(AccountEntry.TABLE_NAME,  AccountEntry.COLUMN_ACCOUNT_NO + "=?" , new String[]{accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        Account account = getAccount(accountNo);

        switch (expenseType) {
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
        }

        ContentValues contentValues = mapAccountIntoContent(account);

        db.update(AccountEntry.TABLE_NAME, contentValues, AccountEntry.COLUMN_ACCOUNT_NO + "=?" , new String[]{accountNo});
    }

    private ContentValues mapAccountIntoContent(Account account) {
        ContentValues content = new ContentValues();
        content.put(AccountEntry.COLUMN_ACCOUNT_NO, account.getAccountNo());
        content.put(AccountEntry.COLUMN_BANK_NAME, account.getBankName());
        content.put(AccountEntry.COLUMN_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        content.put(AccountEntry.COLUMN_BALANCE, account.getBalance());
        return content;
    }
}
