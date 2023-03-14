package com.example.restservice.Service.Payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Payload <MsgType, DataType> {
    private MsgType msg;
    private DataType data;
}


