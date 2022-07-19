package datastructures.blockchain;


import datastructures.block.Block;
import datastructures.transaction.Transaction;
import datastructures.transaction.TransactionInput;
import datastructures.transaction.TransactionOutput;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import util.StringUtil;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class Blockchain {
    private ArrayList<Block> blockchain = new ArrayList<>();
    private HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
    private int difficulty = 3;
    private float minimumTransaction = 0.1f;
    private Transaction genesisTransaction;
//Constructor:
    public Blockchain (PublicKey addressOfGenesisTransaction , float initialSupply){
        initializeBlockchain( addressOfGenesisTransaction , initialSupply);
    }

    public void initializeBlockchain(PublicKey addressOfGenesisTransaction , float initialSupply) {
        //Setup Bouncey castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());
        //everything to do with the first transaction
        KeyPair firstSignature = StringUtil.generateKeyPair();
        genesisTransaction = new Transaction(firstSignature.getPublic(), addressOfGenesisTransaction, initialSupply, null);
        genesisTransaction.generateSignature(firstSignature.getPrivate());	 //manually sign the genesis transaction
        genesisTransaction.setTransactionId("0"); //manually set the transaction id
        genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId())); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0)); //it's important to store our first transaction in the UTXOs list.
        //everything to do with the first block
                                                                                                    //System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        if (!genesis.addTransaction(genesisTransaction, this)){
            System.out.println("initializeBlockchain invalid");
        }
        mineAndAddBlock(genesis);
        System.out.println(isChainValid());
    }

    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));

        //loop through blockchain to check hashes:
        for(int i = 1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.getHash().equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop through blockchains transactions:
            TransactionOutput tempOutput;
            for(int t = 0; t < currentBlock.getTransactions().size(); t++) {
                Transaction currentTransaction = currentBlock.getTransactions().get(t);

                if(currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.getInputs()) {
                    tempOutput = tempUTXOs.get(input.getTransactionOutputId());

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.getUTXO().getValue() != tempOutput.getValue()) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.getTransactionOutputId());
                }

                for(TransactionOutput output: currentTransaction.getOutputs()) {
                    tempUTXOs.put(output.getId(), output);
                }

                if( currentTransaction.getOutputs().get(0).getRecipient() != currentTransaction.getRecipient()) {
                    System.out.println("#Transaction(" + t + ") output recipient is not who it should be");
                    return false;
                }
                if( currentTransaction.getOutputs().get(1).getRecipient() != currentTransaction.getSender()) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public void mineAndAddBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public ArrayList<Block> getBlockchain() {
        return blockchain;
    }
    public HashMap<String, TransactionOutput> getUTXOs() {
        return UTXOs;
    }
    public float getMinimumTransaction() {
        return minimumTransaction;
    }
}