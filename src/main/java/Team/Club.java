package Team;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

public class Club {

    private final SimpleStringProperty name;
    private final SimpleStringProperty trainer;
    private final SimpleStringProperty liga;
    private final SimpleStringProperty stadium;
    private final SimpleFloatProperty rating;

    Club(String name, String trainer, String liga, String stadium, float rating) {
        this.name = new SimpleStringProperty(name);
        this.trainer = new SimpleStringProperty(trainer);
        this.liga = new SimpleStringProperty(liga);
        this.stadium = new SimpleStringProperty(stadium);
        this.rating = new SimpleFloatProperty(rating);
    }

    public String getName() {
        return name.get();
    }

    public String getTrainer() {
        return trainer.get();
    }

    public String getLiga() {
        return liga.get();
    }

    public String getStadium() {
        return stadium.get();
    }

    public float getRating() {
        return rating.get();
    }
}