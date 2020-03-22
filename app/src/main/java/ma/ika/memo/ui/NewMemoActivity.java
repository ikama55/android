package ma.ika.memo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import ma.ika.memo.R;
import ma.ika.memo.database.DbAccess;
import ma.ika.memo.models.Memo;
import ma.ika.memo.ui.fragments.PopupMessage;
import ma.ika.memo.utils.MemoUtils;

public class NewMemoActivity extends AppCompatActivity {

    EditText edTitle, edText;
    private Context mContext;
    private DbAccess mDbAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memo);

        mContext = this;
        mDbAccess = DbAccess.getInstance(mContext);

        edTitle = findViewById(R.id.newTitle);
        edText = findViewById(R.id.newText);

        findViewById(R.id.newBtnMemo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edTitle.getText().toString().toUpperCase().trim();
                String text = edText.getText().toString();
                if (!title.isEmpty()) {
                    PopupMessage popupMsg = new PopupMessage(mContext);
                    long time = MemoUtils.getCurrentTime();
                    Memo memo = new Memo(-1, time, title, text,"default",false);
                    boolean result = mDbAccess.insertMemo(memo);
                    if (result) {
                        popupMsg.setMessage("Insertion du nouvel Mémo avec succès ");
                        popupMsg.show(true);
                    }else{
                        popupMsg.setMessage("Echec d'Insertion du mémo !");
                        popupMsg.show(true);
                    }
                    Intent intent = new Intent(mContext, MainActivity.class);
                    // Fermer l'activity lorsqu'on ouvre une autre
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}
