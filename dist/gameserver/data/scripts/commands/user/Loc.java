package commands.user;

import l2s.gameserver.handler.IUserCommandHandler;
import l2s.gameserver.handler.UserCommandHandler;
import l2s.gameserver.instancemanager.TownManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class Loc implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS;

	@Override
	public boolean useUserCommand(final int id, final Player activeChar)
	{
		if(Loc.COMMAND_IDS[0] != id)
			return false;
		final int nearestTown = TownManager.getInstance().getClosestTownNumber(activeChar);
		int msg = 0;
		switch(nearestTown)
		{
			case 1:
			{
				msg = 910;
				break;
			}
			case 2:
			{
				msg = 914;
				break;
			}
			case 3:
			{
				msg = 915;
				break;
			}
			case 4:
			{
				msg = 920;
				break;
			}
			case 5:
			{
				msg = 921;
				break;
			}
			case 6:
			{
				msg = 912;
				break;
			}
			case 7:
			{
				msg = 911;
				break;
			}
			case 8:
			{
				msg = 916;
				break;
			}
			case 9:
			{
				msg = 918;
				break;
			}
			case 10:
			{
				msg = 922;
				break;
			}
			case 11:
			{
				msg = 924;
				break;
			}
			case 12:
			{
				msg = 923;
				break;
			}
			case 13:
			{
				msg = 926;
				break;
			}
			case 14:
			{
				msg = 1537;
				break;
			}
			case 15:
			{
				msg = 1538;
				break;
			}
			case 16:
			{
				msg = 1714;
				break;
			}
			case 18:
			{
				msg = 1924;
				break;
			}
			default:
			{
				msg = 924;
				break;
			}
		}
		activeChar.sendPacket(new SystemMessage(msg).addNumber(activeChar.getX()).addNumber(activeChar.getY()).addNumber(activeChar.getZ()));
		return true;
	}

	@Override
	public final int[] getUserCommandList()
	{
		return Loc.COMMAND_IDS;
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
		COMMAND_IDS = new int[] { 0 };
	}
}
