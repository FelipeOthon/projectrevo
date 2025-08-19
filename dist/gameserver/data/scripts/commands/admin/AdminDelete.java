package commands.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Log;

public class AdminDelete implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanEditNPC)
			return false;
		if(command.equals("admin_delete"))
			handleDelete(activeChar);
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminDelete._adminCommands;
	}

	private void handleDelete(final Player activeChar)
	{
		final GameObject obj = activeChar.getTarget();
		if(obj != null && obj.isNpc())
		{
			final NpcInstance target = (NpcInstance) obj;
			target.decayMe();
			final Spawn spawn = target.getSpawn();
			if(spawn != null)
			{
				spawn.stopRespawn();
				deleteSpawn(spawn);
			}
			Log.addLog(activeChar.toString() + " deleted NPC" + target.toString(), "gm_actions");
		}
		else
			activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	public void deleteSpawn(final Spawn spawn)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM `spawnlist` WHERE `npc_templateid`=? AND `locx`=? AND `locy`=? AND `locz`=? AND `loc_id`=?");
			statement.setInt(1, spawn.getNpcId());
			statement.setInt(2, spawn.getLocx());
			statement.setInt(3, spawn.getLocy());
			statement.setInt(4, spawn.getLocz());
			statement.setInt(5, spawn.getLocation());
			statement.execute();
		}
		catch(Exception e)
		{
			ScriptFile._log.error("spawn couldnt be deleted in db:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
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
		AdminDelete._adminCommands = new String[] { "admin_delete" };
	}
}
