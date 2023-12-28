package com.example.campusexpense;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import database.DatabaseHelper;
import database.BudgetEntity;
import database.ExpenseEntity;

public class UpdateBudget extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_budget);

        FloatingActionButton fab = findViewById(R.id.fabUpdateBudget);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] options = {"Budget for All Expense","View All Expense", "Add Expense","Add Budget", "View Budget Info", "Logout"};
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateBudget.this);
                builder.setItems(options, (dialog, item) -> {
                    if ("View All Expense".equals(options[item])) {
                        // Already in AllExpenses, do nothing or refresh the data
                        Intent intent = new Intent(getApplicationContext(), AllExpenses.class);
                        startActivity(intent);
                    } else if ("Budget for All Expense".equals(options[item])) {
                        Intent intent = new Intent(getApplicationContext(),BudgetforAllExpenseActivity.class);
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
        findViewById(R.id.buttonUpdateSaveBudget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText budgetNameControl = findViewById(R.id.editTextUpdateBudgetName);
                EditText idTextControl = findViewById(R.id.BudgetId);
                String budgetName = budgetNameControl.getText().toString();
                String budgetID = idTextControl.getText().toString();

                if (budgetID.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Budget Code is required", Toast.LENGTH_SHORT).show();
                    return;
                }


                EditText LimitedBudgetControl = findViewById(R.id.editTextUpdateLimitedBudget);
                String limitedBudget = LimitedBudgetControl.getText().toString();

                int budget_Id;
                try {
                    budget_Id = Integer.parseInt(budgetID);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Budget Code format", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                BudgetEntity budget = new BudgetEntity();
                budget.id = budget_Id;
                budget.budgetName = budgetName;
                budget.limitedBudget = Double.parseDouble(limitedBudget);

                long rowsAffected = dbHelper.updateBudget(budget);

                Toast.makeText(getApplicationContext(), "Rows affected: " + rowsAffected, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ViewBudgetInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
