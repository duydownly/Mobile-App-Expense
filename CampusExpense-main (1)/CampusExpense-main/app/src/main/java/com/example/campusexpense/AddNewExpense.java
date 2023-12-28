package com.example.campusexpense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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


public class AddNewExpense extends AppCompatActivity {
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
        setContentView(R.layout.activity_add_new_expense);
        EditText editTextExpenseDate = findViewById(R.id.editTextText2);

        expenseTypeSpinner = findViewById(R.id.spinner);
        populateBudgetSpinner();
        checkExpenseTableStructure();

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] options = {"Budget for All Expense","View All Expense", "Add Expense","Add Budget", "View Budget Info", "Logout"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewExpense.this);
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

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText expenseNameControl = findViewById(R.id.editTextText);
                String expenseName = expenseNameControl.getText().toString();

                // Spinner thay thế cho expenseType
                Spinner budgetSpinner = findViewById(R.id.spinner);
                String selectedBudgetName = budgetSpinner.getSelectedItem().toString();

                EditText expenseAmountControl = findViewById(R.id.editTextText3);
                String expenseAmount = expenseAmountControl.getText().toString();
                EditText expenseDateControl = findViewById(R.id.editTextText2);
                String expenseDate = expenseDateControl.getText().toString();

                // Lấy ID của ngân sách được chọn
                DatabaseHelper dbHelper = new DatabaseHelper(getApplication());
                int selectedBudgetId = dbHelper.getBudgetIdByName(selectedBudgetName);

                // Tạo đối tượng ExpenseEntity và thiết lập giá trị
                ExpenseEntity expense = new ExpenseEntity();
                expense.expenseName = expenseName;
                expense.amount = expenseAmount;
                expense.expenseDate = expenseDate;
                expense.budgetId = selectedBudgetId;

                // Thêm chi tiêu vào cơ sở dữ liệu
                long id = dbHelper.insertExpense(expense);
                Toast.makeText(getApplication(), String.valueOf(id), Toast.LENGTH_LONG).show();

                // Chuyển sang màn hình AllExpenses
                Intent intent = new Intent(getApplicationContext(), AllExpenses.class);
                startActivity(intent);
            }
        });


    }
    public void populateBudgetSpinner() {
        // Lấy danh sách tên ngân sách từ cơ sở dữ liệu
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        List<BudgetEntity> budgetList = dbHelper.getAllBudgets();

        // Trích xuất tên ngân sách từ danh sách
        String[] budgetNames = new String[budgetList.size()];
        for (int i = 0; i < budgetList.size(); i++) {
            budgetNames[i] = budgetList.get(i).budgetName;
        }

        // Tạo một ArrayAdapter sử dụng mảng chuỗi và một bố cục Spinner mặc định
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, budgetNames);

        // Chỉ định bố cục sử dụng khi danh sách lựa chọn xuất hiện
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Áp dụng Adapter cho Spinner
        expenseTypeSpinner.setAdapter(adapter);
    }
    private void checkExpenseTableStructure() {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        dbHelper.viewExpenseIds();
        Log.d("MyActivity", "Checked Expense Table Structure");
    }
}