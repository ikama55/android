package ma.ika.memo.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;


public class MemoUtils {
//        "MM-dd-yyyy HH:mm:ss"             ->> 07-10-1996 12:08:56
//        "EEE, MMM d, ''yy"                ->>  Wed, July 10, '96
//        "h:mm a"                          ->>  12:08 PM
//        "hh 'o''clock' a, zzzz"           ->>  12 o'clock PM, Pacific Daylight Time
//        "yyyyy.MMMMM.dd GGG hh:mm aaa"    ->>  01996.July.10 AD 12:08 PM

    public static  String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy");
        String currentDate = sdf.format(calendar.getTime());
        return currentDate;
    }

    public static  String getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String currentDate = dateFormat.format(calendar.getTime());
        return currentDate;
    }

    public static  String getDate(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
        String currentDate = dateFormat.format(time);
        return currentDate;
    }

    public static  String getHour(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String currentDate = dateFormat.format(time);
        return currentDate;
    }

    public static long getCurrentTime(){
        Date date = Calendar.getInstance().getTime();
        return date.getTime();
    }


}
