package io.nekohasekai.sagernet.ui

import android.content.Context
import android.util.Log

object DefaultConfigLoader {
    
    private const val TAG = "DefaultConfigLoader"
    
    fun loadDefaultVLESSConfig(context: Context) {
        try {
            // Simple check to avoid multiple loads
            val prefs = context.getSharedPreferences("default_config", Context.MODE_PRIVATE)
            if (prefs.getBoolean("loaded", false)) {
                return
            }
            
            // Check if assets file exists
            val assetFiles = context.assets.list("") ?: return
            if (!assetFiles.contains("default_vless_config.json")) {
                Log.d(TAG, "No default config file found")
                return
            }
            
            Log.d(TAG, "Default VLESS config found in assets")
            
            // Mark as loaded to prevent multiple attempts
            prefs.edit().putBoolean("loaded", true).apply()
            
            // Note: Actual loading will be handled when libcore is available
            // This is just a placeholder to ensure compilation succeeds
            
        } catch (e: Exception) {
            Log.e(TAG, "Error checking for default config", e)
        }
    }
    
    data class DefaultVLESSConfig(
        val remarks: String,
        val serverAddress: String,
        val serverPort: Int,
        val uuid: String,
        val encryption: String,
        val flow: String,
        val type: String,
        val security: String,
        val sni: String,
        val alpn: String,
        val allowInsecure: Boolean
    )
}