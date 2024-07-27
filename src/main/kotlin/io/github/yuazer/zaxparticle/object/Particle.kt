package io.github.yuazer.zaxparticle.`object`

import io.github.yuazer.zaxparticle.utils.MessageUtils
import io.github.yuazer.zaxparticle.utils.ParticleManager
import io.github.yuazer.zaxparticle.utils.ParticleUtils
import org.bukkit.Particle
import org.bukkit.entity.Entity
import taboolib.common.platform.function.submitAsync
import taboolib.module.configuration.Configuration

data class Particle(val name:String,val particleConfig: Configuration) {
    var nameParticleMap: MutableMap<String, BaseParticle> = mutableMapOf()

    init {
        particleConfig.getConfigurationSection("Particle")?.getKeys(false)?.forEach {
            nameParticleMap[it] = BaseParticle(particleConfig, it)
        }
    }

    fun run(entity: Entity) {
        //TODO 生成粒子
        if (ParticleManager.playerParticleManager[entity.name]!=null){
            if (ParticleManager.playerParticleManager[entity.name]?.contains(name) == true){
                executeAction(entity,particleConfig.getStringList("Manager.loop"),0)
            }else{
                MessageUtils.sendLangMsg(entity,"particle-not-have")
            }
        }
    }

    private fun executeAction(entity: Entity, stringList: List<String>, index: Int) {
        if (index>=stringList.size){
            return
        }
        if (ParticleManager.playerParticleManager[entity.name]?.contains(name) == false){
            return
        }
        val action = stringList[index]
        val (key, value) = splitString(action)
        if (key.equals("particle", true)) {
            nameParticleMap[value]?.runParticle(entity)
            executeAction(entity, stringList, index+1)
        } else if (key.equals("delay", true)) {
            submitAsync(delay = value.toLong()) {
                executeAction(entity, stringList, index+1)
            }
        }else if (key.equals("return",true)){
            executeAction(entity, stringList, value.toInt())
        }
    }

    fun splitString(input: String): Pair<String, String> {
        val index = input.indexOf(":")
        return if (index != -1) {
            val key = input.substring(0, index).trim()
            val value = input.substring(index + 1).trim()
            key to value
        } else {
            input to ""
        }
    }
}

class BaseParticle(particleConfig: Configuration, name: String) {
    private val type: Particle = Particle.valueOf(particleConfig.getString("Particle.${name}.type")!!)
    private val x: Double = particleConfig.getDouble("Particle.${name}.x")
    private val y: Double = particleConfig.getDouble("Particle.${name}.y")
    private val z: Double = particleConfig.getDouble("Particle.${name}.z")
    private val count: Int = particleConfig.getInt("Particle.${name}.count")
    private val speed: Double = particleConfig.getDouble("Particle.${name}.speed")
    private val data: List<String>? = particleConfig.getStringList("Particle.${name}.data")

    fun runParticle(entity: Entity) {
        ParticleUtils.spawnParticle(entity, type, count, x, y, z, speed, data)
    }
}