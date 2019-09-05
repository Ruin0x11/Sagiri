package xyz.ruin.sagiri.flow

import flow.ClassKey
import flow.TreeKey

class SettingsGeneralScreen : ClassKey(), TreeKey {
    override fun getParentKey(): Any {
        return SettingsUiKey()
    }
}
