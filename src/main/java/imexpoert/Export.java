package imexpoert;

import com.google.gson.GsonBuilder;
import datastructures.blockchain.Blockchain;
import util.PublicKeyAdapter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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


}
