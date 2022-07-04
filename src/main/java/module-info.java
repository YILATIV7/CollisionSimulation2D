module com.example.collision_sim_2d {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.collision_sim_2d to javafx.fxml;
    exports com.example.collision_sim_2d;
}