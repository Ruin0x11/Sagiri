package xyz.ruin.sagiri.booru

import java.net.URL

typealias ResourceId = Int

enum class PostRating {
    Safe,
    Questionable,
    Explicit;

    override fun toString(): String =
        when (this) {
            PostRating.Safe -> "s"
            PostRating.Questionable -> "q"
            PostRating.Explicit -> "e"
    }
}

fun String.toPostRating(): PostRating? =
    when (this) {
        "s" -> PostRating.Safe
        "q" -> PostRating.Questionable
        "e" -> PostRating.Explicit
        else -> null
    }

enum class TagCategory {
    General,
    Character,
    Copyright,
    Artist,
    Meta;

    fun toInt(): Int =
        when (this) {
            TagCategory.General -> 0
            TagCategory.Artist -> 1
            TagCategory.Copyright -> 3
            TagCategory.Character -> 4
            TagCategory.Meta -> 5
        }
}

fun Int.toTagCategory(): TagCategory? =
    when (this) {
        0 -> TagCategory.General
        1 -> TagCategory.Artist
        3 -> TagCategory.Copyright
        4 -> TagCategory.Character
        5 -> TagCategory.Meta
        else -> null
    }

enum class PoolCategory {
    Series,
    Collection;

    override fun toString(): String =
        when (this) {
            PoolCategory.Series -> "series"
            PoolCategory.Collection -> "collection"
        }
}

fun String.toPoolCategory(): PoolCategory? =
    when (this) {
        "series" -> PoolCategory.Series
        "collection" -> PoolCategory.Collection
        else -> null
    }


data class Post(val id: ResourceId, val tags: List<String>, val pools: List<String>, val rating: PostRating, var source: URL? = null)
data class Pool(val id: ResourceId, val name: String, val description: String, val category: PoolCategory, val posts: List<ResourceId>)
data class Upload(val data: ByteArray, val source: String?, val rating: PostRating, val parentId: ResourceId?, val tags: List<String>)
data class Tag(val id: ResourceId, val name: String, val category: TagCategory)
data class WikiPage(val id: ResourceId, val title: String, val body: String, val otherNames: List<String>)