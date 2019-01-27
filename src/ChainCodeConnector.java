package edu.dku.aiia.fab;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

//import org.hyperledger.fabric.sdk.EventHub;


public class ChainCodeConnector {
	HFClient client;
	CAConnector cac = new CAConnector();
	Channel channel;
	
	public ChainCodeConnector() {
		try {
			client = getHfClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void loginUser(String userID) {
		try {
			client.setUserContext(cac.tryDeserialize(userID));
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Channel connectToChannel(String peerName, String peerAddress, String eventhubName, String eventhubAddress, String ordererName, String ordererAddress, String channelName) {
		try {
			this.channel = getChannel(client, peerName, peerAddress, eventhubName, eventhubAddress, ordererName, ordererAddress, channelName);
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.channel;
	}
	
	public Channel connectToChannels(String[] peerName, String[] peerAddress, String eventhubName, String eventhubAddress, String ordererName, String ordererAddress, String channelName) {
		try {
			this.channel = getChannels(client, peerName, peerAddress, eventhubName, eventhubAddress, ordererName, ordererAddress, channelName);
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.channel;
	}
	
	public  HFClient getHfClient() throws Exception {
		// initialize default cryptosuite
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		// setup the client
		HFClient client = HFClient.createNewInstance();
		client.setCryptoSuite(cryptoSuite);
		return client;
	}
	
	
	private Channel getChannel(HFClient client, String peerName, String peerAddress, String eventhubName, String eventhubAddress, String ordererName, String ordererAddress, String channelName)
			throws InvalidArgumentException, TransactionException {
		// initialize channel
		// peer name and endpoint in fabcar network
		Peer peer = client.newPeer(peerName, peerAddress);
		// eventhub name and endpoint in fabcar network
		//EventHub eventHub = client.newEventHub(eventhubName, eventhubAddress);
		// orderer name and endpoint in fabcar network
		Orderer orderer = client.newOrderer(ordererName, ordererAddress);
		// channel name in fabcar network
		Channel channel = client.newChannel(channelName);
		channel.addPeer(peer);
		//channel.addEventHub(eventHub);
		channel.addOrderer(orderer);
		channel.initialize();
		return channel;
	}
	
	
	private Channel getChannels(HFClient client, String[] peerName, String[] peerAddress, String eventhubName, String eventhubAddress, String ordererName, String ordererAddress, String channelName)
			throws InvalidArgumentException, TransactionException {
		// initialize channel
		// peer name and endpoint in fabcar network
		Channel channel = client.newChannel(channelName);
		
		int peerNum = peerName.length;
		for(int i = 0; i < peerNum; i++) {
			Peer peer = client.newPeer(peerName[i], peerAddress[i]);
			channel.addPeer(peer);
		}
		//Peer peer = client.newPeer(peerName, peerAddress);
		// eventhub name and endpoint in fabcar network
		//EventHub eventHub = client.newEventHub(eventhubName, eventhubAddress);
		// orderer name and endpoint in fabcar network
		Orderer orderer = client.newOrderer(ordererName, ordererAddress);
		// channel name in fabcar network
		
		//channel.addEventHub(eventHub);
		channel.addOrderer(orderer);
		channel.initialize();
		return channel;
	}
	
    public String queryBlockChain(String channelName, String chaincodeName, String functionName, String[] args) throws ProposalException, InvalidArgumentException {
    	String stringResponse = "";
        // get channel instance from client
        Channel channel = client.getChannel(channelName);
        // create chaincode request
        QueryByChaincodeRequest qpr = client.newQueryProposalRequest();
        // build cc id providing the chaincode name. Version is omitted here.
        ChaincodeID CCId = ChaincodeID.newBuilder().setName(chaincodeName).build();
        qpr.setChaincodeID(CCId);
        // CC function to be called
        qpr.setFcn(functionName);
        qpr.setArgs(args);
        Collection<ProposalResponse> res = channel.queryByChaincode(qpr);
        // display response
        for (ProposalResponse pres : res) {
        	stringResponse = new String(pres.getChaincodeActionResponsePayload());
            //log.info(stringResponse);
        }
        return stringResponse;
    }  
    
    public String invokeBlockChain(String channelName, String chaincodeName, String functionName, String[] args) throws ProposalException, InvalidArgumentException {
        // get channel instance from client
    	
     	String txid = "";
        Channel channel = client.getChannel(channelName);
        
        // create chaincode request
        TransactionProposalRequest Request = client.newTransactionProposalRequest();
        
        // build cc id providing the chaincode name. Version is omitted here.
        ChaincodeID CCId = ChaincodeID.newBuilder().setName(chaincodeName).build();
        Request.setChaincodeID(CCId);
        
        // CC function to be called
        Request.setFcn(functionName);
        Request.setArgs(args);
        
        Collection<ProposalResponse> responses = channel.sendTransactionProposal(Request);
      
        List<ProposalResponse> invalid = responses.stream().filter(r -> r.isInvalid()).collect(Collectors.toList());
        if(!invalid.isEmpty()) {
        	invalid.forEach(response ->{
        		
        	});
        	throw new RuntimeException("invalid response(s) found");
        }
        for(ProposalResponse response : responses) {
        	if(response.getStatus() == Status.SUCCESS) {
        		txid = response.getTransactionID();
        	}
        }
        channel.sendTransaction(responses);
        return txid;
    }
    

}