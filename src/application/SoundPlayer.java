package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlayer {

    private MediaPlayer player;

    public void play() {
        // إذا كان الصوت يعمل بالفعل، لا تقم بإعادة تشغيله
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING) {
            return;
        }

        try {
            // المسار الصحيح كما صححناه سابقاً
            var resource = getClass().getResource("/application/resources/media/sample.mp3");
            if (resource == null) throw new RuntimeException("Media file not found");

            Media media = new Media(resource.toExternalForm());
            player = new MediaPlayer(media);
            player.play();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }
}