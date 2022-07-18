package blockchainExplorer;

import com.google.gson.GsonBuilder;
import datastructures.block.Block;
import datastructures.blockchain.Blockchain;
import util.StringUtil;

import java.security.PublicKey;

public class BlockchainExplorer {
    public static void main(String[] args) {
        PublicKey num = StringUtil.generateKeyPair().getPublic();
        Blockchain blockchain = new Blockchain(num, 100);
        Block block2 = new Block(blockchain.blockchain.get(0).hash);
        blockchain.mineAndAddBlock(block2);

        System.out.println(blockchain.blockchain.get(0).transactions.get(0).inputs);

        printOutLastBlockDetails(blockchain);
        printOutBlockchainLength(blockchain);
        printOutBlockDetailsByBlockHash(blockchain,"333");
        printOutBlockDetailsByBlockHeight(blockchain, 0);
        printOutTransactionsIncludedInABlock(blockchain.blockchain.get(0));


    }
    private static String blockDetails(Block block){
        return new GsonBuilder().setPrettyPrinting().create().toJson(block);
    }

    public static void printOutBlockchainLength(Blockchain blockchain){
        System.out.println(blockchain.blockchain.size());
    }
    public static void printOutTransactionsIncludedInABlock(Block block){

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(block.transactions));
    }
    public static void printOutBlockDetailsByBlockHash(Blockchain blockchain, String hash){
        Block blockToPrint = null;
        for (Block block : blockchain.blockchain ){
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
        if (!(blockchain.blockchain.size() >height)){
            System.out.println("Block does not exist!!");
        }
        else {
            System.out.println(blockDetails(blockchain.blockchain.get(height)));
        }
    }
    public static void printOutLastBlockDetails(Blockchain blockchain){
        if (blockchain.blockchain.isEmpty()){
            System.out.println("Block does not exist!!");
        }
        System.out.println(blockDetails(blockchain.blockchain.get(blockchain.blockchain.size()-1)));
    }
}
