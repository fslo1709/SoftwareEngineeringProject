package com.example.restservice.Request.audioFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PutAudioFileRequest {
    @Getter
    @Setter
    public class RequestData {
        private String id;
        private String name;
    }

    private RequestData data;
}

