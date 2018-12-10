package Sample;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Data.DatabaseHandler;
import Team.TableTeamController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SidePanelController extends LoginController implements Initializable, BookReturnCallback {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public JFXButton btn_team, bnt_golden_shoe, btn_player, btn_calendar, btn_table, btn_transfer, btn_adding, btn_close;
    @FXML
    public JFXTabPane tabpane;
    @FXML
    private Text count_team;
    @FXML
    private Text count_player;
    @FXML
    private TableView<Goal> tableView_goal;
    @FXML
    private TableColumn<Goal, String> name_goals;
    @FXML
    private TableColumn<Goal, Integer> goal;
    @FXML
    private TableColumn<Goal, Float> coeff;
    @FXML
    private TableColumn<Goal, Integer> point;

    private ObservableList<Goal> list = FXCollections.observableArrayList();

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
        super.exit(event);
    }

    private void initCol() {
        name_goals.setCellValueFactory(new PropertyValueFactory<>("name"));
        goal.setCellValueFactory(new PropertyValueFactory<>("goals"));
        coeff.setCellValueFactory(new PropertyValueFactory<>("coefficient"));
        point.setCellValueFactory(new PropertyValueFactory<>("points"));
    }

    private void loadData() {
        list.clear();

        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT player.NAME, golden_shoe.goals AS GOALS, golden_shoe.coefficient, golden_shoe.points\n" +
                "FROM golden_shoe,\n" +
                "     player\n" +
                "WHERE golden_shoe.player_id = player.id";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String name = rs.getString("NAME");
                int goals = rs.getInt("GOALS");
                float coefficient = rs.getFloat("COEFFICIENT");
                int points = rs.getInt("POINTS");

                list.add(new Goal(name, goals, coefficient, points));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        tableView_goal.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
        if (getBTN_CLICK() != 1)
            btn_adding.setVisible(false);
        String qu = "SELECT COUNT(*) FROM club";
        String sql = "SELECT COUNT(*) FROM player";
        DatabaseHandler handler = DatabaseHandler.getInstance();
        ResultSet rs = handler.execQuery(qu);
        ResultSet rsl = handler.execQuery(sql);
        try {
            if (rs.next() && rsl.next()) {
                try {
                    int mName = rs.getInt(1);
                    int mMobile = rsl.getInt(1);
                    count_team.setText(String.valueOf(mName));
                    count_player.setText(String.valueOf(mMobile));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btn_team(ActionEvent event) {
        AnchorPane anchorPane = null;
        try {
            anchorPane = FXMLLoader.load(getClass().getResource("/table_team.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Tab tab = new Tab("Команды", anchorPane);
        tabpane.getTabs().add(tab);
    }

    @FXML
    protected void btn_player(ActionEvent event) {
        AnchorPane anchorPane = null;
        try {
            anchorPane = FXMLLoader.load(getClass().getResource("/table_player.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Tab tab = new Tab("Игроки", anchorPane);
        tabpane.getTabs().add(tab);
    }

    @FXML
    protected void btn_adding(ActionEvent event) {
        StackPane stackPane = null;
        try {
            stackPane = FXMLLoader.load(getClass().getResource("/add_manager.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Tab tab = new Tab("Добавление", stackPane);
        tabpane.getTabs().add(tab);
    }

    @FXML
    protected void btn_close(ActionEvent event) throws IOException {
        setBTN_CLICK(2);
        Parent login_page_parent = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene login_page_scene = new Scene(login_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(login_page_scene);
        app_stage.show();
    }

    @Override
    public void loadBookReturn(String bookID) {
    }

    public static class Goal {

        private final SimpleStringProperty name;
        private final SimpleIntegerProperty goals;
        private final SimpleFloatProperty coeff;
        private final SimpleIntegerProperty points;

        Goal(String name, int goals, float coeff, int points) {
            this.name = new SimpleStringProperty(name);
            this.goals = new SimpleIntegerProperty(goals);
            this.coeff = new SimpleFloatProperty(coeff);
            this.points = new SimpleIntegerProperty(points);
        }

        public String getName() {
            return name.get();
        }

        public int getId() {
            return goals.get();
        }

        public float getMobile() {
            return coeff.get();
        }

        public int getEmail() {
            return points.get();
        }

    }
}