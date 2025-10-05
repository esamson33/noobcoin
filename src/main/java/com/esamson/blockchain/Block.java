package com.esamson.blockchain;

import java.util.Date;

public class Block {

    public String hash;
    public String prevHash;
    private String data;        // simple message
    private long timestamp;     // seconds since epoch

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
                data
            );
    }

}
