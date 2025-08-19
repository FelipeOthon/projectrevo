package commands.admin;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.Revive;
import l2s.gameserver.network.l2.s2c.SocialAction;
import l2s.gameserver.scripts.ScriptFile;

public class AdminRes implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Res)
			return false;
		if(command.startsWith("admin_res "))
			this.handleRes(activeChar, command.split(" ")[1]);
		if(command.equals("admin_res"))
			this.handleRes(activeChar);
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminRes._adminCommands;
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
			if(plyr != null)
				obj = plyr;
			else
				try
				{
					final int radius = Math.max(Integer.parseInt(player), 100);
					for(final Creature character : activeChar.getAroundCharacters(radius, radius))
						this.handleRes(character);
					activeChar.sendMessage("Resurrected within " + radius + " unit radius.");
					return;
				}
				catch(NumberFormatException e)
				{
					activeChar.sendMessage("Enter valid player name or radius.");
					return;
				}
		}
		if(obj == null)
			obj = activeChar;
		if(obj.isCreature())
			this.handleRes((Creature) obj);
		else
			activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	private void handleRes(final Creature target)
	{
		if(!target.isDead())
			return;
		if(target.isPlayable())
		{
			if(target.isPlayer())
				((Player) target).doRevive(100.0);
			else
				((Playable) target).doRevive(true);
			target.setCurrentHpMp(target.getMaxHp(), target.getMaxMp(), true);
			target.setCurrentCp(target.getMaxCp());
		}
		else if(target.isNpc())
		{
			((NpcInstance) target).onRes();
			target.broadcastPacket(new L2GameServerPacket[] { new SocialAction(target.getObjectId(), 15) });
			target.broadcastPacket(new L2GameServerPacket[] { new Revive(target) });
		}
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
		AdminRes._adminCommands = new String[] { "admin_res" };
	}
}
