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

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListAdapterViewHolder> {

    private Set<String> mTodoList = new HashSet<>();

    private TodoListItemClickListener mClickListener;

    public TodoListAdapter(TodoListItemClickListener clickListener){
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public TodoListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForTodoListItem = R.layout.todo_list_item;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForTodoListItem, parent, shouldAttachToParentImmediately);
        return new TodoListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.TodoListAdapterViewHolder holder, int position) {
        String todo = getSetItem(position);
        holder.mTodoListItemDisplay.setText(todo);
    }

    @Override
    public int getItemCount() {
        return mTodoList==null ? 0:mTodoList.size();
    }

    public interface TodoListItemClickListener{
        void onClickTodoListItem(String todo);
    }

    public void addNewTodo(String todo){
        mTodoList.add(todo);
        notifyDataSetChanged();
    }

    public void addAllToDo(ArrayList<String> todos) {
        mTodoList.addAll(todos);
        notifyDataSetChanged();
    }

    public void completeTodo(String todo){
        mTodoList.remove(todo);
        notifyDataSetChanged();
    }

    private String getSetItem(int position) {
        int counter = 0;
        String todo = "";
        for(Iterator<String> it=mTodoList.iterator(); it.hasNext();it.next()) {
            if(counter == position) {
                todo = it.next();
                break;
            }
            counter++;
        }
        return todo;
    }

    class TodoListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTodoListItemDisplay;
        private TextView mTodoListItemLogo;

        public TodoListAdapterViewHolder(@NonNull View itemView){
            super(itemView);
            mTodoListItemDisplay = itemView.findViewById(R.id.tv_todo_list_item);
            mTodoListItemLogo = itemView.findViewById(R.id.tv_todo_list_item_logo);

            mTodoListItemDisplay.setOnClickListener(this);
            mTodoListItemLogo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String todo = getSetItem(adapterPosition);
            mClickListener.onClickTodoListItem(todo);
        }
    }

}
