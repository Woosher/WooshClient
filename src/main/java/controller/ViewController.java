package controller;

import com.sun.jndi.ldap.Connection;
import entities.ConnectionInfo;
import entities.ResultsListener;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modellers.interfaces.FlowModelInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ViewController {

    private FlowModelInterface model;
    private Scene dialogScene;
    private Stage connectionStage;

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
        testConnectionsButton.setOnMouseClicked(event -> testConnections());
    }

    public void testConnections(){
        model.testConnections(new ResultsListener<List<ConnectionInfo>>() {
            @Override
            public void onCompletion(List<ConnectionInfo> result) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        connectionStage = new Stage();
                        connectionStage.initModality(Modality.APPLICATION_MODAL);
                        connectionStage.initOwner(new Stage());
                        VBox dialogVbox = new VBox(20);
                        for (ConnectionInfo s : result) {
                            HBox hb = new HBox();
                            ObservableList hbChildren = hb.getChildren();
                            hbChildren.add(new Text("IP: " + s.getMachine().getIp() + " Fingerprint: " + s.getInfo()));
                            hbChildren.add(new CheckBox());
                            dialogVbox.getChildren().add(hb);
                        }
                        Button ok = new Button("OK");
                        ok.setOnMouseClicked(event -> addKnownHosts(result));
                        dialogVbox.getChildren().add(ok);
                        dialogScene = new Scene(dialogVbox, 600, 200);
                        connectionStage.setScene(dialogScene);
                        connectionStage.show();
                    }

                    private void addKnownHosts(List<ConnectionInfo> result) {
                        ObservableList list = dialogScene.getRoot().getChildrenUnmodifiable();
                        List<Machine> hosts = new ArrayList<>();
                        int i = 0;
                        for(Object box : list){
                            if(box instanceof HBox){
                                HBox vBox = (HBox) box;
                                CheckBox cb = (CheckBox)vBox.getChildren().get(1);
                                if(cb.isSelected()){
                                    hosts.add(result.get(i).getMachine());

                                }
                            }
                        i++;
                        }
                        model.addKnownHosts(hosts, new ResultsListener<Boolean>() {
                            @Override
                            public void onCompletion(Boolean result) {
                                print(result.toString());
                                //connectionStage.close();
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                print("meh");
                                print(throwable.getMessage());
                            }
                        });
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
