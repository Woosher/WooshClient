package controller.subcontrollers;

import controller.subcontrollers.listViews.DeployListViewCell;
import controller.subcontrollers.listViews.DeployPartListViewCell;
import controller.subcontrollers.listViews.InfoListViewCell;
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

public class PopupDeployPartController {

    EventHandler eventHandler;

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @FXML
    private ProgressIndicator spinner;

    @FXML
    private Button buttonDeploy;

    @FXML
    private ListView listview;

    private Map<Machine, Boolean> machineMap = new HashMap<>();

    public void resetInfo(){
        listview.setItems(null);
    }

    public void setListViewDisabled(boolean value){
        listview.setDisable(value);
    }

    public void addInfo(ObservableList<Machine> machineObservableList){
        buttonDeploy.setOnMouseClicked(eventHandler);

        machineMap.clear();
        for(Machine m : machineObservableList){
            machineMap.put(m, false);
        }
        listview.setItems(machineObservableList);
        listview.setCellFactory(new Callback<ListView<Machine>, ListCell<Machine>>() {
            @Override
            public ListCell<Machine> call(ListView<Machine> param) {
                return new DeployPartListViewCell(new PopupDeployPartController.Adder() {
                    @Override
                    public void add(Machine machine) {
                        boolean prevValue = machineMap.get(machine);
                        machineMap.put(machine,!prevValue);
                    }
                });
            }
        });
    }

    public void setSpinnerVisibility(boolean visibility){
        spinner.setVisible(visibility);
    }

    public List<Machine> getSelectedMachines(){
        List<Machine> machines = new ArrayList<>();
        for(Machine m : machineMap.keySet()){
            boolean value = machineMap.get(m);
            if(value) machines.add(m);
        }
        return machines;
    }



    public interface Adder{
        void add(Machine machine);
    }

}
