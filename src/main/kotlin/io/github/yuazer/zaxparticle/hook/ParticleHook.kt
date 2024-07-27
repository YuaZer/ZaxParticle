package io.github.yuazer.zaxparticle.hook

import io.github.yuazer.zaxparticle.utils.ParticleManager
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object ParticleHook  : PlaceholderExpansion {
    // 变量前缀
    override val identifier: String = "zaxparticle"

    // 变量操作
    override fun onPlaceholderRequest(player: Player?, args: String): String {
        val particleName = args
        if (player != null) {
            return ParticleManager.playerParticleManager[player.name]?.contains(particleName).toString()
        }
        return "player-null"
    }
}