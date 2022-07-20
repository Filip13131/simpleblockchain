package iterface;

import datastructures.blockchain.Blockchain;
import imexpoert.Import;

public class Interface {


    public static void main(String[] args) {
        Blockchain blockchain = Import.importBlockchainFromPath("savedBlockchains/savedBlockchain.json");
        System.out.println(blockchain.isChainValid());

    }

}
