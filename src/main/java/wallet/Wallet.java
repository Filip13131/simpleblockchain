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

public class Wallet {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private HashMap<String, TransactionOutput> UTXOs = new HashMap<>(); //only UTXOs owned by this wallet.

    public Wallet(){
        KeyPair keyPair = StringUtil.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }
    public Wallet(PrivateKey privateKey, PublicKey publicKey){
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public float getBalance(Blockchain blockchain) {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: blockchain.getUTXOs().entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(getPublicKey())) { //if output belongs to me ( if coins belong to me )
                getUTXOs().put(UTXO.getId(),UTXO); //add it to our list of unspent transactions.
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
        for (Map.Entry<String, TransactionOutput> item: getUTXOs().entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new TransactionInput(UTXO.getId()));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(getPublicKey(), _recipient , value, inputs);
        newTransaction.generateSignature(getPrivateKey());

        for(TransactionInput input: inputs){
            getUTXOs().remove(input.getTransactionOutputId());
        }
        return newTransaction;
    }


    private PrivateKey getPrivateKey() {
        return privateKey;
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }
    private HashMap<String, TransactionOutput> getUTXOs() {
        return UTXOs;
    }
}