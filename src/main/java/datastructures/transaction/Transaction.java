package datastructures.transaction;

import datastructures.blockchain.Blockchain;
import util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;



public class Transaction {
    public String transactionId; // this is also the hash of the transaction.
    public PublicKey sender; // senders address/public key.
    public PublicKey recipient; // Recipients address/public key.
    public float value;
    public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
    public long timeStamp; //as number of milliseconds since 1/1/1970.
    public ArrayList<TransactionInput> inputs;
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();
    private static int sequence = 0; // a rough count of how many transactions have been generated.

    // Constructor:
    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.timeStamp = new Date().getTime();
        this.value = value;
        this.inputs = inputs;
    }
    //Constructor for importing:
    public Transaction(PublicKey from,
                       PublicKey to,
                       float value,
                       ArrayList<TransactionInput> inputs,
                       ArrayList<TransactionOutput> outputs,
                       byte[] signature,
                       String transactionId,
                       long timeStamp){
        this.sender = from;
        this.recipient = to;
        this.outputs = outputs;
        this.signature = signature;
        this.transactionId = transactionId;
        this.timeStamp = timeStamp;
        this.value = value;
        this.inputs = inputs;
    }

    // This Calculates the transaction hash (which will be used as its Id)
    private String calculateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient) +
                        value + sequence +
                        timeStamp
        );
    }
    //Returns true if new transaction could be created.
    public boolean processTransaction(Blockchain blockchain) {

        if(verifySignature()) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent):
        for(TransactionInput i : inputs) {
            i.UTXO = blockchain.getUTXOs().get(i.transactionOutputId);
        }

        //check if transaction is valid:
        if(getInputsValue() < blockchain.getMinimumTransaction()) {
            System.out.println("#Transaction Inputs to small: " + getInputsValue());
            return false;
        }

        //generate transaction outputs:
        float leftOver = getInputsValue() - value; //get value of inputs then the leftover change:
        transactionId = calculateHash();
        outputs.add(new TransactionOutput( this.recipient, value,transactionId)); //send value to recipient
        outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender

        //add outputs to Unspent list
        for(TransactionOutput o : outputs) {
            blockchain.getUTXOs().put(o.id , o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            blockchain.getUTXOs().remove(i.UTXO.id);
        }

        return true;
    }

    //returns sum of inputs(UTXOs) values
    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            total += i.UTXO.value;
        }
        return total;
    }

    //returns sum of outputs:
    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender)
                + StringUtil.getStringFromKey(recipient)
                + value;
        signature = StringUtil.applyECDSASig(privateKey,data);
    }
    //Verifies the data we signed hasn't been tampered with
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender)
                + StringUtil.getStringFromKey(recipient)
                + value;
        return !StringUtil.verifyECDSASig(sender, data, signature);
    }
}
