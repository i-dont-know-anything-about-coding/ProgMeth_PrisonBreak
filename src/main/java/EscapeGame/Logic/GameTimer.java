package EscapeGame.Logic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameTimer {

    private int secondsLeft;
    private Timeline timeline;
    private Runnable onTick;
    private Runnable onTimeUp;

    public GameTimer(int totalSeconds, Runnable onTick, Runnable onTimeUp) {
        this.secondsLeft = totalSeconds;
        this.onTick      = onTick;
        this.onTimeUp    = onTimeUp;
        buildTimeline();
    }

    private void buildTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsLeft--;
            if (onTick != null) onTick.run();
            if (secondsLeft <= 0) {
                timeline.stop();
                if (onTimeUp != null) onTimeUp.run();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start()  { timeline.play();  }
    public void stop()   { timeline.stop();  }
    public void pause()  { timeline.pause(); }
    public void resume() { timeline.play();  }

    public String getFormatted() {
        int m = secondsLeft / 60;
        int s = secondsLeft % 60;
        return String.format("%02d : %02d", m, s);
    }

    public int getSecondsLeft() { return secondsLeft; }
}
