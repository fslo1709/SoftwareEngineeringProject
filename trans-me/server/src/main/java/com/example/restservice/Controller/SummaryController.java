package com.example.restservice.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.Request.summary.PostSummaryRequest;
import com.example.restservice.Response.CommonResponse;
import com.example.restservice.Service.SummaryService;
import com.example.restservice.Response.Msg;

import java.io.IOException;

@RestController
public class SummaryController {
    @Autowired
    com.example.restservice.Service.SummaryService summaryService;

    @PostMapping("/summary")
    public CommonResponse sum(@RequestBody PostSummaryRequest req){
        String content = req.getData().getContent();
        Msg msg = new Msg("success", "get terms");
        CommonResponse<String> ret = new CommonResponse<String>(msg,"");
        try{
        ret = summaryService.handleSummary(content);
        }
        catch (IOException ioe){
        }
        return ret;
    }
}
