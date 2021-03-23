package com.jojo.socket.response;

public class BaseResponse {

    public static final int FAIL = 0;
    public static final int SUCCESS = 1;

    private int code;
    private String message;

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

    public static BaseResponse fail(String message) {
        BaseResponse response = new BaseResponse();
        response.setCode(BaseResponse.FAIL);
        response.setMessage(message);
        return response;
    }

    public static BaseResponse success(String message) {
        BaseResponse response = new BaseResponse();
        response.setCode(BaseResponse.SUCCESS);
        response.setMessage(message);
        return response;
    }

    public static BaseResponse unPack() {
        return null;
    }
}
