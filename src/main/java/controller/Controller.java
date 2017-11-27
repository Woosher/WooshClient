package controller;

import controller.subcontrollers.PopupController;
import controller.subcontrollers.PopupDeployController;
import controller.subcontrollers.PopupErrorController;
import entities.ConnectionInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import static values.Constants.UBUNTU_XENIAL;


public class Controller {


    @FXML
    MenuItem saveMenuItem, loadMenuItem, closeMenuItem, connectionTestMenuItem, deployMenuItem;

    @FXML
    ListView<Machine> machineListView;

    @FXML
    ListView<Node> nodeListView;

    @FXML
    TextField nameTxt, usernameTxt, ipTxt, SSHportTxt, portTxt, pathTxt, nodeNumberTxt, deploymentNameTxt, sshKeyTxt;

    @FXML
    PasswordField passwordTxt;

    @FXML
    VBox infoLayout, nodeExtraInfo, loadBalancerExtraInfo;

    @FXML
    HBox innerBox1, innerBox2, addLoadBalancerBox;

    @FXML
    ComboBox<String> envBox;

    @FXML
    CheckBox passCheck, keyCheck;

    @FXML
    Button saveInfoButton, addNodeButton, toolsAddLb, toolsAddNode, toolsDelMachine, toolsAddNodeToLb;

    PasswordField passwordField, retypePasswordField;

    Label passwordLabel, retypePasswordLabel;

    private int nodeCount = 0;
    private int MachinesCount = 0;
    private Machine currentMachine;
    private boolean deploymentChanged = false;
    private Deployment deployment;
    private Stage popupStage, popupDeployStage, popupErrorStage;
    private PopupController popupController;
    private PopupDeployController popupDeployController;
    private PopupErrorController popupErrorController;
    private FlowModelInterface model;
    private Alert alert;
    private Dialog<Pair<String, String>> dialog = new Dialog<>();
    private javafx.scene.Node doneButton;
    private ButtonType done;
    private ChangeListener<String> savePassListener, loadPassListener;


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
        addLoadBalancerBox.setOnMouseClicked(event -> handleAddLoadbalancer());
        machineListView.setOnMouseClicked(event -> handleMachineClick());
        nodeListView.setOnMouseClicked(event -> handleNodeClick());
        saveInfoButton.setOnMouseClicked(event -> handleSaveInfo());
        addNodeButton.setOnMouseClicked(event -> handleAddNodeToLb());
        toolsAddNode.setOnMouseClicked(event -> handleAddNode());
        toolsAddLb.setOnMouseClicked(event -> handleAddLoadbalancer());
        toolsDelMachine.setOnMouseClicked(event -> handleDeleteMachine());
        toolsAddNodeToLb.setOnMouseClicked(event -> handleAddNodeToLb());
        sshKeyTxt.setOnMouseClicked(event -> handleSettingPath(sshKeyTxt));
        pathTxt.setOnMouseClicked(event -> handleSettingPathForFolder(pathTxt));

        setupLists();
        addListeners();
        createPopup();
        createPopupDeploy();
        createPopupError();
        createPasswordDialog();
    }

    /********************************************************************************************************
     * Button handlers
     */

    private void addListeners(){
        portTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    portTxt.setText(oldValue);
                }
            }
        });
        SSHportTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    SSHportTxt.setText(oldValue);
                }
            }
        });

        passCheck.selectedProperty().addListener(event -> {
            if(passCheck.isSelected()){
                sshKeyTxt.setDisable(true);
                passwordTxt.setDisable(false);
                keyCheck.setSelected(false);
            }
        });

        keyCheck.selectedProperty().addListener(event -> {
            if(keyCheck.isSelected()){
                sshKeyTxt.setDisable(false);
                passwordTxt.setDisable(true);
                passCheck.setSelected(false);
            }
        });
    }

    private void saveWithPasswordDialog(String path) {
        dialog.setTitle("Save with password");
        dialog.setHeaderText("Type your password to encrypt configuration");
        retypePasswordLabel.setVisible(true);
        retypePasswordField.setVisible(true);
        passwordField.setText("");
        retypePasswordField.setText("");

        passwordField.textProperty().addListener(savePassListener);
        retypePasswordField.textProperty().addListener(savePassListener);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == done) {
                if (equalValuePasswords()) {
                    return new Pair<>(retypePasswordField.getText(), passwordField.getText());
                }
            }
            return null;
        });
        Platform.runLater(() -> passwordField.requestFocus());
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(passwords -> {
            saveWithPassword(path, passwords.getKey());
        });
    }

    private void loadwithPasswordDialog(String path) {
        dialog.setTitle("Load with password");
        dialog.setHeaderText("Type your password to decrypt configuration");
        retypePasswordLabel.setVisible(false);
        retypePasswordField.setVisible(false);
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            doneButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == done) {
                return new Pair<>(passwordField.getText(), passwordField.getText());
            }
            return null;
        });

        retypePasswordField.textProperty().removeListener(savePassListener);
        passwordField.textProperty().removeListener(savePassListener);
        passwordField.textProperty().removeListener(loadPassListener);
        passwordField.setText("");
        retypePasswordField.setText("");
        Platform.runLater(() -> passwordField.requestFocus());
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(passwords -> {
            loadWithPassword(path, passwords.getKey());
        });

    }

    private boolean equalValuePasswords() {
        String retype = retypePasswordField.getText();
        String password = passwordField.getText();
        return (retype != null) && (password != null) && !retype.isEmpty() && retype.equals(password);
    }


    private void handleProjectClose() {
        if (deploymentChanged) {
            createCloseDialog();
        } else {
            clearDeployment();
        }
    }

    private void clearDeployment() {
        model.clearDeployment(new ResultsListener<Deployment>() {
            @Override
            public void onCompletion(Deployment result) {
                deployment = result;
                resetGUI();
                setupLists();
            }

            @Override
            public void onFailure(Throwable throwable) {
                showError(throwable.getMessage());
            }
        });
    }


    private void handleNodeClick() {
        Node node = nodeListView.getSelectionModel().getSelectedItem();
        updateInfoLayout(node);
    }

    private void handleMachineClick() {
        setupNodeList();
        Machine machine = machineListView.getSelectionModel().getSelectedItem();
        if (machine != null && machine instanceof LoadBalancer) {
            ObservableList<Node> nodes = ((LoadBalancer) machine).getNodes();
            nodeListView.setItems(nodes);
        } else {
            nodeListView.setItems(null);
        }
        updateInfoLayout(machine);
    }

    private void handleSaveInfo() {
        currentMachine.setPassword(passwordTxt.getText());
        currentMachine.setName(nameTxt.getText());
        currentMachine.setUsername(usernameTxt.getText());
        currentMachine.setIp(ipTxt.getText());
        currentMachine.setSSHPort(Integer.parseInt(SSHportTxt.getText()));
        currentMachine.setPort(Integer.parseInt(portTxt.getText()));
        currentMachine.setSshKeyPath(sshKeyTxt.getText());
        currentMachine.setUseSSHKey(keyCheck.isSelected());
        if (currentMachine instanceof Node) {
            Node node = (Node) currentMachine;
            node.setProgramPath(pathTxt.getText());
            String env = envBox.getSelectionModel().getSelectedItem();
            node.setEnvironment(env);
            node.setOperatingSystem(UBUNTU_XENIAL);
        }
        setupLists();
    }

    private void handleAddNodeToLb() {
        if (deployment != null) {
            if (currentMachine != null && currentMachine instanceof LoadBalancer) {
                LoadBalancer loadBalancer = (LoadBalancer) currentMachine;
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
    }

    private void handleAddNode() {
        if (deployment == null) {
            model.createNewDeployment(deploymentNameTxt.getText(), new ResultsListener<Deployment>() {
                @Override
                public void onCompletion(Deployment result) {
                    deployment = result;
                    putNodeOnDeployment();
                    setupDeploymentInGui();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    showError(throwable.getMessage());
                }
            });
        } else {
            putNodeOnDeployment();
        }
    }

    private void handleDeleteMachine() {
        if (currentMachine != null) {
            if (machineListView.getItems().contains(currentMachine)) {
                Platform.runLater(() -> {
                    model.removeMachineFromDeployment(machineListView.getItems(), currentMachine, new ResultsListener<String>() {
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
            } else if (nodeListView != null && nodeListView.getItems() != null && nodeListView.getItems().contains(currentMachine)) {
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
            }
        }
    }

    private void handleAddLoadbalancer() {
        if (deployment == null) {
            model.createNewDeployment(deploymentNameTxt.getText(), new ResultsListener<Deployment>() {
                @Override
                public void onCompletion(Deployment result) {
                    deployment = result;
                    putLoadbalancerOnDeployment();
                    setupDeploymentInGui();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    showError(throwable.getMessage());
                }
            });
        } else {
            putLoadbalancerOnDeployment();
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
                    Platform.runLater(() -> {
                        popupDeployController.setSpinnerVisibility(false);
                        popupDeployController.resetInfo();
                        popupDeployStage.close();
                    });
                    showError(throwable.getCause().getMessage());

                }
            });
        } else {
            showError("You have nothing inside your deployment");
        }
    }

    private void handleSettingPathForFolder(TextField textField) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select folder of program");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File dir = chooser.showDialog(new Stage());
        if (dir != null) {
            String path = dir.getAbsolutePath();
            if (path != null) {
                textField.setText(path);
            }
        }
    }

    private void handleSettingPath(TextField textField) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select File");
        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            String path = file.getAbsolutePath();
            if (path != null) {
                textField.setText(path);
            }
        }
    }

    private void handleLoad() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            String path = file.getAbsolutePath();
            loadwithPasswordDialog(path);
        }
    }

    private void loadWithPassword(String path, String password){
        if (path != null) {
                model.loadDeployment(path,password, new ResultsListener<Deployment>() {
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

    private void handleSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save File");
        chooser.setInitialFileName(deploymentNameTxt.getText() + ".txt");
        File file = chooser.showSaveDialog(new Stage());
        if (file != null) {
            String path = file.getAbsolutePath();
            saveWithPasswordDialog(path);
        }
    }

    private void saveWithPassword(String path, String password) {
        if (path != null) {
            deployment.setName(deploymentNameTxt.getText());
            model.saveDeployment(path, password, new ResultsListener<Void>() {
                @Override
                public void onCompletion(Void result) {
                    deploymentChanged = false;
                }

                @Override
                public void onFailure(Throwable throwable) {
                    showError(throwable.getMessage());
                }
            });
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

    private void putNodeOnDeployment() {
        if (deployment != null) {
            model.addNodeToDeployment("Node" + ++MachinesCount, new ResultsListener<String>() {
                @Override
                public void onCompletion(String result) {

                }

                @Override
                public void onFailure(Throwable throwable) {
                    showError(throwable.getMessage());
                }
            });
        }
    }

    private void putLoadbalancerOnDeployment() {
        model.addLoadBalancerToDeployment("Loadbalancer" + ++MachinesCount, new ResultsListener<String>() {
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
            sshKeyTxt.setText(machine.getSshKeyPath());
            keyCheck.setSelected(machine.isUseSSHKey());
            passCheck.setSelected(!machine.isUseSSHKey());

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
                pathTxt.setText(node.getProgramPath());
                envBox.getSelectionModel().select(getIndexForString(node.getEnvironment()));
            }
        }
    }

    private int getIndexForString(String environment) {
        if (environment != null) {
            List<String> items = envBox.getItems();
            for (int i = 0; i < items.size(); i++) {
                String option = items.get(i);
                if (environment.equals(option)) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void resetGUI() {
        resetPopup();
        infoLayout.setVisible(false);
        if(machineListView.getItems() != null) machineListView.getItems().clear();
        if(nodeListView.getItems() != null) nodeListView.getItems().clear();
        deploymentNameTxt.setText("");
        deploymentChanged = false;
    }

    private void setupDeploymentInGui() {
        Platform.runLater(() -> {
            deploymentChanged = false;
            machineListView.setItems(deployment.getMachines());
            deploymentNameTxt.setText(deployment.getName());
            setupLists();
            addObservers();
        });
    }

    private void addObservers() {
        Observer observer = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                deploymentChanged = true;
            }
        };

        ListChangeListener<Machine> listChangeListener = new ListChangeListener<Machine>() {
            @Override
            public void onChanged(Change<? extends Machine> c) {
                deploymentChanged = true;
            }
        };

        deployment.addObserver(observer);
        deployment.getMachines().addListener(listChangeListener);

        for (Machine machine : deployment.getMachines()) {
            machine.addObserver(observer);
            if (machine instanceof LoadBalancer) {
                LoadBalancer loadBalancer = (LoadBalancer) machine;
                loadBalancer.getNodes().addListener(listChangeListener);
                for (Node node : loadBalancer.getNodes()) {
                    node.addObserver(observer);
                }
            }
        }
    }

    private void setupLists() {
        setupMachineList();
        setupNodeList();
    }

    private void setupMachineList() {
        machineListView.setCellFactory(new Callback<ListView<Machine>, ListCell<Machine>>() {
            @Override
            public ListCell<Machine> call(ListView<Machine> param) {
                ListCell<Machine> cell = new ListCell<Machine>() {

                    @Override
                    protected void updateItem(Machine item, boolean empty) {
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
        alert.setHeaderText("You have unsaved changes in your deployment");
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

    private void createPasswordDialog() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        retypePasswordField = new PasswordField();
        retypePasswordField.setPromptText("Re-type Password");

        passwordLabel = new Label("Password");
        retypePasswordLabel = new Label("Re-type Password");


        grid.add(retypePasswordLabel, 0, 1);
        grid.add(retypePasswordField, 1, 1);

        grid.add(passwordLabel, 0, 0);
        grid.add(passwordField, 1, 0);

        done = new ButtonType("DONE", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(done, ButtonType.CANCEL);

        doneButton = dialog.getDialogPane().lookupButton(done);
        doneButton.setDisable(true);

        dialog.getDialogPane().setContent(grid);

        savePassListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                doneButton.setDisable(!equalValuePasswords());
            }
        };

        loadPassListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                doneButton.setDisable(newValue.trim().isEmpty());

            }
        };
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
