package test;

import blockchain.Transaction;
import resources.HashUtil;

public class TransTest {
	public static void main(String[] args) {
		Transaction t = new Transaction("SOURCE", "DEST", 10);
		System.out.println(t.getHash());
		System.out.println(t.getHash());
		System.out.println(HashUtil.applySha256(t.getHash()));
	}
}
