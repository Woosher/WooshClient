package controller.subcontrollers.listViews;

import controller.subcontrollers.PopupController;
import controller.subcontrollers.PopupDeployPartController;
import entities.ConnectionInfo;
import entities.parsing.Machine;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class DeployPartListViewCell extends ListCell<Machine> {

    PopupDeployPartController.Adder adder;

    public DeployPartListViewCell(PopupDeployPartController.Adder adder) {
        this.adder = adder;
    }

    @FXML
    private CheckBox checkBox;

    @FXML
    private Label label1;

    private FXMLLoader mLLoader;

    @FXML
    private GridPane gridPane;

    @Override
    protected void updateItem(Machine item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getClassLoader().getResource("deploypartcellinfo.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            label1.setText(item.getName());


            checkBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    adder.add(item);
                }
            });
            setText(null);
            setGraphic(gridPane);
        }
    }
}