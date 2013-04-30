package channel;

import java.util.*;

/*********************Select*****************************/
// implements choice
class Select {
	/*
	 * A select object has a list of Selectable objects, and can select 
	 * one of these when all are ready to make a choice. After the choice
	 * has been made, all the Selectable objects must become available
	 * for a new choice by updating their internal and external guards.
	 */
	private ArrayList<Selectable> list = new ArrayList<Selectable>(2);

	Process pros;//enten Reciever eller Sender; 
	//de er subklasser av Process for at vi ikke skal lagre 
	//unødvendige verdier med nullverdi i denne klassen. 

	public synchronized void add(int i, Selectable s) {
		list.add(i, s);
		s.setSelect(this);
	}

	/*
	 * Allow Selectables to signal that they have become ready
	 */
	public synchronized void signal(){ 
		notifyAll();
	}

	/**
	 * Her returnerer vel ready bare true/false av siste forekomst i listen?..
	 * @return
	 */
	private synchronized boolean readyAll(){
		boolean ready = true;
		for (Selectable s:list){ 
			ready = (ready && s.testReady());

			/**
			 * if (ready == false) {
			 * 		return false...
			 * }
			 * 
			 */
		}
		return ready;
	}

	/*
	 * Test Selectable objects, return one which is enabled
	 */
	private synchronized int testAll() throws InterruptedException  {
		int i = 0;
		for (Selectable s:list){
			System.out.println("testAll " + i + " " + pros.getName());
			if (s.testGuard()) { 
				s.clearReady(); 
				System.out.println("testAll in if " + i + " " + pros.getName());
				return i; 
			}
			i++;
		}
		return -1;
	}

	/*
	 * This method can get suspended if
	 * (a) not all Selectable objects are ready to make a choice
	 * (b) no Selectable object is enabled
	 */
	public synchronized int choose() throws InterruptedException {
		int readyIndex = -1;
		while (readyIndex==-1) {
			if (!readyAll()){ 
				wait(); 
			}  // Not ready to make a choice
			readyIndex=testAll();
			if (readyIndex==-1) { 
				wait(); 
			} // No choice is enabled
		}
		return readyIndex;
	}

	public void setProcess(Process p){
		pros = p;
	}

	public ArrayList<Selectable> getList(){
		return list;
	}
}




