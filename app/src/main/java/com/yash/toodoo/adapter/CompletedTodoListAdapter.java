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

public class CompletedTodoListAdapter extends RecyclerView.Adapter<CompletedTodoListAdapter.CompletedTodoListAdapterViewHolder> {

    private ArrayList<String> mCompletedTodoList = new ArrayList<>();

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
        String completedTodo = mCompletedTodoList.get(position);
        holder.mCompletedTodoListDisplay.setText(completedTodo);
    }

    @Override
    public int getItemCount() {
        return mCompletedTodoList==null ? 0:mCompletedTodoList.size();
    }

    public interface CompletedTodoListItemClickListener{
        void onClickCompletedTodoListItem(String completedTodo);
    }

    public void completeTodo(String todo){
        mCompletedTodoList.add(todo);
        notifyDataSetChanged();
    }

    public void removeCompletedTodo(String completedTodo){
        mCompletedTodoList.remove(completedTodo);
        notifyDataSetChanged();
    }


    class CompletedTodoListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mCompletedTodoListDisplay;
        private TextView mCompletedTodoListLogo;

        public CompletedTodoListAdapterViewHolder(@NonNull View itemView){
            super(itemView);
            mCompletedTodoListDisplay = itemView.findViewById(R.id.tv_completed_todo_list_item);
            mCompletedTodoListLogo = itemView.findViewById(R.id.tv_completed_todo_list_item_logo);

            mCompletedTodoListDisplay.setOnClickListener(this);
            mCompletedTodoListLogo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String completedTodo = mCompletedTodoList.get(adapterPosition);
            mClickListener.onClickCompletedTodoListItem(completedTodo);
        }
    }

}
