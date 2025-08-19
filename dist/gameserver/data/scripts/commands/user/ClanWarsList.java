package commands.user;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.handler.IUserCommandHandler;
import l2s.gameserver.handler.UserCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class ClanWarsList implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS;

	@Override
	public boolean useUserCommand(final int id, final Player activeChar)
	{
		if(id != ClanWarsList.COMMAND_IDS[0] && id != ClanWarsList.COMMAND_IDS[1] && id != ClanWarsList.COMMAND_IDS[2])
			return false;
		final Clan clan = activeChar.getClan();
		if(clan == null)
		{
			activeChar.sendPacket(new SystemMessage(238));
			return false;
		}
		List<Clan> data = new ArrayList<Clan>();
		if(id == 88)
		{
			activeChar.sendPacket(new SystemMessage(1571));
			data = clan.getEnemyClans();
		}
		else if(id == 89)
		{
			activeChar.sendPacket(new SystemMessage(1572));
			data = clan.getAttackerClans();
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(1612));
			for(final Clan c : clan.getEnemyClans())
				if(clan.getAttackerClans().contains(c))
					data.add(c);
		}
		for(final Clan c : data)
		{
			final String clanName = c.getName();
			final int ally_id = c.getAllyId();
			SystemMessage sm;
			if(ally_id > 0)
			{
				sm = new SystemMessage(1200);
				sm.addString(clanName);
				sm.addString(c.getAlliance().getAllyName());
			}
			else
			{
				sm = new SystemMessage(1202);
				sm.addString(clanName);
			}
			activeChar.sendPacket(sm);
		}
		SystemMessage sm = new SystemMessage(490);
		activeChar.sendPacket(sm);
		return true;
	}

	@Override
	public int[] getUserCommandList()
	{
		return ClanWarsList.COMMAND_IDS;
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
		COMMAND_IDS = new int[] { 88, 89, 90 };
	}
}
