package chaincode.example;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import io.netty.handler.ssl.OpenSsl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SimpleChaincode extends ChaincodeBase {

    private static Log _logger = LogFactory.getLog(SimpleChaincode.class);
    private BufferedReader bufferedReader;

    @Override
    public Response init(ChaincodeStub stub) {
        _logger.info("init java simple chaincode");
        return newSuccessResponse();
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        try {
            _logger.info("Invoke java simple chaincode");
            String func = stub.getFunction();
            List<String> params = stub.getParameters();
            if (func.equals("invoke")) {
                return invoke(stub, params);
            }
            if (func.equals("delete")) {
                return delete(stub, params);
            }
            if (func.equals("query")) {
                return query(stub, params);
            }
            if (func.equals("put")) {
                return put(stub, params);
            }
            if (func.equals("keys")) {
                return keys(stub, params);
            }
            if (func.equals("history")) {
                return history(stub, params);
            }
            if (func.equals("text")) {
                return text(stub, params);
            }
            if (func.equals("querytext")) {
                return querytext(stub, params);
            }
            return newErrorResponse(
                    "Invalid invoke function name. Expecting one of: [\"invoke\", \"delete\", \"query\"]");
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }

    private Response invoke(ChaincodeStub stub, List<String> args) {
        if (args.size() != 3) {
            return newErrorResponse("Incorrect number of arguments. Expecting 3");
        }
        String accountFromKey = args.get(0);
        String accountToKey = args.get(1);

        String accountFromValueStr = stub.getStringState(accountFromKey);
        if (accountFromValueStr == null) {
            return newErrorResponse(String.format("Entity %s not found", accountFromKey));
        }
        int accountFromValue = Integer.parseInt(accountFromValueStr);

        String accountToValueStr = stub.getStringState(accountToKey);
        if (accountToValueStr == null) {
            return newErrorResponse(String.format("Entity %s not found", accountToKey));
        }
        int accountToValue = Integer.parseInt(accountToValueStr);

        int amount = Integer.parseInt(args.get(2));

        if (amount > accountFromValue) {
            return newErrorResponse(String.format("not enough money in account %s", accountFromKey));
        }

        accountFromValue -= amount;
        accountToValue += amount;

        _logger.info(String.format("new value of A: %s", accountFromValue));
        _logger.info(String.format("new value of B: %s", accountToValue));

        stub.putStringState(accountFromKey, Integer.toString(accountFromValue));
        stub.putStringState(accountToKey, Integer.toString(accountToValue));

        _logger.info("Transfer complete");

        return newSuccessResponse("invoke finished successfully", ByteString
                .copyFrom(accountFromKey + ": " + accountFromValue + " " + accountToKey + ": " + accountToValue, UTF_8)
                .toByteArray());
    }

    // Deletes an entity from state
    private Response delete(ChaincodeStub stub, List<String> args) {
        if (args.size() != 1) {
            return newErrorResponse("Incorrect number of arguments. Expecting 1");
        }
        String key = args.get(0);
        // Delete the key from the state in ledger
        stub.delState(key);
        return newSuccessResponse();
    }

    // query callback representing the query of a chaincode
    private Response query(ChaincodeStub stub, List<String> args) {
        if (args.size() != 1) {
            return newErrorResponse("Incorrect number of arguments. Expecting name of the person to query");
        }
        String key = args.get(0);
        // byte[] stateBytes
        String val = stub.getStringState(key);
        if (val == null) {
            return newErrorResponse(String.format("Error: state for %s is null", key));
        }
        _logger.info(String.format("Query Response:\nName: %s, Amount: %s\n", key, val));
        return newSuccessResponse(val, ByteString.copyFrom(val, UTF_8).toByteArray());
    }

    private Response put(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 2) {
                newErrorResponse("Incorrect number of arguments. Expecting 2");
            }
            // Initialize the chaincode
            String accountKey = args.get(0);
            String accountValue = args.get(1);

            stub.putStringState(accountKey, accountValue);

            return newSuccessResponse("put finished successfully",
                    ByteString.copyFrom(accountKey + ": " + accountValue, UTF_8).toByteArray());
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }

    private Response keys(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 2) {
                newErrorResponse("Incorrect number of arguments. Expecting 2");
            }
            // Initialize the chaincode
            String startKey = args.get(0);
            String endKey = args.get(1);

            QueryResultsIterator<KeyValue> keysIter = stub.getStateByRange(startKey, endKey);

            Iterator<KeyValue> itr = keysIter.iterator();

            Gson gson = new Gson();
            JsonObject object = new JsonObject();
            int num = 0;
            while (itr.hasNext()) {

                KeyValue str = itr.next();
                object.addProperty("key[" + num + "]", str.getKey());

                System.out.println(str.getKey() + ": " + str.getStringValue());
                num++;
            }
            String json = gson.toJson(object);
            return newSuccessResponse(json, ByteString.copyFrom(json, UTF_8).toByteArray());
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }

    private Response history(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                newErrorResponse("Incorrect number of arguments. Expecting 1");
            }
            // Initialize the chaincode
            String key = args.get(0);
            QueryResultsIterator<KeyModification> keysIter = stub.getHistoryForKey(key);

            Iterator<KeyModification> itr = keysIter.iterator();

            Gson gson = new Gson();
            JsonObject object = new JsonObject();
            int num = 0;
            while (itr.hasNext()) {
                KeyModification str = itr.next();
                object.addProperty("value[" + num + "]", str.getStringValue() + ", " + str.getTxId());
                System.out.println(str.getStringValue() + " / " + str.getTxId());
                num++;
            }
            String json = gson.toJson(object);
            return newSuccessResponse(json, ByteString.copyFrom(json, UTF_8).toByteArray());
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }

    private Response text(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                newErrorResponse("Incorrect number of arguments. Expecting 2");
            }
            // Initialize the chaincode
            String text = args.get(0);

            File file = new File("a.txt");

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

            if (file.isFile() && file.canWrite()) {
                bufferedWriter.write(text);
                bufferedWriter.close();
            }

            return newSuccessResponse("text finished successfully");
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }

    private Response querytext(ChaincodeStub stub, List<String> args) {
        try {
            if (args.size() != 1) {
                newErrorResponse("Incorrect number of arguments. Expecting 2");
            }
            // Initialize the chaincode
            String text = args.get(0);
            String line = "";
            String output = "";

            FileReader filereader = new FileReader(text);

            bufferedReader = new BufferedReader(filereader);
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                output += line + "\n";
            }

            return newSuccessResponse(output);
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("OpenSSL avaliable: " + OpenSsl.isAvailable());
        new SimpleChaincode().start(args);
    }

}
