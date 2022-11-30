package ua.cn.stu.tic_tac_toe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ua.cn.stu.tic_tac_toe.R;

public class LoginFragment extends BaseFragment {

    private static final String LOGIN = "Artemiy";
    private static final String PASSWORD = "test";

    private EditText editTextLogin;
    private EditText editTextPassword;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextLogin = view.findViewById(R.id.loginEditText);
        editTextPassword = view.findViewById(R.id.passwordEditText);

        view.findViewById(R.id.loginButton).setOnClickListener(v -> {
            String login = editTextLogin.getText().toString();
            String password = editTextPassword.getText().toString();

            if (checkLogin(login) && checkPassword(password)) {
                getNavigator().launchGameFragment(login);
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
