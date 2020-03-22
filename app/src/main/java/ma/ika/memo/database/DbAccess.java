package ma.ika.memo.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ma.ika.memo.models.Memo;

public class DbAccess {

    private static DbAccess INSTANCE = null;

    MemoHelper dbHelper;
    SQLiteDatabase database;

    private DbAccess(Context context) {
        super();
        this.dbHelper = new MemoHelper(context);
        this.database = dbHelper.getWritableDatabase();
    }


    public static DbAccess getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DbAccess(context);
        }
        return INSTANCE;
    }

    public boolean insertMemo(Memo memo) {
        ContentValues values = new ContentValues();
        values.put(MemoHelper.COL_TIME, memo.getTime());
        values.put(MemoHelper.COL_TITLE, memo.getTitle());
        values.put(MemoHelper.COL_TEXT, memo.getText());
        values.put(MemoHelper.COL_TAG, memo.getTag());
        int lock = memo.isLock() ? 1 : 0;
        values.put(MemoHelper.COL_LOCK, lock);
        long result = database.insert(MemoHelper.TB_MEMO, null, values);
        return (result != -1);
    }

    public boolean updateMemo(long id, String title, String text, String tag) {
        ContentValues values = new ContentValues();
        values.put(MemoHelper.COL_TITLE, title);
        values.put(MemoHelper.COL_TEXT, text);
        values.put(MemoHelper.COL_TAG, tag);
        long result = database.update(MemoHelper.TB_MEMO, values,
                MemoHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
        return (result > 0);
    }

    public boolean deleteMemo(long id) {
        int delete = database.delete(MemoHelper.TB_MEMO, MemoHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
        return (delete > 0);
    }

    public List<Memo> getAllMemos() {
        List<Memo> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from " + MemoHelper.TB_MEMO,
                null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToMemo(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }


    /**
     * Vérrouille ou Déverrouille le mémo dont l'identifiant est passé en paramètre
     * @param id
     */
    public boolean toLock(long id, boolean isLock) {
        ContentValues values = new ContentValues();
        if (isLock) {
            values.put(MemoHelper.COL_LOCK, 1);
        } else {
            values.put(MemoHelper.COL_LOCK, 0);
        }
        long result = database.update(MemoHelper.TB_MEMO, values,
                MemoHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
        return (result > 0);
    }

    private Memo cursorToMemo(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(MemoHelper.COL_ID));
        long time = cursor.getLong(cursor.getColumnIndexOrThrow(MemoHelper.COL_TIME));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MemoHelper.COL_TITLE));
        String text = cursor.getString(cursor.getColumnIndexOrThrow(MemoHelper.COL_TEXT));
        String tag = cursor.getString(cursor.getColumnIndexOrThrow(MemoHelper.COL_TAG));
        int lock = cursor.getInt(cursor.getColumnIndexOrThrow(MemoHelper.COL_LOCK));
        boolean isLock = (lock == 1)? true : false;
        return new Memo(id, time, title, text, tag, isLock);

    }

    //-------- Gestion table TAG
    public long insertTag(String tag) {
        ContentValues values = new ContentValues();
        values.put(MemoHelper.COL_TAG_TEXT, tag);
        long result = database.insert(MemoHelper.TB_TAG, null, values);
        return result;
    }

    public boolean updateTag(long id, String newText) {
        ContentValues values = new ContentValues();
        values.put(MemoHelper.COL_TAG_TEXT, newText);
        long result = database.update(MemoHelper.TB_TAG, values,
                MemoHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
        return (result > 0);
    }

    public boolean deleteTag(long id) {
        int delete = database.delete(MemoHelper.TB_TAG, MemoHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
        return (delete > 0);
    }


    public List<String> getAllTags() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from " + MemoHelper.TB_TAG,null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MemoHelper.COL_TAG_ID));
                String tag = cursor.getString(cursor.getColumnIndexOrThrow(MemoHelper.COL_TAG_TEXT));
                list.add(tag);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<Memo> getTags(String[] tags) {
        List<Memo> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from " +
                MemoHelper.TB_MEMO,null);
        if (cursor.moveToFirst()) {
            do {
                Memo memo = cursorToMemo(cursor);
                for(int i=0; i<tags.length; i++) {
                    if (memo.getTag().equals(tags[i])) {
                        list.add(memo);
                        break;
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * Associe le tag au mémo
     * @param id l'identifiant du mémo
     * @param tag le nom du tag
     * @return true si réussite
     */
    public boolean setTag(long id, String tag) {
        ContentValues values = new ContentValues();
        values.put(MemoHelper.COL_TAG, tag);
        long result = database.update(MemoHelper.TB_MEMO, values,
                MemoHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
        return (result > 0);
    }
}
