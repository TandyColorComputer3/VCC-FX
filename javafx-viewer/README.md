# JavaFX Headless Viewer

This sample JavaFX app loads the native VCC headless framebuffer API and displays
the 640x480 CoCo video output in a scaled `ImageView`.

## Expected native library

Build the native code into a shared library that exports the symbols declared in
`../VccHeadless.h`, then point Java at it with:

- Windows: `-Dvccfx.library.path=C:\path\to\vcc-headless.dll`
- Linux/macOS: `-Dvccfx.library.path=/path/to/libvcc-headless.so`

## Run

```bash
mvn javafx:run -Dvccfx.library.path=/absolute/path/to/native/library
```
