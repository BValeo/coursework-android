package com.bvaleo.chatapp.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bvaleo.chatapp.R;
import com.bvaleo.chatapp.model.Message;

import java.util.List;


public class    MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int IS_MINE_MESSAGE = 100;
    public static final int IS_MINE_IMAGE = 101;
    public static final int MESSAGE = 200;
    public static final int IMAGE = 201;

    private final static int TYPE_MESSAGE = 1;
    private final static int TYPE_IMAGE = 2;

    List<Message> messages;

    public MessageAdapter(List<Message> list){
        this.messages = list;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView data;
        TextView time;

        public MessageViewHolder(View view){
            super(view);
            data = (TextView) view.findViewById(R.id.txtMsg);
            time = (TextView) view.findViewById(R.id.txtTime);
        }

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView data;
        TextView time;

        public ImageViewHolder(View view){
            super(view);
           // data = (ImageView) view.findViewById();
            time = (TextView) view.findViewById(R.id.txtTime);
        }

    }

    @Override
    public int getItemViewType(int position) {

        int type = messages.get(position).getType();
        boolean isMine = messages.get(position).isMine();

        if(isMine){
            if(type == TYPE_MESSAGE) return IS_MINE_MESSAGE;
            if(type == TYPE_IMAGE) return IS_MINE_IMAGE;
        } else {
            if(type == TYPE_MESSAGE) return MESSAGE;
            if(type == TYPE_IMAGE) return IMAGE;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);


        ((MessageViewHolder) holder).data.setText(message.getMessage());
        ((MessageViewHolder) holder).time.setText(message.getTime());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType){
            case IS_MINE_MESSAGE:
                v = inflater.inflate(R.layout.my_message, viewGroup, false);
                return new MessageViewHolder(v);
            case IS_MINE_IMAGE:
                v = inflater.inflate(R.layout.my_image, viewGroup, false);
                return new ImageViewHolder(v);
            case MESSAGE:
                v = inflater.inflate(R.layout.message, viewGroup, false);
                return new MessageViewHolder(v);
            case IMAGE:
                v = inflater.inflate(R.layout.image, viewGroup, false);
                return new ImageViewHolder(v);

            default:
                return null;
        }
    }


    public int getItemCount() {
        return messages.size();
    }
}
