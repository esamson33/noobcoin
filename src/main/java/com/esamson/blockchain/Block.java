package com.esamson.blockchain;

import java.util.Date;

public class Block {

    public String hash;
    public String prevHash;
    private String data;        // simple message
    private long timestamp;     // seconds since epoch
    private int nonce;          // use once

    /**
     * Block constructor
     */
    public Block(String data, String previousHash) {
        this.data = data;
        this.prevHash = previousHash;
        this.timestamp = new Date().getTime();
        hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                prevHash +
                Long.toString(timestamp) +
                Integer.toString(nonce) +
                data
            );
    }

    public void mineBlock(int difficulty) {
        var target = new String(new char[difficulty])
            .replace('\0', '0'); // Create a string with difficulty * "0"

        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        
        System.out.println("Block mined: " + hash);
    }
}
