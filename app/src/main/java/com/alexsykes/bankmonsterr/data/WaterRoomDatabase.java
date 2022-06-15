package com.alexsykes.bankmonsterr.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Water.class, Parent.class, Marker.class}, version = 1, exportSchema = false)
public abstract class WaterRoomDatabase  extends RoomDatabase {
    public abstract AllDao dao();

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
                AllDao dao = INSTANCE.dao();
                dao.deleteAllWaters();
                dao.deleteAllMarkers();

                Parent parent = new Parent("Aire", "River");
                dao.insertParent(parent);
                parent = new Parent("Wharfe", "River");
                dao.insertParent(parent);
                parent = new Parent("Ure", "River");
                dao.insertParent(parent);
                parent = new Parent("Swale", "River");
                dao.insertParent(parent);
                parent = new Parent("Nidd", "River");
                dao.insertParent(parent);
                parent = new Parent("Calder", "River");
                dao.insertParent(parent);
                parent = new Parent("Leeds and Liverpool", "Canal");
                dao.insertParent(parent);
                parent = new Parent("Lakes", "Lake");
                dao.insertParent(parent);

                Water water = new Water("Buckden","River", 2);
                dao.insertWater(water);
                water = new Water("Burley","River", 2);
                dao.insertWater(water);
                water = new Water("Hubberholme","River", 2);
                dao.insertWater(water);
                water = new Water("Appletreewick","River", 2);
                dao.insertWater(water);
                water = new Water("Gargrave/Brought","River", 1);
                dao.insertWater(water);
                water = new Water("Funkirk/Niffany Farms","River", 1);
                dao.insertWater(water);
                water = new Water("Carlton","River", 1);
                dao.insertWater(water);
                water = new Water("Cononley","River", 1);
                dao.insertWater(water);
                water = new Water("Kildwick","River", 1);
                dao.insertWater(water);
                water = new Water("Kildwick (Eastburn Beck)","River", 1);
                dao.insertWater(water);
                water = new Water("Worton Bridge","River", 3);
                dao.insertWater(water);
                water = new Water("Aysgarth 1","River", 3);
                dao.insertWater(water);
                water = new Water("Aysgarth 2","River", 3);
                dao.insertWater(water);
                water = new Water("Aysgarth 3","River", 3);
                dao.insertWater(water);
                water = new Water("Langthorpe/Roecliffe 1","River", 3);
                dao.insertWater(water);
                water = new Water("Langthorpe/Roecliffe 2","River", 3);
                dao.insertWater(water);
                water = new Water("Lower Dunsforth","River", 3);
                dao.insertWater(water);
                water = new Water("Aysgarth 1","River", 3);
                dao.insertWater(water);
                water = new Water("Aysgarth 2","River", 3);
                dao.insertWater(water);
                water = new Water("Aysgarth 3","River", 3);
                dao.insertWater(water);
                water = new Water("Langthorpe/Roecliffe 1","River", 3);
                dao.insertWater(water);
                water = new Water("Langthorpe/Roecliffe 2","River", 3);
                dao.insertWater(water);
                water = new Water("Lower Dunsforth","River", 3);
                dao.insertWater(water);

                Marker marker= new Marker("Skirbeck","CP","Car park",-2.28000, 54.03128);
                dao.insertMarker(marker);
                marker= new Marker("Skirbeck","LBUS","Limit",-2.28592, 54.03171);
                dao.insertMarker(marker);
                marker= new Marker("Skirbeck","LBDS","Limit",-2.27716, 54.02494);
                dao.insertMarker(marker);
                marker= new Marker("Burley","CP","Car park",-1.76057, 53.91984);
                dao.insertMarker(marker);
                marker= new Marker("Burley","RBDS","Limit",-1.75920, 53.92205);
                dao.insertMarker(marker);
                marker= new Marker("Burley","RBUS","Limit",-1.76784, 53.92357);
                dao.insertMarker(marker);
                marker= new Marker("Gargrave","CP1","Car park",-2.05971, 53.96105);
                dao.insertMarker(marker);
                marker= new Marker("Gargrave","CP2","Car park",-2.101327, 53.982948);
                dao.insertMarker(marker);
            });

        }
    };
}
