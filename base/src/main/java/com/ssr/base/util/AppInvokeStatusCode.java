package com.ssr.base.util;

public class AppInvokeStatusCode {
	/**成功*/
    public static final String SUCCESS = "000000";
    /**参数传递错误*/
    public static final String PARAMETER_ERROR = "000001";
    /**未登录或登录过期*/
    public static final String NOLOGING_ERROR = "000002";
    
    /**用户名或者密码错误，登录失败*/
    public static final String LOGING_FAIL_ERROR = "000003";
    /**未知错误*/
    public static final String UNKNOWN_ERROR = "000004";
    /**两次密码不一样*/
    public static final String PASSWORD_DIFFERENT_ERROR = "000005";
    /**验证码输入错误*/
    public static final String VERIFY_CODE_ERROR = "000006";
    /**注册用户必须先退出登录*/
    public static final String REGISTER_MUST_LOGINOUT_ERROR = "000007";
    /**没有查出数据*/
    public static final String NO_DATA_ERROR = "000008";
    /**账户不存在*/
    public static final String ACCOUNT_EXISTS_ERROR = "000009";
    /**不能和登录密码相同*/
    public static final String SAME_LOGINPASSWORD_ERROR = "000010";
    /**交易密码已经存在*/
    public static final String TRANPASSWORD_EXIST_ERROR = "000011";
    /**密码不能为空*/
    public static final String PASSWORD_ISEMPTY_ERROR = "000012";
    /**俩次密码输入不一*/
    public static final String PASSWORD_NOSAME_ERROR = "000013";
    
    /**验证码过期*/
    public static final String VERIFY_CODE_TIME_OUT = "000046";
    /**用户账户已停用*/
    public static final String ERROR_STATE_TY = "000047";
    /**请求用户与session不一致*/
    public static final String ERROR_USERID = "000048";
    /**登录错误次数超限*/
    public static final String LONGIN_ERROR_COUNT = "000050";
    /**原密码错误*/
    public static final String OLD_PASS_ERROR = "000051";
    /**新密码不能原密码相同*/
    public static final String NEW_PASS_ERROR = "000052";
    /**密码长度为6-20*/
    public static final String PASS_LENGTH_ERROR = "000053";
    
    /**接口内部错误*/
    public static final String INNER_ERROR = "000099";
    /**接口非法参数访问*/
    public static final String NO_DATA = "000098";
    /**接口非法参数访问(时间戳不存在)*/
    public static final String NO_TIME_STAMP = "000097";
    /**接口非法参数访问(时间戳过期)*/
    public static final String REQ_TIMEOUT = "000096";
    /**未查询到相关数据*/
    public static final String NO_RETURN_DATA = "000080";
   
}
