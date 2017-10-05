package controller;

import java.io.IOException;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class NodeComponent extends Pane {

    private Node view;
    private NodeComponentController controller;

    public NodeComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("node.fxml"));
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                controller = new NodeComponentController();
                return controller;
            }
        });
        try {
            view = fxmlLoader.load();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        getChildren().add(view);
    }

    public void setWelcome(String str) {
        controller.getTextInField().set(str);
    }

    public String getWelcome() {
        return controller.getTextInField().get();
    }

    public StringProperty welcomeProperty() {
        return controller.getTextInField();
    }
}