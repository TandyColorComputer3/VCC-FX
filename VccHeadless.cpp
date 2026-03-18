#include "VccHeadless.h"

#include "BuildConfig.h"
#include "CommandLine.h"
#include "DirectDrawInterface.h"
#include "Vcc.h"
#include "audio.h"
#include "coco3.h"
#include "config.h"
#include "defines.h"
#include "pakinterface.h"
#include "throttle.h"

#include <cstring>

namespace
{
	bool g_headlessInitialized = false;
}

int vcc_headless_init(void)
{
	if (g_headlessInitialized)
		return 1;

	memset(&CmdArg, 0, sizeof(CmdArg));
	Cls(0, &EmuState);
	EmuState.Throttle = 0;
	EmuState.WindowHandle = nullptr;
	EmuState.WindowSize.w = VCC::DefaultWidth;
	EmuState.WindowSize.h = VCC::DefaultHeight;
	EmuState.Exiting = false;
	LoadConfig(&EmuState);
	EmuState.FullScreen = 1;

	if (!CreateNullWindow(&EmuState))
		return 0;

	InitSound();
	LoadModule();
	SetClockSpeed(1);
	EmuState.EmulationRunning = 1;
	DoCls(&EmuState);
	DoHardReset(&EmuState);
	g_headlessInitialized = true;
	return 1;
}

void vcc_headless_shutdown(void)
{
	if (!g_headlessInitialized)
		return;

	EmuState.EmulationRunning = 0;
	EmuState.Exiting = true;
	CloseScreen();
	g_headlessInitialized = false;
}

int vcc_headless_reset(void)
{
	if (!g_headlessInitialized && !vcc_headless_init())
		return 0;

	DoCls(&EmuState);
	DoHardReset(&EmuState);
	return 1;
}

int vcc_headless_step_frame(void)
{
	if (!g_headlessInitialized && !vcc_headless_init())
		return 0;

	StartRender();
	RenderFrame(&EmuState);
	EndRender(1);
	GetModuleStatus(&EmuState);
	return 1;
}

const uint32_t* vcc_headless_framebuffer(void)
{
	if (!g_headlessInitialized)
		return nullptr;

	return EmuState.PTRsurface32;
}

int vcc_headless_frame_width(void)
{
	return VCC::DefaultWidth;
}

int vcc_headless_frame_height(void)
{
	return VCC::DefaultHeight;
}

int vcc_headless_frame_stride(void)
{
	return VCC::DefaultWidth;
}
