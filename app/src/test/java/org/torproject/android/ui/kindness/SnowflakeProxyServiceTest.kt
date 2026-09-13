package org.torproject.android.ui.kindness

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.torproject.android.util.Prefs

class SnowflakeProxyServiceTest {
    @Test
    fun relevantPreferenceChangesAreHandled() {
        assertFalse(shouldIgnoreSnowflakePreferenceChange(Prefs.PREF_BRIDGE_COUNTRY))
        assertFalse(shouldIgnoreSnowflakePreferenceChange(Prefs.PREF_CAMO_APP_PACKAGE))
    }

    @Test
    fun unrelatedPreferenceChangesAreIgnored() {
        assertTrue(shouldIgnoreSnowflakePreferenceChange("unrelated_preference"))
        assertTrue(shouldIgnoreSnowflakePreferenceChange(null))
    }
}
