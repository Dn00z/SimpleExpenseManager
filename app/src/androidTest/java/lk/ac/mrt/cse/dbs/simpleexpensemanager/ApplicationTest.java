/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentDemoExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {
    private ExpenseManager expenseManager;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        DatabaseHelper.createDatabaseHelperInstance(context);
        expenseManager = new PersistentDemoExpenseManager();
    }
//unit test for adding new account to the database and get account details from the database
    @Test
    public void testAddAccount() {
        expenseManager.addAccount("000123", "BOC", "Dasun", 10000);
        List<String> accountNumbers = expenseManager.getAccountNumbersList();
        assertTrue(accountNumbers.contains("000123"));
    }
//unit test for logging new transaction to the database and get the transaction list form the database
    @Test
    public void testLogTransaction() {
        expenseManager.addAccount("001234", "HNB", "Nimantha", 20000);
        List<Transaction> beforeTransactions = expenseManager.getTransactionsDAO().getAllTransactionLogs();
        try {
            expenseManager.updateAccountBalance("001234", 10, 5, 2022, ExpenseType.INCOME, "2500");
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
        List<Transaction> afterTransactions = expenseManager.getTransactionsDAO().getAllTransactionLogs();
        assertEquals(beforeTransactions.size() + 1, afterTransactions.size());
    }
}