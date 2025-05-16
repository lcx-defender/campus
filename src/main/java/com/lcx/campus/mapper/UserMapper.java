package com.lcx.campus.mapper;

import com.lcx.campus.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * @param username 身份证号、手机号、邮箱
     * @return 用户信息
     */
    @Select("select * from sys_user where identity = #{username} or phone = #{username} or email = #{username}")
    User selectUserByUserName(String username);


    User selectUserByStudentId(String studentId);
}
