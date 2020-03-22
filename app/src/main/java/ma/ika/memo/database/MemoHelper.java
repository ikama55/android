package ma.ika.memo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MemoHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "data.db";
    private static final int DB_VERSION = 1;

    public static final String TB_MEMO = "memo";
    public static final String COL_ID = "_id";
    public static final String COL_TIME = "time";
    public static final String COL_TITLE = "title";
    public static final String COL_TEXT = "text";
    public static final String COL_TAG = "tag";
    public static final String COL_LOCK = "lock";


    public static final String TB_TAG = "tags";
    public static final String COL_TAG_ID = "_id";
    public static final String COL_TAG_TEXT = "tag";


    private static final String TB_MEMO_CREATE = "CREATE TABLE " + TB_MEMO +
            "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_TIME + " INTEGER NOT NULL," +
            COL_TITLE + " TEXT NOT NULL," +
            COL_TEXT + " TEXT NOT NULL," +
            COL_TAG + " TEXT NOT NULL," +
            COL_LOCK + " INTEGER NOT NULL" +
            ")";

    private static final String TB_TAG_CREATE = "CREATE TABLE " + TB_TAG +
            "(" +
            COL_TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_TAG_TEXT + " TEXT NOT NULL" +
            ")";

    public MemoHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TB_MEMO_CREATE);
        db.execSQL(TB_TAG_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TB_MEMO);
        db.execSQL("DROP TABLE " + TB_TAG);
        onCreate(db);
    }
}
