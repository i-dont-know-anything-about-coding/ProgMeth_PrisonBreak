package EscapeGame;

import EscapeGame.Level.MainMenu;
import EscapeGame.Logic.Inventory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * คลาสหลักของเกมที่จัดการหน้าต่าง (Window) และการเปลี่ยนฉาก
 */
public class PrisonBreakGame extends Application {

    private Stage window;
    // ระบบกระเป๋าเก็บของที่ผู้เล่นพกติดตัวไปได้ทุกด่าน (Logic)
    private Inventory inventory;
    private String playerName = "Player";

    @Override
    public void start(Stage primaryStage) {

        Font.loadFont(getClass().getResourceAsStream("/fonts/BlackOpsOne-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Oswald-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/RubikDirt-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/ChakraPetch-Bold.ttf"), 14);

        System.out.println(javafx.scene.text.Font.getFamilies());

        this.window = primaryStage;
        this.window.setTitle("Prison Break");
        this.window.setResizable(false); // ล็อคขนาดหน้าจอไม่ให้พังเวลาผู้เล่นดึงยืด

        // สร้างกระเป๋าเปล่าๆ ตอนเริ่มเกม
        this.inventory = new Inventory();

        // โหลดและแสดงหน้า Main Menu เป็นหน้าแรก
        MainMenu menu = new MainMenu(this);
        window.setScene(menu.getScene());
        window.show();
    }

    public void resetGame() {
        this.inventory = new Inventory();
        MainMenu menu = new MainMenu(this);
        switchScene(menu.getScene());
    }

    /**
     * ฟังก์ชันสำหรับเปลี่ยนหน้าจอ (Scene) ไปด่านต่างๆ
     * @param newScene หน้าจอใหม่ที่ต้องการให้แสดง
     */
    public void switchScene(Scene newScene) {
        window.setScene(newScene);
    }

    /**
     * ดึงระบบกระเป๋าเก็บของไปใช้งานในด่านต่างๆ
     * @return ออบเจกต์ EscapeGame.Logic.Inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    public String getPlayerName()           { return playerName; }
    public void setPlayerName(String name)  { this.playerName = name; }
    public static void main(String[] args) {
        launch(args); // คำสั่งเริ่มระบบ JavaFX
    }
}