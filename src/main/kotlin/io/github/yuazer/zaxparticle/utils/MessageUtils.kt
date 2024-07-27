package io.github.yuazer.zaxparticle.utils

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.platform.util.asLangText

object MessageUtils {
    fun sendLangMsg(sender: CommandSender,path:String){
        sender.sendMessage(sender.asLangText(path))
    }
    fun sendLangMsg(sender: Player,path:String){
        sender.sendMessage(sender.asLangText(path))
    }
}