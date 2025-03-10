package com.yt.app.common.util;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Redis工具类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/11/27 14:38
 */
@Component
public class RedisUtil {

	private static StringRedisTemplate stringRedisTemplate;

	@Autowired
	public RedisUtil(StringRedisTemplate stringRedisTemplate) {
		RedisUtil.stringRedisTemplate = stringRedisTemplate;
	}

	/** -------------------key相关操作--------------------- */

	/**
	 * 删除key
	 */
	public static void delete(String key) {
		stringRedisTemplate.delete(key);
	}

	/**
	 * 批量删除key
	 */
	public static void delete(Collection<String> keys) {
		stringRedisTemplate.delete(keys);
	}

	/**
	 * 序列化key
	 */
	public static byte[] dump(String key) {
		return stringRedisTemplate.dump(key);
	}

	/**
	 * 是否存在key
	 */
	public static Boolean hasKey(String key) {
		return stringRedisTemplate.hasKey(key);
	}

	/**
	 * 设置过期时间
	 */
	public static Boolean expire(String key, long timeout, TimeUnit unit) {
		return stringRedisTemplate.expire(key, timeout, unit);
	}

	/**
	 * 设置过期时间
	 */
	public static Boolean expireAt(String key, Date date) {
		return stringRedisTemplate.expireAt(key, date);
	}

	/**
	 * 查找匹配的key
	 */
	public static Set<String> keys(String pattern) {
		return stringRedisTemplate.keys(pattern);
	}

	/**
	 * 将当前数据库的 key 移动到给定的数据库 db 当中
	 */
	public static Boolean move(String key, int dbIndex) {
		return stringRedisTemplate.move(key, dbIndex);
	}

	/**
	 * 移除 key 的过期时间，key 将持久保持
	 */
	public static Boolean persist(String key) {
		return stringRedisTemplate.persist(key);
	}

	/**
	 * 返回 key 的剩余的过期时间
	 */
	public static Long getExpire(String key, TimeUnit unit) {
		return stringRedisTemplate.getExpire(key, unit);
	}

	/**
	 * 返回 key 的剩余的过期时间
	 */
	public static Long getExpire(String key) {
		return stringRedisTemplate.getExpire(key);
	}

	/**
	 * 从当前数据库中随机返回一个 key
	 */
	public static String randomKey() {
		return stringRedisTemplate.randomKey();
	}

	/**
	 * 修改 key 的名称
	 */
	public static void rename(String oldKey, String newKey) {
		stringRedisTemplate.rename(oldKey, newKey);
	}

	/**
	 * 仅当 newkey 不存在时，将 oldKey 改名为 newkey
	 */
	public static Boolean renameIfAbsent(String oldKey, String newKey) {
		return stringRedisTemplate.renameIfAbsent(oldKey, newKey);
	}

	/**
	 * 返回 key 所储存的值的类型
	 */
	public static DataType type(String key) {
		return stringRedisTemplate.type(key);
	}

	/** -------------------string相关操作--------------------- */

	/**
	 * 设置指定 key 的值
	 */
	public static void set(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 获取指定 key 的值
	 */
	public static String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	/**
	 * 返回 key 中字符串值的子字符
	 */
	public static String getRange(String key, long start, long end) {
		return stringRedisTemplate.opsForValue().get(key, start, end);
	}

	/**
	 * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
	 */
	public static String getAndSet(String key, String value) {
		return stringRedisTemplate.opsForValue().getAndSet(key, value);
	}

	/**
	 * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
	 */
	public static Boolean getBit(String key, long offset) {
		return stringRedisTemplate.opsForValue().getBit(key, offset);
	}

	/**
	 * 批量获取
	 */
	public static List<String> multiGet(Collection<String> keys) {
		return stringRedisTemplate.opsForValue().multiGet(keys);
	}

	/**
	 * 设置ASCII码, 字符串'a'的ASCII码是97, 转为二进制是'01100001', 此方法是将二进制第offset位值变为value
	 *
	 * @param key
	 * @param offset 位置
	 * @param value  值,true为1, false为0
	 * @return
	 */
	public static boolean setBit(String key, long offset, boolean value) {
		return stringRedisTemplate.opsForValue().setBit(key, offset, value);
	}

	/**
	 * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
	 *
	 * @param key     缓存key
	 * @param value   值
	 * @param timeout 过期时间
	 * @param unit    时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
	 *                秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
	 */
	public static void setEx(String key, String value, long timeout, TimeUnit unit) {
		stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	/**
	 * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
	 *
	 * @param key     缓存key
	 * @param value   值
	 * @param timeout 过期时间
	 * @param unit    时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
	 *                秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
	 */
	public static void setEx(String key, Object value, long timeout, TimeUnit unit) {
		stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), timeout, unit);
	}

	/**
	 * 只有在 key 不存在时设置 key 的值
	 *
	 * @param key
	 * @param value
	 * @return 之前已经存在返回false, 不存在返回true
	 */
	public static boolean setIfAbsent(String key, String value) {
		return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
	}

	/**
	 * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始
	 *
	 * @param key
	 * @param value
	 * @param offset 从指定位置开始覆写
	 */
	public static void setRange(String key, String value, long offset) {
		stringRedisTemplate.opsForValue().set(key, value, offset);
	}

	/**
	 * 获取字符串的长度
	 */
	public static Long size(String key) {
		return stringRedisTemplate.opsForValue().size(key);
	}

	/**
	 * 批量添加
	 */
	public static void multiSet(Map<String, String> maps) {
		stringRedisTemplate.opsForValue().multiSet(maps);
	}

	/**
	 * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
	 *
	 * @param maps
	 * @return 之前已经存在返回false, 不存在返回true
	 */
	public static boolean multiSetIfAbsent(Map<String, String> maps) {
		return stringRedisTemplate.opsForValue().multiSetIfAbsent(maps);
	}

	/**
	 * 增加(自增长), 负数则为自减
	 *
	 * @param key
	 * @param value
	 * @return key对应value值
	 */
	public static Long incrBy(String key, long increment) {
		return stringRedisTemplate.opsForValue().increment(key, increment);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public static Double incrByFloat(String key, double increment) {
		return stringRedisTemplate.opsForValue().increment(key, increment);
	}

	/**
	 * 追加到末尾
	 */
	public static Integer append(String key, String value) {
		return stringRedisTemplate.opsForValue().append(key, value);
	}

	/** -------------------hash相关操作------------------------- */

	/**
	 * 获取存储在哈希表中指定字段的值
	 */
	public static Object hGet(String key, String field) {
		return stringRedisTemplate.opsForHash().get(key, field);
	}

	/**
	 * 获取所有给定字段的值
	 */
	public static Map<Object, Object> hGetAll(String key) {
		return stringRedisTemplate.opsForHash().entries(key);
	}

	/**
	 * 获取所有给定字段的值
	 */
	public static List<Object> hMultiGet(String key, Collection<Object> fields) {
		return stringRedisTemplate.opsForHash().multiGet(key, fields);
	}

	public static void hPut(String key, String hashKey, String value) {
		stringRedisTemplate.opsForHash().put(key, hashKey, value);
	}

	public static void hPutAll(String key, Map<String, String> maps) {
		stringRedisTemplate.opsForHash().putAll(key, maps);
	}

	/**
	 * 仅当hashKey不存在时才设置
	 */
	public static Boolean hPutIfAbsent(String key, String hashKey, String value) {
		return stringRedisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
	}

	/**
	 * 删除一个或多个哈希表字段
	 */
	public static Long hDelete(String key, Object... fields) {
		return stringRedisTemplate.opsForHash().delete(key, fields);
	}

	/**
	 * 查看哈希表 key 中，指定的字段是否存在
	 */
	public static boolean hExists(String key, String field) {
		return stringRedisTemplate.opsForHash().hasKey(key, field);
	}

	/**
	 * 为哈希表 key 中的指定字段的整数值加上增量 increment
	 */
	public static Long hIncrBy(String key, Object field, long increment) {
		return stringRedisTemplate.opsForHash().increment(key, field, increment);
	}

	/**
	 * 为哈希表 key 中的指定字段的整数值加上增量 increment
	 */
	public static Double hIncrByFloat(String key, Object field, double delta) {
		return stringRedisTemplate.opsForHash().increment(key, field, delta);
	}

	/**
	 * 获取所有哈希表中的字段
	 */
	public static Set<Object> hKeys(String key) {
		return stringRedisTemplate.opsForHash().keys(key);
	}

	/**
	 * 获取哈希表中字段的数量
	 */
	public static Long hSize(String key) {
		return stringRedisTemplate.opsForHash().size(key);
	}

	/**
	 * 获取哈希表中所有值
	 */
	public static List<Object> hValues(String key) {
		return stringRedisTemplate.opsForHash().values(key);
	}

	/**
	 * 迭代哈希表中的键值对
	 */
	public static Cursor<Entry<Object, Object>> hScan(String key, ScanOptions options) {
		return stringRedisTemplate.opsForHash().scan(key, options);
	}

	/** ------------------------list相关操作---------------------------- */

	/**
	 * 通过索引获取列表中的元素
	 */
	public static String lIndex(String key, long index) {
		return stringRedisTemplate.opsForList().index(key, index);
	}

	/**
	 * 获取列表指定范围内的元素
	 *
	 * @param key
	 * @param start 开始位置, 0是开始位置
	 * @param end   结束位置, -1返回所有
	 * @return
	 */
	public static List<String> lRange(String key, long start, long end) {
		return stringRedisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 存储在list头部
	 */
	public static Long lLeftPush(String key, String value) {
		return stringRedisTemplate.opsForList().leftPush(key, value);
	}

	public static Long lLeftPushAll(String key, String... value) {
		return stringRedisTemplate.opsForList().leftPushAll(key, value);
	}

	public static Long lLeftPushAll(String key, Collection<String> value) {
		return stringRedisTemplate.opsForList().leftPushAll(key, value);
	}

	/**
	 * 当list存在的时候才加入
	 */
	public static Long lLeftPushIfPresent(String key, String value) {
		return stringRedisTemplate.opsForList().leftPushIfPresent(key, value);
	}

	/**
	 * 如果pivot存在,再pivot前面添加
	 */
	public static Long lLeftPush(String key, String pivot, String value) {
		return stringRedisTemplate.opsForList().leftPush(key, pivot, value);
	}

	public static Long lRightPush(String key, String value) {
		return stringRedisTemplate.opsForList().rightPush(key, value);
	}

	public static Long lRightPushAll(String key, String... value) {
		return stringRedisTemplate.opsForList().rightPushAll(key, value);
	}

	public static Long lRightPushAll(String key, Collection<String> value) {
		return stringRedisTemplate.opsForList().rightPushAll(key, value);
	}

	/**
	 * 为已存在的列表添加值
	 */
	public static Long lRightPushIfPresent(String key, String value) {
		return stringRedisTemplate.opsForList().rightPushIfPresent(key, value);
	}

	/**
	 * 在pivot元素的右边添加值
	 */
	public static Long lRightPush(String key, String pivot, String value) {
		return stringRedisTemplate.opsForList().rightPush(key, pivot, value);
	}

	/**
	 * 通过索引设置列表元素的值
	 *
	 * @param key
	 * @param index 位置
	 * @param value
	 */
	public static void lSet(String key, long index, String value) {
		stringRedisTemplate.opsForList().set(key, index, value);
	}

	/**
	 * 移出并获取列表的第一个元素
	 *
	 * @param key
	 * @return 删除的元素
	 */
	public static String lLeftPop(String key) {
		return stringRedisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
	 *
	 * @param key
	 * @param timeout 等待时间
	 * @param unit    时间单位
	 * @return
	 */
	public static String lBLeftPop(String key, long timeout, TimeUnit unit) {
		return stringRedisTemplate.opsForList().leftPop(key, timeout, unit);
	}

	/**
	 * 移除并获取列表最后一个元素
	 *
	 * @param key
	 * @return 删除的元素
	 */
	public static String lRightPop(String key) {
		return stringRedisTemplate.opsForList().rightPop(key);
	}

	/**
	 * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
	 *
	 * @param key
	 * @param timeout 等待时间
	 * @param unit    时间单位
	 * @return
	 */
	public static String lBRightPop(String key, long timeout, TimeUnit unit) {
		return stringRedisTemplate.opsForList().rightPop(key, timeout, unit);
	}

	/**
	 * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
	 */
	public static String lRightPopAndLeftPush(String sourceKey, String destinationKey) {
		return stringRedisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
	}

	/**
	 * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
	 */
	public static String lBRightPopAndLeftPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit) {
		return stringRedisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
	}

	/**
	 * 删除集合中值等于value得元素
	 *
	 * @param key
	 * @param index index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
	 *              index<0, 从尾部开始删除第一个值等于value的元素;
	 * @param value
	 * @return
	 */
	public static Long lRemove(String key, long index, String value) {
		return stringRedisTemplate.opsForList().remove(key, index, value);
	}

	/**
	 * 裁剪list
	 */
	public static void lTrim(String key, long start, long end) {
		stringRedisTemplate.opsForList().trim(key, start, end);
	}

	/**
	 * 获取列表长度
	 */
	public static Long lLen(String key) {
		return stringRedisTemplate.opsForList().size(key);
	}

	/** --------------------set相关操作-------------------------- */

	/**
	 * set添加元素
	 */
	public static Long sAdd(String key, String... values) {
		return stringRedisTemplate.opsForSet().add(key, values);
	}

	/**
	 * set移除元素
	 */
	public static Long sRemove(String key, Object... values) {
		return stringRedisTemplate.opsForSet().remove(key, values);
	}

	/**
	 * 移除并返回集合的一个随机元素
	 */
	public static String sPop(String key) {
		return stringRedisTemplate.opsForSet().pop(key);
	}

	/**
	 * 将元素value从一个集合移到另一个集合
	 */
	public static Boolean sMove(String key, String value, String destKey) {
		return stringRedisTemplate.opsForSet().move(key, value, destKey);
	}

	/**
	 * 获取集合的大小
	 */
	public static Long sSize(String key) {
		return stringRedisTemplate.opsForSet().size(key);
	}

	/**
	 * 判断集合是否包含value
	 */
	public static Boolean sIsMember(String key, Object value) {
		return stringRedisTemplate.opsForSet().isMember(key, value);
	}

	/**
	 * 获取两个集合的交集
	 */
	public static Set<String> sIntersect(String key, String otherKey) {
		return stringRedisTemplate.opsForSet().intersect(key, otherKey);
	}

	/**
	 * 获取key集合与多个集合的交集
	 */
	public static Set<String> sIntersect(String key, Collection<String> otherKeys) {
		return stringRedisTemplate.opsForSet().intersect(key, otherKeys);
	}

	/**
	 * key集合与otherKey集合的交集存储到destKey集合中
	 */
	public static Long sIntersectAndStore(String key, String otherKey, String destKey) {
		return stringRedisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
	}

	/**
	 * key集合与多个集合的交集存储到destKey集合中
	 */
	public static Long sIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
		return stringRedisTemplate.opsForSet().intersectAndStore(key, otherKeys, destKey);
	}

	/**
	 * 获取两个集合的并集
	 */
	public static Set<String> sUnion(String key, String otherKeys) {
		return stringRedisTemplate.opsForSet().union(key, otherKeys);
	}

	/**
	 * 获取key集合与多个集合的并集
	 */
	public static Set<String> sUnion(String key, Collection<String> otherKeys) {
		return stringRedisTemplate.opsForSet().union(key, otherKeys);
	}

	/**
	 * key集合与otherKey集合的并集存储到destKey中
	 */
	public static Long sUnionAndStore(String key, String otherKey, String destKey) {
		return stringRedisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
	}

	/**
	 * key集合与多个集合的并集存储到destKey中
	 */
	public static Long sUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
		return stringRedisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
	}

	/**
	 * 获取两个集合的差集
	 */
	public static Set<String> sDifference(String key, String otherKey) {
		return stringRedisTemplate.opsForSet().difference(key, otherKey);
	}

	/**
	 * 获取key集合与多个集合的差集
	 */
	public static Set<String> sDifference(String key, Collection<String> otherKeys) {
		return stringRedisTemplate.opsForSet().difference(key, otherKeys);
	}

	/**
	 * key集合与otherKey集合的差集存储到destKey中
	 */
	public static Long sDifference(String key, String otherKey, String destKey) {
		return stringRedisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
	}

	/**
	 * key集合与多个集合的差集存储到destKey中
	 */
	public static Long sDifference(String key, Collection<String> otherKeys, String destKey) {
		return stringRedisTemplate.opsForSet().differenceAndStore(key, otherKeys, destKey);
	}

	/**
	 * 获取集合所有元素
	 */
	public static Set<String> setMembers(String key) {
		return stringRedisTemplate.opsForSet().members(key);
	}

	/**
	 * 随机获取集合中的一个元素
	 */
	public static String sRandomMember(String key) {
		return stringRedisTemplate.opsForSet().randomMember(key);
	}

	/**
	 * 随机获取集合中count个元素
	 */
	public static List<String> sRandomMembers(String key, long count) {
		return stringRedisTemplate.opsForSet().randomMembers(key, count);
	}

	/**
	 * 随机获取集合中count个元素并且去除重复的
	 */
	public static Set<String> sDistinctRandomMembers(String key, long count) {
		return stringRedisTemplate.opsForSet().distinctRandomMembers(key, count);
	}

	public static Cursor<String> sScan(String key, ScanOptions options) {
		return stringRedisTemplate.opsForSet().scan(key, options);
	}

	/** ------------------zSet相关操作-------------------------------- */

	/**
	 * 添加元素,有序集合是按照元素的score值由小到大排列
	 */
	public static Boolean zAdd(String key, String value, double score) {
		return stringRedisTemplate.opsForZSet().add(key, value, score);
	}

	public static Long zAdd(String key, Set<TypedTuple<String>> values) {
		return stringRedisTemplate.opsForZSet().add(key, values);
	}

	public static Long zRemove(String key, Object... values) {
		return stringRedisTemplate.opsForZSet().remove(key, values);
	}

	/**
	 * 增加元素的score值，并返回增加后的值
	 */
	public static Double zIncrementScore(String key, String value, double delta) {
		return stringRedisTemplate.opsForZSet().incrementScore(key, value, delta);
	}

	/**
	 * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
	 *
	 * @param key
	 * @param value
	 * @return 0表示第一位
	 */
	public static Long zRank(String key, Object value) {
		return stringRedisTemplate.opsForZSet().rank(key, value);
	}

	/**
	 * 返回元素在集合的排名,按元素的score值由大到小排列
	 */
	public static Long zReverseRank(String key, Object value) {
		return stringRedisTemplate.opsForZSet().reverseRank(key, value);
	}

	/**
	 * 获取集合的元素, 从小到大排序
	 *
	 * @param key
	 * @param start 开始位置
	 * @param end   结束位置, -1查询所有
	 * @return
	 */
	public static Set<String> zRange(String key, long start, long end) {
		return stringRedisTemplate.opsForZSet().range(key, start, end);
	}

	/**
	 * 获取集合元素, 并且把score值也获取
	 */
	public static Set<TypedTuple<String>> zRangeWithScores(String key, long start, long end) {
		return stringRedisTemplate.opsForZSet().rangeWithScores(key, start, end);
	}

	/**
	 * 根据Score值查询集合元素
	 *
	 * @param key
	 * @param min 最小值
	 * @param max 最大值
	 * @return
	 */
	public static Set<String> zRangeByScore(String key, double min, double max) {
		return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
	}

	/**
	 * 根据Score值查询集合元素, 从小到大排序
	 *
	 * @param key
	 * @param min 最小值
	 * @param max 最大值
	 * @return
	 */
	public static Set<TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max) {
		return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
	}

	public static Set<TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max, long start, long end) {
		return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, start, end);
	}

	/**
	 * 获取集合的元素, 从大到小排序
	 */
	public static Set<String> zReverseRange(String key, long start, long end) {
		return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
	}

	/**
	 * 获取集合的元素, 从大到小排序, 并返回score值
	 */
	public static Set<TypedTuple<String>> zReverseRangeWithScores(String key, long start, long end) {
		return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
	}

	/**
	 * 根据Score值查询集合元素, 从大到小排序
	 */
	public static Set<String> zReverseRangeByScore(String key, double min, double max) {
		return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
	}

	/**
	 * 根据Score值查询集合元素, 从大到小排序
	 */
	public static Set<TypedTuple<String>> zReverseRangeByScoreWithScores(String key, double min, double max) {
		return stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
	}

	public static Set<String> zReverseRangeByScore(String key, double min, double max, long start, long end) {
		return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max, start, end);
	}

	/**
	 * 根据score值获取集合元素数量
	 */
	public static Long zCount(String key, double min, double max) {
		return stringRedisTemplate.opsForZSet().count(key, min, max);
	}

	/**
	 * 获取集合大小
	 */
	public static Long zSize(String key) {
		return stringRedisTemplate.opsForZSet().size(key);
	}

	/**
	 * 获取集合大小
	 */
	public static Long zZCard(String key) {
		return stringRedisTemplate.opsForZSet().zCard(key);
	}

	/**
	 * 获取集合中value元素的score值
	 */
	public static Double zScore(String key, Object value) {
		return stringRedisTemplate.opsForZSet().score(key, value);
	}

	/**
	 * 移除指定索引位置的成员
	 */
	public static Long zRemoveRange(String key, long start, long end) {
		return stringRedisTemplate.opsForZSet().removeRange(key, start, end);
	}

	/**
	 * 根据指定的score值的范围来移除成员
	 */
	public static Long zRemoveRangeByScore(String key, double min, double max) {
		return stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
	}

	/**
	 * 获取key和otherKey的并集并存储在destKey中
	 */
	public static Long zUnionAndStore(String key, String otherKey, String destKey) {
		return stringRedisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
	}

	public static Long zUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
		return stringRedisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
	}

	/**
	 * 交集
	 */
	public static Long zIntersectAndStore(String key, String otherKey, String destKey) {
		return stringRedisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
	}

	/**
	 * 交集
	 */
	public static Long zIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
		return stringRedisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
	}

	public static Cursor<TypedTuple<String>> zScan(String key, ScanOptions options) {
		return stringRedisTemplate.opsForZSet().scan(key, options);
	}

	// ============================ ↓↓↓↓↓↓ 发布订阅 ↓↓↓↓↓↓ ============================

	/**
	 * 发布消息
	 *
	 * @param channel 通道
	 * @param msg     消息内容
	 * @return void
	 * @author zhengqingya
	 * @date 2022/6/17 17:29
	 */
	public static void publish(String channel, String msg) {
		stringRedisTemplate.convertAndSend(channel, msg);
	}

}
