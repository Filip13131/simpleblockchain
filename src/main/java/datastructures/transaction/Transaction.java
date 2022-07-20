package datastructures.transaction;

import datastructures.blockchain.Blockchain;
import util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static util.StringUtil.getStringFromKey;


public class Transaction {
    private String transactionId; // this is also the hash of the transaction.
    private final PublicKey sender; // senders address/public key.
    private final PublicKey recipient; // Recipients address/public key.
    private final float value;
    private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
    private final long timeStamp; //as number of milliseconds since 1/1/1970.
    private final ArrayList<TransactionInput> inputs;
    private ArrayList<TransactionOutput> outputs = new ArrayList<>();
    private static int sequence; // a rough count of how many transactions have been generated.

    // Constructor:
    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.timeStamp = new Date().getTime();
        this.value = value;
        this.inputs = inputs;
    }


    // This Calculates the transaction hash (which will be used as its ID)
    private String calculateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                getStringFromKey(sender) +
                        getStringFromKey(recipient) +
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
            i.setUTXO(blockchain.getUTXOs().get(i.getTransactionOutputId()));
        }

        //check if transaction is valid:
        if(getInputsValue() < blockchain.getMinimumTransaction()) {
            System.out.println("#Transaction Inputs to small: " + getInputsValue());
            return false;
        }

        //generate transaction outputs:
        float leftOver = getInputsValue() - value; //get value of inputs then the leftover change:
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.recipient, value, transactionId)); //send value to recipient
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); //send the left over 'change' back to sender

        //add outputs to Unspent list
        for(TransactionOutput o : outputs) {
            blockchain.getUTXOs().put(o.getId(), o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for(TransactionInput i : inputs) {
            if(i.getUTXO() == null) continue; //if Transaction can't be found skip it
            blockchain.getUTXOs().remove(i.getUTXO().getId());
        }

        return true;
    }
    //returns sum of inputs(UTXOs) values
    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.getUTXO() == null) continue; //if Transaction can't be found skip it
            total += i.getUTXO().getValue();
        }
        return total;
    }
    //returns sum of outputs:
    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.getValue();
        }
        return total;
    }
    public void generateSignature(PrivateKey privateKey) {
        String data = getStringFromKey(sender)
                + getStringFromKey(recipient)
                + value;
        signature = StringUtil.applyECDSASig(privateKey, data);
    }
    //Verifies the data we signed hasn't been tampered with
    public boolean verifySignature() {
        String data = getStringFromKey(sender)
                + getStringFromKey(recipient)
                + value;
        return !StringUtil.verifyECDSASig(sender, data, signature);
    }


    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public PublicKey getSender() {
        return sender;
    }
    public PublicKey getRecipient() {
        return recipient;
    }
    public float getValue() {
        return value;
    }
    public ArrayList<TransactionInput> getInputs() {
        return inputs;
    }
    public ArrayList<TransactionOutput> getOutputs() {
        return outputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Float.compare(that.getValue(), getValue()) == 0 && timeStamp == that.timeStamp && Objects.equals(getTransactionId(), that.getTransactionId()) && Objects.equals(getSender(), that.getSender()) && Objects.equals(getRecipient(), that.getRecipient()) && Arrays.equals(signature, that.signature) && Objects.equals(getInputs(), that.getInputs()) && Objects.equals(getOutputs(), that.getOutputs());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getTransactionId(), getSender(), getRecipient(), getValue(), timeStamp, getInputs(), getOutputs());
        result = 31 * result + Arrays.hashCode(signature);
        return result;
    }
}
