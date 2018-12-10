package Sample;

import Data.DatabaseHandler;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

// BTN_CLICK = 1 - суперадмин
// BTN_CLICK = 0 - админ
// BTN_CLICK = 2 - юзер

public class LoginController implements Initializable {
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton btn_notReg, btn_Reg;
    @FXML
    private JFXTextField name;
    @FXML
    private Label state;

    private static int BTN_CLICK = 2;

    public static int getBTN_CLICK() {
        return BTN_CLICK;
    }

    static void setBTN_CLICK(int BTN_CLICK) {
        LoginController.BTN_CLICK = BTN_CLICK;
    }


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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void max(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    protected void exit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void okBtnAction(ActionEvent event) throws IOException, SQLException {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String nam = name.getText(), pass = password.getText();
        ResultSet resultSet = handler.singUser(nam, pass);
        if (resultSet.next()) {
            String sql_role = "SELECT ROLE FROM MANAGER WHERE LAST_NAME = " + nam + " AND PASSWORD = " + pass;
            ResultSet result = handler.execQuery(sql_role);
            while (result != null && result.next()) {
                setBTN_CLICK(result.getInt("role"));
            }
            state.setText("Добро пожаловать!");
            Parent login_page_parent = FXMLLoader.load(getClass().getResource("/appv2.fxml"));
            Scene login_page_scene = new Scene(login_page_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.hide();
            app_stage.setScene(login_page_scene);
            app_stage.show();
        } else {
            state.setText("Неверный логин или пароль");
        }
    }

    @FXML
    public void notReg(ActionEvent event) throws IOException {
        Parent login_page_parent = FXMLLoader.load(getClass().getResource("/appv2.fxml"));
        Scene login_page_scene = new Scene(login_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(login_page_scene);
        app_stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}