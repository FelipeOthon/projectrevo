package commands.user;

import l2s.gameserver.handler.IUserCommandHandler;
import l2s.gameserver.handler.UserCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.RequestActionUse;
import l2s.gameserver.scripts.ScriptFile;

public class MountDismount implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS;

	@Override
	public boolean useUserCommand(final int id, final Player activeChar)
	{
		if(id != MountDismount.COMMAND_IDS[0] && id != MountDismount.COMMAND_IDS[1])
			return false;
		RequestActionUse.mount(activeChar, activeChar.getServitor());
		return true;
	}

	@Override
	public int[] getUserCommandList()
	{
		return MountDismount.COMMAND_IDS;
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
		COMMAND_IDS = new int[] { 61, 62 };
	}
}
