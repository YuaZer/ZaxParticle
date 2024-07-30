package io.github.yuazer.zaxparticle.utils

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyParticle
import taboolib.common.platform.function.onlinePlayers
import taboolib.common.platform.function.submit
import taboolib.common.util.ProxyLocation
import taboolib.common.util.Vector
import java.awt.Color

object ParticleUtils {
    /**
     * 生成粒子效果
     *
     * @param entity      粒子生成的实体中心
     * @param particle    粒子类型
     * @param count       粒子的数量
     * @param offsetX     粒子的X轴偏移
     * @param offsetY     粒子的Y轴偏移
     * @param offsetZ     粒子的Z轴偏移
     * @param speed       粒子的速度
     * @param data        粒子的附加数据（List<String> 类型）
     */
    fun spawnParticle(
        entity: Entity,
        particle: Particle,
        count: Int,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double,
        speed: Double,
        data: List<String>?
    ) {

        submit {
            val world: World = entity.world
            val location: Location = entity.location

            when (particle) {
                Particle.REDSTONE -> {
                    // 如果是红石粉粒子，需要颜色数据
                    val r = (data?.getOrNull(0)?.toIntOrNull() ?: 255) / 255.0
                    val g = (data?.getOrNull(1)?.toIntOrNull() ?: 0) / 255.0
                    val b = (data?.getOrNull(2)?.toIntOrNull() ?: 0) / 255.0
                    world.spawnParticle(particle, location.clone().add(offsetX, offsetY, offsetZ), count, r, g, b)
                }
                Particle.ITEM_CRACK -> {
                    // 如果是物品破碎粒子，需要ItemStack数据
                    val itemStack = data?.firstOrNull()?.let { materialName ->
                        Material.matchMaterial(materialName)?.let { material ->
                            ItemStack(material)
                        }
                    }
                    if (itemStack != null) {
                        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, itemStack)
                    } else {
                        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed)
                    }
                }
                Particle.BLOCK_CRACK, Particle.BLOCK_DUST, Particle.FALLING_DUST -> {
                    // 如果是方块破碎粒子或落尘粒子，需要BlockData数据
                    val blockData = data?.firstOrNull()?.let { materialName ->
                        Material.matchMaterial(materialName)?.data
                    }
                    if (blockData != null) {
                        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, blockData)
                    } else {
                        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed)
                    }
                }
                Particle.CLOUD ->{
                    world.spawnParticle(particle, location.clone().add(offsetX,offsetY,offsetZ), 0)
                }
                else -> {
                    // 对于其他粒子，不使用额外数据
                    world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed)
                }
            }
        }
    }
    fun spawnParticle(
        entity: Entity,
        particle: ProxyParticle,
        addX: Double,
        addY: Double,
        addZ: Double,
        offsetX:Double,
        offsetY:Double,
        offsetZ:Double,
        count: Int,
        speed: Double,
        data: List<String>?
    ) {

        submit {
            val world: World = entity.world
            val location: ProxyLocation = ProxyLocation(world.name,entity.location.x+addX,entity.location.y+addY,entity.location.z+addZ)

            when (particle) {
                ProxyParticle.REDSTONE -> {
                    // 如果是红石粉粒子，需要颜色数据
                    val r = (data?.getOrNull(0)?.toIntOrNull() ?: 255) / 255.0
                    val g = (data?.getOrNull(1)?.toIntOrNull() ?: 0) / 255.0
                    val b = (data?.getOrNull(2)?.toIntOrNull() ?: 0) / 255.0
                    val size = data?.getOrNull(3)?.toFloatOrNull() ?: 1.0f
                    val color = Color(r.toInt(),g.toInt(),b.toInt())
                    val data1 = ProxyParticle.DustData(color,size)
                    onlinePlayers().forEach {
                        particle.sendTo(it,location, Vector(offsetX,offsetY,offsetZ),count,speed,data1)
                    }
                }
                ProxyParticle.ITEM_CRACK -> {
                    val material: String = data?.getOrNull(0).toString()
                    val itemData: Int = data?.getOrNull(1)?.toIntOrNull() ?:0
                    val name: String = data?.getOrNull(2).toString()
                    val customModelData: Int = data?.getOrNull(3)?.toIntOrNull()?:0
                    val loreList = data?.filter { it.startsWith("lore:") }!!
                    val data1 = ProxyParticle.ItemData(material,itemData,name,loreList,customModelData)
                    onlinePlayers().forEach {
                        particle.sendTo(it,location,Vector(offsetX,offsetY,offsetZ),count,speed,data1)
                    }
                }
                ProxyParticle.BLOCK_CRACK, ProxyParticle.BLOCK_DUST, ProxyParticle.FALLING_DUST -> {
                    val material: String = data?.getOrNull(0).toString()
                    val block: Int = data?.getOrNull(1)?.toIntOrNull() ?:0
                    val blockData = ProxyParticle.BlockData(material,block)
                    onlinePlayers().forEach {
                        particle.sendTo(it,location,Vector(offsetX,offsetY,offsetZ),count,speed,blockData)
                    }
                }
                else -> {
                    onlinePlayers().forEach {
                        particle.sendTo(it,location,Vector(offsetX,offsetY,offsetZ),count,speed)
                    }                }
            }
        }
    }
    fun spawnParticle(
        particle: ProxyParticle,
        bukkitLocation: Location,
        offsetX:Double,
        offsetY:Double,
        offsetZ:Double,
        count: Int,
        speed: Double,
        data: List<String>?
    ) {

        submit {
            val world: World = bukkitLocation.world
            val location: ProxyLocation = ProxyLocation(world.name,bukkitLocation.x,bukkitLocation.y,bukkitLocation.z)

            when (particle) {
                ProxyParticle.REDSTONE -> {
                    // 如果是红石粉粒子，需要颜色数据
                    val r = (data?.getOrNull(0)?.toIntOrNull() ?: 255) / 255.0
                    val g = (data?.getOrNull(1)?.toIntOrNull() ?: 0) / 255.0
                    val b = (data?.getOrNull(2)?.toIntOrNull() ?: 0) / 255.0
                    val size = data?.getOrNull(3)?.toFloatOrNull() ?: 1.0f
                    val color = Color(r.toInt(),g.toInt(),b.toInt())
                    val data1 = ProxyParticle.DustData(color,size)
                    onlinePlayers().forEach {
                        particle.sendTo(it,location, Vector(offsetX,offsetY,offsetZ),count,speed,data1)
                    }
                }
                ProxyParticle.ITEM_CRACK -> {
                    val material: String = data?.getOrNull(0).toString()
                    val itemData: Int = data?.getOrNull(1)?.toIntOrNull() ?:0
                    val name: String = data?.getOrNull(2).toString()
                    val customModelData: Int = data?.getOrNull(3)?.toIntOrNull()?:0
                    val loreList = data?.filter { it.startsWith("lore:") }!!
                    val data1 = ProxyParticle.ItemData(material,itemData,name,loreList,customModelData)
                    onlinePlayers().forEach {
                        particle.sendTo(it,location,Vector(offsetX,offsetY,offsetZ),count,speed,data1)
                    }
                }
                ProxyParticle.BLOCK_CRACK, ProxyParticle.BLOCK_DUST, ProxyParticle.FALLING_DUST -> {
                    val material: String = data?.getOrNull(0).toString()
                    val block: Int = data?.getOrNull(1)?.toIntOrNull() ?:0
                    val blockData = ProxyParticle.BlockData(material,block)
                    onlinePlayers().forEach {
                        particle.sendTo(it,location,Vector(offsetX,offsetY,offsetZ),count,speed,blockData)
                    }
                }
                else -> {
                    onlinePlayers().forEach {
                        particle.sendTo(it,location,Vector(offsetX,offsetY,offsetZ),count,speed)
                    }                }
            }
        }
    }
}
