package com.bh.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.MessageQSet;

public interface MessageQSetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageQSet record);

    int insertSelective(MessageQSet record);

    MessageQSet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessageQSet record);

    int updateByPrimaryKey(MessageQSet record);
    
    
    
    List<MessageQSet> selectAll();
    
    List<MessageQSet> pageList(@Param("type")String type);
}