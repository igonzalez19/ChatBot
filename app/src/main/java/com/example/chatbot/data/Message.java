package com.example.chatBot.data;

public class Message
{
    private boolean outcoming;
    private String message, time;

    public Message(boolean outcoming, String message, String time) {
        this.outcoming = outcoming;
        this.message = message;
        this.time = time;
    }

    public boolean isOutcoming() {
        return outcoming;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
