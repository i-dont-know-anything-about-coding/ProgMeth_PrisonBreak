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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Level2Hallway {

    private final PrisonBreakGame game;
    private final Inventory inventory;
    private Scene scene;
    private HUD hud;
    private GameTimer levelTimer;
    private GameTimer cameraTimer;
    private Label feedbackLabel;
    private Label cameraLabel;

    private boolean hiding        = false;
    private boolean hasScrew      = false;
    private boolean hasCutter     = false;
    private boolean panelUnlocked = false;

    private static final int CAM_INTERVAL = 15;
    private static final int TIME_LIMIT   = 480;

    public Level2Hallway(PrisonBreakGame game) {
        this.game      = game;
        this.inventory = game.getInventory();
        buildScene();
    }

    private void buildScene() {
        hud = new HUD("LEVEL 2 : Hallway");

        feedbackLabel = new Label("หลบกล้องให้ดี...");
        feedbackLabel.setStyle("-fx-text-fill:yellow; -fx-font-size:16;");

        cameraLabel = new Label("CAM : 15");
        cameraLabel.setStyle("-fx-text-fill:red; -fx-font-size:18; -fx-font-weight:bold;");

        Button btnHide     = new Button("ซ่อนในรถเข็น");
        Button btnSearch   = new Button("ค้นรถเข็น");
        Button btnLocker   = new Button("งัดตู้เก็บของ");
        Button btnPanel    = new Button("เปิดตู้ไฟ");
        Button btnRedWire  = new Button("ตัดสายแดง");
        Button btnBlueWire = new Button("ตัดสายน้ำเงิน");
        Button btnSettings = new Button("⚙");

        VBox actions = new VBox(8, cameraLabel, btnHide, btnSearch,
                btnLocker, btnPanel, btnRedWire, btnBlueWire,
                feedbackLabel);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#0f0f1a;");
        root.setTop(hud.getTopBar());
        root.setCenter(actions);

        StackPane bottomLayer = new StackPane(root);
        StackPane.setAlignment(btnSettings, Pos.BOTTOM_LEFT);
        StackPane.setMargin(btnSettings, new Insets(0, 0, 10, 10));
        bottomLayer.getChildren().add(btnSettings);

        scene = new Scene(bottomLayer, 800, 450);
        wireButtons(btnHide, btnSearch, btnLocker, btnPanel,
                btnRedWire, btnBlueWire, btnSettings);
        startTimers();
    }

    private void startTimers() {
        levelTimer = new GameTimer(TIME_LIMIT,
                () -> hud.updateTimer(levelTimer.getFormatted()),
                () -> gameOver());
        levelTimer.start();
        resetCameraTimer();
    }

    private void resetCameraTimer() {
        if (cameraTimer != null) cameraTimer.stop();
        cameraTimer = new GameTimer(CAM_INTERVAL,
                () -> cameraLabel.setText("CAM : " + cameraTimer.getSecondsLeft()),
                () -> { if (!hiding) gameOver(); });
        cameraTimer.start();
    }

    private void wireButtons(Button btnHide, Button btnSearch, Button btnLocker,
                             Button btnPanel, Button btnRed, Button btnBlue,
                             Button btnSettings) {
        btnHide.setOnAction(e -> {
            hiding = true;
            setFeedback("ซ่อนตัวแล้ว...");
            resetCameraTimer();
            new GameTimer(2, null, () -> hiding = false).start();
        });

        btnSearch.setOnAction(e -> {
            if (!hasScrew) {
                hasScrew = true;
                inventory.addItem(new Item("ไขควง", "ไขควงปากแบน"));
                setFeedback("ได้รับ: ไขควง");
            } else setFeedback("ค้นแล้ว");
        });

        btnLocker.setOnAction(e -> {
            if (!inventory.hasItem("ไขควง")) { setFeedback("ต้องการไขควง"); return; }
            if (!hasCutter) {
                hasCutter = true;
                inventory.addItem(new Item("คีมตัดลวด", "คีมแหลมคม"));
                setFeedback("ได้รับ: คีมตัดลวด");
            } else setFeedback("เปิดแล้ว");
        });

        btnPanel.setOnAction(e -> {
            if (!inventory.hasItem("ไขควง")) { setFeedback("ต้องการไขควง"); return; }
            panelUnlocked = true;
            setFeedback("เห็นสายแดงและสายน้ำเงิน!");
        });

        btnRed.setOnAction(e -> {
            if (!panelUnlocked) { setFeedback("ต้องเปิดตู้ไฟก่อน"); return; }
            if (!inventory.hasItem("คีมตัดลวด")) { setFeedback("ต้องการคีมตัดลวด"); return; }
            levelTimer.stop(); cameraTimer.stop();
            game.switchScene(new Level3GuardRoom(game).getScene());
        });

        btnBlue.setOnAction(e -> {
            if (!panelUnlocked) { setFeedback("ต้องเปิดตู้ไฟก่อน"); return; }
            levelTimer.stop(); cameraTimer.stop();
            setFeedback("สัญญาณเตือนดัง! ถูกจับ!");
            gameOver();
        });

        btnSettings.setOnAction(e -> {
            levelTimer.pause(); cameraTimer.pause();
            SettingsPopup.display(game, levelTimer);
        });
    }

    private void setFeedback(String msg) { feedbackLabel.setText(msg); }
    private void gameOver() { game.switchScene(new EndScreen(game, false, 0).getScene()); }
    public Scene getScene() { return scene; }
}
