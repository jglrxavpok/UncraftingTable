package org.jglrxavpok.mods.decraft.common.network.message;

import org.jglrxavpok.mods.decraft.common.config.ModConfiguration;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ConfigSyncMessage implements IMessage
{

	private int uncraftMethod = ModConfiguration.uncraftMethod;
	private int standardLevel = ModConfiguration.standardLevel;
	private int maxUsedLevel = ModConfiguration.maxUsedLevel;
	private String[] excludedItems = ModConfiguration.excludedItems;



	public ConfigSyncMessage()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		uncraftMethod = ByteBufUtils.readVarShort(buf);
		standardLevel = ByteBufUtils.readVarShort(buf);
		maxUsedLevel = ByteBufUtils.readVarShort(buf);
		excludedItems = ByteBufUtils.readUTF8String(buf).split("|");
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarShort(buf, uncraftMethod);
		ByteBufUtils.writeVarShort(buf, standardLevel);
		ByteBufUtils.writeVarShort(buf, maxUsedLevel);
		ByteBufUtils.writeUTF8String(buf, String.join("|", excludedItems));
	}


	public static final class MessageHandler implements IMessageHandler<ConfigSyncMessage, IMessage>
	{
		@Override
		public IMessage onMessage(final ConfigSyncMessage message, MessageContext ctx)
		{
			ModConfiguration.uncraftMethod = message.uncraftMethod;
			ModConfiguration.maxUsedLevel = message.maxUsedLevel;
			ModConfiguration.standardLevel = message.standardLevel;
			ModConfiguration.excludedItems = message.excludedItems;

			return null;
		}
	}

}
