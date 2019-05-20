package io.zy.common.aliyun.oss.util;
import io.zy.common.aliyun.oss.PostObjectSample;

public class Upload {
	
    //aliyun
	 
	
//单文件上传
public boolean singleupload(String yunType,String localFilePath,String key)
{
	  boolean retBl=false;
		try
		{
			String ret="0";
			//String Type=yunType;
			//System.out.println("Type :" + Type); 
			
			 if(yunType.equals("aliyun"))
			  {
				PostObjectSample ossPostObject=new PostObjectSample();
				ret=ossPostObject.PostObject(localFilePath,key);
			}
			if(ret.equals(""))
			{
				 retBl=true;
			}	
			
		}catch (Exception e)
		{
			//res =  BhResult.build(400, "信息提示:网络繁忙,请稍后再试");
			retBl=false;
		 } 
        return retBl;
	    
	}
    public static void main(String args[]){
    	Upload u = new Upload();
    	u.singleupload("aliyun", "D:/test.jpg", "test.jpg");
    }

}
