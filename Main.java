import java.util.*;
import java.io.*;

public class Main {

	Queue<PCB> readyQueue;
	Queue<PCB> FinQueue;
	Queue<PCB> BlockedQueue;
	Mutex mutex;
	Memory memory;
	int finished = 0;
	int noOfPcb = 3;
	int clock;
	int timeslice;
	String[] schedule= new String[3];

	public Main() throws InvalidAccess {
		String time;
		mutex = new Mutex();
		readyQueue = new LinkedList<PCB>();
		FinQueue = new LinkedList<PCB>();
		BlockedQueue = new LinkedList<PCB>();
		memory = new Memory();
		Scanner sc1 = new Scanner(System.in);
		System.out.println("At which cycle will Program_1 enter?");
		time = sc1.nextLine();
		this.schedule[1]=time+ " "+ "Program_1";
		Scanner sc2 = new Scanner(System.in);
		System.out.println("At which cycle will Program_2 enter?");
		time = sc2.nextLine();
		this.schedule[2]=time+ " "+ "Program_2";
		Scanner sc3 = new Scanner(System.in);
		System.out.println("At which cycle will Program_3 enter?");
		time = sc3.nextLine();
		this.schedule[0]=time+ " "+ "Program_3";
		Scanner sc4 = new Scanner(System.in);
		System.out.println("what is the value of time slice do you wish to use?");
		timeslice = sc4.nextInt();
		Scheduler();		
		this.clock = 0;
		
	}

	public void addPCB(PCB p) {
		// lw awel wa7da fadya
		if (memory.getMemory()[0] == null) {
			String currName = p.getName();
			try (BufferedReader reader = new BufferedReader(new FileReader(
					"programs/" + currName + ".txt"))) {
				String line;
				p.setBaseAddress(0);
				p.setMemoryLimit(19);
				p.setCounter(8);
				memory.getMemory()[0] = new Word("ProcessName", currName);
				memory.getMemory()[1] = new Word("ProcessState", p.getState()
						+ "");
				memory.getMemory()[2] = new Word("ProcessCounter",
						p.getCounter() + "");
				memory.getMemory()[3] = new Word("BaseAddress",
						p.getBaseAddress() + "");
				memory.getMemory()[4] = new Word("MemoryLimit",
						p.getMemoryLimit() + "");
				memory.getMemory()[5] = new Word("Variable1", "");
				memory.getMemory()[6] = new Word("Variable2", "");
				memory.getMemory()[7] = new Word("Variable3", "");
				int indexOfMemory = 8;
				while (!Objects.isNull(line = reader.readLine())
						&& indexOfMemory < 20) {
					String[] words = line.split(" ");
					// for (int indexOfMemory = 8; indexOfMemory < 20;
					// indexOfMemory++) {
					String data = "";
					for (int i = 1; i < words.length; i++) {
						data += words[i] + " ";
					}
					data.trim();
					memory.getMemory()[indexOfMemory] = new Word(words[0], data);
					indexOfMemory++;

				}
				for (int i = indexOfMemory; i <= p.getMemoryLimit(); i++) {
					memory.getMemory()[i] = new Word("", "empty");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (Objects.isNull(memory.getMemory()[20])) { // lw tany wa7da
																// fadya
			//System.out.println("should be proc 2 ---- " + p.getName());
			String currName = p.getName();
			try (BufferedReader reader = new BufferedReader(new FileReader("programs/" + currName + ".txt"))) {
				String line;
				p.setBaseAddress(20);
				p.setMemoryLimit(39);
				p.setCounter(28);
				memory.getMemory()[20] = new Word("ProcessName", currName);
				memory.getMemory()[21] = new Word("ProcessState", p.getState()
						+ "");
				memory.getMemory()[22] = new Word("ProcessCounter",
						p.getCounter() + "");
				memory.getMemory()[23] = new Word("BaseAddress",p.getBaseAddress() + "");
				memory.getMemory()[24] = new Word("MemoryLimit",
						p.getMemoryLimit() + "");
				memory.getMemory()[25] = new Word("Variable1", "");
				memory.getMemory()[26] = new Word("Variable2", "");
				memory.getMemory()[27] = new Word("Variable3", "");
				int indexOfMemory = 28;
				while (!Objects.isNull(line = reader.readLine())
						&& indexOfMemory < 40) {
					String[] words = line.split(" ");
					// for (int indexOfMemory = 28; indexOfMemory < 40;
					// indexOfMemory++) {
					String data = "";
					for (int i = 1; i < words.length; i++) {
						data += words[i] + " ";
					}
					data.trim();
					memory.getMemory()[indexOfMemory] = new Word(words[0], data);
					indexOfMemory++;
					// }
				}
				for (int i = indexOfMemory; i <= p.getMemoryLimit(); i++) {
					memory.getMemory()[i] = new Word("", "empty");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { // iaw mafish makan fel memory --> SWAP
			swap(p);
			System.out.println("Process swapped  " + p.getName());
		}
	}

	private void swap(PCB currProcess) { // 3'yrt feeha
		ArrayList<Word> arr = new ArrayList<>();
		System.out.println();
		int baseAddressRemoved = 0;

		if(memory.getMemory()[21].getData().equals(PCBState.FINISHED + ""))
			baseAddressRemoved = 20;
		else
			if(memory.getMemory()[1].getData().equals(PCBState.FINISHED + ""))
				baseAddressRemoved = 0;
			else
				if (memory.getMemory()[21].getData().equals(PCBState.BLOCKED + ""))
					baseAddressRemoved = 20;
				else 
					if (memory.getMemory()[1].getData().equals(PCBState.BLOCKED + ""))
						baseAddressRemoved = 0;
					else
						if (memory.getMemory()[1].getData().equals(PCBState.READY + ""))
							baseAddressRemoved = 0;
					else
						if (memory.getMemory()[21].getData().equals(PCBState.READY + ""))
						baseAddressRemoved = 20;
		for (int i = baseAddressRemoved; i < baseAddressRemoved + 20; i++) {
			arr.add(memory.getMemory()[i]);
		}
		System.out.println("To be moved to the disk "+arr);
		//System.out.println(baseAddressRemoved);
		convertToTextFile(arr);
		String currName = currProcess.getName();
//		System.out.println(currProcess.getName());
//		System.out.println(memory.getMemory()[baseAddressRemoved]+" "+memory.getMemory()[baseAddressRemoved + 1].getData());
//		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		try (BufferedReader reader = new BufferedReader(new FileReader(
				"programs/" + currName + ".txt"))) {
			String line;
			currProcess.setBaseAddress(baseAddressRemoved);
			currProcess.setMemoryLimit(baseAddressRemoved + 19);
			currProcess.setCounter(baseAddressRemoved + 8);
			memory.getMemory()[baseAddressRemoved] = new Word("ProcessName",
					currName + "");
			memory.getMemory()[baseAddressRemoved + 1] = new Word(
					"ProcessState", currProcess.getState() + "");
			memory.getMemory()[baseAddressRemoved + 2] = new Word(
					"ProcessCounter", currProcess.getCounter() + "");
			memory.getMemory()[baseAddressRemoved + 3] = new Word(
					"BaseAddress", currProcess.getBaseAddress() + "");
			memory.getMemory()[baseAddressRemoved + 4] = new Word(
					"MemoryLimit", currProcess.getMemoryLimit() + "");
			memory.getMemory()[baseAddressRemoved + 5] = new Word("Variable1",
					"");
			memory.getMemory()[baseAddressRemoved + 6] = new Word("Variable2",
					"");
			memory.getMemory()[baseAddressRemoved + 7] = new Word("Variable3",
					"");
			int indexOfMemory = baseAddressRemoved + 8;
			while (!Objects.isNull(line = reader.readLine())
					&& indexOfMemory < baseAddressRemoved + 20) {
				String[] words = line.split(" ");
				// for (int indexOfMemory = 8; indexOfMemory < 20;
				// indexOfMemory++) {
				String data = "";
				for (int i = 1; i < words.length; i++) {
					data += words[i] + " ";
				}
				data.trim();
				memory.getMemory()[indexOfMemory] = new Word(words[0], data);
				indexOfMemory++;
			}
			for (int i = indexOfMemory; i <= currProcess.getMemoryLimit(); i++) {
				memory.getMemory()[i] = new Word("", "empty");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void convertToTextFile(ArrayList<Word> arr) {
		// TODO Auto-generated method stub
		try (FileWriter writer = new FileWriter(arr.get(0).getData().toString())) {
			for (Word word : arr) {
				if (!Objects.isNull(word))
					writer.write(word.getA() + " " + word.getData()
							+ System.lineSeparator());
			}
			System.out
					.println("Word objects written to the file successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> systemCallReader(String filePath, int base) {
		ArrayList<String> out = new ArrayList<String>();
		for (int i = base + 5; i <= base + 7; i++) {
			if (memory.getMemory()[i].getA().equals(filePath))
				filePath = memory.getMemory()[i].getData();
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(
				"programs/" + filePath + ".txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// Process each line of the file
				out.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	public String systemCallReader1(String filePath,int base) {
		String out = "";
		for (int i = base + 5; i <= base + 7; i++) {
			if (memory.getMemory()[i].getA().equals(filePath))
				filePath = memory.getMemory()[i].getData();
		}
		try (BufferedReader reader = new BufferedReader(new FileReader("programs/" + filePath + ".txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// Process each line of the file
				out = line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	public void systemCallWriter(String myInst, int base) {
		String[] s = myInst.split(" ");
		String filePath = s[0];
		String content = s[1];
		for (int i = base + 5; i <= base + 7; i++) {
			if (memory.getMemory()[i].getA().equals(s[0]))
				filePath = memory.getMemory()[i].getData();
			if (memory.getMemory()[i].getA().equals(s[1]))
				content = memory.getMemory()[i].getData();
		}
		File file = new File("programs/" + filePath + ".txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(
				"programs/" + filePath + ".txt"))) {
			file.createNewFile();
			writer.write(content);
			System.out.println("File written to the disk successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkSchedule() {
		for (int i = 0; i < schedule.length; i++) {
			String[] s = schedule[i].split(" ");
			if (clock == Integer.parseInt(s[0])) {
				PCB pcb = new PCB(s[1]);
				if(!this.readyQueue.contains(pcb)){
					   this.readyQueue.add(pcb);
					   addPCB(pcb);
					}
			}
		}
	}

	private void checkSchedule(int clk) {
		for (int i = 0; i < schedule.length; i++) {
			String[] s = schedule[i].split(" ");
			if (clk == Integer.parseInt(s[0])) {
				PCB pcb = new PCB(s[1]);
				if(!this.readyQueue.contains(pcb)){
				   this.readyQueue.add(pcb);
				   addPCB(pcb);
				}
				}
		}
	}

	public void Scheduler() throws InvalidAccess {

		while (this.finished < this.noOfPcb) {
			System.out.println("Clock cycle-> "+clock);
			checkSchedule();
			if (!this.readyQueue.isEmpty()) {
				// System.out.println("Ready Queue ->" + readyQueue.toString());
				PCB currProcess = this.readyQueue.poll();
				System.out.println("Process to run ->" + currProcess.getName());
				if (!Objects.isNull(memory.getMemory()[0])
						&& !Objects.isNull(memory.getMemory()[20])
						&& !currProcess.getName().equals(
								memory.getMemory()[0].getData().toString())
						&& !currProcess.getName().equals(
								memory.getMemory()[20].getData().toString())) {
					System.out.println("Process swapped  " + currProcess.getName());

					swap(currProcess);
				}
				if (currProcess.getName().equals(
						memory.getMemory()[0].getData().toString())) {
					int ExecTime = clock + timeslice;
					while (clock < ExecTime) {
						int counter = currProcess.getCounter();
						int base = Integer.parseInt(memory.getMemory()[3]
								.getData());
						int limit = Integer.parseInt(memory.getMemory()[4]
								.getData());
						memory.getMemory()[1].setData(PCBState.RUNNING + "");
						currProcess.setState(PCBState.RUNNING);
						System.out.println("Memory ->" + memory.toString());
						//System.out.println("Counter ->" + counter);
						System.out.println("Instruction ->"
								+ memory.getMemory()[counter].toString());
						Word extraProc = excute(memory.getMemory()[counter],
								currProcess, base, limit);
						if (!extraProc.equals(memory.getMemory()[counter])) {
							memory.getMemory()[counter].setA(extraProc.getA());
							memory.getMemory()[counter].setData(extraProc.getData());
						} else if (currProcess.getCounter() > limit|| memory.getMemory()[counter].getData()
										.equals("empty")) {
							memory.getMemory()[1].setData(PCBState.FINISHED+ ""); // zyada
							currProcess.setState(PCBState.FINISHED);
							FinQueue.add(currProcess);
							this.finished++;
							// removeFromMemory(currProcess);
							break;
							// unloaded from memory when swapped
						} else if (currProcess.getState().equals(PCBState.BLOCKED) && !BlockedQueue.contains(currProcess)) {
							BlockedQueue.add(currProcess);
							clock++;
							break;
						}
						clock++;
					}
					for(int i=1;i<timeslice;i++)
					    checkSchedule(clock - i);
					if (currProcess.getState().equals(PCBState.RUNNING)) {
						memory.getMemory()[1].setData(PCBState.READY + ""); // zyada
						currProcess.setState(PCBState.READY);
						this.readyQueue.add(currProcess);
					}
					// checkSchedule();
				} else if (currProcess.getName().equals(memory.getMemory()[20].getData().toString())) {
					int ExecTime = clock + timeslice;
					while (clock < ExecTime) {
						int counter = currProcess.getCounter();
						int base = Integer.parseInt(memory.getMemory()[23].getData());
						int limit = Integer.parseInt(memory.getMemory()[24].getData());
						memory.getMemory()[21].setData(PCBState.RUNNING + "");
						currProcess.setState(PCBState.RUNNING);
						System.out.println("Memory ->" + memory.toString());
						//System.out.println("Counter ->" + counter);
						System.out.println("Instruction ->"+ memory.getMemory()[counter].toString());
						Word extraProc = excute(memory.getMemory()[counter],currProcess, base, limit);
						if (!extraProc.equals(memory.getMemory()[counter])) {
							memory.getMemory()[counter].setA(extraProc.getA());
							memory.getMemory()[counter].setData(extraProc.getData());
						} else if (currProcess.getCounter() > limit
								|| memory.getMemory()[currProcess.getCounter()].getData().equals("empty")) {
							memory.getMemory()[21].setData(PCBState.FINISHED+ ""); // zyada
							currProcess.setState(PCBState.FINISHED);
							FinQueue.add(currProcess);
							//System.out.println("finished");
							this.finished++;
							// removeFromMemory(currProcess);
							break;
							// unloaded from memory when swapped
						} else if (currProcess.getState().equals(PCBState.BLOCKED) && !BlockedQueue.contains(currProcess)) {
							BlockedQueue.add(currProcess);
							clock++;
							break;
						}
						clock++;
					}
					//System.out.println("Ready Queue->> "+ this.readyQueue.toString());
					for(int i=1;i<timeslice;i++)
					    checkSchedule(clock - i);
					if (currProcess.getState().equals(PCBState.RUNNING)) {
						memory.getMemory()[21].setData(PCBState.READY + ""); // zyada
						currProcess.setState(PCBState.READY);
						this.readyQueue.add(currProcess);
						
					}
				}
			} else
				clock++;
			System.out.print("Ready Queue->> ");
			Queue<PCB> temp1 = new LinkedList<>();
			while (!readyQueue.isEmpty()) {
				PCB tempProc = readyQueue.poll();
				System.out.print(tempProc.getName() + " ");
				temp1.add(tempProc);
			}
			while (!temp1.isEmpty()) {
				readyQueue.add(temp1.poll());
			}
			System.out.println();
			System.out.print("Finished Queue->> ");
			Queue<PCB> temp2 = new LinkedList<>();
			while (!FinQueue.isEmpty()) {
				PCB tempProc = FinQueue.poll();
				System.out.print(tempProc.getName() + " ");
				temp2.add(tempProc);
			}
			while (!temp2.isEmpty()) {
				FinQueue.add(temp2.poll());
			}
			System.out.println();
			System.out.print("Blocked Queue->> ");
			Queue<PCB> temp3 = new LinkedList<>();
			while (!BlockedQueue.isEmpty()) {
				PCB tempProc = BlockedQueue.poll();
				System.out.print(tempProc.getName() + " ");
				temp3.add(tempProc);
			}
			while (!temp3.isEmpty()) {
				BlockedQueue.add(temp3.poll());
			}
			System.out.println();
		}
	}

	private void removeFromMemory(PCB currProcess) {
		for (int i = currProcess.getBaseAddress(); i < currProcess.getMemoryLimit(); i++) {
			memory.getMemory()[i] = null;
		}
		if (!readyQueue.isEmpty()) {
			PCB nextPcb = readyQueue.poll();
			swap(nextPcb);
			System.out.println("Process swapped  " + nextPcb.getName());
			readyQueue.add(nextPcb);
		}
	}

	private Word excute(Word myInstruction, PCB currPCB, int base, int limit)
			throws InvalidAccess {
		// currPCB.setState(PCBState.RUNNING);
		Word moreProc = myInstruction;
		Word currLine = myInstruction;
		String currInstruction = myInstruction.getA();
		switch (currInstruction) {
			case "print":
				String ss="";
				for (int i = base + 5; i <= base + 7; i++) {
					if (memory.getMemory()[i].getA().equals(myInstruction.getData().trim()))
						ss = memory.getMemory()[i].getData();
				}
				System.out.println(ss);
				break;
			case "assign":
				moreProc = assign(myInstruction.getData(), base, limit);
				break;
			case "writeFile":
				systemCallWriter(myInstruction.getData(), base);
				break;
			case "readFile":
				systemCallReader(myInstruction.getData(), base);
				break;
			case "printFromTo":
				//System.out.println("Counting from from");
				String s[] = myInstruction.getData().split(" ");
				int x = 0;
				int y = 0;
				for (int a = base + 5; a <= base + 7; a++) {
					if (memory.getMemory()[a].getA().equals(s[0])) {
						x = Integer.parseInt(memory.getMemory()[a].getData());
					}
					if (memory.getMemory()[a].getA().equals(s[1])) {
						y = Integer.parseInt(memory.getMemory()[a].getData());
					}
				}
				if (x < y) {
					for (int i = x; i <= y; i++)
						System.out.println(i);
				} else {
					for (int i = y; i <= x; i++)
						System.out.println(i);
				}
				break;
			case "semWait":
				boolean run = mutex.semWait(currPCB, myInstruction.getData());
				if (!run) {
					memory.getMemory()[base + 1].setData(PCBState.BLOCKED + "");
				}
				break;
			case "semSignal":
				try {
					PCB pcb = mutex.semSignal(currPCB, myInstruction.getData());
					readyQueue.add(pcb);
					BlockedQueue.remove(pcb);
					if (memory.getMemory()[0].getData().equals(pcb.getName()))
						memory.getMemory()[1].setData(PCBState.READY + "");
					else if (memory.getMemory()[20].getData().equals(pcb.getName()))
						memory.getMemory()[21].setData(PCBState.READY + "");
				} catch (NullPointerException e) {
					//System.out.println("no PCBs waiting for semSignal");
				}
				break;
		}

		//System.out.println("Counter in exec 1  " + currPCB.getCounter());
		if (moreProc.getA().equals(myInstruction.getA())
				&& moreProc.getData().equals(myInstruction.getData())
				&& !currPCB.getState().equals(PCBState.BLOCKED)) {
			memory.getMemory()[base + 2]
					.setData(""
							+ (Integer.parseInt(memory.getMemory()[base + 2]
									.getData()) + 1));
			currPCB.setCounter(currPCB.getCounter() + 1);
		}
		//System.out.println("Counter in exec 2  " + currPCB.getCounter());

		return moreProc;
	}

	private Word assign(String currInstruction, int baseAddress, int limit)
			throws InvalidAccess {
		// int baseAddress = currPCB.getBaseAddress();
		Word b = new Word("assign", currInstruction);
		String[] splited = currInstruction.split(" ");
		// if (i > limit)
		// throw new InvalidAccess("Invalid Access");
		String data = "";
		switch (splited[1]) {
			case "input":
				Scanner sc = new Scanner(System.in);
				System.out.println("Please enter a value");
				data = sc.nextLine();
				b.setData(splited[0] + " " + data);
				return b;
			case "readFile":
				String arr = systemCallReader1(splited[2],baseAddress);
				b.setData(splited[0] + " " + arr);
				return b;
			default:
				for (int i = 1; i < splited.length; i++) {
					data += splited[i]+" ";
				}
				data = data.trim();
		}

		int i;
		boolean alreadyCreated = false;
		for (i = baseAddress + 5; i <= baseAddress + 7; i++) {
			if (memory.getMemory()[i].getA().equals(splited[0])) {
				alreadyCreated = true;
				break;
			}
		}
		if (alreadyCreated)
			memory.getMemory()[i].setData(data);
		else {
			if (memory.getMemory()[baseAddress + 5].getA().equals("Variable1")) {
				memory.getMemory()[baseAddress + 5].setA(splited[0]);
				memory.getMemory()[baseAddress + 5].setData(data);
			} else if (memory.getMemory()[baseAddress + 6].getA().equals(
					"Variable2")) {
				memory.getMemory()[baseAddress + 6].setA(splited[0]);
				memory.getMemory()[baseAddress + 6].setData(data);
			} else if (memory.getMemory()[baseAddress + 7].getA().equals(
					"Variable3")) {
				memory.getMemory()[baseAddress + 7].setA(splited[0]);
				memory.getMemory()[baseAddress + 7].setData(data);
			} else {
				System.out
						.println("there are enough variables in use and can't create more!!");
			}
		}
		return b;
	}
}