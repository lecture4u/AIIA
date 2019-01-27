package edu.dku.aiia.fab;

import org.hyperledger.fabric.sdk.Channel;



import static java.lang.String.format;



public class Main {
    public static void main(String[] args) throws Exception {
    	
        // create fabric-ca, chaincode, array, eventStr
        CAConnector caConnector = new CAConnector("http://192.168.0.49:7054");
        ChainCodeConnector chaincodeConnector = new ChainCodeConnector();
        String output;
        
        // enroll or load admin
		caConnector.issuAdmin("admin", "adminpw", "org1", "Org1MSP");

		caConnector.registerUser("user1", "org1", "Org1MSP");
        System.out.println("user1");
        //login or enroll user and getchanel
        chaincodeConnector.loginUser("user1");
        Channel ch1 = chaincodeConnector.connectToChannel("peer0.org1.example.com", "grpc://192.168.0.49:7051", "eventhub01", "grpc://192.168.0.49:7053", "orderer.example.com", "grpc://192.168.0.49:7050", "mychannel");
        
        
        //invoke request
		for (int i = 0; i < 10; i++) {
			
			//chain Info
			org.hyperledger.fabric.sdk.BlockchainInfo channelInfo2 = ch1.queryBlockchainInfo();
			System.out.println(">>>>>>BlockNum: "+channelInfo2.getHeight());

			String invokeReseult = chaincodeConnector.invokeBlockChain("mychannel", "mycc", "put", new String[] { "a", "Data [" + (i + 2) + "]" });
			System.out.println(">>>>>>TxID: "+invokeReseult);

		}
    	
		Thread.sleep(2000);
		
        output = chaincodeConnector.queryBlockChain("mychannel", "mycc", "history", new String[] {"a"});
        System.out.println(output);
        
        output = chaincodeConnector.queryBlockChain("mychannel", "mycc", "query", new String[] {"a"});
        System.out.println(output);
       
    }
}