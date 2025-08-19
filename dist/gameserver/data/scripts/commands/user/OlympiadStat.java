package commands.user;

import l2s.gameserver.handler.IUserCommandHandler;
import l2s.gameserver.handler.UserCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class OlympiadStat implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS;

	@Override
	public boolean useUserCommand(final int id, final Player activeChar)
	{
		if(id != OlympiadStat.COMMAND_IDS[0])
			return false;
		SystemMessage sm;
		if(!activeChar.isNoble())
			sm = new SystemMessage(1674);
		else
		{
			sm = new SystemMessage(1673);
			sm.addNumber(Olympiad.getCompetitionDone(activeChar.getObjectId()));
			sm.addNumber(Olympiad.getCompetitionWin(activeChar.getObjectId()));
			sm.addNumber(Olympiad.getCompetitionLoose(activeChar.getObjectId()));
			sm.addNumber(Olympiad.getNoblePoints(activeChar.getObjectId()));
		}
		activeChar.sendPacket(sm);
		return true;
	}

	@Override
	public int[] getUserCommandList()
	{
		return OlympiadStat.COMMAND_IDS;
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
		COMMAND_IDS = new int[] { 109 };
	}
}
