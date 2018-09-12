package blockchain;

public class Genesis extends Block {
	public Genesis() {
		super(0, 1, "0");
		Transaction trans = new Transaction("genesis", "", 0);
		this.addTransaction(trans);
	}
	
	public void mine(int d) {
		super.mine(1);
	}
	
	public boolean isGenesis() {
		return true;
	}
        
        public String toString(){
            return "Block genesis\n";
        }
}