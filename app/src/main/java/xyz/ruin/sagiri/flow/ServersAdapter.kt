package xyz.ruin.sagiri.flow

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import xyz.ruin.sagiri.android.Account
import java.util.ArrayList
import xyz.ruin.sagiri.R;

class ServersAdapter : BaseAdapter() {
    private val servers = ArrayList<Account>()

    fun setServers(servers: List<Account>) {
        this.servers.clear()
        this.servers.addAll(servers)
        Log.i(ServersAdapter::class.java.name, "Set servers ${this.servers.size}")
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return servers.size
    }

    override fun getItem(position: Int): Account {
        return servers[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val server = getItem(position)
        var view: View? = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(parent.context)
            view = inflater.inflate(R.layout.list_servers_screen_row_view, parent, false)
        }
        (view!!.findViewById<View>(R.id.server_type) as TextView).text = server.name
        (view.findViewById<View>(R.id.server_uri) as TextView).text = server.baseUrl.toString()
        return view
    }
}
