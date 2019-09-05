package xyz.ruin.sagiri.flow

import flow.ClassKey
import flow.TreeKey

class ServerListScreen : ClassKey(), TreeKey {
    override fun getParentKey(): Any = SettingsUiKey()
}
