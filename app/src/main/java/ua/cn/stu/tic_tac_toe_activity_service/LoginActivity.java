package ua.cn.stu.tic_tac_toe_activity_service;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN = "Artemiy";
    private static final String PASSWORD = "test";

    private EditText editTextLogin;
    private EditText editTextPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextLogin = findViewById(R.id.loginEditText);
        editTextPassword = findViewById(R.id.passwordEditText);

        findViewById(R.id.loginButton).setOnClickListener(v -> {
            String login = editTextLogin.getText().toString();
            String password = editTextPassword.getText().toString();

            if (checkLogin(login) && checkPassword(password)) {
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra(GameActivity.EXTRA_NAME, login);
                startActivity(intent);
            }
        });
    }

    private boolean checkLogin(String login) {
        if (login.equals("")) {
            editTextLogin.setError("Login is empty");
            return false;
        }
        if (!login.equals(LOGIN)) {
            editTextLogin.setError("Login is wrong");
            return false;
        }
        return true;
    }

    private boolean checkPassword(String password) {
        if (password.equals("")) {
            editTextPassword.setError("PASSWORD is empty");
            return false;
        }
        if (!password.equals(PASSWORD)) {
            editTextPassword.setError("PASSWORD is wrong");
            return false;
        }
        return true;
    }
}
