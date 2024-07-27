package io.github.yuazer.zaxparticle.data

import io.github.yuazer.zaxparticle.ZaxParticle.config

object DataLoader {
    fun saveData(){
        if (config.getString("StoreType").equals("YAML",true)){
            YamlStore.save()
        }
    }
    fun loadData(){
        if (config.getString("StoreType").equals("YAML",true)){
            YamlStore.load()
        }
    }
}