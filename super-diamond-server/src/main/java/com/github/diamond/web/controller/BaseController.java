/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.diamond.web.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Create on @2013-8-28 @下午8:37:39 
 * @author bsli@ustcinfo.com
 */
abstract public class BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@ExceptionHandler()
	public void handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
		logger.error(exception.getMessage(), exception);
		HttpSession httpSession = request.getSession();
		
		if(exception instanceof CannotGetJdbcConnectionException) {
			httpSession.setAttribute("message", "不能获取数据库连接，请联系管理员！");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		RequestDispatcher rd =  request.getSession().getServletContext().getRequestDispatcher("/error"); 
        try {
			rd.forward(request, response);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * 
	 * @param response
	 * @param content
	 */
	protected void outputContent(HttpServletResponse response,String content){
        OutputStream os = null;
        try {
            //response.setHeader("Content-Type", "application/json;charset=utf-8");
            response.setHeader("Cache-Control", "no-cache");
            byte[] bs = content.getBytes("utf-8");
            response.setContentLength(bs.length);
            os = response.getOutputStream();
            os.write(bs);
            os.flush();
            os.close();
            os = null;
        } catch (Exception exp) {
            exp.printStackTrace();
            response.setStatus(407);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }
	}
}
