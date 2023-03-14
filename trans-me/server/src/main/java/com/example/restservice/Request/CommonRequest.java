package com.example.restservice.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.example.restservice.Request.Msg;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class CommonRequest <ReturnDataType> {
    @NonNull
    private Msg msg;
    
    private ReturnDataType data;
}
