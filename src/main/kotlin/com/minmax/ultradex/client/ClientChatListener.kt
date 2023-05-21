package com.minmax.ultradex.client

import com.minmax.ultradex.UltraDex
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandSource
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object ClientChatListener {
    @SubscribeEvent
    fun onPlayerChat(event: ClientChatEvent) {
        if (!event.message.startsWith("!")) {
            return
        }
        UltraDex.LOGGER.debug("onPlayerChat(${event.message})")
        val source = getCommandSource() ?: return
        try {
            val parse = UltraDex.COMMANDS_DISPATCHER.parse(event.message.substring(1), source)
            if (parse.context.nodes.size > 0) {
                event.isCanceled = true
                Minecraft.getInstance().gui.chat.addRecentChat(event.originalMessage)
                UltraDex.COMMANDS_DISPATCHER.execute(parse)
            }
        } catch (e: CommandSyntaxException) {
            UltraDex.LOGGER.error("error running command ${event.originalMessage}: $e")
            source.sendFailure(TranslationTextComponent(e.message ?: "error running command"))
        }
    }

    private fun getCommandSource(): CommandSource? = Minecraft.getInstance().player?.createCommandSourceStack()
}