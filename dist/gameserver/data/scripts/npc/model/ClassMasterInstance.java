package npc.model;

import java.util.StringTokenizer;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.MerchantInstance;
import l2s.gameserver.network.l2.s2c.ItemList;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Util;

public final class ClassMasterInstance extends MerchantInstance
{
	private static final long serialVersionUID = 1L;

	public ClassMasterInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	private String makeMessage(final Player player)
	{
		final ClassId classId = player.getClassId();
		int jobLevel = classId.getLevel();
		final int level = player.getLevel();
		final StringBuilder html = new StringBuilder();
		if(Config.ALLOW_CLASS_MASTERS_LIST.isEmpty() || !Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
			jobLevel = 4;
		if((level >= 20 && jobLevel == 1 || level >= 40 && jobLevel == 2 || level >= 76 && jobLevel == 3) && Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
		{
			final ItemTemplate item = ItemTable.getInstance().getTemplate(Config.CLASS_MASTERS_PRICE_ITEM[jobLevel]);
			if(Config.CLASS_MASTERS_PRICE_LIST[jobLevel] > 0)
				html.append("Price: ").append(Util.formatAdena(Config.CLASS_MASTERS_PRICE_LIST[jobLevel])).append(" ").append(item.getName()).append("<br1>");
			for(final ClassId cid : ClassId.values())
				if(Config.EVER_BASE_CLASS && cid.getLevel() == 2 && jobLevel == 1)
					html.append("<a action=\"bypass -h npc_").append(getObjectId()).append("_change_class ").append(cid.getId()).append(" ").append(Config.CLASS_MASTERS_PRICE_LIST[jobLevel]).append("\">").append(cid.name()).append("</a><br1>");
				else if(Config.EVER_BASE_CLASS && cid.childOf(classId) && cid.getLevel() == classId.getLevel() + 1 && jobLevel != 1)
					html.append("<a action=\"bypass -h npc_").append(getObjectId()).append("_change_class ").append(cid.getId()).append(" ").append(Config.CLASS_MASTERS_PRICE_LIST[jobLevel]).append("\">").append(cid.name()).append("</a><br1>");
				else if(!Config.EVER_BASE_CLASS && cid.childOf(classId) && cid.getLevel() == classId.getLevel() + 1)
					html.append("<a action=\"bypass -h npc_").append(getObjectId()).append("_change_class ").append(cid.getId()).append(" ").append(Config.CLASS_MASTERS_PRICE_LIST[jobLevel]).append("\">").append(cid.name()).append("</a><br1>");
			player.sendPacket(new NpcHtmlMessage(player, this).setHtml(html.toString()));
		}
		else
			switch(jobLevel)
			{
				case 1:
				{
					html.append("Come back here when you reached level 20 to change your class.");
					break;
				}
				case 2:
				{
					html.append("Come back here when you reached level 40 to change your class.");
					break;
				}
				case 3:
				{
					html.append("Come back here when you reached level 76 to change your class.");
					break;
				}
				case 4:
				{
					html.append("There is no class changes for you any more.");
					break;
				}
			}
		return html.toString();
	}

	@Override
	public void showChatWindow(final Player player, final int val, final Object... replace)
	{
		final NpcHtmlMessage msg = new NpcHtmlMessage(player, this, (String) null, 0);
		final String html = HtmCache.getInstance().getHtml("custom/31860.htm", player);
		msg.setHtml(html);
		msg.replace("%classmaster%", makeMessage(player));
		player.sendPacket(msg);
	}

	@Override
	public void onBypassFeedback(final Player player, final String command)
	{
		if(!canBypassCheck(player, this))
			return;
		final StringTokenizer st = new StringTokenizer(command);
		if(st.nextToken().equals("change_class"))
		{
			if(Config.ALLOW_CLASS_MASTERS_LIST.isEmpty() || !Config.ALLOW_CLASS_MASTERS_LIST.contains(player.getClassId().getLevel()))
			{
				player.sendMessage("There is no class changes for you any more.");
				return;
			}
			final int jobLevel = player.getClassId().getLevel();
			final int val = Integer.parseInt(st.nextToken());
			final int price = Config.CLASS_MASTERS_PRICE_LIST[jobLevel];
			final ItemInstance pay = player.getInventory().getItemByItemId(Config.CLASS_MASTERS_PRICE_ITEM[jobLevel]);
			if(pay != null && pay.getCount() >= price || price <= 0)
			{
				if(price > 0)
					player.getInventory().destroyItem(pay, price, true);
				changeClass(player, val);
			}
			else if(Config.CLASS_MASTERS_PRICE_ITEM[jobLevel] == 57)
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			else
				player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
		}
		else
			super.onBypassFeedback(player, command);
	}

	private void changeClass(final Player player, final int val)
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
	}
}
