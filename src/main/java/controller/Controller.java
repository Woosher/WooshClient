package controller;

import controller.subcontrollers.PopupController;
import controller.subcontrollers.PopupDeployController;
import controller.subcontrollers.PopupErrorController;
import entities.ConnectionInfo;
import javafx.collections.ListChangeListener;
import modellers.FlowModeller;
import modellers.interfaces.ResultsListener;
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
import tools.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;


public class Controller {


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
    Button saveInfoButton, addNodeButton, toolsAddLb, toolsDelLb, toolsAddNode, toolsDelNode;


    private int nodeCount = 0;
    private int loadBalancerCount = 0;
    private Machine currentMachine;
    private boolean deploymentChanged = false;
    private Deployment deployment;
    private Stage popupStage, popupDeployStage, popupErrorStage;
    private PopupController popupController;
    private PopupDeployController popupDeployController;
    private PopupErrorController popupErrorController;
    private FlowModelInterface model;
    private Alert alert;


    @FXML
    public void initialize() {
        model = new FlowModeller();
        initLayout();
    }

    private void initLayout() {
        saveMenuItem.setOnAction(event -> handleSave());
        loadMenuItem.setOnAction(event -> handleLoad());
        deployMenuItem.setOnAction(event -> handleDeploy());
        closeMenuItem.setOnAction(event -> handleProjectClose());
        connectionTestMenuItem.setOnAction(event -> handleTestConnections());
        addLoadBalancerMenuItem.setOnAction(event -> handleAddLoadBalancer());
        addLoadBalancerBox.setOnMouseClicked(event -> handleAddLoadBalancer());
        loadBalancerListView.setOnMouseClicked(event -> handleLoadBalancerClick());
        nodeListView.setOnMouseClicked(event -> handleNodeClick());
        saveInfoButton.setOnMouseClicked(event -> handleSaveInfo());
        addNodeButton.setOnMouseClicked(event -> handleAddNode());
        toolsAddNode.setOnMouseClicked(event -> handleAddNode());
        toolsAddLb.setOnMouseClicked(event -> handleAddLoadBalancer());
        toolsDelNode.setOnMouseClicked(event -> handleDeleteNode());
        toolsDelLb.setOnMouseClicked(event -> handleDeleteLoadbalancer());
        setupLists();
        createPopup();
        createPopupDeploy();
        createPopupError();
    }

    /********************************************************************************************************
     * Button handlers
     */

    private void handleProjectClose() {
        if (deploymentChanged) {
            createCloseDialog();
        } else {
            clearDeployment();
        }
    }

    private void clearDeployment() {
        model.clearDeployment(deployment, new ResultsListener<Deployment>() {
            @Override
            public void onCompletion(Deployment result) {
                resetGUI();
                setupLists();
            }

            @Override
            public void onFailure(Throwable throwable) {
                showError(throwable.getMessage());
            }
        });
    }

    private void handleDeleteNode() {
        if (currentMachine instanceof Node) {
            Platform.runLater(() -> {
                model.removeNodeFromLoadBalancer(nodeListView.getItems(), (Node) currentMachine, new ResultsListener<String>() {
                    @Override
                    public void onCompletion(String result) {
                        infoLayout.setVisible(false);
                        setupNodeList();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        showError(throwable.getMessage());
                    }
                });
            });
        } else {
            showError("Select a node");
        }
    }

    private void handleDeleteLoadbalancer() {
        if (currentMachine != null) {
            if (currentMachine instanceof LoadBalancer) {
                Platform.runLater(() -> {
                    model.removeLoadBalancerFromDeployment(loadBalancerListView.getItems(), (LoadBalancer) currentMachine, new ResultsListener<String>() {
                        @Override
                        public void onCompletion(String result) {
                            infoLayout.setVisible(false);
                            setupLists();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            showError(throwable.getMessage());
                        }
                    });
                });
            } else {
                showError("Select a loadbalancer");
            }
        }
    }

    private void handleNodeClick() {
        Node node = nodeListView.getSelectionModel().getSelectedItem();
        updateInfoLayout(node);
    }

    private void handleLoadBalancerClick() {
        setupNodeList();
        LoadBalancer loadBalancer = loadBalancerListView.getSelectionModel().getSelectedItem();
        if (loadBalancer != null) {
            ObservableList<Node> nodes = loadBalancer.getNodes();
            updateInfoLayout(loadBalancer);
            nodeListView.setItems(nodes);
        }
    }

    private void handleSaveInfo() {
        currentMachine.setPassword(passwordTxt.getText());
        currentMachine.setName(nameTxt.getText());
        currentMachine.setUsername(usernameTxt.getText());
        currentMachine.setIp(ipTxt.getText());
        currentMachine.setSSHPort(Integer.parseInt(SSHportTxt.getText()));
        currentMachine.setPort(Integer.parseInt(portTxt.getText()));
        if (currentMachine instanceof Node) {
            Node node = (Node) currentMachine;
            node.setPath(pathTxt.getText());
            node.setEnvironment(envTxt.getText());
            node.setOperatingSystem(osTxt.getText());
        }
        setupLists();
    }

    private void handleAddNode() {
        LoadBalancer loadBalancer = (LoadBalancer) currentMachine;
        if (currentMachine != null) {
            Platform.runLater(() -> {
                model.addNodeToLoadBalancer(loadBalancer, "node" + ++nodeCount, new ResultsListener<String>() {
                    @Override
                    public void onCompletion(String result) {
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        showError(throwable.getMessage());
                    }
                });
            });
        } else {
            showError("You have not selected a loadbalancer");
        }
    }

    private void handleAddLoadBalancer() {
        if (deployment == null) {
            model.createNewDeployment(deploymentNameTxt.getText(), new ResultsListener<Deployment>() {
                @Override
                public void onCompletion(Deployment result) {
                    deployment = result;
                    putLoadBalancersOnDeployment();
                    setupDeploymentInGui();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    showError(throwable.getMessage());
                }
            });
        } else {
            putLoadBalancersOnDeployment();
        }
    }

    private void handleDeploy() {
        if (deployment != null) {
            popupDeployStage.show();
            popupDeployController.setSpinnerVisibility(true);
            popupDeployController.resetInfo();
            model.sendPackages(new ResultsListener<List<ConnectionInfo>>() {
                @Override
                public void onCompletion(List<ConnectionInfo> result) {
                    Platform.runLater(() -> {
                        popupDeployController.setSpinnerVisibility(false);
                        ObservableList<ConnectionInfo> observableList = FXCollections.observableArrayList();
                        observableList.addAll(result);
                        popupDeployController.addInfo(observableList);
                        popupDeployStage.show();
                    });
                }

                @Override
                public void onFailure(Throwable throwable) {
                    showError(throwable.getMessage());
                }
            });
        } else {
            showError("You have nothing inside your deployment");
        }
    }

    private void handleLoad() {
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
                        setupDeploymentInGui();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        showError(throwable.getCause().getMessage());
                    }
                });
            }
        }
    }

    private void handleSave() {
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
                        showError(throwable.getMessage());
                    }
                });
            }
        }
    }

    private void handleAddKnownHosts(List<Machine> hosts) {
        model.addKnownHosts(hosts, new ResultsListener<Boolean>() {
            @Override
            public void onCompletion(Boolean result) {
                resetPopup();
            }

            @Override
            public void onFailure(Throwable throwable) {
                resetPopup();
                showError(throwable.getMessage());
            }
        });
    }

    private void handleTestConnections() {
        if (deployment != null) {
            popupController.setSpinnerVisibility(true);
            popupStage.show();
            model.testConnections(new ResultsListener<List<ConnectionInfo>>() {
                @Override
                public void onCompletion(List<ConnectionInfo> result) {
                    Platform.runLater(() -> {
                        popupController.setSpinnerVisibility(false);
                        ObservableList<ConnectionInfo> observableList = FXCollections.observableArrayList();
                        observableList.addAll(result);
                        popupController.addInfo(observableList);
                    });
                }

                @Override
                public void onFailure(Throwable throwable) {
                    showError(throwable.getMessage());
                }
            });
        } else {
            showError("You have nothing inside your deployment");
        }
    }

    /********************************************************************************************************
     * GUI Update methods
     */

    private void showError(String error) {
        Platform.runLater(() -> {
            popupErrorController.setMessage(error);
            popupErrorStage.show();
        });

    }

    private void putLoadBalancersOnDeployment() {
        model.addLoadBalancerToDeployment("loadBalancer" + ++loadBalancerCount, new ResultsListener<String>() {
            @Override
            public void onCompletion(String result) {

            }

            @Override
            public void onFailure(Throwable throwable) {
                showError(throwable.getMessage());
            }
        });
    }

    private void updateInfoLayout(Machine machine) {
        currentMachine = machine;
        if (currentMachine != null) {
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

    private void resetGUI() {
        resetPopup();
        infoLayout.setVisible(false);
        loadBalancerListView.getItems().clear();
        nodeListView.getItems().clear();
        deploymentNameTxt.setText("");
    }

    private void setupDeploymentInGui() {
        Platform.runLater(() -> {
            loadBalancerListView.setItems(deployment.getLoadBalancers());
            deploymentNameTxt.setText(deployment.getName());
            setupLists();
            addObservers();
        });


    }

    private void addObservers() {
        deployment.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                deploymentChanged = true;
            }
        });

        deployment.getLoadBalancers().addListener(new ListChangeListener<LoadBalancer>() {
            @Override
            public void onChanged(Change<? extends LoadBalancer> c) {
                deploymentChanged = true;
            }
        });

        for (LoadBalancer loadBalancer : deployment.getLoadBalancers()) {
            loadBalancer.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    deploymentChanged = true;
                }
            });
            loadBalancer.getNodes().addListener(new ListChangeListener<Node>() {
                @Override
                public void onChanged(Change<? extends Node> c) {
                    deploymentChanged = true;
                }
            });
            for (Node node : loadBalancer.getNodes()) {
                node.addObserver(new Observer() {
                    @Override
                    public void update(Observable o, Object arg) {
                        deploymentChanged = true;
                    }
                });
            }
        }
    }

    private void setupLists() {
        setupLoadbalancerList();
        setupNodeList();
    }

    private void setupLoadbalancerList() {
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

    private void setupNodeList() {
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

    private void createCloseDialog() {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Woosh");
        alert.setHeaderText("You have unsaved changed in your deployment");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Save");
        ButtonType buttonTypeTwo = new ButtonType("Close without saving");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            handleSave();
        } else if (result.get() == buttonTypeTwo) {
            clearDeployment();
        } else {
            alert.close();
        }
    }


    private void createPopupError() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("popupError.fxml"));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        popupErrorStage = new Stage();
        popupErrorStage.initModality(Modality.APPLICATION_MODAL);
        popupErrorStage.setTitle("Woosh");
        popupErrorStage.setScene(new Scene(root1));
        popupErrorStage.setResizable(false);
        popupErrorController = fxmlLoader.getController();
        popupErrorStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            }
        });
        popupErrorController.setEventHandler(new EventHandler() {
            @Override
            public void handle(Event event) {
                popupErrorStage.close();
            }
        });
    }

    private void createPopupDeploy() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("popupDeploy.fxml"));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        popupDeployStage = new Stage();
        popupDeployStage.initModality(Modality.APPLICATION_MODAL);
        popupDeployStage.setTitle("Woosh");
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

    private void createPopup() {
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
                Platform.runLater(() -> {
                    popupController.setListViewDisabled(true);
                    popupController.setSpinnerVisibility(true);
                });

                List<Machine> machines = popupController.getSelectedMachines();
                handleAddKnownHosts(machines);
            }
        });

    }

    private void resetPopup() {
        Platform.runLater(() -> {
            popupController.setSpinnerVisibility(false);
            popupController.setListViewDisabled(false);
            popupController.resetInfo();
            popupStage.close();
        });
    }


}
