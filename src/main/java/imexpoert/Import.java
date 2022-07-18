package imexpoert;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import datastructures.blockchain.Blockchain;
import util.PublicKeyAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;

public class Import {

    public static Blockchain fromJsonToBlockchain(String json){
        Blockchain blockchain;
        blockchain = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(PublicKey.class, new PublicKeyAdapter())
                .create()
                .fromJson( json , new TypeToken<Blockchain>(){}.getType() );
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
