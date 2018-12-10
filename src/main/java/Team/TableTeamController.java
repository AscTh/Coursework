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

    private ObservableList<Member> list = FXCollections.observableArrayList();

    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member, String> name;
    @FXML
    private TableColumn<Member, String> trainer;
    @FXML
    private TableColumn<Member, String> liga;
    @FXML
    private TableColumn<Member, String> stadium;
    @FXML
    private TableColumn<Member, Float> rating;
    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }

    private void initCol() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        trainer.setCellValueFactory(new PropertyValueFactory<>("id"));
        liga.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        stadium.setCellValueFactory(new PropertyValueFactory<>("email"));
        rating.setCellValueFactory(new PropertyValueFactory<>("qwer"));
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
                String id = rs.getString("NAME");
                String first_name = rs.getString("TRAINER");
                String last_name = rs.getString("CHAMPIONSHIP");
                String password = rs.getString("STADIUM");
                float qwer = rs.getFloat("RATING_UEFA");

                list.add(new Member(id, last_name, first_name, password, qwer));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        tableView.setItems(list);
    }

    public static class Member {

        private final SimpleStringProperty name;
        private final SimpleStringProperty id;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;
        private final SimpleFloatProperty qwer;

        Member(String name, String id, String mobile, String email, float qwer) {
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleStringProperty(id);
            this.mobile = new SimpleStringProperty(mobile);
            this.email = new SimpleStringProperty(email);
            this.qwer = new SimpleFloatProperty(qwer);
        }

        public String getName() {
            return name.get();
        }

        public String getId() {
            return id.get();
        }

        public String getMobile() {
            return mobile.get();
        }

        public String getEmail() {
            return email.get();
        }

        public float getQwer() {
            return qwer.get();
        }
    }
}