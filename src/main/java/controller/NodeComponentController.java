package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class NodeComponentController implements Initializable {

    @FXML private TextField textField;

    public StringProperty getTextInField(){
        return textField.textProperty();
    }

    @FXML
    protected void doSomething() {
        textField.setText("The button was clicked #");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textField.setText("Just click the button!");
    }
}