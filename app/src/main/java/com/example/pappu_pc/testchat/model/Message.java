package com.example.pappu_pc.testchat.model;

import java.io.Serializable;

/**
 * Created by pappu-pc on 6/11/2017.
 */

public class Message implements Serializable {
    String message,to,from,time;

    public Message(String message, String to, String from, String time) {
        this.message = message;
        this.to = to;
        this.from = from;
        this.time = time;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
