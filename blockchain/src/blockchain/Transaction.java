package blockchain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import resources.HashUtil;

public class Transaction {
	private String src;
	private String dst;
	private int amt;

	/**
	 * Constructor of a transaction
	 * 
	 * @param src
	 *            source
	 * @param dst
	 *            destination
	 * @param amt
	 *            amount of money
	 */
	
	public Transaction(String src, String dst, int amt) {
		this.src = src;
		this.dst = dst;
		this.amt = amt;
	}

	public Transaction(File f) {
	}// Lecture fichier niveau 2

	public Transaction() {
		this.src = randomEnt("files/entreprises");
		this.dst = randomEnt("files/entreprises");
		this.amt = randomAmt();
	}

	public String getTrans() {
		return src + "-" + dst + amt;
	}

	/**
	 * Calculate hash of a transaction
	 * 
	 * @return Hash of transaction
	 */
	public String getHash() {
		return HashUtil.applySha256(getTrans());
	}

	/**
	 * Generate random company
	 * 
	 * @param filePath
	 *            Path of the file where the companies are
	 * @return a random line of the file
	 */
	private String randomEnt(String filePath) {
		try {
			InputStream stream = new FileInputStream(filePath);
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader buff = new BufferedReader(reader);
			String line = "";
			Random rand = new Random();
			int n = rand.nextInt(30) + 1;
			for (int i = 0; i < n; i++) {
				line = buff.readLine();
			}
			buff.close();
			return line;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return "";
	}

	/**
	 * Generate random amount
	 * 
	 * @return random amount
	 */
	private int randomAmt() {
		Random rand = new Random();
		int n = rand.nextInt(1000) + 1;
		return n;
	}
        
        @Override
        public String toString(){
            String ret = "Transaction de "+amt+"DCC de "+src+" vers "+dst+"\n";
            return ret;
        }
}