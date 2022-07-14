package datastructures;

import org.jetbrains.annotations.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Block {
    private String hash;
    private String previousHash;

    private List<Transaction> data;
    private long timeStamp;
    private int nonce = 0;

    public Block(List<Transaction> data, String previousHash, long timeStamp) throws NoSuchAlgorithmException {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = calculateBlockHash();
    }
    public String calculateBlockHash() throws NoSuchAlgorithmException {
        String dataToHash = previousHash
                + Long.toString(timeStamp)
                + Integer.toString(nonce)
                + fromDataToString();
        MessageDigest digest = null;
        byte[] bytes = null;
        digest = MessageDigest.getInstance("SHA-256");
        bytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
        StringBuilder buffer = new StringBuilder();
        assert bytes != null;
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }

        return buffer.toString();
    }

    private @NotNull String fromDataToString() {
        StringBuilder res= new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            res.append(data.get(i).getSender() + data.get(i).getReceiver()+data.get(i).getAmount());
        }
        return res.toString();
    }

    public String mineBlock(int prefix) throws NoSuchAlgorithmException {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hash.substring(0, prefix).equals(prefixString)) {
            nonce++;
            hash = calculateBlockHash();
        }
        return hash;
    }
}