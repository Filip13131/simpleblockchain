package imexpoert;

import com.google.gson.GsonBuilder;
import datastructures.block.Block;
import datastructures.blockchain.Blockchain;
import util.StringUtil;

import java.security.PublicKey;

public class Export {
    public static void main (String args[]){
        PublicKey num = StringUtil.generateKeyPair().getPublic();
        Blockchain blockchain = new Blockchain(num, 100);
        Block block2 = new Block(blockchain.blockchain.get(0).hash);
        blockchain.mineAndAddBlock(block2);
        System.out.println(getBlockchainAsJson(blockchain));
        System.out.println(getUTXOsAsJson(blockchain));
        System.out.println(blockchain.blockchain.get(0).transactions.get(0).inputs);
    }

    public static void exportToPath(String path){

    }
    private static String getBlockchainAsJson(Blockchain blockchain){
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain.blockchain);
        return blockchainJson;
    }
    private static String getUTXOsAsJson(Blockchain blockchain){
        String UTXOsJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain.UTXOs);
        return UTXOsJson;
    }

    private static String getMetaDataAsJson(Blockchain blockchain){ return ""; }



}
