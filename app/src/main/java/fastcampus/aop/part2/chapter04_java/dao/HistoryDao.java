package fastcampus.aop.part2.chapter04_java.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fastcampus.aop.part2.chapter04_java.model.History;

@Dao
public interface HistoryDao {

    @Query("SELECT * FROM history")
    List<History> getAll();

    @Insert
    void insertHistory(History history);

    @Query("DELETE FROM history")
    void deleteAll();

}
