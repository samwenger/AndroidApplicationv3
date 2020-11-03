package com.example.androidapplicationv3.database;

import android.os.AsyncTask;
import android.util.Log;

import com.example.androidapplicationv3.database.converters.Converters;
import com.example.androidapplicationv3.database.entity.UserEntity;

import java.sql.Date;

/**
 * Generates dummy data
 */
public class DatabaseInitializer {

    private static Converters converters = new Converters();

    public static final String TAG = "DatabaseInitializer";

    public static void populateDatabase(final AppDatabase db) {
        Log.i(TAG, "Inserting demo data.");
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static void addUser(final AppDatabase db, final String lastname,
                                  final String firstname, final String login, final String password, Date birthday, int remainingDays) {
        UserEntity user = new UserEntity(lastname, firstname, login, password, converters.dateToTimestamp(birthday), remainingDays);
        db.userDao().insert(user);
    }


    private static void populateWithTestData(AppDatabase db) {
        db.userDao().deleteAll();

        addUser(db,
                "Gallay", "Robin", "robin.gallay", "AdminHevs01", new Date(1998,02,21),25
        );

        addUser(db,
                "Wenger", "Samuel", "samuel.wenger", "AdminHevs01", new Date(1997,10,05),10
        );

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
