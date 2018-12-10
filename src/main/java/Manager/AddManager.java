package Manager;

import Alert.AlertMaker;
import Data.DataHelper;
import Data.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddManager implements Initializable {

    @FXML
    public JFXTextField add_name;
    @FXML
    public JFXTextField add_login;
    @FXML
    public JFXTextField add_password;
    @FXML
    public JFXButton btn_save;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseHandler.getInstance();
    }

    @FXML
    private void addManager(ActionEvent event) {
        String name = add_name.getText();
        String login = add_login.getText();
        String password = add_password.getText();

        if (name.isEmpty() || login.isEmpty() || password.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Недостаточно данных", "Пожалуйста заполните все поля.");
            return;
        }

        if (DataHelper.isBookExists(login)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Дублирование логина", "Менеджер с данным логином уже существует.\nПожалуйста используйте новый.");
            return;
        }

        Manager manager = new Manager(name, login, password);
        boolean result = DataHelper.insertNewBook(manager);
        System.out.println(result);
        if (result) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Добавлен новый менеджер", name + " успешно добавлен");
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Не удалось добавить нового менеджера", "Проверьте все записи и повторите попытку");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void clearEntries() {
        add_name.clear();
        add_login.clear();
        add_password.clear();
    }
}