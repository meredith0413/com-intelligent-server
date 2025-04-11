package com.intelligent.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    全局异常处理
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("异常 exception = {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器内部错误");
    }

//    /*
//    自定义异常处理
//    @ExceptionHandler(CustomException.class)
//public ResponseEntity<String> handleCustomException(CustomException e) {
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//}
//    */
}