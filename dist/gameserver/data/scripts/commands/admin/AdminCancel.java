package commands.admin;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.scripts.ScriptFile;

public class AdminCancel implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanEditChar)
			return false;
		if(command.equals("admin_cancel"))
			handleCancel(activeChar, null, true);
		else if(command.startsWith("admin_cancel "))
			handleCancel(activeChar, command.split(" ")[1], true);
		else if(command.equals("admin_cleanse"))
			handleCancel(activeChar, null, false);
		else if(command.startsWith("admin_cleanse "))
			handleCancel(activeChar, command.split(" ")[1], false);
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminCancel._adminCommands;
	}

	private void handleCancel(final Player activeChar, final String player, final boolean canc)
	{
		GameObject obj = activeChar.getTarget();
		if(player != null)
		{
			final Player plyr = World.getPlayer(player);
			if(plyr != null)
				obj = plyr;
			else
				try
				{
					final int radius = Math.max(Integer.parseInt(player), 100);
					for(final Creature character : activeChar.getAroundCharacters(radius, 200))
						if(canc)
							character.getAbnormalList().stopAll();
						else
							for(final Abnormal e : character.getAbnormalList().values())
								if(e != null && e.isOffensive())
									e.exit();
					activeChar.sendMessage("Apply " + (canc ? "Cancel" : "Cleanse") + " within " + radius + " unit radius.");
					return;
				}
				catch(NumberFormatException e3)
				{
					activeChar.sendMessage("Enter valid player name or radius!");
					return;
				}
		}
		if(obj == null)
			obj = activeChar;
		if(obj.isCreature())
		{
			final Creature target = (Creature) obj;
			if(canc)
				target.getAbnormalList().stopAll();
			else
				for(final Abnormal e2 : target.getAbnormalList().values())
					if(e2 != null && e2.isOffensive())
						e2.exit();
			activeChar.sendMessage((canc ? "Cancel" : "Cleanse") + " applied for: " + target.getName());
		}
		else
			activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	@Override
	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		AdminCancel._adminCommands = new String[] { "admin_cancel", "admin_cleanse" };
	}
}
