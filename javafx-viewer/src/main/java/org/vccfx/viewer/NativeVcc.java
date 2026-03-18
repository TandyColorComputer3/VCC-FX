package org.vccfx.viewer;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface NativeVcc extends Library {
    NativeVcc INSTANCE = Native.load(resolveLibraryPath(), NativeVcc.class);

    int vcc_headless_init();
    void vcc_headless_shutdown();
    int vcc_headless_reset();
    int vcc_headless_step_frame();
    Pointer vcc_headless_framebuffer();
    int vcc_headless_frame_width();
    int vcc_headless_frame_height();
    int vcc_headless_frame_stride();

    private static String resolveLibraryPath() {
        String explicit = System.getProperty("vccfx.library.path");
        if (explicit != null && !explicit.isBlank()) {
            return explicit;
        }
        return "vcc-headless";
    }
}
