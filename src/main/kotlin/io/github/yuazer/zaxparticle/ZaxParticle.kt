package io.github.yuazer.zaxparticle

import io.github.yuazer.zaxparticle.data.DataLoader
import io.github.yuazer.zaxparticle.utils.ParticleManager
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.info
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.platform.BukkitPlugin
import java.io.File

object ZaxParticle : Plugin() {
    @Config("config.yml")
    lateinit var config: ConfigFile

    @Config("database.yml")
    lateinit var databaseConfig: ConfigFile
    @Config("particles/test.yml")
    lateinit var testParticleConfig:ConfigFile
    override fun onEnable() {
        initFolder()
        //加载粒子数据
        ParticleManager.reloadParticleData()

        DataLoader.loadData()
        logLoaded()
    }

    override fun onDisable() {
        DataLoader.saveData()
    }
    fun initFolder(){
        val dataFolder = File(getDataFolder(),"data")
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }
    }
    private fun logLoaded() {
        val description = BukkitPlugin.getInstance().description
        val version = description.version
        val pluginName = description.name

        info("""
            
        §e══════════════════════════════════════════════
        §e|      §b ________        __       ___  ___    §e|
        §e|      §b("      "\      /""\     |"  \/"  |   §e|
        §e|      §b \___/   :)    /    \     \   \  /    §e|
        §e|      §b   /  ___/    /' /\  \     \\  \/     §e|
        §e|      §b  //  \__    //  __'  \    /\.  \     §e|
        §e|      §b (:   / "\  /   /  \\  \  /  \   \    §e|
        §e|      §b  \_______)(___/    \___)|___/\___|   §e|
        §e══════════════════════════════════════════════
        §6[§eZAX§6] §aPlugin: §b$pluginName
        §6[§eZAX§6] §aVersion: §e$version
        §6[§eZAX§6] §aAuthor: §bZ菌[QQ:1109132]
        §e══════════════════════════════════════════════
    """.trimIndent())
    }

}