package demo.tdsss.wifistate.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import demo.tdsss.wifistate.R

/**
 * @author TDSSS
 * @datetime 2023/11/22 17:47
 */
class WifiIpWidget : AppWidgetProvider() {

    private val TAG = "wifi state widget"

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        updateInfo(context)
        Log.w(TAG, "onEnabled: " )
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.w(TAG, "onUpdate: " )
        updateInfo(context)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        Log.w(TAG, "onAppWidgetOptionsChanged: " )
        val intent = Intent(context,this.javaClass)
        intent.action = "touch"
        val remoteViews = RemoteViews(context?.packageName, R.layout.widget_layout).also {
            it.setOnClickPendingIntent(
                R.id.wifi_widget,
                PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                ))
        }
        appWidgetManager!!.partiallyUpdateAppWidget(appWidgetId, remoteViews)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.w(TAG, "onDisabled: " )
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Log.w(TAG, "onDeleted: ", )
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
//        Log.w(TAG, "onReceive: " )
//        Log.w(TAG, "action: ${intent?.action}" )
//        updateInfo(context)
        if(intent?.action == "touch"){
            Log.w(TAG, "onReceive: action == touch" )
            updateInfo(context)
        }
        if (intent?.action != null && intent.action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION) && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.w(TAG, "onReceive: NETWORK_STATE_CHANGED_ACTION" )
            updateInfo(context)
        }
    }

    private fun updateInfo(context : Context?){
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(networkInfo)
        if(capabilities == null || !(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))){
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(ComponentName(context,this.javaClass))
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout).also {
                it.setTextViewText(R.id.wifiAddress, "未连接wifi")
            }
            appWidgetManager.partiallyUpdateAppWidget(ids, remoteViews)
            return
        }
        val linkProperties = connectivityManager.getLinkProperties(networkInfo)
        val address = linkProperties?.linkAddresses
        Log.w(TAG, "onReceive address: $address" )
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(context,this.javaClass))
        val intent = Intent(context,this.javaClass)
        intent.action = "touch"
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout).also {
            it.setTextViewText(R.id.wifiAddress, "${address?.get(1)}")
            it.setOnClickPendingIntent(
                R.id.wifi_widget,
                PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                ))
        }
        appWidgetManager.partiallyUpdateAppWidget(ids, remoteViews)
    }
}