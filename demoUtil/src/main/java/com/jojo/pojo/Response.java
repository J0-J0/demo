package com.jojo.pojo;

public class Response {

    public static final int SUCCESS = 1;
    public static final int FAIL = 0;

    private int code;
    private String message;
    private Object data;

    public Response() {
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setFailMessage(String message) {
        this.code = FAIL;
        this.message = message;
    }

    public void setSuccessMessage(String message) {
        this.code = SUCCESS;
        this.message = message;
    }

    public Response appendMessage(String message) {
        this.message += message;
        return this;
    }

    public static Response succeed(String message) {
        Response response = new Response();
        response.code = SUCCESS;
        response.message = message;
        return response;
    }
}
