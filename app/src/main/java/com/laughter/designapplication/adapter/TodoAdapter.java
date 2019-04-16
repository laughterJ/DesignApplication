package com.laughter.designapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.R;
import com.laughter.designapplication.activity.TodoListActivity;
import com.laughter.designapplication.model.TodoItem;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.framework.util.SpUtil;
import com.laughter.framework.util.ToastUtil;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 江浩
 * 创建时间： 2019/4/15
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.designapplication.adapter
 */
public class TodoAdapter extends BaseAdapter implements HttpCallbackListener {

    private Context mContext;
    private List<TodoItem> todoItems;

    public TodoAdapter(Context mContext, List<TodoItem> todoItems) {
        this.mContext = mContext;
        this.todoItems = todoItems;
    }

    @Override
    public int getCount() {
        return todoItems.size();
    }

    @Override
    public Object getItem(int position) {
        return todoItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItem todoItem = todoItems.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_todo_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTodoTitle.setText(todoItem.getTitle());
        holder.tvTodoDeadline.setText(String.format(holder.deadline, todoItem.getDateStr()));
        String priority = null;
        if (todoItem.getPriority() == 1){
            priority = "紧急";
        }else if (todoItem.getPriority() == 2){
            priority = "一般";
        }else if (todoItem.getPriority() == 3){
            priority = "暂缓";
        }
        holder.tvTodoPriority.setText(String.format(holder.priority, priority));

        if (todoItem.getStatus() == 0){
            holder.cbTodoFinish.setChecked(false);
            holder.tvTodoTitle.getPaint().setFlags(0);
        }else {
            holder.cbTodoFinish.setChecked(true);
            holder.tvTodoTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        String localCookie = SpUtil.getString(mContext, "Cookie", null);
        holder.cbTodoFinish.setOnClickListener(v -> {
            ((TodoListActivity)mContext).showLoading();
            String path = "lg/todo/done/" + todoItem.getId() + "/json";
            JsonObject params = new JsonObject();
            params.addProperty("status", todoItem.getStatus() == 0 ? 1: 0);
            HttpUtil.post(path, 0, params, localCookie, false, this);
        });
        holder.imgTodoDelete.setOnClickListener(v -> {
            ((TodoListActivity)mContext).showLoading();
            String path = "lg/todo/delete/" +todoItem.getId() + "/json";
            HttpUtil.post(path, 1, null, localCookie, false, this);
        });

        holder.itemView.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putBoolean("isAdded", true);
            args.putInt("id", todoItem.getId());
            args.putString("title", todoItem.getTitle());
            args.putString("deadline", todoItem.getDateStr());
            args.putInt("priority", todoItem.getPriority());
            ((TodoListActivity)mContext).showFragment(args);
        });

        return convertView;
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        ((Activity)mContext).runOnUiThread(() -> {
            if (JsonUtil.getErrorCode(response) == 0){
                ((TodoListActivity)mContext).updateList();
            }else {
                ToastUtil.showShortToast(mContext, JsonUtil.getErrorMsg(response));
            }
        });
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }

    class ViewHolder {
        @BindView(R.id.todo_finish) CheckBox cbTodoFinish;
        @BindView(R.id.todo_title) TextView tvTodoTitle;
        @BindView(R.id.todo_delete) ImageView imgTodoDelete;
        @BindView(R.id.todo_deadline) TextView tvTodoDeadline;
        @BindView(R.id.todo_priority) TextView tvTodoPriority;

        @BindString(R.string.deadline_text) String deadline;
        @BindString(R.string.priority_text) String priority;

        View itemView;
        public ViewHolder(View itemView){
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
