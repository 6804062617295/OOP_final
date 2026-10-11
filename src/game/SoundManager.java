package game;

import javax.sound.sampled.*;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public final class SoundManager {

    private static final String[] SEARCH_DIRECTORIES = {
            "assets/sounds/",
            "assets/"
    };

    private static Clip bgmClip;

    private SoundManager() {
    }

    public static synchronized void playBgm() {
        if (GraphicsEnvironment.isHeadless()) return;
        if (bgmClip != null && bgmClip.isRunning()) return;

        File bgmFile = findAudioFile("bgm", "music");
        if (bgmFile == null) return;

        try {
            if (bgmClip != null) {
                bgmClip.close();
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(bgmFile);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(ais);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ignored) {
        } catch (Exception ignored) {
        }
    }

    public static synchronized void stopBgm() {
        if (bgmClip != null) {
            try {
                bgmClip.stop();
                bgmClip.close();
            } catch (Exception ignored) {
            } finally {
                bgmClip = null;
            }
        }
    }

    public static void playFoodDone() {
        playSfx("food_done", "cook_done", "ding");
    }

    public static void playSfx(String... candidateNames) {
        if (GraphicsEnvironment.isHeadless()) return;

        new Thread(() -> {
            File sfxFile = findAudioFile(candidateNames);
            if (sfxFile == null) return;

            try (AudioInputStream ais = AudioSystem.getAudioInputStream(sfxFile)) {
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ignored) {
            } catch (Exception ignored) {
            }
        }, "SFX-Player").start();
    }

    public static File findAudioFile(String... names) {
        for (String dir : SEARCH_DIRECTORIES) {
            for (String name : names) {
                File wav = new File(dir + name + ".wav");
                if (wav.exists() && wav.isFile()) {
                    return wav;
                }
            }
        }
        return null;
    }
}
