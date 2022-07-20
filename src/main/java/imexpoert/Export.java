package imexpoert;

import com.google.gson.GsonBuilder;
import datastructures.blockchain.Blockchain;
import util.typeAdapters.PrivateKeyAdapter;
import util.typeAdapters.PublicKeyAdapter;
import wallet.Wallet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Export {

    public static void exportBlockchain( Blockchain blockchain ) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("savedBlockchains/savedBlockchain.json"));
            writer.write(new GsonBuilder().setPrettyPrinting().serializeNulls().registerTypeAdapter(PublicKey.class, new PublicKeyAdapter()).create().toJson(blockchain));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportWallet (Wallet wallet, String name){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("savedWallets/"+name+".json"));
            writer.write(new GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .registerTypeAdapter(PrivateKey.class, new PrivateKeyAdapter())
                    .registerTypeAdapter(PublicKey.class, new PublicKeyAdapter())
                    .create()
                    .toJson(wallet));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
