package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ScheduledFuture;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.database.mysql;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.utils.Util;

public class DonateParse extends Functions implements ScriptFile
{
	public static boolean started;
	private static ScheduledFuture<?> startTask;

	public static void start()
	{
		if(DonateParse.startTask == null)
			DonateParse.startTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Parse(), Config.DONATE_PARSE_DELAY, Config.DONATE_PARSE_DELAY);
		DonateParse.started = true;
	}

	public static void stop()
	{
		if(DonateParse.startTask != null)
		{
			DonateParse.startTask.cancel(false);
			DonateParse.startTask = null;
		}
		DonateParse.started = false;
	}

	@Override
	public void onLoad()
	{
		if(!Config.ALLOW_DONATE_PARSE)
			return;
		start();
		ScriptFile._log.info("Loaded Service: DonateParse");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	private static class Parse implements Runnable
	{
		@Override
		public void run()
		{
			Connection con = null;
			PreparedStatement statement = null;
			ResultSet rset = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("SELECT * FROM `character_donate` WHERE `given`=0");
				rset = statement.executeQuery();
				while(rset.next())
				{
					final int id = rset.getInt("id");
					final int objId = rset.getInt("obj_Id");
					final int itemId = rset.getInt("item_id");
					final int count = rset.getInt("count");
					final int enchant = rset.getInt("enchant");
					final Player player = GameObjectsStorage.getPlayer(objId);
					if(player != null)
					{
						if(enchant > 0)
						{
							final ItemInstance item = ItemTable.getInstance().createItem(itemId);
							if(!item.canBeEnchanted())
							{
								ScriptFile._log.warn("ItemId " + itemId + " in character_donate with id " + id + " can't be enchanted!");
								continue;
							}
							item.setEnchantLevel(enchant);
							player.getInventory().addItem(item);
							player.sendPacket(SystemMessage.obtainItems(itemId, count, enchant));
						}
						else
							Functions.addItem(player, itemId, count);
					}
					else
						Util.giveItem(objId, itemId, count);
					mysql.set("UPDATE `character_donate` SET `given`=1 WHERE `id`=" + id + " LIMIT 1");
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			finally
			{
				DbUtils.closeQuietly(con, statement, rset);
			}
		}
	}
}
