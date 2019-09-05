package xyz.ruin.sagiri.util

import org.json.JSONArray

fun JSONArray.sequence(): Sequence<Any>
        = (0 until length()).asSequence().map { get(it) }

operator fun JSONArray.iterator(): Iterator<Any>
        = sequence().iterator()