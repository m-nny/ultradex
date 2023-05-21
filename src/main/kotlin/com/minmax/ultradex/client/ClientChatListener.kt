package com.minmax.ultradex.client

import com.minmax.ultradex.UltraDex
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandSource
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object ClientChatListener {
    @SubscribeEvent
    fun onPlayerChat(event: ClientChatEvent) {
        if (!event.message.startsWith("!")) {
            return
        }
        try {
            UltraDex.LOGGER.debug("onPlayerChat(${event.message})")
            val source = getCommandSource() ?: return
            val parse = UltraDex.COMMANDS_DISPATCHER.parse(event.message.substring(1), source)
            if (parse.context.nodes.size > 0) {
                event.isCanceled = true
                UltraDex.COMMANDS_DISPATCHER.execute(parse)
            }
        } catch (_: CommandSyntaxException) {
        }
    }

    private fun getCommandSource(): CommandSource? = Minecraft.getInstance().player?.createCommandSourceStack()
}