package com.example.restservice.Request.transcription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostTranscriptionRequest {
    // @AllArgsConstructor // 用了就出事
    @Getter
    @Setter
    public class RequestData {
        private String username;
	    private String audioFileId;
    }

    private RequestData data;
}

