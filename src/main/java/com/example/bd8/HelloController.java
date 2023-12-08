package com.example.bd8;

import com.example.bd8.Models.PlayerViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    // Establish a connection



    @FXML
    public TextField playerIdText;
    @FXML
    public Button getButton;
    @FXML
    private TableColumn<PlayerViewModel, String> colFirstName;
    @FXML
    private TableColumn<PlayerViewModel, String> colLastName;
    @FXML
    private TableColumn<PlayerViewModel, Integer> colAge;
    @FXML
    private TableColumn<PlayerViewModel, Integer> colId;
    @FXML
    private TableColumn<PlayerViewModel, String> colEmail;
    @FXML
    private TableView<PlayerViewModel> table;
    @FXML
    public Label playerErrorLabel;


    @FXML
    public TextField playerIdTextInput;
    @FXML
    public TextField playerFirstNameTextInput;
    @FXML
    public TextField playerLastNameTextInput;
    @FXML
    public TextField playerAgeTextInput;
    @FXML
    public TextField playerEmailTextInput;
    @FXML
    public Button postButton;
    @FXML
    private TableColumn<PlayerViewModel, String> colFirstName1;
    @FXML
    private TableColumn<PlayerViewModel, String> colLastName1;
    @FXML
    private TableColumn<PlayerViewModel, Integer> colAge1;
    @FXML
    private TableColumn<PlayerViewModel, Integer> colId1;
    @FXML
    private TableColumn<PlayerViewModel, String> colEmail1;
    @FXML
    private TableView<PlayerViewModel> table1;
    @FXML
    public Label playerErrorLabel1;


    @FXML
    public Label sessionProcedureLabel;
    @FXML
    public TextField sessionParticipantsIdTextInput;
    @FXML
    public TextField sessionWinnerIdTextInput;
    @FXML
    public TextField sessionRoomTakenIdTextInput;
    @FXML
    public TextField sessionGameSetTakenTextInput;
    @FXML
    public TextField sessionBeginningTimeStampTextInput;
    @FXML
    public TextField sessionDurationTextInput;
    @FXML
    public Button executeProcedure;
    @FXML
    public Label sessionErrorLabel;



    @FXML
    protected void onGetButtonClicked() {
//        ObservableList<PlayerViewModel> list = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * FROM player WHERE player_id = " + playerIdText.getText();
        PlayerViewModel pl = new PlayerViewModel(this, sqlQuery);

        table.getItems().add(pl);

//        table.setItems(list);
    }

    @FXML
    protected void onPostButtonClicked() {
//        ObservableList<PlayerViewModel> list = FXCollections.observableArrayList();
        String sqlQuery = "INSERT INTO player (first_name, last_name, age, email) VALUES (?,?,?,?)";

        PlayerViewModel pl = new PlayerViewModel(this);
        try{

            pl.insertIntoTable(
                    sqlQuery,
                    playerFirstNameTextInput.getText(),
                    playerLastNameTextInput.getText(),
                    Integer.valueOf(playerAgeTextInput.getText()),
                    playerEmailTextInput.getText()
            );
        } catch (IllegalArgumentException e){
            handleIllegalArguments(e);
        }
        table1.getItems().add(pl);

//        table.setItems(list);
    }

    @FXML
    protected void onExecuteProcedureButtonClicked() {
//        ObservableList<PlayerViewModel> list = FXCollections.observableArrayList();
        // Define the SQL call for the stored procedure
        String call = "CALL insert_session(?, ?, ?, ?, ?, ?)";

        String[] parts = sessionParticipantsIdTextInput.getText().split(",");
        Integer[] participants = new Integer[parts.length];
        for (int i = 0; i < parts.length; i++) {
            participants[i] = Integer.parseInt(parts[i]);
        }
        PlayerViewModel pl = new PlayerViewModel(this);
        try{
        pl.executeProcedureInsertSession(
                call,
                participants,
                Integer.valueOf(sessionWinnerIdTextInput.getText()),
                Integer.valueOf(sessionRoomTakenIdTextInput.getText()),
                Integer.valueOf(sessionGameSetTakenTextInput.getText()),
                sessionBeginningTimeStampTextInput.getText(),
                sessionDurationTextInput.getText()
        );
        } catch (IllegalArgumentException e){
            handleIllegalArguments(e);
        }

        sessionProcedureLabel.setText("Success");

//        table.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set up columns
        colFirstName.setCellValueFactory(new PropertyValueFactory<PlayerViewModel, String>("first_name"));
        colLastName.setCellValueFactory(new PropertyValueFactory<PlayerViewModel, String>("last_name"));
        colAge.setCellValueFactory(new PropertyValueFactory<PlayerViewModel, Integer>("age"));
        colEmail.setCellValueFactory(new PropertyValueFactory<PlayerViewModel, String>("email"));
        colId.setCellValueFactory(new PropertyValueFactory<PlayerViewModel, Integer>("id"));

        colFirstName1.setCellValueFactory(new PropertyValueFactory<PlayerViewModel, String>("first_name"));
        colLastName1.setCellValueFactory(new PropertyValueFactory<PlayerViewModel, String>("last_name"));
        colAge1.setCellValueFactory(new PropertyValueFactory<PlayerViewModel, Integer>("age"));
        colEmail1.setCellValueFactory(new PropertyValueFactory<PlayerViewModel, String>("email"));
        colId1.setCellValueFactory(new PropertyValueFactory<PlayerViewModel, Integer>("id"));


    }

    private void handleIllegalArguments(IllegalArgumentException e) {
        System.err.println("SQLException:");
        StringBuilder str = new StringBuilder();

        str.append("SQL State  : " + e.getMessage() + "\n");
        str.append("Error Code : " + e.getCause() + "\n");
        str.append("Message    : " + e.toString() + "\n");

        sessionErrorLabel.setText(str.toString());
        playerErrorLabel.setText(str.toString());
        playerErrorLabel1.setText(str.toString());
    }


}