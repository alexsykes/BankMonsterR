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

@Database(entities = {Water.class, Parent.class}, version = 1, exportSchema = false)
public abstract class WaterRoomDatabase  extends RoomDatabase {

    public abstract WaterDao waterDao();
    public abstract ParentDao parentDao();

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
                WaterDao waterDao = INSTANCE.waterDao();
                ParentDao parentDao = INSTANCE.parentDao();
                waterDao.deleteAll();
                parentDao.deleteAll();

                Parent parent = new Parent("Aire", "River");
                parentDao.insert(parent);
                parent = new Parent("Wharfe", "River");
                parentDao.insert(parent);
                parent = new Parent("Ure", "River");
                parentDao.insert(parent);
                parent = new Parent("Swale", "River");
                parentDao.insert(parent);
                parent = new Parent("Nidd", "River");
                parentDao.insert(parent);
                parent = new Parent("Calder", "River");
                parentDao.insert(parent);
                parent = new Parent("Leeds and Liverpool", "Canal");
                parentDao.insert(parent);
                parent = new Parent("Lakes", "Lake");
                parentDao.insert(parent);

                Water water = new Water("Buckden","Wharfe");
                waterDao.insert(water);
                water = new Water("Burley-in-Wharfedale","Wharfe");
                waterDao.insert(water);
                water = new Water("Hubberholme","Wharfe");
                waterDao.insert(water);
                water = new Water("Appletreewick","Wharfe");
                waterDao.insert(water);
            });

        }
    };
}
