package model;

public class ThreadTime extends Thread {

	private boolean running;

	public void kill() {
		running = false;
	}

	@Override
	public void run() {
		
		running = true;
		
		while (running) {
			
			try {sleep(1000);
            } catch (InterruptedException e) {e.printStackTrace();}
			
			System.out.print('.');
		}
	}
}