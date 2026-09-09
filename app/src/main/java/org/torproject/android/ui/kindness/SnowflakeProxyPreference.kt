package org.torproject.android.ui.kindness

import org.torproject.android.util.Prefs

internal fun shouldIgnoreSnowflakePreferenceChange(key: String?): Boolean =
    key != Prefs.PREF_BRIDGE_COUNTRY && key != Prefs.PREF_CAMO_APP_PACKAGE
