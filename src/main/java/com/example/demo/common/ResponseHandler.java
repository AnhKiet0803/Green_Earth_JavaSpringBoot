package com.example.demo.common;

import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.enums.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
    public static <T>ResponseEntity<ResponseDTO<T>> success(T data,String customMessenge){
        ResponseDTO<T> dto = new ResponseDTO<>(
                StatusCode.SUCCESS.getCode(),
                customMessenge != null? customMessenge:StatusCode.SUCCESS.getMessenge(),data
        );
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    public static <T>ResponseEntity<ResponseDTO<T>> error(StatusCode status, String customMessenge){
        ResponseDTO<T> dto = new ResponseDTO<>(
                status.getCode(),
                customMessenge != null? customMessenge:StatusCode.SUCCESS.getMessenge(),null
        );
        return ResponseEntity.status(status.getHttpStatus()).body(dto);
    }
}