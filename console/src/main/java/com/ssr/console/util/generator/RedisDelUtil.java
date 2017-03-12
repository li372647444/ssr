package com.ssr.console.util.generator;

import com.ssr.base.redis.RedisUtil;

public class RedisDelUtil {

	public static void main(String[] args) {
		delEnergy();
		delEnergyGroup();
	}
	
	public static void delEnergy(){
		String per = "C16101300";
		int max = 2915;
		String sux = "";
		String date = "20161014";
		for (int i = 1; i <= max; i++) {
			if(i <10){
				sux = "000" + i;
			}
			else if(i>=10 && i <100){
				sux = "00" + i;
			}
			else if(i>=100 && i <1000){
				sux = "0" + i;
			}
			else{
				sux = "" + i;
			}
			RedisUtil.deleteByKey(per + sux);
			RedisUtil.deleteByKey(per + sux + date);
		}
	}
	
	public static void delEnergyGroup(){
		String per = "6B_1";
		int max = 100;
		for (int i = 0; i <= max; i++) {
			RedisUtil.deleteByKey(per + i);
		}
	}

}
