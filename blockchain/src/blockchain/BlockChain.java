package blockchain;

import java.util.ArrayList;
import java.util.Random;
import resources.BCJsonUtils;

public class BlockChain {
	private int difficulty;
	private int nbBlocks = 0;
        private int nbTransMax;
	private String strDiff = ""; // For the checks, "0" difficulty
	private ArrayList<Block> blocks = new ArrayList<Block>();

	/**
	 * Create an empty BlockChain (only genesis)
	 * 
	 * @param diff
	 *            difficulty of BlockChain
	 */
	public BlockChain(int diff) {
		difficulty = diff;
		for (int i = 0; i < difficulty; i++) {
			strDiff += "0";
		}
		genesis();

	}

	/**
	 * Create a Blockchain with n Blocks (counting genesis)
	 * 
	 * @param diff
	 *            difficulty of the BlockChain
	 * @param nbBlocks
	 *            number of Blocks
	 */
	public BlockChain(int diff, int nbBlocks, int nbTransMax) {
		difficulty = diff;
                this.nbTransMax = nbTransMax;
		for (int i = 0; i < difficulty; i++) {
			strDiff += "0";
		}
		genesis();
		addListBlock(nbBlocks - 1);

	}

	/**
	 * Create a BC from a Json file
	 * 
	 * @param jsonFile
	 *            path to Json file used
	 */
	public BlockChain(String jsonFile) {
		BlockChain chain = BCJsonUtils.BCJsonReader(jsonFile);
		difficulty = chain.difficulty;
		nbBlocks = chain.nbBlocks;
		strDiff = chain.strDiff;
		blocks = chain.blocks;
	}

	/**
	 * Add a Block to the BlockChain
	 * 
	 * @param b
	 *            Block to add
	 * @return number of block after adding b
	 */
	private int addBlock(Block b) {
		b.mine(difficulty);
		blocks.add(b);
		nbBlocks++;
		return nbBlocks;
	}

	/**
	 * Generate a Block with random number of transactions
	 * 
	 * @return Block generated
	 */
	private Block generateBlock() {
		Block prevHash = blocks.get(nbBlocks - 1);
		Random rand = new Random();
		int nbTrans = rand.nextInt(nbTransMax) + 1;
		Block b = new Block(nbBlocks, nbTrans, prevHash.getHash());
		b.listTransRandom();
		return b;
	}

	/**
	 * Add list of Blocks to the BlockChain
	 * 
	 * @param n
	 *            number of blocks to add
	 */
	public void addListBlock(int n) {
		int size = nbBlocks + n;
		while (nbBlocks < size) {
			addBlock(generateBlock());
		}
	}

	/**
	 * Getter block
	 * 
	 * @param i
	 *            index of block to get (begin at 1)
	 * @return ith block in the BlockChain
	 */
	public Block getBlock(int i) {
		return blocks.get(i - 1);
	}
        
        public int getNbBlocks(){
            return nbBlocks;
        }

	/**
	 * Create genesis
	 */
	private void genesis() {
		Genesis genesis = new Genesis();
		addBlock(genesis);
	}

	/**
	 * Check the hash of each Blocks in the BlockChain
	 * 
	 * @return false if a hash is critical, true if all is right
	 */
	public boolean checkHash() {
		Block b;
		String cHash = "";
		for (int i = 1; i < nbBlocks; i++) {
			b = blocks.get(i);
			cHash = b.getHash();
			if (!cHash.startsWith(strDiff)) {
				return false;
			}
			if (cHash.substring(difficulty).length() != 64) {
				return false;
			}
			if (!blocks.get(i - 1).getHash().equals(b.getPrevHash())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check the genesis AND the hash of all blocks
	 * 
	 * @return True if all ok, False else
	 */
	public boolean verif1() {
		boolean hash = checkHash();
		boolean gene = checkGenesis();
		System.out.println(hash);
		System.out.println(gene);
		return (hash && gene);
	}

	/**
	 * Check genesis of the BlockChain (first block)
	 * 
	 * @return false if
	 */
	public boolean checkGenesis() {
		Block genesis = blocks.get(0);
		return genesis.isGenesis();
	}

	public void printHash() {
		for (int i = 0; i < nbBlocks; i++) {
			System.out.println(blocks.get(i).getHash());
		}
	}

	/**
	 * Create a Json file with the block
	 * 
	 * @param jsonFile
	 *            path to Json file created
	 */
	public void jsonWrite(String jsonFile) {
		BCJsonUtils.BCJsonWriter(this, jsonFile);
	}
        
        @Override
        public String toString(){
            String ret = "BlockChain : \nNombre de blocks : "+nbBlocks+".\nDifficulté : "+difficulty+".\n Nombre de transaction max : "+nbTransMax+"\n";
            for (int i = 0 ; i < nbBlocks ; i++){
                ret += blocks.get(i).toString();
            }       
            return ret;
        }
}