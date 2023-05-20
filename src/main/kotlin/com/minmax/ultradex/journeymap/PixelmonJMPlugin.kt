package com.minmax.ultradex.journeymap

import com.minmax.ultradex.UltraDex
import journeymap.client.api.ClientPlugin
import journeymap.client.api.IClientAPI
import journeymap.client.api.IClientPlugin
import journeymap.client.api.display.MarkerOverlay
import journeymap.client.api.event.ClientEvent
import journeymap.client.api.event.ClientEvent.Type
import journeymap.client.api.model.MapImage
import journeymap.client.api.model.TextProperties
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.kotlinforforge.kotlin.enumSetOf

@ClientPlugin
class PixelmonJMPlugin : IClientPlugin {
    private lateinit var jmAPI: IClientAPI
    override fun initialize(jmAPI: IClientAPI) {
        this.jmAPI = jmAPI
        UltraDex.LOGGER.debug("JmPlugin init")
        this.jmAPI.subscribe(modId, enumSetOf(Type.MAPPING_STARTED))
    }

    override fun getModId(): String = UltraDex.MOD_ID

    override fun onEvent(clientEvent: ClientEvent) {
        when (clientEvent.type) {
            Type.MAPPING_STARTED -> {
                createMarker()
                UltraDex.LOGGER.debug("adding-biomes")
            }

            else -> {}
        }
    }

    fun createMarker() {
        val marker = ResourceLocation("ultradex:images/waystone.png")
        val icon = MapImage(marker, 32, 32)
            .setAnchorX(12.0)
            .setAnchorY(24.0)
            .setDisplayWidth(24.0)
            .setDisplayHeight(24.0)
            .setColor(0xffffff)

        val textProperties = TextProperties()
            .setOpacity(1.0f)

        val pos = BlockPos(100, 100, 100)
        val markerOverlay = MarkerOverlay(UltraDex.MOD_ID, "waystone_0", pos, icon)
        markerOverlay.setDimension(World.OVERWORLD)
            .setLabel("waystone_0")
            .setTextProperties(textProperties)

        try {
            jmAPI.show(markerOverlay)
        } catch (e: Exception) {
            UltraDex.LOGGER.error(e)
        }
    }
}