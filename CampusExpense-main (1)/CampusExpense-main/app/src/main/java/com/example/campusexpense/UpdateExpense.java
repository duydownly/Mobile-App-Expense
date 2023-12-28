package com.example.campusexpense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

import database.BudgetEntity;
import database.DatabaseHelper;
import database.ExpenseEntity;

public class UpdateExpense extends AppCompatActivity {
    Spinner expenseTypeSpinner;
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        public EditText editText;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker.
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it.
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            editText.setText(day + "/" + (month + 1) + "/" + year);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_expense);
        EditText editTextExpenseDate = findViewById(R.id.updateEditTextText2);

        expenseTypeSpinner = findViewById(R.id.updateSpinner);
        setupBudgetSpinner();

        FloatingActionButton fab = findViewById(R.id.updateFloatingActionButton1234);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] options = {"Budget for All Expense","View All Expense", "Add Expense","Add Budget", "View Budget Info", "Logout"};
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateExpense.this);
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

        editTextExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.editText = editTextExpenseDate;
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        findViewById(R.id.updateButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText expenseNameControl = findViewById(R.id.updateEditTextText);
                EditText idTextControl = findViewById(R.id.IDexpense);
                String expenseIdString = idTextControl.getText().toString();
                String expenseName = expenseNameControl.getText().toString();

                if (expenseIdString.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Expense ID is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                Spinner budgetSpinner = findViewById(R.id.updateSpinner);
                String selectedBudgetName = budgetSpinner.getSelectedItem().toString();

                EditText expenseAmountControl = findViewById(R.id.updateEditTextText3);
                String expenseAmount = expenseAmountControl.getText().toString();
                EditText expenseDateControl = findViewById(R.id.updateEditTextText2);
                String expenseDate = expenseDateControl.getText().toString();

                int expenseId;
                try {
                    expenseId = Integer.parseInt(expenseIdString);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Expense ID format", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                int selectedBudgetId = dbHelper.getBudgetIdByName(selectedBudgetName);

                ExpenseEntity expense = new ExpenseEntity();
                expense.id = expenseId;
                expense.expenseName = expenseName;
                expense.amount = expenseAmount;
                expense.expenseDate = expenseDate;
                expense.budgetId = selectedBudgetId;

                long rowsAffected = dbHelper.updateExpense(expense);

                Toast.makeText(getApplicationContext(), "Rows affected: " + rowsAffected, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), AllExpenses.class);
                startActivity(intent);
            }
        });
    }

    public void setupBudgetSpinner() {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        List<BudgetEntity> budgetList = dbHelper.getAllBudgets();

        String[] budgetNames = new String[budgetList.size()];
        for (int i = 0; i < budgetList.size(); i++) {
            budgetNames[i] = budgetList.get(i).budgetName;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, budgetNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseTypeSpinner.setAdapter(adapter);
    }
}
