package com.techforb.challenge.Services.Impl;

import com.techforb.challenge.Commons.LoginUserCommon;
import com.techforb.challenge.Commons.NewUserCommon;
import com.techforb.challenge.Configs.JwtTokenFilter;
import com.techforb.challenge.DTOs.Response;
import com.techforb.challenge.DTOs.TokenDTO;
import com.techforb.challenge.Entities.UserEntity;
import com.techforb.challenge.Repositories.UserRepository;
import com.techforb.challenge.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Response<HttpStatus> createUser(NewUserCommon newUserCommon) {
        Response<HttpStatus> response = new Response<>();

        Optional<UserEntity> userConfirm = userRepository.findByEmail(newUserCommon.getEmail());
        if(userConfirm.isPresent()){
            response.SetError(HttpStatus.CONFLICT, "El email ya existe"); //409
            return response;
        }

        newUserCommon.setPassword(passwordEncoder.encode(newUserCommon.getPassword())); //codificamos contraseña

        UserEntity userEntity = modelMapper.map(newUserCommon, UserEntity.class);

        if (userEntity == null) {
            response.SetError(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear el usuario / 500");
            return response;
        }

        response.setData(HttpStatus.CREATED);

        return response;


    }

    public Response<TokenDTO> login(LoginUserCommon loginUserCommon) {

        Response<TokenDTO> apiResponse = new Response<>();

        Optional<UserEntity> usuarioEntity = userRepository.findByEmail(loginUserCommon.getEmail());
        if (usuarioEntity.get() == null){
            apiResponse.SetError(HttpStatus.valueOf(401),"invalid email or password"); //unauthorized
            return apiResponse;
        }
        boolean match = passwordEncoder.matches(loginUserCommon.getPassword(), usuarioEntity.get().getPassword());
        if(!match){
            apiResponse.SetError(HttpStatus.valueOf(401),"invalid email or password");
            return apiResponse;
        }

        apiResponse.setData(JwtTokenFilter.generateToken(usuarioEntity.get().getName(),
                usuarioEntity.get().getLastName(),
                usuarioEntity.get().getEmail()));

        return apiResponse;
    }
}
