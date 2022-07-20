package datastructures.transaction;

import java.util.Objects;

public class TransactionInput {
    private String transactionOutputId; //Reference to TransactionOutputs -> transactionId
    private TransactionOutput UTXO; //Contains the Unspent transaction output

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }
    public TransactionOutput getUTXO() {
        return UTXO;
    }
    public void setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionInput)) return false;
        TransactionInput that = (TransactionInput) o;
        return Objects.equals(getTransactionOutputId(), that.getTransactionOutputId()) && Objects.equals(getUTXO(), that.getUTXO());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransactionOutputId(), getUTXO());
    }
}
