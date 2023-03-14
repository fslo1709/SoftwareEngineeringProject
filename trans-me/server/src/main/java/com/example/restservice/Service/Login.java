package com.example.restservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restservice.Response.Msg;
import com.example.restservice.Service.Payload.Payload;
import com.example.restservice.Model.Account;
import com.example.restservice.Repository.AccountRepository;

@Service
public class Login { // 1126 已測
    @Autowired
    AccountRepository accountRepository;

    public Payload <Msg, Account> login(String username, String password) {
        System.out.println("Login：");
        System.out.println("username = "+username);
        Account user =  accountRepository.findByUsername(username);
        
        if (user == null) {
            System.out.println("username not found");
            return new Payload <Msg, Account> (
                new Msg(
                    "error",
                    "username not found"
                ),
                null
            );
            
        }
        else {
            // unit test
                // System.out.println("input pw："+ password);
                // System.out.println("DB pw："+ user.getPassword());

            if (!user.getPassword().equals(password)) {
                System.out.println("incorrect password");
                return new Payload <Msg, Account> (
                    new Msg(
                        "error",
                        "incorrect password"
                    ),                    
                    null
                );
            }
            else {
                System.out.println("success");
                System.out.println(user);
                return new Payload <Msg, Account> (
                    new Msg(
                       "success",
                       "return user data"
                    ),
                    user
                );
            }
            
        }
        
    }
}
