package com.example.campusexpense;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        Button createAccountButton = findViewById(R.id.createAccountButton);
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve entered username and password
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check if the username is unique
                if (!dbHelper.isUsernameUnique(username)) {
                    Toast.makeText(RegisterActivity.this, "Username already exists. Please choose another one.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert the user into the Login table
                long id = dbHelper.insertUser(username, password);

                if (id != -1) {
                    // Registration successful
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    navigateToLoginActivity();
                } else {
                    // Handle the case where registration failed
                    Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();  // Close the current activity to prevent going back to it when pressing back
    }
}
