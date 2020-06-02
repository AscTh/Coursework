package Player;

import Alert.AlertMaker;
import Data.DatabaseHandler;
import Sample.SidePanelController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddPlayer extends SidePanelController implements Initializable {
    @FXML
    public StackPane rootPane;
    @FXML
    public AnchorPane mainContainer;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField height;
    @FXML
    public JFXTextField weight;
    @FXML
    public JFXTextField numder;
    @FXML
    public JFXTextField role;
    @FXML
    public JFXTextField name_team;
    @FXML
    public JFXButton saveButton;
    @FXML
    public JFXButton cancelButton;

    private Boolean isInEditMode = false;
    DatabaseHandler handler;

    private double x, y;

    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    protected void min(ActionEvent event) {
        super.min(event);
    }

    @FXML
    protected void max(ActionEvent event) {
        super.max(event);
    }

    @FXML
    protected void exit(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DatabaseHandler.getInstance();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    private int ID_player() throws SQLException {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String nam = name_team.getText();
        String position = role.getText();
        return handler.getId(nam, position);
    }

    @FXML
    private void addMember(ActionEvent event) {
        String mName = name.getText();
        String mHeight = height.getText();
        String mWeight = weight.getText();
        String mNumber = numder.getText();
        String mRole = role.getText();
        String mTeam_name = name_team.getText();

        boolean flag = mName.isEmpty() || mHeight.isEmpty() || mWeight.isEmpty() || mNumber.isEmpty() || mRole.isEmpty() || mTeam_name.isEmpty();
        if (flag) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in all fields.");
            return;
        }

        if (isInEditMode) {
            try {
                handleUpdateMember();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        /*Player player = new Player(mName, Integer.parseInt(mHeight), Float.parseFloat(mWeight), Integer.parseInt(mNumber), mRole, mTeam_name);
        boolean result = DataHelper.insertNewMember(player);
        if (result) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "New member added", mName + " has been added");
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed to add new member", "Check you entries and try again.");
        }*/
    }

    void inflateUI(Player player) {
        name.setText(player.getName());
        height.setText(String.valueOf(player.getHeight()));
        weight.setText(String.valueOf(player.getWeight()));
        numder.setText(String.valueOf(player.getNumber()));
        role.setText(player.getRole());
        role.setEditable(false);
        name_team.setText(player.getTeam_name());
        name_team.setEditable(false);
        isInEditMode = Boolean.TRUE;
    }

    private void handleUpdateMember() throws SQLException {
        Player player = new Player(name.getText(), Integer.parseInt(height.getText()), Float.parseFloat(weight.getText()), Integer.parseInt(numder.getText()), role.getText(), name_team.getText());
        if (DatabaseHandler.getInstance().updateMember(player, ID_player())) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success", "Member data updated.");
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed", "Can't update member.");
        }
    }
}
