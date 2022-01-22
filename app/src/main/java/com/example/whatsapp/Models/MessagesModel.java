package com.example.whatsapp.Models;

public class MessagesModel {

    String messagerId,message,messageId;
    Long timeStamp;

    public MessagesModel(String messagerId, String message, Long timeStamp) {
        this.messagerId = messagerId;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public MessagesModel(String messagerId, String message) {
        this.messagerId = messagerId;
        this.message = message;
    }

    public MessagesModel(){}

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessagerId() {
        return messagerId;
    }

    public void setMessagerId(String messagerId) {
        this.messagerId = messagerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
