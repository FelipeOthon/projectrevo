package commands.voiced;

import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.scripts.ScriptFile;

public class PlayerCastleDoors implements IVoicedCommandHandler, ScriptFile
{
	private static String[] _voicedCommands;

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String target)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		if(command.startsWith("open") && target.equals("doors") && activeChar.isClanLeader())
		{
			if(!(activeChar.getTarget() instanceof DoorInstance))
				return false;
			final DoorInstance door = (DoorInstance) activeChar.getTarget();
			final Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, activeChar.getClan().getHasCastle());
			if(door == null || castle == null)
				return false;
			if(castle.checkIfInZone(door.getX(), door.getY(), door.getZ()))
				door.openMe();
		}
		else if(command.startsWith("close") && target.equals("doors") && activeChar.isClanLeader())
		{
			if(!(activeChar.getTarget() instanceof DoorInstance))
				return false;
			final DoorInstance door = (DoorInstance) activeChar.getTarget();
			final Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, activeChar.getClan().getHasCastle());
			if(door == null || castle == null)
				return false;
			if(castle.checkIfInZone(door.getX(), door.getY(), door.getZ()))
				door.closeMe();
		}
		return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return PlayerCastleDoors._voicedCommands;
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

	static
	{
		PlayerCastleDoors._voicedCommands = new String[] { "open", "close" };
	}
}
