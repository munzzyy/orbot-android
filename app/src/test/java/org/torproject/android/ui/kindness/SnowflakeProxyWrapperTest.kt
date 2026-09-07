package org.torproject.android.ui.kindness

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SnowflakeProxyWrapperTest {

    @Test
    fun aStartThatStillOwnsItsGenerationCommits() {
        assertTrue(
            SnowflakeProxyWrapper.shouldCommitStart(
                generationAtLaunch = 3, currentGeneration = 3, proxyPresent = false
            )
        )
    }

    @Test
    fun aStopThatArrivedFirstWins() {
        assertFalse(
            SnowflakeProxyWrapper.shouldCommitStart(
                generationAtLaunch = 3, currentGeneration = 4, proxyPresent = false
            )
        )
    }

    @Test
    fun aNewerStartWins() {
        assertFalse(
            SnowflakeProxyWrapper.shouldCommitStart(
                generationAtLaunch = 3, currentGeneration = 5, proxyPresent = false
            )
        )
    }

    @Test
    fun neverDoublesUpOnALiveProxy() {
        assertFalse(
            SnowflakeProxyWrapper.shouldCommitStart(
                generationAtLaunch = 3, currentGeneration = 3, proxyPresent = true
            )
        )
    }

    @Test
    fun portsRoundTrip() {
        val ports = listOf(50000, 50001, 50002)
        assertEquals(ports, SnowflakeProxyWrapper.decodePorts(SnowflakeProxyWrapper.encodePorts(ports)))
    }

    @Test
    fun emptyRecordDecodesToNothing() {
        assertTrue(SnowflakeProxyWrapper.decodePorts("").isEmpty())
    }

    @Test
    fun garbageInTheRecordIsDropped() {
        assertEquals(listOf(50000), SnowflakeProxyWrapper.decodePorts("50000,junk,,-3,70000"))
    }
}
