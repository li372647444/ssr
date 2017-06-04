package com.ssr.base.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

/**
 * 监控方法执行时间,压力测试时开启,正式环境请关闭
 *
 * @author 
 */
public class InvokeLogAop {

    private long serviceStartTime;
    private long serviceEndTime;
    private Logger logger = Logger.getLogger(getClass());

    public InvokeLogAop() {

    }

    public void before(JoinPoint jp) throws Exception {
        serviceStartTime = System.currentTimeMillis();
    }

    public void after(JoinPoint jp) {
        StringBuffer logSb = new StringBuffer();
        if("putLogMessage".equals(jp.getSignature().getName())) {
            return;
        }
        logSb.append("调用类:" + jp.getTarget().getClass().getName());
        logSb.append(";执行方法:" + jp.getSignature().getName());
        logSb.append(";包含参数个数:" + jp.getArgs().length);

        logSb.append("{");
        Object args[] = jp.getArgs();
        for(Object o : args){
            if(o == null){
                logSb.append("[ null ]");
            }else{
                logSb.append("[").append(o.getClass().getName()).append(":").append(o.toString()).append("]");
            }
        }
        logSb.append("}");

        serviceEndTime = System.currentTimeMillis();

        logSb.append(";耗费毫秒数:" + (serviceEndTime - serviceStartTime));

        logger.debug(logSb.toString());
    }

}
