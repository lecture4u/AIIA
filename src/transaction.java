package hyperledger.fabric.model;

import java.text.SimpleDateFormat;
import java.util.Date;



public class transaction {

	private String channelName;
	private String txId;
	private Date timeStamp;
	private boolean validCode;
	private String type;
	private String creator;
	private String endorsement;
	private String chaincodeName;
	private String chaincodeVersion;
	private String key;
	private String value;
	
	//getter
	public String getChannelName() {
		return channelName;
	}
	
	public String getValidcode() {
		return String.valueOf(validCode);
	}
	
	public String getTxId() {
		return txId;
	}
	
	public String getTimeStamp() {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = transFormat.format(timeStamp);
		return result;
	}
	
	public String getCreator() {
		return creator;
	}
	
	public String getType() {
		return type;
	}
	
	public String getChaincodeName() {
		return chaincodeName;
	}
	
	public String getChaincodeVersion() {
		return chaincodeVersion;
	}
	
	public String getEndorsement() {
		return endorsement;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	//settter
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	public void setValidcode(boolean validCode) {
		this.validCode = validCode;
	}
	
	public void setTxId(String txId) {
		this.txId = txId;
	}
	
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setChaincodeName(String chaincodeName) {
		this.chaincodeName = chaincodeName;
	}
	
	public void setChaincodeVersion(String chaincodeVersion) {
		this.chaincodeVersion = chaincodeVersion;
	}
	
	public void setEndorsement(String endorsement) {
		this.endorsement = endorsement;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
