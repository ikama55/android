package ma.ika.memo.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ma.ika.memo.R;
import ma.ika.memo.models.Memo;


public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder>
        implements Filterable {
    Context mContext;
    List<Memo> items;
    List<Memo> searchResult;

    public MemoAdapter(Context mContext, List<Memo> items) {
        this.mContext = mContext;
        this.items = items;
        searchResult = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new MemoViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        Memo memo = items.get(position);
        holder.tvDate.setText(memo.getDate());
        holder.tvHour.setText(memo.getHour());
        holder.tvTitle.setText(memo.getTitle());

        holder.tvText.setText(memo.getText());

        if (memo.isLock()) {
            holder.imgViewLock.setVisibility(View.VISIBLE);
            holder.tvText.setVisibility(View.INVISIBLE);
        } else {
            holder.imgViewLock.setVisibility(View.INVISIBLE);
            holder.tvText.setVisibility(View.VISIBLE);
            holder.tvText.setText(memo.getText());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void sortByTitle() {
        Collections.sort(items, new Memo.SortByTitle());
        notifyDataSetChanged();
    }

    public void sortByDate() {
        Collections.sort(items, new Memo.SortByDate());
        notifyDataSetChanged();
    }

    public void refreshList(List<Memo> memos) {
        if (items == null){
            items = new ArrayList<>();
        }
        else{
            items.clear();
        }
        items.addAll(memos);
        notifyDataSetChanged();
    }

    public void addItem(Memo memo) {
        this.items.add(memo);
        notifyDataSetChanged();
    }

    public void toLock(int position, boolean isLock) {
        this.items.get(position).setLock(isLock);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        Memo remove = items.remove(position);
        if (remove != null) {
            notifyItemRemoved(position);
        }
    }

    //---------



    //--------------- Filtre
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Memo> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(searchResult);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Memo item : searchResult) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    //--------------- Fin Filtre

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onUpdateClick(int position);
        void onDeleteClick(int position);
        void onLockClick(int adapterPosition);
        void onUnLockClick(int position);
        void onAddTagClick(int adapterPosition);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // --------

    public class MemoViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvHour, tvTitle, tvText;
        TextView tvPopupMenu;
        ImageView imgViewLock;

        public MemoViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvText = itemView.findViewById(R.id.tvText);

            // Modification
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("DEBUG","-------"+"itemView.setOnClickListener");
                    listener.onUpdateClick(getAdapterPosition());
                }
            });

            //--------------- Menu Popup
            tvPopupMenu = itemView.findViewById(R.id.tvPopuMenu);
            tvPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(tvPopupMenu.getContext(), itemView);
                    popup.inflate(R.menu.popup_menu);
                    popup.setGravity(Gravity.RIGHT);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int position = getAdapterPosition();
                            switch (item.getItemId()) {

                                case R.id.popupMenu_actionDelete:
                                    listener.onDeleteClick(position);
                                    return true;

                                case R.id.popupMenu_action_assingTag:
                                    listener.onAddTagClick(position);
                                    return true;

                                case R.id.popupMenu_action_lock:
                                    listener.onLockClick(position);
                                    return true;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });

            imgViewLock = itemView.findViewById(R.id.imgViewLock);
            imgViewLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUnLockClick(getAdapterPosition());
                }
            });

        }
    }
}
