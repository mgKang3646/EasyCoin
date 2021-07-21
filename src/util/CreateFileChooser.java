package util;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CreateFileChooser {
	
	
	public File showFileChooser(String message, Stage stage) {
		
		FileChooser fc = new FileChooser();
		fc.setTitle(message);
		fc.setInitialDirectory(new File("./pem"));
		File file = fc.showOpenDialog(stage);
		
		return file;
	}

}
