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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.net.URL;

public class MainMenu {

    private Scene scene;
    private PrisonBreakGame game;
    private MediaPlayer mediaPlayer;

    public MainMenu(PrisonBreakGame game) {
        this.game = game;
        createUI();
    }

    private void createUI() {
        MediaView bgView = new MediaView();
        try {
            // ดึงไฟล์ mp4 จากโฟลเดอร์ resources/videos/
            String videoPath = getClass().getResource("/Prison_Break.mp4").toExternalForm();
            Media media = new Media(videoPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            // สั่งให้วิดีโอเล่นวนซ้ำไปเรื่อยๆ (Loop)
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            bgView.setMediaPlayer(mediaPlayer);

            // ตั้งขนาดวิดีโอให้เต็มจอพอดี (สมมติจอ 800x450)
            bgView.setFitWidth(800);
            bgView.setFitHeight(450);
            bgView.setPreserveRatio(false); // ปิดการรักษาสัดส่วน เพื่อให้ยืดเต็มจอ

            // สั่งเล่นวิดีโอ
            mediaPlayer.play();

        } catch (Exception e) {
            System.err.println("หาวิดีโอไม่เจอ หรือไฟล์ไม่รองรับ: " + e.getMessage());
        }

        Label titleLabel = new Label("PRISON\nBREAK");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 70));
        titleLabel.setTextFill(Color.WHITE);

        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnStart = createMenuButton("start");
        Button btnCharacter = createMenuButton("character");
        Button btnHowToPlay = createMenuButton("how to play");
        Button btnAboutUs = createMenuButton("about us");
        Button btnExit = createMenuButton("exit");

        buttonBox.getChildren().addAll(btnStart, btnCharacter, btnHowToPlay, btnAboutUs, btnExit);

        AnchorPane layout = new AnchorPane();
        layout.getChildren().addAll(titleLabel, buttonBox);
        AnchorPane.setLeftAnchor(titleLabel, 50.0);
        AnchorPane.setTopAnchor(titleLabel, 120.0);
        AnchorPane.setRightAnchor(buttonBox, 50.0);
        AnchorPane.setTopAnchor(buttonBox, 50.0);
        AnchorPane.setBottomAnchor(buttonBox, 50.0);

        StackPane root = new StackPane();
        root.getChildren().addAll(bgView, layout);
        scene = new Scene(root, 800, 450);

        // ── btnStart: หยุดวิดีโอ แล้วไป Level 1 ──
        btnStart.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.stop();
            Level1Jail level1 = new Level1Jail(game);
            game.switchScene(level1.getScene());
        });

        // ── character: รับชื่อผู้เล่น ──
        btnCharacter.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog(game.getPlayerName());
            dialog.setTitle("Character");
            dialog.setHeaderText("ตั้งชื่อตัวละคร");
            dialog.setContentText("ชื่อ:");
            dialog.showAndWait().ifPresent(name -> {
                if (!name.isBlank()) game.setPlayerName(name.trim());
            });
        });

        btnHowToPlay.setOnAction(e -> PopupBox.display("How to Play", "วิธีการเล่น:\n1. คลิกปุ่มเพื่อสำรวจ\n2. รวบรวมไอเทม\n3. แข่งกับเวลา!"));
        btnAboutUs.setOnAction(e -> PopupBox.display("About Us", "พัฒนาโดย: นิสิต\nใช้ JavaFX และ OOP"));
        btnExit.setOnAction(e -> Platform.exit());
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);

        btn.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 25; " + // ทำให้ขอบโค้งมน
                        "-fx-pref-width: 200px; " +
                        "-fx-pref-height: 45px;"
        );
        btn.setOpacity(0.7);

        btn.setOnMouseEntered(e -> btn.setOpacity(1.0)); // 0.7 คือความทึบ 70% (จางลงนิดนึง)
        btn.setOnMouseExited(e -> btn.setOpacity(0.7));  // 1.0 คือความทึบ 100% (กลับเป็นปกติ)

        return btn;
    }

    public Scene getScene() { return scene; }
}