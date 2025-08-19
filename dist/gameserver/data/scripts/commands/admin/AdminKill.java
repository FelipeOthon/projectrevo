package commands.admin;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Log;

public class AdminKill implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanEditNPC)
			return false;
		if(command.startsWith("admin_kill "))
			this.handleKill(activeChar, command.split(" ")[1]);
		if(command.equals("admin_kill"))
			this.handleKill(activeChar);
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminKill._adminCommands;
	}

	private void handleKill(final Player activeChar)
	{
		this.handleKill(activeChar, null);
	}

	private void handleKill(final Player activeChar, final String player)
	{
		GameObject obj = activeChar.getTarget();
		if(player != null)
		{
			final Player plyr = World.getPlayer(player);
			if(plyr == null)
			{
				final int radius = Math.max(Integer.parseInt(player), 100);
				for(final Creature character : activeChar.getAroundCharacters(radius, 200))
					character.reduceCurrentHp(character.getMaxHp() + character.getMaxCp() + 1, character, (Skill) null, 0, false, true, true, false, false, false, false, false);
				activeChar.sendMessage("Killed within " + radius + " unit radius.");
				return;
			}
			obj = plyr;
		}
		if(obj != null && obj.isCreature())
		{
			final Creature target = (Creature) obj;
			if(target.isInvul())
				target.doDie(activeChar);
			else
				target.reduceCurrentHp(target.getMaxHp() + target.getMaxCp() + 1, activeChar, (Skill) null, 0, false, true, true, false, false, false, false, false);
			Log.addLog(activeChar.toString() + " kill character " + target.toString(), "gm_actions");
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
		AdminKill._adminCommands = new String[] { "admin_kill" };
	}
}
