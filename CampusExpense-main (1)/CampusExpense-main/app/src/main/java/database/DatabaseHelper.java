package database;

import static database.TableCreator.BudgetTable.TABLE_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends TableCreator {

    public DatabaseHelper(Context context) {
        super(context);
    }

    // EXPENSEEEEEEE
    public void deleteExpense(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableCreator.ExpenseTable.TABLE_NAME, TableCreator.KEY_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public long insertExpense(ExpenseEntity expense) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableCreator.ExpenseTable.KEY_EXPENSE_NAME, expense.expenseName);
        values.put(TableCreator.ExpenseTable.KEY_DATE, expense.expenseDate);
        values.put(TableCreator.ExpenseTable.KEY_AMOUNT, expense.amount);
        values.put(TableCreator.ExpenseTable.KEY_BUDGET_ID, expense.budgetId);  // Thêm BudgetID vào ContentValues

        long result = db.insertOrThrow(TableCreator.ExpenseTable.TABLE_NAME, null, values);
        db.close();
        return result;
    }

    private int getColumnIndex(Cursor cursor, String columnName) {
        if (cursor == null || columnName == null) {
            return -1; // Invalid input
        }

        return cursor.getColumnIndex(columnName);
    }

    public List<ExpenseEntity> getAllExpenses() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor results = db.rawQuery(
                "SELECT e.*, b." + BudgetTable.KEY_BUDGET_NAME +
                        " FROM " + ExpenseTable.TABLE_NAME + " e " +
                        " LEFT JOIN " + TABLE_NAME + " b ON e." + ExpenseTable.KEY_BUDGET_ID +
                        " = b." + KEY_ID +
                        " ORDER BY e." + ExpenseTable.KEY_DATE,
                null
        );
        Log.d("SQLQuery", "SQL Query: " +
                "SELECT e.*, b." + BudgetTable.KEY_BUDGET_NAME +
                " FROM " + ExpenseTable.TABLE_NAME + " e " +
                " LEFT JOIN " + TABLE_NAME + " b ON e." + ExpenseTable.KEY_BUDGET_ID +
                " = b." + KEY_ID +
                " ORDER BY e." + ExpenseTable.KEY_DATE);
        results.moveToFirst();
        List<ExpenseEntity> expenses = new ArrayList<>();
        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String date = results.getString(1);
            String amount = results.getString(2); // Corrected data type to String
            String expensename = results.getString(3);
            @SuppressLint("Range") String budgetName = results.getString(results.getColumnIndex(BudgetTable.KEY_BUDGET_NAME));
            Log.d("ExpenseData", "ID: " + id + ", Date: " + date + ", Amount: " + amount + ", ExpenseName: " + expensename + ", BudgetName: " + budgetName);

            ExpenseEntity expense = new ExpenseEntity();
            expense.id = id;
            expense.expenseName = expensename;
            expense.expenseDate = date;
            expense.amount = amount;
            expense.budgetName = budgetName;
            expenses.add(expense);
            results.moveToNext();
        }

        results.close();
        db.close();
        return expenses;
    }


    //BUDGETttt
    public void deleteBudget(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, TableCreator.KEY_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public List<BudgetEntity> getAllBudgets() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor results = db.query(
                TABLE_NAME,
                new String[]{
                        TableCreator.KEY_ID,
                        TableCreator.BudgetTable.KEY_BUDGET_NAME,
                        TableCreator.BudgetTable.KEY_LIMITED_BUDGET,
                },
                null,
                null,
                null,
                null,
                TableCreator.BudgetTable.KEY_BUDGET_NAME // You can change the ordering as needed
        );

        results.moveToFirst();
        List<BudgetEntity> budgets = new ArrayList<>();
        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String budgetName = results.getString(1);
            double limitedBudget = results.getDouble(2);

            BudgetEntity budget = new BudgetEntity();
            budget.id = id;
            budget.budgetName = budgetName;
            budget.limitedBudget = limitedBudget;

            budgets.add(budget);
            results.moveToNext();
        }

        results.close();
        db.close();
        return budgets;
    }

    public long insertBudget(BudgetEntity budget) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableCreator.BudgetTable.KEY_BUDGET_NAME, budget.budgetName); // Replace with the actual budget name or remove if not needed
        values.put(TableCreator.BudgetTable.KEY_LIMITED_BUDGET, budget.limitedBudget); // Assuming limitedBudget is a public field in BudgetEntity

        long result = db.insertOrThrow(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public int getBudgetIdByName(String budgetName) {
        SQLiteDatabase db = getReadableDatabase();
        int budgetId = -1; // Giả sử không tìm thấy

        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{TableCreator.KEY_ID},
                TableCreator.BudgetTable.KEY_BUDGET_NAME + "=?",
                new String[]{budgetName},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            budgetId = cursor.getInt(cursor.getColumnIndex(TableCreator.KEY_ID));
        }

        cursor.close();
        db.close();

        return budgetId;
    }

    //Register
    public boolean isUsernameUnique(String username) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TableCreator.LoginTable.TABLE_NAME,
                new String[]{TableCreator.KEY_ID},
                TableCreator.LoginTable.KEY_USERNAME + "=?",
                new String[]{username},
                null, null, null
        );

        boolean isUnique = cursor.getCount() == 0;

        cursor.close();
        db.close();

        return isUnique;
    }

    public long insertUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableCreator.LoginTable.KEY_USERNAME, username);
        values.put(TableCreator.LoginTable.KEY_PASSWORD, password);

        long result = db.insertOrThrow(TableCreator.LoginTable.TABLE_NAME, null, values);

        db.close();
        return result;
    }

    //LOGINNNNNNNNNNNNNNNN
// Inside your DatabaseHelper class
    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {TableCreator.KEY_ID};
        String selection = TableCreator.LoginTable.KEY_USERNAME + "=? AND " +
                TableCreator.LoginTable.KEY_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TableCreator.LoginTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        boolean isAuthenticated = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return isAuthenticated;
    }
    //UPDATEEEEEEEEEEEEEEEEEE Expense

    public long updateExpense(ExpenseEntity expense) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExpenseTable.KEY_EXPENSE_NAME, expense.expenseName);
        values.put(ExpenseTable.KEY_DATE, expense.expenseDate);
        values.put(ExpenseTable.KEY_AMOUNT, expense.amount);
        values.put(ExpenseTable.KEY_BUDGET_ID, expense.budgetId);

        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = {String.valueOf(expense.id)};

        long rowsAffected = 0;

        try {
            rowsAffected = db.update(ExpenseTable.TABLE_NAME, values, whereClause, whereArgs);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, log, or show an error message as needed
        } finally {
            db.close();
        }

        return rowsAffected;
    }

//TESTTTTTTTTTTT
    // Các biến, hằng và phương thức khác của DatabaseHelper


    public void viewExpenseIds() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(ExpenseTable.TABLE_NAME, new String[]{KEY_ID}, null, null, null, null, null);

        try {
            int idIndex = cursor.getColumnIndex(KEY_ID);

            while (cursor.moveToNext()) {
                int expenseId = cursor.getInt(idIndex);

                // Use Log to print the ID values
                Log.d("ExpenseTable", "Expense ID: " + expenseId);
            }
        } finally {
            cursor.close();
            db.close();
        }
    }


    // Các phương thức khác của DatabaseHelper
    public int updateBudget(BudgetEntity budget) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BudgetTable.KEY_BUDGET_NAME, budget.budgetName);
        values.put(BudgetTable.KEY_LIMITED_BUDGET, budget.limitedBudget);

        // Assuming KEY_ID is the column name for the budget ID
        String selection = KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(budget.id)};

        // Update the row with the specified budget ID
        int rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs);

        db.close();

        return rowsAffected;
    }

    //BUDGETFORALLEX
    public void updateBudgets(int budgetId, double newExpenseBudget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BudgetTable.KEY_EXPENSE_BUDGET, newExpenseBudget);

        try {
            db.update(
                    BudgetTable.TABLE_NAME,
                    values,
                    KEY_ID + " = ?",
                    new String[]{String.valueOf(budgetId)}
            );
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        } finally {
            db.close();
        }
    }

    // Save a new budget to the database
    public void saveBudgets(BudgetEntity budgetEntity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BudgetTable.KEY_EXPENSE_BUDGET, budgetEntity.expenseBudget);
        try {
            db.insert(BudgetTable.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        } finally {
            db.close();
        }
    }

    @SuppressLint("Range")
    public double getExpenseBudgetForBudgetId1() {
        SQLiteDatabase db = this.getReadableDatabase();
        double expenseBudget = 0;  // Initialize with a default value

        // Define the columns to be retrieved in the query
        String[] columns = {BudgetTable.KEY_EXPENSE_BUDGET};

        // Define the selection criteria
        String selection = KEY_ID + " = ?";

        // Define the selection arguments (in this case, the budget ID is 1)
        String[] selectionArgs = {"1"};

        // Perform the query and retrieve the expense budget directly
        Cursor cursor = db.query(
                BudgetTable.TABLE_NAME,  // Table name
                columns,                  // Columns to be retrieved
                selection,                // Selection criteria
                selectionArgs,            // Selection arguments
                null,                     // Group by (null in this case)
                null,                     // Having (null in this case)
                null                      // Order by (null in this case)
        );

        // Check if the cursor is not null and move to the first row
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the expense budget from the cursor
            expenseBudget = cursor.getDouble(cursor.getColumnIndex(BudgetTable.KEY_EXPENSE_BUDGET));

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return expenseBudget;
    }

}