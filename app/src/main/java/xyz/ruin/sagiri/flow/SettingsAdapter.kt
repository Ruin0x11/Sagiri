package xyz.ruin.sagiri.flow

import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import xyz.ruin.sagiri.R

enum class SettingsMenuType {
    General,
    Servers;

    fun toScreen(): Any = when(this) {
        General -> SettingsGeneralScreen()
        Servers -> ServerListScreen()
    }
}

internal class SettingsAdapter : BaseAdapter() {
    override fun getCount(): Int = SettingsMenuType.values().size

    override fun getItem(position: Int): SettingsMenuType = when (position) {
        0 -> SettingsMenuType.General
        1 -> SettingsMenuType.Servers
        else -> SettingsMenuType.General
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val menu = getItem(position)

        val text = when(menu) {
            SettingsMenuType.General -> R.string.general_settings
            SettingsMenuType.Servers -> R.string.server_list
        }.let { parent.context.getString(it) }

        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(parent.context)
            view = inflater.inflate(R.layout.settings_screen_row_view, parent, false)
        }
        (view!!.findViewById<View>(R.id.setting_name) as TextView).text = text
        return view
    }
}