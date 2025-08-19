package commands.user;

import l2s.gameserver.GameTimeController;
import l2s.gameserver.handler.IUserCommandHandler;
import l2s.gameserver.handler.UserCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class Time implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS;

	@Override
	public boolean useUserCommand(final int id, final Player activeChar)
	{
		if(Time.COMMAND_IDS[0] != id)
			return false;
		final int t = GameTimeController.getInstance().getGameTime();
		final int h = t / 60 % 24;
		final String m = (t % 60 < 10 ? "0" : "") + t % 60;
		SystemMessage sm;
		if(h >= 0 && h < 6)
			sm = new SystemMessage(928);
		else
			sm = new SystemMessage(927);
		sm.addNumber(h).addString(m);
		activeChar.sendPacket(sm);
		sm = null;
		return true;
	}

	@Override
	public final int[] getUserCommandList()
	{
		return Time.COMMAND_IDS;
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
		COMMAND_IDS = new int[] { 77 };
	}
}
