package blockchaincode.service;

import java.util.Map;

import blockchaincode.entity.BlockDataEntity;

/**
 * 通用业务层接口
 * @author WangSong
 *
 */
public interface IBlockChaincodeService {

	/**
	 * 保存信息
	 * @param eduInfo
	 * @return
	 */
	void saveToBlock(BlockDataEntity entity,String path,String flag) throws Exception;
	
	
	/**
	 * 查询指定区块信息(cardno + createddate)
	 * @param eduInfo
	 * @return
	 */
	Map getBlockInfo(BlockDataEntity entity,String path) throws Exception;
	
	/**
	 * 信息溯源
	 * @param eduInfo
	 * @return
	 */
	Map getBlockHistoryInfo(BlockDataEntity entity,String path) throws Exception;
	
	/**
	 * 狀態
	 * @return
	 */
	String status();
	
}
