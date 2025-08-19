package commands.admin;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.scripts.ScriptFile;

public class AdminHeal implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Heal)
			return false;
		if(command.equals("admin_heal"))
			this.handleRes(activeChar);
		else if(command.startsWith("admin_heal"))
			try
			{
				final String healTarget = command.substring(11);
				this.handleRes(activeChar, healTarget);
			}
			catch(StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Incorrect target/radius specified.");
			}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminHeal._adminCommands;
	}

	private void handleRes(final Player activeChar)
	{
		this.handleRes(activeChar, null);
	}

	private void handleRes(final Player activeChar, final String player)
	{
		GameObject obj = activeChar.getTarget();
		if(player != null)
		{
			final Player plyr = World.getPlayer(player);
			if(plyr == null)
			{
				int radius = 0;
				try
				{
					radius = Math.max(Integer.parseInt(player), 100);
				}
				catch(Exception e)
				{
					return;
				}
				for(final Creature character : activeChar.getAroundCharacters(radius, 200))
				{
					character.setCurrentHpMp(character.getMaxHp(), character.getMaxMp(), false);
					if(character.isPlayer())
						character.setCurrentCp(character.getMaxCp());
				}
				activeChar.sendMessage("Healed within " + radius + " unit radius.");
				return;
			}
			obj = plyr;
		}
		if(obj == null)
			obj = activeChar;
		if(obj.isCreature())
		{
			final Creature target = (Creature) obj;
			target.setCurrentHpMp(target.getMaxHp(), target.getMaxMp(), false);
			if(target.isPlayer())
				target.setCurrentCp(target.getMaxCp());
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
		AdminHeal._adminCommands = new String[] { "admin_heal" };
	}
}
