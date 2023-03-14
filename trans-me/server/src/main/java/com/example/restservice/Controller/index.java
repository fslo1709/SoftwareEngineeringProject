package com.example.restservice.Controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.restservice.Drive.*;

@Tag(name = "index")
@RestController
public class index {
    
    @GetMapping("/")
    @Operation(summary = "get index", description = "get it！")
    public String getIndex() {
        
        // return new Account(null, param, param, null, null, null);
        // return "Hello " + param;
        return "Hello ";
    }

}

