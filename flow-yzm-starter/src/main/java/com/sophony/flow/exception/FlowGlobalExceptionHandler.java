package com.sophony.flow.exception;

import com.sophony.flow.commons.ResultDTO;
import com.yzm.project.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class FlowGlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ResultDTO<?> paramException(BusinessException e) {
        return ResultDTO.failed(e.getMessage());
    }

}
