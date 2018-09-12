package blockchain;

import java.sql.Timestamp;
import java.util.ArrayList;

import resources.HashUtil;
import resources.MerkleUtil;

public class Block {
	// Initialized in constructor
	private int index;
	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	private String prevHash;
	private int nbTrans;
	// Randomized
	private ArrayList<Transaction> transactions;
	// Mining
	private String merkleRoot = null;
	private String hash = null;
	private int nonce = 0;

	/**
	 * Constructor
	 * 
	 * @param index
	 *            index of the block in the chain
	 * @param nbTrans
	 *            number of transactions
	 * @param prevHash
	 *            hash of previous block in chain
	 */
	public Block(int index, int nbTrans, String prevHash) {
		this.index = index;
		this.nbTrans = nbTrans;
		this.prevHash = prevHash;
		this.transactions = new ArrayList<Transaction>();
	}

	/**
	 * Create random list of transactions
	 */
	public void listTransRandom() {
		Transaction t;
		for (int i = 0; i < nbTrans; i++) {
			t = new Transaction(); // Random constructor
			addTransaction(t);
		}
	}

	public void addTransaction(Transaction t) {
		transactions.add(t);
	}

	/**
	 * Calculates merkle root of transaction list
	 */
	public String merkleRoot() {
		merkleRoot = MerkleUtil.merkleRoot(transactions);
		return merkleRoot;
	}

	/**
	 * Mine the block
	 * 
	 * @param d
	 *            difficulty
	 */
	public void mine(int d) {
		String zeros = ""; // zeros string at the beginning of Hash
		while (nonce < d) {
			zeros += "0"; // Add a 0 for each calculation
			hash = zeros + HashUtil.applySha256(index + timestamp.toString() + prevHash + nbTrans + merkleRoot() + nonce);
			nonce++;
		}
		nonce = 0;
	}

	public String getHash() {
		return hash;
	}
	
	public String getPrevHash() {
		return prevHash;
	}

	public boolean isGenesis() {
		return false;
	}
	
	public Timestamp getDate() {
		return timestamp;
	}
        
        @Override
        public String toString(){
            String ret = "Block n°"+index+1+"\nPossede "+nbTrans+" transactions :\n";
            for (int i = 0 ; i < nbTrans ; i++){
                ret+=transactions.get(i).toString();
            }
            return ret;
        }
}