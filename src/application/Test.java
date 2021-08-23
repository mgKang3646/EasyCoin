package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class Test {

	public void doTest() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("parent1 : " + loader.getRoot().toString());
		System.out.println("parent2 : " + loader.getRoot().toString());
		System.out.println("loader1 : " + loader.getController().toString());
		System.out.println("loader2 : " + loader.getController().toString());
	}
}
