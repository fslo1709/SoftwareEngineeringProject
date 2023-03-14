package com.example.restservice.Request.term;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostTermRequest {
    @Getter
    @Setter
    public class RequestData {
        private String content;
    }

    private RequestData data;
}
