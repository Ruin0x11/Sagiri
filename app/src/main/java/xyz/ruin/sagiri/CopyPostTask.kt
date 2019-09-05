package xyz.ruin.sagiri

import android.os.AsyncTask
import xyz.ruin.sagiri.booru.IBooruClient
import xyz.ruin.sagiri.booru.ResourceId
import java.net.URL
import java.util.concurrent.Callable

class CopyPostTask(private val source: IBooruClient, private val dest: IBooruClient, private val id: ResourceId, private val sourceUrl: URL) : Callable<Exception?> {
    override fun call(): Exception? {
        val post = source.downloadPost(id) ?: return Exception("Cannot find post $id in source")

        post.source = sourceUrl

        post.tags.forEach {
            if (dest.searchTagByName(it) == null)
            {
                val tag = source.searchTagByName(it)
                if (tag != null)
                {
                    dest.uploadTag(tag)

                    if (dest.searchWikiPageByName(it) == null)
                    {
                        val wikiPage = source.searchWikiPageByName(it)

                        if (wikiPage != null)
                        {
                            dest.uploadWikiPage(wikiPage)
                        }
                        else
                        {
                            println("no wiki page")
                        }
                    }
                }
                else
                {
                    return Exception("Cannot find tag $it in source")
                }
            }
        }

        post.pools.forEach {
            val id = it.toIntOrNull()
            if (id != null)
            {
                val pool = source.downloadPool(id)

                if (pool != null)
                {
                    if (dest.searchPoolByName(pool.name) == null)
                    {
                        dest.uploadPool(pool)
                    }
                    else
                    {
                        // dest.addPostToPool(pool)
                    }
                }
                else
                {
                    return Exception("Cannot find pool $it in source")
                }
            }
        }

        return null
    }
}