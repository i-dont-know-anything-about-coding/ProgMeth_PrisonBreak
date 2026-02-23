package EscapeGame.Level;

import EscapeGame.Gui.HUD;
import EscapeGame.Gui.SettingsPopup;
import EscapeGame.Logic.GameTimer;
import EscapeGame.Logic.Inventory;
import EscapeGame.Model.Item;
import EscapeGame.PrisonBreakGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Level1Jail {

    private final PrisonBreakGame game;
    private final Inventory inventory;
    private Scene scene;
    private HUD hud;
    private GameTimer timer;
    private Label feedbackLabel;

    private boolean hasSoap      = false;
    private boolean hasSpoon     = false;
    private boolean hasBlueprint = false;
    private boolean hasSoapKey   = false;
    private boolean panelOpened  = false;

    private static final String SECRET_CODE = "4927";
    private static final int    TIME_LIMIT   = 600;

    public Level1Jail(PrisonBreakGame game) {
        this.game      = game;
        this.inventory = game.getInventory();
        buildScene();
    }

    private void buildScene() {
        hud = new HUD("LEVEL 1 : Jail");

        feedbackLabel = new Label("สำรวจห้องเพื่อหาเบาะแส...");
        feedbackLabel.setStyle(
                "-fx-text-fill:yellow; -fx-font-size:16; -fx-font-weight:bold;");

        Label playerLabel = new Label(game.getPlayerName());
        playerLabel.setStyle("-fx-text-fill:white; -fx-font-size:14;");

        Button btnSink     = new Button("อ่างล้างมือ");
        Button btnBed      = new Button("เตียง");
        Button btnUnderBed = new Button("ใต้เตียง");
        Button btnToilet   = new Button("ส้วม (คราฟต์กุญแจ)");
        Button btnWall     = new Button("กำแพง");
        Button btnBars     = new Button("ซี่กรง (ออกด่าน)");
        Button btnSettings = new Button("⚙");

        VBox actions = new VBox(10, playerLabel, btnSink, btnBed, btnUnderBed,
                btnToilet, btnWall, btnBars, feedbackLabel);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#1a1a2e;");
        root.setTop(hud.getTopBar());
        root.setCenter(actions);

        StackPane bottomLayer = new StackPane(root);
        StackPane.setAlignment(btnSettings, Pos.BOTTOM_LEFT);
        StackPane.setMargin(btnSettings, new Insets(0, 0, 10, 10));
        bottomLayer.getChildren().add(btnSettings);

        scene = new Scene(bottomLayer, 800, 450);
        wireButtons(btnSink, btnBed, btnUnderBed, btnToilet,
                btnWall, btnBars, btnSettings);

        timer = new GameTimer(TIME_LIMIT,
                () -> hud.updateTimer(timer.getFormatted()),
                () -> gameOver());
        timer.start();
    }

    private void wireButtons(Button btnSink, Button btnBed, Button btnUnderBed,
                             Button btnToilet, Button btnWall, Button btnBars,
                             Button btnSettings) {
        btnSink.setOnAction(e -> {
            if (!hasSoap) {
                hasSoap = true;
                inventory.addItem(new Item("สบู่", "สบู่ก้อนหนึ่ง"));
                setFeedback("ได้รับ: สบู่");
            } else setFeedback("ไม่มีอะไรเพิ่มแล้ว");
        });

        btnBed.setOnAction(e -> {
            if (!hasSpoon) {
                hasSpoon = true;
                inventory.addItem(new Item("ช้อน", "ช้อนโลหะ"));
                setFeedback("ได้รับ: ช้อน");
            } else setFeedback("ไม่มีอะไรเพิ่มแล้ว");
        });

        btnUnderBed.setOnAction(e -> {
            if (!hasBlueprint) {
                hasBlueprint = true;
                inventory.addItem(new Item("พิมพ์เขียว", "แบบแผนกุญแจ"));
                setFeedback("ได้รับ: พิมพ์เขียว");
            } else setFeedback("ไม่มีอะไรเพิ่มแล้ว");
        });

        btnToilet.setOnAction(e -> {
            if (hasSoap && hasSpoon && hasBlueprint && !hasSoapKey) {
                hasSoapKey = true;
                inventory.addItem(new Item("กุญแจสบู่", "กุญแจแกะสลักจากสบู่"));
                setFeedback("คราฟต์สำเร็จ! ได้รับ: กุญแจสบู่");
            } else if (hasSoapKey) {
                setFeedback("คราฟต์แล้ว");
            } else {
                setFeedback("ต้องการ: สบู่ + ช้อน + พิมพ์เขียว");
            }
        });

        btnWall.setOnAction(e -> setFeedback("บนกำแพงมีรอยขีด... '4'  '9'  '2'  '7'"));

        btnBars.setOnAction(e -> {
            if (!inventory.hasItem("กุญแจสบู่")) {
                setFeedback("ต้องการกุญแจสบู่ก่อน"); return;
            }
            if (panelOpened) { setFeedback("ออกไปแล้ว"); return; }
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("กุญแจชั้นที่ 2");
            dialog.setHeaderText("ใส่รหัส 4 หลัก");
            dialog.setContentText("รหัส:");
            dialog.showAndWait().ifPresent(input -> {
                if (input.equals(SECRET_CODE)) {
                    panelOpened = true;
                    timer.stop();
                    Level2Hallway level2 = new Level2Hallway(game);
                    game.switchScene(level2.getScene());
                } else setFeedback("รหัสผิด! ลองใหม่");
            });
        });

        btnSettings.setOnAction(e -> {
            timer.pause();
            SettingsPopup.display(game, timer);
        });
    }

    private void setFeedback(String msg) { feedbackLabel.setText(msg); }

    private void gameOver() {
        game.switchScene(new EndScreen(game, false, 0).getScene());
    }

    public Scene getScene() { return scene; }
}
