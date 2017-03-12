package com.ssr.base.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.ssr.base.util.SystemConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis工具类
 * @author pl
 *
 */
public class RedisUtil {
	private static JedisPool jedisPool = null;
	private static JedisCluster jedisCluster = null;
	private static Logger logger = Logger.getLogger(RedisUtil.class);
	
	
	/** 
     * redis过期时间,以秒为单位 
     */  
    public final static int EXRP_HOUR = 60*60;          //一小时  
    public final static int EXRP_DAY = 60*60*24;        //一天  
    public final static int EXRP_MONTH = 60*60*24*30;   //一个月
	
	private static void buildJedisPool(){
		JedisPoolConfig config = new JedisPoolConfig();
		/**
		 * 可用连接实例的最大数目,默认值为8
		 * 如果赋值为-1,则表示不限制,如果pool已经分配了maxActive个jedis实例,则此时pool的状态为exhausted(耗尽)
		 */
		config.setMaxTotal(Integer.parseInt(SystemConfig.getValueByKey("redis.maxTotal")));
		/**
		 * 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例,默认值也是8
		 */
		config.setMaxIdle(Integer.parseInt(SystemConfig.getValueByKey("redis.maxIdle")));
		/**
		 * 等待可用连接的最大时间,单位毫秒,默认值为-1,表示永不超时
		 * 如果超过等待时间,则直接抛出JedisConnectionException
		 */
		config.setMaxWaitMillis(Long.parseLong(SystemConfig.getValueByKey("redis.maxWaitMillis")));
		/**
		 * 在borrow一个jedis实例时,是否提前进行validate操作；如果为true,则得到的jedis实例均是可用的
		 */
		config.setTestOnBorrow(true);
		try {
			jedisPool = new JedisPool(config, SystemConfig.getValueByKey("redis.host"), 
	        		Integer.parseInt(SystemConfig.getValueByKey("redis.port")), 
	        		Integer.parseInt(SystemConfig.getValueByKey("redis.timeout")), 
	        		SystemConfig.getValueByKey("redis.password"));
		} catch (Exception e) {
			logger.error("creat JedisPool failed", e);
		}
        
	}
	
	public static synchronized void poolInit(){
		if(jedisPool == null){
			buildJedisPool();
		}
	}
	
	public static Jedis getJedis() {
		if (jedisPool == null) {
			poolInit();
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			logger.error("get Jedis failed", e);
		}
		return jedis;
	}
	
	/**
	 * 设置 String
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setString(String key, String value) {
		value = StringUtils.isEmpty(value) ? "" : value;
		Jedis jedis = null;
		boolean re = true;
		try {
			jedis = getJedis();
			jedis.set(key, value);
		} catch (Exception e) {
			re = false;
			logger.error("set string value failed.key=" + key + "|value=" + value, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return re;
	}
	
	/**
	 * 设置值+1(对应值必须为64位有符号整数)
	 * @param key
	 * @param value
	 */
	public static void incrObject(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.incr(key);
		} catch (Exception e) {
			logger.error("incr object value failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * 批量设置 String
	 * @param keysvalues
	 */
	public static void msetString(String... keysvalues) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.mset(keysvalues);
		} catch (Exception e) {
			logger.error("mset string value failed.keysvalues=" + keysvalues, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * 获取String
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		Jedis jedis = null;
		String value = "";
		try {
			jedis = getJedis();
			value = jedis.get(key);
		} catch (Exception e) {
			logger.error("get string value failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return value;
	}
	
	/**
	 * 追加String
	 * @param key
	 * @param value
	 */
	public static void appendString(String key, String value) {
		value = StringUtils.isEmpty(value) ? "" : value;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.append(key, value);
		} catch (Exception e) {
			logger.error("append string value failed.key=" + key + "|value=" + value, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * 设置map
	 * @param key
	 * @param value
	 */
	public static void setMap(String key, Map<String, String> value) {
		if(value == null){
			value = new HashMap<String, String>();
		}
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.hmset(key, value);
		} catch (Exception e) {
			logger.error("set map value failed.key=" + key + "|value=" + value, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * 获取map对应的值
	 * @param key
	 * @param fields
	 * @return
	 */
	public static List<String> getMap(String key, String... fields) {
		Jedis jedis = null;
		List<String> value = null;
		try {
			jedis = getJedis();
			value = jedis.hmget(key, fields);
		} catch (Exception e) {
			logger.error("get map value failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return value;
	}
	
	/**
	 * 获取map对应的值
	 * 不推荐使用,推荐通过序列化map当成字符串存储
	 * @param key
	 * @return
	 */
	public static Map<String, String> getMap(String key) {
		Jedis jedis = null;
		Map<String, String> value = new HashMap<String, String>();
		try {
			jedis = getJedis();
			Set<String> set = jedis.hkeys(key);
			Iterator<String> iter = set.iterator();
			String tk = null;
			while(iter.hasNext()) {
				tk = iter.next();
				value.put(tk, jedis.hget(key, tk));
			}
		} catch (Exception e) {
			logger.error("get map value failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return value;
	}
	
	/**
	 * 删除map中key对应的值
	 * @param key
	 * @param mapKey
	 */
	public static void delMapValueByKey(String key, String mapKey){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.hdel(key, mapKey);
		} catch (Exception e) {
			logger.error("delete map value by map key failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * 判断key对应值是否存在
	 * @param key
	 * @return
	 */
	public static boolean exists(String key){
		Jedis jedis = null;
		boolean re = false;
		try {
			jedis = getJedis();
			re = jedis.exists(key);
		} catch (Exception e) {
			logger.error("judge key exists failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return re;
	}
	
	/**
	 * 在list前端添加数据
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean lpushList(String key, String... value) {
		Jedis jedis = null;
		boolean re = true;
		try {
			jedis = getJedis();
			jedis.lpush(key, value);
		} catch (Exception e) {
			re = false;
			logger.error("lpush list value failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return re;
	}
	
	/**
	 * 在list后端添加数据
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean rpushList(String key, String... value) {
		Jedis jedis = null;
		boolean re = true;
		try {
			jedis = getJedis();
			jedis.rpush(key, value);
		} catch (Exception e) {
			re = false;
			logger.error("rpush list value failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return re;
	}
	
	/**
	 * 获取list数据
	 * @param key
	 * @param start
	 * @param limit
	 * @return
	 */
	public static List<String> lrangeList(String key, long start, long limit){
		Jedis jedis = null;
		List<String> value = null;
		try {
			jedis = getJedis();
			value = jedis.lrange(key, start, limit);
		} catch (Exception e) {
			logger.error("lpush list value failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return value;
	}
	
	/**
	 * 获取list长度
	 * @param key
	 * @return
	 */
	public static long llenList(String key){
		Jedis jedis = null;
		long value = 0;
		try {
			jedis = getJedis();
			value = jedis.llen(key);
		} catch (Exception e) {
			logger.error("llen list value failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return value;
	}
	
	/**
	 * 删除并返回列表头元素
	 * @param key
	 * @return
	 */
	public static String lpopList(String key){
		Jedis jedis = null;
		String value = "";
		try {
			jedis = getJedis();
			value = jedis.lpop(key);
		} catch (Exception e) {
			logger.error("lpop list value failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return value;
	}
	
	/**
	 * 删除并返回列表尾元素
	 * @param key
	 * @return
	 */
	public static String rpopList(String key){
		Jedis jedis = null;
		String value = "";
		try {
			jedis = getJedis();
			value = jedis.rpop(key);
		} catch (Exception e) {
			logger.error("rpop list value failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return value;
	}
	
	/**
	 * 根据key删除对象
	 * @param key
	 */
	public static void deleteByKey(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.del(key);
		} catch (Exception e) {
			logger.error("delete value by key failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * 根据key设置过期时间
	 * @param key
	 */
	public static void expireByKey(String key, int seconds) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.expire(key, seconds);
		} catch (Exception e) {
			logger.error("expire by key failed.key=" + key, e);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	
	private static void buildJedisClusterPool(){
		HashSet<HostAndPort> haps = new HashSet<HostAndPort>();
		String hosts = SystemConfig.getValueByKey("redis.cluster.hostAndPort");
		String th[] = hosts.split(",");
		String tz[] = null;
		for(String t : th){
			tz = t.split(":");
			HostAndPort hap = new HostAndPort(tz[0], Integer.parseInt(tz[1]));
			haps.add(hap);
		}
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxIdle(Integer.parseInt(SystemConfig.getValueByKey("redis.cluster.maxIdle")));
		config.setMaxTotal(Integer.parseInt(SystemConfig.getValueByKey("redis.cluster.maxTotal")));
		config.setMaxWaitMillis(Long.parseLong(SystemConfig.getValueByKey("redis.cluster.maxWaitMillis")));
		/**
		 * maxRedirections为集群节点个数
		 */
		try {
			jedisCluster = new JedisCluster(haps, 
					Integer.parseInt(SystemConfig.getValueByKey("redis.cluster.timeout")), 
					Integer.parseInt(SystemConfig.getValueByKey("redis.cluster.soTimeout")), 
					Integer.parseInt(SystemConfig.getValueByKey("redis.cluster.maxRedirections")), 
					SystemConfig.getValueByKey("redis.cluster.password"), 
					config);
		} catch (Exception e) {
			logger.error("creat JedisCluster failed", e);
		}
		
	}
	
	public synchronized void clusterPoolInit(){
		if(jedisCluster == null){
			buildJedisClusterPool();
		}
	}
}
