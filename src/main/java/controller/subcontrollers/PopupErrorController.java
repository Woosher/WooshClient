package controller.subcontrollers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;

public class PopupErrorController {

    EventHandler eventHandler;

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @FXML
    private Button buttonOk;

    @FXML
    private Label labelError;

    public void setMessage(String message){
        buttonOk.setOnMouseClicked(eventHandler);
        labelError.setText(message);
    }

}
