package datastructures.transaction;

import util.StringUtil;

import java.security.PublicKey;

public class TransactionOutput {
    private final String id;
    private final PublicKey recipient; //also known as the new owner of these coins.
    private final float value; //the amount of coins they own
    private final String parentTransactionId; //the id of the transaction this output was created in

    //Constructor
    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(recipient)
                + value
                + parentTransactionId);
    }

    //Check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == recipient);
    }
    public String getId() {
        return id;
    }
    public PublicKey getRecipient() {
        return recipient;
    }
    public float getValue() {
        return value;
    }

}