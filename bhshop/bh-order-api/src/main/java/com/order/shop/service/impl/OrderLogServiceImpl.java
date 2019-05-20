package com.order.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.order.mapper.OrderLogMapper;
import com.bh.order.pojo.OrderLog;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.order.shop.service.OrderLogService;

import groovy.xml.Entity;

/**
 * 
 * @author xxj
 *
 */
@Service
@Transactional
public class OrderLogServiceImpl implements OrderLogService{
	@Autowired
	private OrderLogMapper logMapper; //订单日志

	
	/**
	 * 后台获取日志分页列表
	 */
	@Override
	public PageBean<OrderLog> PageAll(String orderNo, String action, String currentPage, String pageSize, int shopId) throws Exception {
		OrderLog entity = new OrderLog();
		entity.setOrderNo(orderNo);
		entity.setAction(action);
		entity.setShopId(shopId);
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<OrderLog> list = logMapper.getAllList(entity);
		PageBean<OrderLog> pageBean = new PageBean<>(list);
		return pageBean;
	}
	@Override
	public int insert(OrderLog record) {
		// TODO Auto-generated method stub
		return logMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderLog record) {
		// TODO Auto-generated method stub
		return logMapper.insertSelective(record);
	}

}
