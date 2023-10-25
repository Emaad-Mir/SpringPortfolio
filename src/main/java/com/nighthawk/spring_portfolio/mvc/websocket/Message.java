package com.nighthawk.spring_portfolio.mvc.websocket;

public class Message {

    private String senderName;
    private String targetUserName;
    private String message;

    // Constructors, getters, setters, etc.
    public Message() {
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
