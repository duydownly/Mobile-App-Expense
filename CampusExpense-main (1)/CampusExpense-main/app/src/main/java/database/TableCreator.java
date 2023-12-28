package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public  class TableCreator  extends SQLiteOpenHelper {

    protected static final String KEY_ID = "ID";
    // Common column names
    // Database name and version
    private static final String DATABASE_NAME = "Budget_management_";
    private static final int DATABASE_VERSION = 1;

    // Budget table
    public static class BudgetTable {
        public static final String TABLE_NAME = "Budget";
        public static final String KEY_BUDGET_NAME = "BudgetName";
        public static final String KEY_LIMITED_BUDGET = "LimitedBudget";
        public static final String KEY_EXPENSE_BUDGET = "ExpenseBudget";
    }

    // Expense table
    public static class ExpenseTable {
        public static final String TABLE_NAME = "Expense";
        public  static  final String KEY_EXPENSE_NAME = "ExpenseName";
        public static final String KEY_DATE = "Date";
        public static final String KEY_AMOUNT = "Amount";
        public static final String KEY_BUDGET_ID = "BudgetID";
    }

    // Login table
    public static class LoginTable {
        public static final String TABLE_NAME = "Login";
        public static final String KEY_USERNAME = "Username";
        public static final String KEY_PASSWORD = "Password";
    }

    // Table creation queries
    private static final String CREATE_TABLE_BUDGET = "CREATE TABLE " + BudgetTable.TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BudgetTable.KEY_BUDGET_NAME + " TEXT,"
            + BudgetTable.KEY_EXPENSE_BUDGET +"NUMERIC,"
            + BudgetTable.KEY_LIMITED_BUDGET + " NUMERIC);";


    private static final String CREATE_TABLE_EXPENSE = "CREATE TABLE " + ExpenseTable.TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ExpenseTable.KEY_EXPENSE_NAME + " TEXT,"
            + ExpenseTable.KEY_DATE + " TEXT,"
            + ExpenseTable.KEY_AMOUNT + " NUMERIC,"  // Change to TEXT if storing as text
            + ExpenseTable.KEY_BUDGET_ID + " INTEGER,"
            + "FOREIGN KEY(" + ExpenseTable.KEY_BUDGET_ID + ") REFERENCES " +
            BudgetTable.TABLE_NAME + "(" + KEY_ID + "));";


    private static final String CREATE_TABLE_LOGIN = "CREATE TABLE " + LoginTable.TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + LoginTable.KEY_USERNAME + " TEXT,"
            + LoginTable.KEY_PASSWORD + " TEXT);";

    public TableCreator(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BUDGET);
        db.execSQL(CREATE_TABLE_EXPENSE);
        db.execSQL(CREATE_TABLE_LOGIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + ExpenseTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BudgetTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LoginTable.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

}
