package services;

import java.util.HashMap;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.pledge.ClanMember;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.PledgeShowInfoUpdate;
import l2s.gameserver.network.l2.s2c.PledgeStatusChanged;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ClanTable;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.tables.SkillTree;
import l2s.gameserver.utils.Log;
import l2s.gameserver.utils.SiegeUtils;
import l2s.gameserver.utils.Util;

public class Clan extends Functions implements ScriptFile
{
	public void list()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_CLAN_ITEM < 1)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		if(!Config.SERVICES_CLAN_PAGE_PATH.isEmpty())
		{
			show(HtmCache.getInstance().getHtml(Config.SERVICES_CLAN_PAGE_PATH, player), player);
			return;
		}
		final boolean en = !player.isLangRus();
		final l2s.gameserver.model.pledge.Clan clan = player.getClan();
		String append = "<br><center><font color=\"LEVEL\">" + (en ? "Clan service" : "\u041a\u043b\u0430\u043d\u043e\u0432\u044b\u0439 \u0441\u0435\u0440\u0432\u0438\u0441") + "</font><br><br><br>";
		if(Config.SERVICES_CHANGE_CLAN_NAME_PRICE > 0)
			append = append + "<button value=\"" + (en ? "Clan name" : "\u0418\u043c\u044f \u043a\u043b\u0430\u043d\u0430") + "\" action=\"bypass -h scripts_services.Clan:rename_clan_page\" width=135 height=23 back=\"L2UI_CH3.bigbutton3_down\" fore=\"L2UI_CH3.bigbutton3\"><br><br>";
		if(Config.SERVICES_CLAN_REP_PRICE > 0)
			append = append + "<button value=\"+" + Config.SERVICES_CLAN_REP_COUNT + " " + (en ? "CR" : "\u0420\u041a") + " = " + Config.SERVICES_CLAN_REP_PRICE + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_CLAN_ITEM).getName() + "\" action=\"bypass -h scripts_services.Clan:reputation\" width=135 height=23 back=\"L2UI_CH3.bigbutton3_down\" fore=\"L2UI_CH3.bigbutton3\"><br><br>";
		if(clan == null || clan.getLevel() < 8)
		{
			final int i = clan == null || clan.getLevel() == 0 ? Config.SERVICES_CLAN_LVL1_PRICE : clan.getLevel() == 1 ? Config.SERVICES_CLAN_LVL2_PRICE : clan.getLevel() == 2 ? Config.SERVICES_CLAN_LVL3_PRICE : clan.getLevel() == 3 ? Config.SERVICES_CLAN_LVL4_PRICE : clan.getLevel() == 4 ? Config.SERVICES_CLAN_LVL5_PRICE : clan.getLevel() == 5 ? Config.SERVICES_CLAN_LVL6_PRICE : clan.getLevel() == 6 ? Config.SERVICES_CLAN_LVL7_PRICE : Config.SERVICES_CLAN_LVL8_PRICE;
			final int n = clan == null ? 1 : clan.getLevel() + 1;
			append = append + "<button value=\"" + n + "" + (en ? " lvl of clan" : "-\u0439 \u043b\u0432\u043b \u043a\u043b\u0430\u043d\u0430") + " = " + i + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_CLAN_ITEM).getName() + "\" action=\"bypass -h scripts_services.Clan:levelUp\" width=135 height=23 back=\"L2UI_CH3.bigbutton3_down\" fore=\"L2UI_CH3.bigbutton3\"><br><br>";
		}
		if(Config.SERVICES_CLAN_SKILLS_PRICE > 0 && (clan == null || !SkillTree.getInstance().getPledgeSkillsMax(player).isEmpty()))
			append = append + "<button value=\"" + (en ? "Clan skills" : "\u0421\u043a\u0438\u043b\u044b \u043a\u043b\u0430\u043d\u0430") + " = " + Config.SERVICES_CLAN_SKILLS_PRICE + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_CLAN_ITEM).getName() + "\" action=\"bypass -h scripts_services.Clan:getSkills\" width=135 height=23 back=\"L2UI_CH3.bigbutton3_down\" fore=\"L2UI_CH3.bigbutton3\"><br><br>";
		append += "</center><br>";
		show(append, player);
	}

	public void getSkills()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_CLAN_ITEM < 1 || Config.SERVICES_CLAN_SKILLS_PRICE < 1)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		final l2s.gameserver.model.pledge.Clan clan = player.getClan();
		if(clan == null)
		{
			if(!player.isLangRus())
				player.sendMessage("You have no clan.");
			else
				player.sendMessage("\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u043a\u043b\u0430\u043d\u0430.");
			return;
		}
		if(clan.getLevel() < 8)
		{
			if(!player.isLangRus())
				player.sendMessage("Requires Level 8 Clan.");
			else
				player.sendMessage("\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c 8-\u0439 \u0443\u0440\u043e\u0432\u0435\u043d\u044c \u043a\u043b\u0430\u043d\u0430.");
			return;
		}
		
		if (player.getInventory().getItemByItemId(Config.SERVICES_CLAN_ITEM) == null)
		{
			return;
		}
		
		if (player.getInventory().getItemByItemId(Config.SERVICES_CLAN_ITEM).getCount() < Config.SERVICES_CLAN_SKILLS_PRICE)
		{
			return;
		}

		if(deleteItem(player, Config.SERVICES_CLAN_ITEM, Config.SERVICES_CLAN_SKILLS_PRICE))
		{
			final HashMap<Integer, Integer> _skills = SkillTree.getInstance().getPledgeSkillsMax(player);
			if(!_skills.isEmpty())
				for(final int id : _skills.keySet())
				{
					final Skill sk = SkillTable.getInstance().getInfo(id, _skills.get(id));
					if(sk == null)
						continue;
					clan.addNewSkill(sk, true);
					clan.broadcastToOnlineMembers(new L2GameServerPacket[] { new SystemMessage(1788).addSkillName(id, _skills.get(id)) });
				}
			clan.sendMessageToAll(player.getName() + " has acquired all clan skills!", player.getName() + " \u043f\u0440\u0438\u043e\u0431\u0440\u0435\u043b \u0432\u0441\u0435 \u043a\u043b\u0430\u043d\u043e\u0432\u044b\u0435 \u0441\u043a\u0438\u043b\u044b!");
			list();
		}
		else if(Config.SERVICES_CLAN_ITEM == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	public void reputation()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_CLAN_ITEM < 1 || Config.SERVICES_CLAN_REP_PRICE < 1)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		final l2s.gameserver.model.pledge.Clan clan = player.getClan();
		if(clan == null)
		{
			if(!player.isLangRus())
				player.sendMessage("You have no clan.");
			else
				player.sendMessage("\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u043a\u043b\u0430\u043d\u0430.");
			return;
		}
		if(clan.getLevel() < 5)
		{
			if(!player.isLangRus())
				player.sendMessage("Requires Level 5 Clan.");
			else
				player.sendMessage("\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c 5-\u0439 \u0443\u0440\u043e\u0432\u0435\u043d\u044c \u043a\u043b\u0430\u043d\u0430.");
			return;
		}
		if(getItemCount(player, Config.SERVICES_CLAN_ITEM) < Config.SERVICES_CLAN_REP_PRICE)
		{
			if(Config.SERVICES_CLAN_ITEM == 57)
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			else
				player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
			return;
		}
		if(clan.getReputationScore() >= 10000000)
		{
			if(!player.isLangRus())
				player.sendMessage("You have reached the maximum.");
			else
				player.sendMessage("\u0412\u044b \u0434\u043e\u0441\u0442\u0438\u0433\u043b\u0438 \u043c\u0430\u043a\u0441\u0438\u043c\u0443\u043c\u0430.");
			return;
		}
		removeItem(player, Config.SERVICES_CLAN_ITEM, Config.SERVICES_CLAN_REP_PRICE);
		clan.incReputation(Config.SERVICES_CLAN_REP_COUNT, false, "ClanService");
		if(!player.isLangRus())
			player.sendMessage(Config.SERVICES_CLAN_REP_COUNT + " points added to your clan's reputation.");
		else
			player.sendMessage("\u0412\u0430\u0448\u0435\u043c\u0443 \u043a\u043b\u0430\u043d\u0443 \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d\u043e " + Config.SERVICES_CLAN_REP_COUNT + " \u0440\u0435\u043f\u0443\u0442\u0430\u0446\u0438\u0438.");
		list();
	}

	public void levelUp()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_CLAN_ITEM < 1)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		final l2s.gameserver.model.pledge.Clan clan = player.getClan();
		if(clan == null)
		{
			if(!player.isLangRus())
				player.sendMessage("You have no clan.");
			else
				player.sendMessage("\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u043a\u043b\u0430\u043d\u0430.");
			return;
		}
		if(clan.getLevel() >= 8)
		{
			if(!player.isLangRus())
				player.sendMessage("Maximum level of your clan.");
			else
				player.sendMessage("\u0423\u0440\u043e\u0432\u0435\u043d\u044c \u0432\u0430\u0448\u0435\u0433\u043e \u043a\u043b\u0430\u043d\u0430 \u043c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u0435\u043d.");
			return;
		}

		final int i = clan.getLevel() == 0 ? Config.SERVICES_CLAN_LVL1_PRICE : clan.getLevel() == 1 ? Config.SERVICES_CLAN_LVL2_PRICE : clan.getLevel() == 2 ? Config.SERVICES_CLAN_LVL3_PRICE : clan.getLevel() == 3 ? Config.SERVICES_CLAN_LVL4_PRICE : clan.getLevel() == 4 ? Config.SERVICES_CLAN_LVL5_PRICE : clan.getLevel() == 5 ? Config.SERVICES_CLAN_LVL6_PRICE : clan.getLevel() == 6 ? Config.SERVICES_CLAN_LVL7_PRICE : Config.SERVICES_CLAN_LVL8_PRICE;
		if (player.getInventory().getItemByItemId(Config.SERVICES_CLAN_ITEM) == null)
		{
			return;
		}
		if (player.getInventory().getItemByItemId(Config.SERVICES_CLAN_ITEM).getCount() < i)
		{
			return;
		}
		if(deleteItem(player, Config.SERVICES_CLAN_ITEM, i))
		{
			clan.setLevel((byte) (clan.getLevel() + 1));
			clan.updateClanInDB();
			player.doCast(SkillTable.getInstance().getInfo(5103, 1), player, true);
			if(clan.getLevel() > 3)
				SiegeUtils.addSiegeSkills(player);
			if(clan.getLevel() == 5)
				player.sendPacket(new SystemMessage(1771));
			final PledgeShowInfoUpdate pu = new PledgeShowInfoUpdate(clan);
			final PledgeStatusChanged ps = new PledgeStatusChanged(clan);
			for(final ClanMember mbr : clan.getMembers())
				if(mbr.isOnline())
				{
					mbr.getPlayer().updatePledgeClass();
					mbr.getPlayer().sendPacket(new IBroadcastPacket[] { Msg.CLANS_SKILL_LEVEL_HAS_INCREASED, pu, ps });
					mbr.getPlayer().broadcastUserInfo(true);
				}
			list();
		}
		else if(Config.SERVICES_CLAN_ITEM == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	public void rename_clan_page()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_CLAN_ITEM < 1 || Config.SERVICES_CHANGE_CLAN_NAME_PRICE < 1)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		if(player.getClan() == null || !player.isClanLeader())
		{
			player.sendPacket(new SystemMessage(9).addString(player.getName()));
			return;
		}
		String append = player.isLangRus() ? "\u0421\u043c\u0435\u043d\u0430 \u0438\u043c\u0435\u043d\u0438 \u043a\u043b\u0430\u043d\u0430" : "Rename clan";
		append += "<br>";
		append = append + "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.Rename.RenameFor").addString(Util.formatAdena(Config.SERVICES_CHANGE_CLAN_NAME_PRICE)).addItemName(Config.SERVICES_CLAN_ITEM).toString(player) + "</font>";
		append += "<table>";
		append = append + "<tr><td>" + new CustomMessage("scripts.services.Rename.NewName").toString(player) + ": <edit var=\"new_name\" width=120></td></tr>";
		append += "<tr><td></td></tr>";
		append = append + "<tr><td><button value=\"" + new CustomMessage("scripts.services.Rename.RenameButton").toString(player) + "\" action=\"bypass -h scripts_services.Clan:rename_clan $new_name\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>";
		append += "</table>";
		show(append, player);
	}

	public void rename_clan(final String[] param)
	{
		final Player player = getSelf();
		if(player == null || param == null || param.length == 0)
			return;
		if(Config.SERVICES_CLAN_ITEM < 1 || Config.SERVICES_CHANGE_CLAN_NAME_PRICE < 1)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		final l2s.gameserver.model.pledge.Clan clan = player.getClan();
		if(clan == null || !player.isClanLeader())
		{
			player.sendPacket(new SystemMessage(9).addString(player.getName()));
			return;
		}
		if(player.getEvent(SiegeEvent.class) != null)
		{
			show(new CustomMessage("scripts.services.Rename.SiegeNow"), player);
			return;
		}
		if(!Util.isMatchingRegexp(param[0], Config.CLAN_NAME_TEMPLATE))
		{
			player.sendPacket(new SystemMessage(261));
			return;
		}
		if(ClanTable.getInstance().getClanByName(param[0]) != null)
		{
			player.sendPacket(new SystemMessage(79));
			return;
		}
		if(getItemCount(player, Config.SERVICES_CLAN_ITEM) < Config.SERVICES_CHANGE_CLAN_NAME_PRICE)
		{
			if(Config.SERVICES_CLAN_ITEM == 57)
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			else
				player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
			return;
		}
		removeItem(player, Config.SERVICES_CLAN_ITEM, Config.SERVICES_CHANGE_CLAN_NAME_PRICE);
		final String oldName = clan.getName();
		clan.setName(param[0]);
		clan.updateClanInDB();
		show(new CustomMessage("scripts.services.Rename.changedname").addString(oldName).addString(clan.getName()), player);
		clan.broadcastClanStatus(true, true, false);
		Log.add(player.toString() + " ClanName " + oldName + " changed to " + clan.getName(), "services");
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Clan");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
