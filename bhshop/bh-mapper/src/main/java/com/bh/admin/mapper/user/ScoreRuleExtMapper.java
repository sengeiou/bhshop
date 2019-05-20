package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.ScoreRuleExt;


public interface ScoreRuleExtMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScoreRuleExt record);

    int insertSelective(ScoreRuleExt record);

    ScoreRuleExt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScoreRuleExt record);

    int updateByPrimaryKey(ScoreRuleExt record);
    
    //根据积分规则ID查询
    List<ScoreRuleExt> selectScoreRuleExtBysrId(ScoreRuleExt record);
    List<ScoreRuleExt> selectScoreRuleExtBysrIdAndKey(ScoreRuleExt rExt);
    int updateScoreRuleExtByValue(ScoreRuleExt ext);
    int updateExtByIsDel();
}