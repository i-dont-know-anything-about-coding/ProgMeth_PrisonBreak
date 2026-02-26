package EscapeGame.Gui;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class PopupBox {

    public static void display(String title, String message) {
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.initStyle(StageStyle.TRANSPARENT);
        popupWindow.setTitle(title);

        // ── ROOT ──────────────────────────────────────────────────────────
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(0,0,0,0.75);");
        root.setPrefSize(520, 360);

        // ── CARD — เข้มขึ้นนิดหน่อย ไม่ดำสนิท ──────────────────────────
        VBox card = new VBox(0);
        card.setMaxWidth(460);
        card.setMaxHeight(320);
        card.setStyle(
                "-fx-background-color: #1e1818;" +   // น้ำตาลแดงเข้ม แทนดำ
                        "-fx-border-color: #6b2a2a;" +        // ขอบแดงหม่น ไม่สด
                        "-fx-border-width: 1.5px;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-background-radius: 4px;"
        );
        DropShadow cardShadow = new DropShadow();
        cardShadow.setColor(Color.color(0.45, 0.05, 0.05, 0.65)); // เงาแดงหม่น
        cardShadow.setRadius(38);
        cardShadow.setSpread(0.08);
        card.setEffect(cardShadow);

        // ── HEADER ────────────────────────────────────────────────────────
        StackPane header = new StackPane();
        header.setPrefHeight(52);
        // gradient แดงเข้มหม่น ลด saturation ลง
        header.setStyle(
                "-fx-background-color: linear-gradient(to right,#4a1010,#7a2020,#4a1010);" +
                        "-fx-background-radius: 2px 2px 0 0;"
        );

        // เส้นบน — แดงหม่น opacity ลด
        Line topLine = new Line(0, 0, 460, 0);
        topLine.setStroke(Color.web("#b03030"));
        topLine.setStrokeWidth(1.5);
        topLine.setOpacity(0.7);

        HBox headerContent = new HBox(12);
        headerContent.setAlignment(Pos.CENTER);
        headerContent.setPadding(new Insets(0, 20, 0, 20));

        Label barIcon = new Label("⊞");
        barIcon.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        barIcon.setTextFill(Color.web("#c07070")); // ชมพูแดงหม่น
        barIcon.setOpacity(0.65);

        Label titleLabel = new Label(title.toUpperCase());
        titleLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#f0e0e0")); // ขาวอมชมพู อ่านง่าย ไม่เจิดจ้า
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.9),6,0.8,0,1);");

        Label barIcon2 = new Label("⊞");
        barIcon2.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        barIcon2.setTextFill(Color.web("#c07070"));
        barIcon2.setOpacity(0.65);

        headerContent.getChildren().addAll(barIcon, titleLabel, barIcon2);

        Line bottomLine = new Line(0, 0, 460, 0);
        bottomLine.setStroke(Color.web("#4a1818"));
        bottomLine.setStrokeWidth(1);
        StackPane.setAlignment(bottomLine, Pos.BOTTOM_CENTER);

        header.getChildren().addAll(headerContent, topLine, bottomLine);
        StackPane.setAlignment(topLine, Pos.TOP_CENTER);

        // ── DIVIDER ───────────────────────────────────────────────────────
        Rectangle divider = new Rectangle(460, 1);
        divider.setFill(Color.web("#2e1414"));

        // ── CONTENT ───────────────────────────────────────────────────────
        VBox content = new VBox(20);
        content.setPadding(new Insets(28, 32, 22, 32));
        content.setAlignment(Pos.CENTER);
        VBox.setVgrow(content, Priority.ALWAYS);

        HBox msgRow = new HBox(14);
        msgRow.setAlignment(Pos.CENTER_LEFT);

        // แถบซ้าย — แดงหม่น opacity ลด ไม่แสบ
        Rectangle accent = new Rectangle(3, 80);
        accent.setFill(Color.web("#9a2c2c"));
        accent.setOpacity(0.80);
        accent.setArcWidth(2);
        accent.setArcHeight(2);

        Label msgLabel = new Label(message);
        msgLabel.setFont(Font.font("Chakra Petch", 14));
        msgLabel.setTextFill(Color.web("#ddd0c8")); // ขาวอมชมพูอ่อน อ่านสบาย
        msgLabel.setWrapText(true);
        msgLabel.setTextAlignment(TextAlignment.LEFT);
        msgLabel.setMaxWidth(370);
        msgLabel.setLineSpacing(5);
        HBox.setHgrow(msgLabel, Priority.ALWAYS);

        msgRow.getChildren().addAll(accent, msgLabel);

        // ── CLOSE BUTTON ──────────────────────────────────────────────────
        Button closeButton = new Button("[ CLOSE ]");
        closeButton.setFont(Font.font("Courier New", FontWeight.BOLD, 13));
        closeButton.setPrefSize(160, 42);

        String btnBase =
                "-fx-background-color:#2a1010;" +
                        "-fx-text-fill:#c06050;" +           // แดงหม่น อ่านออก ไม่แสบ
                        "-fx-border-color:#6b2a2a;" +
                        "-fx-border-width:1.5px;" +
                        "-fx-border-radius:3px;" +
                        "-fx-background-radius:3px;" +
                        "-fx-cursor:hand;";
        String btnHover =
                "-fx-background-color:#5a1818;" +
                        "-fx-text-fill:#e8a090;" +           // hover สว่างขึ้นนิดหน่อย ยังไม่แสบ
                        "-fx-border-color:#b03030;" +
                        "-fx-border-width:1.5px;" +
                        "-fx-border-radius:3px;" +
                        "-fx-background-radius:3px;" +
                        "-fx-cursor:hand;" +
                        "-fx-effect:dropshadow(gaussian,rgba(160,50,50,0.35),12,0.25,0,0);";

        closeButton.setStyle(btnBase);
        closeButton.setOnMouseEntered(e -> closeButton.setStyle(btnHover));
        closeButton.setOnMouseExited(e  -> closeButton.setStyle(btnBase));
        closeButton.setOnAction(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(200), root);
            ft.setFromValue(1); ft.setToValue(0);
            ft.setOnFinished(ev -> popupWindow.close());
            ft.play();
        });

        // ── BADGE ─────────────────────────────────────────────────────────
        Label badge = new Label("INMATE #00001 – CLASSIFIED");
        badge.setFont(Font.font("Courier New", 10));
        badge.setTextFill(Color.web("#5a3a3a")); // อ่านออกนิดหน่อย ไม่หายไป

        content.getChildren().addAll(msgRow, closeButton, badge);

        // ── ASSEMBLE ──────────────────────────────────────────────────────
        card.getChildren().addAll(header, divider, content);
        root.getChildren().add(card);

        // ── ENTRANCE ANIMATION ────────────────────────────────────────────
        card.setOpacity(0); card.setTranslateY(-20); root.setOpacity(0);

        FadeTransition bgFade   = new FadeTransition(Duration.millis(250), root);
        bgFade.setFromValue(0); bgFade.setToValue(1);
        FadeTransition cardFade = new FadeTransition(Duration.millis(300), card);
        cardFade.setFromValue(0); cardFade.setToValue(1);
        TranslateTransition cardSlide = new TranslateTransition(Duration.millis(300), card);
        cardSlide.setFromY(-20); cardSlide.setToY(0);

        Scene scene = new Scene(root, 520, 360);
        scene.setFill(Color.TRANSPARENT);
        popupWindow.setScene(scene);
        popupWindow.setOnShown(ev -> {
            bgFade.play();
            cardFade.play();
            cardSlide.play();
        });

        popupWindow.showAndWait();
    }
}