package com.example.campusexpense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle login button click
                // For example, navigate to another activity
                Intent intent = new Intent(LoginActivity.this, BudgetforAllExpenseActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle register button click
                // For example, navigate to the registration activity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Add authentication logic here
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        Button authenticateButton = findViewById(R.id.loginButton);

        authenticateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check if the credentials are valid
                if (dbHelper.authenticateUser(username, password)) {
                    // Successful login
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, AllExpenses.class);
                    startActivity(intent);
                } else {
                    // Invalid credentials
                    Toast.makeText(LoginActivity.this, "Invalid username or password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
