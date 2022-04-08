package fastcampus.aop.part2.chapter04_java.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class History {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "expression")
    public String expression;

    @ColumnInfo(name = "result")
    public String result;

}
