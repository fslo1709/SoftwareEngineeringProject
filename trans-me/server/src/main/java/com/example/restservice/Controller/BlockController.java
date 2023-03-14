package com.example.restservice.Controller;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.Response.CommonResponse;
import com.example.restservice.Response.Msg;
import com.example.restservice.Service.Registration;
import com.example.restservice.Service.Payload.Payload;
import com.example.restservice.Model.Account;
import com.example.restservice.Model.Block;
import com.example.restservice.Service.AccountService;
import com.example.restservice.Service.Login;

import com.example.restservice.Request.account.PostAccountRequest;
import com.example.restservice.Request.account.PutAccountRequest;
import com.example.restservice.Request.block.GetBlockRequest;
import com.example.restservice.Request.block.PostBlockRequest;
import com.example.restservice.Request.block.PutBlockRequest;
import com.example.restservice.Request.account.GetAccountRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Block Controller")
@RestController
@RequestMapping("/block")
public class BlockController {
    // 其實 http://localhost:8080/blocks 有東西
    
    @Autowired
    AccountService accountService;
    
    // getBlock
    @GetMapping("/ids")
    public CommonResponse getBlock(@RequestBody GetBlockRequest req) {
    
        Payload <Msg, List <Block>> result = accountService.readBlocks(req.getData().getBlocksId());

        return new CommonResponse <List <Block> >(
            result.getMsg(),
            result.getData()
        );
        
    }
    
    // getBlockByUsername
    @GetMapping("")
    public CommonResponse getBlockByUsername(@RequestParam("username") String username) {
    
        Payload <Msg, List <Block>> result = accountService.readBlocksByUsername(username);

        return new CommonResponse <List <Block> >(
            result.getMsg(),
            result.getData()
        );
        
    }

    // postBlock
    @PostMapping("")
    public CommonResponse postBlock(@RequestBody PostBlockRequest req) {
    
        List<Block> inBlocks = new ArrayList<Block>();
        inBlocks.add(req.getData().getBlock());
        
        Payload <Msg, List <Block>> result = accountService.createBlocks(inBlocks);

        return new CommonResponse <Block>(
            result.getMsg(),
            result.getData().get(0)
            // TODO : 失敗的話會 null pointer exception ...
        );
        
    }

    // putBlock
    @PutMapping("")
    public CommonResponse putBlock(@RequestBody PutBlockRequest req) {
    
        Payload <Msg, List <Block>> result = accountService.updateBlocks(req.getData().getBlocks());

        return new CommonResponse <List <Block> >(
            result.getMsg(),
            result.getData()
        );
        
    } 

    // deleteBlock
    @DeleteMapping("")
    public CommonResponse deleteBlock(@RequestBody GetBlockRequest req) {
    
        Payload <Msg, String> result = accountService.deleteBlocks(req.getData().getBlocksId());

        return new CommonResponse <String>(
            result.getMsg(),
            result.getData()
        );
        
    }

}
	
	
	