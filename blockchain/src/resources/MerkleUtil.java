package resources;

import java.util.ArrayList;

import blockchain.Transaction;

public class MerkleUtil {
	private static ArrayList<Transaction> split(ArrayList<Transaction> trans, boolean left) {
		int size = trans.size();
		int nb = size/2;
		ArrayList<Transaction> half = new ArrayList<Transaction>();
		if (left) {
			for (int i = 0; i < nb; i++) {
				half.add(trans.get(i));
			}
		} else {
			for (int i = nb; i < size; i++) {
				half.add(trans.get(i));
			}
		}
		return half;
	}

	public static String merkleRoot(ArrayList<Transaction> trans) {
		int nb = trans.size();
		if (nb == 1) {
			return trans.get(0).getHash();
		}
		if (nb % 2 == 1) {
			Transaction copy = (Transaction) trans.get(nb - 1);
			trans.add(copy);
			nb++;
		}
		ArrayList<Transaction> left = split(trans, true);
		ArrayList<Transaction> right = split(trans, false);
		return HashUtil.applySha256(merkleRoot(left) + merkleRoot(right));
	}
}
