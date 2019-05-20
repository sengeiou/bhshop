package com.bh.admin.controller.goods;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.admin.pojo.goods.Coupon;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.CouponService;
import com.bh.admin.util.JedisUtil;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;
import com.bh.utils.StringUtil;

@Controller
@RequestMapping("/coupon")
public class CouponController {

	/**
	 * 优惠劵
	 */
	
	@Autowired	
	private CouponService couponService;
	
	
	
	/**
	 * 优惠劵添加操作
	 */
	@RequestMapping("/addCoupon")
	@ResponseBody
	public BhResult addCoupon(@RequestBody Coupon coupon,HttpServletRequest request){
		   BhResult  result = null;
		  
		   try{ 
             
			   if(StringUtils.isEmpty(coupon.getTitle())){
				   result = new BhResult(400,"缺少参数", null);
				   return result;
			   }
			   //获取shopId
			  String token = request.getHeader("token");
			   JedisUtil jedisUtil= JedisUtil.getInstance();  
			   JedisUtil.Strings strings=jedisUtil.new Strings();
			   String userJson = strings.get(token);
			   Map map = JSON.parseObject(userJson, Map.class);
			   Object sId = map.get("shopId");
			   Integer shopId = 1;
			   if(sId!=null){//是商家的操作
			    shopId = (Integer)sId;
			    coupon.setShopId(shopId);
			   }else{//平台操作，shopId为1
				   coupon.setShopId(1);
			   }
			 
			    
			   
			   //把金额元转换成分，字符串转double ，double转integer
			   double amount2 = Double.valueOf(coupon.getAmount2());
			   coupon.setAmount(Integer.parseInt(new java.text.DecimalFormat("0").format(amount2*10*10)));
         
			   double need_amount2 = Double.valueOf(coupon.getNeedAmount2());
			   coupon.setNeedAmount(Integer.parseInt(new java.text.DecimalFormat("0").format(need_amount2*10*10)));
			
               if(coupon.getShopId()>1){
				   coupon.setType(1);//商家
			   }else{
				   coupon.setType(0);//平台
			   }
			   int num = couponService.addCoupon(coupon);
		       if(num>0){
		    	   result = new BhResult(200,"添加成功", null);
		       }else{
		    	   result = new BhResult(400,"添加失败", null);
		       }
			  
		   }catch(Exception e){
			   result = new BhResult(400,"添加失败", null);
			   e.printStackTrace();
		   }
		   return result;
	}
	
	
	/**
	 * 优惠劵修改操作
	 */
	@RequestMapping("/updateCoupon")
	@ResponseBody
	public BhResult updateCoupon(@RequestBody Coupon coupon){
		   BhResult  result = null;
		  
		   try{ 
             
			   if(StringUtils.isEmpty(coupon.getTitle())){
				   result = new BhResult(400,"缺少参数", null);
				   return result;
			   }
               //把金额元转换成分
               double amount2 = Double.valueOf(coupon.getAmount2());
			   coupon.setAmount(Integer.parseInt(new java.text.DecimalFormat("0").format(amount2*10*10)));
         
			   double need_amount2 = Double.valueOf(coupon.getNeedAmount2());
			   coupon.setNeedAmount(Integer.parseInt(new java.text.DecimalFormat("0").format(need_amount2*10*10)));
			   int num = couponService.updateCoupon(coupon);
		       if(num>0){
		    	   result = new BhResult(200,"修改成功", null);
		       }else{
		    	   result = new BhResult(400,"修改失败", null);
		       }
			  
		   }catch(Exception e){
			   result = new BhResult(400,"修改失败", null);
			   e.printStackTrace();
		   }
		   return result;
	}
	

	/**
	 * 优惠劵删除操作
	 */
	@RequestMapping("/deleteCoupon")
	@ResponseBody
	public BhResult deleteCoupon(@RequestBody Coupon coupon){
		   BhResult  result = null;
		  
		   try{ 
               int num = couponService.deleteCoupon(coupon);
		       if(num==1){
		    	   result = new BhResult(200,"删除成功", null);
		       }else if(num==666){
		    	   result = new BhResult(400,"该优惠券已绑定礼包", null);
		       }else{
		    	   result = new BhResult(400,"删除失败", null);
		       }
			  
		   }catch(Exception e){
			   result = new BhResult(400,"删除失败", null);
			   e.printStackTrace();
		   }
		   return result;
	}
	
	
	
	/**
	 * PC端优惠劵分页查询操作
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody Coupon coupon , HttpServletRequest request){
		   BhResult  result = null;
		   try{
			   //获取shopId
			   String token = request.getHeader("token");
			   JedisUtil jedisUtil= JedisUtil.getInstance();  
			   JedisUtil.Strings strings=jedisUtil.new Strings();
			   String userJson = strings.get(token);
			   Map map2 = JSON.parseObject(userJson, Map.class);
		       Object sId = map2.get("shopId");
		       Integer shopId = 1;
			   if(sId!=null){//是商家的操作
			    shopId = (Integer)sId;
			    coupon.setShopId(shopId);
			   }
			   Map<String,Object> map = couponService.listCoupon(coupon);
			   result = new BhResult();
			   result.setStatus(BhResultEnum.SUCCESS.getStatus());
			   result.setMsg(BhResultEnum.SUCCESS.getMsg());
			   result.setData(map);
		   }catch(Exception e){
			   result = new BhResult(400,"操作失败",null);
			   e.printStackTrace();
		   }
		   return result;
	}
	
	/**
	 * 移动端优惠劵分页查询操作，用户可领取
	 */
	@RequestMapping("/moblieListPage")
	@ResponseBody
	public BhResult moblielistPage(@RequestBody Coupon coupon , HttpServletRequest request){
		   BhResult  result = null;
		  
		   try{
			   Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);

			   Map<String,Object> map = couponService.moblieListCoupon(coupon);
			   result = new BhResult();
			   result.setStatus(BhResultEnum.SUCCESS.getStatus());
			   result.setMsg(BhResultEnum.SUCCESS.getMsg());
			   result.setData(map);
		   }catch(Exception e){
			   result = new BhResult(400,"操作失败",null);
			   e.printStackTrace();
		   }
		   return result;
	}
	
	
	/**
	 * <p>Description:添加礼包时获取有效优惠券列表 </p>
	 *  @author scj  
	 *  @date 2018年6月12日
	 */
	@RequestMapping("/fitListPage")
	@ResponseBody
	public BhResult fitListPage(@RequestBody Coupon coupon , HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map map2 = JSON.parseObject(userJson, Map.class);
	        Object sId = map2.get("shopId");
	        Integer shopId = 1;
		    if(sId!=null){//是商家的操作
		    	shopId = (Integer)sId;
			    coupon.setShopId(shopId);
		    }
		    PageBean<Coupon> data = couponService.fitListPage(coupon);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	
}
