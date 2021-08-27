package util;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PemFileChooser {
	
	private FileChooser fileChooser;
	
	public PemFileChooser() {
		fileChooser = new FileChooser();
	}
	
	public File getFileFromFileChooeser(Stage stage, String title) {
		fileChooser.setTitle(title);
		fileChooser.setInitialDirectory(new File("./pem"));
		return fileChooser.showOpenDialog(stage);
	}
	
	
}
