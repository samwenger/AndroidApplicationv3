package com.example.androidapplicationv3.database;

import android.os.AsyncTask;
import android.util.Log;

import com.example.androidapplicationv3.database.entity.StatusEntity;
import com.example.androidapplicationv3.database.entity.TypeEntity;
import com.example.androidapplicationv3.database.entity.UserEntity;


/**
 * Generates dummy data
 */
public class DatabaseInitializer {

    public static final String TAG = "DatabaseInitializer";

    public static void populateDatabase(final AppDatabase db) {
        Log.i(TAG, "Inserting demo data.");
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static void addUser(final AppDatabase db, final String lastname,
                                  final String firstname, final String login, final String password, int remainingDays,  Boolean isAdmin) {
        UserEntity user = new UserEntity(lastname, firstname, login, password, remainingDays, isAdmin);
        db.userDao().insert(user);
    }

    private static void addStatus(final AppDatabase db, final Long idStatus, final String status) {
        StatusEntity statusEntity= new StatusEntity(idStatus, status);
        db.statusDao().insert(statusEntity);
    }

    private static void addType(final AppDatabase db, final Long idType, final String type) {
        TypeEntity typeEntity= new TypeEntity(idType, type);
        db.typesDao().insert(typeEntity);
    }


    private static void populateWithTestData(AppDatabase db) {
        db.userDao().deleteAll();
        db.typesDao().deleteAll();
        db.statusDao().deleteAll();

        // Generating users
        // Creating a simple user (no admin access)
        addUser(db,
                "Gallay", "Robin", "robin.gallay", "AdminHevs01",25, false
        );


        // Creating an admin user (with admin access)
        addUser(db,
                "Wenger", "Samuel", "samuel.wenger", "AdminHevs01",10, true
        );

        // Generating leaves types
        addType(db,new Long(1),"Vacation");
        addType(db,new Long(2),"Special Leave");
        addType(db,new Long(3),"Overtime Compensation");
        addType(db,new Long(4),"Leave without pay");


        // Generating leaves status
        addStatus(db,new Long(1),"Pending");
        addStatus(db,new Long(2),"Approved");
        addStatus(db,new Long(3),"Refused");

        try {
            // Let's ensure that the clients are already stored in the database before we continue.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase database;

        PopulateDbAsync(AppDatabase db) {
            database = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(database);
            return null;
        }

    }
}
