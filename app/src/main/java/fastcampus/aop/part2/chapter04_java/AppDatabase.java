package fastcampus.aop.part2.chapter04_java;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import fastcampus.aop.part2.chapter04_java.dao.HistoryDao;
import fastcampus.aop.part2.chapter04_java.model.History;

@Database(entities = History.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HistoryDao historyDao();
}
