package com.yash.toodoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yash.toodoo.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CompletedTodoListAdapter extends RecyclerView.Adapter<CompletedTodoListAdapter.CompletedTodoListAdapterViewHolder> {

    private Set<String> mCompletedTodoList = new HashSet<>();

    private CompletedTodoListItemClickListener mClickListener;

    public CompletedTodoListAdapter(CompletedTodoListItemClickListener clickListener){
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public CompletedTodoListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForCompletedTodoListItem = R.layout.completed_todo_list_item;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForCompletedTodoListItem, parent, shouldAttachToParentImmediately);
        return new CompletedTodoListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedTodoListAdapter.CompletedTodoListAdapterViewHolder holder, int position) {
        String completedTodo = getSetItem(position);
        holder.mCompletedTodoListDisplay.setText(completedTodo);
    }

    @Override
    public int getItemCount() {
        return mCompletedTodoList==null ? 0:mCompletedTodoList.size();
    }

    public interface CompletedTodoListItemClickListener{
        void onClickCompletedTodoListItem(String completedTodo);
        void onLongClickCompletedTodoListItem(String completedTodo);
    }

    public void completeTodo(String todo){
        mCompletedTodoList.add(todo);
        notifyDataSetChanged();
    }

    public void completeAllTodo(ArrayList<String> todo) {
        mCompletedTodoList.addAll(todo);
        notifyDataSetChanged();
    }

    public void removeCompletedTodo(String completedTodo){
        mCompletedTodoList.remove(completedTodo);
        notifyDataSetChanged();
    }

    private String getSetItem(int position) {
        int counter = 0;
        String completed = "";
        for(Iterator<String> it=mCompletedTodoList.iterator(); it.hasNext();it.next()) {
            if(counter == position) {
                 completed = it.next();
                 break;
            }
            counter++;
        }
        return completed;
    }


    class CompletedTodoListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView mCompletedTodoListDisplay;
        private TextView mCompletedTodoListLogo;

        public CompletedTodoListAdapterViewHolder(@NonNull View itemView){
            super(itemView);
            mCompletedTodoListDisplay = itemView.findViewById(R.id.tv_completed_todo_list_item);
            mCompletedTodoListLogo = itemView.findViewById(R.id.tv_completed_todo_list_item_logo);

            mCompletedTodoListDisplay.setOnClickListener(this);
            mCompletedTodoListDisplay.setOnLongClickListener(this);

            mCompletedTodoListLogo.setOnClickListener(this);
            mCompletedTodoListLogo.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String completedTodo = getSetItem(adapterPosition);
            mClickListener.onClickCompletedTodoListItem(completedTodo);
        }

        @Override
        public boolean onLongClick(View v) {
            int adapterPosition = getAdapterPosition();
            String completedTodo = getSetItem(adapterPosition);
            mClickListener.onLongClickCompletedTodoListItem(completedTodo);
            return true;
        }
    }

}
