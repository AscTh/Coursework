package Player;

import Alert.AlertMaker;
import Data.DatabaseHandler;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static Sample.LoginController.getBTN_CLICK;

public class TablePlayerController implements Initializable {

    public MenuItem refresh;
    public MenuItem edit;
    public MenuItem delete;
    private ObservableList<Player> list = FXCollections.observableArrayList();

    @FXML
    public TableView<Player> tableView;
    @FXML
    public TableColumn<Player, String> name;
    @FXML
    public TableColumn<Player, Integer> height;
    @FXML
    public TableColumn<Player, Float> weight;
    @FXML
    public TableColumn<Player, Integer> number;
    @FXML
    public TableColumn<Player, String> role;
    @FXML
    public TableColumn<Player, String> team_name;
    @FXML
    public JFXTextField filter;
    @FXML
    public AnchorPane contentPane;
    @FXML
    public ContextMenu context;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        loadData();
        initFilter();
        if (getBTN_CLICK() == 2) {
            refresh.setVisible(false);
            edit.setVisible(false);
            delete.setVisible(false);
        }
    }

    private void initCol() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        height.setCellValueFactory(new PropertyValueFactory<>("height"));
        weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        team_name.setCellValueFactory(new PropertyValueFactory<>("team_name"));
    }

    private void loadData() {
        list.clear();

        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT player.name || ' ' || player.last_name   AS NAME,\n" +
                "       player.height,\n" +
                "       player.weight,\n" +
                "       player.numder,\n" +
                "       player.role_position,\n" +
                "       club.team_name                               AS CLUB\n" +
                "FROM player,\n" +
                "     club\n" +
                "WHERE player.club_id = club.id";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String name = rs.getString("NAME");
                int height = rs.getInt("HEIGHT");
                float weight = rs.getFloat("WEIGHT");
                int number = rs.getInt("NUMDER");
                String role = rs.getString("ROLE_POSITION");
                String team_name = rs.getString("CLUB");

                list.add(new Player(name, height, weight, number, role, team_name));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        tableView.setItems(list);
    }

    private void initFilter() {
        filter.textProperty().addListener(o -> {
            if (filter.textProperty().get().isEmpty()) {
                tableView.setItems(list);
                return;
            }
            ObservableList<Player> tableItems = FXCollections.observableArrayList();
            ObservableList<TableColumn<Player, ?>> cols = tableView.getColumns();
            for (Player aFilterData : list) {
                for (TableColumn<Player, ?> col1 : cols) {
                    String cellValue = ((TableColumn) col1).getCellData(aFilterData).toString();
                    cellValue = cellValue.toLowerCase();
                    if (cellValue.contains(filter.textProperty().get().toLowerCase())) {
                        tableItems.add(aFilterData);
                        break;
                    }
                }
            }
            tableView.setItems(tableItems);
        });
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadData();
    }

    @FXML
    private void handleMemberEdit(ActionEvent event) {
        Player selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("Ни один игрок не выбран", "Пожалуйста, выберите игрока для редактирования.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_player.fxml"));
            Parent parent = loader.load();

            AddPlayer controller = loader.getController();
            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(parent));
            stage.show();
            stage.setOnCloseRequest((e) -> handleRefresh(new ActionEvent()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleMemberDelete(ActionEvent event) {
        //Fetch the selected row
        Player selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("No member selected", "Please select a member for deletion.");
            return;
        }
        /*if (DatabaseHandler.getInstance().isMemberHasAnyBooks(selectedForDeletion)) {
            AlertMaker.showErrorMessage("Cant be deleted", "This member has some books.");
            return;
        }*/
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting book");
        alert.setContentText("Are you sure want to delete " + selectedForDeletion.getName() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            boolean result = DatabaseHandler.getInstance().deleteMember(selectedForDeletion);
            if (result) {
                AlertMaker.showSimpleAlert("Book deleted", selectedForDeletion.getName() + " was deleted successfully.");
                list.remove(selectedForDeletion);
            } else {
                AlertMaker.showSimpleAlert("Failed", selectedForDeletion.getName() + " could not be deleted");
            }
        } else {
            AlertMaker.showSimpleAlert("Deletion cancelled", "Deletion process cancelled");
        }
    }

}