package xyz.ruin.sagiri.booru

import android.net.Uri
import khttp.responses.Response
import khttp.structures.files.FileLike
import java.net.URI

class ApiClient(private val url: Uri, private val login: String, private val apiKey: String) : IApiClient {
    private val authParams = mapOf(login to "login", apiKey to "api_key")

    override fun get(path: String, params: Map<String, String>): Response {
        println(concatUrl(url, path))
        return khttp.get(concatUrl(url, path), params.plus(authParams))
    }

    override fun post(path: String, params: Map<String, String>, files: List<FileLike>): Response {
        return khttp.post(concatUrl(url, path), params.plus(authParams), files = files)
    }
}

private fun concatUrl(url: Uri, path: String): String = url.buildUpon().appendPath(path).toString()