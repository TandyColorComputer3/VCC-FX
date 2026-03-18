#pragma once

#include <stdint.h>

#if defined(_WIN32)
#define VCC_HEADLESS_EXPORT __declspec(dllexport)
#else
#define VCC_HEADLESS_EXPORT
#endif

#ifdef __cplusplus
extern "C" {
#endif

VCC_HEADLESS_EXPORT int vcc_headless_init(void);
VCC_HEADLESS_EXPORT void vcc_headless_shutdown(void);
VCC_HEADLESS_EXPORT int vcc_headless_reset(void);
VCC_HEADLESS_EXPORT int vcc_headless_step_frame(void);
VCC_HEADLESS_EXPORT const uint32_t* vcc_headless_framebuffer(void);
VCC_HEADLESS_EXPORT int vcc_headless_frame_width(void);
VCC_HEADLESS_EXPORT int vcc_headless_frame_height(void);
VCC_HEADLESS_EXPORT int vcc_headless_frame_stride(void);

#ifdef __cplusplus
}
#endif
