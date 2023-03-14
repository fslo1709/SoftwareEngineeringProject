package com.example.restservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.Request.transcription.PostTranscriptionRequest;
import com.example.restservice.Response.CommonResponse;
import com.example.restservice.Response.Msg;
import com.example.restservice.Service.Transcription;
import com.example.restservice.Service.Payload.Payload;
import com.example.restservice.Transcription.TranscriptionSupport;

// @Tag(name = "Block Controller")
@RestController
@RequestMapping("/transcription")
public class TranscriptionController {
    
    @Autowired
    Transcription transcription;
    
    // postTranscription
    @PostMapping("")
    public CommonResponse postTranscription(@RequestBody PostTranscriptionRequest req) {
    
        // "C:\\Users\\user\\Desktop\\test.wav"

        Payload <Msg, String> result = transcription.transcribe(
            req.getData().getUsername(),
            req.getData().getAudioFileId()
        );
        
        return new CommonResponse <String>(
            result.getMsg(),
            result.getData()
        );

        
    }
}
