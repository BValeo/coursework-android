package com.bvaleo.chatapp.model;

import java.util.List;
import java.util.UUID;

public class Dialoge {

    private UUID id;
    private String name;
    private String lastMessage;
    private String time;
    private int unReadCount;

    List<Message> messages;

    public Dialoge(){
        this.id = UUID.randomUUID();
    }

    public Dialoge(String name, String lastMessage, String time, int unReadCount, List<Message> messages) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.unReadCount = unReadCount;
        this.messages = messages;
    }

    public String getId() {
        return id.toString();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
}
