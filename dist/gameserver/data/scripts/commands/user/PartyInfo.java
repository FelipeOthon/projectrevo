package commands.user;

import l2s.gameserver.handler.IUserCommandHandler;
import l2s.gameserver.handler.UserCommandHandler;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class PartyInfo implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS;

	@Override
	public boolean useUserCommand(final int id, final Player activeChar)
	{
		if(id != PartyInfo.COMMAND_IDS[0])
			return false;
		final Party playerParty = activeChar.getParty();
		if(!activeChar.isInParty())
			return false;
		final Player partyLeader = playerParty.getPartyLeader();
		if(partyLeader == null)
			return false;
		final int memberCount = playerParty.getMemberCount();
		final int lootDistribution = playerParty.getLootDistribution();
		activeChar.sendPacket(new SystemMessage(1030));
		switch(lootDistribution)
		{
			case 0:
			{
				activeChar.sendPacket(new SystemMessage(1031));
				break;
			}
			case 3:
			{
				activeChar.sendPacket(new SystemMessage(1034));
				break;
			}
			case 4:
			{
				activeChar.sendPacket(new SystemMessage(1035));
				break;
			}
			case 1:
			{
				activeChar.sendPacket(new SystemMessage(1032));
				break;
			}
			case 2:
			{
				activeChar.sendPacket(new SystemMessage(1033));
				break;
			}
		}
		activeChar.sendPacket(new SystemMessage(1611).addString(partyLeader.getName()));
		activeChar.sendMessage(new CustomMessage("scripts.commands.user.PartyInfo.Members").addNumber(memberCount));
		activeChar.sendPacket(new SystemMessage(500));
		return true;
	}

	@Override
	public final int[] getUserCommandList()
	{
		return PartyInfo.COMMAND_IDS;
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
		COMMAND_IDS = new int[] { 81 };
	}
}
