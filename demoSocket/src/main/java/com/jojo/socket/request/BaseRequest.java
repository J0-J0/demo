package com.jojo.socket.request;

public class BaseRequest {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String pack() {
        return message;
    }
}
