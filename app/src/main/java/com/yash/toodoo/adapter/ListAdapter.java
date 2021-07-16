package com.yash.toodoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yash.toodoo.AddTodoActivity;
import com.yash.toodoo.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListAdapterViewHolder> {

    private Set<String> mLists = new HashSet<>();

    private Context context;

    private ListItemOnLongClickListener mLongClickListener;

    public ListAdapter(Context context, ListItemOnLongClickListener longClickListener){
        this.context = context;
        mLongClickListener = longClickListener;
    }

    public interface ListItemOnLongClickListener{
        void onListItemLongClick(String list);
    }

    @NonNull
    @Override
    public ListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterViewHolder holder, int position) {
        String list = getSetItem(position);
        holder.mListItemTextView.setText(list);
    }

    @Override
    public int getItemCount() {
        return mLists==null ? 0: mLists.size();
    }

    public void addNewList(String list){
        mLists.add(list);
        notifyDataSetChanged();
    }

    public void addAllList(ArrayList<String> list){
        mLists.addAll(list);
        notifyDataSetChanged();
    }

    public void removeList(String list){
        mLists.remove(list);
        notifyDataSetChanged();
    }

    protected String getSetItem(int position){
        String list = "";
        int counter = 0;
        for (Iterator<String> it=mLists.iterator(); it.hasNext();it.next()){
            if(counter==position){
                list = it.next();
                break;
            }
            counter++;
        }
        return list;
    }


    class ListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public final TextView mListItemTextView;

        public ListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mListItemTextView = itemView.findViewById(R.id.tv_list_item);
            mListItemTextView.setOnClickListener(this);
            mListItemTextView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String list = getSetItem(adapterPosition);
            Intent addTodoIntent = new Intent(context, AddTodoActivity.class);
            addTodoIntent.putExtra(Intent.EXTRA_TEXT, list);
            context.startActivity(addTodoIntent);
        }

        @Override
        public boolean onLongClick(View v) {
            int adapterPosition = getAdapterPosition();
            String list = getSetItem(adapterPosition);
            mLongClickListener.onListItemLongClick(list);
            return true;
        }
    }
}
