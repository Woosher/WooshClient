package controller;

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

public class InfoListViewCell extends ListCell<ConnectionInfo> {

    EventHandler eventHandler;

    public InfoListViewCell(EventHandler eventHandler){
        this.eventHandler = eventHandler;
    }

    @FXML
    private CheckBox checkBox;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

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
            checkBox.setOnMouseClicked(eventHandler);
            setText(null);
            setGraphic(gridPane);

        }
    }
}
