package Player;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Player {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty height;
    private final SimpleFloatProperty weight;
    private final SimpleIntegerProperty number;
    private final SimpleStringProperty role;
    private final SimpleStringProperty team_name;

    Player(String name, int height, float weight, int number, String role, String team_name) {
        this.name = new SimpleStringProperty(name);
        this.height = new SimpleIntegerProperty(height);
        this.weight = new SimpleFloatProperty(weight);
        this.number = new SimpleIntegerProperty(number);
        this.role = new SimpleStringProperty(role);
        this.team_name = new SimpleStringProperty(team_name);
    }

    public String getName() {
        return name.get();
    }

    public int getHeight() {
        return height.get();
    }

    public float getWeight() {
        return weight.get();
    }

    public int getNumber() {
        return number.get();
    }

    public String getRole() {
        return role.get();
    }

    public String getTeam_name() {
        return team_name.get();
    }
}
