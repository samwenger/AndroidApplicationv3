package com.example.androidapplicationv3.ui.managment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.androidapplicationv3.BaseApp;
import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.database.entity.UserEntity;
import com.example.androidapplicationv3.database.firebase.UserLiveData;
import com.example.androidapplicationv3.database.repository.UserRepository;
import com.example.androidapplicationv3.ui.BaseActivity;
import com.example.androidapplicationv3.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameView;
    private EditText passwordView;
    private ProgressBar progressBar;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Check if dark mode was activated during the last connection
        if(darkModeOn()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        // initialize
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_login);
        setContentView(R.layout.activity_login);

        userRepository = ((BaseApp) getApplication()).getUserRepository();
        progressBar = findViewById(R.id.loading);
        usernameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(view -> attemptLogin());
    }

    /**
     * Try login into the app
     */
    private void attemptLogin() {
        // Reset errors
        usernameView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            passwordView.setText("");
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username) || !isUsernameValid(username)) {
            usernameView.setError(getString(R.string.error_invalid_username));
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            // Error, back on the view with focus
            focusView.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            userRepository.signIn(username, password, task -> {
                if (task.isSuccessful()) {

                    UserLiveData currentUser = (UserLiveData) userRepository.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
                //    UserEntity currentUser2 = userRepository.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue();
                    UserEntity currentUser2 = userRepository.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());


                    SharedPreferences.Editor editor = getSharedPreferences(BaseActivity.PREFS_NAME,0).edit();
                 //   editor.putBoolean(BaseActivity.PREFS_ISADMIN, currentUser.getIsAdmin());
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    usernameView.setText("");
                    passwordView.setText("");

                } else {
                    passwordView.setError(getString(R.string.error_incorrect_password));
                    passwordView.requestFocus();
                    passwordView.setText("");
                }
                progressBar.setVisibility(View.GONE);
            });
        }
    }

    /**
     * Check if password is valid
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * Check if username is valid
     * @param username
     * @return
     */
    private boolean isUsernameValid(String username) {
        return username.contains(".") && username.length()>=6;
    }

    /**
     * Check if darkmode was activated during the last session
     * @return
     */
    private boolean darkModeOn() {
        SharedPreferences sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        boolean isChecked = sharedPreferences.getBoolean("switchDarkMode", false);

        if(isChecked){
            return true;
        }
        else {
            return false;
        }
    }
}