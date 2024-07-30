package io.github.yuazer.zaxparticle.objectives

import io.github.yuazer.zaxparticle.ZaxParticle
import io.github.yuazer.zaxparticle.nms.NMSImpl
import io.github.yuazer.zaxparticle.utils.MessageUtils
import io.github.yuazer.zaxparticle.utils.ParticleManager
import io.github.yuazer.zaxparticle.utils.ParticleUtils
import io.github.yuazer.zaxparticle.utils.PlayerUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyParticle
import taboolib.common.platform.function.submitAsync
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import taboolib.platform.compat.replacePlaceholder

data class Particle(val name: String, val particleConfig: Configuration) {
    var nameParticleMap: MutableMap<String, BaseParticle> = mutableMapOf()

    init {
        particleConfig.getConfigurationSection("Particle")?.getKeys(false)?.forEach {
            nameParticleMap[it] = BaseParticle(particleConfig, it)
        }
    }

    fun run(entity: Entity) {
        //TODO 生成粒子
        if (ParticleManager.playerParticleManager[entity.name] != null) {
            if (ParticleManager.playerParticleManager[entity.name]?.contains(name) == true) {
                executeAction(entity, particleConfig.getStringList("Manager.loop"), 0)
            } else {
                MessageUtils.sendLangMsg(entity, "particle-not-have")
            }
        }
    }

    private fun executeAction(entity: Entity, stringList: List<String>, index: Int) {
        if (index >= stringList.size) {
            return
        }
        if (ParticleManager.playerParticleManager[entity.name]?.contains(name) == false) {
            return
        }
        val action = stringList[index]
        val (key, value) = splitString(action)
        if (key.equals("particle", true)) {
            nameParticleMap[value]?.runParticle(entity)
            executeAction(entity, stringList, index + 1)
        } else if (key.equals("delay", true)) {
            submitAsync(delay = value.toLong()) {
                executeAction(entity, stringList, index + 1)
            }
        } else if (key.equals("return", true)) {
            executeAction(entity, stringList, value.toInt())
        } else if (key.equals("command", true)) {
            if (entity is Player) {
                Bukkit.getScheduler().runTask(BukkitPlugin.getInstance(), Runnable {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value.replacePlaceholder(entity))
                })
            }
            executeAction(entity, stringList, index + 1)
        } else if (key.equals("kether", true)) {
            if (entity is Player) {
                PlayerUtils.runKether(particleConfig.getStringList("Kether.${value}"), entity)
            }
            executeAction(entity, stringList, index + 1)
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
     val type: Particle = Particle.valueOf(particleConfig.getString("Particle.${name}.type")!!)
    val proxyType:ProxyParticle = ProxyParticle.valueOf(particleConfig.getString("Particle.${name}.type")!!)
     val x: Double = particleConfig.getDouble("Particle.${name}.x")
     val y: Double = particleConfig.getDouble("Particle.${name}.y")
     val z: Double = particleConfig.getDouble("Particle.${name}.z")
    val offsetX: Double = particleConfig.getDouble("Particle.${name}.offsetX",0.0)
    val offsetY: Double = particleConfig.getDouble("Particle.${name}.offsetY",0.0)
    val offsetZ: Double = particleConfig.getDouble("Particle.${name}.offsetZ",0.0)
     val count: Int = particleConfig.getInt("Particle.${name}.count")
     val speed: Double = particleConfig.getDouble("Particle.${name}.speed")
     val data: List<String>? = particleConfig.getStringList("Particle.${name}.data")
    fun runParticle(entity: Entity) {
        if (ZaxParticle.config.getString("ImplMode").equals("bukkit",true)){
            ParticleUtils.spawnParticle(entity, proxyType,  x, y, z,offsetX,offsetY,offsetZ ,count,speed, data)
        }else{
            NMSImpl.INSTANCE.sendParticle(entity,this)
        }
    }
    fun getLocation(entity: Entity):Location {
        return entity.location.add(x,y,z)
    }
}