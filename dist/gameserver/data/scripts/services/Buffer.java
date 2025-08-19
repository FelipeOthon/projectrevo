package services;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.Env;
import l2s.gameserver.skills.effects.EffectTemplate;
import l2s.gameserver.tables.GmListTable;
import l2s.gameserver.tables.SkillTable;

public class Buffer extends Functions implements ScriptFile
{
	public void doBuffGroup(final String[] args)
	{
		final Player player = getSelf();
		if(!checkCondition(player))
			return;
		if(Config.PVPCB_BUFFER_PRICE_GRP > 0)
		{
			ItemInstance i = player.getInventory().getItemByItemId(Config.PVPCB_BUFFER_PRICE_ITEM);
			if(i == null || i.getCount() < Config.PVPCB_BUFFER_PRICE_GRP)
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
				return;
			}
			player.sendMessage(Config.PVPCB_BUFFER_PRICE_GRP +" " + i.getName()+" disappeared");
			player.getInventory().destroyItem(i,Config.PVPCB_BUFFER_PRICE_GRP, false);
		}
		final boolean sn = player.getServitor() != null && player.getVarBoolean("BuffSummon");
		for(final int[] buff : Config.GROUP_BUFFS)
			if(buff[2] == Integer.valueOf(args[0]))
				giveBuff(player, SkillTable.getInstance().getInfo(buff[0], buff[1]), sn, 0);
		show(HtmCache.getInstance().getHtml("scripts/services/buffs.htm", player), player);
	}

	public void doBuff(final String[] args)
	{
		final Player player = getSelf();
		if(!checkCondition(player))
			return;
		final int id = Integer.valueOf(args[0]);
		final int lvl = Integer.valueOf(args[1]);
		if(!Config.CB_BUFFS.containsKey(id) || Config.CB_BUFFS.get(id) != lvl)
		{
			ScriptFile._log.warn("Player " + player.getName() + " [" + player.getObjectId() + "] tried to use illegal buff: " + id);
			GmListTable.broadcastMessageToGMs("Player " + player.getName() + " [" + player.getObjectId() + "] tried to use illegal buff: " + id);
			player.sendMessage("\u0413\u041c \u0438\u043d\u0444\u043e\u0440\u043c\u0438\u0440\u043e\u0432\u0430\u043d. \u041e\u0436\u0438\u0434\u0430\u0439\u0442\u0435 \u0431\u0430\u043d\u0430.");
			return;
		}
		if(Config.PVPCB_BUFFER_PRICE_ONE > 0)
		{
			ItemInstance i = player.getInventory().getItemByItemId(Config.PVPCB_BUFFER_PRICE_ITEM);
			if(i == null || i.getCount() < Config.PVPCB_BUFFER_PRICE_ONE)
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
				return;
			}
			player.sendMessage(Config.PVPCB_BUFFER_PRICE_ONE +" " + i.getName()+" disappeared");
			player.getInventory().destroyItem(i,Config.PVPCB_BUFFER_PRICE_ONE, false);
		}
		giveBuff(player, SkillTable.getInstance().getInfo(id, lvl), player.getServitor() != null && player.getVarBoolean("BuffSummon"), 0);
		show(HtmCache.getInstance().getHtml("scripts/services/buffs.htm", player), player);
	}

	public void doHeal()
	{
		final Player player = getSelf();
		if(!checkCondition(player))
			return;
		final Servitor pet = player.getServitor();
		if(!player.getVarBoolean("BuffSummon") || pet == null)
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp(), false);
			player.setCurrentCp(player.getMaxCp());
		}
		else
			pet.setCurrentHpMp(pet.getMaxHp(), pet.getMaxMp(), false);
		show(HtmCache.getInstance().getHtml("scripts/services/buffs.htm", player), player);
	}

	public void doCancel()
	{
		final Player player = getSelf();
		if(!checkCondition(player))
			return;
		final Servitor pet = player.getServitor();
		if((!player.getVarBoolean("BuffSummon") || pet == null) && player.getAbnormalList().getEffectsBySkillId(4515) == null && player.getAbnormalList().getEffectsBySkillId(3632) == null)
			player.getAbnormalList().stop();
		else if(pet != null && pet.getAbnormalList().getEffectsBySkillId(4515) == null)
			pet.getAbnormalList().stopAll();
		show(HtmCache.getInstance().getHtml("scripts/services/buffs.htm", player), player);
	}

	private static boolean checkCondition(final Player player)
	{
		if(player == null)
			return false;
		if(!Config.ALLOW_PVPCB_BUFFER)
		{
			player.sendMessage(player.isLangRus() ? "\u0424\u0443\u043d\u043a\u0446\u0438\u044f \u0431\u0430\u0444\u0444\u0430 \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u0430." : "This function disabled.");
			return false;
		}
		if(player.isActionsDisabled() || player.isSitting() || player.isInDuel() || player.isInOlympiadMode() || player.isInVehicle() || player.isFlying() || player.inObserverMode())
			return false;
		if(player.isInCombat())
		{
			player.sendMessage(player.isLangRus() ? "\u0411\u0430\u0444\u0444 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d \u0432 \u0431\u043e\u044e." : "You can not use buff in combat.");
			return false;
		}
		if(player.getLevel() > Config.PVPCB_BUFFER_MAX_LVL || player.getLevel() < Config.PVPCB_BUFFER_MIN_LVL)
		{
			if(!player.isLangRus())
				player.sendMessage("Your level does not meet the requirements!");
			else
				player.sendMessage("\u0412\u0430\u0448 \u0443\u0440\u043e\u0432\u0435\u043d\u044c \u043d\u0435 \u0443\u0434\u043e\u0432\u043b\u0435\u0442\u0432\u043e\u0440\u044f\u0435\u0442 \u0443\u0441\u043b\u043e\u0432\u0438\u044f.");
			return false;
		}
		if(!Config.PVPCB_BUFFER_ALLOW_EVENT && player.getTeam() > 0)
		{
			if(!player.isLangRus())
				player.sendMessage("You can not use buff in event.");
			else
				player.sendMessage("\u0411\u0430\u0444\u0444 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d \u0432 \u044d\u0432\u0435\u043d\u0442\u0430\u0445.");
			return false;
		}
		if(!Config.PVPCB_BUFFER_ALLOW_SIEGE && player.getEvent(SiegeEvent.class) != null)
		{
			if(!player.isLangRus())
				player.sendMessage("You can not use buff in siege.");
			else
				player.sendMessage("\u0411\u0430\u0444\u0444 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d \u0432\u043e \u0432\u0440\u0435\u043c\u044f \u043e\u0441\u0430\u0434\u044b.");
			return false;
		}
		if(Config.PVPCB_BUFFER_PEACE && !player.isInZonePeace())
		{
			if(!player.isLangRus())
				player.sendMessage("Buff is available only in a peace zone.");
			else
				player.sendMessage("\u0411\u0430\u0444\u0444 \u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d \u0442\u043e\u043b\u044c\u043a\u043e \u0432 \u043c\u0438\u0440\u043d\u043e\u0439 \u0437\u043e\u043d\u0435.");
			return false;
		}
		if(!Config.PVPCB_BUFFER_ALLOW_PK && player.getKarma() > 0)
		{
			player.sendMessage(player.isLangRus() ? "\u0411\u0430\u0444\u0444 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d \u0432 \u043a\u0430\u0440\u043c\u0435." : "You can not use buff with karma.");
			return false;
		}
		if(Config.NO_BUFF_EPIC && player.isInZone(Zone.ZoneType.epic))
		{
			player.sendMessage(player.isLangRus() ? "\u0411\u0430\u0444\u0444 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d \u0432 \u044d\u043f\u0438\u043a \u043b\u043e\u043a\u0430\u0446\u0438\u0438." : "You can not use buff in epic zone.");
			return false;
		}
		return true;
	}

	public void SelectBuffs()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final String html = HtmCache.getInstance().getHtml("scripts/services/buffs.htm", player);
		show(html, player);
	}

	public void SelectBuffsDeluxe()
	{
		final Player player = getSelf();
		if(player == null)
			return;
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
		String html = HtmCache.getInstance().getHtml("scripts/services/buffsDeluxe.htm", player);
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
		String html = HtmCache.getInstance().getHtml("scripts/services/buffsDeluxe.htm", player);
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

	private void giveBuff(final Player player, final Skill skill, final boolean sn, final int i)
	{
		for(final EffectTemplate et : skill.getEffectTemplates())
		{
			final Env env = new Env(sn ? player.getServitor() : player, sn ? player.getServitor() : player, skill);
			final Abnormal effect = et.getEffect(env);
			if(Config.PVPCB_BUFFER_ALT_TIME > 0L && (effect.getCount() != 1 || effect.getPeriod() != 0L))
				effect.setPeriod(Config.PVPCB_BUFFER_ALT_TIME);
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
		ScriptFile._log.info("Loaded Service: Buffer");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
