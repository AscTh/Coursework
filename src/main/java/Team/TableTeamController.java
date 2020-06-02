package Team;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import Data.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TableTeamController implements Initializable {

    private ObservableList<Club> list = FXCollections.observableArrayList();

    @FXML
    private TableView<Club> tableView;
    @FXML
    private TableColumn<Club, String> name;
    @FXML
    private TableColumn<Club, String> trainer;
    @FXML
    private TableColumn<Club, String> liga;
    @FXML
    private TableColumn<Club, String> stadium;
    @FXML
    private TableColumn<Club, Float> rating;
    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }

    private void initCol() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        trainer.setCellValueFactory(new PropertyValueFactory<>("trainer"));
        liga.setCellValueFactory(new PropertyValueFactory<>("liga"));
        stadium.setCellValueFactory(new PropertyValueFactory<>("stadium"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
    }

    private void loadData() {
        list.clear();

        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT club.team_name                             AS NAME,\n" +
                "       trainer.first_name || ' ' || trainer.last_name AS TRAINER,\n" +
                "       championship.name                              AS CHAMPIONSHIP,\n" +
                "       stadium.name                                   AS STADIUM,\n" +
                "       rating_uefa.rating                             AS RATING_UEFA\n" +
                "FROM club,\n" +
                "     trainer,\n" +
                "     championship,\n" +
                "     stadium,\n" +
                "     rating_uefa\n" +
                "WHERE club.championship_id = championship.id\n" +
                "  AND club.stadium_id = stadium.id\n" +
                "  AND club.trainer_id = trainer.id\n" +
                "  AND club.id = rating_uefa.club_id";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String name = rs.getString("NAME");
                String trainer = rs.getString("TRAINER");
                String liga = rs.getString("CHAMPIONSHIP");
                String stadium = rs.getString("STADIUM");
                float rating = rs.getFloat("RATING_UEFA");

                list.add(new Club(name, trainer, liga, stadium, rating));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        tableView.setItems(list);
    }

}