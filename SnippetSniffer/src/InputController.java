import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class InputController extends Application{

	@FXML private TextField fileChooser;
	@FXML private Button browseButton;
	private Stage stage;
	private StringBuilder output;

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Input.fxml"));
		Parent root = (Parent) fxmlLoader.load();
		Scene scene = new Scene(root);
		stage.setTitle("Snippet Sniffer");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	public static void start() {
		launch();
	}

	@FXML private void onClick() throws IOException {
		output = new StringBuilder();
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("src file");
		File f = chooser.showDialog(stage);
		if (f != null) {
			findFiles(f);
		} else System.out.println("No source files found");
		if (output.length() == 0) {
			System.out.print("Files are clear of test blocks");
		}
		System.out.print(output);

	}

	private void evaluateFile(File file) throws IOException {
		FileReader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = null;
		int count = 0;
		while ((line = bufferedReader.readLine()) != null) {
			count++;
			if (line.toUpperCase().contains("TEST START")) {
				output.append(file.getAbsolutePath() + " contains a test block at line " + count + "\n");
			}
		}
		reader.close();
	}

	private void findFiles(File file) throws IOException {

		File[] list = file.listFiles();
		int i = list.length - 1;
		while (i >= 0) {
			if (list[i].isFile()) {
				if (list[i].getName().endsWith(".java")) {
					evaluateFile(list[i]);
				}
			} else {
				findFiles(list[i]);
			}

			i--;
		}
	}



}
