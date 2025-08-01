package com.codesyncer.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codesyncer.backend.model.User;
import com.codesyncer.backend.repository.UserRepo;


@Service
public class UserServiceImpl implements UserService {


    private UserRepo userRepo;


    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.getAllUsers().get();
    }

}