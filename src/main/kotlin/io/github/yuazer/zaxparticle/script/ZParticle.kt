package io.github.yuazer.zaxparticle.script

import io.github.yuazer.zaxparticle.utils.ParticleUtils
import org.bukkit.Location
import taboolib.common.platform.ProxyParticle
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

class ZParticle(val particleToken: ParsedAction<*>,val location: ParsedAction<*>): ScriptAction<Void>() {
    override fun run(frame: ScriptFrame): CompletableFuture<Void> {
        val script = frame.script()
        frame.newFrame(particleToken).run<String>().thenAccept { token->
            frame.newFrame(location).run<Location>().thenAccept { location ->
                val tokens = token.split(" ")
                val particleType = tokens[0]
                val proxyParticle = ProxyParticle.valueOf(particleType)
                val offsetX = getTokenValue(token,"-offsetX",0.00)
                val offsetY = getTokenValue(token,"-offsetZ",0.00)
                val offsetZ = getTokenValue(token,"-offsetZ",0.00)
                val speed = getTokenValue(token,"-speed",0.00)
                val count = getTokenValue(token,"-count",1)
                val dataList = filterDataElements(token,"-data")
                ParticleUtils.spawnParticle(proxyParticle,location,offsetX,offsetY,offsetZ,count,speed,dataList)
            }
        }
        println(script)
        return CompletableFuture.completedFuture(null)
    }
    fun filterDataElements(token: String, flag: String): List<String> {
        val tokens = token.split(" ")
        val index = tokens.indexOf(flag)

        return if (index != -1 && index + 1 < tokens.size) {
            tokens.subList(index + 1, tokens.size)
        } else {
            emptyList()
        }
    }
    fun getTokenValue(token: String,key:String):String{
        var result = "0"
        val tokens = token.split(" ")
        if (tokens.contains(key)){
            val value = getOffsetValue(token, key)
            result = if (value != null && !value.contains("-")) value else "0"
        }
        return result
    }
    fun getTokenValue(token: String,key:String,default:Int):Int{
        var result = default
        val tokens = token.split(" ")
        if (tokens.contains(key)){
            val value = getOffsetValue(token, key)
            result = if (value != null && !value.contains("-")) value.toInt() else default
        }
        return result
    }
    fun getTokenValue(token: String,key:String,default:Double):Double{
        var result = default
        val tokens = token.split(" ")
        if (tokens.contains(key)){
            val value = getOffsetValue(token, key)
            result = if (value != null && !value.contains("-")) value.toDouble() else default
        }
        return result
    }
    fun getOffsetValue(token: String, offsetFlag: String): String? {
        val tokens = token.split(" ").filter { it.isNotBlank() }
        val index = tokens.indexOf(offsetFlag)

        return if (index != -1 && index + 1 < tokens.size) {
            val value = tokens[index + 1]
            if (value.contains("-")) null else value
        } else {
            null
        }
    }
    companion object {
        @KetherParser(["zparticle"], namespace = "zaxparticle", shared = true)
        fun parser() = scriptParser {
            val particleToken = it.next(ArgTypes.ACTION)
            val location = it.next(ArgTypes.ACTION)
            ZParticle(particleToken, location)
        }
    }
}