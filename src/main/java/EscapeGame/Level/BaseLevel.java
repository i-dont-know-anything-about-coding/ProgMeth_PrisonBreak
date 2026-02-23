package EscapeGame.Level;

import EscapeGame.Gui.PopupBox;
import EscapeGame.PrisonBreakGame;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.net.URL;

/**
 * คลาสแม่สำหรับทุกด่านในเกม
 * จัดการเรื่อง Layout พื้นฐาน และระบบนับเวลาถอยหลัง
 */
public abstract class BaseLevel {

    protected PrisonBreakGame game;
    protected Scene scene;
    protected TextArea storyText; // กล่องข้อความบรรยายเนื้อเรื่อง
    protected GridPane buttonGrid; // ที่วางปุ่มคำสั่งต่างๆ
    protected VBox centerArea;
    // ระบบเวลา
    private int timeSeconds;
    private Label timeLabel;
    protected boolean isGameRunning = true;

    public BaseLevel(PrisonBreakGame game, String levelTitle, String bgImagePath, int timeLimitMinutes) {
        this.game = game;
        this.timeSeconds = timeLimitMinutes * 60;

        // 1. สร้างพื้นหลัง
        ImageView bgView = new ImageView();
        try {
            URL imageUrl = getClass().getResource("/images/" + bgImagePath);
            if (imageUrl != null) {
                bgView.setImage(new Image(imageUrl.toString()));
                bgView.setFitWidth(800);
                bgView.setFitHeight(600);
            }
        } catch (Exception e) {
            System.err.println("ไม่พบภาพพื้นหลังด่าน: " + bgImagePath);
        }

        // 2. โครงสร้างหลัก (BorderPane)
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        // --- ส่วนบน (Top): ชื่อด่าน และ เวลา ---
        Label levelLabel = new Label(levelTitle);
        levelLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 22));
        levelLabel.setTextFill(Color.WHITE);

        timeLabel = new Label("TIME LEFT : " + formatTime(timeSeconds));
        timeLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 22));
        timeLabel.setTextFill(Color.RED);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(levelLabel, spacer, timeLabel);
        mainLayout.setTop(topBar);

        // --- ส่วนกลาง (Center): ชื่อผู้เล่น, TextArea, ปุ่ม ---
        centerArea = new VBox(15);
        centerArea.setAlignment(Pos.CENTER);
        centerArea.setPadding(new Insets(20, 100, 20, 100));

        Label playerName = new Label("PLAYER: นักโทษหมายเลข 404");
        playerName.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        playerName.setTextFill(Color.YELLOW);

        storyText = new TextArea();
        storyText.setEditable(false);
        storyText.setWrapText(true);
        storyText.setPrefHeight(150);
        storyText.setStyle("-fx-control-inner-background: black; -fx-text-fill: white; -fx-font-family: 'Tahoma';");

        buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);
        buttonGrid.setAlignment(Pos.CENTER);

        centerArea.getChildren().addAll(playerName, storyText, buttonGrid);
        mainLayout.setCenter(centerArea);

        // --- ส่วนล่าง (Bottom): ปุ่ม Setting ---
        Button btnSetting = new Button("⚙ SETTING");
        btnSetting.setStyle("-fx-background-color: #444; -fx-text-fill: white;");
        btnSetting.setOnAction(e -> PopupBox.display("Settings", "เมนูตั้งค่า\n- ปรับระดับเสียง\n- ออกจากเกม"));

        HBox bottomBar = new HBox(btnSetting);
        bottomBar.setAlignment(Pos.BOTTOM_LEFT);
        mainLayout.setBottom(bottomBar);

        // รวมทุกอย่าง
        StackPane root = new StackPane(bgView, mainLayout);
        scene = new Scene(root, 800, 600);

        startTimer();
        setupLevel(); // เรียกให้คลาสลูกใส่ปุ่มและเนื้อเรื่องเอง
    }

    private void startTimer() {
        Thread timerThread = new Thread(() -> {
            while (isGameRunning && timeSeconds > 0) {
                try {
                    Thread.sleep(1000);
                    timeSeconds--;
                    Platform.runLater(() -> {
                        timeLabel.setText("TIME LEFT : " + formatTime(timeSeconds));
                        if (timeSeconds <= 0) {
                            storyText.setText("🚨 หมดเวลา! คุณถูกผู้คุมจับได้ GAME OVER");
                            buttonGrid.setDisable(true);
                        }
                    });
                } catch (InterruptedException e) { break; }
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    private String formatTime(int sec) {
        return String.format("%02d:%02d", sec / 60, sec % 60);
    }

    // เมธอดช่วยเหลือให้คลาสลูกใช้ง่ายๆ
    protected void log(String msg) {
        storyText.appendText("\n> " + msg);
    }

    protected Button createButton(String text, int col, int row) {
        Button btn = new Button(text);
        btn.setPrefSize(180, 40);
        btn.setOpacity(0.7);
        btn.setOnMouseEntered(e -> btn.setOpacity(1.0));
        btn.setOnMouseExited(e -> btn.setOpacity(0.7));
        buttonGrid.add(btn, col, row);
        return btn;
    }

    public Scene getScene() { return scene; }

    // คลาสลูกต้องเอาไปเขียนเองว่าด่านนั้นมีปุ่มอะไรบ้าง
    protected abstract void setupLevel();
}