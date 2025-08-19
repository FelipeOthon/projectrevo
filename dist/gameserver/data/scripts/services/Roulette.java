package services;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Stat;
import l2s.gameserver.utils.Util;

public class Roulette extends Functions implements ScriptFile
{
	private static final String R = "red";
	private static final String B = "black";
	private static final String fst = "first";
	private static final String snd = "second";
	private static final String trd = "third";
	private static final String E = "even";
	private static final String O = "odd";
	private static final String L = "low";
	private static final String H = "high";
	private static final String Z = "zero";
	private static final String[][] Numbers;

	public void dialog()
	{
		final Player player = getSelf();
		show(HtmCache.getInstance().getHtml("scripts/services/roulette.htm", player).replaceFirst("%min%", Util.formatAdena(Config.SERVICES_ROULETTE_MIN_BET)).replaceFirst("%max%", Util.formatAdena(Config.SERVICES_ROULETTE_MAX_BET)), player);
	}

	public void play(final String[] param)
	{
		if(!Config.SERVICES_ALLOW_ROULETTE)
			return;
		final Player player = getSelf();
		int bet = 0;
		String betID = "";
		GameType type;
		try
		{
			if(param.length != 3)
				throw new NumberFormatException();
			type = GameType.valueOf(param[0]);
			betID = param[1].trim();
			bet = Integer.parseInt(param[2]);
			if(type == GameType.StraightUp && (betID.length() > 2 || Integer.parseInt(betID) < 0 || Integer.parseInt(betID) > 36))
				throw new NumberFormatException();
		}
		catch(NumberFormatException e)
		{
			if(player.isLangRus())
				show("\u041d\u0435\u0432\u0435\u0440\u043d\u044b\u0439 \u0432\u0432\u043e\u0434 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u044f!<br><a action=\"bypass -h scripts_services.Roulette:dialog\">\u041d\u0430\u0437\u0430\u0434</a>", player);
			else
				show("Invalid value input!<br><a action=\"bypass -h scripts_services.Roulette:dialog\">Back</a>", player);
			return;
		}
		if(bet < Config.SERVICES_ROULETTE_MIN_BET)
		{
			if(player.isLangRus())
				show("\u0421\u043b\u0438\u0448\u043a\u043e\u043c \u043c\u0430\u043b\u0435\u043d\u044c\u043a\u0430\u044f \u0441\u0442\u0430\u0432\u043a\u0430!<br><a action=\"bypass -h scripts_services.Roulette:dialog\">\u041d\u0430\u0437\u0430\u0434</a>", player);
			else
				show("Too small bet!<br><a action=\"bypass -h scripts_services.Roulette:dialog\">Back</a>", player);
			return;
		}
		if(bet > Config.SERVICES_ROULETTE_MAX_BET)
		{
			if(player.isLangRus())
				show("\u0421\u043b\u0438\u0448\u043a\u043e\u043c \u0431\u043e\u043b\u044c\u0448\u0430\u044f \u0441\u0442\u0430\u0432\u043a\u0430!<br><a action=\"bypass -h scripts_services.Roulette:dialog\">\u041d\u0430\u0437\u0430\u0434</a>", player);
			else
				show("Too large bet!<br><a action=\"bypass -h scripts_services.Roulette:dialog\">Back</a>", player);
			return;
		}
		if(player.getAdena() < bet)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			if(player.isLangRus())
				show("\u041d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u0430\u0434\u0435\u043d\u044b!<br><a action=\"bypass -h scripts_services.Roulette:dialog\">\u041d\u0430\u0437\u0430\u0434</a>", player);
			else
				show("You do not have enough adena!<br><a action=\"bypass -h scripts_services.Roulette:dialog\">Back</a>", player);
			return;
		}
		final String[] roll = Roulette.Numbers[Rnd.get(Roulette.Numbers.length)];
		final int result = check(betID, roll, type);
		String ret = HtmCache.getInstance().getHtml("scripts/services/rouletteresult.htm", player);
		if(result == 0)
		{
			removeItem(player, 57, bet);
			Stat.addRoulette(bet);
			if(player.isLangRus())
				ret = ret.replace("%result%", "<font color=\"FF0000\">\u041d\u0435\u0443\u0434\u0430\u0447\u0430!</font>");
			else
				ret = ret.replace("%result%", "<font color=\"FF0000\">Fail!</font>");
		}
		else
		{
			addItem(player, 57, bet * result);
			Stat.addRoulette(-1 * bet * result);
			if(player.isLangRus())
				ret = ret.replace("%result%", "<font color=\"00FF00\">\u0423\u0441\u043f\u0435\u0445!</font>");
			else
				ret = ret.replace("%result%", "<font color=\"00FF00\">Success!</font>");
		}
		if(player.isGM())
			player.sendMessage("Roulette balance: " + Util.formatAdena(Stat.getRouletteSum()));
		ret = ret.replace("%bettype%", new CustomMessage("Roulette." + type.toString()).toString(player));
		ret = ret.replace("%betnumber%", type == GameType.StraightUp ? betID : new CustomMessage("Roulette." + betID).toString(player));
		ret = ret.replace("%number%", roll[0]);
		ret = ret.replace("%color%", new CustomMessage("Roulette." + roll[1]).toString(player));
		ret = ret.replace("%evenness%", new CustomMessage("Roulette." + roll[4]).toString(player));
		ret = ret.replace("%column%", new CustomMessage("Roulette." + roll[3]).toString(player));
		ret = ret.replace("%dozen%", new CustomMessage("Roulette." + roll[2]).toString(player));
		ret = ret.replace("%highness%", new CustomMessage("Roulette." + roll[5]).toString(player));
		ret = ret.replace("%param%", param[0] + " " + param[1] + " " + param[2]);
		show(ret, player);
	}

	private static final int check(final String betID, final String[] roll, final GameType type)
	{
		switch(type)
		{
			case StraightUp:
			{
				if(betID.equals(roll[0]))
					return 35;
				return 0;
			}
			case ColumnBet:
			{
				if(betID.equals(roll[3]))
					return 2;
				return 0;
			}
			case DozenBet:
			{
				if(betID.equals(roll[2]))
					return 2;
				return 0;
			}
			case RedOrBlack:
			{
				if(betID.equals(roll[1]))
					return 1;
				return 0;
			}
			case EvenOrOdd:
			{
				if(betID.equals(roll[4]))
					return 1;
				return 0;
			}
			case LowOrHigh:
			{
				if(betID.equals(roll[5]))
					return 1;
				return 0;
			}
			default:
			{
				return 0;
			}
		}
	}

	public String DialogAppend_30990(final Integer val)
	{
		return getHtmlAppends(getSelf(), val);
	}

	public String DialogAppend_30991(final Integer val)
	{
		return getHtmlAppends(getSelf(), val);
	}

	public String DialogAppend_30992(final Integer val)
	{
		return getHtmlAppends(getSelf(), val);
	}

	public String DialogAppend_30993(final Integer val)
	{
		return getHtmlAppends(getSelf(), val);
	}

	public String DialogAppend_30994(final Integer val)
	{
		return getHtmlAppends(getSelf(), val);
	}

	public String getHtmlAppends(Player player, final Integer val)
	{
		if(Config.SERVICES_ALLOW_ROULETTE)
			return "<br><a action=\"bypass -h scripts_services.Roulette:dialog\">" + new CustomMessage("Roulette.dialog").toString(player) + "</a>";
		return "";
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Roulette");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		Numbers = new String[][] {
				{ "0", "zero", "zero", "zero", "zero", "zero" },
				{ "1", "red", "first", "first", "odd", "low" },
				{ "2", "black", "first", "second", "even", "low" },
				{ "3", "red", "first", "third", "odd", "low" },
				{ "4", "black", "first", "first", "even", "low" },
				{ "5", "red", "first", "second", "odd", "low" },
				{ "6", "black", "first", "third", "even", "low" },
				{ "7", "red", "first", "first", "odd", "low" },
				{ "8", "black", "first", "second", "even", "low" },
				{ "9", "red", "first", "third", "odd", "low" },
				{ "10", "black", "first", "first", "even", "low" },
				{ "11", "black", "first", "second", "odd", "low" },
				{ "12", "red", "first", "third", "even", "low" },
				{ "13", "black", "second", "first", "odd", "low" },
				{ "14", "red", "second", "second", "even", "low" },
				{ "15", "black", "second", "third", "odd", "low" },
				{ "16", "red", "second", "first", "even", "low" },
				{ "17", "black", "second", "second", "odd", "low" },
				{ "18", "red", "second", "third", "even", "low" },
				{ "19", "red", "second", "first", "odd", "high" },
				{ "20", "black", "second", "second", "even", "high" },
				{ "21", "red", "second", "third", "odd", "high" },
				{ "22", "black", "second", "first", "even", "high" },
				{ "23", "red", "second", "second", "odd", "high" },
				{ "24", "black", "second", "third", "even", "high" },
				{ "25", "red", "third", "first", "odd", "high" },
				{ "26", "black", "third", "second", "even", "high" },
				{ "27", "red", "third", "third", "odd", "high" },
				{ "28", "black", "third", "first", "even", "high" },
				{ "29", "black", "third", "second", "odd", "high" },
				{ "30", "red", "third", "third", "even", "high" },
				{ "31", "black", "third", "first", "odd", "high" },
				{ "32", "red", "third", "second", "even", "high" },
				{ "33", "black", "third", "third", "odd", "high" },
				{ "34", "red", "third", "first", "even", "high" },
				{ "35", "black", "third", "second", "odd", "high" },
				{ "36", "red", "third", "third", "even", "high" } };
	}

	private enum GameType
	{
		StraightUp,
		ColumnBet,
		DozenBet,
		RedOrBlack,
		EvenOrOdd,
		LowOrHigh;
	}
}
