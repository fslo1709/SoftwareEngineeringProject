package com.example.restservice.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// message
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Msg {
    public String status;
    public String msg;
}