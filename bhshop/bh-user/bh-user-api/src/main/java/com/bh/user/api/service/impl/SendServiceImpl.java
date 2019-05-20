package com.bh.user.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.order.mapper.OrderSendInfoMapper;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.user.api.service.SendService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberSendMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberSend;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
public class SendServiceImpl implements SendService{
	@Autowired
	private MemberSendMapper memberSendMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private OrderSendInfoMapper orderSendInfoMapper;
	
	
	public int insertSendMsg(MemberSend memberSend) throws Exception{
		Member member = new Member();
		member.setPhone(memberSend.getPhone());
		List<Member> member2 =  memberMapper.selecMemberByPhone(member);
		int row=0;
		if (member2.size() < 1) {
			memberSend.setmId(0);
		}else{
			MemberSend memberSend2 = memberSendMapper.selectByPrimaryKey(member2.get(0).getId());
			if (memberSend2 == null) {//不存在
				memberSend.setmId(member2.get(0).getId());
				memberSend.setType(0);
				row = memberSendMapper.insertSelective(memberSend);
				Member record = new Member();
				record.setId(member2.get(0).getId());
				record.setType(3);
				row = memberMapper.updateByPrimaryKeySelective(record);
			}else{
				memberSend.setmId(member2.get(0).getId());
				row = memberSendMapper.updateByPrimaryKeySelective(memberSend);
			}
		}
		
		return row;
	}
	
	public MemberSend getMsg(String phone) throws Exception{
		MemberSend memberSend = new MemberSend();
		Member member1 = new Member();
		member1.setPhone(phone);
		List<Member> member = memberMapper.selecMemberByPhone(member1);
		if (member == null) {
			memberSend.setFullName("0");
			return memberSend;
		}else{
			memberSend = memberSendMapper.selectByPrimaryKey(member.get(0).getId());
			if (memberSend == null) {
				
				MemberSend s = new MemberSend();
				s.setFullName("1");
				return s;
			}else{
				memberSend.setFullName("2");
				return memberSend;
			}
		}
		
	}
	
	public List<Member> getMsg1(String phone,Integer id) throws Exception{
		Member member1 = new Member();
		member1.setPhone(phone);
		member1.setId(id);
		List<Member> members = memberMapper.selecMemberByPhone(member1);
		return members;
	}
	
	
	/**
	 * CHENG-20171124-01
	 * 速达-个人中心
	 * @return MemberSend对象
	 */
	public MemberSend selectMemberSendCenter(MemberSend memberSend) throws Exception{
		MemberSend memberSend2 = new MemberSend();
		memberSend2 = memberSendMapper.selectByPrimaryKey(memberSend.getmId());
		Member member = memberMapper.selectByPrimaryKey(memberSend.getmId());
		String phone = member.getPhone();
		phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");  
		
		memberSend2.setMember(member);
		memberSend2.setPhone(phone);
		//接单量<20
		OrderSendInfo orderSendInfo =new OrderSendInfo();
		orderSendInfo.setsId(memberSend.getmId());
		List<OrderSendInfo> orderSendInfoList = orderSendInfoMapper.selectOrderShopBymid(orderSendInfo);
		if ( (orderSendInfoList.size()>0) && (orderSendInfoList.size()<= Contants.sendLevel1) ) {
			memberSend2.setLevelName(Contants.sendLevelName1);//0<x<100
		}else if ((orderSendInfoList.size()>Contants.sendLevel1) && (orderSendInfoList.size()<=Contants.sendLevel2)) {
			memberSend2.setLevelName(Contants.sendLevelName2);//100 < x < 400
		}else if ((orderSendInfoList.size()>Contants.sendLevel2) && (orderSendInfoList.size()<=Contants.sendLevel3)) {
			memberSend2.setLevelName(Contants.sendLevelName3);
		}else if ((orderSendInfoList.size()>Contants.sendLevel3) && (orderSendInfoList.size()<=Contants.sendLevel4)) {
			memberSend2.setLevelName(Contants.sendLevelName4);
		}else {
			memberSend2.setLevelName(Contants.sendLevelName5);
		}
		int mark = orderSendInfoMapper.selectSendMarkBymid(orderSendInfo);
		memberSend2.setMark(mark);
		return memberSend2;
	}

	
	/** 
     * 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替 
     *  
     * @param content 
     *            传入的字符串 
     * @param frontNum 
     *            保留前面字符的位数 
     * @param endNum 
     *            保留后面字符的位数 
     * @return 带星号的字符串 
     */  
  
    public String getStarString(String content, int frontNum, int endNum) {  
  
        if (frontNum >= content.length() || frontNum < 0) {  
            return content;  
        }  
        if (endNum >= content.length() || endNum < 0) {  
            return content;  
        }  
        if (frontNum + endNum >= content.length()) {  
            return content;  
        }  
        String starStr = "";  
        for (int i = 0; i < (content.length() - frontNum - endNum); i++) {  
            starStr = starStr + "*";  
        }  
        return content.substring(0, frontNum) + starStr  
                + content.substring(content.length() - endNum, content.length());  
  
    }  

	
	/**
	 * 配送员详情
	 */
	@Override
	public MemberSend sendDetails(String mId) throws Exception {
		MemberSend send = memberSendMapper.selectByPrimaryKey(Integer.parseInt(mId));
		if(send!=null){
			if(send.getTool()==1){ //交通工具
				send.setSendTool(Contants.BIKE);
			}
			if(send.getTool()==2){
				send.setSendTool(Contants.TRICYCLE);
			}
			if(send.getTool()==3){
				send.setSendTool(Contants.ELECTROMBILE);
			}
			if(send.getTool()==4){
				send.setSendTool(Contants.BIG_ELECTROMBILE);
			}
			if(send.getTool()==5){
				send.setSendTool(Contants.MOTORBIKE);
			}
			if(send.getTool()==6){
				send.setSendTool(Contants.SEDAN_CAR);
			}
			if(send.getTool()==7){
				send.setSendTool(Contants.TRUCKS);
			}
			
			if(send.getType()==1){ //接单类型
				send.setSendType(Contants.ALL_TYPE);
			}
			if(send.getType()==2){
				send.setSendType(Contants.SHORT_RANGE);
			}
			if(send.getType()==3){
				send.setSendType(Contants.FAR_RANGE);
			}
			if(send.getType()==4){
				send.setSendType(Contants.HIGH_PRICE);
			}
			if(send.getType()==5){
				send.setSendType(Contants.LOW_PRICE);
			}
			
			if(send.getStatus()==0){//审核状态
				send.setAuditStatus(Contants.AUDIT_WAIT);
			}
			if(send.getStatus()==1){
				send.setAuditStatus(Contants.AUDIT_SUCCESS);
			}
			if(send.getStatus()==2){
				send.setAuditStatus(Contants.AUDIT_FAIL);
			}
			
			double realTotalIncome = (double)send.getTotalIncome()/100;
			send.setRealTotalIncome(realTotalIncome);
			
			double realBalance = (double)send.getBalance()/100;
			send.setRealBalance(realBalance);
			
			double realScope = (double)Integer.parseInt(send.getScope())/1000;
			send.setRealScope(realScope); //配送范围 km
			
			if(send.getIdentity()!=null){
				String identity = getStarString(send.getIdentity(), 4, 4);
				send.setIdentity(identity);
			}
			
			Member member = memberMapper.selectByPrimaryKey(send.getmId());
			if(member!=null){
				send.setHeadImage(member.getHeadimgurl()); //头像
				send.setPhoneNumber(member.getPhone()); //联系方式
			}
		}
		return send;
	}

	
	/**
	 * 获取用户身份证
	 */
	@Override
	public String getIdentity(String mId) throws Exception {
		String str = null;
		MemberSend send = memberSendMapper.selectByPrimaryKey(Integer.parseInt(mId));
		if(send!=null){
			if(send.getIdentity()!=null){
				str = send.getIdentity();
			}
		}
		return str;
	}
}
