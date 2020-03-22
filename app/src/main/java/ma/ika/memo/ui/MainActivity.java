package ma.ika.memo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;

import ma.ika.memo.R;
import ma.ika.memo.adapters.MemoAdapter;
import ma.ika.memo.database.DbAccess;
import ma.ika.memo.models.Memo;
import ma.ika.memo.ui.fragments.PopupMessage;


public class MainActivity extends AppCompatActivity {

    private Context mContext;
    RecyclerView mRecyclerView;
    private DbAccess mDbAccess;
    private MemoAdapter mAdapter;
    private List<Memo> items;

    FloatingActionButton floatingActionBtn;

    private final String TAG = "MainActivity";

    private String passWord = "ika";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionBtn = findViewById(R.id.floatingActionBtn);
        floatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityNewMemoShow();
            }
        });

        mContext = this;
        mDbAccess = DbAccess.getInstance(mContext);

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        items = mDbAccess.getAllMemos();
        mAdapter = new MemoAdapter(mContext, items);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MemoAdapter.OnItemClickListener() {
            @Override
            public void onUpdateClick(int position) {
                activityEditMemoShow(position);
            }

            @Override
            public void onDeleteClick(int position) {
                dialogDeleteConfirme(position);
            }

            @Override
            public void onLockClick(int adapterPosition) {

            }

            @Override
            public void onUnLockClick(int position) {

            }

            @Override
            public void onAddTagClick(int adapterPosition) {

            }
        });
    }

    private void dialogDeleteConfirme(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("SUPRESSION")
                .setMessage("Confirmez la suppression du Mémo.")
                .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Memo item = items.get(position);
                        boolean result = mDbAccess.deleteMemo(item.getId());
                        if (result) {
                            mAdapter.deleteItem((int) item.getId());
                            PopupMessage popupMsg = new PopupMessage(mContext, "INFORMATION", "Suppression effectuée avec succès");
                            popupMsg.show(true);

                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void activityEditMemoShow(int position) {
        Memo memo = items.get(position);
        if (memo != null) {
            Intent intent = new Intent(mContext, EditMemoActivity.class);
            intent.putExtra("iId", memo.getId());
            intent.putExtra("iTitle", memo.getTitle());
            intent.putExtra("iText", memo.getText());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        items = mDbAccess.getAllMemos();
        if (items.size() > 0) {
            mAdapter.refreshList(items);
            findViewById(R.id.tvMemoVide).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.tvMemoVide).setVisibility(View.VISIBLE);
        }
    }

    // affiche l'activity NewMemo
    private void activityNewMemoShow() {
        startActivity(new Intent(mContext, NewMemoActivity.class));
    }

    public void builRecyclerView() {
        items = mDbAccess.getAllMemos();
        if (items.size() > 0) {
            mAdapter = new MemoAdapter(mContext, items);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    // Menu Toobar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.main_menu_action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.main_menu_action_add:
                activityNewMemoShow();
                //Toast.makeText(mContext, "Action ajouter", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.main_menu_action_filter:
                Toast.makeText(mContext, "Action filtrer", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.main_menu_action_sort:
                Toast.makeText(mContext, "Action trier", Toast.LENGTH_SHORT).show();
                dialogSort();
                return true;

//            case R.id.main_menu_action_search:
//                Toast.makeText(mContext, "Action chercher", Toast.LENGTH_SHORT).show();
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dialogSort() {
        String[] items = {"Par date","Par titre"};

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Trier par");
        alertDialog.setIcon(R.drawable.ic_sort);

        alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        mAdapter.sortByDate();
                        break;
                    case 1:
                        mAdapter.sortByTitle();
                }
                dialog.dismiss();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
    // Fin Menu Toobar

}
