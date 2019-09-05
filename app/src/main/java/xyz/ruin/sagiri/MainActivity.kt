package xyz.ruin.sagiri

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import flow.*
import xyz.ruin.sagiri.android.AccountViewModel
import xyz.ruin.sagiri.flow.*

class MainActivity : AppCompatActivity() {
    private var accountViewModel: AccountViewModel? = null
    private var adapter: ServersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        adapter = ServersAdapter()
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        accountViewModel!!.allAccounts.observe(this, Observer { adapter!!.setServers(it) })
    }

    override fun attachBaseContext(baseContext: Context) {
        Flow.configure(baseContext, this)
            .addServicesFactory(FlowServices())
            .defaultKey(WelcomeScreen())
            .dispatcher(KeyDispatcher.configure(this, Changer()).build())
            .install()
            .let { super.attachBaseContext(it) }

    }

    override fun onBackPressed() {
        if (!Flow.get(this).goBack()) {
            super.onBackPressed()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "sagiri"
    }

    private inner class Changer : KeyChanger {
        override fun changeKey(
            outgoingState: State?, incomingState: State,
            direction: Direction, incomingContexts: Map<Any, Context>,
            callback: TraversalCallback
        ) {

            val key = incomingState.getKey<Any>()
            val context = incomingContexts[key] ?: error("no context")

            if (outgoingState != null) {
                val view = findViewById<ViewGroup>(android.R.id.content)
                outgoingState.save(view.getChildAt(0))
            }

            val view = when (key) {
                is WelcomeScreen -> showLayout(context, R.layout.settings_view)
                is SettingsGeneralScreen -> showLayout(context, R.layout.settings_general_view)
                is ServerListScreen -> showLayout(context, R.layout.servers_list_view)
                else -> showLayout(context, R.layout.settings_view)
            }

            when (view) {
                is ServerListView -> {
                    view.adapter = adapter
                }
            }

            incomingState.restore(view)
            setContentView(view)

            callback.onTraversalCompleted()
        }

        private fun showLayout(context: Context, @LayoutRes layout: Int): View {
            val inflater = LayoutInflater.from(context)
            return inflater.inflate(layout, null)
        }

        private fun showKeyAsText(context: Context, key: Any, nextScreenOnClick: Any?): View {
            val view = TextView(context)
            view.text = key.toString()

            if (nextScreenOnClick == null) {
                view.setOnClickListener(null)
            } else {
                view.setOnClickListener { v -> Flow.get(v).set(nextScreenOnClick) }
            }
            return view
        }
    }
}
