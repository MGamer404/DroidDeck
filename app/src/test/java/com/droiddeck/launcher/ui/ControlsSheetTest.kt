package com.droiddeck.launcher.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.droiddeck.launcher.gpu.FrameGen
import com.droiddeck.launcher.gpu.Lossless
import com.droiddeck.launcher.input.SecondScreenMode
import com.droiddeck.launcher.session.SessionService
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], qualifiers = "w800dp-h600dp")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ControlsSheetTest {
    @get:Rule val compose = createRobolectricComposeRule()

    @Test fun theControlsTabTurnsThePadCursorAutoHideOff() {
        val picks = mutableListOf<Boolean>()
        compose.setContent {
            DroidDeckTheme {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    ModeSheetRows(
                        ModeSettings(mode = SessionService.MODE_STEAM, resolution = "auto", hdr = false, hdrReason = null,
                            touchMode = "auto", suspendPolicy = "auto", oscMode = "auto", directAudio = null, mic = null),
                        ModeSettingsActions(onResolution = {}, onHdr = {}, onTouch = {}, onHideCursorOnPad = { picks += it },
                            onSuspendPolicy = {}, onOsc = {}, onInputSettings = {}, onDirectAudio = {}, onMic = {}, onDismiss = {}),
                        SheetTab.CONTROLS,
                    )
                }
            }
        }
        compose.onNodeWithTag("setting-hide-cursor-on-pad").performScrollTo().performClick()
        compose.runOnIdle { assertEquals(listOf(false), picks) }
    }

    @Test fun theControlsTabHidesTheRowInDesktopMode() {
        compose.setContent {
            DroidDeckTheme {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    ModeSheetRows(
                        ModeSettings(mode = SessionService.MODE_DESKTOP, resolution = "auto", hdr = false, hdrReason = null,
                            touchMode = "auto", suspendPolicy = "auto", oscMode = "auto", directAudio = null, mic = null),
                        ModeSettingsActions(onResolution = {}, onHdr = {}, onTouch = {}, onSuspendPolicy = {}, onOsc = {},
                            onInputSettings = {}, onDirectAudio = {}, onMic = {}, onDismiss = {}),
                        SheetTab.CONTROLS,
                    )
                }
            }
        }
        compose.onNodeWithTag("setting-hide-cursor-on-pad").assertDoesNotExist()
    }

    @Test fun theDrawerTurnsThePadCursorAutoHideOff() {
        val picks = mutableListOf<Boolean>()
        compose.setContent {
            DroidDeckTheme { SessionDrawer(true, SessionDrawerPage.CONTROLLER, false, {}, drawerActions(steam = true, onHideCursorOnPad = { picks += it })) }
        }
        compose.onNodeWithTag("setting-hide-cursor-on-pad").performScrollTo().performClick()
        compose.runOnIdle { assertEquals(listOf(false), picks) }
    }

    @Test fun theDrawerHidesTheRowInDesktopMode() {
        compose.setContent {
            DroidDeckTheme { SessionDrawer(true, SessionDrawerPage.CONTROLLER, false, {}, drawerActions(steam = false)) }
        }
        compose.onNodeWithTag("setting-hide-cursor-on-pad").assertDoesNotExist()
    }

    private fun drawerActions(steam: Boolean, onHideCursorOnPad: (Boolean) -> Unit = {}) =
        DrawerActions(steam = steam, desktop = !steam, isHomeApp = false, androidApps = emptyList(), hudOn = false,
            frameGen = FrameGen.Mode.OFF, lossless = Lossless.State.NONE, oscMode = "never",
            suspendPolicy = "auto", backActionsInverted = false,
            touchMode = "auto", touchAuto = "touchpad", pen = null, fexPreset = "default",
            onHideCursorOnPad = onHideCursorOnPad,
            secondScreenMode = SecondScreenMode.NONE, secondScreenDisplays = emptyList(), selectedSecondScreenDisplay = 1,
            onHud = {}, onFrameGenPick = {}, onImportLossless = {}, onKeyboard = {}, onHardwareKeyboard = {},
            onSteamMenu = null, onQam = null, onOsc = {}, onSuspendPolicy = {}, onBackActionsInverted = {},
            onTouch = {}, onFexPreset = {}, onSecondScreenMode = {}, onSecondScreenDisplay = {},
            onLaunchAndroidApp = { _, _ -> }, onBackground = {}, onShareLogs = {}, onStop = {}, onClose = {})
}
