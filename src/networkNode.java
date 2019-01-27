package edu.dku.aiia.fab;


public class networkNode {
	
	private String Orderer;
	
	private String Peer;
	
	private int OrdererNum;
	
	private int PeerNum;
	
	//getter
	
	public String getOrderer() {
		return Orderer;
	}
	
	public String getPeer() {
		return Peer;
	}
	
	public String getOrdererNum() {
		return String.valueOf(OrdererNum);
	}
	
	public String getPeerNum() {
		return String.valueOf(PeerNum);
	}
	
	//setter
	public void setOrderer(String orderer) {
		this.Orderer = orderer;
	}
	
	public void setPeer(String peer) {
		this.Peer = peer;
	}
	
	public void setOrdererNum(int onum) {
		this.OrdererNum = onum;
	}
	
	public void setPeerNum(int pnum) {
		this.PeerNum = pnum;
	}

}
