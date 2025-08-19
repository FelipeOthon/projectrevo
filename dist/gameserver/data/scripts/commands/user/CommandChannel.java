package commands.user;

import l2s.gameserver.handler.IUserCommandHandler;
import l2s.gameserver.handler.UserCommandHandler;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.ExMultiPartyCommandChannelInfo;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class CommandChannel implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS;

	@Override
	public boolean useUserCommand(final int id, final Player activeChar)
	{
		if(id != CommandChannel.COMMAND_IDS[0] && id != CommandChannel.COMMAND_IDS[1] && id != CommandChannel.COMMAND_IDS[2] && id != CommandChannel.COMMAND_IDS[3])
			return false;
		switch(id)
		{
			case 92:
			{
				activeChar.sendMessage(new CustomMessage("scripts.commands.user.CommandChannel"));
				break;
			}
			case 93:
			{
				if(!activeChar.isInParty() || !activeChar.getParty().isInCommandChannel())
					return true;
				if(activeChar.getParty().getCommandChannel().getChannelLeader() == activeChar)
				{
					final l2s.gameserver.model.CommandChannel channel = activeChar.getParty().getCommandChannel();
					channel.disbandChannel();
					break;
				}
				activeChar.sendPacket(new SystemMessage(1682));
				break;
			}
			case 96:
			{
				if(!activeChar.isInParty() || !activeChar.getParty().isInCommandChannel())
					return true;
				if(!activeChar.getParty().isLeader(activeChar))
				{
					activeChar.sendPacket(new SystemMessage(1683));
					return true;
				}
				final l2s.gameserver.model.CommandChannel channel = activeChar.getParty().getCommandChannel();
				if(channel.getChannelLeader() != activeChar)
				{
					final Party party = activeChar.getParty();
					channel.removeParty(party);
					party.broadCast(new L2GameServerPacket[] { new SystemMessage(1586) });
					channel.broadcastToChannelMembers(new SystemMessage(1587).addString(activeChar.getName()));
					break;
				}
				if(channel.getParties().size() > 1)
					return false;
				channel.disbandChannel();
				return true;
			}
			case 97:
			{
				if(!activeChar.isInParty() || !activeChar.getParty().isInCommandChannel())
					return false;
				activeChar.sendPacket(new ExMultiPartyCommandChannelInfo(activeChar.getParty().getCommandChannel()));
				break;
			}
		}
		return true;
	}

	@Override
	public final int[] getUserCommandList()
	{
		return CommandChannel.COMMAND_IDS;
	}

	@Override
	public void onLoad()
	{
		UserCommandHandler.getInstance().registerUserCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		COMMAND_IDS = new int[] { 92, 93, 96, 97 };
	}
}
