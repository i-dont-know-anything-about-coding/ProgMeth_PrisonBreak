package EscapeGame.Gui;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class CharacterSelect {

    private static final String[] CHAR_NAMES  = {"INMATE #7685", "INMATE #1456"};
    private static final String[] CHAR_DESC   = {
            "ผู้ต้องขังชาย — ใจเย็น\nแต่แน่วแน่ในทุกการตัดสินใจ",
            "ผู้ต้องขังหญิง — ฉลาด\nและหาทางออกได้เสมอ"
    };
    // path รูปภาพใน resources — วางไฟล์ไว้ที่ resources/images/
    private static final String[] CHAR_IMAGES = {
            "/images/CharBoy.png",   // ชาย
            "/images/CharGirl.png"    // หญิง
    };
    private static final String[] CHAR_ACCENT = {"#D4703A", "#C8885A"};

    private int currentIndex = 0;

    private ImageView    charImageView;
    private Label        charNameLabel;
    private Label        charDescLabel;
    private Label        indexLabel;
    private ScaleTransition breatheAnim;

    public interface OnSelectListener {
        void onSelected(int characterIndex, String playerName);
    }

    public static void display(OnSelectListener listener) {
        new CharacterSelect().show(listener);
    }

    private void show(OnSelectListener listener) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

        // ── ROOT ──────────────────────────────────────────────────────────
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(0,0,0,0.72);");
        root.setPrefSize(700, 500);

        // ── CARD ──────────────────────────────────────────────────────────
        VBox card = new VBox(0);
        card.setMaxWidth(640);
        card.setMaxHeight(460);
        card.setStyle(
                "-fx-background-color: #2c2420;" +
                        "-fx-border-color: #7a4a28;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;"
        );
        DropShadow shadow = new DropShadow(45, Color.color(0.3, 0.1, 0.0, 0.7));
        shadow.setOffsetY(5);
        card.setEffect(shadow);

        // ── HEADER ────────────────────────────────────────────────────────
        StackPane header = new StackPane();
        header.setPrefHeight(50);
        header.setStyle(
                "-fx-background-color: linear-gradient(to right,#3a2a1e,#4e3828,#3a2a1e);" +
                        "-fx-background-radius: 2 2 0 0;"
        );
        Rectangle topAccent = new Rectangle(640, 3);
        topAccent.setFill(Color.web("#D4703A"));
        StackPane.setAlignment(topAccent, Pos.TOP_CENTER);

        HBox headerRow = new HBox(10);
        headerRow.setAlignment(Pos.CENTER);
        Label hIcon  = new Label("⊞");
        hIcon.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        hIcon.setTextFill(Color.web("#a06840"));
        Label headerTitle = new Label("SELECT YOUR INMATE");
        headerTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 17));
        headerTitle.setTextFill(Color.web("#f0dcc8"));
        headerTitle.setStyle("-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.8),6,0.8,0,1);");
        Label hIcon2 = new Label("⊞");
        hIcon2.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        hIcon2.setTextFill(Color.web("#a06840"));
        headerRow.getChildren().addAll(hIcon, headerTitle, hIcon2);
        header.getChildren().addAll(headerRow, topAccent);

        // ── BODY ──────────────────────────────────────────────────────────
        HBox body = new HBox(0);
        body.setPrefHeight(355);
        body.setStyle("-fx-background-color: #2c2420;");

        Button btnLeft = createArrowButton("◀");
        btnLeft.setOnAction(e -> navigate(-1));

        // ── CHARACTER DISPLAY ─────────────────────────────────────────────
        VBox charDisplay = new VBox(12);
        charDisplay.setAlignment(Pos.CENTER);
        charDisplay.setPadding(new Insets(16, 10, 16, 10));
        HBox.setHgrow(charDisplay, Priority.ALWAYS);

        // ── IMAGE CONTAINER ───────────────────────────────────────────────
        StackPane imgContainer = new StackPane();
        imgContainer.setPrefSize(200, 240);
        imgContainer.setMaxSize(200, 240);

        // วงกลม glow ด้านหลัง
        Circle bgGlow = new Circle(96);
        bgGlow.setFill(Color.web("#1e1810"));
        bgGlow.setStroke(Color.web(CHAR_ACCENT[currentIndex]));
        bgGlow.setStrokeWidth(1.5);
        bgGlow.setOpacity(0.5);

        // รูปตัวละคร
        charImageView = new ImageView();
        charImageView.setFitWidth(190);
        charImageView.setFitHeight(230);
        charImageView.setPreserveRatio(true);
        charImageView.setSmooth(true);
        loadImage(currentIndex);

        imgContainer.getChildren().addAll(bgGlow, charImageView);

        // ── BREATHE ANIMATION — scale ขึ้นลงเบาๆ ─────────────────────────
        breatheAnim = new ScaleTransition(Duration.millis(2200), charImageView);
        breatheAnim.setFromX(1.00); breatheAnim.setFromY(1.00);
        breatheAnim.setToX(1.04);   breatheAnim.setToY(1.04);
        breatheAnim.setAutoReverse(true);
        breatheAnim.setCycleCount(Animation.INDEFINITE);
        breatheAnim.setInterpolator(Interpolator.EASE_BOTH);
        breatheAnim.play();

        // ── TEXT ──────────────────────────────────────────────────────────
        charNameLabel = new Label(CHAR_NAMES[currentIndex]);
        charNameLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 19));
        charNameLabel.setTextFill(Color.web(CHAR_ACCENT[currentIndex]));
        charNameLabel.setStyle("-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.6),5,0.5,0,1);");

        Rectangle nameLine = new Rectangle(220, 1);
        nameLine.setFill(Color.web("#5a3820"));

        charDescLabel = new Label(CHAR_DESC[currentIndex]);
        charDescLabel.setFont(Font.font("Chakra Petch", 12));
        charDescLabel.setTextFill(Color.web("#d4c0a8"));
        charDescLabel.setTextAlignment(TextAlignment.CENTER);
        charDescLabel.setWrapText(true);
        charDescLabel.setMaxWidth(280);
        charDescLabel.setLineSpacing(5);

        indexLabel = new Label(buildDots(currentIndex));
        indexLabel.setFont(Font.font(14));
        indexLabel.setTextFill(Color.web("#8a5830"));

        charDisplay.getChildren().addAll(imgContainer, charNameLabel, nameLine, charDescLabel, indexLabel);

        Button btnRight = createArrowButton("▶");
        btnRight.setOnAction(e -> navigate(1));

        body.getChildren().addAll(btnLeft, charDisplay, btnRight);

        // ── DIVIDER ───────────────────────────────────────────────────────
        Rectangle divider = new Rectangle(640, 1);
        divider.setFill(Color.web("#3e2a18"));

        // ── FOOTER ────────────────────────────────────────────────────────
        HBox footer = new HBox(12);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(13, 32, 13, 32));
        footer.setStyle("-fx-background-color:#241c18;-fx-background-radius:0 0 4 4;");

        Label nameLabel = new Label("NAME :");
        nameLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 12));
        nameLabel.setTextFill(Color.web("#a07850"));

        TextField nameField = new TextField("PRISONER");
        nameField.setFont(Font.font("Courier New", 12));
        nameField.setPrefWidth(190);
        nameField.setStyle(
                "-fx-background-color:#382e28;" +
                        "-fx-text-fill:#f0d8b8;" +
                        "-fx-border-color:#7a4a28;" +
                        "-fx-border-width:1;" +
                        "-fx-border-radius:2;" +
                        "-fx-background-radius:2;" +
                        "-fx-padding:6 10;"
        );

        Button btnConfirm = new Button("[ CONFIRM ]");
        btnConfirm.setFont(Font.font("Courier New", FontWeight.BOLD, 12));
        btnConfirm.setPrefSize(130, 36);
        String confirmBase =
                "-fx-background-color:#3e2810;-fx-text-fill:#D4703A;" +
                        "-fx-border-color:#7a4a28;-fx-border-width:1.5;" +
                        "-fx-border-radius:2;-fx-background-radius:2;-fx-cursor:hand;";
        String confirmHover =
                "-fx-background-color:#5a3418;-fx-text-fill:#f0a070;" +
                        "-fx-border-color:#D4703A;-fx-border-width:1.5;" +
                        "-fx-border-radius:2;-fx-background-radius:2;-fx-cursor:hand;" +
                        "-fx-effect:dropshadow(gaussian,rgba(200,100,40,0.4),12,0.3,0,0);";
        btnConfirm.setStyle(confirmBase);
        btnConfirm.setOnMouseEntered(e -> btnConfirm.setStyle(confirmHover));
        btnConfirm.setOnMouseExited(e  -> btnConfirm.setStyle(confirmBase));
        btnConfirm.setOnAction(e -> {
            String name = nameField.getText().isBlank()
                    ? CHAR_NAMES[currentIndex] : nameField.getText().trim();
            breatheAnim.stop();
            FadeTransition ft = new FadeTransition(Duration.millis(180), root);
            ft.setFromValue(1); ft.setToValue(0);
            ft.setOnFinished(ev -> {
                stage.close();
                if (listener != null) listener.onSelected(currentIndex, name);
            });
            ft.play();
        });

        Button btnCancel = new Button("[ CANCEL ]");
        btnCancel.setFont(Font.font("Courier New", FontWeight.BOLD, 12));
        btnCancel.setPrefSize(110, 36);
        String cancelBase =
                "-fx-background-color:transparent;-fx-text-fill:#7a5838;" +
                        "-fx-border-color:#4a3020;-fx-border-width:1;" +
                        "-fx-border-radius:2;-fx-background-radius:2;-fx-cursor:hand;";
        String cancelHover =
                "-fx-background-color:#382818;-fx-text-fill:#b08060;" +
                        "-fx-border-color:#8a5838;-fx-border-width:1;" +
                        "-fx-border-radius:2;-fx-background-radius:2;-fx-cursor:hand;";
        btnCancel.setStyle(cancelBase);
        btnCancel.setOnMouseEntered(e -> btnCancel.setStyle(cancelHover));
        btnCancel.setOnMouseExited(e  -> btnCancel.setStyle(cancelBase));
        btnCancel.setOnAction(e -> {
            breatheAnim.stop();
            FadeTransition ft = new FadeTransition(Duration.millis(180), root);
            ft.setFromValue(1); ft.setToValue(0);
            ft.setOnFinished(ev -> stage.close());
            ft.play();
        });

        footer.getChildren().addAll(nameLabel, nameField, btnConfirm, btnCancel);

        // ── ASSEMBLE ──────────────────────────────────────────────────────
        card.getChildren().addAll(header, body, divider, footer);
        root.getChildren().add(card);

        card.setOpacity(0); card.setTranslateY(-16); root.setOpacity(0);
        FadeTransition bgFade   = new FadeTransition(Duration.millis(220), root);
        bgFade.setFromValue(0); bgFade.setToValue(1);
        FadeTransition cardFade = new FadeTransition(Duration.millis(280), card);
        cardFade.setFromValue(0); cardFade.setToValue(1);
        TranslateTransition cardSlide = new TranslateTransition(Duration.millis(280), card);
        cardSlide.setFromY(-16); cardSlide.setToY(0);

        Scene scene = new Scene(root, 700, 500);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setOnShown(ev -> { bgFade.play(); cardFade.play(); cardSlide.play(); });
        stage.showAndWait();
    }

    // ── โหลดรูป ───────────────────────────────────────────────────────────────
    private void loadImage(int index) {
        try {
            Image img = new Image(
                    getClass().getResourceAsStream(CHAR_IMAGES[index]),
                    190, 230, true, true
            );
            charImageView.setImage(img);
        } catch (Exception e) {
            System.err.println("โหลดรูปไม่ได้: " + CHAR_IMAGES[index]);
        }
    }

    // ── Navigate ──────────────────────────────────────────────────────────────
    private void navigate(int dir) {
        breatheAnim.stop();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(120), charImageView);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            currentIndex = (currentIndex + dir + CHAR_NAMES.length) % CHAR_NAMES.length;
            loadImage(currentIndex);
            updateCard();
            charImageView.setOpacity(0);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(160), charImageView);
            fadeIn.setToValue(1);
            fadeIn.setOnFinished(ev -> breatheAnim.play());
            fadeIn.play();
        });
        fadeOut.play();
    }

    private void updateCard() {
        charNameLabel.setText(CHAR_NAMES[currentIndex]);
        charNameLabel.setTextFill(Color.web(CHAR_ACCENT[currentIndex]));
        charDescLabel.setText(CHAR_DESC[currentIndex]);
        indexLabel.setText(buildDots(currentIndex));
    }

    private String buildDots(int active) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CHAR_NAMES.length; i++)
            sb.append(i == active ? "⬤   " : "○   ");
        return sb.toString().trim();
    }

    private Button createArrowButton(String symbol) {
        Button btn = new Button(symbol);
        btn.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
        btn.setPrefSize(60, 355);
        String base  =
                "-fx-background-color:transparent;-fx-text-fill:#6a4828;" +
                        "-fx-cursor:hand;-fx-border-color:transparent;";
        String hover =
                "-fx-background-color:rgba(120,60,20,0.18);-fx-text-fill:#D4703A;" +
                        "-fx-cursor:hand;-fx-border-color:transparent;" +
                        "-fx-effect:dropshadow(gaussian,rgba(180,80,20,0.35),8,0.3,0,0);";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }
}