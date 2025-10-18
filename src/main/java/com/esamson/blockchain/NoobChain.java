package com.esamson.blockchain;

import java.security.Security;
import java.util.*;
//import java.util.Base64;
//import com.google.gson.GsonBuilder;

public class NoobChain 
{
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

    public static int difficulty = 5;
    public static float minimumTransaction = 0.1f;
    public static Wallet walletA;
    public static Wallet walletB;

    public static void main( String[] args ) {

        // Setup Bouncy castle as a Security Provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // Create the new wallets
        walletA = new Wallet();
        walletB = new Wallet();

        // Test public and private keys
        System.out.println("Private and public keys:");
        System.out.println(" - " + StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(" - " + StringUtil.getStringFromKey(walletA.publicKey));

        // Create a test transaction from WalletA to WalletB
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);

        // Verify the signature works and verify it from the public key
        System.out.println("Is signature verified: ");
        System.out.println(" - " + (transaction.verifySignature() ? "true" : "false"));

        /*
        blockchain.add(new Block("Hi im the first block", "0"));
        System.out.println("Mining block 1...");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new Block("Yo im the second block", blockchain.get(blockchain.size() - 1).hash));        
        System.out.println("Mining block 2...");
        blockchain.get(1).mineBlock(difficulty);
        
        blockchain.add(new Block("Hey im the third block", blockchain.get(blockchain.size() - 1).hash));        
        System.out.println("Mining block 3...");
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("The blockchain is " + (isChainValid() ? "valid" : "not valid") + ".");

        var blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);
         */
    }

    public static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for (int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            // Compare registered hash vs calculated hash
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current hashes not equal");
                return false;
            }

            // Compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.prevHash)) {
                System.out.println("Previous hashes not equal");
                return false;
            }

            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}
