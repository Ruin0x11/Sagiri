package xyz.ruin.sagiri.flow

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView
import flow.Flow

class SettingsView(context: Context, attrs: AttributeSet) : ListView(context, attrs) {
    private val adapter = SettingsAdapter()

    override fun onFinishInflate() {
        super.onFinishInflate()
        setAdapter(adapter)

        onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val menu = adapter.getItem(position)
            Flow.get(this@SettingsView).set(menu.toScreen())
        }
    }
}