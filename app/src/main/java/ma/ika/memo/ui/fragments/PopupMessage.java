package ma.ika.memo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;

import androidx.appcompat.app.AlertDialog;

public class PopupMessage  {

    Context mContext;
    String title;
    String message;

    AlertDialog.Builder builder;

    public PopupMessage(Context context, String title, String message) {
        this.mContext = context;
        this.title = title;
        this.message = message;
        builder = new AlertDialog.Builder(context);
        //builder.setIcon(R.drawable.ic_local_offer_black_24dp);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public PopupMessage(Context context) {
        this.mContext = context;
        this.title = "";
        this.message = "";
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void show(boolean vibration){
        if (vibration) {
            String vibratorService = mContext.VIBRATOR_SERVICE;
            Vibrator vibrator = (Vibrator) mContext.getSystemService(vibratorService);
            vibrator.vibrate(300);
        }
        builder.show();
    }
}

