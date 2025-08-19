package commands.voiced;

import l2s.gameserver.Config;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.instancemanager.DimensionalRiftManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.skillclasses.Call;
import l2s.gameserver.utils.Location;

public class Relocate implements IVoicedCommandHandler, ScriptFile
{
	private final String[] _commandList;

	public Relocate()
	{
		_commandList = new String[] { "rcm" };
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String target)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		if(!command.equalsIgnoreCase("rcm"))
			return false;
		if(!Config.ALLOW_RCM)
		{
			activeChar.sendMessage(activeChar.isLangRus() ? "\u041e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u043e." : "Disabled.");
			return false;
		}
		if(!activeChar.isClanLeader())
		{
			activeChar.sendPacket(new SystemMessage(236));
			return false;
		}
		final Clan clan = activeChar.getClan();
		if(clan.NEXT_RCM > System.currentTimeMillis())
		{
			final int sec = (int) ((clan.NEXT_RCM - System.currentTimeMillis()) / 1000L);
			if(activeChar.isLangRus())
				activeChar.sendMessage("\u0414\u043e \u043f\u043e\u0432\u0442\u043e\u0440\u043d\u043e\u0433\u043e \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u044f " + sec + " \u0441\u0435\u043a.");
			else
				activeChar.sendMessage("Usage time remaining " + sec + " sec.");
			return false;
		}
		if(activeChar.isAlikeDead())
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Relocate.Dead"));
			return false;
		}
		if(DimensionalRiftManager.getInstance().checkIfInRiftZone(activeChar.getLoc(), false))
		{
			activeChar.sendPacket(new SystemMessage(650));
			return false;
		}
		final SystemMessage msg = Call.canSummonHere(activeChar);
		if(msg != null)
		{
			activeChar.sendPacket(msg);
			return false;
		}
		clan.NEXT_RCM = Config.DELAY_RCM * 1000L + System.currentTimeMillis();
		final boolean ss = activeChar.isIn7sDungeon();
		final Player[] onlineMembers;
		final Player[] ms = onlineMembers = clan.getOnlineMembers(activeChar.getObjectId());
		for(final Player pl : onlineMembers)
			if(pl != null)
				if(Call.canBeSummoned(pl, ss) == null)
					pl.summonCharacterRequest(activeChar, Location.findAroundPosition(activeChar.getLoc(), 100, 150, pl.getGeoIndex()), 5);
		return true;
	}

	@Override
	public void onLoad()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
