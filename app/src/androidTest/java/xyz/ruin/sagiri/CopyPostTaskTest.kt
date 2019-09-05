package xyz.ruin.sagiri

import androidx.test.runner.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import xyz.ruin.sagiri.booru.IBooruClient

@RunWith(AndroidJUnit4::class)
class CopyPostTaskTest {
    @Test
    fun testFailure() {
        val source = mockk<IBooruClient>()
        val dest = mockk<IBooruClient>()
        every { source.downloadPost(0) } returns null
        val task = CopyPostTask(source, dest, 0)
        Assert.assertEquals(false, task.get())
    }
}