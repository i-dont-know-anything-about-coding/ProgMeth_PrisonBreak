package EscapeGame.Gui;

import EscapeGame.Logic.GameTimer;
import EscapeGame.PrisonBreakGame;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsPopup {

    public static void display(PrisonBreakGame game, GameTimer timer) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Settings");

        Label lblVolume = new Label("Volume");
        lblVolume.setStyle("-fx-text-fill:white; -fx-font-size:16;");

        Slider sliderVolume = new Slider(0, 100, 70);
        sliderVolume.setShowTickLabels(true);
        sliderVolume.setShowTickMarks(true);

        Button btnResume   = new Button("กลับเกม");
        Button btnMainMenu = new Button("หน้าหลัก");
        Button btnExit     = new Button("ออกจากโปรแกรม");

        styleBtn(btnResume,   "#16a34a");
        styleBtn(btnMainMenu, "#374151");
        styleBtn(btnExit,     "#dc2626");

        btnResume.setOnAction(e -> {
            if (timer != null) timer.resume();
            popup.close();
        });
        btnMainMenu.setOnAction(e -> {
            if (timer != null) timer.stop();
            popup.close();
            game.resetGame();
        });
        btnExit.setOnAction(e -> Platform.exit());

        VBox root = new VBox(15, lblVolume, sliderVolume,
                btnResume, btnMainMenu, btnExit);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color:#1f2937;");

        popup.setScene(new Scene(root, 300, 300));
        popup.showAndWait();
    }

    private static void styleBtn(Button btn, String color) {
        btn.setStyle("-fx-background-color:" + color + "; -fx-text-fill:white;" +
                "-fx-font-size:15; -fx-background-radius:10; -fx-pref-width:200;");
    }
}
