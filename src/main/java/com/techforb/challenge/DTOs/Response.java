package com.techforb.challenge.DTOs;

import lombok.Data;import org.springframework.http.HttpStatus;

@Data
public class Response<T> {

    private HttpStatus status;
    private String message;
    private T data;

    public Response() {
        this.status = HttpStatus.OK;
        this.message = "OK";
        this.data = null;
    }

    public void SetError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}
