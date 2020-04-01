/**
 * @projectName:hearken
 * @date:Mar 5, 2019 2:03:09 PM
 * @author:xiaohui
 * Copyright: 2019 www.sunsheen.cn Inc. All rights reserved.
 * 注意：本内容仅限于成都淞幸科技有限责任公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package blockchaincode.provider;

import blockchaincode.entity.BlockDataEntity;

import com.sunsheen.jfids.das.core.annotation.Bean;
import com.sunsheen.jfids.das.core.annotation.Provider;

/**
 * 该类为一个RPC服务提供者
 * @author WangSong
 *
 */
@Bean("DAS应用-RPC")
@Provider//表示对外发布该类所有接口为相应的接口服务。
public class BlockChaincodeRpc {

	/* 返回字符串的情况
	 * @see com.sunsheen.demo.listener.das.IDasAppProvider#run(java.lang.String)
	 */
	public Object run(String execution) {
		return execution+"-这是来自RPC服务提供者";
	}

	/* 返回实体对象的情况
	 * @see com.sunsheen.das.provider.IDasAppProvider#getCount()
	 */
	public BlockDataEntity get() {
		return new BlockDataEntity();
	}

	
}
