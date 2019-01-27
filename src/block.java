package hyperledger.fabric.model;

import java.util.List;

public class block {
	private long blockNum;
	private String previousHash;
	private String dataHash;
	private String blockHash;
	private String chanelName;
	private int transactionCount;
	private List<String> transaction;
	
	//getter
	public String getBlockNum() {
		return String.valueOf(blockNum);
	}
	
	public String getPreviousHash() {
		return previousHash;
	}
	
	public String getDataHash() {
		return dataHash;
	}
	
	public String getBlockHash() {
		return blockHash;
	}
	
	public String getChannelName() {
		return chanelName;
	}
	
	public String getTransactionCount() {
		return String.valueOf(transactionCount);
	}
	
	public List<String> getTransaction(){
		return transaction;
	}
	
	
	//setter
	public void setBlockNum(long blockNum) {
		this.blockNum = blockNum;
	}
	
	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}
	
	public void setDataHash(String dataHash) {
		this.dataHash = dataHash;
	}
	
	public void setBlockhash(String blockHash) {
		this.blockHash = blockHash;
	}
	
	public void setChannelName(String channelName) {
		this.chanelName = channelName;
	}
	
	public void setTransactionCount(int transactionCount) {
		this.transactionCount = transactionCount;
	}
	
	public void setTransaction(List<String> transactionJson) {
		this.transaction = transactionJson;
	}
}
