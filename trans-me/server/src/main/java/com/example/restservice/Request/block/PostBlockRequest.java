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
public class PostBlockRequest {
    // @AllArgsConstructor // 用了就出事
    @Getter
    @Setter
    public class RequestData {
        private Block block;
        // 不確定這樣可不可以，input 沒有 id 會不會死?
            // 答案是不會
    }

    private RequestData data;
}
