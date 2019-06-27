package com.netease.repository.mysql.dao.core;

import com.netease.common.entity.mysql.WechatUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/19
 */
public interface WechatUserDao {

    String CACHE_PREFIX = "r.wud";

    String TABLE_NAME = "cubex_wechat_user";

    String COLUMNS_WITHOUT_ID = "openid, nick_name, phone, active, bind_time, db_update_time, db_create_time";

    String COLUMNS = "id, " + COLUMNS_WITHOUT_ID;

    @Insert("insert into " + TABLE_NAME + "(" + COLUMNS_WITHOUT_ID + ")" + " values " + "(#{openid}, #{nickName}, #{phone}, 1, #{bindTime}, now(), now())")
    Integer insert(WechatUser wechatUser);

    @Cacheable(cacheNames = CACHE_PREFIX + ".id", key = "#p0")
    @Select("select " + COLUMNS + " from " + TABLE_NAME + " where id = #{id}")
    WechatUser queryById(@Param("id") Long id);
}
