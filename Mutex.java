import java.util.LinkedList;
import java.util.Queue;
import java.util.*;

public class Mutex {

	private boolean userInput;
	private boolean userOutput;
	private boolean file;
	private String inputPCBname;
	private String outputPCBname;
	private String filePCBname;
	private Queue<PCB> blockedUserInputQueue;
	private Queue<PCB> blockedUserOutputQueue;
	private Queue<PCB> blockedFileQueue;
	

	public Mutex() {
		this.userInput = true;
		this.userOutput = true;
		this.file = true;
		this.blockedUserInputQueue = new LinkedList<PCB>();
		this.blockedUserOutputQueue = new LinkedList<PCB>();
		this.blockedFileQueue = new LinkedList<PCB>();
	}

	public boolean isUserInput() {
		return userInput;
	}

	public boolean isUserOutput() {
		return userOutput;
	}

	public boolean isFile() {
		return file;
	}

	public boolean semWait(PCB p, String st) {

		switch (st.trim()) {
		case "userInput":{
			if (this.userInput) {
				this.userInput = false;
				this.inputPCBname = p.getName();
				p.setState(PCBState.RUNNING);
				return true;
			} else {
				p.setState(PCBState.RUNNING);
				p.setState(PCBState.BLOCKED);
				blockedUserInputQueue.add(p);
				return false;
			}
		}
		case "userOutput":{
			if (userOutput) {
				userOutput = false;
				outputPCBname = p.getName();
				p.setState(PCBState.RUNNING);
				return true;
			} else {
				p.setState(PCBState.RUNNING);
				p.setState(PCBState.BLOCKED);
				blockedUserOutputQueue.add(p);
				return false;
			}
		}
		case "file":{
			if (file) {
				file = false;
				filePCBname = p.getName();
				p.setState(PCBState.RUNNING);
				return true;
			} else {
				p.setState(PCBState.RUNNING);
				p.setState(PCBState.BLOCKED);
				blockedFileQueue.add(p);
				return false;
			}
		}
		}return false;
	}

	public PCB semSignal(PCB p, String s) {
		switch (s.trim()) {
		case "userInput":
			if (!userInput && p.getName().equals(inputPCBname)) {
				userInput = true;
				blockedUserInputQueue.peek().setState(PCBState.READY);
				return blockedUserInputQueue.poll();
			}
			else{
				System.out.println(p.getName() +" "+inputPCBname);
			}
			break;
		case "userOutput":
			if (!userOutput && p.getName().equals(outputPCBname)) {
				userOutput = true;
				blockedUserOutputQueue.peek().setState(PCBState.READY);
				return blockedUserOutputQueue.poll();
			}
			break;
		case "file":
			if (!file && p.getName().equals(filePCBname)) {
				file = true;
				blockedFileQueue.peek().setState(PCBState.READY);
				return blockedFileQueue.poll();
			}
			break;

		}
		return null;

	}

}