package EscapeGame.Gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class HUD {

    private Label timerLabel;
    private HBox  topBar;

    public HUD(String levelName) {
        Label levelLabel = new Label(levelName);
        levelLabel.setStyle(
                "-fx-font-size:20; -fx-font-weight:bold; -fx-text-fill:white;");

        timerLabel = new Label("TIME LEFT : 10 : 00");
        timerLabel.setStyle(
                "-fx-font-size:20; -fx-font-weight:bold; -fx-text-fill:white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar = new HBox(levelLabel, spacer, timerLabel);
        topBar.setStyle(
                "-fx-background-color:rgba(0,0,0,0.7); -fx-padding:10 20 10 20;");
    }

    public void updateTimer(String formatted) {
        timerLabel.setText("TIME LEFT : " + formatted);
    }

    public HBox getTopBar() { return topBar; }
}
