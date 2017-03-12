package com.ssr.base.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * 全局Filter,处理加载全局数据资源到内存中
 *
 * @author PengLian
 */
public class GlobalFilter implements Filter {

    /**
     * 根据需要初始化全局数据到内存中
     * @param filterConfig
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 根据需要过滤请求
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest req,
                         ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
    
    }

    public void destroy() {

    }
}
