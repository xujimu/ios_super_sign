package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface UserDao {
    /**
     *查询用户
     * @param account
     * @return
     */
    @Select("select * from user where account = #{account}")
    User queryAccount(String account);



    /**
     * 查询所有用户
     * @return
     */
    @Select("select * from user")
    List<User> queryAll();

    /**
     * 修改用户密码
     * @param account
     * @param password
     * @return
     */
    @Update("update user set password = #{password} where account = #{account}")
    int updatePassword(String account,String password);

    /**
     * 减少一次
     * @param account
     * @return
     */
    @Update("update user set count = count - 1 where account = #{account}")
    int reduceCount(String account);

    /**
     * 减少一次
     * @param account
     * @return
     */
    @Update("update user set count = count - #{count} where account = #{account}")
    int reduceCountC(@Param("account") String account,@Param("count") Integer count);

    /**
     * 减少次数
     * @param account
     * @return
     */
    @Update("update user set count = count + #{count} where account = #{account}")
    int addCount(String account,Integer count);

    /**
     * 修改用户类型
     * @param account
     * @param type
     * @return
     */
    @Update("update user set type = #{type} where account = #{account}")
    int updateType(String account,Integer type);

    /**
     * 删除用户
     * @param account
     * @return
     */
    @Delete("delete from user where account = #{account}")
    int deleteAcount(String account);

    /**
     *
     * 新增用户
     * @param user
     * @return
     */
    @Insert("insert into user() values(#{user.id},#{user.account},#{user.password},#{user.createTime},#{user.type},#{user.count})")
    int addAccount(@Param("user") User user);

}
