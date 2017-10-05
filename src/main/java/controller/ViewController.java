package controller;

import com.sun.jndi.ldap.Connection;
import entities.ConnectionInfo;
import entities.ResultsListener;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import modellers.interfaces.FlowModelInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class ViewController {

    private FlowModelInterface model;
    private Scene dialogScene;
    private Stage connectionStage;

    @FXML
    MenuItem saveMenuItem, loadMenuItem, closeMenuItem, connectionTestMenuItem, deployMenuItem, addLoadBalancerMenuItem;

    @FXML
    ListView<LoadBalancer> loadBalancerListView;

    @FXML
    ListView<Node> nodeListView;

    @FXML
    TextField nameTxt, usernameTxt, ipTxt, portTxt, passwordTxt, pathTxt, osTxt;

    @FXML
    VBox infoLayout;

    @FXML
    Button saveInfoButton, addNodeButton;

    private Machine currentMachine;
    private Deployment deployment;

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
        loadBalancerListView.setOnMouseClicked(event -> loadBalancerClick());
        nodeListView.setOnMouseClicked(event -> handleNodeClick() );
        saveInfoButton.setOnMouseClicked(event -> saveInfo());
        addNodeButton.setOnMouseClicked(event -> addNode());
        setupLists();

    }



    private void handleNodeClick(){
        Node node = nodeListView.getSelectionModel().getSelectedItem();
        updateInfoLayout(node);
    }

    private void loadBalancerClick(){
        setupNodeList();
        LoadBalancer loadBalancer = loadBalancerListView.getSelectionModel().getSelectedItem();
        ObservableList<Node> nodes = loadBalancer.getNodes();
        updateInfoLayout(loadBalancer);
        nodeListView.setItems(nodes);

    }

    private void updateInfoLayout(Machine machine){
        currentMachine = machine;
        if(!infoLayout.isVisible()){
            infoLayout.setVisible(true);
        }
        nameTxt.setText(machine.getName());
        usernameTxt.setText(machine.getUsername());
        ipTxt.setText(machine.getIp());
        portTxt.setText(machine.getPort() + "");
        passwordTxt.setText(machine.getPassword());


        if(machine instanceof LoadBalancer){
            addNodeButton.setVisible(true);
        }else {
            addNodeButton.setVisible(false);
        }
    }

    private void saveInfo(){
        currentMachine.setPassword(passwordTxt.getText());
        currentMachine.setName(nameTxt.getText());
        currentMachine.setUsername(usernameTxt.getText());
        currentMachine.setIp(ipTxt.getText());
        currentMachine.setPort(Integer.parseInt(portTxt.getText()));
        setupLists();
    }

    private void addNode(){
        LoadBalancer loadBalancer = (LoadBalancer) currentMachine;
        Node node = new Node();
        node.setName("New Node");
        node.setOperatingSystem("WIN");
        node.setPath("/path/to/stuff");
        node.setEnvironment("JAVA");
        loadBalancer.getNodes().add(node);
        printDeployMent(deployment);
    }

    private void addLoadBalancer() {
        LoadBalancer loadBalancer = new LoadBalancer();
        loadBalancer.setName("New loadbalancer");
        loadBalancer.setCachingAttributes("aoskdoaskd");
        List<Node> nodes = new ArrayList<>();
        loadBalancer.setNodes(nodes);
        deployment.getLoadBalancers().add(loadBalancer);
        printDeployMent(deployment);
    }


    public void handleDeploy() {
        model.sendPackages(new ResultsListener<String>() {
            @Override
            public void onCompletion(String result) {

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
                setupLists();
            }
        });
    }

    public void handleSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save File");
        chooser.setInitialFileName("deploymentsave.txt");
        File file = chooser.showSaveDialog(new Stage());
        if (file != null) {
            String path = file.getAbsolutePath();
            if (path != null) {
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

    public void testConnections() {
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
                            hbChildren.add(new Text("IP: " + s.getMachine().getIp() + s.getInfo()));
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
                        for (Object box : list) {
                            if (box instanceof HBox) {
                                HBox vBox = (HBox) box;
                                CheckBox cb = (CheckBox) vBox.getChildren().get(1);
                                if (cb.isSelected()) {
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


    public void printDeployMent(Deployment deployment) {
        print("NAME OF DEPLOYMENT: " + deployment.getName());
        print("SSL PATH: " + deployment.getSsl_path());
        print("LOADBALANCERS: ");
        for (LoadBalancer loadBalancer : deployment.getLoadBalancers()) {
            print("");
            print("\tNAME OF LB: " + loadBalancer.getName());
            print("\tIP OF LB: " + loadBalancer.getPort());
            print("\tCATCHE OF LB: " + loadBalancer.getCachingAttributes());
            print("\tNODES");
            for (Node node : loadBalancer.getNodes()) {
                print("");
                print("\t\tNAME OF NODE: " + node.getName());
                print("\t\tIP OF NODE: " + node.getIp());
                print("\t\tPORT OF NODE: " + node.getPort());
                print("\t\tSE OF NODE: " + node.getEnvironment());
                print("\t\tOS OF NODE: " + node.getOperatingSystem());
            }
        }
    }

    private void print(String args) {
        System.out.println(args);
    }

    class Observer implements java.util.Observer {

        @Override
        public void update(Observable o, Object arg) {
            System.out.println("asdasda");
            setupLists();
        }
    }

}
