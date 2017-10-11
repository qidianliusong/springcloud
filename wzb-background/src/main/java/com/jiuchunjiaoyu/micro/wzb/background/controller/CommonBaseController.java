package com.jiuchunjiaoyu.micro.wzb.background.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.wzb.background.exception.WzbBackgroundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 普通controller基类，提供部分基础功能
 */
public class CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(CommonBaseController.class);

    private static final String DATE_TIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";


    @ExceptionHandler(Exception.class)
    public @ResponseBody
    BaseResult<Void> exceptionHandler(Exception exception) {
        BaseResult<Void> baseResult = new BaseResult<>();
        if (exception instanceof WzbBackgroundException) {
            WzbBackgroundException we = (WzbBackgroundException) exception;
            baseResult.setCode(we.getCode());
            baseResult.setMessage(we.getMessage());
            logger.error(exception.getMessage(), exception);
            return baseResult;
        }
        baseResult.setCode(SystemCode.systemErr.getCode());
        baseResult.setMessage(SystemCode.systemErr.getMessage() + ":" + exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return baseResult;
    }

    protected boolean validateArgs(BindingResult bindingResult, BaseResult<?> baseResult) {
        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            String fieldName = "";
            if (objectError instanceof FieldError) {
                fieldName = ((FieldError) objectError).getField();
            }
            baseResult.setCode(SystemCode.validateFail.getCode());
            baseResult.setMessage(fieldName + objectError.getDefaultMessage());
            return false;
        }
        return true;
    }

    /**
     * 获取给定日期前的开始时间
     *
     * @param days
     * @return
     */
    protected String getStartTimeBeforeDays(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -days);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
        return simpleDateFormat.format(c.getTime());
    }

    /**
     * 获取给定日期前的结束时间
     *
     * @param days
     * @return
     */
    protected String getEndTimeBeforeDays(int days) {
        return getStartTimeBeforeDays(days - 1);
    }
}
