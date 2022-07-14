package datastructures;


import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class Blockchain {
    private List<Block> blockchain = new ArrayList<>();

    public Blockchain(int initialSupply, String firstAdress) throws NoSuchAlgorithmException {
        Transaction firstTransfer = new Transaction("God", firstAdress, initialSupply );
        List<Transaction> firstTransactions = new ArrayList<Transaction>();
        firstTransactions.add(firstTransfer);
        Block genesisBlock =  new Block(firstTransactions ,"00000000000000000000000000000000", System.currentTimeMillis());
        blockchain.add(genesisBlock);
    }

    public void createTransaction () {

    }

    public void AddBlockToBlockchain (Block newBlock) {
        blockchain.add(newBlock);
    }

    public List<Block> getBlockchain() {
        return blockchain;
    }

//    public void setBlockchain(List<Block> blockchain) {
//        this.blockchain = blockchain;
//    }
}
