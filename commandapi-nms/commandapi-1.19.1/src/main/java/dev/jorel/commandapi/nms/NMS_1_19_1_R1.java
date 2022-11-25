/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.ExceptionHandlingArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Either;

import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.preprocessor.NMSMeta;
import dev.jorel.commandapi.preprocessor.RequireField;
import io.netty.channel.Channel;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.ServerFunctionLibrary;
import net.minecraft.world.level.gameevent.EntityPositionSource;

import java.lang.reflect.Field;
import java.util.Map;

// Mojang-Mapped reflection
/**
 * NMS implementation for Minecraft 1.19.1
 */
@NMSMeta(compatibleWith = { "1.19.1", "1.19.2" })
@RequireField(in = ServerFunctionLibrary.class, name = "dispatcher", ofType = CommandDispatcher.class)
@RequireField(in = EntitySelector.class, name = "usesSelector", ofType = boolean.class)
@RequireField(in = EntityPositionSource.class, name = "entityOrUuidOrId", ofType = Either.class)
public class NMS_1_19_1_R1 extends NMS_1_19_Common {

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.19.1", "1.19.2" };
	}

	@Differs(from = "1.19", by = "Use of 1.19.1 chat preview handler")
	@Override
	public void hookChatPreview(Plugin plugin, Player player) {
		final Channel playerChannel = ((CraftPlayer) player).getHandle().connection.connection.channel;
		if (playerChannel.pipeline().get("CommandAPI_" + player.getName()) == null) {
			playerChannel.pipeline().addBefore("packet_handler", "CommandAPI_" + player.getName(), new NMS_1_19_1_R1_ChatPreviewHandler(this, plugin, player));
		}
	}

	@Override
	public void registerCustomArgumentType() {
		try {
			System.out.println("Registering custom ArgumentType");
			Field mapField = CommandAPIHandler.getInstance().getField(ArgumentTypeInfos.class, "a");
			Map infoMap = (Map) mapField.get(null);

			CustomArgumentInfo info = new CustomArgumentInfo();
			infoMap.put(ExceptionHandlingArgumentType.class, info);

			Field isFrozen = CommandAPIHandler.getInstance().getField(MappedRegistry.class, "ca");
			isFrozen.set(Registry.COMMAND_ARGUMENT_TYPE, false);

			Registry.<ArgumentTypeInfo<?, ?>>register(Registry.COMMAND_ARGUMENT_TYPE, "commandapi:argument", info);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
}
