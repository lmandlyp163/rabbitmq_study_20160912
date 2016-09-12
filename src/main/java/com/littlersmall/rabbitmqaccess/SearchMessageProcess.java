/**  
 * Project Name:beauty  
 * File Name:SearchMessageProcess.java  
 * Package Name:com.netease.beauty.common.queue.rabbitmq.common  
 * Date:2016年8月27日上午11:33:59  
 * Copyright (c) 2016,  All Rights Reserved.  
 *  
*/  
  
package com.littlersmall.rabbitmqaccess;

import com.littlersmall.rabbitmqaccess.common.DetailRes;

/**  
 * ClassName:SearchMessageProcess <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2016年8月27日 上午11:33:59 <br/>  
 * @author   hzlimao  
 * @version    
 * @since    JDK 1.8 
 * @see        
 */
public class SearchMessageProcess implements MessageProcess<String>{

	@Override
	public DetailRes process(String  message) {
        System.out.println("Word");

        return new DetailRes(true, "");
	}

}
  
