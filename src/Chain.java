package edu.dku.aiia.fab;

public class Chain {


	private long Height;
	private String previousHash;
	private String currentBlockHash;
	private long chainTransactionCount;
	
	//getter
	public String getHeight() {
		return String.valueOf(Height);
	}
	
	public String getPreviousHash() {
		return previousHash;
	}
	
	public String getCurrentBlockHash() {
		return currentBlockHash;
	}
	
	public String getChainTransactionCount() {
		return String.valueOf(chainTransactionCount);
	}
	
	//setter
	public void setHeight(long height) {
		this.Height = height;
	}
	
	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}
	
	public void setCurrentBlockHash(String currentBlockHash) {
		this.currentBlockHash = currentBlockHash;
	}
	
	public void setChainTrasactionCount(long chainTransactionCount) {
		this.chainTransactionCount = chainTransactionCount;
	}
}

