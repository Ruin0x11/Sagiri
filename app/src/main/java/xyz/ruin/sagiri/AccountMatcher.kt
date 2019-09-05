package xyz.ruin.sagiri

import android.content.Context
import android.net.Uri
import xyz.ruin.sagiri.android.Account
import xyz.ruin.sagiri.android.AppDatabase

class AccountMatcher(val applicationContext: Context) {
    fun findAccount(uri: Uri): Account? {
        val accounts = AppDatabase.getInstance(applicationContext).accountDao().getAccounts()

        return accounts.value?.find { urlMatches(it.baseUrl, uri) }
    }

    private fun urlMatches(accountUri: Uri, targetUri: Uri): Boolean =
        accountUri.authority == targetUri.authority

}