package controller;

import entities.ConnectionInfo;
import entities.parsing.Node;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.util.List;

public class PopupController {
    @FXML
    private Button buttontest;

    @FXML
    private ListView listview;

    private boolean mealChosen;

    @FXML
    private void submit() {
        mealChosen = true;
    }

    public String getMeal() {
        return  "Tis";
    }

    public void addInfo(ObservableList<ConnectionInfo> connectionInfoList){
        if(listview == null){
            System.out.println("NULLLLL");
        }
        listview.setItems(connectionInfoList);
        listview.setCellFactory(new Callback<ListView<ConnectionInfo>, ListCell<ConnectionInfo>>() {
            @Override
            public ListCell<ConnectionInfo> call(ListView<ConnectionInfo> param) {
                    return new InfoListViewCell();
//                ListCell<ConnectionInfo> cell = new ListCell<ConnectionInfo>() {
//
//                    @Override
//                    protected void updateItem(ConnectionInfo item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (item != null) {
//                            setText(item.getMachine().getName() + " " + item.getInfo());
//                        }
//                    }
//                };
//                return cell;
            }

        });
    }



}