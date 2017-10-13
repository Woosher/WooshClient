package controller;

import entities.ConnectionInfo;
import entities.parsing.Machine;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopupDeployController {
    EventHandler eventHandler;

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @FXML
    private Button buttonAdd;

    @FXML
    private ListView listview;

    @FXML
    private ProgressIndicator spinner;


    public void resetInfo(){
        listview.setItems(null);
    }

    public void setListViewDisabled(boolean value){
        listview.setDisable(value);
    }

    public void addInfo(ObservableList<ConnectionInfo> connectionInfoList){
        buttonAdd.setOnMouseClicked(eventHandler);
        listview.setItems(connectionInfoList);
        listview.setCellFactory(new Callback<ListView<ConnectionInfo>, ListCell<ConnectionInfo>>() {
            @Override
            public ListCell<ConnectionInfo> call(ListView<ConnectionInfo> param) {
                return new DeployListViewCell();
            }
        });
    }



    public void setSpinnerVisibility(boolean visibility){
        spinner.setVisible(visibility);
    }

}
