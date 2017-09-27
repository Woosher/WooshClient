package controller;

import entities.ResultsListener;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import modellers.FlowModeller;
import modellers.interfaces.FlowModelInterface;

import java.awt.event.ActionListener;


public class ViewController {

    private FlowModelInterface model;

    @FXML
    Button centerButton, secondButton;


    public void initModel(FlowModelInterface model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model ;
        initLayout();
    }

    private void initLayout(){
        centerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                model.sendPackage(null, new ResultsListener<String>() {
                    @Override
                    public void onCompletion(String result) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                centerButton.setText(result);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
        });

        secondButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });


    }

}
