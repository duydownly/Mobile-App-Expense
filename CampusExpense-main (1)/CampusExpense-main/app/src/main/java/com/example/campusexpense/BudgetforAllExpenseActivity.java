package com.example.campusexpense;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import database.DatabaseHelper;
import database.BudgetEntity;

public class BudgetforAllExpenseActivity extends AppCompatActivity {

    // Declare EditText fields
    private EditText budgetNameEditText;
    private EditText expenseBudgetEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgetfor_all_expense);

        // Initialize EditText fields
        expenseBudgetEditText = findViewById(R.id.budgetforallexpense_editTextBudgetName);

        FloatingActionButton fab = findViewById(R.id.fabExBudget);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] options = {"Budget for All Expense", "View All Expense", "Add Expense", "Add Budget", "View Budget Info", "Logout"};
                AlertDialog.Builder builder = new AlertDialog.Builder(BudgetforAllExpenseActivity.this);
                builder.setItems(options, (dialog, item) -> {
                    if ("View All Expense".equals(options[item])) {
                        // Already in AllExpenses, do nothing or refresh the data
                        Intent intent = new Intent(getApplicationContext(), AllExpenses.class);
                        startActivity(intent);
                    } else if ("Budget for All Expense".equals(options[item])) {
                        // Do nothing, already in BudgetforAllExpenseActivity
                    } else if ("Add Expense".equals(options[item])) {
                        // Start AddNewExpense activity
                        Intent intent = new Intent(getApplicationContext(), AddNewExpense.class);
                        startActivity(intent);
                    } else if ("Add Budget".equals(options[item])) {
                        // Start AddBudget activity
                        Intent intent = new Intent(getApplicationContext(), AddBudget.class);
                        startActivity(intent);
                    } else if ("View Budget Info".equals(options[item])) {
                        // Start ViewBudgetInfoActivity activity
                        Intent intent = new Intent(getApplicationContext(), ViewBudgetInfoActivity.class);
                        startActivity(intent);
                    } else if ("Logout".equals(options[item])) {
                        // Start LoginActivity and clear previous activities
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // This will finish the current activity (BudgetforAllExpenseActivity)
                    }
                });
                builder.show();
            }
        });



        findViewById(R.id.budgetforallexpense_buttonUpdateBudget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAndDisplayBudget();
            }
        });
    }

    // Inside your BudgetforAllExpenseActivity class
    private void updateAndDisplayBudget() {
        // Retrieve data from the EditText fields
        int budgetId = 1; // Get the ID of the budget you want to update;
        double newExpenseBudget = Double.parseDouble(expenseBudgetEditText.getText().toString());

        // Assuming you have a DatabaseHelper class to handle database operations
        DatabaseHelper databaseHelper = new DatabaseHelper(BudgetforAllExpenseActivity.this);

        // Update the budget with the newExpenseBudget
        databaseHelper.updateBudgets(budgetId, newExpenseBudget);

        // Retrieve the updated BudgetEntity with ID 1 from the database
        double budgetEntity = databaseHelper.getExpenseBudgetForBudgetId1();

            TextView expenseBudgetTextView = findViewById(R.id.textView2);
        expenseBudgetTextView.setText(String.valueOf(budgetEntity));

    }

}
