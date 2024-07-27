package io.github.yuazer.zaxparticle.data

import io.github.yuazer.zaxparticle.utils.ParticleManager
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import java.io.File

object YamlStore :IDataStore{

    override fun save() {
        ParticleManager.playerParticleManager.forEach { (key, value) ->
            val file = newFile(getDataFolder(),"data/${key}.yml",true)
            val data = Configuration.loadFromFile(file, Type.YAML)
            data[key] = value
            data.saveToFile(file)
        }
    }

    override fun load() {
        val file = newFile(getDataFolder(), "data",true)
        if (!file.exists()){
            println("data not found")
            return
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