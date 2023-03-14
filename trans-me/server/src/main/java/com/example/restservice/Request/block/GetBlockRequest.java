package com.example.restservice.Request.block;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetBlockRequest {
    // @AllArgsConstructor // 用了就出事
    @Getter
    @Setter
    public class RequestData {
        private List<String> blocksId;
    }

    private RequestData data;
}
