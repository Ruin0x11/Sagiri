package xyz.ruin.sagiri.booru

import khttp.structures.files.FileLike
import org.json.JSONObject
import xyz.ruin.sagiri.util.sequence
import java.net.URL

class DanbooruClient(private val client: IApiClient) : IBooruClient {
    override fun downloadPost(id: ResourceId): Post? {
        val r = client.get("/posts/$id.json")
        println(r.text)
        if (r.statusCode != 200)
            return null

        return parsePost(r.jsonObject)
    }

    override fun downloadTag(id: ResourceId): Tag? {
        val r = client.get("/tags/$id.json")
        if (r.statusCode != 200)
            return null

        return parseTag(r.jsonObject)
    }

    override fun downloadPool(id: ResourceId): Pool? {
        val r = client.get("/pools/$id.json")
        if (r.statusCode != 200)
            return null

        return parsePool(r.jsonObject)
    }

    override fun searchTagByName(name: String): Tag? {
        val r = client.get("/tags.json", mapOf(name to "search[title]"))
        if (r.statusCode != 200)
            return null

        return parseTag(r.jsonObject)
    }

    override fun searchPoolByName(name: String): Pool? {
        val r = client.get("/pools.json", mapOf(name to "search[title]"))
        if (r.statusCode != 200)
            return null

        return parsePool(r.jsonObject)
    }

    override fun searchWikiPageByName(name: String): WikiPage? {
        val r = client.get("/wiki_pages.json", mapOf(name to "search[title]"))
        if (r.statusCode != 200)
            return null

        return parseWikiPage(r.jsonObject)
    }


    override fun uploadUpload(upload: Upload) {
        val params = mutableMapOf(
            upload.rating.toString() to "upload[rating]",
            upload.tags.joinToString(" ") to "upload[tag_string]")

        if (upload.source != null)
        {
            params["upload[source]"] = upload.source
        }
        if (upload.parentId != null)
        {
            params["upload[parent_id]"] = upload.parentId.toString()
        }

        client.post("/wiki_pages.json", params, listOf(FileLike("upload[file]", upload.data)))
    }

    override fun uploadTag(tag: Tag) {
        if (downloadTag(tag.id) == null)
            return

        client.post("/tags.json", mapOf(tag.category.toInt().toString() to "tag[category]"))
    }

    override fun uploadPool(pool: Pool) {
        client.post("/pools.json", mapOf(
            pool.name to "pool[name]",
            pool.description to "pool[description]",
            pool.category.toString() to "pool[category]",
            pool.posts.joinToString(" ") to "pool[post_ids]"))
    }

    override fun uploadWikiPage(wikiPage: WikiPage) {
        client.post("/wiki_pages.json", mapOf(
            wikiPage.title to "wiki_page[title]",
            wikiPage.body to "wiki_page[body]",
            wikiPage.otherNames.joinToString(" ") to "wiki_page[other_names]"))
    }
}

fun parsePost(o: JSONObject): Post {
    val id = o.getInt("id")
    val tags = o.getString("tag_string").split(' ')
    val pools = o.getString("pool_string").split(' ').map { it.removePrefix("pool:") }
    val rating = o.getString("rating").toPostRating()!!
    val source = o.optString("source")?.let(::URL)

    return Post(id, tags, pools, rating, source)
}

fun parsePool(o: JSONObject): Pool {
    val id = o.getInt("id")
    val name = o.getString("title")
    val description = o.getString("description")
    val posts = o.getJSONArray("post_ids").sequence().map { it as Int }.toList()
    val category = o.getString("category").toPoolCategory()!!

    return Pool(id, name, description, category, posts)
}

fun parseTag(o: JSONObject): Tag {
    val id = o.getInt("id")
    val name = o.getString("title")
    val category = o.getInt("category").toTagCategory()!!

    return Tag(id, name, category)
}

fun parseWikiPage(o: JSONObject): WikiPage {
    val id = o.getInt("id")
    val name = o.getString("title")
    val body = o.getString("body")
    val otherNames = o.getJSONArray("other_names_string").sequence().map { it as String }.toList()

    return WikiPage(id, name, body, otherNames)
}