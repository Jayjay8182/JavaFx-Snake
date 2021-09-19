module com.game.snakegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.game.snakegame to javafx.fxml;
    exports com.game.snakegame;
}