package imexpoert;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import datastructures.block.Block;
import util.PublicKeyAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.ArrayList;

public class Import {



    public static ArrayList<Block> fromJasonToBlockchain(){


        ArrayList<Block> blockchain;

        blockchain = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(PublicKey.class, new PublicKeyAdapter())
                .create()
                .fromJson(importJasonFromFile("savedBlockchains/savedBlockchain.json" ), new TypeToken<ArrayList<Block>>(){}.getType());
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
