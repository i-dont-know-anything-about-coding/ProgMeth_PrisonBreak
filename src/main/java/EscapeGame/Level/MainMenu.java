package EscapeGame.Level;

import EscapeGame.Gui.PopupBox;
import EscapeGame.PrisonBreakGame;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.animation.*;
import javafx.util.Duration;

public class MainMenu {

    private Scene scene;
    private PrisonBreakGame game;
    private MediaPlayer mediaPlayer;

    public MainMenu(PrisonBreakGame game) {
        this.game = game;
        createUI();
    }

    private void createUI() {

        // ── Background Video ──
        MediaView bgView = new MediaView();
        try {
            String videoPath = getClass().getResource("/Prison_Break.mp4").toExternalForm();
            Media media = new Media(videoPath);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgView.setMediaPlayer(mediaPlayer);
            bgView.setFitWidth(800);
            bgView.setFitHeight(450);
            bgView.setPreserveRatio(false);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("หาวิดีโอไม่เจอ: " + e.getMessage());
        }

        // ── Overlay — จางมากๆ แค่ให้อ่านง่าย ──
        Rectangle overlay = new Rectangle(800, 450);
        overlay.setFill(Color.rgb(5, 5, 8, 0.22));

        // ── Prison Bars — เทาเข้มกลมกลืนกับคอนกรีต ──
        Pane barsPane = new Pane();
        barsPane.setMouseTransparent(true);

        // แนวนอนบน-ล่าง — เหล็กเทาเข้ม
        Line hTop = new Line(0, 22, 800, 22);
        Line hBot = new Line(0, 428, 800, 428);
        for (Line h : new Line[]{hTop, hBot}) {
            h.setStroke(Color.rgb(38, 38, 42, 0.90));
            h.setStrokeWidth(22);
        }
        barsPane.getChildren().addAll(hTop, hBot);

        int barCount = 11;
        double spacing = 800.0 / barCount;
        for (int i = 0; i <= barCount; i++) {
            double x = i * spacing;

            // เงา
            Line shadow = new Line(x - 4, 0, x - 4, 450);
            shadow.setStroke(Color.rgb(0, 0, 0, 0.60));
            shadow.setStrokeWidth(8);

            // ตัวบาร์ — เทาเข้มอมน้ำเงินนิดๆ เหมือนเหล็กกล้า
            Line bar = new Line(x, 0, x, 450);
            bar.setStroke(Color.rgb(52, 54, 60, 0.72));
            bar.setStrokeWidth(14);

            // ไฮไลต์สะท้อนแสง — ขาวจางมากๆ
            Line shine = new Line(x + 5, 0, x + 5, 450);
            shine.setStroke(Color.rgb(220, 225, 235, 0.12));
            shine.setStrokeWidth(4);

            // Shake เบาๆ
            TranslateTransition tt = new TranslateTransition(
                    Duration.millis(2200 + i * 130), bar);
            tt.setByX(i % 2 == 0 ? 1.2 : -1.2);
            tt.setAutoReverse(true);
            tt.setCycleCount(TranslateTransition.INDEFINITE);
            tt.play();

            barsPane.getChildren().addAll(shadow, bar, shine);
        }

        // ── TITLE — Black Ops One + Flicker ──
        // ต้องโหลดฟอนต์ก่อน: Font.loadFont(..., 14) ใน PrisonBreakGame.java
        Label titleLabel = new Label("PRISON\nBREAK");
        titleLabel.setStyle(
                "-fx-font-family: 'Black Ops One';" +
                        "-fx-font-size: 86px;" +
                        "-fx-text-fill: #DCDFE8;" +          // ขาวเทาเย็น กลมกลืนกับคอนกรีต
                        "-fx-effect: " +
                        "dropshadow(gaussian, #000000, 14, 0.95, 4, 4)," +  // เงาหนัก
                        "dropshadow(gaussian, #CC2222, 28, 0.35, 0, 0);" +  // glow แดงจางๆ รอบนอก
                        "-fx-line-spacing: -12;"
        );

        // Flicker — ไฟกระชากในทางเดินมืด
        Timeline flicker = new Timeline(
                new KeyFrame(Duration.millis(0),    e -> titleLabel.setOpacity(1.00)),
                new KeyFrame(Duration.millis(80),   e -> titleLabel.setOpacity(0.78)),
                new KeyFrame(Duration.millis(120),  e -> titleLabel.setOpacity(1.00)),
                new KeyFrame(Duration.millis(140),  e -> titleLabel.setOpacity(0.35)),  // กระชากแรง
                new KeyFrame(Duration.millis(180),  e -> titleLabel.setOpacity(1.00)),
                new KeyFrame(Duration.millis(1200), e -> titleLabel.setOpacity(1.00)),
                new KeyFrame(Duration.millis(1230), e -> titleLabel.setOpacity(0.88)),
                new KeyFrame(Duration.millis(1260), e -> titleLabel.setOpacity(1.00)),
                new KeyFrame(Duration.millis(3000), e -> titleLabel.setOpacity(1.00)),
                new KeyFrame(Duration.millis(3020), e -> titleLabel.setOpacity(0.20)),  // กระชากแรงมาก
                new KeyFrame(Duration.millis(3060), e -> titleLabel.setOpacity(1.00)),
                new KeyFrame(Duration.millis(3080), e -> titleLabel.setOpacity(0.65)),
                new KeyFrame(Duration.millis(3120), e -> titleLabel.setOpacity(1.00)),
                new KeyFrame(Duration.millis(4800), e -> titleLabel.setOpacity(1.00))
        );
        flicker.setCycleCount(Timeline.INDEFINITE);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(700), titleLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setOnFinished(e -> flicker.play());
        fadeIn.play();

        // ── Subtitle ──
        Label subtitle = new Label("⛓  C A N   Y O U   E S C A P E ?  ⛓");
        subtitle.setStyle(
                "-fx-font-family: 'Oswald';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: 300;" +
                        "-fx-text-fill: #8A8F9E;" +          // เทาอมน้ำเงิน
                        "-fx-effect: dropshadow(gaussian, #000000, 5, 0.95, 1, 1);"
        );

        // ── Prisoner ID badge ──
        Label badge = new Label("INMATE  #00001");
        badge.setStyle(
                "-fx-font-family: 'Courier New';" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #CC2222;" +          // แดงเลือดหมู
                        "-fx-border-color: #CC2222;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 2;" +
                        "-fx-padding: 3 8 3 8;" +
                        "-fx-background-color: rgba(180,0,0,0.10);" +
                        "-fx-background-radius: 2;" +
                        "-fx-effect: dropshadow(gaussian, #000000, 5, 0.9, 1, 1);" +
                        "-fx-rotate: -8;"
        );

        VBox titleBox = new VBox(8, titleLabel, subtitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        // ── Buttons ──
        VBox buttonBox = new VBox(9);
        buttonBox.setAlignment(Pos.CENTER);

        // Start — แดงเลือดหมูเข้ม โดดออกมาเป็น CTA
        Button btnStart     = createMenuButton("▶   START GAME",  "#7A1515", "#550E0E", true);
        Button btnCharacter = createMenuButton("👤   Character",   "#252830", "#181A20", false);
        Button btnHowToPlay = createMenuButton("📖   How To Play", "#252830", "#181A20", false);
        Button btnAboutUs   = createMenuButton("ℹ   About Us",    "#252830", "#181A20", false);
        Button btnExit      = createMenuButton("✖   Exit",         "#1A1A1A", "#101010", false);

        buttonBox.getChildren().addAll(
                btnStart, btnCharacter, btnHowToPlay, btnAboutUs, btnExit);

        // ── Panel — เหมือนแผ่นเหล็กติดผนัง ──
        Rectangle panel = new Rectangle(248, 348);
        panel.setArcWidth(4);
        panel.setArcHeight(4);
        panel.setFill(Color.rgb(12, 13, 16, 0.72));
        panel.setStroke(Color.rgb(55, 58, 68, 0.80));  // ขอบเหล็กเทาเข้ม
        panel.setStrokeWidth(1.5);

        // เส้นขอบด้านบนสีแดงบางๆ เหมือน warning strip
        Rectangle topStrip = new Rectangle(248, 4);
        topStrip.setArcWidth(4);
        topStrip.setArcHeight(4);
        topStrip.setFill(Color.rgb(180, 20, 20, 0.85));
        topStrip.setTranslateY(-172); // ชิดบนสุดของ panel

        Label panelHeader = new Label("— SELECT —");
        panelHeader.setStyle(
                "-fx-font-family: 'Oswald';" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: 400;" +
                        "-fx-text-fill: #55585F;" +
                        "-fx-padding: 0 0 6 0;"
        );

        VBox panelContent = new VBox(6, panelHeader, buttonBox);
        panelContent.setAlignment(Pos.CENTER);

        StackPane buttonPane = new StackPane(panel, topStrip, panelContent);

        // ── Layout ──
        AnchorPane layout = new AnchorPane();
        layout.getChildren().addAll(titleBox, badge, buttonPane);

        AnchorPane.setLeftAnchor(titleBox, 48.0);
        AnchorPane.setTopAnchor(titleBox, 90.0);

        AnchorPane.setLeftAnchor(badge, 62.0);
        AnchorPane.setTopAnchor(badge, 310.0);

        AnchorPane.setRightAnchor(buttonPane, 35.0);
        AnchorPane.setTopAnchor(buttonPane, 35.0);
        AnchorPane.setBottomAnchor(buttonPane, 35.0);

        // ── Root ──
        StackPane root = new StackPane();
        root.getChildren().addAll(bgView, overlay, barsPane, layout);
        scene = new Scene(root, 800, 450);

        // ── Button Actions ──
        btnStart.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.stop();
            Level1Jail level1 = new Level1Jail(game);
            game.switchScene(level1.getScene());
        });
        btnCharacter.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog(game.getPlayerName());
            dialog.setTitle("Character");
            dialog.setHeaderText("ตั้งชื่อตัวละคร");
            dialog.setContentText("ชื่อ:");
            dialog.showAndWait().ifPresent(name -> {
                if (!name.isBlank()) game.setPlayerName(name.trim());
            });
        });
        btnHowToPlay.setOnAction(e -> PopupBox.display("How to Play",
                "วิธีการเล่น:\n1. คลิกปุ่มเพื่อสำรวจ\n2. รวบรวมไอเทม\n3. แข่งกับเวลา!"));
        btnAboutUs.setOnAction(e -> PopupBox.display("About Us",
                "พัฒนาโดย: นิสิต\nใช้ JavaFX และ OOP"));
        btnExit.setOnAction(e -> Platform.exit());
    }

    private Button createMenuButton(String text, String colorFrom, String colorTo,
                                    boolean isAccent) {
        Button btn = new Button(text);

        // สีตัวอักษร: แดงสว่างสำหรับ START, เทาขาวสำหรับปุ่มทั่วไป
        String textColor   = isAccent ? "#F0A0A0" : "#B8BCC8";
        String hoverText   = isAccent ? "#FFFFFF"  : "#FFFFFF";
        String hoverBorder = isAccent ? "#CC2222"  : "#6A6E80";
        String hoverGlow   = isAccent
                ? "dropshadow(gaussian, #AA0000, 20, 0.55, 0, 0)"
                : "dropshadow(gaussian, #3A3D4A, 14, 0.45, 0, 0)";

        String base = String.format(
                "-fx-background-color: linear-gradient(to bottom, %s, %s);" +
                        "-fx-text-fill: %s;" +
                        "-fx-font-family: 'Oswald';" +
                        "-fx-font-size: 17px;" +
                        "-fx-font-weight: 500;" +
                        "-fx-letter-spacing: 1;" +
                        "-fx-background-radius: 3;" +
                        "-fx-pref-width: 210px;" +
                        "-fx-pref-height: 43px;" +
                        "-fx-border-color: rgba(80,83,95,0.50);" +
                        "-fx-border-radius: 3;" +
                        "-fx-border-width: 1;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.95), 6, 0.7, 0, 2);",
                colorFrom, colorTo, textColor
        );

        String hover = String.format(
                "-fx-background-color: linear-gradient(to bottom, %s, %s);" +
                        "-fx-text-fill: %s;" +
                        "-fx-font-family: 'Oswald';" +
                        "-fx-font-size: 17px;" +
                        "-fx-font-weight: 500;" +
                        "-fx-letter-spacing: 1;" +
                        "-fx-background-radius: 3;" +
                        "-fx-pref-width: 210px;" +
                        "-fx-pref-height: 43px;" +
                        "-fx-border-color: %s;" +
                        "-fx-border-radius: 3;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: %s;",
                colorFrom, colorTo, hoverText, hoverBorder, hoverGlow
        );

        btn.setStyle(base);

        ScaleTransition up   = new ScaleTransition(Duration.millis(100), btn);
        up.setToX(1.04); up.setToY(1.04);
        ScaleTransition down = new ScaleTransition(Duration.millis(100), btn);
        down.setToX(1.0); down.setToY(1.0);

        btn.setOnMouseEntered(e -> { btn.setStyle(hover); up.playFromStart(); });
        btn.setOnMouseExited(e  -> { btn.setStyle(base);  down.playFromStart(); });

        return btn;
    }

    public Scene getScene() { return scene; }
}