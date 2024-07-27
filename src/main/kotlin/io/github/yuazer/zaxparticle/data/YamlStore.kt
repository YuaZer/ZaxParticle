package io.github.yuazer.zaxparticle.data

import io.github.yuazer.zaxparticle.utils.ParticleManager
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.submitAsync
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type

object YamlStore :IDataStore{

    override fun save() {
        submitAsync(now = true) {
            ParticleManager.playerParticleManager.forEach { (key, value) ->
                val file = newFile(getDataFolder(),"data/${key}.yml",true)
                val data = Configuration.loadFromFile(file, Type.YAML)
                data[key] = value
                data.saveToFile(file)
            }
        }
    }

    override fun load() {
        submitAsync(now = true) {
            val file = newFile(getDataFolder(), "data",true)
            if (!file.exists()){
                println("data not found")
                return@submitAsync
            }
            if (file.isDirectory){
                file.listFiles()?.forEach {
                    if (it.name.endsWith(".yml")){
                        val name = it.name.replace(".yml","")
                        val data = Configuration.loadFromFile(it, Type.YAML)
                        println("list:\n"+data.getStringList(name).toMutableList())
                        ParticleManager.playerParticleManager[name] = data.getStringList(name).toMutableList()
                    }
                }
            }
        }
    }
}