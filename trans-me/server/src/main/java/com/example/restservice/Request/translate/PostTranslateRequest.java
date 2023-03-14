package com.example.restservice.Request.translate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostTranslateRequest {
    @Getter
    @Setter
    public class RequestData {
        private String content;
        private String language;
    }
    private RequestData data;
}
