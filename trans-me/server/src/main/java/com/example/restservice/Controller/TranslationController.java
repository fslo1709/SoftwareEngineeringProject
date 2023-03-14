package com.example.restservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.Request.translate.PostTranslateRequest;
import com.example.restservice.Response.CommonResponse;
import com.example.restservice.Service.TranslateService;
import com.example.restservice.Response.Msg;

import java.io.IOException;

@RestController
public class TranslationController {
    @Autowired
    com.example.restservice.Service.TranslateService translateService;

    @PostMapping("/translate")
    public CommonResponse  trans(@RequestBody PostTranslateRequest req){
        String content = req.getData().getContent();
        String language = req.getData().getLanguage();
        Msg msg = new Msg("success", "get terms");
        CommonResponse<String> ret = new CommonResponse<String>(msg,"");
        try{
            ret = translateService.handleTranslate(content,language);
            }
            catch (IOException ioe){
            }
        return ret;
    }
}
