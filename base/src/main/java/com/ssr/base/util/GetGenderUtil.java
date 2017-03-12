package com.ssr.base.util;

/**
 * Title： 根据身份证判断性别 Description： Company：ssr
 * 
 * @author chenwei
 * @date 2016年7月28日
 */
public class GetGenderUtil {
	/**
	 * 根据身份证号获取性别
	 * Description：
	 * @author chenwei
	 * @date 2016年7月28日
	 * @param idCard
	 * @return
	 */
	public static String getGenderByIdCard(String idCard) {
		String sex = "";
		if (idCard != null) {
			if (idCard.length() == 15) {
				return  getResultValue(idCard.substring(14));
			} else if (idCard.length() == 18) {
				return getResultValue(idCard.substring(16, 17));
			}
		}
		return sex;
	}

	/**
	 * 返回性别
	 * Description：
	 * @author chenwei
	 * @date 2016年7月28日
	 * @param flag
	 * @return
	 */
	public static String getResultValue(String flag) {
		try {
			int rerult = Integer.parseInt(flag);
			if (rerult % 2 == 0) {
				return "女";
			} else {
				return "男";
			}
		} catch (NumberFormatException e) {
			return "";
		}

	}
}
