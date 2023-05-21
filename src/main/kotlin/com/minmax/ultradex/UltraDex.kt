package com.minmax.ultradex

import com.minmax.ultradex.client.ClientChatListener
import com.minmax.ultradex.journeymap.LocateBiomeCommand
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

@Mod(UltraDex.MOD_ID)
object UltraDex {
    const val MOD_ID = "ultradex"

    val LOGGER: Logger = LogManager.getLogger()

    val COMMANDS_DISPATCHER = CommandDispatcher<CommandSource>()


    init {
        LOGGER.debug("UltraDex init")
        val cmd = COMMANDS_DISPATCHER.register(
            Commands.literal(MOD_ID)
                .then(LocateBiomeCommand.CMD)
        )
        COMMANDS_DISPATCHER.register(Commands.literal("ud").redirect(cmd))
        FORGE_BUS.register(ClientChatListener)
    }
}