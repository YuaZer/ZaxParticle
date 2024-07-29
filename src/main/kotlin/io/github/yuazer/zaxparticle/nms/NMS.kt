package io.github.yuazer.zaxparticle.nms

import io.github.yuazer.zaxparticle.objectives.BaseParticle
import org.bukkit.entity.Entity

abstract class NMS {
    abstract fun sendParticle(entity: Entity, baseParticle: BaseParticle)
}