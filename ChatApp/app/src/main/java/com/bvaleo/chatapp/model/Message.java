package com.bvaleo.chatapp.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Message implements Parcelable{

    public final static int TYPE_MESSAGE = 1;
    public final static int TYPE_IMAGE = 2;

    private int type;
    private String username;
    private String toUsername;
    private String message;
    private String time;
    private boolean isMine;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public Message(){}

    public Message(Parcel in){
        type = in.readInt();
        username = in.readString();
        toUsername = in.readString();
        message = in.readString();
        time = in.readString();
        isMine = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Message> CREATOR
            = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(username);
        dest.writeString(toUsername);
        dest.writeString(message);
        dest.writeString(time);
        dest.writeByte((byte) (isMine ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mToUsername;
        private String mMessage;
        private String mTime;
        private boolean mIsMine;

        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder toUsername(String toUsername) {
            mUsername = toUsername;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder isMine(boolean isMine){
            mIsMine = isMine;
            return this;
        }

        public Builder time(String time){
            mTime = time;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.setType(mType);
            message.setUsername(mUsername);
            message.setToUsername(mToUsername);
            message.setMessage(mMessage);
            message.setTime(mTime);
            message.setMine(mIsMine);
            return message;
        }
    }
}
