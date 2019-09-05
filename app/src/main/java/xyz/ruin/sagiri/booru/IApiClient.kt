package xyz.ruin.sagiri.booru

import khttp.responses.Response
import khttp.structures.files.FileLike

interface IApiClient {
    fun get(path: String, params: Map<String, String> = mapOf()): Response
    fun post(path: String, params: Map<String, String> = mapOf(), files: List<FileLike> = listOf()): Response
}