package com.example.restservice.Response;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class CommonResponse <ReturnDataType> {
    @lombok.NonNull
    private Msg msg;
    private ReturnDataType data;
}
