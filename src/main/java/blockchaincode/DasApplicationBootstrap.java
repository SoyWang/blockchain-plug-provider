/**
 * @projectName:hearken-das-sdk-demo
 * @date:Mar 12, 2019 10:49:03 AM
 * @author:xiaohui
 * Copyright: 2019 www.sunsheen.cn Inc. All rights reserved.
 * 注意：本内容仅限于成都淞幸科技有限责任公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package blockchaincode;

import blockchaincode.service.IBlockChaincodeService;

import com.sunsheen.jfids.das.core.DasApplication;
import com.sunsheen.jfids.das.core.annotation.DasBootApplication;
import com.sunsheen.jfids.das.core.annotation.Subscribe;

/**
 * 
 * 当独立开发HKDAS应用时（Java工程、Maven工程），使用这种方式启动应用。
 * @author WangSong
 *
 */

@DasBootApplication() //HKDAS应用标识，必须。
@Subscribe(IBlockChaincodeService.class) //订阅接口服务，可多个。
//其他接口服务订阅
//@Subscribe(XXXX.class)
public class DasApplicationBootstrap {

	/**
	 * 项目启动入口
	 * @param args
	 */
	public static void main(String[] args) {
		//启动HKDAS应用，必须。
		DasApplication.run(DasApplicationBootstrap.class, args);
		//查找远程服务方法。本地服务有同一个接口的实现对象，则优先返回本地对象。
//		IBlockChaincodeService serviceProvider = ServiceContexts.find(IBlockChaincodeService.class);
//		if(null == serviceProvider){
//			System.out.println("服务为空！");
//		}else{
//			serviceProvider.status();
//		}
	}
	

}
