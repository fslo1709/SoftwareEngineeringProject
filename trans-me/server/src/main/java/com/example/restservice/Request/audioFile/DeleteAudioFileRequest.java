package com.example.restservice.Request.audioFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeleteAudioFileRequest {
    @Getter
    @Setter
    public class RequestData {
        private String username;
        private String id;
    }

    private RequestData data;
}
