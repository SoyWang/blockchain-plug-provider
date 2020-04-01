package blockchaincode.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.TransactionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blockchaincode.common.BlockChainConstant;
import blockchaincode.entity.BlockDataEntity;
import blockchaincode.fabric.FabricClient;
import blockchaincode.fabric.UserContext;
import blockchaincode.fabric.UserUtils;
import blockchaincode.service.IBlockChaincodeService;
import blockchaincode.utils.JsonParseUtil;

import com.google.gson.Gson;
import com.sunsheen.jfids.das.core.annotation.Bean;

/**
 * 上链信息处理
 * @author WangSong
 *
 */
@Bean("blockChaincodeService")
public class BlockChaincodeServiceImpl implements IBlockChaincodeService{

	private static Logger log = LoggerFactory.getLogger(BlockChaincodeServiceImpl.class);
	
	/**
	 * 信息上链
	 */
	public void saveToBlock(BlockDataEntity entity, String path,String flag) throws Exception{
		UserContext userContext = new UserContext();
		userContext.setAffiliation("Org1");
		userContext.setMspId("Org1MSP");
		userContext.setAccount("admin");
		userContext.setName("admin");
		Enrollment enrollment = UserUtils.getEnrollment(path+ File.separator +BlockChainConstant.keyFolderPath,
				BlockChainConstant.keyFileName, path+ File.separator +BlockChainConstant.certFoldePath, BlockChainConstant.certFileName);
		userContext.setEnrollment(enrollment);
		FabricClient fabricClient = new FabricClient(userContext);
		Peer peer0 = fabricClient.getPeer("peer0.org1.example.com",
				"grpcs://peer0.org1.example.com:7051", path+ File.separator +BlockChainConstant.tlsPeerFilePath);
		List<Peer> peers = new ArrayList<>();
		peers.add(peer0);
		Orderer order = fabricClient.getOrderer("orderer.example.com",
				"grpcs://orderer.example.com:7050", path+ File.separator +BlockChainConstant.tlsOrderFilePath);
		Gson gson =new Gson();
		if(null == entity.getCreatedDate())
			entity.setCreatedDate(new Date());
		String arg = gson.toJson(entity).toString();
		String initArgs[] = {};
		List<String> params = new ArrayList<String>();
		//根据标识符，处理数据
		if(flag.equals("insert")){
			params.add(arg);
			params.add("eventAdd");
			initArgs = params.toArray(new String[params.size()]);
			fabricClient.invoke("mychannel", TransactionRequest.Type.GO_LANG,
					"CommonBlockChaincode", order, peers, "add", initArgs);
		}else if(flag.equals("update")){
			params.add(arg);
			params.add("eventUpdate");
			initArgs = params.toArray(new String[params.size()]);
			fabricClient.invoke("mychannel", TransactionRequest.Type.GO_LANG,
					"CommonBlockChaincode", order, peers, "update", initArgs);
		}
	}


	@Override
	public Map getBlockInfo(BlockDataEntity entity, String path)
			throws Exception {
		UserContext userContext = new UserContext();
		userContext.setAffiliation("Org1");
		userContext.setMspId("Org1MSP");
		userContext.setAccount("admin");
		userContext.setName("admin");
		Enrollment enrollment = UserUtils.getEnrollment(path+ File.separator +BlockChainConstant.keyFolderPath,
				BlockChainConstant.keyFileName, path+ File.separator +BlockChainConstant.certFoldePath, BlockChainConstant.certFileName);
		userContext.setEnrollment(enrollment);
		FabricClient fabricClient = new FabricClient(userContext);
		Peer peer0 = fabricClient.getPeer("peer0.org1.example.com",
				"grpcs://peer0.org1.example.com:7051", path+ File.separator +BlockChainConstant.tlsPeerFilePath);
		List<Peer> peers = new ArrayList<>();
		peers.add(peer0);
		Gson gson =new Gson();
		String arg = gson.toJson(entity).toString();
		String initArgs[] = {entity.getId(),entity.getType()};
		//这里只能有一个区块info，链码方法中控制
		Map<Integer,String> map = fabricClient.queryBlockInfo(peers, "mychannel",
				TransactionRequest.Type.GO_LANG, "CommonBlockChaincode", "queryInfoByIdAndType", initArgs);
		System.out.println(map);
		//取出页面需要的唯一区块值
		Map<String, Object> uniqueMap = new HashMap<String,Object>();
		loop:for(Integer code : map.keySet()){
			if(200 != code){
				uniqueMap.put(String.valueOf(code), map.get(code));
				break;
			}
			String value = map.get(200);
			List<Map<String, Object>> lists = JsonParseUtil.parseJSON2List(value);//转换成list
			for(Map<String, Object> resultMap : lists){
				//只需要当前区块信息
				Map<String, Object> paramMap = JsonParseUtil.json2Map(arg);
				if(resultMap.get("createdDate").equals(paramMap.get("createdDate"))){
					uniqueMap = resultMap;
					break loop;
				}
			}
		}
		return uniqueMap;
	}

	@Override
	public Map getBlockHistoryInfo(BlockDataEntity entity, String path)
			throws Exception {
		log.debug("进来了.............................");
		UserContext userContext = new UserContext();
		userContext.setAffiliation("Org1");
		userContext.setMspId("Org1MSP");
		userContext.setAccount("admin");
		userContext.setName("admin");
		Enrollment enrollment = UserUtils.getEnrollment(path+ File.separator +BlockChainConstant.keyFolderPath,
				BlockChainConstant.keyFileName, path+ File.separator +BlockChainConstant.certFoldePath, BlockChainConstant.certFileName);
		userContext.setEnrollment(enrollment);
		log.debug("用户权限认证通过。。。。。。。。。。。");
		FabricClient fabricClient = new FabricClient(userContext);
		Peer peer0 = fabricClient.getPeer("peer0.org1.example.com",
				"grpcs://peer0.org1.example.com:7051", path+ File.separator +BlockChainConstant.tlsPeerFilePath);
		List<Peer> peers = new ArrayList<>();
		peers.add(peer0);
		String initArgs[] = {entity.getId(), entity.getType()};
		Map<Integer,Object> map = fabricClient.queryChaincode(peers, "mychannel",
				TransactionRequest.Type.GO_LANG, "CommonBlockChaincode", "queryInfoByIdAndType", initArgs);
		System.out.println(map);
		return map;
	}


	@Override
	public String status() {
		// TODO 自动生成的方法存根
		return "正常運行...";
	}


}
