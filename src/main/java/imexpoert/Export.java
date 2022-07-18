package imexpoert;

import com.google.gson.GsonBuilder;
import datastructures.block.Block;
import datastructures.blockchain.Blockchain;
import util.PublicKeyAdapter;
import util.StringUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PublicKey;

public class Export {
    public static void main (String[] args) throws IOException {
        PublicKey num = StringUtil.generateKeyPair().getPublic();
        Blockchain blockchain = new Blockchain(num, 100);
        Block block2 = new Block(blockchain.blockchain.get(0).hash);
        blockchain.mineAndAddBlock(block2);
        System.out.println(getBlockchainAsJson(blockchain));
        System.out.println(getUTXOsAsJson(blockchain));
        System.out.println(blockchain.blockchain.get(0).transactions.get(0).inputs);

        exportBlockchain(blockchain);

    }

    public static void exportBlockchain( Blockchain blockchain) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("savedBlockchains/savedBlockchain.json"));
            writer.write(getBlockchainAsJson(blockchain));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("savedBlockchains/savedUTXOs.json"));
            writer.write(getUTXOsAsJson(blockchain));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static String getBlockchainAsJson(Blockchain blockchain){
        return new GsonBuilder().setPrettyPrinting().serializeNulls().registerTypeAdapter(PublicKey.class, new PublicKeyAdapter()).create().toJson(blockchain.blockchain);
    }
    private static String getUTXOsAsJson(Blockchain blockchain){
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(PublicKey.class, new PublicKeyAdapter()).create().toJson(blockchain.UTXOs);
    }


}
