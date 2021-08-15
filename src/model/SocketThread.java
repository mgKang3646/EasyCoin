package model;

import java.io.IOException;

public abstract class SocketThread extends Thread {
	@Override
	public abstract void run();
	public abstract void initializeObjects() throws IOException;
	public abstract void send(String msg);
		
}

