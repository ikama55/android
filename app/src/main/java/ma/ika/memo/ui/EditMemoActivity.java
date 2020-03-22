package ma.ika.memo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ma.ika.memo.R;
import ma.ika.memo.database.DbAccess;
import ma.ika.memo.models.Memo;

public class EditMemoActivity extends AppCompatActivity {

    EditText edTitle, edText;
    private Context mContext;
    private DbAccess mDbAccess;

    long id=-1;// l'id du mémo à modifier

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        mContext = this;
        mDbAccess = DbAccess.getInstance(mContext);

        edTitle = findViewById(R.id.editTitle);
        edText = findViewById(R.id.editText);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            this.id = intent.getLongExtra("iId",-1);
            String title = intent.getStringExtra("iTitle");
            String text = intent.getStringExtra("iText");
            edTitle.setText(title);
            edText.setText(text);
        }

        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edTitle.getText().toString().toUpperCase().trim();
                String text = edText.getText().toString();
                if (!title.isEmpty()) {
                    mDbAccess.updateMemo(id, title, text,"default");
                    Intent intent = new Intent(EditMemoActivity.this, MainActivity.class);
                    // Fermer l'activity lorsqu'on ouvre une autre
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}
