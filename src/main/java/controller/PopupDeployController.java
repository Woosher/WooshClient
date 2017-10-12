package controller;

import entities.ConnectionInfo;
import entities.parsing.Machine;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import java.util.HashMap;
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

    private Map<Machine, Boolean> machineMap = new HashMap<>();

    public void addInfo(ObservableList<ConnectionInfo> connectionInfoList){
        buttonAdd.setOnMouseClicked(eventHandler);

        machineMap.clear();
        for(ConnectionInfo c : connectionInfoList){
            machineMap.put(c.getMachine(), false);
        }
        listview.setItems(connectionInfoList);
        listview.setCellFactory(new Callback<ListView<ConnectionInfo>, ListCell<ConnectionInfo>>() {
            @Override
            public ListCell<ConnectionInfo> call(ListView<ConnectionInfo> param) {
                return new InfoListViewCell(new PopupController.Adder() {
                    @Override
                    public void add(ConnectionInfo connectionInfo) {
                        boolean prevValue = machineMap.get(connectionInfo.getMachine());
                        machineMap.put(connectionInfo.getMachine(),!prevValue);
                    }
                });
            }

        });
    }


    public interface Adder{
        void add(ConnectionInfo connectionInfo);
    }
}
