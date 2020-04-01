package blockchaincode.common;

import java.io.File;

/**
 * 区块链常量
 * @author WangSong
 *
 */
public class BlockChainConstant {
	/**
	 * 密钥文件位置
	 */
	public static final String keyFolderPath = "crypto-config"+File.separator+"peerOrganizations"+File.separator+"org1.example.com"+File.separator+"users"+File.separator+"Admin@org1.example.com"+File.separator+"msp"+File.separator+"keystore";
	
	/**
	 * 密钥文件名
	 */
	public static final String keyFileName = "9eaf7e41010a893d83b4b35d52efa782bb4e07bbcbf4838c274dbb1edffc20b7_sk";
	
	/**
	 * cert文件位置
	 */
	public static final String certFoldePath = "crypto-config"+File.separator+"peerOrganizations"+File.separator+"org1.example.com"+File.separator+"users"+File.separator+"Admin@org1.example.com"+File.separator+"msp"+File.separator+"admincerts";
	
	/**
	 * cert文件名
	 */
	public static final String certFileName = "Admin@org1.example.com-cert.pem";
	
	/**
	 * tlsOrder文件位置
	 */
	public static final String tlsOrderFilePath = "crypto-config"+File.separator+"ordererOrganizations"+File.separator+"example.com"+File.separator+"tlsca"+File.separator+"tlsca.example.com-cert.pem";
	
	/**
	 * tlsOrder名
	 */
	public static final String txfilePath = "test1.tx";
	
	/**
	 * tlsPeer文件位置
	 */
	public static final String tlsPeerFilePath = "crypto-config"+File.separator+"peerOrganizations"+File.separator+"org1.example.com"+File.separator+"peers"+File.separator+"peer0.org1.example.com"+File.separator+"msp"+File.separator+"tlscacerts"+File.separator+"tlsca.org1.example.com-cert.pem";
	
	/**
	 * tlspeer
	 */
	public static final String tlsPeerFilePathAddtion = "crypto-config"+File.separator+"peerOrganizations"+File.separator+"org1.example.com"+File.separator+"tlsca"+File.separator+"tlsca.org1.example.com-cert.pem";
	
	
}
