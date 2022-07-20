package datastructures.block;

import datastructures.blockchain.Blockchain;
import util.StringUtil;
import datastructures.transaction.Transaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Block {

    private String hash;
    private final String previousHash;
    private String merkleRoot;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private final long timeStamp; //as number of milliseconds since 1/1/1970.
    private int nonce;

    //Block Constructor.
    public Block( String previousHash ) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }

    //Calculate new hash based on blocks contents
    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                        timeStamp +
                        nonce +
                        merkleRoot
        );
    }

    //Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty) {
        ArrayList<String> transactionIds = new ArrayList<>();
        for (Transaction transaction: transactions){
            transactionIds.add(transaction.getTransactionId());
        }
        merkleRoot = StringUtil.getMerkleRoot(transactionIds);
        String target = StringUtil.getDifficultyString(difficulty); //Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction, Blockchain blockchain) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if((!previousHash.equals("0"))) {
            if((!transaction.processTransaction(blockchain))) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }

    public String getHash() {
        return hash;
    }
    public String getPreviousHash() {
        return previousHash;
    }
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public String getMerkleRoot() {
        return merkleRoot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block) o;
        return getTimeStamp() == block.getTimeStamp() && nonce == block.nonce && Objects.equals(getHash(), block.getHash()) && Objects.equals(getPreviousHash(), block.getPreviousHash()) && Objects.equals(getMerkleRoot(), block.getMerkleRoot()) && Objects.equals(getTransactions(), block.getTransactions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHash(), getPreviousHash(), getMerkleRoot(), getTransactions(), getTimeStamp(), nonce);
    }
}