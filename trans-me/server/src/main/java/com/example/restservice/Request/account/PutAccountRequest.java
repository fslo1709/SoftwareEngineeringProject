package com.example.restservice.Request.account;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PutAccountRequest {
    // @AllArgsConstructor // 用了就出事
    @Getter
    @Setter
    public class RequestData {
        private String username;
        // private String password;
        private List<String> audioFilesId;
        private List<String> blocksId;
    }

    private RequestData data;
}
