package com.example.campusexpense;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import database.DatabaseHelper;
import database.BudgetEntity;

public class AddBudget extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        FloatingActionButton fab = findViewById(R.id.fabAddBudget);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] options = {"Budget for All Expense","View All Expense", "Add Expense","Add Budget", "View Budget Info", "Logout"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBudget.this);
                builder.setItems(options, (dialog, item) -> {
                    if ("View All Expense".equals(options[item])) {
                        // Already in AllExpenses, do nothing or refresh the data
                        Intent intent = new Intent(getApplicationContext(), AllExpenses.class);
                        startActivity(intent);
                    } else if ("Budget for All Expense".equals(options[item])) {
                        Intent intent = new Intent(getApplicationContext(), BudgetforAllExpenseActivity.class);
                        startActivity(intent);
                    } else if ("Add Expense".equals(options[item])) {
                        // Start AddNewExpense activity
                        Intent intent = new Intent(getApplicationContext(), AddNewExpense.class);
                        startActivity(intent);
                    } else if ("Add Budget".equals(options[item])) {
                        Intent intent = new Intent(getApplicationContext(),AddBudget.class);
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
                        finish(); // This will finish the current activity (AllExpenses)
                    }
                });
                builder.show();
            }
        });
        findViewById(R.id.buttonSaveBudget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText budgetnamecontrol = findViewById(R.id.editTextBudgetName);
                String budgetName = budgetnamecontrol.getText().toString();
                EditText limitedBudgets = findViewById(R.id.editTextLimitedBudget);
                String limitedBudget = limitedBudgets.getText().toString();

                BudgetEntity budget = new BudgetEntity();
                budget.budgetName = budgetName;
                budget.limitedBudget = (Double.parseDouble(limitedBudget)); // Assuming limitedBudget is a double
                // You may want to add more fields and validation based on your BudgetEntity structure

                DatabaseHelper dbHelper = new DatabaseHelper(getApplication());
                long id = dbHelper.insertBudget(budget);
                Toast.makeText(getApplication(), String.valueOf(id), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), ViewBudgetInfoActivity .class);
                startActivity(intent);
            }
        });
    }
}
