/**
 * @projectName:hearken
 * @date:Mar 5, 2019 2:03:09 PM
 * @author:WangSong
 * Copyright: 2019 www.sunsheen.cn Inc. All rights reserved.
 * 注意：本内容仅限于成都淞幸科技有限责任公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package blockchaincode.provider;

import java.io.File;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.utils.json.Json;

import blockchaincode.entity.BlockDataEntity;
import blockchaincode.service.IBlockChaincodeService;
import blockchaincode.service.impl.BlockChaincodeServiceImpl;

import com.sunsheen.jfids.das.common.helper.StringHelper;
import com.sunsheen.jfids.das.common.web.BaseResource;
import com.sunsheen.jfids.das.core.annotation.Bean;
import com.sunsheen.jfids.das.core.annotation.Describe;

/**
 * 该类为一个REST接口服务
 * @author WangSong
 *
 */
@Bean("DAS应用-REST")
//访问路径，表示一个业务类别。系统会默认加上前缀：api/rest/das。即真实的访问地址为：http://IP:PORT/api/rest/das/BlockChaincodeRest
@Path("/block-chaincode")
public class BlockChaincodeRest extends BaseResource{

	
	private static  Logger logger = LoggerFactory.getLogger(BlockChaincodeRest.class);
	
	//真实的访问地址为：http://IP:PORT/api/rest/das/hearken/getOne
	/**
	 * 查詢指定區塊信息
	 * @param servletRequest
	 * @return
	 */
	
	@Describe("块信息查询")
	@Path("/block-info")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOne() {
		BlockDataEntity entity = new BlockDataEntity();
		IBlockChaincodeService serviceProvider = new BlockChaincodeServiceImpl();
		Map result = null;
		try {
			String json = StringHelper.inputStreamToString(servletRequest.getInputStream());
			entity = Json.toBean(json, entity.getClass());
			String proPath = System.getProperty("user.dir");//当前项目路径地址
			/** windows上目录 **/
			String realPath = proPath.concat(File.separator +"src"+ 
					File.separator +"main"+ File.separator +"resources");
			/** Linux上目录 **/
//			String realPath = (proPath.substring(0,proPath.indexOf("bin"))).concat("app");
			result = serviceProvider.getBlockInfo(entity,realPath);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.EXPECTATION_FAILED).entity(e.toString()).build();
		}
		return Response.status(Status.OK).entity(result).build();
	}
	
	@Describe("链上信息溯源")
	@Path("/history")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHistory() {
		BlockDataEntity entity = new BlockDataEntity();
		IBlockChaincodeService serviceProvider = new BlockChaincodeServiceImpl();
		Map result = null;
		try {
			
			String json = StringHelper.inputStreamToString(servletRequest.getInputStream());
			entity = Json.toBean(json, entity.getClass());
			String proPath = System.getProperty("user.dir");//当前项目路径地址
			/** windows上目录 **/
			String realPath = proPath.concat(File.separator +"src"+ 
					File.separator +"main"+ File.separator +"resources");
			/** Linux上目录 :/home/hkdas-blockchain/HKDAS/app **/
//			String realPath = (proPath.substring(0,proPath.indexOf("bin"))).concat("app");
			result = serviceProvider.getBlockHistoryInfo(entity, realPath);
		} catch (Exception e) {
			return Response.status(Status.EXPECTATION_FAILED).entity(e.toString()).build();
		}
		return Response.status(Status.OK).entity(result.get(200)).build(); 
	}
	
	/**
	 * 存入信息
	 * @return
	 */
	@Describe("信息上链")
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(){
		BlockDataEntity entity = new BlockDataEntity();
		IBlockChaincodeService serviceProvider = new BlockChaincodeServiceImpl();
		//調用方法
		try {
			String json = StringHelper.inputStreamToString(servletRequest.getInputStream());
			entity = Json.toBean(json, entity.getClass());
			String proPath = System.getProperty("user.dir");//当前项目路径地址
			/** windows上目录 **/
			String realPath = proPath.concat(File.separator +"src"+ 
					File.separator +"main"+ File.separator +"resources");
			/** Linux上目录 **/
//			String realPath = (proPath.substring(0,proPath.indexOf("bin"))).concat("app");
			serviceProvider.saveToBlock(entity, realPath, "insert");
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.EXPECTATION_FAILED).entity(e.toString()).build();
		}
		
		return Response.status(Status.OK).entity(null).build();
	}

	/**
	 * 修改信息
	 * @return
	 */
	@Describe("上链数据修改")
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(){
		//接收页面数据
		BlockDataEntity entity = new BlockDataEntity();
		IBlockChaincodeService serviceProvider =  new BlockChaincodeServiceImpl();
		//調用方法
		try {
			String json = StringHelper.inputStreamToString(servletRequest.getInputStream());
			entity = Json.toBean(json, entity.getClass());
			String proPath = System.getProperty("user.dir");//当前项目路径地址
			/** windows上目录 **/
			String realPath = proPath.concat(File.separator +"src"+ 
					File.separator +"main"+ File.separator +"resources");
			/** Linux上目录 **/
//			String realPath = (proPath.substring(0,proPath.indexOf("bin"))).concat("app");
			serviceProvider.saveToBlock(entity,realPath, "update");
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.EXPECTATION_FAILED).entity(e.toString()).build();
		}
		
		return Response.status(Status.OK).entity("ok").build();
	}
	
	
	/**
	 * 解决跨域问题
	 */
	private void access(){
		servletResponse.setHeader("Access-Control-Allow-Origin", "*");//所有请求都能跨域
		servletResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,HEAD,PUT,PATCH");
		servletResponse.setHeader("Access-Control-Max-Age", "36000");
		servletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization,authorization");
		servletResponse.setHeader("Access-Control-Allow-Credentials","true");//保持跨域 Ajax 时的 Cookie

	}
	
}
