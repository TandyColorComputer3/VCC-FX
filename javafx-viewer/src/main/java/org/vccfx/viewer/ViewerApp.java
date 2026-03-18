package org.vccfx.viewer;

import java.nio.IntBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class ViewerApp extends Application {
    private static final NativeVcc NATIVE = NativeVcc.INSTANCE;

    private ScheduledExecutorService executor;
    private PixelBuffer<IntBuffer> pixelBuffer;
    private IntBuffer frontBuffer;
    private int width;
    private int height;

    @Override
    public void start(Stage stage) {
        if (NATIVE.vcc_headless_init() == 0) {
            throw new IllegalStateException("Unable to initialize native VCC headless core");
        }

        width = NATIVE.vcc_headless_frame_width();
        height = NATIVE.vcc_headless_frame_height();
        frontBuffer = IntBuffer.allocate(width * height);
        pixelBuffer = new PixelBuffer<>(width, height, frontBuffer, PixelFormat.getIntArgbPreInstance());

        ImageView imageView = new ImageView(new WritableImage(pixelBuffer));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width * 2.0);
        imageView.setFitHeight(height * 2.0);

        stage.setTitle("VCC FX Viewer");
        stage.setScene(new Scene(new StackPane(imageView), width * 2.0, height * 2.0));
        stage.show();

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::renderFrame, 0, 16, TimeUnit.MILLISECONDS);
    }

    private void renderFrame() {
        if (NATIVE.vcc_headless_step_frame() == 0) {
            return;
        }

        var framebuffer = NATIVE.vcc_headless_framebuffer();
        if (framebuffer == null) {
            return;
        }

        int[] pixels = framebuffer.getIntArray(0, width * height);
        frontBuffer.position(0);
        frontBuffer.put(pixels);
        frontBuffer.position(0);

        Platform.runLater(() -> pixelBuffer.updateBuffer(buffer -> null));
    }

    @Override
    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
        NATIVE.vcc_headless_shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
