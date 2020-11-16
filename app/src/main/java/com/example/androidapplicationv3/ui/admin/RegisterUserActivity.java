package com.example.androidapplicationv3.ui.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.database.async.requests.AddRequest;
import com.example.androidapplicationv3.database.async.users.AddUser;
import com.example.androidapplicationv3.database.entity.UserEntity;
import com.example.androidapplicationv3.ui.BaseActivity;
import com.example.androidapplicationv3.util.OnAsyncEventListener;

import java.text.ParseException;

public class RegisterUserActivity extends BaseActivity {

    private static final String TAG = "RegisterUserActivity";
    private static final int SAVE_USER = 1;

    private EditText inputLastname;
    private EditText inputFirstname;
    private EditText inputUsername;
    private EditText inputPassword;
    private Switch switchIsAdmin;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_register_user, frameLayout);

        setTitle(getString(R.string.title_activity_registerUser));
        navigationView.setCheckedItem(position);

        initiateView();

    }

    private void initiateView() {
        inputLastname = findViewById(R.id.registerInputLastname);
        inputFirstname = findViewById(R.id.registerInputFirstname);
        inputUsername = findViewById(R.id.registerInputUsername);
        inputPassword = findViewById(R.id.registerInputPassword);
        switchIsAdmin = findViewById(R.id.registerSwitchIsAdmin);
    }

    private void saveUser(String lastname, String firstname, String username, String password, Boolean isAdmin) {

        if(lastname.isEmpty()) {
           inputLastname.setError("Please fill in your lastname");
           inputLastname.requestFocus();
           Toast.makeText(this,"Error",Toast.LENGTH_LONG);
           return;
        }
        if(firstname.isEmpty()) {
            inputFirstname.setError("Please fill in your firstname");
            inputFirstname.requestFocus();
            return;
        }
        if(username.isEmpty()) {
            inputUsername.setError("Please choose an username");
            inputUsername.requestFocus();
            return;
        }
        if(username.length() < 6) {
           inputUsername.setError("This username is too short (<6 characters)");
           inputUsername.requestFocus();
           return;
        }
        if(!username.contains(".")) {
            inputUsername.setError("Username must contain a dot '.'");
            inputUsername.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            inputPassword.setError("Please choose a new password");
            inputPassword.setText("");
            inputPassword.requestFocus();
            return;
        }
        if(password.length() < 6) {
            inputPassword.setError("This username is too short (<6 characters)");
            inputPassword.requestFocus();
            return;
        }

        UserEntity newUser = new UserEntity();

        newUser.setLastname(lastname);
        newUser.setFirstname(firstname);
        newUser.setLogin(username);
        newUser.setPassword(password);
        newUser.setIsAdmin(isAdmin);

        new AddUser(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "createUser: success");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "createUser: failure", e);
                setResponse(false);

            }
        }).execute(newUser);

    }

    private void setResponse(Boolean response) {
        if (response) {
            toast = Toast.makeText(this, "User created", Toast.LENGTH_LONG);
            toast.show();
            onBackPressed();
        } else {
            inputUsername.setError("Username already in use");
            inputUsername.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, SAVE_USER, Menu.NONE, getString(R.string.action_save))
                .setIcon(R.drawable.ic_baseline_save_alt_24)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == SAVE_USER) {
            saveUser(
                    inputLastname.getText().toString(),
                    inputFirstname.getText().toString(),
                    inputUsername.getText().toString(),
                    inputPassword.getText().toString(),
                    switchIsAdmin.isChecked()
            );
        } else {
            return false;
        }
        return true;
    }
}