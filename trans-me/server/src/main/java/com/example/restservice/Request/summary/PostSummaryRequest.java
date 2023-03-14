package com.example.restservice.Request.summary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostSummaryRequest {
    @Getter
    @Setter
    public class RequestData {
        private String content;
    }

    private RequestData data;
}
