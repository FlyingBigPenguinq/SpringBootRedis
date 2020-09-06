package com.study.boot.Redis.server.services;

import cn.hutool.core.date.DateTime;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.study.boot.Redis.model.dto.ArticlePraiseRankDto;
import com.study.boot.Redis.model.dto.PraiseDto;
import com.study.boot.Redis.model.entity.Article;
import com.study.boot.Redis.model.entity.ArticlePraise;
import com.study.boot.Redis.model.mapper.ArticleMapper;
import com.study.boot.Redis.model.mapper.ArticlePraiseMapper;
import com.study.boot.Redis.model.mapper.UserMapper;
import com.study.boot.Redis.server.enums.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName PraiseService
 * @Description: TODO
 * @Author lxl
 * @Date 2020/9/5
 * @Version V1.0
 **/
@Service
public class PraiseService {

    private static final Logger log = LoggerFactory.getLogger(PraiseService.class);

    private static final String SplitChar = "-";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticlePraiseMapper articlePraiseMapper;

    @Autowired
    private UserMapper userMapper;
    public List<Article> getAll() {
        return articleMapper.selectAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean praiseOn(PraiseDto dto) throws Exception {
        final String key = Constant.RedisArticlePraiseUser + dto.getArticleId() + dto.getUserId();
        //判断当前用户是否已经点暂当前文章   --并发操作的风险
         Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(key, 1);//原子操作如果存在设置为1
        //如果可以将当前的数据插入DB
        if (ifAbsent){
            ArticlePraise entity = new ArticlePraise(dto.getArticleId(), dto.getUserId(), DateTime.now().toSqlDate());
            int res = articlePraiseMapper.insertSelective(entity);
            if (res > 0){
                articleMapper.updatePraiseTotal(dto.getArticleId(), 1);
                //缓存点暂的相关信息
                this.RedisPraiseOn(dto);
            }
        }


        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean praiseCancel(PraiseDto dto) throws Exception {
        final String key = Constant.RedisArticlePraiseUser + dto.getArticleId() + dto.getUserId();
        //查看当前用户是否点赞过当前文章
        Boolean canCancel = redisTemplate.opsForValue().setIfAbsent(key, 1);
        if (canCancel){
            //移除DB 中的记录
            int res = articlePraiseMapper.cancelPraise(dto.getArticleId(), dto.getUserId());
            if (res > 0) {
                //移除缓存中的是否点赞的key
                redisTemplate.delete(key);
                //更新文章的点赞量
                articleMapper.updatePraiseTotal(dto.getArticleId(), -1);
                //缓存中取消点赞的相关信息
                this.redisPraiseCancel(dto);
            }
        }
        return true;
    }

    public Map<String,Object> getArticleInfo(Integer articleId, Integer currUserId) throws Exception{
        Map resMap = Maps.newHashMap();
        resMap.put("ArticleInfo", articleMapper.selectByPK(articleId));

        //添加用户昵称的列表
        HashOperations<String, String, Set<Integer>> hashOperations = redisTemplate.opsForHash();
        Set<Integer> userIds = hashOperations.get(Constant.RedisArticlePraiseHashKey, articleId);
        if (userIds != null && !userIds.isEmpty()){
            resMap.put("PraiseUserId", userIds);
            //ids序列化
            String ids = Joiner.on(",").join(userIds);
            resMap.put("UserName", userMapper.selectNamesById(ids));
            //当前用户Id是否点赞过当前文章
            if (currUserId != null){
                resMap.put("CurrUserHasPrasice", userIds.contains(currUserId));
            }
        }
        //获取点赞排行榜由高到底的排行
        List<ArticlePraiseRankDto> rankDtos = Lists.newLinkedList();
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        Long total = zSetOperations.size(Constant.RedisArticlePraiseSortKey);
        Set<TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(Constant.RedisArticlePraiseSortKey, 0L, total);
        typedTuples.forEach(t->{
            String[] strs = StringUtils.split(SplitChar, t.getValue());
            String aId = strs[0];
            String aTitle = strs[1];
            Double score = t.getScore();
            rankDtos.add(new ArticlePraiseRankDto(aId, aTitle, score.toString(),score));
        });
        resMap.put("RedisPraise", rankDtos);
        return resMap;
    }

    public Map<String,Object> getUserArticles(final Integer currUserId) throws Exception{
        Map<String,Object> resMap=Maps.newHashMap();

        //用户详情-直接db查询
        resMap.put("userInfo~用户详情",userMapper.selectByPrimaryKey(currUserId));

        //用户点赞过的历史文章-查redis的hash
        List<PraiseDto> userArticles=Lists.newLinkedList();
        HashOperations<String,String,String> hash=redisTemplate.opsForHash();
        //field-value对
        Map<String,String> map=hash.entries(Constant.RedisArticleUserPraiseKey);
        for (Map.Entry<String, String> entry:map.entrySet()){
            String field=entry.getKey();
            String value=entry.getValue();

            String[] arr=StringUtils.split(field,SplitChar);

            //判断 “文章标题是否为空” - 如果为空，则代表已经取消点赞了
            if (StringUtils.isNotBlank(value)){
                //判断当前 arr[0] 是否为当前用户id，如果是，则代表 arr[1] 为当前用户点赞过的文章id
                if (currUserId.toString().equals(arr[0])){
                    userArticles.add(new PraiseDto(currUserId,Integer.valueOf(arr[1]),value));
                }
            }
        }
        resMap.put("userPraiseArticles~用户点赞过的历史文章",userArticles);

        return resMap;
    }

    private void RedisPraiseOn(final PraiseDto dto) throws Exception{
        HashOperations<String, String, Set<Integer>> hashOperations = redisTemplate.opsForHash();
        Set<Integer> uIds = hashOperations.get(Constant.RedisArticlePraiseHashKey, dto.getArticleId().toString());
        //记录点赞的　明细
        if (uIds == null || uIds.isEmpty()){
            uIds = Sets.newHashSet();
        }
        uIds.add(dto.getUserId());
        hashOperations.put(Constant.RedisArticlePraiseHashKey,dto.getArticleId().toString(),uIds);
        //缓存点暂排行榜
        this.cacheArticlePraiseRank(dto, uIds.size());
        //缓存用户点赞记录

    }

    //清除缓存里面的信息
    private void redisPraiseCancel(final PraiseDto dto) throws Exception{
        HashOperations<String, String, Set<Integer>> hashOperations = redisTemplate.opsForHash();
        Set<Integer> uIds = hashOperations.get(Constant.RedisArticlePraiseHashKey, dto.getArticleId().toString());
        if (uIds != null && !uIds.isEmpty() && uIds.contains(dto.getUserId())){
            uIds.remove(dto.getUserId());
            hashOperations.put(Constant.RedisArticlePraiseHashKey,dto.getArticleId().toString(),uIds);
        }
        //删除点暂排行榜　的相关信息
        this.cacheArticlePraiseRank(dto, uIds.size());
        //缓存用户点赞记录　更新
    }

    /**
     * @MethodName: cacheArticlePraiseRank
     * @Description: TODO: 设置缓存里面的点赞排行榜的分数
     * @Param: [praiseDto, total]
     * @Return: void
     * @Author: lxl
     * @Date: 上午1:11
    **/
    private void cacheArticlePraiseRank(final PraiseDto praiseDto, final Integer total){
        final String key = praiseDto.getArticleId() + SplitChar + praiseDto.getTitle();
        ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
        //先清除之前的记录
        zSetOperations.remove(Constant.RedisArticlePraiseSortKey, key);
        //插入新值
        zSetOperations.add(Constant.RedisArticlePraiseSortKey, key, total.doubleValue());
    }

    /**
     * @MethodName: cacheUserPraiseArticle
     * @Description: TODO: 缓存用户点赞过的文章
     * @Param: [dto, isOn]
     * @Return: void
     * @Author: lxl
     * @Date: 上午1:52
    **/
    private void cacheUserPraiseArticle(final PraiseDto dto,Boolean isOn) {
        final String fieldKey = dto.getUserId() + SplitChar + dto.getArticleId();

        HashOperations<String, String, String> hash = redisTemplate.opsForHash();
        if (isOn) {
            hash.put(Constant.RedisArticleUserPraiseKey, fieldKey, dto.getTitle());
        } else {
            hash.put(Constant.RedisArticleUserPraiseKey, fieldKey, "");
        }
    }
}
