package controller.subcontrollers.listViews;

import controller.subcontrollers.PopupController;
import entities.ConnectionInfo;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

import static values.Constants.TRUSTED;

public class InfoListViewCell extends ListCell<ConnectionInfo> {

    PopupController.Adder adder;

    public InfoListViewCell(PopupController.Adder adder){
        this.adder = adder;
    }

    @FXML
    private CheckBox checkBox;

    @FXML
    private Label label1, label2;

    private FXMLLoader mLLoader;

    @FXML
    private GridPane gridPane;

    @Override
    protected void updateItem(ConnectionInfo item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getClassLoader().getResource("listcellinfo.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            label1.setText(item.getMachine().getName());
            label2.setText(item.getInfo());

            if(item.getInfo().equals(TRUSTED)){
                checkBox.indeterminateProperty().setValue(false);
                checkBox.setSelected(true);
                checkBox.setDisable(true);
            }

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
