package com.jiuchunjiaoyu.micro.wzbOrder.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.wzbOrder.exception.WzbOrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 普通controller基类，提供部分基础功能
 *
 */
public class CommonBaseController {

	private static Logger logger = LoggerFactory.getLogger(CommonBaseController.class);

	@ExceptionHandler(Exception.class)
	public @ResponseBody BaseResult<Void> runtimeExceptionHandler(Exception exception) {
		BaseResult<Void> baseResult = new BaseResult<>();
		if (exception instanceof WzbOrderException) {
			WzbOrderException we = (WzbOrderException) exception;
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


}
