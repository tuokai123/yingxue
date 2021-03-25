package com.baizhi.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomerHandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(CustomerHandlerExceptionResolver.class);

    private Map<String,Object> result = new HashMap<>();

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map<String,Object> handle(Exception e) {
        if(e instanceof DataIntegrityViolationException){
            log.error("违反完整性约束："+e.getMessage());
            result.put("err_msg", "违反完整性约束!");
            return result;
        }
        if(e instanceof DuplicateKeyException){
            log.error("主键冲突："+e.getMessage());
            result.put("err_msg","主键冲突!");
            return result;
        }
        log.error("错误信息："+e.getMessage());
        result.put("err_msg", e.getMessage());
        return result;
    }


}
