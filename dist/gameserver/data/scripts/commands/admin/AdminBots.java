package commands.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.PlayerManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.ScriptFile;

public class AdminBots implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().BotsControl)
			return false;
		final String args = null;
		if(command.equals("admin_load_bots"))
		{
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					Player.loadBots();
				}
			}, 0L);
			if(Config.ENABLE_BOTS)
				activeChar.sendMessage("Bots loaded!");
			else
				activeChar.sendMessage("Bots not enabled!");
		}
		else if(command.equals("admin_clear_bots_ai"))
		{
			Player.clearBotsAi();
			activeChar.sendMessage("All ai removed from cache!");
		}
		else if(command.equals("admin_pars_bots_ai"))
		{
			Player.parseBotsAi();
			activeChar.sendMessage("Pars ai done!");
		}
		else if(command.equals("admin_spawn_bots"))
		{
			Player.startSpawnBots(0L);
			activeChar.sendMessage("Bots spawn started!");
		}
		else if(command.equals("admin_stop_spawn_bots"))
		{
			Player.stopSpawnBots();
			activeChar.sendMessage("Bots spawn task stopped!");
		}
		else if(command.startsWith("admin_pars_bots_file"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				final int id = Integer.parseInt(st.nextToken());
				Player.parseBotsFile(id);
				activeChar.sendMessage("Pars file done!");
			}
			else
				activeChar.sendMessage("Usage: //pars_bots_file <id>");
		}
		else if(command.equals("admin_del_bots"))
		{
			for(final Player em : GameObjectsStorage.getPlayers())
				if(em != null && em.isFashion)
				{
					if(em.stopBot != null)
						em.stopBot.cancel(false);
					em.stopBot = ThreadPoolManager.getInstance().schedule(new Player.DeleteBotTask(em.getObjectId()), 10L);
				}
			activeChar.sendMessage("Bots deleted!");
		}
		else if(command.equals("admin_del_db_bots"))
		{
			Connection con = null;
			PreparedStatement statement = null;
			ResultSet rset = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("SELECT `obj_Id` FROM `characters` WHERE `account_name`='" + Config.BOTS_ACC + "' AND `online`=0");
				rset = statement.executeQuery();
				while(rset.next())
					PlayerManager.deleteCharByObjId(rset.getInt("obj_Id"));
			}
			catch(Exception e)
			{
				ScriptFile._log.error("Deleting bots from db: ", e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement, rset);
			}
			activeChar.sendMessage("All offline bots deleted from DB!");
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminBots._adminCommands;
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
		AdminBots._adminCommands = new String[] {
				"admin_load_bots",
				"admin_clear_bots_ai",
				"admin_pars_bots_ai",
				"admin_spawn_bots",
				"admin_stop_spawn_bots",
				"admin_pars_bots_file",
				"admin_del_bots",
				"admin_del_db_bots" };
	}
}
