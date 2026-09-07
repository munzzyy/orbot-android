package org.torproject.android.ui.kindness

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KindnessWatchdogWorkerTest {

    @Test
    fun restartsWhenWantedAndDead() {
        assertTrue(
            KindnessWatchdogWorker.shouldRestart(
                wantsProxy = true, serviceRunning = false, regionBlocked = false
            )
        )
    }

    @Test
    fun leavesARunningServiceAlone() {
        assertFalse(
            KindnessWatchdogWorker.shouldRestart(
                wantsProxy = true, serviceRunning = true, regionBlocked = false
            )
        )
    }

    @Test
    fun respectsTheUserSayingNo() {
        assertFalse(
            KindnessWatchdogWorker.shouldRestart(
                wantsProxy = false, serviceRunning = false, regionBlocked = false
            )
        )
    }

    @Test
    fun respectsARegionBlock() {
        assertFalse(
            KindnessWatchdogWorker.shouldRestart(
                wantsProxy = true, serviceRunning = false, regionBlocked = true
            )
        )
    }
}
