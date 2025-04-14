package com.intelligent.common;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class RestResponse<T> implements Serializable {


    public static final String successCode = "000000";
    public static final String failedCode = "999999";
    private static final long serialVersionUID = 1L;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date busiDate;
    private String code;
    private String message;
    private T resultBody;

    public RestResponse() {
    }

    public RestResponse<T> success(T resultBody) {
        this.code = "000000";
        this.busiDate = new Date();
        this.resultBody = resultBody;
        return this;
    }

    public RestResponse success() {
        this.code = "000000";
        this.busiDate = new Date();
        return this;
    }

    public RestResponse<T> failure() {
        this.code = "999999";
        this.busiDate = new Date();
        return this;
    }

    public RestResponse<T> failure(String code, String message) {
        this.setCode(code);
        this.busiDate = new Date();
        this.message = message;
        return this;
    }

    public RestResponse<T> failure(String code) {
        this.setCode(code);
        this.busiDate = new Date();
        return this;
    }

    public RestResponse<T> failure(String code, T resultBody) {
        this.resultBody = resultBody;
        this.code = code;
        this.busiDate = new Date();
        return this;
    }

    public static RestResponse successOk(Object body) {
        return builder().resultBody(body).build().success();
    }

    public static RestResponse successOk(Object body, String msg) {
        return builder().resultBody(body).message(msg).build().success();
    }

    public static RestResponse failureError() {
        return builder().build().failure();
    }

    public static RestResponse failureError(String code) {
        return builder().code(code).build().failure();
    }

    public static RestResponse failureError(String code, String msg) {
        return builder().code(code).message(msg).build().failure();
    }

    public static <T> RestResponseBuilder<T> builder() {
        return new RestResponseBuilder();
    }

    public String toString() {
        return "RestResponse(busiDate=" + this.getBusiDate() + ", code=" + this.getCode() + ", message=" + this.getMessage() + ", resultBody=" + this.getResultBody() + ")";
    }

    public RestResponse(final Date busiDate, final String code, final String message, final T resultBody) {
        this.busiDate = busiDate;
        this.code = code;
        this.message = message;
        this.resultBody = resultBody;
    }

    public Date getBusiDate() {
        return this.busiDate;
    }

    public RestResponse<T> setBusiDate(final Date busiDate) {
        this.busiDate = busiDate;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public RestResponse<T> setCode(final String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public RestResponse<T> setMessage(final String message) {
        this.message = message;
        return this;
    }

    public T getResultBody() {
        return this.resultBody;
    }

    public RestResponse<T> setResultBody(final T resultBody) {
        this.resultBody = resultBody;
        return this;
    }

    public static class RestResponseBuilder<T> {
        private Date busiDate;
        private String code;
        private String message;
        private T resultBody;

        RestResponseBuilder() {
        }

        public RestResponseBuilder<T> busiDate(final Date busiDate) {
            this.busiDate = busiDate;
            return this;
        }

        public RestResponseBuilder<T> code(final String code) {
            this.code = code;
            return this;
        }

        public RestResponseBuilder<T> message(final String message) {
            this.message = message;
            return this;
        }

        public RestResponseBuilder<T> resultBody(final T resultBody) {
            this.resultBody = resultBody;
            return this;
        }

        public RestResponse<T> build() {
            return new RestResponse(this.busiDate, this.code, this.message, this.resultBody);
        }

        public String toString() {
            return "RestResponse.RestResponseBuilder(busiDate=" + this.busiDate + ", code=" + this.code + ", message=" + this.message + ", resultBody=" + this.resultBody + ")";
        }
    }
}
