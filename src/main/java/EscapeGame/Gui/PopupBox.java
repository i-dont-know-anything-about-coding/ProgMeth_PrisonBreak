package EscapeGame.Gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

public class PopupBox {

    public static void display(String title, String message) {
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle(title);
        popupWindow.setMinWidth(400);
        popupWindow.setMinHeight(250);

        // 1. สร้างภาพพื้นหลัง (ImageView)
        ImageView bgView = new ImageView();
        try {
            // โหลดรูปภาพชื่อ popup_bg.png จากโฟลเดอร์ resources/images
            URL imageUrl = PopupBox.class.getResource("/Prison_Break.png");
            if (imageUrl != null) {
                bgView.setImage(new Image(imageUrl.toString()));
                // ตั้งขนาดภาพให้พอดีกับหน้าต่าง Pop-up
                bgView.setFitWidth(450);
                bgView.setFitHeight(300);
            }
        } catch (Exception e) {
            System.err.println("หารูปพื้นหลัง Pop-up ไม่เจอ: " + e.getMessage());
        }

        // 2. ข้อความและปุ่ม (ให้อยู่ใน VBox)
        Label labelMsg = new Label(message);
        labelMsg.setFont(Font.font("Tahoma", 16));
        labelMsg.setTextFill(Color.WHITE); // เปลี่ยนสีตัวหนังสือเป็นสีขาวให้อ่านง่าย
        labelMsg.setWrapText(true);

        Button closeButton = new Button("ปิดหน้าต่าง (Close)");
        closeButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 10;");
        closeButton.setPrefSize(150, 40);
        closeButton.setOnAction(e -> popupWindow.close());

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(labelMsg, closeButton);
        layout.setAlignment(Pos.CENTER);

        // ใส่พื้นหลังกึ่งโปร่งใสสีดำให้ VBox เพื่อให้ตัวหนังสือลอยเด่นขึ้นมาจากภาพหลัง
        layout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");

        // 3. นำภาพพื้นหลัง (bgView) และ Layout ข้อความ มาซ้อนกันด้วย StackPane
        StackPane root = new StackPane();
        root.getChildren().addAll(bgView, layout);

        Scene scene = new Scene(root);
        popupWindow.setScene(scene);
        popupWindow.showAndWait();
    }
}