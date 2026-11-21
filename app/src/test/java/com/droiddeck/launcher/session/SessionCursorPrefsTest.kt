package com.droiddeck.launcher.session

import android.content.Context
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class SessionCursorPrefsTest {
    private val context get() = RuntimeEnvironment.getApplication()

    @Before fun clearSettings() {
        context.getSharedPreferences("session", Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test fun hidingTheCursorOnPadInputDefaultsOnAndHonorsTheSteamChoice() {
        assertTrue(SessionPrefs.hideCursorOnPad(context, SessionService.MODE_STEAM))
        SessionPrefs.setHideCursorOnPad(context, SessionService.MODE_STEAM, false)
        assertFalse(SessionPrefs.hideCursorOnPad(context, SessionService.MODE_STEAM))
    }

    @Test fun aSteamCursorChoiceNeverChangesDesktop() {
        SessionPrefs.setHideCursorOnPad(context, SessionService.MODE_STEAM, false)
        assertTrue(SessionPrefs.hideCursorOnPad(context, SessionService.MODE_DESKTOP))
    }

    @Test fun aDesktopCursorChoiceIsNotStored() {
        SessionPrefs.setHideCursorOnPad(context, SessionService.MODE_DESKTOP, false)
        assertTrue(SessionPrefs.hideCursorOnPad(context, SessionService.MODE_DESKTOP))
        assertTrue(SessionPrefs.hideCursorOnPad(context, SessionService.MODE_STEAM))
    }
}
