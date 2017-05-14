package com.bvaleo.chatapp.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bvaleo.chatapp.R;
import com.bvaleo.chatapp.model.Dialoge;

import java.util.List;


public class DialogeAdapter extends RecyclerView.Adapter<DialogeAdapter.MyViewHolder> {

    private List<Dialoge> list;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView nickname;
        public TextView message;
        public TextView time;
        public TextView count;

        public MyViewHolder(View view) {
            super(view);
            nickname = (TextView) view.findViewById(R.id.nicknameD);
            message = (TextView) view.findViewById(R.id.lastmessageD);
            time = (TextView) view.findViewById(R.id.timedispatchD);
            count = (TextView) view.findViewById(R.id.count);
        }
    }

    public DialogeAdapter(List<Dialoge> dialoges){
        this.list = dialoges;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialoge_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Dialoge dialoge = list.get(position);

        holder.nickname.setText(dialoge.getName());
        holder.message.setText(dialoge.getLastMessage());
        if (dialoge.getUnReadCount() > 0) {
            holder.count.setText(String.valueOf(dialoge.getUnReadCount()));
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }
        holder.time.setText(dialoge.getTime());
    }
}
