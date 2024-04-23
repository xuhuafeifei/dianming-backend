/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xhf.common.exception;

import com.xhf.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.crypto.BadPaddingException;

/**
 * 异常处理器
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
@RestControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());


	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RRException.class)
	public R handleRRException(RRException e){
		e.printStackTrace();
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());
//		logger.info("开始打印信息:");
//		log.info("code:{}", e.getCode());
//		log.info("msg:{}", e.getMessage());
		return r;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public R handlerNoFoundException(Exception e) {
		logger.error(e.getMessage(), e);
		return R.error(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(BadPaddingException.class)
	public R handlerBadPaddingException(BadPaddingException e) {
		logger.error(e.getMessage(), e);
		return R.error(ErrorCode.DECRYPTION_ERROR);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return R.error("数据库中已存在该记录");
	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		logger.error(e.getMessage(), e);
		return R.error();
	}
}
