package xyz.ruin.sagiri.flow

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView
import xyz.ruin.sagiri.android.AccountDao
import xyz.ruin.sagiri.android.AppDatabase

class ServerListView(context: Context, attrs: AttributeSet) : ListView(context, attrs)  {
    private var storage: AccountDao? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val server = adapter.getItem(position)
            // Flow.get(this@ServerListView).set(EditServerScreen(server.id))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        storage = AppDatabase.getInstance(context).accountDao()
    }
}