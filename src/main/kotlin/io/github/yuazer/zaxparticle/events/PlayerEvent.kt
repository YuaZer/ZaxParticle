package io.github.yuazer.zaxparticle.events

import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent

object PlayerEvent {
    @SubscribeEvent
    fun onJoin(event: PlayerJoinEvent){
        val name = event.player.name

    }
}