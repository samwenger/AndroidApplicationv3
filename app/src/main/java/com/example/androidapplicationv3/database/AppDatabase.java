package com.example.androidapplicationv3.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.example.androidapplicationv3.database.dao.RequestDao;
import com.example.androidapplicationv3.database.dao.StatusDao;
import com.example.androidapplicationv3.database.dao.TypeDao;
import com.example.androidapplicationv3.database.dao.UserDao;
import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.entity.StatusEntity;
import com.example.androidapplicationv3.database.entity.TypeEntity;
import com.example.androidapplicationv3.database.entity.UserEntity;

import java.util.concurrent.Executors;


@Database(entities = {RequestEntity.class, StatusEntity.class, TypeEntity.class, UserEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";

    private static AppDatabase instance;

    private static final String DATABASE_NAME = "vacationrequestDB";

    public abstract RequestDao requestDao();

    public abstract UserDao userDao();

    public abstract StatusDao statusDao();

    public abstract TypeDao typesDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context.getApplicationContext());
                    instance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext) {
        Log.i(TAG, "Database will be initialized.");
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            AppDatabase database = AppDatabase.getInstance(appContext);
                            initializeDemoData(database);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                }).build();
    }

    public static void initializeDemoData(final AppDatabase database) {
        Executors.newSingleThreadExecutor().execute(() -> {
            database.runInTransaction(() -> {
                Log.i(TAG, "Wipe database.");
                database.userDao().deleteAll();
                database.typesDao().deleteAll();
                database.statusDao().deleteAll();

                DatabaseInitializer.populateDatabase(database);
            });
        });
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            Log.i(TAG, "Database initialized.");
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
