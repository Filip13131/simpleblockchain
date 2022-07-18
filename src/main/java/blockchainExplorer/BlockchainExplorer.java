package blockchainExplorer;

import com.google.gson.GsonBuilder;
import datastructures.block.Block;
import datastructures.blockchain.Blockchain;
import util.PublicKeyAdapter;

import java.security.PublicKey;

public class BlockchainExplorer {

    private static String blockDetails(Block block){
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(PublicKey.class, new PublicKeyAdapter()).create().toJson(block);
    }

    public static void printOutBlockchainLength(Blockchain blockchain){
        System.out.println(blockchain.getBlockchain().size());
    }
    public static void printOutTransactionsIncludedInABlock(Block block){

        System.out.println(new GsonBuilder().setPrettyPrinting().registerTypeAdapter(PublicKey.class, new PublicKeyAdapter()).create().toJson(block.transactions));
    }
    public static void printOutBlockDetailsByBlockHash(Blockchain blockchain, String hash){
        Block blockToPrint = null;
        for (Block block : blockchain.getBlockchain()){
            if (!block.hash.equals(hash)){
                continue;
            }
            blockToPrint=block;
            break;
        }
        if (blockToPrint==null){
            System.out.println("Block does not exist!!");
        }
        else {
            System.out.println(blockDetails(blockToPrint));
        }
    }
    public static void printOutBlockDetailsByBlockHeight(Blockchain blockchain, int height){
        if (!(blockchain.getBlockchain().size() >height)){
            System.out.println("Block does not exist!!");
        }
        else {
            System.out.println(blockDetails(blockchain.getBlockchain().get(height)));
        }
    }
    public static void printOutLastBlockDetails(Blockchain blockchain){
        if (blockchain.getBlockchain().isEmpty()){
            System.out.println("Block does not exist!!");
        }
        System.out.println(blockDetails(blockchain.getBlockchain().get(blockchain.getBlockchain().size()-1)));
    }
}
