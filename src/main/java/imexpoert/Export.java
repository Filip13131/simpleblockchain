package imexpoert;

import com.google.gson.GsonBuilder;
import datastructures.block.Block;
import datastructures.blockchain.Blockchain;
import datastructures.transaction.Transaction;
import datastructures.transaction.TransactionInput;
import util.PublicKeyAdapter;
import util.StringUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;

import static imexpoert.Import.importJasonFromFile;

public class Export {
    public static void main (String[] args) throws IOException {
        KeyPair kp = StringUtil.generateKeyPair();
        PublicKey num = kp.getPublic();
        Blockchain blockchain;
        blockchain = new Blockchain(num, 100);
        Block block2 = new Block(blockchain.getBlockchain().get(0).getHash());
        blockchain.mineAndAddBlock(block2);



        Block block3 = new Block(blockchain.getBlockchain().get(1).getHash());
        PublicKey num2 = StringUtil.generateKeyPair().getPublic();
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        inputs.add(new TransactionInput(blockchain.getBlockchain().get(0).getTransactions().get(0).getOutputs().get(0).getId()));
        Transaction transaction = new Transaction(num ,num2, 50,  inputs);
        transaction.generateSignature(kp.getPrivate());
        block3.addTransaction(transaction, blockchain);
        blockchain.mineAndAddBlock(block3);
        System.out.println(getBlockchainAsJson(blockchain));
        System.out.println(blockchain.getBlockchain().get(0).getTransactions().get(0).getInputs());
        exportBlockchain(blockchain);
        System.out.println("sdfghefjhsgjfaeryfgeargjhrajlhglkjahgjfhahgkahgjkhaklsjhgjrhgfdsajkghajgkhkjahgjkhakjghkjahgkjhdkajhglkjasdhgkljhakjgh" +
                "lakga;gkjha;jg;akjg;lajgkljagljahjgadgjk");
        System.out.println(importJasonFromFile("savedBlockchains/savedBlockchain.json"));
        Blockchain newBlockchain = Import.fromJsonToBlockchain(importJasonFromFile("savedBlockchains/savedBlockchain.json"));
        System.out.println("sfdgftygakfhgajhfgkjahsfkjhakjfhkjashfkjhasfkjhaskjfhaksjhfkjhsafkjfhaksjhfkjashfkjhaskjfhksajhfkjhaskjfhsakjhfkjsahfksajhfksjafhsa");
        System.out.println(new GsonBuilder().setPrettyPrinting().serializeNulls().registerTypeAdapter(PublicKey.class, new PublicKeyAdapter()).create().toJson(newBlockchain));

    }

    public static void exportBlockchain( Blockchain blockchain ) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("savedBlockchains/savedBlockchain.json"));
            writer.write(getBlockchainAsJson(blockchain));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static String getBlockchainAsJson(Blockchain blockchain){
        return new GsonBuilder().setPrettyPrinting().serializeNulls().registerTypeAdapter(PublicKey.class, new PublicKeyAdapter()).create().toJson(blockchain);
    }



}
