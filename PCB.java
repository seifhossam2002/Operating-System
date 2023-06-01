
public class PCB {

	private String name;
	private PCBState state;
	private int counter;
	private int baseAddress;
	private int memoryLimit;

	public PCB(String name) {
		this.name = name;
		state = state.READY;
//		counter = 0;
//		this.baseAddress = baseAddress;
//		this.memoryLimit = baseAddress + 20;
	}

	public PCBState getState() {
		return state;
	}

	public void setState(PCBState state) {
		this.state = state;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getBaseAddress() {
		return baseAddress;
	}

	public void setBaseAddress(int baseAddress) {
		this.baseAddress = baseAddress;
	}

	public int getMemoryLimit() {
		return memoryLimit;
	}

	public void setMemoryLimit(int memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	public String getName() {
		return name;
	}

}
