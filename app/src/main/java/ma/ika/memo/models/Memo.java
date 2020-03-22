package ma.ika.memo.models;

import androidx.annotation.NonNull;

import java.util.Comparator;

import ma.ika.memo.utils.MemoUtils;

public class Memo {
    private long id;
    private long time;
    private String title;
    private String text;
    private String tag;
    private boolean lock;

    public Memo(long id, long time, String title, String text, String tag, boolean lock) {
        this.id = id;
        this.time = time;
        this.title = title;
        this.text = text;
        this.tag = tag;
        this.lock = lock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate(){
        return MemoUtils.getDate(this.time);
    }

    public String getHour(){
        return MemoUtils.getHour(this.time);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("id=%d %s %s",this.id, this.title, this.text);
    }

    public static class SortByDate implements Comparator<Memo> {
        @Override
        public int compare(Memo memo1, Memo memo2) {
            if (memo1.time < memo2.time)
                return 1;
            else if (memo1.time > memo2.time)
                return -1;
            else
                return 0;
        }
    }

    public static class SortByTitle implements Comparator<Memo> {
        @Override
        public int compare(Memo memo1, Memo memo2) {
            return memo1.getTitle().compareTo(memo2.getTitle());
        }
    }
}
