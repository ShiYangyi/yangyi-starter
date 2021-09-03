package com.example.yangyistarter.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseMessage handleHttpClientError(MethodArgumentNotValidException ex) {
        //这里就是返回到postman信息中的details部分，是存储hashMap元素的列表。
        //需要自定义ObjectError类。没有选择自定义返回类，而是在后面把需要的信息获取出来后，改变在填充detail信息时填充的内容，不再是填充原始的errors，而是自定义的errorList。
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        if (errors.size() == 1 && ResponseCode.contains(errors.get(0).getCode())) {
            ResponseCode errorCode = ResponseCode.valueOf(errors.get(0).getCode());
            /*返回到IDEA界面：实验中显示的是走else分支。
            invalid request with errors:
            [Field error in object 'userDTO' on field 'name':
            rejected value [a];
            codes
                [Length.userDTO.name,
                Length.name,
                Length.java.lang.String,
                Length];

            arguments
                codes
                    [userDTO.name,
                    name];

                arguments
                    [];
                default message
                    [name],2147483647,3];
            default message
                [用户名不能少于3个字符],
            Field error in object 'userDTO' on field 'password':
            rejected value [a];
            codes
                [Length.userDTO.password,
                Length.password,
                Length.java.lang.String,
                Length];
            arguments
                codes
                    [userDTO.password,
                    password];
                arguments [];
                default message
                    [password],2147483647,6];
            default message
                [密码不能少于3个字符]]

IDEA界面上显示的与postman上显示的是一致的。

{
    "code": 10000,
    "message": "invalid request",
    "details": {
        "errors": [
            {
                "codes": [
                    "Length.userDTO.name",
                    "Length.name",
                    "Length.java.lang.String",
                    "Length"
                ],
                "arguments": [
                    {
                        "codes": [
                            "userDTO.name",
                            "name"
                        ],
                        "arguments": null,
                        "defaultMessage": "name",
                        "code": "name"
                    },
                    2147483647,
                    3
                ],
                "defaultMessage": "用户名不能少于3个字符",
                "objectName": "userDTO",
                "field": "name",
                "rejectedValue": "a",
                "bindingFailure": false,
                "code": "Length"
            },
            {
                "codes": [
                    "Length.userDTO.password",
                    "Length.password",
                    "Length.java.lang.String",
                    "Length"
                ],
                "arguments": [
                    {
                        "codes": [
                            "userDTO.password",
                            "password"
                        ],
                        "arguments": null,
                        "defaultMessage": "password",
                        "code": "password"
                    },
                    2147483647,
                    6
                ],
                "defaultMessage": "密码不能少于3个字符",
                "objectName": "userDTO",
                "field": "password",
                "rejectedValue": "a",
                "bindingFailure": false,
                "code": "Length"
            }
        ]
    }
}
            * */

            log.warn("invalid request with error code: {}", errorCode);
            return errorCode.toResponseMessage();
        } else {

            log.warn("invalid request with errors: {}", errors);//判断出来是走的else分支，当打断点在这里的时候，
            // 单步运行，发现返回的details是一个长度为1的列表，元素为hashMap，key为errors，value为长度为2的列表，
            // 列表中有两个对象，都是hashMap类型的列表，现在只需要取其中的一个hashMap就可以。取key为defaultMessage的哈希对。
            // 所以如果要修改这个返回到postman的对象格式，那么需要往前找怎么填充的这个errors，上面有这条语句：
            // List<ObjectError> errors = ex.getBindingResult().getAllErrors();其中getBindingResult()就是根据校验规则去填充信息。所以需要去修改ObjectError类，
            // 去自定义一个error类。
            List<String> errorList = new ArrayList<>();
            for(int index = 0; index < errors.size(); index++) {
                errorList.add(errors.get(index).getDefaultMessage());
            }
            return ResponseCode.INVALID_REQUEST.toResponseMessage().withDetails("errors", errorList);
            //return ResponseCode.INVALID_REQUEST.toResponseMessage().withDetails("errors", errors);
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseMessage handleHttpClientError(IllegalArgumentException ex) {
        log.warn("invalid request", ex);
        return new ResponseMessage(ResponseCode.INVALID_REQUEST.getCode(), ex.getMessage());
    }

    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public ResponseEntity handleHttpException(HttpException ex) {
        ResponseStatus responseStatus = ex.getClass().getDeclaredAnnotation(ResponseStatus.class);
        log.warn(String.format("http exception code %s", responseStatus), ex);
        return ResponseEntity.status(responseStatus.value())
                .body(new ResponseMessage(ex.getResponseCode().getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseMessage handleException(Exception ex) {
        log.warn("request exception", ex);
        return new ResponseMessage(ResponseCode.SERVER_ERROR.getCode(), "server error");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    //ConstraintViolationException是底层的异常类
    public ResponseMessage handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseMessage(ResponseCode.INVALID_REQUEST.getCode(), ex.getMessage());
    }
}
