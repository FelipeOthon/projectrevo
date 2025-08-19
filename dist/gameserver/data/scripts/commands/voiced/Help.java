package commands.voiced;

import org.apache.commons.lang3.tuple.Pair;

import l2s.gameserver.Config;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.listener.actor.player.OnAnswerListener;
import l2s.gameserver.listener.actor.player.impl.ReviveAnswerListener;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.TradeList;
import l2s.gameserver.model.entity.SevenSigns;
import l2s.gameserver.network.l2.s2c.ConfirmDlg;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.TimeUtils;
import services.RateBonus;

public class Help extends Functions implements IVoicedCommandHandler, ScriptFile
{
	private static final String[] _commandList;

	@Override
	public boolean useVoicedCommand(String command, final Player activeChar, final String args)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		command = command.intern();
		if(command.equalsIgnoreCase("help"))
		{
			show(HtmCache.getInstance().getHtml("scripts/commands/voiced/help.htm", activeChar), activeChar);
			return true;
		}
		if(command.equalsIgnoreCase("heading"))
		{
			activeChar.sendMessage(String.valueOf(activeChar.getHeading()));
			return true;
		}
		if(command.equalsIgnoreCase("fake"))
		{
			activeChar.sendActionFailed();
			return true;
		}
		if(command.equalsIgnoreCase("ct"))
		{
			if(activeChar.isInStoreMode())
				TradeList.cancelStore(activeChar);
			return true;
		}
		if(command.equalsIgnoreCase("ss"))
		{
			if(!Config.ALLOW_SEVEN_SIGNS)
				return false;

			if(activeChar.getTarget() != null && activeChar.getTarget().isPlayer())
			{
				final Player target = (Player) activeChar.getTarget();
				if(SevenSigns.getInstance().getPlayerCabal(target) == 2)
					activeChar.sendMessage(target.getName() + ": Dawn");
				else if(SevenSigns.getInstance().getPlayerCabal(target) == 1)
					activeChar.sendMessage(target.getName() + ": Dusk");
				else
					activeChar.sendMessage(target.getName() + ": none");
			}
			return true;
		}
		if(command.equalsIgnoreCase("nn"))
		{
			final GameObject target2 = activeChar.getTarget();
			if(target2 == null)
				return true;
			if(target2.isNpc() || target2.isSummon())
				activeChar.sendMessage("NpcId: " + target2.getNpcId());
			else if(activeChar.isGM() && target2.isPlayer())
				activeChar.sendMessage(target2.getName() + " objId: " + target2.getObjectId());
			return true;
		}
		else
		{
			if(command.equalsIgnoreCase("res"))
			{
				final Pair<Integer, OnAnswerListener> ask = activeChar.getAskListener(false);
				final ReviveAnswerListener reviveAsk = ask != null && ask.getValue() instanceof ReviveAnswerListener ? (ReviveAnswerListener) ask.getValue() : null;
				if(reviveAsk != null)
				{
					final ConfirmDlg pkt = new ConfirmDlg(1510, 0);
					pkt.addString(reviveAsk.getRevName());
					pkt.setRequestId(ask.getKey());
					activeChar.sendPacket(pkt);
				}
				else
					activeChar.sendMessage("No resurrection request.");
				return true;
			}
			if(command.equalsIgnoreCase("tvt"))
			{
				show(HtmCache.getInstance().getHtml("scripts/events/TvT/31225.html", activeChar), activeChar);
				return true;
			}
			if(command.equalsIgnoreCase("ctf"))
			{
				show(HtmCache.getInstance().getHtml("scripts/events/CtF/31225.html", activeChar), activeChar);
				return true;
			}
			if(command.equalsIgnoreCase("lh"))
			{
				show(HtmCache.getInstance().getHtml("scripts/events/lastHero/31225.html", activeChar), activeChar);
				return true;
			}
			if(command.equalsIgnoreCase("premium"))
			{
				if(Config.SERVICES_RATE_TYPE < 1)
					return true;
				if(activeChar.isPremium())
				{
					final long endtime = activeChar.getNetConnection().getBonusExpire() * 1000L;
					if(endtime > System.currentTimeMillis())
					{
						activeChar.sendMessage((activeChar.isLangRus() ? "\u0411\u043e\u043d\u0443\u0441" : "Bonus") + ": x" + activeChar.getNetConnection().getBonus());
						activeChar.sendMessage((activeChar.isLangRus() ? "\u041e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u0435 \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u0430" : "Over of premium account") + ": " + TimeUtils.toSimpleFormat(endtime));
						return true;
					}
				}
				activeChar.sendMessage(activeChar.isLangRus() ? "\u0423 \u0412\u0430\u0441 \u043d\u0435\u0442 \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u0430." : "You don't have premium account.");
				return true;
			}
			else if(command.equalsIgnoreCase("premiumbuff"))
			{
				if(!Config.ALLOW_PB_COMMAND)
					return true;
				final String v = activeChar.getVar("PremiumBuff");
				if(v == null)
				{
					if(!activeChar.isLangRus())
						activeChar.sendMessage("You don't have premium access for buff.");
					else
						activeChar.sendMessage("\u0423 \u0412\u0430\u0441 \u043d\u0435\u0442 \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f\u0430 \u043a \u0431\u0430\u0444\u0444\u0443.");
					listPB(activeChar);
					return true;
				}
				if(Long.parseLong(v) <= System.currentTimeMillis())
				{
					if(!activeChar.isLangRus())
						activeChar.sendMessage("Your premium access for buff is over.");
					else
						activeChar.sendMessage("\u0412\u0430\u0448 \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f \u043a \u0431\u0430\u0444\u0444\u0443 \u0437\u0430\u043a\u043e\u043d\u0447\u0438\u043b\u0441\u044f.");
					activeChar.unsetVar("PremiumBuff");
					listPB(activeChar);
					return true;
				}
				if(!activeChar.isLangRus())
					activeChar.sendMessage("Over of premium access for buff: " + TimeUtils.toSimpleFormat(Long.parseLong(v)));
				else
					activeChar.sendMessage("\u041e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u0435 \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f\u0430 \u043a \u0431\u0430\u0444\u0444\u0443: " + TimeUtils.toSimpleFormat(Long.parseLong(v)));
				return true;
			}
			else
			{
				if(command.equalsIgnoreCase("jail"))
				{
					final int time = activeChar.getVarInt("jailed", 0);
					if(System.currentTimeMillis() / 1000L < time)
						activeChar.sendMessage((activeChar.isLangRus() ? "\u041e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u0435 \u0441\u0440\u043e\u043a\u0430: " : "The end of the prison term: ") + TimeUtils.toSimpleFormat(time * 1000L));
					else
						activeChar.sendMessage(activeChar.isLangRus() ? "\u0412\u044b \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0435\u0441\u044c \u0437\u0430\u043a\u043b\u044e\u0447\u0435\u043d\u043d\u044b\u043c." : "You are not a prisoner.");
					return true;
				}
				if(command.equalsIgnoreCase("esbonus"))
				{
					if(!Config.ALLOW_ES_BONUS)
						return true;
					final String v = activeChar.getVar("BonusES");
					if(v == null)
					{
						activeChar.sendMessage(activeChar.isLangRus() ? "\u0423 \u0412\u0430\u0441 \u043d\u0435\u0442 \u0431\u043e\u043d\u0443\u0441\u0430 \u0437\u0430\u0442\u043e\u0447\u043a\u0438 \u0441\u043a\u0438\u043b\u043e\u0432." : "You don't have enchant skill bonus.");
						listESB(activeChar);
						return true;
					}
					if(Long.parseLong(v) <= System.currentTimeMillis())
					{
						activeChar.sendMessage(activeChar.isLangRus() ? "\u0412\u0430\u0448 \u0431\u043e\u043d\u0443\u0441 \u0437\u0430\u0442\u043e\u0447\u043a\u0438 \u0441\u043a\u0438\u043b\u043e\u0432 \u0437\u0430\u043a\u043e\u043d\u0447\u0438\u043b\u0441\u044f." : "Your enchant skill bonus is over.");
						activeChar.unsetVar("BonusES");
						listESB(activeChar);
						return true;
					}
					if(!activeChar.isLangRus())
						activeChar.sendMessage("Over of enchant skill bonus: " + TimeUtils.toSimpleFormat(Long.parseLong(v)));
					else
						activeChar.sendMessage("\u041e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u0435 \u0431\u043e\u043d\u0443\u0441\u0430 \u0437\u0430\u0442\u043e\u0447\u043a\u0438 \u0441\u043a\u0438\u043b\u043e\u0432: " + TimeUtils.toSimpleFormat(Long.parseLong(v)));
					return true;
				}
				else if(command.equalsIgnoreCase("herostatus"))
				{
					if(!Config.SERVICES_HERO_STATUS_ENABLE)
						return true;
					final String v = activeChar.getVar("HeroStatus");
					if(v == null)
					{
						if(!activeChar.isLangRus())
							activeChar.sendMessage("You don't have Hero status.");
						else
							activeChar.sendMessage("\u0423 \u0412\u0430\u0441 \u043d\u0435\u0442 \u0441\u0442\u0430\u0442\u0443\u0441\u0430 \u0413\u0435\u0440\u043e\u044f.");
						listHS(activeChar);
						return true;
					}
					if(Long.parseLong(v) <= System.currentTimeMillis())
					{
						if(!activeChar.isLangRus())
							activeChar.sendMessage("Your Hero status is over.");
						else
							activeChar.sendMessage("\u0412\u0430\u0448 \u0441\u0442\u0430\u0442\u0443\u0441 \u0413\u0435\u0440\u043e\u044f \u0437\u0430\u043a\u043e\u043d\u0447\u0438\u043b\u0441\u044f.");
						activeChar.unsetVar("HeroStatus");
						listHS(activeChar);
						return true;
					}
					if(!activeChar.isLangRus())
						activeChar.sendMessage("Over of Hero status: " + TimeUtils.toSimpleFormat(Long.parseLong(v)));
					else
						activeChar.sendMessage("\u041e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u0435 \u0441\u0442\u0430\u0442\u0443\u0441\u0430 \u0413\u0435\u0440\u043e\u044f: " + TimeUtils.toSimpleFormat(Long.parseLong(v)));
					return true;
				}
				else
				{
					if(!command.equalsIgnoreCase("heroaura"))
						return false;
					if(!Config.SERVICES_HERO_AURA)
					{
						activeChar.sendMessage(activeChar.isLangRus() ? "Сервис отключен." : "Service disabled.");
						return true;
					}
					if(activeChar.noHeroAura)
					{
						activeChar.unsetVar("NoHeroAura");
						activeChar.noHeroAura = false;
						activeChar.sendMessage(activeChar.isLangRus() ? "\u0410\u0443\u0440\u0430 \u0413\u0435\u0440\u043e\u044f \u0430\u043a\u0442\u0438\u0432\u0438\u0440\u043e\u0432\u0430\u043d\u0430." : "Hero aura enabled.");
						activeChar.broadcastUserInfo(true);
						return true;
					}
					activeChar.setVar("NoHeroAura", "1");
					activeChar.noHeroAura = true;
					activeChar.sendMessage(activeChar.isLangRus() ? "\u0410\u0443\u0440\u0430 \u0413\u0435\u0440\u043e\u044f \u0434\u0435\u0437\u0430\u043a\u0442\u0438\u0432\u0438\u0440\u043e\u0432\u0430\u043d\u0430." : "Hero aura disabled.");
					activeChar.broadcastUserInfo(true);
					return true;
				}
			}
		}
	}

	private static void listPB(final Player player)
	{
		if(player == null)
			return;
		show("<center><br><br><br><br><br><button value=\"" + (player.isLangRus() ? "\u041a\u0443\u043f\u0438\u0442\u044c \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f" : "Buy premium access") + "\" action=\"bypass -h scripts_services.RateBonus:listPB\" width=135 height=23 back=\"L2UI_CH3.bigbutton3_down\" fore=\"L2UI_CH3.bigbutton3\">", player);
	}

	private static void listESB(final Player player)
	{
		if(player == null)
			return;
		show("<center><br><br><br><br><br><button value=\"" + (player.isLangRus() ? "\u041a\u0443\u043f\u0438\u0442\u044c \u0431\u043e\u043d\u0443\u0441 ES" : "Buy ES bonus") + "\" action=\"bypass -h scripts_services.RateBonus:listESB\" width=135 height=23 back=\"L2UI_CH3.bigbutton3_down\" fore=\"L2UI_CH3.bigbutton3\">", player);
	}

	private static void listHS(final Player player)
	{
		if(player == null)
			return;
		show("<center><br><br><br><br><br><button value=\"" + (player.isLangRus() ? "\u041a\u0443\u043f\u0438\u0442\u044c \u0441\u0442\u0430\u0442\u0443\u0441 \u0413\u0443\u0440\u043e\u044f" : "Buy Hero status") + "\" action=\"bypass -h scripts_services.HeroStatus:listHS\" width=135 height=23 back=\"L2UI_CH3.bigbutton3_down\" fore=\"L2UI_CH3.bigbutton3\">", player);
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return Help._commandList;
	}

	@Override
	public void onLoad()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		_commandList = new String[] {
				"help",
				"heading",
				"fake",
				"ct",
				"ss",
				"nn",
				"res",
				"tvt",
				"ctf",
				"lh",
				"premium",
				"premiumbuff",
				"jail",
				"esbonus",
				"herostatus",
				"heroaura" };
	}
}
