package com.example.campusexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import database.DatabaseHelper;
import database.BudgetEntity; // Import the BudgetEntity class

public class ViewBudgetInfoActivity extends AppCompatActivity {

    List<BudgetEntity> allBudgets; // Change the data type to BudgetEntity
    //...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_budget_info);

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        allBudgets = dbHelper.getAllBudgets(); // Change to get all budgets
        ArrayAdapter adapter = new ArrayAdapter<BudgetEntity>(this,
                R.layout.activity_listview, allBudgets); // Change to BudgetEntity
        ListView listView = (ListView) findViewById(R.id.listViewBudgetInfoExpense);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BudgetEntity entry = (BudgetEntity) parent.getItemAtPosition(position); // Change to BudgetEntity
                final String[] options = {"Delete", "Update"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewBudgetInfoActivity.this);
                builder.setItems(options, (dialog, item) -> {
                    if ("Delete".equals(options[item])) {
                        // 1. Remove from the ListView
                        allBudgets.remove(position);
                        adapter.notifyDataSetChanged();
                        // 2. Remove from database
                        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                        dbHelper.deleteBudget(entry.id);
                    } else if ("Update".equals(options[item])) {
                        // Navigate to UpdateBudget activity (you should create this activity)
                        Intent intent = new Intent(getApplicationContext(), UpdateBudget.class);
                        startActivity(intent);
                        finish(); // This will finish the current activity (AllExpenses)
                    }
                });
                builder.show();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fabViewBudgetInfoAddExpense);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] options = {"Budget for All Expense","View All Expense", "Add Expense","Add Budget", "View Budget Info", "Logout"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewBudgetInfoActivity.this);
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
    }
}
