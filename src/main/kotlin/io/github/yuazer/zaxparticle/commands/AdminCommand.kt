package io.github.yuazer.zaxparticle.commands

import io.github.yuazer.zaxparticle.ZaxParticle
import io.github.yuazer.zaxparticle.utils.MessageUtils
import io.github.yuazer.zaxparticle.utils.ParticleManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.player
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.onlinePlayers
import taboolib.platform.util.sendLang

@CommandHeader("zaxparticle", ["zp"], permission = "zaxparticle.admin")

object AdminCommand {
    @CommandBody(permission = "zaxparticle.reload")
    val reload = subCommand {
        execute<CommandSender> { sender, context, argument ->
            ZaxParticle.config.reload()
            ParticleManager.reloadParticleData()
            sender.sendLang("reload-message")
        }
    }
    @CommandBody(permission = "zaxparticle.run")
    val run = subCommand {
        dynamic("particleName") {
            suggestion<CommandSender>(uncheck = true) { sender, context ->
                ParticleManager.playerParticleManager[sender.name]
            }
            execute<CommandSender> { sender, context, argument ->
                // 获取参数的值
                if (sender is Player){
                    val user: Player = sender
                    val particleName = context["particleName"]
                    if (ParticleManager.playerParticleManager[user.name]?.contains(particleName) == true){
                        ParticleManager.getParticle(particleName)?.run(user)
                        MessageUtils.sendLangMsg(sender,"run-success")
                    }else{
                        MessageUtils.sendLangMsg(sender,"particle-not-have")
                    }
                }
            }
            player("player") {
                suggestion<CommandSender>(uncheck = true) { sender, context ->
                    onlinePlayers.map { it.name }
                }
                execute<CommandSender> { sender, context, argument ->
                    val user: ProxyPlayer =context.player("player")
                    val bukkitPlayer = user.castSafely<Player>()
                    val particleName = context["particleName"]
                    if (ParticleManager.playerParticleManager[user.name]?.contains(particleName) == true){
                        ParticleManager.getParticle(particleName)?.run(bukkitPlayer as Entity)
                        MessageUtils.sendLangMsg(sender,"run-success")
                    }else{
                        MessageUtils.sendLangMsg(sender,"particle-not-have")
                    }
                }
            }
        }
    }
    @CommandBody
    val add = subCommand {
        dynamic("particleName") {
            suggestion<CommandSender>(uncheck = true) { sender, context ->
                ParticleManager.particleMap.keys.toList()
            }
            execute<CommandSender> { sender, context, argument ->
                // 获取参数的值
                if (sender is Player){
                    val user: Player = sender
                    val particleName = context["particleName"]
                    ParticleManager.addParticleEffect(user.name,particleName)
                    MessageUtils.sendLangMsg(user,"add-success")
                }
            }
            player("player") {
                suggestion<CommandSender>(uncheck = true) { sender, context ->
                    ParticleManager.particleMap.keys.toList()
                }
                execute<CommandSender> { sender, context, argument ->
                    val user: ProxyPlayer =context.player("player")
                    val bukkitPlayer = user.castSafely<Player>()
                    val particleName = context["particleName"]
                    ParticleManager.addParticleEffect(user.name,particleName)
                    MessageUtils.sendLangMsg(bukkitPlayer!!,"add-success")
                }
            }
        }
    }
    @CommandBody
    val remove = subCommand {
        dynamic("particleName") {
            suggestion<CommandSender>(uncheck = true) { sender, context ->
                ParticleManager.playerParticleManager[sender.name]
            }
            execute<CommandSender> { sender, context, argument ->
                // 获取参数的值
                if (sender is Player){
                    val user: Player = sender
                    val particleName = context["particleName"]
                    ParticleManager.removeParticleEffect(user.name,particleName)
                    MessageUtils.sendLangMsg(user,"remove-success")
                }
            }
            player("player") {
                suggestion<CommandSender>(uncheck = true) { sender, context ->
                    onlinePlayers.map { it.name }
                }
                execute<CommandSender> { sender, context, argument ->
                    val user: ProxyPlayer =context.player("player")
                    val bukkitPlayer = user.castSafely<Player>()
                    val particleName = context["particleName"]
                    ParticleManager.removeParticleEffect(user.name,particleName)
                    MessageUtils.sendLangMsg(bukkitPlayer!!,"remove-success")
                }
            }
        }
    }
}