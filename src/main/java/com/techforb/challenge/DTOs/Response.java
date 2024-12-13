package com.techforb.challenge.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private HttpStatus status;
    private String message;
    private T data;
}
