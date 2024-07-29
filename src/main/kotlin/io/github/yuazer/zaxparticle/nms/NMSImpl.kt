package io.github.yuazer.zaxparticle.nms

import io.github.yuazer.zaxparticle.objectives.BaseParticle
import org.bukkit.entity.Entity
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.nmsProxy
import taboolib.module.nms.sendPacket

class NMSImpl :NMS(){
    override fun sendParticle(entity: Entity, baseParticle: BaseParticle) {
        taboolib.platform.util.onlinePlayers.forEach {
//            it.castSafely<Player>()?.sendPacket(PacketParticle().createMyParticlePacket(entity,baseParticle))
            it.sendPacket(PacketParticle().createMyParticlePacket(entity,baseParticle))
        }
    }
    companion object {
        val INSTANCE by unsafeLazy {
            nmsProxy<NMS>()
        }
    }
}