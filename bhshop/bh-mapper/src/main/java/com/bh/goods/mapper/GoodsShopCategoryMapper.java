package com.bh.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsShopCategory;

public interface GoodsShopCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsShopCategory record);

    int insertSelective(GoodsShopCategory record);

    GoodsShopCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsShopCategory record);

    int updateByPrimaryKey(GoodsShopCategory record);
    
    
    
    List<GoodsShopCategory> getListByFirstReid(String name, Integer currentPage, Integer pageSize, Integer reid);
	
	int countAll(String name, Integer reid);
	
	int delectCount(Integer reid);
	
	List<GoodsShopCategory> selectByParent(Integer reid);
	
	List<GoodsShopCategory> selectAll();
	
	List<GoodsShopCategory> selectThreeLevel();
	
	List<GoodsShopCategory> selectLastLevel();
	
	List<GoodsShopCategory> selectByName(String name);
	
	List<GoodsShopCategory> selectUpdateByName(GoodsShopCategory record);
	 
	List<GoodsShopCategory> selectAllByName();
	
	List<GoodsShopCategory> selectAllByReid(Integer reid);
	
	List<GoodsShopCategory> getBySeries(GoodsShopCategory record);
	
	//cheng
	List<GoodsShopCategory> selectByUserAndParams(GoodsShopCategory g);
}