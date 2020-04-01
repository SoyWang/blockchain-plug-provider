package blockchaincode.entity;

import java.util.Date;

/**
 * 通用上链数据实体
 * @author WangSong
 *
 */
public class BlockDataEntity {
	
	private String id;//主键
	private String content;//数据内容
	private String type;//数据类型
	private Date createdDate;//创建时间
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return "BlockDataEntity [id=" + id + ", content=" + content + ", type="
				+ type + ", createdDate=" + createdDate + "]";
	}
	
	
	
}
