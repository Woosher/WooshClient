package controller;

import entities.ResultsListener;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modellers.interfaces.FlowModelInterface;

import java.io.File;


public class ViewController {

    private FlowModelInterface model;

    @FXML
    Button deployButton, loadButton, saveButton, testConnectionsButton;
    @FXML
    TextField pathField, savePathField;


    public void initModel(FlowModelInterface model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model ;
        initLayout();
    }

    private void initLayout(){
        deployButton.setOnMouseClicked(event -> handleDeploy());
        loadButton.setOnMouseClicked(event -> handleLoad());
        saveButton.setOnMouseClicked(event -> handleSave());
    }

    public void testConnections(){
        model.sendPackages(new ResultsListener<String>() {
            @Override
            public void onCompletion(String result) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        deployButton.setText(result);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                print(throwable.getMessage());

            }
        });
    }

    public void handleDeploy(){
        model.sendPackages(new ResultsListener<String>() {
            @Override
            public void onCompletion(String result) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        deployButton.setText(result);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                print(throwable.getMessage());

            }
        });
    }

    public void handleLoad(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        String path = file.getAbsolutePath();
        if(path != null){
            pathField.setText(path);
            model.loadDeployment(pathField.getText(), new ResultsListener<Deployment>() {
                @Override
                public void onCompletion(Deployment result) {

                }

                @Override
                public void onFailure(Throwable throwable) {
                    print(throwable.getMessage());
                }
            });
        }
    }

    public void handleSave(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save File");
        File file = chooser.showSaveDialog(new Stage());
        String path = file.getAbsolutePath();
        if (path != null) {
            savePathField.setText(path);
            model.saveDeployment(savePathField.getText(), new ResultsListener<Void>() {
                @Override
                public void onCompletion(Void result) {
                }

                @Override
                public void onFailure(Throwable throwable) {
                    print(throwable.getMessage());
                }
            });
        }
    }

    public void printDeployMent(Deployment deployment){
//        print("NAME OF DEPLOYMENT: " + deployment.getName());
//        print("SSL PATH: " + deployment.getSsl_path());
//        print("LOADBALANCERS: ");
//        for(LoadBalancer loadBalancer : deployment.getLoadBalancers()){
//            print("");
//            print("\tNAME OF LB: " + loadBalancer.getName());
//            print("\tIP OF LB: " + loadBalancer.getPort());
//            print("\tCATCHE OF LB: " + loadBalancer.getCachingAttributes());
//            print("\tNODES");
//            for(Node node : loadBalancer.getNodes()){
//                print("");
//                print("\t\tNAME OF NODE: " + node.getName());
//                print("\t\tIP OF NODE: " + node.getIp());
//                print("\t\tPORT OF NODE: " + node.getPort());
//                print("\t\tSE OF NODE: " + node.getEnvironment());
//                print("\t\tOS OF NODE: " + node.getOperatingSystem());
//            }
//        }
    }

    private void print(String args){
        System.out.println(args);
    }

}
