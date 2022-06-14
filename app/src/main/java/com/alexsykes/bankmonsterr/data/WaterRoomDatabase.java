package com.alexsykes.bankmonsterr.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Water.class}, version = 1, exportSchema = false)
public abstract class WaterRoomDatabase  extends RoomDatabase {

    public abstract WaterDao waterDao();

    private static volatile WaterRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static WaterRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WaterRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    WaterRoomDatabase.class, "water_database")
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                WaterDao dao = INSTANCE.waterDao();
                dao.deleteAll();

                Water water = new Water("Aire");
                dao.insert(water);
                water = new Water("Wharfe");
                dao.insert(water);

            });

        }
    };
}
