package com.example.androidapplicationv3.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.ui.admin.RegisterUserActivity;
import com.example.androidapplicationv3.ui.admin.RequestsAdminActivity;
import com.example.androidapplicationv3.ui.managment.LoginActivity;
import com.example.androidapplicationv3.ui.managment.SettingsActivity;
import com.example.androidapplicationv3.ui.request.AddRequestActivity;
import com.example.androidapplicationv3.ui.request.RequestsActivity;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static final String PREFS_NAME = "SharedPrefs";
    public static final String PREFS_IDUSER = "IDUser";
    public static final String PREFS_ISADMIN = "ISAdmin";


    protected FrameLayout frameLayout;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    protected static int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialize view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = findViewById(R.id.flContent);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        // Navigation drawer configuration
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        // Check if the current user has admin access
        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        Boolean isAdmin = settings.getBoolean(BaseActivity.PREFS_ISADMIN, false);

        if (isAdmin) {
            // Display admin menus only to admin users
            navigationView.getMenu().findItem(R.id.nav_adminrequests).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_registeruser).setVisible(true);
        }


    }


    /**
     * Handle clicks on navigation drawer menu and start the corresponding activity
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == BaseActivity.position) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        BaseActivity.position = id;
        Intent intent = null;

        navigationView.setCheckedItem(id);

        switch (id) {
            case R.id.nav_home:
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.nav_requests:
                intent = new Intent(this, RequestsActivity.class);
                break;
            case R.id.nav_addrequest:
                intent = new Intent(this, AddRequestActivity.class);
                break;
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_adminrequests:
                intent = new Intent(this, RequestsAdminActivity.class);
                break;
            case R.id.nav_registeruser:
                intent = new Intent(this, RegisterUserActivity.class);
                break;
        }
        if (intent != null) {
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_NO_ANIMATION
            );
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    /**
     * Create the options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }



    /**
     * Handle click on options menu (settings)
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Logout user
     */
    public void logout() {
        SharedPreferences.Editor editor = getSharedPreferences(BaseActivity.PREFS_NAME, 0).edit();
        editor.remove(BaseActivity.PREFS_IDUSER);
        editor.remove(BaseActivity.PREFS_ISADMIN);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


}