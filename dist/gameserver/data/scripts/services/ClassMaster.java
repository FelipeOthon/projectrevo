package services;

import java.util.StringTokenizer;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.ItemList;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.Util;

public class ClassMaster extends Functions implements ScriptFile
{
	public void list()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final ClassId classId = player.getClassId();
		int jobLevel = classId.getLevel();
		final int level = player.getLevel();
		final boolean en = !player.isLangRus();
		final StringBuilder append = new StringBuilder();
		if(Config.ALLOW_CLASS_MASTERS_LIST.isEmpty() || !Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
			jobLevel = 4;
		if((level >= 20 && jobLevel == 1 || level >= 40 && jobLevel == 2 || level >= 76 && jobLevel == 3) && Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
		{
			append.append("<title>" + (en ? "Profession Master" : "Мастер Профессий") + "</title><br><center><br><br><br><br>");
			final ItemTemplate item = ItemTable.getInstance().getTemplate(Config.CLASS_MASTERS_PRICE_ITEM[jobLevel]);
			if(Config.CLASS_MASTERS_PRICE_LIST[jobLevel] > 0)
				append.append("<font color=\"LEVEL\">" + (en ? "Price" : "Стоимость") + ": ").append(Util.formatAdena(Config.CLASS_MASTERS_PRICE_LIST[jobLevel])).append(" ").append(item.getName()).append("</font><br1>");
			for(final ClassId cid : ClassId.values())
				if(!Config.EVER_BASE_CLASS && cid.childOf(classId) && cid.getLevel() == classId.getLevel() + 1)
					append.append("<br><br><br><button value=\"").append(cid.name()).append("\" action=\"bypass -h scripts_services.ClassMaster:change ").append(cid.getId()).append("\" width=135 height=23 back=\"L2UI_CH3.bigbutton3_down\" fore=\"L2UI_CH3.bigbutton3\"><br1>");
				else if(Config.EVER_BASE_CLASS && (cid.getLevel() == 2 && jobLevel == 1 || cid.childOf(classId) && cid.getLevel() == classId.getLevel() + 1 && jobLevel != 1))
					append.append("<br><br><br><button value=\"").append(cid.name()).append("\" action=\"bypass -h scripts_services.ClassMaster:change ").append(cid.getId()).append("\" width=135 height=23 back=\"L2UI_CH3.bigbutton3_down\" fore=\"L2UI_CH3.bigbutton3\"><br1>");
			show(append.toString(), player);
		}
		else
		{
			if(jobLevel == 4)
				append.append("<title>" + (en ? "Profession Master" : "Мастер Профессий") + "</title><br><center></center><br>" + (en ? "There is no class changes for you any more." : "Для Вас больше нет доступных профессий."));
			else
				switch(jobLevel)
				{
					case 1:
					{
						append.append("<title>" + (en ? "Profession Master" : "Мастер Профессий") + "</title><br><center></center><br>" + (en ? "To change your profession you have to reach level " : "Для того, чтобы сменить вашу профессию, Вы должны достичь ") + "20" + (en ? "." : "-го уровня."));
						break;
					}
					case 2:
					{
						append.append("<title>" + (en ? "Profession Master" : "Мастер Профессий") + "</title><br><center></center><br>" + (en ? "To change your profession you have to reach level " : "Для того, чтобы сменить вашу профессию, Вы должны достичь ") + "40" + (en ? "." : "-го уровня."));
						break;
					}
					case 3:
					{
						append.append("<title>" + (en ? "Profession Master" : "Мастер Профессий") + "</title><br><center></center><br>" + (en ? "To change your profession you have to reach level " : "Для того, чтобы сменить вашу профессию, Вы должны достичь ") + "76" + (en ? "." : "-го уровня."));
						break;
					}
				}
			show(append.toString(), player);
		}
	}

	public void change(final String[] command)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.ALLOW_CLASS_MASTERS_LIST.isEmpty() || !Config.ALLOW_CLASS_MASTERS_LIST.contains(player.getClassId().getLevel()))
		{
			player.sendMessage(player.isLangRus() ? "Для Вас больше нет доступных профессий." : "There is no class changes for you any more.");
			return;
		}
		final int jobLevel = player.getClassId().getLevel();
		final StringTokenizer st = new StringTokenizer(command[0]);
		final int val = Integer.parseInt(st.nextToken());
		final int price = Config.CLASS_MASTERS_PRICE_LIST[jobLevel];
		ItemInstance z= player.getInventory().getItemByItemId(Config.CLASS_MASTERS_PRICE_ITEM[jobLevel]);

		if(player.getInventory().getItemByItemId(Config.CLASS_MASTERS_PRICE_ITEM[jobLevel])==null || z.getCount()<price)//if(Config.CLASS_MASTERS_PRICE_ITEM[jobLevel] == 57 && z.getCount()<price)
			//player.sendPacket(SystemMessage.item);
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
		else if(price <= 0 || deleteItem(player, Config.CLASS_MASTERS_PRICE_ITEM[jobLevel], price))
		{
			player.setClassId(val, false, false);
			if(player.getClassId().getLevel() == 4)
			{
				player.sendPacket(new SystemMessage(1606));
				if(Config.CLASS_MASTERS_REWARD.length > 1)
				{
					boolean upd = false;
					for(int i = 0; i < Config.CLASS_MASTERS_REWARD.length; i += 2)
					{
						player.getInventory().addItem(Config.CLASS_MASTERS_REWARD[i], Config.CLASS_MASTERS_REWARD[i + 1]);
						player.sendPacket(SystemMessage.obtainItems(Config.CLASS_MASTERS_REWARD[i], Config.CLASS_MASTERS_REWARD[i + 1], 0));
						upd = true;
					}
					if(upd)
						player.sendPacket(new ItemList(player, false));
				}
			}
			else
				player.sendPacket(new SystemMessage(1308));
			if(player.getLevel() >= 40 && player.getClassId().getLevel() == 2 || player.getLevel() >= 76 && player.getClassId().getLevel() == 3)
				list();
		}
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: ClassMaster");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
