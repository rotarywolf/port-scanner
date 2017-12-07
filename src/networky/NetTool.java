package networky;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
 
public class NetTool extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryWindow) {
		primaryWindow.setTitle("lithien's network tool");
		primaryWindow.setResizable(false);
		
		/* TAB: PORT SCANNER */
		GridPane portScanGrid = new GridPane();
		portScanGrid.setAlignment(Pos.CENTER);
		portScanGrid.setHgap(10);
		portScanGrid.setVgap(4);
		portScanGrid.setPadding( new Insets(10, 10, 10, 10) );
		
		// IP label and field
		Label scanIpLabel = new Label("IP/host to scan:");
		portScanGrid.add(scanIpLabel, 0, 0);
		TextField scanIpField = new TextField();
		scanIpField.setAlignment(Pos.CENTER);
		scanIpField.setText("www.google.com.au");
		portScanGrid.add(scanIpField, 1, 0);
		
		// radio buttons and labels
		ToggleGroup scanType = new ToggleGroup();
		RadioButton optRange = new RadioButton("port range");
		optRange.setToggleGroup(scanType);
		portScanGrid.add(optRange, 0, 1);
		RadioButton optSingle = new RadioButton("single port");
		optSingle.setToggleGroup(scanType);
		optSingle.setSelected(true);
		portScanGrid.add(optSingle, 0, 2);
		
		// port range text fields and label
		GridPane portRangeGrid = new GridPane();
		portRangeGrid.setAlignment(Pos.CENTER);
		portRangeGrid.setHgap(5);
		portRangeGrid.setVgap(0);
		
		TextField startPort = new TextField();
		startPort.setPrefWidth(50);
		startPort.setAlignment(Pos.CENTER);
		portRangeGrid.add(startPort, 0, 0);
		Label rangeLabel = new Label("to");
		portRangeGrid.add(rangeLabel, 1, 0);
		TextField endPort = new TextField();
		endPort.setPrefWidth(50);
		endPort.setAlignment(Pos.CENTER);
		portRangeGrid.add(endPort, 2, 0);
		portScanGrid.add(portRangeGrid, 1, 1);
		
		// single port text field
		TextField singlePort = new TextField();
		singlePort.setPrefWidth(50);
		singlePort.setAlignment(Pos.CENTER);
		singlePort.setText("80");
		HBox singlePortBox = new HBox(singlePort);
		singlePortBox.setAlignment(Pos.CENTER);
		portScanGrid.add(singlePortBox, 1, 2);
		
		// timeout label and text field
		GridPane timeoutGrid = new GridPane();
		timeoutGrid.setAlignment(Pos.CENTER);
		timeoutGrid.setHgap(5);
		timeoutGrid.setVgap(0);
		
		Label timeoutLabel = new Label("timeout:");
		timeoutGrid.add(timeoutLabel, 0, 0);
		TextField timeout = new TextField();
		timeout.setPrefWidth(50);
		timeout.setAlignment(Pos.CENTER);
		timeout.setText("220");
		timeoutGrid.add(timeout, 1, 0);
		Label msLabel = new Label("ms");
		timeoutGrid.add(msLabel, 2, 0);
		portScanGrid.add(timeoutGrid, 0, 3, 2, 1);
		
		// port lister
		ObservableList<String> portList = FXCollections.observableArrayList(); // what even is this?
		ListView<String> portListView = new ListView<String>(portList);
		portListView.setPrefHeight(200);
		portListView.setPrefWidth(80);
		portScanGrid.add(portListView, 2, 0, 1, 6);
		
		// go button
		Button scanBtn = new Button("gimme"); // TODO: add event handler to change button if scanning in progress
		HBox scanBtnBox = new HBox(scanBtn); // a HBox wraps around an element,
		scanBtnBox.setAlignment(Pos.CENTER); // so we can align the element.
		portScanGrid.add(scanBtnBox, 0, 4, 2, 1);
		
		// result label
		Label scanResult = new Label("");
		HBox scanResultBox = new HBox(scanResult);
		scanResultBox.setAlignment(Pos.CENTER);
		portScanGrid.add(scanResultBox, 0, 5, 2, 1);
		
		// go button Event Handler
		scanBtn.setOnAction( new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				scanBtn.setDisable(true); // we could disable the button in the PortScanner's go() method, but it's probably safest to disable it as soon as possible after it's clicked.
				portList.clear();
				PortScanner portScan = new PortScanner(scanIpField.getText(), portList, scanBtn, scanResult);
				
				if (optRange.isSelected()) {
					portScan.go(Integer.parseInt(startPort.getText()), Integer.parseInt(endPort.getText()), Integer.parseInt(timeout.getText()) );
				}
				else {
					portScan.go(Integer.parseInt(singlePort.getText()), Integer.parseInt(timeout.getText()) );
				}
			}
		});
		
		Tab portScanner = new Tab("port scanner", portScanGrid);
		portScanner.setClosable(false);
		
		/* TAB: TCP CONNECTION THING */
		Tab tcpTester = new Tab("TCP tester");
		tcpTester.setClosable(false);
		tcpTester.setDisable(true);
		
		/* FINAL STUFF */
		TabPane tabs = new TabPane(portScanner, tcpTester);
		Scene primaryScene = new Scene(tabs);
	
		primaryWindow.setScene(primaryScene);
		primaryWindow.show();

	}
}