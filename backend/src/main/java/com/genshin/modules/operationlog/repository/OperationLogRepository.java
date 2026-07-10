package com.genshin.modules.operationlog.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.genshin.modules.operationlog.entity.SysOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OperationLogRepository extends BaseMapper<SysOperationLog> {

    @Select("SELECT * FROM sys_operation_log ORDER BY created_at DESC LIMIT #{limit}")
    List<SysOperationLog> findRecent(int limit);

    @Select("SELECT * FROM sys_operation_log WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit}")
    List<SysOperationLog> findByUserId(Long userId, int limit);
}
