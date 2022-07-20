package wallet;

import datastructures.blockchain.Blockchain;
import datastructures.transaction.Transaction;
import datastructures.transaction.TransactionInput;
import datastructures.transaction.TransactionOutput;
import util.StringUtil;

import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Wallet {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private HashMap<String, TransactionOutput> UTXOs = new HashMap<>(); //only UTXOs owned by this wallet.
    private ArrayList<Transaction> transactionHistory = new ArrayList<>();
    private int historyMarker = 0;

    public Wallet(){
        KeyPair keyPair = StringUtil.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }
    public Wallet(PrivateKey privateKey, PublicKey publicKey){
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
    public void refreshTransactionHistory(Blockchain blockchain){
        for (int i = historyMarker; i < blockchain.getBlockchain().size() ; i++){
            ArrayList<Transaction> transactionsToCheck;
            transactionsToCheck = blockchain.getBlockchain().get(i).getTransactions();
            for (Transaction transaction : transactionsToCheck){
                if (!transaction.getSender().equals(publicKey)){
                    if (!transaction.getRecipient().equals(publicKey)){
                        continue;
                    }
                }
                transactionHistory.add(transaction);
            }
        }
        historyMarker = blockchain.getBlockchain().size();
    }
    public float getBalance(Blockchain blockchain) {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: blockchain.getUTXOs().entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
                UTXOs.put(UTXO.getId(),UTXO); //add it to our list of unspent transactions.
                total += UTXO.getValue();
            }
        }
        return total;
    }
    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(PublicKey _recipient, float value , Blockchain blockchain) {
        if(getBalance(blockchain) < value) { //gather balance and check funds.
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        //create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new TransactionInput(UTXO.getId()));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input: inputs){
            UTXOs.remove(input.getTransactionOutputId());
        }
        return newTransaction;
    }
    public ArrayList<Transaction> getTransactionHistory(Blockchain blockchain){
        refreshTransactionHistory(blockchain);
        return transactionHistory;
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    public HashMap<String, TransactionOutput> getUTXOs() {
        return UTXOs;
    }
    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
    public int getHistoryMarker() {
        return historyMarker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wallet)) return false;
        Wallet wallet = (Wallet) o;
        return getHistoryMarker() == wallet.getHistoryMarker() && getPrivateKey().equals(wallet.getPrivateKey()) && getPublicKey().equals(wallet.getPublicKey()) && Objects.equals(getUTXOs(), wallet.getUTXOs()) && Objects.equals(getTransactionHistory(), wallet.getTransactionHistory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrivateKey(), getPublicKey(), getUTXOs(), getTransactionHistory(), getHistoryMarker());
    }
}