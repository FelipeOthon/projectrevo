package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.Config;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.database.mysql;
import l2s.gameserver.instancemanager.PlayerManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.utils.Log;

public class Account extends Functions implements ScriptFile
{
	public void Show()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_ACC_MOVE_ITEM < 1)
		{
			show(player.isLangRus() ? "Сервис отключен." : "Service is disabled.", player);
			return;
		}
		String append = player.isLangRus() ? "\u041f\u0435\u0440\u0435\u043d\u043e\u0441 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0435\u0439 \u043c\u0435\u0436\u0434\u0443 \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u0430\u043c\u0438:<br>" : "Transfer characters between accounts:<br>";
		append += player.isLangRus() ? "<font color=\"LEVEL\">\u0426\u0435\u043d\u0430: " + Config.SERVICES_ACC_MOVE_PRICE + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_ACC_MOVE_ITEM).getName() + "</font><br>" : "<font color=\"LEVEL\">Price: " + Config.SERVICES_ACC_MOVE_PRICE + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_ACC_MOVE_ITEM).getName() + "</font><br>";
		append += player.isLangRus() ? "<font color=\"ff0000\">\u0412\u043d\u0438\u043c\u0430\u0442\u0435\u043b\u044c\u043d\u043e \u0432\u0432\u043e\u0434\u0438\u0442\u0435 \u043b\u043e\u0433\u0438\u043d \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u0430, \u043d\u0430 \u043a\u043e\u0442\u043e\u0440\u044b\u0439 \u043f\u0435\u0440\u0435\u043d\u043e\u0441\u0438\u0442\u0435 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430.<br1>\u0410\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044f \u043d\u0435 \u0432\u043e\u0437\u0432\u0440\u0430\u0449\u0430\u0435\u0442 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0435\u0439.</font><br1>" : "<font color=\"ff0000\">Carefully enter the username to transfer, the administration does not return characters.</font><br1>";
		append += player.isLangRus() ? "<font color=\"ff33ff\">\u0412\u043d\u0438\u043c\u0430\u043d\u0438\u0435!!!</font> \u041f\u043e\u0441\u043b\u0435 \u0443\u0441\u043f\u0435\u0448\u043d\u043e\u0433\u043e \u043f\u0435\u0440\u0435\u043d\u043e\u0441\u0430 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 \u043d\u0430 \u0434\u0440\u0443\u0433\u043e\u0439 \u0430\u043a\u043a\u0430\u0443\u043d\u0442, \u043a\u043b\u0438\u0435\u043d\u0442 \u0437\u0430\u043a\u0440\u043e\u0435\u0442\u0441\u044f!!!<br1>" : "<font color=\"ff33ff\">Attention!!!</font> After a successful migration character to another customer's, account will be closed!!!<br1>";
		append += player.isLangRus() ? "\u0412\u044b \u043f\u0435\u0440\u0435\u043d\u043e\u0441\u0438\u0442\u0435 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 <font color=\"33ff00\">" + player.getName() + "</font><br1>\u043d\u0430 \u043a\u0430\u043a\u043e\u0439 \u0430\u043a\u043a\u0430\u0443\u043d\u0442 \u0435\u0433\u043e \u043f\u0435\u0440\u0435\u043d\u0435\u0441\u0442\u0438?" : "You transfer your character <font color=\"33ff00\">" + player.getName() + "</font><br1>on which account to transfer it?";
		append += "<edit var=\"new_acc\" width=180>";
		append += player.isLangRus() ? "<button value=\"\u041f\u0435\u0440\u0435\u043d\u0435\u0441\u0442\u0438\" action=\"bypass -h scripts_services.Account:NewAccount $new_acc\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br>" : "<button value=\"Transfer\" action=\"bypass -h scripts_services.Account:NewAccount $new_acc\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br>";
		show(append, player, (NpcInstance) null);
	}

	public void NewAccount(final String[] name)
	{
		if(name.length < 1)
			return;
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_ACC_MOVE_ITEM < 1)
		{
			show(player.isLangRus() ? "Сервис отключен." : "Service is disabled.", player);
			return;
		}
		if(getItemCount(player, Config.SERVICES_ACC_MOVE_ITEM) < Config.SERVICES_ACC_MOVE_PRICE)
		{
			player.sendMessage(player.isLangRus() ? "\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442\u0443 " + Config.SERVICES_ACC_MOVE_PRICE + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_ACC_MOVE_ITEM) : "You have no " + Config.SERVICES_ACC_MOVE_PRICE + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_ACC_MOVE_ITEM));
			Show();
			return;
		}
		String _name = name[0];
		try
		{
			_name = name[0].trim();
		}
		catch(Exception e2)
		{
			return;
		}
		if(_name.equals(""))
			return;
		if(_name.equalsIgnoreCase(player.getAccountName()))
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0435\u043b\u044c\u0437\u044f \u043f\u0435\u0440\u0435\u043d\u0435\u0441\u0442\u0438 \u043d\u0430 \u044d\u0442\u043e\u0442 \u0436\u0435 \u0430\u043a\u043a\u0430\u0443\u043d\u0442." : "Need enter another account.");
			return;
		}
		if(PlayerManager.accountCharNumber(_name) >= 7)
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0430 \u044d\u0442\u043e\u043c \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u0435 \u043c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u044c\u043d\u043e\u0435 \u043a\u043e\u043b-\u0432\u043e \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0435\u0439." : "Characters count on this account is max.");
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT `obj_Id` FROM `characters` WHERE `account_name` = ? LIMIT 1");
			statement.setString(1, _name);
			rs = statement.executeQuery();
			if(rs.next())
			{
				DbUtils.closeQuietly(statement);
				removeItem(player, Config.SERVICES_ACC_MOVE_ITEM, Config.SERVICES_ACC_MOVE_PRICE);
				statement = con.prepareStatement("UPDATE `characters` SET `account_name` = '" + _name + "' WHERE `obj_Id` = " + player.getObjectId() + " LIMIT 1");
				statement.executeUpdate();
				mysql.set("DELETE FROM `hwid_locks` WHERE `obj_Id`=" + player.getObjectId() + " LIMIT 1");
				Log.addLog("Player " + player.getName() + " [" + player.getObjectId() + "] transferred to account " + _name, "AccMove");
				player.kick(false);
			}
			else
			{
				player.sendMessage(player.isLangRus() ? "\u0412\u0432\u0435\u0434\u0435\u043d\u043d\u044b\u0439 \u0430\u043a\u043a\u0430\u0443\u043d\u0442 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d." : "The entered account is not found.");
				Show();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
