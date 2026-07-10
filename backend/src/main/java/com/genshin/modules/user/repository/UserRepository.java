package com.genshin.modules.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.genshin.modules.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserRepository extends BaseMapper<SysUser> {

    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    Optional<SysUser> findByUsername(String username);

    @Select("SELECT EXISTS(SELECT 1 FROM sys_user WHERE username = #{username})")
    boolean existsByUsername(String username);
}
