package io.github.yuazer.zaxparticle

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info

object ZaxParticle : Plugin() {

    override fun onEnable() {
        info("Successfully running ExamplePlugin!")
    }
}