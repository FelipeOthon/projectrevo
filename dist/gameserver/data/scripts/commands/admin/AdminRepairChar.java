package commands.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.ScriptFile;

public class AdminRepairChar implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(activeChar.getPlayerAccess() == null || !activeChar.getPlayerAccess().CanEditChar)
			return false;
		handleRepair(command);
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminRepairChar._adminCommands;
	}

	private void handleRepair(final String command)
	{
		final String[] parts = command.split(" ");
		if(parts.length != 2)
			return;
		final String cmd = "UPDATE characters SET x=-84318, y=244579, z=-3730 WHERE char_name=?";
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(cmd);
			statement.setString(1, parts[1]);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("SELECT obj_id FROM characters where char_name=?");
			statement.setString(1, parts[1]);
			rset = statement.executeQuery();
			int objId = 0;
			if(rset.next())
				objId = rset.getInt(1);
			DbUtils.closeQuietly(statement, rset);
			if(objId == 0)
				return;
			statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE char_obj_id=?");
			statement.setInt(1, objId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE items SET loc='INVENTORY' WHERE owner_id=? AND loc!='WAREHOUSE'");
			statement.setInt(1, objId);
			statement.execute();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
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
		AdminRepairChar._adminCommands = new String[] { "admin_restore", "admin_repair" };
	}
}
