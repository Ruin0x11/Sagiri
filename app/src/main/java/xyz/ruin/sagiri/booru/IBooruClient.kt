package xyz.ruin.sagiri.booru

interface IBooruClient {
    fun downloadPost(id: ResourceId): Post?
    fun downloadTag(id: ResourceId): Tag?
    fun downloadPool(id: ResourceId): Pool?

    fun uploadUpload(upload: Upload)
    fun uploadTag(tag: Tag)
    fun uploadPool(pool: Pool)
    fun uploadWikiPage(wikiPage: WikiPage)

    fun searchTagByName(name: String): Tag?
    fun searchPoolByName(name: String): Pool?
    fun searchWikiPageByName(name: String): WikiPage?
}