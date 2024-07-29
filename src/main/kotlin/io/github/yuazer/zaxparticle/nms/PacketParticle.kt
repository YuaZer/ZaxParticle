package io.github.yuazer.zaxparticle.nms

import io.github.yuazer.zaxparticle.objectives.BaseParticle
import taboolib.module.nms.NMSParticleImpl
import org.bukkit.Particle
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

class PacketParticle {
    fun createMyParticlePacket(entity: Entity,baseParticle: BaseParticle): Any {
        val nmsParticleImpl = NMSParticleImpl()
        return nmsParticleImpl.createParticlePacketCustom(
            baseParticle.type,
            baseParticle.getLocation(entity),
            Vector(0,0,0),
            baseParticle.speed,
            baseParticle.count,
            baseParticle.data
            )
    }
    fun NMSParticleImpl.createParticlePacketCustom(
        particle: Particle,
        location: Location,
        offset: Vector,
        speed: Double,
        count: Int,
        data: Any?,
    ): Any {

        return Any()
    }
}