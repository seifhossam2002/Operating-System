public class Memory {

	private Word[] memory;
	private static int currAvailable = 0;

	public Memory() {
		memory = new Word[40];
//		fillMemoryInitial();
	}

	public Word[] getMemory() {
		return memory;
	}

	public void setMemory(Word[] memory) {
		this.memory = memory;
	}

	@Override
	public String toString() {
		String s="";
		for (int i = 0; i < this.memory.length; i++){
			if(memory[i] != null)
				s+= memory[i].toString()+ ",";
		}
		return s;
	}
}