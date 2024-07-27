package io.github.yuazer.zaxparticle.events

import io.github.yuazer.zaxparticle.ZaxParticle
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.lang.event.PlayerSelectLocaleEvent
import taboolib.module.lang.event.SystemSelectLocaleEvent

object LangEvent {
    @SubscribeEvent
    fun lang(event: PlayerSelectLocaleEvent) {
        event.locale = ZaxParticle.config.getString("Lang", "zh_CN")!!
    }

    @SubscribeEvent
    fun lang(event: SystemSelectLocaleEvent) {
        event.locale = ZaxParticle.config.getString("Lang", "zh_CN")!!
    }
}