package services;

import l2s.gameserver.Config;
import l2s.gameserver.listener.actor.player.OnAnswerListener;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.network.l2.s2c.ConfirmDlg;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.utils.Log;

public class NoblessSell extends Functions implements ScriptFile
{
	public void get()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.SERVICES_NOBLESS_SELL_ENABLED)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		if(player.isNoble())
		{
			player.sendMessage(player.isLangRus() ? "\u0412\u044b \u0443\u0436\u0435 \u0414\u0432\u043e\u0440\u044f\u043d\u0438\u043d." : "You are already Noblesse.");
			return;
		}
		final int id = player.getObjectId();
		final String price = Config.SERVICES_NOBLESS_SELL_PRICE + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_NOBLESS_SELL_ITEM).getName();
		player.ask(new ConfirmDlg(2010, 0).addString(player.isLangRus() ? "\u0412\u044b \u0445\u043e\u0442\u0438\u0442\u0435 \u0441\u0442\u0430\u0442\u044c \u0414\u0432\u043e\u0440\u044f\u043d\u0438\u043d\u043e\u043c \u0437\u0430 " + price + "?" : "Are you want to become a Noblesse for " + price + "?"), new OnAnswerListener(){
			@Override
			public void sayYes()
			{
				set(id);
			}

			@Override
			public void sayNo()
			{}
		});
	}

	private static void set(final int id)
	{
		final Player player = GameObjectsStorage.getPlayer(id);
		if(player == null)
			return;

		if(!Config.SERVICES_NOBLESS_SELL_ENABLED)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}

		if(player.isNoble())
		{
			player.sendMessage(player.isLangRus() ? "\u0412\u044b \u0443\u0436\u0435 \u0414\u0432\u043e\u0440\u044f\u043d\u0438\u043d." : "You are already Noblesse.");
			return;
		}

		if(player.getBaseLevel() < Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS)
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c " + Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS + "+ \u0443\u0440\u043e\u0432\u0435\u043d\u044c." : "You must have level " + Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS + "+ first.");
			return;
		}

		if(ClassId.values()[player.getBaseClassId()].getLevel() < 3)
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u0430 \u0432\u0442\u043e\u0440\u0430\u044f \u043f\u0440\u043e\u0444\u0435\u0441\u0441\u0438\u044f." : "You must have second class first.");
			return;
		}
		if(getItemCount(player, Config.SERVICES_NOBLESS_SELL_ITEM) >= Config.SERVICES_NOBLESS_SELL_PRICE) {
			if (deleteItem(player, Config.SERVICES_NOBLESS_SELL_ITEM, Config.SERVICES_NOBLESS_SELL_PRICE)) {
				player.setNoble();
				player.getInventory().addItem(7694, 1L);
				player.sendMessage(player.isLangRus() ? "\u041f\u043e\u0437\u0434\u0440\u0430\u0432\u043b\u044f\u0435\u043c! \u0412\u044b \u0442\u0435\u043f\u0435\u0440\u044c \u0414\u0432\u043e\u0440\u044f\u043d\u0438\u043d." : "Congratulations! You are Noblesse now.");
				Log.addLog(player.toString() + " buy status of Noblesse", "services");
			}
		}
		else if(Config.SERVICES_NOBLESS_SELL_ITEM == 57)
			player.sendPacket(new SystemMessage(279));
		else
			player.sendPacket(new SystemMessage(351));
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Nobless sell");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
