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

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentDemoExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
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

    @Test
    public void testCreateAccount() {
        //add data into the database
        expenseManager.addAccount("00123456", "BOC", "Dasun", 10000.67);
        //retrieve data from the database
        List<String> accountNoList = expenseManager.getAccountNumbersList();
        assertTrue(accountNoList.contains("00123456"));

    }

    @Test
    public void testLogTransaction() {
        //add data into the database
        expenseManager.addAccount("01234567", "HNB", "Disal", 10000);
        List<Transaction> beforeTransactionList = expenseManager.getTransactionsDAO().getAllTransactionLogs();

        try {
            expenseManager.updateAccountBalance("01234567", 10, 5, 2022, ExpenseType.INCOME, "2000");

        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
        List<Transaction> afterTransactionList = expenseManager.getTransactionsDAO().getAllTransactionLogs();
        assertEquals(beforeTransactionList.size() + 1, afterTransactionList.size());
    }




}