package io.nekohasekai.sagernet.ui

import android.content.Context
import com.google.gson.Gson
import io.nekohasekai.sagernet.database.DataStore
import io.nekohasekai.sagernet.database.ProfileManager
import io.nekohasekai.sagernet.database.ProxyEntity
import io.nekohasekai.sagernet.fmt.v2ray.VMessBean
import io.nekohasekai.sagernet.ktx.Logs
import java.io.InputStreamReader

object DefaultConfigLoader {
    
    fun loadDefaultVLESSConfig(context: Context) {
        try {
            // Check if default config already loaded
            if (DataStore.defaultConfigLoaded) return
            
            // Read config from assets
            val inputStream = context.assets.open("default_vless_config.json")
            val reader = InputStreamReader(inputStream)
            val configJson = reader.readText()
            reader.close()
            
            // Parse JSON config
            val gson = Gson()
            val configData = gson.fromJson(configJson, DefaultVLESSConfig::class.java)
            
            // Create VLESS profile
            val vmessBean = VMessBean().apply {
                name = configData.remarks
                serverAddress = configData.serverAddress
                serverPort = configData.serverPort
                uuid = configData.uuid
                encryption = configData.encryption
                alterId = -1 // This marks it as VLESS
                type = configData.type
                security = configData.security
                sni = configData.sni
                alpn = configData.alpn
                allowInsecure = configData.allowInsecure
            }
            
            // Create proxy entity
            val proxyEntity = ProxyEntity(
                type = ProxyEntity.TYPE_VMESS,
                vmessBean = vmessBean
            )
            
            // Save to database
            val profileId = ProfileManager.create(proxyEntity)
            
            // Set as selected profile if no profile selected
            if (DataStore.selectedProxy == 0L) {
                DataStore.selectedProxy = profileId
            }
            
            // Mark default config as loaded
            DataStore.defaultConfigLoaded = true
            
            Logs.d("Default VLESS config loaded successfully")
            
        } catch (e: Exception) {
            Logs.e("Failed to load default VLESS config", e)
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