package factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class IOFactory {

	public BufferedReader getBufferedReader(Socket socket) throws IOException {
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public PrintWriter getPrintWriter(Socket socket) throws IOException {
		return new PrintWriter(socket.getOutputStream(),true);
	}
	
}
