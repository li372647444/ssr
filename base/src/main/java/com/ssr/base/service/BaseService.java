package com.ssr.base.service;

/**
 * 所有Service基类接口,必须继承
 *
 * @author PengLian
 */
public interface BaseService {

    /**
     * 写入trace日志
     * @param message
     */
    void trace(Object message);

    /**
     * 写入trace日志(包含异常)
     * @param message
     * @param t
     */
    void trace(Object message, Throwable t);

    /**
     * 写入debug日志
     * @param message
     */
    void debug(Object message);
    /**
     * 写入debug日志(包含异常)
     * @param message
     * @param t
     */
    void debug(Object message, Throwable t);
    /**
     * 写入info日志
     * @param message
     */
    void info(Object message);
    /**
     * 写入info日志(包含异常)
     * @param message
     * @param t
     */
    void info(Object message, Throwable t);
    /**
     * 写入warn日志
     * @param message
     */
    void warn(Object message);
    /**
     * 写入warn日志(包含异常)
     * @param message
     * @param t
     */
    void warn(Object message, Throwable t);
    /**
     * 写入error日志
     * @param message
     */
    void error(Object message);
    /**
     * 写入error日志(包含异常)
     * @param message
     * @param t
     */
    void error(Object message, Throwable t);
    /**
     * 写入fatal日志
     * @param message
     */
    void fatal(Object message);
    /**
     * 写入fatal日志(包含异常)
     * @param message
     * @param t
     */
    void fatal(Object message, Throwable t);
}
