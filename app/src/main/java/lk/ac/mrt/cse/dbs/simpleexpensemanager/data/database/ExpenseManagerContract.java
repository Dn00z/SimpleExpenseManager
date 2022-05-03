package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.provider.BaseColumns;

public final class ExpenseManagerContract {
    private ExpenseManagerContract() {}

    public static class AccountEntry implements BaseColumns {
        public final static String TABLE_NAME = "accounts";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ACCOUNT_NO = "account_no";
        public final static String COLUMN_BANK_NAME = "bank_name";
        public final static String COLUMN_ACCOUNT_HOLDER_NAME = "account_holder_name";
        public final static String COLUMN_BALANCE = "balance";
    }

    public static class TransactionEntry implements BaseColumns {
        public final static String TABLE_NAME = "transactions";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_ACCOUNT_NO = "account_no";
        public final static String COLUMN_EXPENSE_TYPE = "expense_type";
        public final static String COLUMN_AMOUNT = "amount";
    }
}
