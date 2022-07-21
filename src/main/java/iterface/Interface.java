package iterface;

import blockchainExplorer.BlockchainExplorer;
import datastructures.block.Block;
import datastructures.blockchain.Blockchain;
import datastructures.transaction.Transaction;
import imexpoert.Export;
import imexpoert.Import;
import util.StringUtil;
import wallet.Wallet;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Interface {
    private static Block currentBlock;
    private static Blockchain blockchain;
    private static ArrayList<Wallet> wallets;

    public static void printMenu(String[] options) {
        for (String option : options) {
            System.out.println(option);
        }
        System.out.print("Choose your option : ");

    }

    public static void printMenu(ArrayList<String> options) {
        for (String option : options) {
            System.out.println(option);
        }
        System.out.print("Choose your option : ");

    }

    public static void main(String[] args) {
        String[] options = {"1- initialize a new blockchain -1",
                "2- import blockchain -2",
                "3- exit -3"};

        Scanner scanner = new Scanner(System.in);
        int option = 1;
        while (option != 3) {
            printMenu(options);
            try {
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> startByInitializingNewBlockchain();
                    case 2 -> startByImportingOldBlockchain();
                    case 3 -> exit(0);
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter an integer value between 1 and " + options.length);
                scanner.next();
            } catch (Exception ex) {
                System.out.println("An unexpected error happened. Please try again");
                scanner.next();
            }
        }
    }

    public static void startByInitializingNewBlockchain() {
        ArrayList<Wallet> newWallets = new ArrayList<>();
        Wallet firstWallet = new Wallet();
        newWallets.add(firstWallet);
        Blockchain newBlockchain = new Blockchain(firstWallet.getPublicKey(), 100);
        currentBlock = new Block(newBlockchain.getBlockchain().get(newBlockchain.getBlockchain().size() - 1).getHash());
        blockchain = newBlockchain;
        wallets = newWallets;
        transactionWalletsBlockchainExplorerExportMenu();
    }

    public static void startByImportingOldBlockchain() {
        Blockchain oldBlockchain = Import.importBlockchainFromPath("savedBlockchains/savedBlockchain.json");
        ArrayList<Wallet> oldWallets = new ArrayList<>();
        int i = 0;
        boolean exists = true;
        while (exists) {
            Path path = Paths.get("savedWallets/" +
                    "wallet" +
                    i +
                    ".json");
            exists = Files.exists(path);
            if (exists) {
                oldWallets.add(Import.importWalletFromPath("savedWallets/" +
                        "wallet" +
                        i +
                        ".json"));
            }
            i++;
        }
        currentBlock = new Block(oldBlockchain.getBlockchain().get(oldBlockchain.getBlockchain().size() - 1).getHash());
        blockchain = oldBlockchain;
        wallets = oldWallets;
        transactionWalletsBlockchainExplorerExportMenu();
    }

    public static void transactionWalletsBlockchainExplorerExportMenu() {
        String[] options = {"1- go to transaction creator -1",
                "2- go to wallets -2",
                "3- go to blockchain explorer -3",
                "4- export -4",
                "5- exit -5"};
        Scanner scanner = new Scanner(System.in);
        int option = 1;
        while (option != 5) {
            printMenu(options);
            try {
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> transactionCreator();
                    case 2 -> walletsExplorer();
                    case 3 -> blockchainExplorer();
                    case 4 -> export();
                    case 5 -> exit(0);
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter an integer value between 1 and " + options.length);
                scanner.next();
            } catch (Exception ex) {
                System.out.println("An unexpected error happened. Please try again");
                scanner.next();
            }
        }

    }

    private static void transactionCreator() {
        String[] options = {"1- create new transaction -1",
                "2- mine new block -2",
                "3- exit -3"
        };
        Scanner scanner = new Scanner(System.in);
        int option = 1;
        while (option != 3) {
            printMenu(options);
            try {
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> createNewTransaction();
                    case 2 -> mineCurrentBlock();
                    case 4 -> exit(0);
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter an integer value between 1 and " + options.length);
                scanner.next();
            } catch (Exception ex) {
                System.out.println("An unexpected error happened. Please try again");
                scanner.next();
            }
        }

    }


    private static void createNewTransaction() {
        int from;
        int to;
        float value;
        ArrayList<String> options = new ArrayList<>();
        for (int i = 0; i < wallets.size(); i++) {
            options.add(i + "- wallet" + i + " with balance: " + wallets.get(i).getBalance(blockchain) + " -" + i);
        }
        System.out.println("from witch wallet would you like to make a transaction:");
        from = fromAndTo(options);

        System.out.println("to witch wallet would you like to make a transaction:");
        to = fromAndTo(options);

        System.out.println("enter value of the new transaction:");
        Scanner scanner = new Scanner(System.in);
        value = scanner.nextFloat();

        Transaction newTransaction = wallets.get(from).sendFunds(wallets.get(to).getPublicKey(), value, blockchain);
        currentBlock.addTransaction(newTransaction, blockchain);

    }

    private static int fromAndTo(ArrayList<String> options) {
        Scanner scanner = new Scanner(System.in);
        int option = 1;
        while (true) {
            printMenu(options);
            try {
                option = scanner.nextInt();
                return option;
            } catch (InputMismatchException ex) {
                System.out.println("Please enter an integer value between 1 and " + options.size());
                scanner.next();
            } catch (Exception ex) {
                System.out.println("An unexpected error happened. Please try again");
                scanner.next();
            }
        }
    }

    private static void mineCurrentBlock() {
        blockchain.mineAndAddBlock(currentBlock);
        currentBlock = new Block(currentBlock.getHash());
    }

    private static void walletsExplorer() {
        String[] options = {
                "1- print all wallets details -1",
                "2- add new wallet -2",
                "3- exit -3"
        };
        Scanner scanner = new Scanner(System.in);
        int option = 1;
        while (option != 3) {
            printMenu(options);
            try {
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> printAllWallets();
                    case 2 -> addWallet();
                    case 4 -> exit(0);
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter an integer value between 1 and " + options.length);
                scanner.next();
            } catch (Exception ex) {
                System.out.println("An unexpected error happened. Please try again");
                scanner.next();
            }
        }
    }

    private static void addWallet() {
        Wallet wallet = new Wallet();
        wallets.add(wallet);
    }

    private static void printAllWallets() {
        for (int i = 0; i < wallets.size(); i++) {
            System.out.println("wallet" + i + ":");
            System.out.println(" Address: " + StringUtil.getStringFromKey(wallets.get(i).getPublicKey()));
            System.out.println(" Balance: " + wallets.get(i).getBalance(blockchain));
            System.out.println("  TransactionHistory:");
            for (Transaction transaction : wallets.get(i).getTransactionHistory(blockchain)) {
                System.out.println("--------------------------------");
                System.out.println("   Sender: " + StringUtil.getStringFromKey(transaction.getSender()));
                System.out.println("   Recipient: " + StringUtil.getStringFromKey(transaction.getRecipient()));
                System.out.println("   Value: " + transaction.getValue());
                System.out.println("--------------------------------");

            }
        }
    }

    private static void blockchainExplorer() {
        String[] options = {
                "1- Print out blockchain length -1",
                "2- Print out transactions included in a block -2",
                "3- Print out block by Block Hash -3",
                "4- Print out block by Block Height -4",
                "5- Print out the last block -5",
                "6- exit -6"
        };
        Scanner scanner = new Scanner(System.in);
        int option = 1;
        while (option != 6) {
            printMenu(options);
            try {
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> BlockchainExplorer.printOutBlockchainLength(blockchain);
                    case 2 -> printOutTransactionsIncludedInABlock();
                    case 3 -> printOutBlockByHash();
                    case 4 -> printOutBlockByHeight();
                    case 5 -> BlockchainExplorer.printOutLastBlockDetails(blockchain);
                    case 8 -> exit(0);
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter an integer value between 1 and " + options.length);
                scanner.next();
            } catch (Exception ex) {
                System.out.println("An unexpected error happened. Please try again");
                scanner.next();
            }
        }
    }
    private static void printOutTransactionsIncludedInABlock () {
        System.out.println("Provide block height:");
        Scanner scanner = new Scanner(System.in);
        BlockchainExplorer.printOutTransactionsIncludedInABlock(blockchain.getBlockchain().get(scanner.nextInt()));

    }

    private static void printOutBlockByHash () {
        System.out.println("Provide hash:");
        Scanner scanner = new Scanner(System.in);
        BlockchainExplorer.printOutBlockDetailsByBlockHash(blockchain, scanner.nextLine());
    }
    private static void printOutBlockByHeight () {
        System.out.println("Provide height:");
        Scanner scanner = new Scanner(System.in);
        BlockchainExplorer.printOutBlockDetailsByBlockHeight(blockchain, scanner.nextInt());
    }
    private static void export () {
        Export.exportBlockchain(blockchain);
        for (int i=0 ; i<wallets.size(); i++) Export.exportWallet(wallets.get(i), "wallet" + i);
        System.out.println("Export succeeded!!!");
    }

}

