package controller;

import entities.ConnectionInfo;
import entities.ResultsListener;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.util.Callback;
import modellers.interfaces.FlowModelInterface;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class ViewController {

    private FlowModelInterface model;

    @FXML
    MenuItem saveMenuItem, loadMenuItem, closeMenuItem, connectionTestMenuItem, deployMenuItem, addLoadBalancerMenuItem;

    @FXML
    ListView<LoadBalancer> loadBalancerListView;

    @FXML
    ListView<Node> nodeListView;

    @FXML
    TextField nameTxt, usernameTxt, ipTxt, SSHportTxt, portTxt, passwordTxt, pathTxt, osTxt, envTxt, nodeNumberTxt, deploymentNameTxt;

    @FXML
    VBox infoLayout, nodeExtraInfo, loadBalancerExtraInfo;

    @FXML
    HBox innerBox1, innerBox2, addLoadBalancerBox;

    @FXML
    Button saveInfoButton, addNodeButton;

    private int nodeCount = 0;
    private int loadBalancerCount = 0;


    private Machine currentMachine;
    private Deployment deployment = new Deployment();
    private Stage popupStage, popupDeployStage;
    private PopupController popupController;
    private PopupDeployController popupDeployController;

    public void initModel(FlowModelInterface model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
        initLayout();
    }

    private void initLayout() {
        saveMenuItem.setOnAction(event -> handleSave());
        loadMenuItem.setOnAction(event -> handleLoad());
        deployMenuItem.setOnAction(event -> handleDeploy());
        connectionTestMenuItem.setOnAction(event -> testConnections());
        addLoadBalancerMenuItem.setOnAction(event -> addLoadBalancer());
        addLoadBalancerBox.setOnMouseClicked(event -> addLoadBalancer());
        loadBalancerListView.setOnMouseClicked(event -> loadBalancerClick());
        nodeListView.setOnMouseClicked(event -> handleNodeClick() );
        saveInfoButton.setOnMouseClicked(event -> saveInfo());
        addNodeButton.setOnMouseClicked(event -> addNode());
        setupLists();
        createPopup();
        createPopupDeploy();
    }

    private void handleNodeClick(){
        Node node = nodeListView.getSelectionModel().getSelectedItem();
        updateInfoLayout(node);
    }

    private void loadBalancerClick(){
        setupNodeList();
        LoadBalancer loadBalancer = loadBalancerListView.getSelectionModel().getSelectedItem();
        if(loadBalancer != null){
            ObservableList<Node> nodes = loadBalancer.getNodes();
            updateInfoLayout(loadBalancer);
            nodeListView.setItems(nodes);
        }
    }

    private void updateInfoLayout(Machine machine){
        currentMachine = machine;
        if(currentMachine != null) {
            if (!infoLayout.isVisible()) {
                infoLayout.setVisible(true);
            }
            nameTxt.setText(machine.getName());
            usernameTxt.setText(machine.getUsername());
            ipTxt.setText(machine.getIp());
            portTxt.setText(machine.getPort() + "");
            SSHportTxt.setText(Integer.toString(machine.getSSHPort()));
            passwordTxt.setText(machine.getPassword());

            if (machine instanceof LoadBalancer) {
                LoadBalancer loadBalancer = (LoadBalancer) machine;
                loadBalancerExtraInfo.setVisible(true);
                loadBalancerExtraInfo.setManaged(true);
                nodeExtraInfo.setVisible(false);
                nodeExtraInfo.setManaged(false);
                nodeNumberTxt.setText(loadBalancer.getNodes().size() + "");
            } else {
                Node node = (Node) machine;
                nodeExtraInfo.setVisible(true);
                nodeExtraInfo.setManaged(true);
                loadBalancerExtraInfo.setVisible(false);
                loadBalancerExtraInfo.setManaged(false);
                osTxt.setText(node.getOperatingSystem());
                pathTxt.setText(node.getPath());
                envTxt.setText(node.getEnvironment());
            }
        }
    }


    private void saveInfo(){
        currentMachine.setPassword(passwordTxt.getText());
        currentMachine.setName(nameTxt.getText());
        currentMachine.setUsername(usernameTxt.getText());
        currentMachine.setIp(ipTxt.getText());
        currentMachine.setSSHPort(Integer.parseInt(SSHportTxt.getText()));
        currentMachine.setPort(Integer.parseInt(portTxt.getText()));
        if(currentMachine instanceof Node){
            Node node = (Node) currentMachine;
            node.setPath(pathTxt.getText());
            node.setEnvironment(envTxt.getText());
            node.setOperatingSystem(osTxt.getText());
        }
        setupLists();
    }

    private void addNode(){
        LoadBalancer loadBalancer = (LoadBalancer) currentMachine;
        Node node = new Node();
        node.setName("node" + ++nodeCount);
        loadBalancer.getNodes().add(node);
    }

    private void addLoadBalancer() {
        if(deployment == null){
            model.createNewDeployment(deploymentNameTxt.getText(), new ResultsListener<Deployment>() {
                @Override
                public void onCompletion(Deployment result) {
                    deployment = result;
                    putLoadBalancersOnDeployment();
                    setupDeploymentInGui();

                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }else {
            putLoadBalancersOnDeployment();
        }
    }

    private void putLoadBalancersOnDeployment(){
        LoadBalancer loadBalancer = new LoadBalancer();
        loadBalancer.setName("loadBalancer" + ++loadBalancerCount);
        loadBalancer.setCachingAttributes("aoskdoaskd");
        deployment.getLoadBalancers().add(loadBalancer);
    }


    public void handleDeploy() {
        popupDeployStage.show();
        popupDeployController.setSpinnerVisibility(true);
        model.sendPackages(new ResultsListener<List<ConnectionInfo>>() {
            @Override
            public void onCompletion(List<ConnectionInfo> result) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        popupDeployController.setSpinnerVisibility(false);
                        ObservableList<ConnectionInfo> observableList = FXCollections.observableArrayList();
                        observableList.addAll(result);
                        popupDeployController.addInfo(observableList);
                        popupDeployStage.show();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                print(throwable.getMessage());
            }
        });
    }

    public void handleLoad() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            String path = file.getAbsolutePath();
            if (path != null) {
                model.loadDeployment(path, new ResultsListener<Deployment>() {
                    @Override
                    public void onCompletion(Deployment result) {
                        deployment = result;
                       // deployment.getLoadBalancers().get(0).addObserver(new Observer());
                        setupDeploymentInGui();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        print(throwable.getMessage());
                    }
                });
            }
        }
    }

    private void setupDeploymentInGui() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loadBalancerListView.setItems(deployment.getLoadBalancers());
                deploymentNameTxt.setText(deployment.getName());
                setupLists();
            }
        });
    }

    public void handleSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save File");
        chooser.setInitialFileName(deploymentNameTxt.getText() + ".txt");
        File file = chooser.showSaveDialog(new Stage());
        if (file != null) {
            String path = file.getAbsolutePath();
            if (path != null) {
                deployment.setName(deploymentNameTxt.getText());
                model.saveDeployment(path, new ResultsListener<Void>() {
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
    }

    private void setupLists(){
        setupLoadbalancerList();
        setupNodeList();
    }

    private void setupLoadbalancerList(){
        loadBalancerListView.setCellFactory(new Callback<ListView<LoadBalancer>, ListCell<LoadBalancer>>() {
            @Override
            public ListCell<LoadBalancer> call(ListView<LoadBalancer> param) {
                ListCell<LoadBalancer> cell = new ListCell<LoadBalancer>() {

                    @Override
                    protected void updateItem(LoadBalancer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void setupNodeList(){
        nodeListView.setCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
            @Override
            public ListCell<Node> call(ListView<Node> param) {
                ListCell<Node> cell = new ListCell<Node>() {

                    @Override
                    protected void updateItem(Node item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        }
                    }
                };
                return cell;
            }
        });
    }
    private void createPopupDeploy(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("popupDeploy.fxml"));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        popupDeployStage = new Stage();
        popupDeployStage.initModality(Modality.APPLICATION_MODAL);
        popupDeployStage.setTitle("Deployment report");
        popupDeployStage.setScene(new Scene(root1));
        popupDeployStage.setResizable(false);
        popupDeployController = fxmlLoader.getController();
        popupDeployStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                popupDeployController.resetInfo();
            }
        });
        popupDeployController.setEventHandler(new EventHandler() {
            @Override
            public void handle(Event event) {
                popupDeployStage.close();
            }
        });
    }

    private void createPopup(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("popup.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Connection info");
        popupStage.setScene(new Scene(root1));
        popupStage.setResizable(false);
        popupStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                popupController.resetInfo();
            }
        });
        popupController = (PopupController) fxmlLoader.getController();
        popupController.setEventHandler(new EventHandler() {
            @Override
            public void handle(Event event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        popupController.setListViewDisabled(true);
                        popupController.setSpinnerVisibility(true);
                    }
                });

                List<Machine> machines =popupController.getSelectedMachines();
                addKnownHosts(machines);
            }
        });

    }

    public void testConnections() {
        popupController.setSpinnerVisibility(true);
        popupStage.show();
        model.testConnections(new ResultsListener<List<ConnectionInfo>>() {
            @Override
            public void onCompletion(List<ConnectionInfo> result) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        popupController.setSpinnerVisibility(false);
                        ObservableList<ConnectionInfo> observableList = FXCollections.observableArrayList();
                        observableList.addAll(result);
                        popupController.addInfo(observableList);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                print(throwable.getMessage());

            }
        });
    }


    private void addKnownHosts(List<Machine> hosts) {
        model.addKnownHosts(hosts, new ResultsListener<Boolean>() {
            @Override
            public void onCompletion(Boolean result) {
                resetPopup();
                print(result + "");
            }

            @Override
            public void onFailure(Throwable throwable) {
                resetPopup();
                print(throwable.getMessage());
            }
        });
    }

    private void resetPopup(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                popupController.setSpinnerVisibility(false);
                popupController.setListViewDisabled(false);
                popupController.resetInfo();
                popupStage.close();
            }
        });
    }

    private void print(String args) {
        System.out.println(args);
    }


}
