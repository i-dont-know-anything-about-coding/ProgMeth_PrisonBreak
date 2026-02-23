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

public class Level4Gate {

    private final PrisonBreakGame game;
    private final Inventory inventory;
    private Scene scene;
    private HUD hud;
    private GameTimer timer;
    private Label feedbackLabel;

    private boolean hasCostume  = false;
    private boolean radioOpened = false;
    private String  dailyCode;

    private static final String[] CODE_POOL =
            {"ALPHA", "DELTA", "OMEGA", "BRAVO"};
    private static final int TIME_LIMIT = 300;

    public Level4Gate(PrisonBreakGame game) {
        this.game      = game;
        this.inventory = game.getInventory();
        dailyCode = CODE_POOL[(int)(Math.random() * CODE_POOL.length)];
        buildScene();
    }

    private void buildScene() {
        hud = new HUD("LEVEL 4 : Main Gate");
        feedbackLabel = new Label("สำรวจบริเวณประตูใหญ่...");
        feedbackLabel.setStyle("-fx-text-fill:yellow; -fx-font-size:16;");

        Button btnFind     = new Button("หาชุดยาม");
        Button btnWear     = new Button("สวมชุดยาม");
        Button btnRadio    = new Button("เปิดวิทยุสื่อสาร");
        Button btnExit     = new Button("ออกประตู!");
        Button btnSettings = new Button("⚙");

        VBox actions = new VBox(10, btnFind, btnWear, btnRadio, btnExit, feedbackLabel);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#0d1b2a;");
        root.setTop(hud.getTopBar()); root.setCenter(actions);

        StackPane bottomLayer = new StackPane(root);
        StackPane.setAlignment(btnSettings, Pos.BOTTOM_LEFT);
        StackPane.setMargin(btnSettings, new Insets(0, 0, 10, 10));
        bottomLayer.getChildren().add(btnSettings);

        scene = new Scene(bottomLayer, 800, 450);
        wireButtons(btnFind, btnWear, btnRadio, btnExit, btnSettings);

        timer = new GameTimer(TIME_LIMIT,
                () -> hud.updateTimer(timer.getFormatted()),
                () -> gameOver());
        timer.start();
    }

    private void wireButtons(Button btnFind, Button btnWear, Button btnRadio,
                             Button btnExit, Button btnSettings) {
        btnFind.setOnAction(e -> {
            inventory.addItem(new Item("ชุดยาม", "เครื่องแบบยามคุก"));
            setFeedback("ได้รับ: ชุดยาม");
        });

        btnWear.setOnAction(e -> {
            if (!inventory.hasItem("ชุดยาม")) { setFeedback("ต้องหาชุดก่อน"); return; }
            hasCostume = true;
            setFeedback("สวมชุดยามเรียบร้อย!");
        });

        btnRadio.setOnAction(e -> {
            radioOpened = true;
            setFeedback("วิทยุ: รหัสประจำวันคือ \"" + dailyCode + "\"");
        });

        btnExit.setOnAction(e -> {
            if (!hasCostume) {
                setFeedback("ยามเห็นว่าไม่ใส่ชุด! ถูกจับ!");
                timer.stop(); gameOver(); return;
            }
            if (!radioOpened) { setFeedback("ต้องรู้รหัสลับก่อน"); return; }
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("ยามถาม");
            dialog.setHeaderText("ยาม: รหัสลับประจำวัน?");
            dialog.setContentText("รหัส:");
            dialog.showAndWait().ifPresent(input -> {
                if (input.equalsIgnoreCase(dailyCode)) {
                    timer.stop();
                    int timeUsed = TIME_LIMIT - timer.getSecondsLeft();
                    game.switchScene(new EndScreen(game, true, timeUsed).getScene());
                } else {
                    timer.stop();
                    setFeedback("รหัสผิด! ถูกจับ!");
                    gameOver();
                }
            });
        });

        btnSettings.setOnAction(e -> { timer.pause(); SettingsPopup.display(game, timer); });
    }

    private void setFeedback(String msg) { feedbackLabel.setText(msg); }
    private void gameOver() { game.switchScene(new EndScreen(game, false, 0).getScene()); }
    public Scene getScene() { return scene; }
}
