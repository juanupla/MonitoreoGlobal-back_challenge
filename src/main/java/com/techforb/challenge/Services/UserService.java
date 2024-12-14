package com.techforb.challenge.Services;

import com.techforb.challenge.Commons.NewUserCommon;
import com.techforb.challenge.DTOs.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Response<HttpStatus> createUser(NewUserCommon newUserCommon);

}
