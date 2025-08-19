package services;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.listener.actor.player.OnAnswerListener;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.network.l2.s2c.ConfirmDlg;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.utils.Util;

public class Level extends Functions implements ScriptFile
{
	public void list()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		list1(player.getObjectId());
	}

	private static void list1(final int id)
	{
		final Player player = GameObjectsStorage.getPlayer(id);
		if(player == null)
			return;

		if(!Config.SERVICES_LVL_PAGE_PATH.isEmpty())
		{
			show(HtmCache.getInstance().getHtml(Config.SERVICES_LVL_PAGE_PATH, player), player);
			return;
		}

		String html = "<br><br><br><center>";
		if(Config.SERVICES_LVL80_ITEM > 0 && player.getLevel() < 80)
			html = html + "<br><button value=\"" + (player.isLangRus() ? "\u0423\u0440\u043e\u0432\u0435\u043d\u044c 80" : "Set level 80") + " = " + Util.format(Config.SERVICES_LVL80_PRICE, player.isLangRus()) + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_LVL80_ITEM).getName() + " \" action=\"bypass -h scripts_services.Level:get80\" width=140 height=21 back=\"L2UI_CH3.msnbutton_down\" fore=\"L2UI_CH3.msnbutton\"></a>";
		if(Config.SERVICES_LVL_UP_ITEM > 0 && player.getLevel() < 80)
			html = html + "<br><button value=\"" + (player.isLangRus() ? "+1 \u0443\u0440\u043e\u0432\u0435\u043d\u044c" : "+1 level") + " = " + Util.format(Config.SERVICES_LVL_UP_PRICE, player.isLangRus()) + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_LVL_UP_ITEM).getName() + " \" action=\"bypass -h scripts_services.Level:increaseLvl\" width=140 height=21 back=\"L2UI_CH3.msnbutton_down\" fore=\"L2UI_CH3.msnbutton\"></a>";
		if(Config.SERVICES_LVL_DOWN_ITEM > 0 && player.getLevel() > (player.isSubClassActive() ? 40 : 1))
			html = html + "<br><button value=\"" + (player.isLangRus() ? "-1 \u0443\u0440\u043e\u0432\u0435\u043d\u044c" : "+1 level") + " = " + Util.format(Config.SERVICES_LVL_DOWN_PRICE, player.isLangRus()) + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_LVL_DOWN_ITEM).getName() + " \" action=\"bypass -h scripts_services.Level:decreaseLvl\" width=140 height=21 back=\"L2UI_CH3.msnbutton_down\" fore=\"L2UI_CH3.msnbutton\"></a>";
		if(Config.SERVICES_SP_ITEM > 0)
			html = html + "<br><button value=\"" + (player.isLangRus() ? "+10\u043a\u043a SP" : "+10kk SP") + " = " + Util.format(Config.SERVICES_SP_PRICE, player.isLangRus()) + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_SP_ITEM).getName() + " \" action=\"bypass -h scripts_services.Level:getSp\" width=140 height=21 back=\"L2UI_CH3.msnbutton_down\" fore=\"L2UI_CH3.msnbutton\"></a>";
		show(html, player);
	}

	public void getSp()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_SP_ITEM <= 0)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		if(getItemCount(player, Config.SERVICES_SP_ITEM) >= Config.SERVICES_SP_PRICE){
		if(deleteItem(player, Config.SERVICES_SP_ITEM, Config.SERVICES_SP_PRICE))
		{
			player.setSp(player.getSp() + 10000000);
			player.sendMessage(player.isLangRus() ? "\u0412\u044b \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u0438 10\u043a\u043a SP." : "You got 10kk SP.");
			player.sendChanges();
			list();
			}
		}
		else if(Config.SERVICES_SP_ITEM == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	public void increaseLvl()
	{
		final Player player = getSelf();
		if(!checkCondition(player))
			return;
		if(Config.SERVICES_LVL_UP_ITEM <= 0)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		if(player.getLevel() >= 80)
		{
			player.sendMessage(player.isLangRus() ? "\u0412\u0430\u0448 \u0443\u0440\u043e\u0432\u0435\u043d\u044c \u043c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u0435\u043d." : "Your level is maximum.");
			return;
		}
		if(getItemCount(player, Config.SERVICES_LVL_UP_ITEM) >= Config.SERVICES_LVL_UP_PRICE) {
			if (deleteItem(player, Config.SERVICES_LVL_UP_ITEM, Config.SERVICES_LVL_UP_PRICE)) {
				final Byte level = (byte) (player.getLevel() + 1);
				final Long exp_add = Experience.LEVEL[level] - player.getExp();
				player.addExpAndSp(exp_add, 0L, false, false);
				list();
			}
		}
		else if(Config.SERVICES_LVL_UP_ITEM == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	public void get80()
	{
		final Player player = getSelf();
		if(!checkCondition(player))
			return;
		if(Config.SERVICES_LVL80_ITEM <= 0)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		if(player.getLevel() >= 80)
		{
			player.sendMessage(player.isLangRus() ? "\u0412\u0430\u0448 \u0443\u0440\u043e\u0432\u0435\u043d\u044c \u043c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u0435\u043d." : "Your level is maximum.");
			return;
		}
		if(getItemCount(player, Config.SERVICES_LVL80_ITEM) >= Config.SERVICES_LVL80_PRICE) {
			if (deleteItem(player, Config.SERVICES_LVL80_ITEM, Config.SERVICES_LVL80_PRICE)) {
				final Long exp_add = Experience.LEVEL[80] - player.getExp();
				player.addExpAndSp(exp_add, 0L, false, false);
				list();
			}
		}
		else if(Config.SERVICES_LVL80_ITEM == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	private static boolean checkCondition(final Player player)
	{
		if(player == null)
			return false;
		if(player.isAlikeDead() || player.isInCombat() || player.isAttackingNow() || player.getTeam() > 0)
			return false;
		if(player.isInOlympiadMode())
		{
			player.sendMessage(player.isLangRus() ? "\u0412\u043e \u0432\u0440\u0435\u043c\u044f \u043e\u043b\u0438\u043c\u043f\u0438\u0439\u0441\u043a\u043e\u0433\u043e \u0431\u043e\u044f \u043d\u0435\u043b\u044c\u0437\u044f \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0434\u0430\u043d\u043d\u0443\u044e \u0444\u0443\u043d\u043a\u0446\u0438\u044e." : "During the Olympic battle you can not use this feature.");
			return false;
		}
		return true;
	}

	private static boolean checker(final Player player)
	{
		if(player == null)
			return false;
		if(Config.SERVICES_LVL_DOWN_ITEM <= 0)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return false;
		}
		if(player.getLevel() <= (player.isSubClassActive() ? 40 : 1))
		{
			player.sendMessage(player.isLangRus() ? "\u0412\u0430\u0448 \u0443\u0440\u043e\u0432\u0435\u043d\u044c \u043c\u0438\u043d\u0438\u043c\u0430\u043b\u0435\u043d." : "Your level is minimum.");
			return false;
		}
		return true;
	}

	public void decreaseLvl()
	{
		final Player player = getSelf();
		if(!checker(player))
			return;
		if(Config.SERVICES_LVL_DOWN_CLASS)
		{
			final int lvl = player.getLevel();
			final int jobLevel = player.getClassId().getLevel();
			if(jobLevel == 2 && lvl <= 20 || jobLevel == 3 && lvl <= 40 || jobLevel == 4 && lvl <= 76)
			{
				final int id = player.getObjectId();
				player.ask(new ConfirmDlg(2010, 0).addString(player.isLangRus() ? "\u0415\u0441\u043b\u0438 \u0412\u044b \u043f\u0440\u043e\u0434\u043e\u043b\u0436\u0438\u0442\u0435, \u0442\u043e \u043f\u043e\u0442\u0435\u0440\u044f\u0435\u0442\u0435 \u0442\u0435\u043a\u0443\u0449\u0443\u044e \u043f\u0440\u043e\u0444\u0435\u0441\u0441\u0438\u044e. \u041f\u0440\u043e\u0434\u043e\u043b\u0436\u0438\u0442\u044c?" : "You will lost your current class. Continue?"), new OnAnswerListener(){
					@Override
					public void sayYes()
					{
						downLvl(id);
					}

					@Override
					public void sayNo()
					{}
				});
				return;
			}
		}
		downLvl(player.getObjectId());
	}

	private static void downLvl(final int id)
	{
		final Player player = GameObjectsStorage.getPlayer(id);
		if(!checker(player))
			return;
		if(getItemCount(player, Config.SERVICES_LVL_DOWN_ITEM) >= Config.SERVICES_LVL_DOWN_PRICE) {
			if (deleteItem(player, Config.SERVICES_LVL_DOWN_ITEM, Config.SERVICES_LVL_DOWN_PRICE)) {
				final int lvl = player.getLevel();
				if (Config.SERVICES_LVL_DOWN_CLASS) {
					final ClassId classId = player.getClassId();
					final int jobLevel = classId.getLevel();
					if (jobLevel == 2 && lvl <= 20 || jobLevel == 3 && lvl <= 40 || jobLevel == 4 && lvl <= 76) {
						if (!player.isSubClassActive() && jobLevel == 3) {
							player.sendMessage(player.isLangRus() ? "\u0412\u044b \u043d\u0435 \u043c\u043e\u0436\u0435\u0442\u0435 \u043f\u043e\u0442\u0435\u0440\u044f\u0442\u044c \u0442\u0435\u043a\u0443\u0449\u0443\u044e \u043f\u0440\u043e\u0444\u0435\u0441\u0441\u0438\u044e \u0431\u0430\u0437\u043e\u0432\u043e\u0433\u043e \u043a\u043b\u0430\u0441\u0441\u0430." : "You can't lost current base class.");
							return;
						}
						final int val = classId.getParent().getId();
						player.setClassId(val, true, false);
						player.checkSkills(1);
					}
				}
				final Byte level = (byte) (lvl - 1);
				final Long exp_add = Experience.LEVEL[level] - player.getExp();
				player.addExpAndSp(exp_add, 0L, false, false);
				player.sendMessage(player.isLangRus() ? "\u0412\u0430\u0448 \u0443\u0440\u043e\u0432\u0435\u043d\u044c \u043f\u043e\u043d\u0438\u0436\u0435\u043d." : "Your level decreased.");
				list1(player.getObjectId());
			}
		}
		else if(Config.SERVICES_LVL_DOWN_ITEM == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	public void getOlyPoints()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_OP_ITEM <= 0)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		if(!player.isNoble())
		{
			player.sendMessage(player.isLangRus() ? "\u0414\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0442\u043e\u043b\u044c\u043a\u043e \u0434\u043b\u044f \u0414\u0432\u043e\u0440\u044f\u043d." : "Allowed for Noblesse only.");
			return;
		}
		final int id = player.getObjectId();
		player.ask(new ConfirmDlg(2010, 0).addString(player.isLangRus() ? "\u0415\u0441\u043b\u0438 \u0443 \u0432\u0430\u0441 \u043c\u0435\u043d\u0435\u0435 3-\u0445 \u043e\u0447\u043a\u043e\u0432 \u041e\u043b\u0438\u043c\u043f\u0438\u0430\u0434\u044b, \u0442\u043e \u0432\u0430\u0448\u0435 \u043e\u0431\u0449\u0435\u0435 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u043e\u0447\u043a\u043e\u0432 \u0431\u0443\u0434\u0435\u0442 \u0440\u0430\u0432\u043d\u043e " + Config.OLYMPIAD_POINTS_DEFAULT + ". \u0416\u0435\u043b\u0430\u0435\u0442\u0435 \u043f\u0440\u043e\u0434\u043e\u043b\u0436\u0438\u0442\u044c?" : "If you have less than 3 points, your points total will be " + Config.OLYMPIAD_POINTS_DEFAULT + ". Continue?"), new OnAnswerListener(){
			@Override
			public void sayYes()
			{
				setOlyPoints(id);
			}

			@Override
			public void sayNo()
			{}
		});
	}

	private static void setOlyPoints(final int id)
	{
		final Player player = GameObjectsStorage.getPlayer(id);
		if(player == null)
			return;

		if(Config.SERVICES_OP_ITEM <= 0)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}

		if(!player.isNoble())
		{
			player.sendMessage(player.isLangRus() ? "\u0414\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0442\u043e\u043b\u044c\u043a\u043e \u0434\u043b\u044f \u0414\u0432\u043e\u0440\u044f\u043d." : "Allowed for Noblesse only.");
			return;
		}

		if(Olympiad.getNoblePoints(player.getObjectId()) >= 3)
		{
			player.sendMessage(player.isLangRus() ? "\u0423 \u0432\u0430\u0441 \u0434\u043e\u043b\u0436\u043d\u043e \u0431\u044b\u0442\u044c \u043c\u0435\u043d\u0435\u0435 \u0447\u0435\u043c 3 \u043e\u0447\u043a\u0430." : "Your total points must be low than 3.");
			return;
		}
		if(getItemCount(player, Config.SERVICES_OP_ITEM) >= Config.SERVICES_OP_PRICE) {
			if (deleteItem(player, Config.SERVICES_OP_ITEM, Config.SERVICES_OP_PRICE)) {
				Olympiad.manualSetNoblePoints(player.getObjectId(), Config.OLYMPIAD_POINTS_DEFAULT);
				player.sendMessage(player.isLangRus() ? "\u0422\u0435\u043f\u0435\u0440\u044c \u0443 \u0432\u0430\u0441 " + Config.OLYMPIAD_POINTS_DEFAULT + " \u043e\u0447\u043a\u043e\u0432." : "Your total points: " + Config.OLYMPIAD_POINTS_DEFAULT);
			}
		}
		else
			player.sendPacket(Config.SERVICES_OP_ITEM == 57 ? Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA : Msg.INCORRECT_ITEM_COUNT);
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Level");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
