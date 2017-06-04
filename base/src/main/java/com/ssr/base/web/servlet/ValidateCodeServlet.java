package com.ssr.base.web.servlet;

import org.apache.log4j.Logger;

import com.ssr.base.util.SystemConstants;
import com.ssr.base.util.img.VerifyCodeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码生成Servlet
 * 默认生成登录和注册验证码
 * 可根据需要传入验证码类型
 * 生成器会将验证码存入传入类型对应的SESSION域中
 * valcmcuteCode?type=login
 *
 */
public class ValidateCodeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createImage(req, resp);
    }

    private void createImage(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        String valcmcuteCodeType = request.getParameter("type");
        if(valcmcuteCodeType == null || "".equals(valcmcuteCodeType)){
            valcmcuteCodeType = "loginValcmcuteCode";
        }
        
        String valcmcuteCodeLength = request.getParameter("length");
        int codeLength = 6;
        if(valcmcuteCodeLength != null && !"".equals(valcmcuteCodeLength)){
        	codeLength = Integer.parseInt(valcmcuteCodeLength);
        }

        String s = VerifyCodeUtils.generateVerifyCode(codeLength);
        request.getSession().setAttribute(valcmcuteCodeType, s);
        request.getSession().setAttribute(SystemConstants.SESSION_VCODE_TIME, System.currentTimeMillis());

        logger.debug("生成验证码:valcmcuteCodeType=" + valcmcuteCodeType + "|length=" + codeLength + "|code=" + s);
        VerifyCodeUtils.outputImage(response.getOutputStream(), s);
    }
}
