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
    public abstract MarkerDao dao();
    public abstract WaterDao wdao();
    public abstract ParentDao pdao();

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
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                MarkerDao dao = INSTANCE.dao();
                WaterDao wdao = INSTANCE.wdao();
                ParentDao pdao = INSTANCE.pdao();
                wdao.deleteAllWaters();
                dao.deleteAllMarkers();

                Parent parent = new Parent("Aire", "River");
                pdao.insertParent(parent);
                parent = new Parent("Wharfe", "River");
                pdao.insertParent(parent);
                parent = new Parent("Ure", "River");
                pdao.insertParent(parent);
                parent = new Parent("Swale", "River");
                pdao.insertParent(parent);
                parent = new Parent("Nidd", "River");
                pdao.insertParent(parent);
                parent = new Parent("Calder", "River");
                pdao.insertParent(parent);
                parent = new Parent("Leeds and Liverpool", "Canal");
                pdao.insertParent(parent);
                parent = new Parent("Lakes", "Lake");
                pdao.insertParent(parent);
                parent = new Parent("Ribble", "River");
                pdao.insertParent(parent);

                Water water = new Water("Buckden","River", 2);
                wdao.insertWater(water);
                water = new Water("Burley","River", 2);
                wdao.insertWater(water);
                water = new Water("Hubberholme","River", 2);
                wdao.insertWater(water);
                water = new Water("Appletreewick","River", 2);
                wdao.insertWater(water);
                water = new Water("Gargrave/Broughton","River", 1);
                wdao.insertWater(water);
                water = new Water("Funkirk/Niffany Farms","River", 1);
                wdao.insertWater(water);
                water = new Water("Carlton","River", 1);
                wdao.insertWater(water);
                water = new Water("Cononley","River", 1);
                wdao.insertWater(water);
                water = new Water("Kildwick","River", 1);
                wdao.insertWater(water);
                water = new Water("Kildwick (Eastburn Beck)","River", 1);
                wdao.insertWater(water);
                water = new Water("Worton Bridge","River", 3);
                wdao.insertWater(water);
                water = new Water("Aysgarth 1","River", 3);
                wdao.insertWater(water);
                water = new Water("Aysgarth 2","River", 3);
                wdao.insertWater(water);
                water = new Water("Aysgarth 3","River", 3);
                wdao.insertWater(water);
                water = new Water("Langthorpe/Roecliffe 1","River", 3);
                wdao.insertWater(water);
                water = new Water("Langthorpe/Roecliffe 2","River", 3);
                wdao.insertWater(water);
                water = new Water("Lower Dunsforth","River", 3);
                wdao.insertWater(water);
                water = new Water("Aysgarth 1","River", 3);
                wdao.insertWater(water);
                water = new Water("Aysgarth 2","River", 3);
                wdao.insertWater(water);
                water = new Water("Aysgarth 3","River", 3);
                wdao.insertWater(water);
                water = new Water("Langthorpe/Roecliffe 1","River", 3);
                wdao.insertWater(water);
                water = new Water("Langthorpe/Roecliffe 2","River", 3);
                wdao.insertWater(water);
                water = new Water("Lower Dunsforth","River", 3);
                wdao.insertWater(water);
                water = new Water("Oakworth Lakes","Lake", 8);
                wdao.insertWater(water);
                water = new Water("Staveley Lakes","Lake", 8);
                wdao.insertWater(water);
                water = new Water("Shipton Lake","Lake", 8);
                wdao.insertWater(water);
                water = new Water("Bingley/Shipley","Canal", 7);
                wdao.insertWater(water);
                water = new Water("Shipley/Esholt","Canal", 7);
                wdao.insertWater(water);
                water = new Water("Esholt","Canal", 7);
                wdao.insertWater(water);
                water = new Water("Skirbeck","River", 9);
                wdao.insertWater(water);


                // Buckden
                Marker marker= new Marker("CP","Car park",1, -2.089700,   54.191975);
                dao.insertMarker(marker);
                marker= new Marker("RBUS","Buckden 1",1, -2.10315,   54.19753);
                dao.insertMarker(marker);
                marker= new Marker("RBDS","Buckden 1",1, -2.09683,   54.19540);
                dao.insertMarker(marker);
                marker= new Marker("LBUS","Buckden 2",1, -2.09766,   54.19672);
                dao.insertMarker(marker);
                marker= new Marker("LBDS","Buckden 2",1, -2.09527,   54.19300);
                dao.insertMarker(marker);
                marker= new Marker("RBUS","Buckden 3",1, -2.09556,   54.19100);
                dao.insertMarker(marker);
                marker= new Marker("RBDS","Buckden 3",1, -2.08542,   54.17877);
                dao.insertMarker(marker);
                marker= new Marker("LBUS","Buckden 4",1, -2.09347,   54.18750);
                dao.insertMarker(marker);
                marker= new Marker("LBDS","Buckden 4",1, -2.09290,   54.18644);
                dao.insertMarker(marker);
                marker= new Marker("RBDS","Buckden 5",1, -2.08333,   54.17602);
                dao.insertMarker(marker);
                marker= new Marker("RBUS","Buckden 5",1, -2.08465,   54.17738);
                dao.insertMarker(marker);

                // Hubberholme
                marker= new Marker("CP","Car park",3, -2.113885, 54.199842);
                dao.insertMarker(marker);

                // Appletreewick
                marker= new Marker("CP","Car park",4, -1.929755, 54.037724);
                dao.insertMarker(marker);
                marker= new Marker("LBUS","Appletreewick",4, -1.93441,   54.03894);
                dao.insertMarker(marker);
                marker= new Marker("LBDS","Appletreewick",4, -1.93075,   54.03671);
                dao.insertMarker(marker);


                // Skirbeck
                 marker= new Marker("CP","Car park",30, -2.28007, 54.03128);
                dao.insertMarker(marker);
                marker= new Marker("LBUS","Limit",30, -2.28592, 54.03171); // Skirbeck
                dao.insertMarker(marker);
                marker= new Marker("LBDS","Limit",30, -2.27716, 54.02494);
                dao.insertMarker(marker);

                // Burley
                marker= new Marker("CP","Car park",2, -1.76057, 53.91984);
                dao.insertMarker(marker);
                marker= new Marker("RBDS","Limit",2, -1.75920, 53.92205);
                dao.insertMarker(marker);
                marker= new Marker("RBUS","Limit",2, -1.76784, 53.92357);
                dao.insertMarker(marker);

                // Gargrave
                marker= new Marker("CP1","Car park",5, -2.10640, 53.98305);
                dao.insertMarker(marker);
                marker= new Marker("CP2","Car park",5, -2.05971, 53.96105);
                dao.insertMarker(marker);
                marker= new Marker("RBUS","Limit",5, -2.10443, 53.98260);
                dao.insertMarker(marker);
                marker= new Marker("RBDS","Limit",5, -2.06434, 53.96480);
                dao.insertMarker(marker);
                marker= new Marker("LBUS","Limit",5, -2.08204, 53.97757);
                dao.insertMarker(marker);
                marker= new Marker("LBDS","Limit",5, -2.05741, 53.95937);
                dao.insertMarker(marker);
            });
        }
    };
}
