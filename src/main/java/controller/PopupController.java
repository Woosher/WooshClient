package controller;

import entities.ConnectionInfo;
import entities.parsing.Machine;
import entities.parsing.Node;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import tools.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopupController {
    @FXML
    private Button buttontest;

    @FXML
    private ListView listview;

    private Map<Machine, Boolean> machineMap = new HashMap<>();

    public void addInfo(ObservableList<ConnectionInfo> connectionInfoList){
        machineMap.clear();
        for(ConnectionInfo c : connectionInfoList){
            machineMap.put(c.getMachine(), false);
        }
        listview.setItems(connectionInfoList);
        listview.setCellFactory(new Callback<ListView<ConnectionInfo>, ListCell<ConnectionInfo>>() {
            @Override
            public ListCell<ConnectionInfo> call(ListView<ConnectionInfo> param) {
                    return new InfoListViewCell(new EventHandler() {
                        @Override
                        public void handle(Event event) {
                        }
                    });
            }

        });
    }



}