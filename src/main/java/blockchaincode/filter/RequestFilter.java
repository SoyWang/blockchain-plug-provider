/**
 * @projectName:hearken-das-core
 * @date:Nov 16, 2018 11:49:32 AM
 * @author:xiaohui
 * Copyright: 2018 www.sunsheen.cn Inc. All rights reserved.
 * 注意：本内容仅限于成都淞幸科技有限责任公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package blockchaincode.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;

import com.sunsheen.jfids.das.core.annotation.Bean;
import com.sunsheen.jfids.das.core.annotation.Filter;

/**
 * 请求过滤器demo
 * 必须实现接口：javax.ws.rs.container.ContainerRequestFilter
 * 必须添加Bean注解：com.sunsheen.jfids.das.core.annotation.Bean
 * 在未自定义过滤器注解时，必须添加默认注解：com.sunsheen.jfids.das.core.annotation.Filter
 * 
 * 注意：
 *   1、如果要进行全局rest接口拦截（即不需要再resource类上标注过滤器注解），请添加注解：javax.ws.rs.container.PreMatching
 *   2、@Filter注解标注在方法上只拦截该方法，标注在类上，类里面的所有方法将被拦截。
 * @author xiaohui
 *
 */
//@Bean
//@Filter
////全局匹配。即所有的rest接口都会被拦截。
//@PreMatching
public class RequestFilter implements ContainerRequestFilter {
	@Context 
	private HttpServletRequest request;

	@Context 
	private HttpServletResponse response;
	/**
	 *拦截后执行的方法体
	 */
	@Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException {
		System.out.println("这是请求过滤器");
		//使用abortWith方法将中断后续过滤器的执行（如果有多个的情况下）, 并且不会进入被拦截的方法
		//containerRequestContext.abortWith(Response.status(401).entity("To access this method you need token !").build());
	}

}
