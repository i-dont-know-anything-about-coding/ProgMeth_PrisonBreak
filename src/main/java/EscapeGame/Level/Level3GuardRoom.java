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

public class Level3GuardRoom {

    private final PrisonBreakGame game;
    private final Inventory inventory;
    private Scene scene;
    private HUD hud;
    private GameTimer timer;
    private Label feedbackLabel;

    private boolean safeKeyFound  = false;
    private boolean safeOpened    = false;
    private boolean notebookTaken = false;

    private static final String SAFE_PASSWORD = "7391";
    private static final int    TIME_LIMIT    = 480;

    public Level3GuardRoom(PrisonBreakGame game) {
        this.game = game; this.inventory = game.getInventory(); buildScene();
    }

    private void buildScene() {
        hud = new HUD("LEVEL 3 : Guard Room");
        feedbackLabel = new Label("สำรวจห้องพักยาม...");
        feedbackLabel.setStyle("-fx-text-fill:yellow; -fx-font-size:16;");

        Button btnArrange  = new Button("เรียงกุญแจบนกระดาน");
        Button btnOpenSafe = new Button("ไขตู้เซฟ");
        Button btnNotebook = new Button("ค่อยๆ ดึงสมุด");
        Button btnExit     = new Button("ออกประตู (ไปด่าน 4)");
        Button btnSettings = new Button("⚙");

        VBox actions = new VBox(10, btnArrange, btnOpenSafe,
                btnNotebook, btnExit, feedbackLabel);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#1a0a00;");
        root.setTop(hud.getTopBar()); root.setCenter(actions);

        StackPane bottomLayer = new StackPane(root);
        StackPane.setAlignment(btnSettings, Pos.BOTTOM_LEFT);
        StackPane.setMargin(btnSettings, new Insets(0, 0, 10, 10));
        bottomLayer.getChildren().add(btnSettings);

        scene = new Scene(bottomLayer, 800, 450);
        wireButtons(btnArrange, btnOpenSafe, btnNotebook, btnExit, btnSettings);

        timer = new GameTimer(TIME_LIMIT,
                () -> hud.updateTimer(timer.getFormatted()),
                () -> gameOver());
        timer.start();
    }

    private void wireButtons(Button btnArrange, Button btnSafe,
                             Button btnNotebook, Button btnExit, Button btnSettings) {
        btnArrange.setOnAction(e -> {
            safeKeyFound = true;
            inventory.addItem(new Item("กุญแจตู้เซฟ", "กุญแจแปลกปลอม"));
            setFeedback("พบ กุญแจตู้เซฟ!");
        });

        btnSafe.setOnAction(e -> {
            if (!safeKeyFound) { setFeedback("ต้องเรียงกุญแจก่อน"); return; }
            if (safeOpened)    { setFeedback("เปิดแล้ว"); return; }
            safeOpened = true;
            inventory.addItem(new Item("KeyCard", "บัตรผ่านประตู"));
            setFeedback("ได้รับ: KeyCard!");
        });

        btnNotebook.setOnAction(e -> {
            if (notebookTaken) { setFeedback("ดูแล้ว"); return; }
            notebookTaken = true;
            inventory.addItem(new Item("รหัสผ่าน", SAFE_PASSWORD));
            setFeedback("ได้รหัสผ่าน: " + SAFE_PASSWORD);
        });

        btnExit.setOnAction(e -> {
            if (!inventory.hasItem("KeyCard"))   { setFeedback("ต้องการ KeyCard"); return; }
            if (!inventory.hasItem("รหัสผ่าน")) { setFeedback("ต้องการรหัสผ่าน"); return; }
            timer.stop();
            game.switchScene(new Level4Gate(game).getScene());
        });

        btnSettings.setOnAction(e -> { timer.pause(); SettingsPopup.display(game, timer); });
    }

    private void setFeedback(String msg) { feedbackLabel.setText(msg); }
    private void gameOver() { game.switchScene(new EndScreen(game, false, 0).getScene()); }
    public Scene getScene() { return scene; }
}
