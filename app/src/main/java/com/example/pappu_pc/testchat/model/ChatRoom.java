package com.example.pappu_pc.testchat.model;

import java.io.Serializable;

/**
 * Created by pappu-pc on 6/11/2017.
 */

public class ChatRoom implements Serializable {
    String name,lastMessage,time,unreadCount;

    public ChatRoom(String name, String lastMessage, String time, String unreadCount) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.unreadCount = unreadCount;
    }

    public ChatRoom() {
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

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }
}
