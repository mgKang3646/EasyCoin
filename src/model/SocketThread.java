package model;

import java.io.IOException;

public abstract class SocketThread extends Thread {

		public abstract void initializeObjects() throws IOException;
		@Override
		public abstract void run();
		public abstract void send(String msg) throws IOException;
		
}

