package com.example.restservice.Request.block;

import java.util.List;

import com.example.restservice.Model.Block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PutBlockRequest {
    // @AllArgsConstructor // 用了就出事
    @Getter
    @Setter
    public class RequestData {
        private List<Block> blocks;
    }

    private RequestData data;
}
