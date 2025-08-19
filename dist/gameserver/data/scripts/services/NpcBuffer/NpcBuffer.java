package services.NpcBuffer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

import javolution.text.TextBuilder;
import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.Env;
import l2s.gameserver.skills.effects.EffectTemplate;
import l2s.gameserver.tables.GmListTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.EffectsComparator;
import l2s.gameserver.utils.Util;
import services.RateBonus;

public class NpcBuffer extends Functions implements ScriptFile
{
	public void doBuffGroup(final String[] args)
	{
		final Player player = getSelf();
		if(!checkCondition(player))
			return;
		if(Config.BUFF_PICE_GRP > 0 && player.getLevel() > Config.FREE_BUFFS_MAX_LVL)
		{
			if(!deleteItem(player, Config.BUFF_ITEM_GRP, Config.BUFF_PICE_GRP))
			{
				player.sendPacket(Config.BUFF_ITEM_GRP == 57 ? Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA : Msg.INCORRECT_ITEM_COUNT);
				return;
			}
		}
		final boolean sn = player.getServitor() != null && player.getVarBoolean("BuffSummon");
		for(final int[] buff : Config.GROUP_BUFFS)
			if(buff[2] == Integer.valueOf(args[0]))
				giveBuff(player, SkillTable.getInstance().getInfo(buff[0], buff[1]), sn, 0);
		if(args.length == 1)
			show(HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffs.htm", player), player);
		else
			show(HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffs" + (args[1].equals("0") ? "" : "-" + args[1]) + ".htm", player), player);
	}

	public void doBuff(final String[] args)
	{
		final Player player = getSelf();
		if(!checkCondition(player))
			return;
		final int id = Integer.valueOf(args[0]);
		final int lvl = Integer.valueOf(args[1]);
		if(!Config.NPC_BUFFS.containsKey(id) || Config.NPC_BUFFS.get(id) != lvl)
		{
			ScriptFile._log.warn("Player " + player.getName() + " [" + player.getObjectId() + "] tried to use illegal buff: " + id);
			GmListTable.broadcastMessageToGMs("Player " + player.getName() + " [" + player.getObjectId() + "] tried to use illegal buff: " + id);
			player.sendMessage("\u0413\u041c \u0438\u043d\u0444\u043e\u0440\u043c\u0438\u0440\u043e\u0432\u0430\u043d. \u041e\u0436\u0438\u0434\u0430\u0439\u0442\u0435 \u0431\u0430\u043d\u0430.");
			return;
		}
		if(Config.BUFF_PRICE_ONE > 0 && player.getLevel() > Config.FREE_BUFFS_MAX_LVL)
		{
			if(!deleteItem(player, Config.BUFF_ITEM_ONE, Config.BUFF_PRICE_ONE))
			{
				player.sendPacket(Config.BUFF_ITEM_ONE == 57 ? Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA : Msg.INCORRECT_ITEM_COUNT);
				return;
			}
		}
		giveBuff(player, SkillTable.getInstance().getInfo(id, lvl), player.getServitor() != null && player.getVarBoolean("BuffSummon"), 0);
		if(args.length == 2)
			show(HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffs.htm", player), player);
		else
			show(HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffs" + (args[2].equals("0") ? "" : "-" + args[2]) + ".htm", player), player);
	}

	public void doHeal(String[] args)
	{
		Player player = getSelf();
		if(!checkCondition(player))
			return;

		Servitor pet = player.getServitor();
		if(!player.getVarBoolean("BuffSummon") || pet == null)
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp(), false);
			player.setCurrentCp(player.getMaxCp());
			player.sendPacket(new CustomMessage("services.NpcBuffer.healed.player"));
		}
		else
		{
			pet.setCurrentHpMp(pet.getMaxHp(), pet.getMaxMp(), false);
			player.sendPacket(new CustomMessage("services.NpcBuffer.healed.servitor"));
		}

		if(args[0].equals("s"))
			schemes();
		else if(args[0].equals("b"))
			show(HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffs.htm", player), player);
		else
			show(HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffs" + (args[0].equals("0") ? "" : "-" + args[0]) + ".htm", player), player);
	}

	public void doCancel(final String[] args)
	{
		final Player player = getSelf();
		if(!checkCondition(player))
			return;
		final Servitor pet = player.getServitor();
		if((!player.getVarBoolean("BuffSummon") || pet == null) && player.getAbnormalList().getEffectsBySkillId(4515) == null && player.getAbnormalList().getEffectsBySkillId(3632) == null)
			player.getAbnormalList().stop();
		else if(pet != null && pet.getAbnormalList().getEffectsBySkillId(4515) == null)
			pet.getAbnormalList().stopAll();
		if(args[0].equals("s"))
			schemes();
		else if(args[0].equals("b"))
			show(HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffs.htm", player), player);
		else
			show(HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffs" + (args[0].equals("0") ? "" : "-" + args[0]) + ".htm", player), player);
	}

	public void SelectBuffsDeluxe(String[] args)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return;
		if(npc.getNpcId() != Config.SERVICES_Buffer_Id)
			return;
		player.turn(npc, 3000);
		if(!Config.ALLOW_DELUXE_BUFF)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		boolean buy = false;
		final boolean ru = player.isLangRus();
		if((Config.DELUXE_BUFF_ITEM <= 0 || player.getInventory().getItemByItemId(Config.DELUXE_BUFF_ITEM) == null) && (!Config.DELUXE_BUFF_PREMIUM || !player.isPremium()))
		{
			final String v = player.getVar("PremiumBuff");
			if(v == null)
			{
				player.sendMessage(ru ? "\u041d\u0443\u0436\u0435\u043d \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f \u043a \u0431\u0430\u0444\u0444\u0443." : "Need premium access for buff.");
				buy = true;
			}
			else if(Long.parseLong(v) < System.currentTimeMillis())
			{
				player.sendMessage(ru ? "\u0412\u0430\u0448 \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f \u043a \u0431\u0430\u0444\u0444\u0443 \u0437\u0430\u043a\u043e\u043d\u0447\u0438\u043b\u0441\u044f." : "Your premium access for buff is over.");
				player.unsetVar("PremiumBuff");
				buy = true;
			}
		}
		String html = HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffsDeluxe" + (args[0].equals("0") ? "" : "-" + args[0]) + ".htm", player);
		html = html.replaceFirst("%action%", buy ? ru ? "\u041a\u0443\u043f\u0438\u0442\u044c \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f" : "Buy premium access" : ru ? "\u041f\u0440\u043e\u0434\u043b\u0438\u0442\u044c \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f" : "Extend premium access");
		show(html, player);
	}

	public void doBuffDeluxe(final String[] args)
	{
		final Player player = getSelf();
		if(!checkCondition(player))
			return;
		if(!Config.ALLOW_DELUXE_BUFF)
		{
			if(player.isLangRus())
				player.sendMessage("Сервис отключен.");
			else
				player.sendMessage("Service disabled.");
			return;
		}
		final int id = Integer.valueOf(args[0]);
		final int lvl = Integer.valueOf(args[1]);
		if(!Config.DELUXE_BUFFS.containsKey(id) || Config.DELUXE_BUFFS.get(id) != lvl)
		{
			ScriptFile._log.warn("Player " + player.getName() + " [" + player.getObjectId() + "] tried to use illegal buff: " + id);
			GmListTable.broadcastMessageToGMs("Player " + player.getName() + " [" + player.getObjectId() + "] tried to use illegal buff: " + id);
			player.sendMessage("\u0413\u041c \u0438\u043d\u0444\u043e\u0440\u043c\u0438\u0440\u043e\u0432\u0430\u043d. \u041e\u0436\u0438\u0434\u0430\u0439\u0442\u0435 \u0431\u0430\u043d\u0430.");
			return;
		}
		if((Config.DELUXE_BUFF_ITEM <= 0 || player.getInventory().getItemByItemId(Config.DELUXE_BUFF_ITEM) == null) && (!Config.DELUXE_BUFF_PREMIUM || !player.isPremium()))
		{
			final String v = player.getVar("PremiumBuff");
			if(v == null)
			{
				player.sendMessage(player.isLangRus() ? "\u041d\u0443\u0436\u0435\u043d \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f \u043a \u0431\u0430\u0444\u0444\u0443." : "Need premium access for buff.");
				RateBonus.boardPB(player);
				return;
			}
			if(Long.parseLong(v) < System.currentTimeMillis())
			{
				player.sendMessage(player.isLangRus() ? "\u0412\u0430\u0448 \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f \u043a \u0431\u0430\u0444\u0444\u0443 \u0437\u0430\u043a\u043e\u043d\u0447\u0438\u043b\u0441\u044f." : "Your premium access for buff is over.");
				player.unsetVar("PremiumBuff");
				RateBonus.boardPB(player);
				return;
			}
		}
		giveBuff(player, SkillTable.getInstance().getInfo(id, lvl), player.getServitor() != null && player.getVarBoolean("BuffSummon"), 0);
		String html = HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffsDeluxe" + (args[2].equals("0") ? "" : "-" + args[2]) + ".htm", player);
		html = html.replaceFirst("%action%", player.isLangRus() ? "\u041f\u0440\u043e\u0434\u043b\u0438\u0442\u044c \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f" : "Extend premium access");
		show(html, player);
	}

	public void doBuffDeluxeCost(final String[] args)
	{
		final Player player = getSelf();
		if(!checkCondition(player))
			return;
		if(Config.DELUXE_BUFF_COST <= 0)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		final int id = Integer.valueOf(args[0]);
		final int lvl = Integer.valueOf(args[1]);
		if(!Config.DELUXE_BUFFS.containsKey(id) || Config.DELUXE_BUFFS.get(id) != lvl)
		{
			ScriptFile._log.warn("Player " + player.getName() + " [" + player.getObjectId() + "] tried to use illegal buff: " + id);
			GmListTable.broadcastMessageToGMs("Player " + player.getName() + " [" + player.getObjectId() + "] tried to use illegal buff: " + id);
			player.sendMessage("\u0413\u041c \u0438\u043d\u0444\u043e\u0440\u043c\u0438\u0440\u043e\u0432\u0430\u043d. \u041e\u0436\u0438\u0434\u0430\u0439\u0442\u0435 \u0431\u0430\u043d\u0430.");
			return;
		}

		if(deleteItem(player, Config.DELUXE_BUFF_COST, Config.DELUXE_BUFF_PRICE))
		{
			giveBuff(player, SkillTable.getInstance().getInfo(id, lvl), player.getServitor() != null && player.getVarBoolean("BuffSummon"), 0);
			if(!Config.DELUXE_BUFF_PAGE_PATH.isEmpty())
				show(HtmCache.getInstance().getHtml(Config.DELUXE_BUFF_PAGE_PATH, player), player);
			return;
		}
		player.sendPacket(Config.DELUXE_BUFF_COST == 57 ? Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA : Msg.INCORRECT_ITEM_COUNT);
	}

	private static boolean checkCondition(final Player player)
	{
		if(player == null)
			return false;
		final NpcInstance npc = player.getLastNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return false;
		if(npc.getNpcId() != Config.SERVICES_Buffer_Id)
			return false;
		if(player.isInOlympiadMode())
			return false;
		if(player.getTeam() > 0)
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0432 \u044d\u0432\u0435\u043d\u0442\u0430\u0445." : "Not available in event.");
			return false;
		}
		if(player.isInCombat())
		{
			if(!player.isLangRus())
				player.sendMessage("Not available while in combat.");
			else
				player.sendMessage("\u041d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u043f\u043e\u043a\u0430 \u0432\u044b \u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0435\u0441\u044c \u0432 \u0431\u043e\u044e.");
			return false;
		}
		if(player.getLevel() > Config.SERVICES_Max_lvl || player.getLevel() < Config.SERVICES_Min_lvl)
		{
			if(!player.isLangRus())
				player.sendMessage("Your level does not meet the requirements!");
			else
				player.sendMessage("\u0412\u0430\u0448 \u0443\u0440\u043e\u0432\u0435\u043d\u044c \u043d\u0435 \u0443\u0434\u043e\u0432\u043b\u0435\u0442\u0432\u043e\u0440\u044f\u0435\u0442 \u0443\u0441\u043b\u043e\u0432\u0438\u044f.");
			return false;
		}
		if(!Config.SERVICES_Buffer_Siege && player.getEvent(SiegeEvent.class) != null)
		{
			if(!player.isLangRus())
				player.sendMessage("You can not use buff in siege.");
			else
				player.sendMessage("\u0411\u0430\u0444\u0444\u0435\u0440 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d \u0432\u043e \u0432\u0440\u0435\u043c\u044f \u043e\u0441\u0430\u0434\u044b.");
			return false;
		}
		if(!Config.BUFFER_ALLOW_PK && player.getKarma() > 0)
		{
			player.sendMessage(player.isLangRus() ? "\u0411\u0430\u0444\u0444\u0435\u0440 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d \u0432 \u043a\u0430\u0440\u043c\u0435." : "You can not use buff with karma.");
			return false;
		}
		if(Config.NO_BUFFER_EPIC && player.isInZone(Zone.ZoneType.epic))
		{
			player.sendMessage(player.isLangRus() ? "\u0411\u0430\u0444\u0444\u0435\u0440 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d \u0432 \u044d\u043f\u0438\u043a \u043b\u043e\u043a\u0430\u0446\u0438\u0438." : "You can not use buff in epic zone.");
			return false;
		}
		player.turn(npc, 3000);
		return true;
	}

	public void target(final String[] args)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return;
		if(npc.getNpcId() != Config.SERVICES_Buffer_Id)
			return;
		player.turn(npc, 3000);
		if(player.getServitor() == null)
		{
			if(!player.isLangRus())
				player.sendMessage("You don't have a summon.");
			else
				player.sendMessage("\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u0441\u0430\u043c\u043c\u043e\u043d\u0430.");
			return;
		}
		if(!player.getVarBoolean("BuffSummon"))
		{
			player.setVar("BuffSummon", "1");
			if(!player.isLangRus())
				player.sendMessage("Target for buffs: Summon");
			else
				player.sendMessage("\u0426\u0435\u043b\u044c \u0434\u043b\u044f \u0431\u0430\u0444\u0444\u0430: \u0421\u0430\u043c\u043c\u043e\u043d");
			player.sendPacket(new ExShowScreenMessage("Summon", 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, false));
		}
		else
		{
			player.unsetVar("BuffSummon");
			if(!player.isLangRus())
				player.sendMessage("Target for buffs: Player");
			else
				player.sendMessage("\u0426\u0435\u043b\u044c \u0434\u043b\u044f \u0431\u0430\u0444\u0444\u0430: \u041f\u0435\u0440\u0441\u043e\u043d\u0430\u0436");
			player.sendPacket(new ExShowScreenMessage("Player", 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, false));
		}
		if(args[0].equals("s"))
			schemes();
		else if(args[0].equals("b"))
			show(HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffs.htm", player), player);
		else
			show(HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffs" + (args[0].equals("0") ? "" : "-" + args[0]) + ".htm", player), player);
	}

	public void SelectBuffs()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return;
		if(npc.getNpcId() != Config.SERVICES_Buffer_Id)
			return;
		player.turn(npc, 3000);
		final String html = HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffs.htm", player);
		show(html, player);
	}

	public void schemes()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return;
		if(npc.getNpcId() != Config.SERVICES_Buffer_Id)
			return;
		player.turn(npc, 3000);
		final TextBuilder html = new TextBuilder();
		html.append("<table width=120>");
		for(final String i : player.schemesB.keySet())
		{
			html.append("<tr>");
			html.append("<td>");
			html.append("<button value=\"" + i + "\" action=\"bypass -h scripts_services.NpcBuffer.NpcBuffer:restoreScheme " + i + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
			html.append("</td>");
			html.append("<td>");
			html.append("<button value=\"-\" action=\"bypass -h scripts_services.NpcBuffer.NpcBuffer:delScheme " + i + "\" width=20 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
			html.append("</td>");
			html.append("</tr>");
		}
		html.append("</table>");
		String content = HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffSchemes.htm", player);
		content = content.replace("%sch%", html.toString());
		show(content, player);
	}

	public void delScheme(final String[] args)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return;
		if(npc.getNpcId() != Config.SERVICES_Buffer_Id)
			return;
		player.turn(npc, 3000);
		player.schemesB.remove(args[0]);
		schemes();
	}

	public void saveScheme(final String[] args)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.BUFFER_SAVE_RESTOR)
		{
			if(player.isLangRus())
				player.sendMessage("\u0424\u0443\u043d\u043a\u0446\u0438\u044f \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u0430.");
			else
				player.sendMessage("Function disabled.");
			return;
		}
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return;
		if(npc.getNpcId() != Config.SERVICES_Buffer_Id)
			return;
		player.turn(npc, 3000);
		final String SchName = args[0];
		if(!Util.isMatchingRegexp(SchName, Config.BUFFER_SCHEM_NAME))
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e\u0435 \u0438\u043c\u044f \u0441\u0445\u0435\u043c\u044b!" : "Incorrect scheme name!");
			return;
		}
		String allbuff = "";
		final Abnormal[] skill = player.getAbnormalList().getAllFirstEffects();
		Arrays.sort(skill, EffectsComparator.getInstance());
		final String v = player.getVar("PremiumBuff");
		final HashMap<Integer, Integer> aEff = v != null && Long.parseLong(v) > System.currentTimeMillis() || Config.DELUXE_BUFF_PREMIUM && player.isPremium() || Config.DELUXE_BUFF_ITEM > 0 && player.getInventory().getItemByItemId(Config.DELUXE_BUFF_ITEM) != null ? Config.DELUXE_BUFFS : Config.NPC_BUFFS;
		for(int i = 0; i < skill.length; ++i)
		{
			final int sId = skill[i].getSkill().getId();
			final int sLvl = skill[i].getSkill().getLevel();
			if(aEff.containsKey(sId) && aEff.get(sId) == sLvl)
				allbuff += sId + "," + sLvl + ";";
		}
		if(allbuff.equals(""))
			player.sendMessage(player.isLangRus() ? "\u041d\u0430 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0435 \u043d\u0435\u0442 \u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b\u0445 \u0431\u0430\u0444\u0444\u043e\u0432 \u0434\u043b\u044f \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u044f." : "No valid buffs to save.");
		else if(player.schemesB.size() < Config.BUFFER_MAX_SCHEM)
		{
			if(player.schemesB.containsKey(SchName))
				player.sendMessage(player.isLangRus() ? "\u042d\u0442\u043e \u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435 \u0443\u0436\u0435 \u0437\u0430\u043d\u044f\u0442\u043e." : "This name is already taken.");
			else
				player.schemesB.put(SchName, allbuff);
		}
		else
			player.sendMessage(player.isLangRus() ? "\u0412\u044b \u0443\u0436\u0435 \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u043b\u0438 \u043c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u044c\u043d\u043e\u0435 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u0441\u0445\u0435\u043c." : "You have already saved maximum schemes count.");
		schemes();
	}

	public void restoreScheme(final String[] args)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.BUFFER_SAVE_RESTOR)
		{
			if(player.isLangRus())
				player.sendMessage("\u0424\u0443\u043d\u043a\u0446\u0438\u044f \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u0430.");
			else
				player.sendMessage("Function disabled.");
			return;
		}
		if(!checkCondition(player))
			return;
		if(Config.BUFF_PICE_GRP > 0 && player.getLevel() > Config.FREE_BUFFS_MAX_LVL)
		{
			if(!deleteItem(player, Config.BUFF_ITEM_GRP, Config.BUFF_PICE_GRP))
			{
				player.sendPacket(Config.BUFF_ITEM_GRP == 57 ? Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA : Msg.INCORRECT_ITEM_COUNT);
				return;
			}
		}
		try
		{
			if(!player.schemesB.containsKey(args[0]))
				return;
			final String allskills = player.schemesB.get(args[0]);
			final StringTokenizer stBuff = new StringTokenizer(allskills, ";");
			String v = player.getVar("PremiumBuff");
			if(v != null && Long.parseLong(v) < System.currentTimeMillis())
			{
				player.sendMessage(player.isLangRus() ? "\u0412\u0430\u0448 \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f \u043a \u0431\u0430\u0444\u0444\u0443 \u0437\u0430\u043a\u043e\u043d\u0447\u0438\u043b\u0441\u044f." : "Your premium access for buff is over.");
				player.unsetVar("PremiumBuff");
				v = null;
			}
			final HashMap<Integer, Integer> aEff = v != null || Config.DELUXE_BUFF_PREMIUM && player.isPremium() || Config.DELUXE_BUFF_ITEM > 0 && player.getInventory().getItemByItemId(Config.DELUXE_BUFF_ITEM) != null ? Config.DELUXE_BUFFS : Config.NPC_BUFFS;
			int i = 0;
			final boolean sn = player.getServitor() != null && player.getVarBoolean("BuffSummon");
			while(stBuff.hasMoreTokens())
			{
				final String s = stBuff.nextToken();
				final StringTokenizer sk = new StringTokenizer(s, ",");
				final int id = Integer.parseInt(sk.nextToken());
				final int lvl = Integer.parseInt(sk.nextToken());
				if(aEff.containsKey(id))
				{
					if(aEff.get(id) != lvl)
						continue;
					giveBuff(player, SkillTable.getInstance().getInfo(id, lvl), sn, i);
					++i;
				}
			}
		}
		catch(Exception e)
		{
			ScriptFile._log.error("NpcBuffer.restoreScheme: ", e);
		}
		schemes();
	}

	private void giveBuff(final Player player, final Skill skill, final boolean sn, final int i)
	{
		for(final EffectTemplate et : skill.getEffectTemplates())
		{
			final Env env = new Env(sn ? player.getServitor() : player, sn ? player.getServitor() : player, skill);
			final Abnormal effect = et.getEffect(env);
			if(Config.BUFFER_BUFFS_TIME > 0L && (effect.getCount() != 1 || effect.getPeriod() != 0L))
				effect.setPeriod(Config.BUFFER_BUFFS_TIME);
			effect.setStartTime(System.currentTimeMillis() + i);
			if(sn)
				player.getServitor().getAbnormalList().add(effect);
			else
				player.getAbnormalList().add(effect);
		}
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: NpcBuffer");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
