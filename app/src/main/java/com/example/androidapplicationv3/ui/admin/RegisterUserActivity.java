package com.example.androidapplicationv3.ui.admin;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.database.async.users.AddUser;
import com.example.androidapplicationv3.database.entity.UserEntity;
import com.example.androidapplicationv3.ui.BaseActivity;
import com.example.androidapplicationv3.util.OnAsyncEventListener;

public class RegisterUserActivity extends BaseActivity {

    private static final String TAG = "RegisterUserActivity";
    private static final int SAVE_USER = 1;

    private EditText inputLastname;
    private EditText inputFirstname;
    private EditText inputUsername;
    private EditText inputPassword;
    private SwitchCompat switchIsAdmin;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // initialize view
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_register_user, frameLayout);
        setTitle(getString(R.string.title_activity_registeruser));
        navigationView.setCheckedItem(position);
        initiateView();

    }

    /**
     * Initialize view
     */
    private void initiateView() {
        inputLastname = findViewById(R.id.registerInputLastname);
        inputFirstname = findViewById(R.id.registerInputFirstname);
        inputUsername = findViewById(R.id.registerInputUsername);
        inputPassword = findViewById(R.id.registerInputPassword);
        switchIsAdmin = findViewById(R.id.registerSwitchIsAdmin);
    }

    /**
     * Initialize ActionBar of the activity
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, SAVE_USER, Menu.NONE, getString(R.string.action_save))
                .setIcon(R.drawable.ic_baseline_save_alt_24)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    /**
     * Manage actions of the action bar
     * @param item
     * @return
     */
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


    /**
     * Check inputs and add new user to database
     *
     * @param lastname
     * @param firstname
     * @param username
     * @param password
     * @param isAdmin
     */
    private void saveUser(String lastname, String firstname, String username, String password, Boolean isAdmin) {

        if(lastname.isEmpty()) {
           inputLastname.setError(getString(R.string.error_register_no_lastname));
           inputLastname.requestFocus();
           return;
        }
        if(firstname.isEmpty()) {
            inputFirstname.setError(getString(R.string.error_register_no_firstname));
            inputFirstname.requestFocus();
            return;
        }
        if(username.isEmpty()) {
            inputUsername.setError(getString(R.string.error_register_no_username));
            inputUsername.requestFocus();
            return;
        }
        if(username.length() < 6) {
           inputUsername.setError(getString(R.string.error_register_username_too_short));
           inputUsername.requestFocus();
           return;
        }
        if(!username.contains(".")) {
            inputUsername.setError(getString(R.string.error_register_invalid_username));
            inputUsername.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            inputPassword.setError(getString(R.string.error_register_no_password));
            inputPassword.setText("");
            inputPassword.requestFocus();
            return;
        }
        if(password.length() < 6) {
            inputPassword.setError(getString(R.string.error_invalid_password));
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
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                setResponse(false);
            }
        }).execute(newUser);

    }


    /**
     * Message to user to confirm the result of the action
     * @param response
     */
    private void setResponse(Boolean response) {
        if (response) {
            toast = Toast.makeText(this, getString(R.string.user_created_msg), Toast.LENGTH_LONG);
            toast.show();
            onBackPressed();
        } else {
            inputUsername.setError(getString(R.string.error_register_username_alreadyinuse));
            inputUsername.requestFocus();
        }
    }


}