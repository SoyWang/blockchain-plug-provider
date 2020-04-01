package blockchaincode.test;

import java.util.Date;
import java.util.Map;

import blockchaincode.entity.BlockDataEntity;
import blockchaincode.service.IBlockChaincodeService;
import blockchaincode.service.impl.BlockChaincodeServiceImpl;

/**
 * 测试rest接
 * @author WangSong
 *
 */
public class Test {
	
	
	public static void main(String[] args ) throws Exception{
		String path = "E:\\WangSong\\work_space\\blockchaincode-provider\\src\\main\\webapp\\";

		IBlockChaincodeService service = new BlockChaincodeServiceImpl();
		BlockDataEntity entity = new BlockDataEntity();
		entity.setId("1");
		entity.setContent("entity222");
		entity.setCreatedDate(new Date());
		entity.setType("test");
		
//		service.saveToBlock(entity, path, "insert"); 
//		service.saveToBlock(entity, path, "update");
		
		System.out.println(service.getBlockInfo(entity, path));
		service.getBlockHistoryInfo(entity, path);
	}

}
