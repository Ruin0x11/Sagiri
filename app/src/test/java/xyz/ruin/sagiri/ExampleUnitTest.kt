package xyz.ruin.sagiri

import io.mockk.every
import io.mockk.mockk
import khttp.responses.Response
import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*
import xyz.ruin.sagiri.booru.ApiClient
import xyz.ruin.sagiri.booru.DanbooruClient
import xyz.ruin.sagiri.booru.IApiClient
import java.net.URI

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val client = ApiClient(URI("https://danbooru.donmai.us"), "necoma", "Owpt0cA9jy7yvzGWHLwt1_TZ1JpkNaEsq_e5YVZHbV0")
        val booru = DanbooruClient(client)
        val post = booru.downloadPost(25)!!
        println(post.id)
        post.tags.forEach { println(it) }
    }

    @Test
    fun addition_isCorrect2() {
        val client = mockk<IApiClient>()
        val resp = mockk<Response>()
        every { resp.statusCode } returns 200
        every { resp.text } returns "{ \"id\": 1, \"tag_string\": \"a b c\", \"pool_string\": \"\" }"
        every { resp.jsonObject } returns JSONObject(resp.text)
        every { client.get(any(), any()) } returns resp

        val booru = DanbooruClient(client)
        val post = booru.downloadPost(1)!!
        assertEquals(1, post.id)
        assertEquals(listOf("a", "b", "c"), post.tags)
    }
}