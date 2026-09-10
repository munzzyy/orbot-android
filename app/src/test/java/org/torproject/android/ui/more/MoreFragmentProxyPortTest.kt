package org.torproject.android.ui.more

import org.junit.Assert.assertEquals
import org.junit.Test

class MoreFragmentProxyPortTest {
    private val notSet = "——"

    @Test
    fun disabledHttpDoesNotHideSocksPort() {
        assertEquals(notSet to "9050", proxyPortDisplayValues(0, 9050, notSet))
    }

    @Test
    fun disabledSocksDoesNotHideHttpPort() {
        assertEquals("8118" to notSet, proxyPortDisplayValues(8118, 0, notSet))
    }

    @Test
    fun activePortsAreBothShown() {
        assertEquals("8118" to "9050", proxyPortDisplayValues(8118, 9050, notSet))
    }
}
