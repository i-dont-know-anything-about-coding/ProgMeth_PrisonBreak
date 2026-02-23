package EscapeGame.Level;

import EscapeGame.PrisonBreakGame;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class EndScreen {

    private PrisonBreakGame game;
    private Scene scene;

    public EndScreen(PrisonBreakGame game, boolean isWin, int timeUsed) {
        this.game = game;
        buildScene(isWin, timeUsed);
    }

    private void buildScene(boolean isWin, int timeUsed) {
        Label titleLabel = new Label(
                isWin ? "ยินดีด้วย! หลบหนีสำเร็จ!" : "เสียใจด้วย... หลบหนีไม่สำเร็จ");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(isWin ? Color.GOLD : Color.RED);

        Label detailLabel = new Label(
                isWin ? String.format("ใช้เวลาทั้งหมด %02d:%02d", timeUsed/60, timeUsed%60)
                        : "คุณถูกย้ายไปขังที่แน่นกว่าเดิม...");
        detailLabel.setFont(Font.font("Arial", 20));
        detailLabel.setTextFill(Color.WHITE);

        Button btnPlayAgain = new Button("เล่นอีกครั้ง");
        Button btnMainMenu  = new Button("หน้าหลัก");

        styleBtn(btnPlayAgain, isWin ? "#16a34a" : "#dc2626");
        styleBtn(btnMainMenu, "#374151");

        btnPlayAgain.setOnAction(e -> game.resetGame());
        btnMainMenu.setOnAction(e -> game.resetGame());

        HBox btnBox = new HBox(20, btnPlayAgain, btnMainMenu);
        btnBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(30, titleLabel, detailLabel, btnBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:" +
                (isWin ? "#052e16" : "#450a0a") + ";");

        scene = new Scene(root, 800, 450);
    }

    private void styleBtn(Button btn, String color) {
        btn.setStyle("-fx-background-color:" + color + "; -fx-text-fill:white;" +
                "-fx-font-size:18; -fx-font-weight:bold;" +
                "-fx-background-radius:15; -fx-pref-width:180; -fx-pref-height:50;");
    }

    public Scene getScene() { return scene; }
}
