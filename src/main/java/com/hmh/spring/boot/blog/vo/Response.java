package com.hmh.spring.boot.blog.vo;

/**
 * 封装响应，最终RESTful接口返回
 *
 * @author hmh
 * @date 2019/3/6
 */
public class Response {

    /**
     * 响应状态（成功或者失败）
     */
    private boolean success;
    /**
     * 响应状态描述
     */
    private String message;
    /**
     * 响应数据体
     */
    private Object body;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Response(boolean success, String message, Object body) {
        this.success = success;
        this.message = message;
        this.body = body;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
