package io.github.yuazer.zaxparticle.utils

import io.github.yuazer.zaxparticle.`object`.Particle
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.submitAsync
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object ParticleManager {
    val particleMap:ConcurrentHashMap<String,Particle> = ConcurrentHashMap()
    val playerParticleManager:ConcurrentHashMap<String,MutableList<String>> = ConcurrentHashMap()
    fun reloadParticleData(){
        submitAsync(now = true) {
            particleMap.clear()
            val particlesFolder = File(getDataFolder(), "particles")
            loadParticleData(particlesFolder)
        }
    }
    fun addParticleEffect(player: String, effect: String) {
        val currentEffects = playerParticleManager[player] ?: mutableListOf()
        currentEffects.add(effect)
        playerParticleManager[player] = currentEffects
    }

    fun getParticleEffects(player: String): MutableList<String>? {
        return playerParticleManager[player]
    }

    fun updateParticleEffects(player: String, effects: MutableList<String>) {
        playerParticleManager[player] = effects
    }

    fun removeParticleEffect(player: String, effect: String) {
        playerParticleManager[player]?.remove(effect)
    }
    private fun loadParticleData(folder:File){
        folder.listFiles()?.forEach {
            if (it.isDirectory){
                loadParticleData(it)
            }else{
                val particleName = it.name.replace(".yml","")
                val particleYaml = Configuration.loadFromFile(it,Type.YAML)
                saveParticle(particleName,Particle(particleName,particleYaml))
            }
        }
    }
    /**
     * 获取粒子对象
     *
     * @param key 粒子的唯一标识
     * @return 对应的粒子对象，如果不存在则返回null
     */
    fun getParticle(key: String): Particle? {
        return particleMap[key]
    }

    /**
     * 保存粒子对象
     *
     * @param key 粒子的唯一标识
     * @param particle 要保存的粒子对象
     */
    fun saveParticle(key: String, particle: Particle) {
        particleMap[key] = particle
    }

    /**
     * 删除粒子对象
     *
     * @param key 粒子的唯一标识
     */
    fun removeParticle(key: String) {
        particleMap.remove(key)
    }
}