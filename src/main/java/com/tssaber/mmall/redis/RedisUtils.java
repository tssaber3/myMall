package com.tssaber.mmall.redis;

import com.google.gson.Gson;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.*;

/**
 * @description: redis工具类
 * @author: tssaber
 * @time: 2019/11/25 0025 19:27
 */
@Component
public class RedisUtils {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private JedisPool jedisPool;

    private static Gson gson = CommentUtil.INSTANCE.getGson();

    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);

    /**
     * 获取锁成功
     */
    private static final String LOCK_SUCCESS = "OK";

    /**
     * 不存在就创建
     */
    private static final String SET_IF_NOT_EXIST = "NX";

    /**
     * 时间毫秒单位
     */
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 释放锁成功
     */
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * redis的lua脚本用来进行库存的扣除
     */
    private static final String STOCK_LUA;

    /**
     * 分布式锁 解锁的lua脚本
     */
    private static final String UNLOCK_LUA;

    static {
        StringBuilder sb1 = new StringBuilder();
        sb1.append("if(redis.call('exists',KEYS[1]) == 1) then");
        sb1.append("     local stock = tonumber(redis.call('get',KEYS[1]));");
        sb1.append("     if(stock == -1)then");
        sb1.append("         return 1;");
        sb1.append("     end;");
        sb1.append("     if(stock > 0) then");
        sb1.append("         redis.call('incrby',KEYS[1],-1);");
        sb1.append("         return stock;");
        sb1.append("     end;");
        sb1.append("     return 0;");
        sb1.append("end;");
        sb1.append(" return -1;");
        STOCK_LUA = sb1.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end");
        UNLOCK_LUA = sb2.toString();
    }

    public RedisUtils(RedisTemplate<String,Object> restTemplate){
        this.redisTemplate = restTemplate;
    }


    /**
     * 扣库存
     *
     * @param key 库存key
     * @return 扣减之前剩余的库存【0:库存不足; -1:库存未初始化; 大于0:扣减库存之前的剩余库存】
     */
    public Long stockTest(KeyPrefix keyPrefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //脚本里的KEYS参数
            List<String> keys = new ArrayList<>();
            String realKey = keyPrefix.getPrefix() + key;
            keys.add(realKey);
            //脚本里的ARGV参数
            List<String> args = new ArrayList<>();
            Long result = (Long) jedis.eval(STOCK_LUA,keys,args);
            System.out.println(result);
            return result;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return -1L;
    }

    /**
     * redis 分布式锁 获取锁
     * @param keyPrefix
     * @param key
     * @param value
     * @return
     */
    public boolean tryLock(KeyPrefix keyPrefix,String key,String value){
        Jedis jedis = null;
        try {
            System.out.println("获取");
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            String result = jedis.set(realKey,value,SET_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME,keyPrefix.expireSeconds());
            if (LOCK_SUCCESS.equals(result)){
                log.info("获取锁成功");
                return true;
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return false;
    }

    /**
     * 分布式锁  解锁
     * @param keyPrefix
     * @param key
     * @param value
     * @return
     */
    public boolean unLock(KeyPrefix keyPrefix,String key,String value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            Object result = jedis.eval(UNLOCK_LUA,Collections.singletonList(realKey),Collections.singletonList(value));
            System.out.println(UNLOCK_LUA);
            if (RELEASE_SUCCESS.equals(result)){
                log.info("解锁成功");
                return true;
            }
            log.info("解锁失败");
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return false;
    }

    /**
     * 判断键是否存在
     * @param key
     * @return
     */
    public boolean hasKey(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            close(jedis);
        }
    }


    /**
     * 自增1 只能对value是Integer的用
     * @param prefix
     * @param key
     */
    public void incr(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            jedis.incr(realKey);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(jedis);
        }
    }


    public boolean exists(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            close(jedis);
        }
    }

    /**
     *
     * @param prefix:前缀
     * @param key:键
     * @param value:值
     * @param <T>:成功与否
     * @return
     */
    public <T> Boolean set(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str == null || str.length() <= 0){
                return false;
            }
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();
            if (seconds <= 0){
                jedis.set(realKey,str);
            }else {
                jedis.setex(realKey,seconds,str);
            }
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return false;
    }

    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return null;
    }

    public boolean del(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret > 0;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return false;
    }

    /**
     * 尝试获取分布式锁
     * @param lockKey:锁的key
     * @param requestId:线程id(值)
     * @param expireTime:过期时间
     * @return
     */
    public Boolean tryGetDistributedLock(String lockKey,String requestId,int expireTime){
        log.info("尝试获取分布式锁");
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.set(lockKey,requestId,SET_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME,expireTime);
            if (LOCK_SUCCESS.equals(result)){
                log.info("获取到分布式锁");
                return true;
            }
            log.info("分布式锁获取失败");
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return false;
    }

    /**
     * 尝试释放分布式锁
     * @param lockKey:键
     * @param requestId:线程ID(值)
     * @return
     */
    public Boolean releaseDistributedLocks(String lockKey,String requestId){
        log.info("尝试释放分布式锁");
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey),Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)){
                log.info("释放分布式锁成功");
                return true;
            }
            log.info("释放分布式锁失败");
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return false;
    }

    //----------------------hash------------------------------//
    /**
     * 向名为 key的hash添加 field<-->(域-值)对
     * 添加一条记录 如果map_key存在 则更新value
     * hset 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作
     * 如果字段已经存在于哈希表中，旧值将被覆盖
     * @param keyPrefix:前缀
     * @param key：键
     * @param field:域
     * @param value：值
     * @param <T>
     * @return
     */
    public <T> Boolean hset(KeyPrefix keyPrefix, String key, String field, T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str == null || str.length() == 0){
                return false;
            }
            String realKey = keyPrefix.getPrefix() + key;
            jedis.hset(realKey,field,str);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return false;
    }
    /**
     * 批量添加记录
     * hmset 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * 此命令会覆盖哈希表中已存在的域
     * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作
     * @param keyPrefix
     * @param key
     * @param map
     * @param <T>
     * @return
     */
    public <T> Boolean hsetAll(KeyPrefix keyPrefix, String key, Map<String,T> map){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Map<String,String> newMap = beansToString(map);
            if (newMap.isEmpty()){
                return false;
            }
            String realKey = keyPrefix.getPrefix() + key;
            jedis.hmset(realKey,newMap);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return false;
    }

    /**
     * 删除hash中 field对应的值
     * hdel 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略
     * @param keyPrefix
     * @param key
     * @param field
     * @return
     */
    public boolean hdel(KeyPrefix keyPrefix, String key, String... field){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            jedis.hdel(realKey,field);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return false;
    }

    /**
     * 获取hash中 指定的field的值
     * hmget 返回哈希表 key 中，一个或多个给定域的值。
     * 如果给定的域不存在于哈希表，那么返回一个 nil 值。
     * 不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表
     * @param keyPrefix
     * @param key
     * @param field
     * @return
     */
    public <T> List<T> hget(KeyPrefix keyPrefix, String key, Class<T> clazz, String... field){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            List<String> result = jedis.hmget(realKey,field);
            return listToBean(result,clazz);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return null;
    }

    /**
     * 获取hash中 所有的field value
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return 在返回值里，紧跟每个字段名(field name)之后是字段的值(value)，所以返回值的长度是哈希表大小的两倍
     */
    public <T> Map<String,T> hgetAll(KeyPrefix keyPrefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            Map<String,String> map = jedis.hgetAll(realKey);
            return stringsToBeans(map,clazz);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return null;
    }

    /**
     * 判断hash中指定的field是否存在
     * @param keyPrefix
     * @param key
     * @param field
     * @return 如果哈希不包含字段或key不存在 返回0，如果哈希包含字段 返回1
     */
    public boolean hifExist(KeyPrefix keyPrefix, String key, String field){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.hexists(realKey,field);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return false;
    }

    /**
     * 获取hash 的size
     * hlen 获取哈希表中字段的数量
     * @param keyPrefix
     * @param key
     * @return
     */
    public long hsize(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.hlen(realKey);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return 0;
    }

    //-------------------------list---------------------------//
    /**
     * 可以用
     * 根据list的key返回list的长度
     * @param keyPrefix
     * @param key
     * @return
     */
    public long lsize(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            Long listLength = jedis.llen(realKey);
            return listLength;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return -1;
    }

    /**
     * 可以用
     * 在list指定位置 插入值
     * 覆盖原有的值
     * @param keyPrefix
     * @param key
     * @param index
     * @param value
     * @return
     */
    public <T> boolean lset(KeyPrefix keyPrefix, String key, int index, T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            String newValue = beanToString(value);
            jedis.lset(realKey,index,newValue);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return false;
    }

    /**
     * 可以用
     * @param keyPrefix:key的前缀
     * @param key:list的key
     * @param position:前插还是后插
     * @param pivot:list中的相对位置的value
     * @param value:插入的内容
     * @return 更新后list的长度,如果没有任何操作返回-1
     */
    public <T> long linsert(KeyPrefix keyPrefix, String key, BinaryClient.LIST_POSITION position, T pivot, T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            String newPivot = beanToString(pivot);
            String newValue = beanToString(value);
            return jedis.linsert(realKey,position,newPivot,newValue);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return -1;
    }

    /**
     * push推 从左边推进去 list左边加入记录 队列和栈的使用
     * 将一个或多个值value 插入list左边
     * 如果list不存在 则创建list并进行push操作
     * @param keyPrefix
     * @param key
     * @param value
     * @return
     */
    public long lPush(KeyPrefix keyPrefix, String key, String... value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.lpush(realKey,value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return -1;
    }


    /**
     * push推 从右边推进去 list右边加入记录 队列和栈的使用
     * 将一个或多个值value 插入list右边
     * 如果list不存在 则创建list并进行push操作
     * @param keyPrefix
     * @param key
     * @param value
     * @return
     */
    public long rPush(KeyPrefix keyPrefix, String key, String... value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.rpush(realKey,value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return -1;
    }

    /**
     * 可以用
     * 获取list中 指定位置的值
     * index从0开始
     * 如果 index 参数的值不在列表的区间范围内(out of range)，返回 nil
     * @param keyPrefix
     * @param key
     * @param index
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T lget(KeyPrefix keyPrefix, String key, int index, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            String str = jedis.lindex(realKey,index);
            return stringToBean(str,clazz);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return null;
    }

    /**
     * 获取指定范围的记录
     * lrange 下标从0开始 -1表示最后一个元素
     * @param keyPrefix
     * @param key
     * @param start
     * @param end
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> lRange(KeyPrefix keyPrefix, String key, long start, long end, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            List<String> list = jedis.lrange(realKey,start,end);
            if (list.size() != 0){
                return listToBean(list,clazz);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return null;
    }

    /**
     * 移除并返回列表 key 左边的 第一个值
     * 当 key 不存在时，返回 nil
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T lPop(KeyPrefix keyPrefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            String result = jedis.lpop(realKey);
            return stringToBean(result,clazz);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return null;
    }

    /**
     * 移除并返回列表 key 右边的 第一个值
     * 当 key 不存在时，返回 nil
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T rPop(KeyPrefix keyPrefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            String result = jedis.rpop(realKey);
            return stringToBean(result,clazz);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return null;
    }

    public String clear(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            String result = jedis.ltrim(realKey,0,0);
            jedis.lpop(realKey);
            return result;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            close(jedis);
        }
        return null;
    }



    //--------------------util---------------------//



    public <T> Map<String,T> stringsToBeans(Map<String,String> map,Class<T> clazz){
        Map<String,T> newMap = new HashMap<>();
        for (Map.Entry<String,String> entry:map.entrySet()){
            newMap.put(entry.getKey(),stringToBean(entry.getValue(),clazz));
        }
        return newMap;
    }

    /**
     * list<String> 转化为list<T>
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> listToBean(List<String> list,Class<T> clazz){
        List<T> newList = new ArrayList<>();
        for (String str:list){
            newList.add(stringToBean(str,clazz));
        }
        return newList;
    }

    public <T> Map<String, String> beansToString(Map<String,T> map){
        Map<String,String> newMap = new HashMap<>();
        for (Map.Entry<String,T> entry:map.entrySet()){
            newMap.put(entry.getKey(),beanToString(entry.getValue()));
        }
        return newMap;
    }

    public void close(Jedis jedis){
        if (jedis != null){
            jedis.close();
        }
    }

    /**
     * 将bean转换为string
     * @param value
     * @param <T>
     * @return
     */
    public <T> String beanToString(T value){
        if (value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class){
            return String.valueOf(value);
        }else if (clazz == long.class || clazz == Long.class){
            return String.valueOf(value);
        }else if (clazz == String.class){
            return (String) value;
        }else {
            return gson.toJson(value);
        }
    }

    /**
     * 将string转化为bean
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T stringToBean(String str,Class<T> clazz){
        if (str == null || str.length() <= 0 || clazz == null){
            return null;
        }
        if (clazz == int.class || clazz == Integer.class){
            return (T) Integer.valueOf(str);
        }else if (clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(str);
        }else if (clazz == String.class){
            return (T) str;
        }else {
            return gson.fromJson(str,clazz);
        }
    }
}
