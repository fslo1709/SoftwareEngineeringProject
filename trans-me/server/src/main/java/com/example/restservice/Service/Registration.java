package com.example.restservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restservice.Response.Msg;
import com.example.restservice.Drive.DriveOperator;
import com.example.restservice.Model.Account;
import com.example.restservice.Repository.AccountRepository;

@Service
public class Registration {
    
    @Autowired
    AccountRepository accountRepository;

    public Msg register(String username, String password) {
        System.out.println("Register：");
        System.out.println("username = "+username);
        if (accountRepository.existsByUsername(username)) {
            System.out.println("error");
            return new Msg(
                "error",
                "user with the same username already exists"
            );
        }
        else {
            // Creates a subfolder in transme to store the audio files
            String driveId;
            try {
                driveId = DriveOperator.createFolder(username);
            }
            catch (Exception e) {
                System.out.println("Error creating google drive subfolder");
                return new Msg(
                    "error",
                    "Error creating google drive subfolder"
                );
            }
            Account newAccount = accountRepository.save(new Account(
                "", username, password, null, null, driveId
            ));
            if (newAccount != null) {
                System.out.println("success");
                return new Msg(
                    "success",
                    "new acount created"
                );
            }
            else {
                System.out.println("Error creating MongoDB Entry");
                return new Msg(
                    "error",
                    "Error creating MongoDB Entry"
                );
            }
        }
        
    }
}
