package io.github.yuazer.zaxparticle.data.mysql

import io.github.yuazer.zaxparticle.ZaxParticle.databaseConfig
import io.github.yuazer.zaxparticle.data.IDataStore
import io.github.yuazer.zaxparticle.utils.ParticleManager

object MySQLStore : IDataStore {
    private val db = Database(databaseConfig)
    override fun save() {
        ParticleManager.playerParticleManager.keys.forEach {
            db.upsert(it, ParticleManager.playerParticleManager[it]?.toList() ?: return)
        }
    }

    override fun load() {
        db.getAllPlayers().forEach {
            ParticleManager.playerParticleManager[it] = db.select(it)?.toMutableList()!!
        }
    }
}