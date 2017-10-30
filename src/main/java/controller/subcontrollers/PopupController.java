package controller.subcontrollers;

import controller.subcontrollers.listViews.InfoListViewCell;
import entities.ConnectionInfo;
import entities.parsing.Machine;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopupController {

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

    private Map<Machine, Boolean> machineMap = new HashMap<>();

    public void resetInfo(){
        listview.setItems(null);
    }

    public void setListViewDisabled(boolean value){
        listview.setDisable(value);
    }

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
                    return new InfoListViewCell(new Adder() {
                        @Override
                        public void add(ConnectionInfo connectionInfo) {
                            boolean prevValue = machineMap.get(connectionInfo.getMachine());
                            machineMap.put(connectionInfo.getMachine(),!prevValue);
                        }
                    });
            }
        });
    }

    public List<Machine> getSelectedMachines(){
        List<Machine> machines = new ArrayList<>();
        for(Machine m : machineMap.keySet()){
            boolean value = machineMap.get(m);
            if(value) machines.add(m);
        }
        return machines;
    }

    public void setSpinnerVisibility(boolean visibility){
        spinner.setVisible(visibility);
    }

    public interface Adder{
        void add(ConnectionInfo connectionInfo);
    }

}