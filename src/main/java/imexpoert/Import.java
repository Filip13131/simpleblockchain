package imexpoert;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import datastructures.block.Block;
import datastructures.transaction.TransactionOutput;
import util.PublicKeyAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class Import {


    public static HashMap<String, TransactionOutput> fromJsonToUTXOs(String json){
        return null;
    }
    public static ArrayList<Block> fromJsonToBlockchain(String json){
        ArrayList<Block> blockchain;
        blockchain = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(PublicKey.class, new PublicKeyAdapter())
                .create()
                .fromJson( json , new TypeToken<ArrayList<Block>>(){}.getType() );
        return blockchain;
    }

    public static String importJasonFromFile (String path){
        String output = null;
        try {
            output = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }




}
