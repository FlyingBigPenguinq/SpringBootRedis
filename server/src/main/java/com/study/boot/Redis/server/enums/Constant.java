package com.study.boot.Redis.server.enums;/**
 * Created by Administrator on 2019/10/29.
 */

/**
 * 系统常量配置
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2019/10/29 21:10
 **/
public class Constant {

    public static final String RedisStringPrefix="SpringBootRedis:String:V1:";

    public static final String RedisListPrefix="SpringBootRedis:List:User:V1:";

    public static final String RedisListNoticeKey="SpringBootRedis:List:Queue:Notice:V1";

    public static final String RedisSetKey="SpringBoot:Redis:Set:User:Email";

    public static final String RedisSetProblemKey="SpringBoot:Redis:Set:Problem";

    public static final String RedisSetProblemsKey="SpringBoot:Redis:Set:Problems:V1";

    public static final String RedisSortedSetKey1 ="SpringBootRedis:SortedSet:PhoneFare:key1";

    public static final String RedisSortedSetKey2 ="SpringBootRedis:SortedSet:PhoneFare:key:V1";

    public static final String RedisHashKeyConfig ="SpringBootRedis:Hash:Key:SysConfig:V1";

    public static final String RedisExpireKey ="SpringBootRedis:Key:Expire";

    public static final String RedisRepeatKey ="SpringBootRedis:Key:Expire:Repeat:";


    public static final String RedisCacheBeatLockKey="SpringBootRedis:LockKey:";


    public static final String RedisArticlePraiseUser ="SpringBootRedis:Article:Praise:User:V2:";

    public static final String RedisArticlePraiseHashKey ="SpringBootRedis:Hash:Article:Praise:V2";

    public static final String RedisArticlePraiseSortKey ="SpringBootRedis:Hash:Article:Sort:V2";

    public static final String RedisArticleUserPraiseKey ="SpringBootRedis:Hash:Article:User:Praise:V2";
}























