package edu.dku.aiia.fab;

import org.hyperledger.fabric.protos.common.Common.Block;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeType;
import org.hyperledger.fabric.sdk.BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo;
import org.hyperledger.fabric.sdk.BlockListener;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.TransactionInfo;
import org.hyperledger.fabric.sdk.TxReadWriteSetInfo;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class App {
    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello World!" ); 
    	
        // create fabric-ca, chaincode, array, eventStr
        CAConnector caConnector = new CAConnector("http://127.0.0.1:7054");
        ChainCodeConnector chaincodeConnector = new ChainCodeConnector();
        String output;
        
        // enroll or load admin
	caConnector.issuAdmin("admin", "adminpw", "org1", "Org1MSP");

	caConnector.registerUser("user1", "org1", "Org1MSP");
        System.out.println("user1");
        //login or enroll user and getchanel
        chaincodeConnector.loginUser("user1");
        Channel ch1 = chaincodeConnector.connectToChannel("peer0.org1.example.com", "grpc://127.0.0.1:7051", "eventhub01", "grpc://127.0.0.1:7053", "orderer.example.com", "grpc://127.0.0.1:7050", "mychannel");
        
        
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
