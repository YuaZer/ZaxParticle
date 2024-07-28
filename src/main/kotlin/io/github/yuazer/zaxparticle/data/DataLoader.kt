package io.github.yuazer.zaxparticle.data

import io.github.yuazer.zaxparticle.ZaxParticle.config
import io.github.yuazer.zaxparticle.data.mysql.MySQLStore
import io.github.yuazer.zaxparticle.data.yaml.YamlStore

object DataLoader {
    fun saveData(){
        if (config.getString("StoreType").equals("YAML",true)){
            YamlStore.save()
        }else{
            MySQLStore.save()
        }
    }
    fun loadData(){
        if (config.getString("StoreType").equals("YAML",true)){
            YamlStore.load()
        }else{
            MySQLStore.load()
        }
    }
}