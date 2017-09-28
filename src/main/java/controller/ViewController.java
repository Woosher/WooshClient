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
import modellers.interfaces.FlowModelInterface;


public class ViewController {

    private FlowModelInterface model;

    @FXML
    Button deployButton, loadButton;
    @FXML
    TextField pathField;


    public void initModel(FlowModelInterface model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model ;
        initLayout();
    }

    private void initLayout(){
        deployButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                model.sendPackage(null, new ResultsListener<String>() {
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

                    }
                });
            }
        });

        loadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                model.loadDeployment("/home/toby/Desktop/deploymenttest.txt", new ResultsListener<Deployment>() {
                    @Override
                    public void onCompletion(Deployment result) {
                        printDeployMent(result);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        print(throwable.getMessage());
                    }
                });

            }
        });


    }

    public void printDeployMent(Deployment deployment){
        print("NAME OF DEPLOYMENT: " + deployment.getName());
        print("SSL PATH: " + deployment.getSsl_path());
        print("LOADBALANCERS: ");
        for(LoadBalancer loadBalancer : deployment.getLoadBalancers()){
            print("");
            print("\tNAME OF LB: " + loadBalancer.getName());
            print("\tIP OF LB: " + loadBalancer.getPort());
            print("\tCATCHE OF LB: " + loadBalancer.getCachingAttributes());
            print("\tNODES");
            for(Node node : loadBalancer.getNodes()){
                print("");
                print("\t\tNAME OF NODE: " + node.getName());
                print("\t\tIP OF NODE: " + node.getIp());
                print("\t\tPORT OF NODE: " + node.getPort());
                print("\t\tSE OF NODE: " + node.getEnvironment());
                print("\t\tOS OF NODE: " + node.getOperatingSystem());
            }
        }
    }

    private void print(String args){
        System.out.println(args);
    }

}
