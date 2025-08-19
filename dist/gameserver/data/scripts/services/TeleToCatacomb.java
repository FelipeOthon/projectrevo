package services;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class TeleToCatacomb extends Functions implements ScriptFile
{
	public String DialogAppend_31212(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31213(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31214(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31215(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31216(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31217(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31218(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31219(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31220(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31221(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31222(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31223(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31224(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31767(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_31768(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String DialogAppend_32048(final Integer val)
	{
		return getHtmlAppends(val);
	}

	public String getHtmlAppends(final Integer val)
	{
		if(val != 0 || !Config.ALT_SIMPLE_SIGNS)
			return "";
		final Player player = getSelf();
		if(player == null)
			return "";
		String append = "";
		append += "<br>";
		if(!player.isLangRus())
		{
			append += "Teleport to catacomb or necropolis.<br1> ";
			append += "You may teleport to any of the following hunting locations. Each teleport requires 100000 adena.<br>";
		}
		else
		{
			append += "\u0417\u0430 100000 \u0430\u0434\u0435\u043d \u0432\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u043f\u0435\u0440\u0435\u043c\u0435\u0441\u0442\u0438\u0442\u044c\u0441\u044f \u0432 \u043a\u0430\u0442\u0430\u043a\u043e\u043c\u0431\u044b \u0438\u043b\u0438 \u043d\u0435\u043a\u0440\u043e\u043f\u043e\u043b\u0438\u0441\u044b.<br1> ";
			append += "\u0421\u043f\u0438\u0441\u043e\u043a \u0434\u043e\u0441\u0442\u0443\u043f\u043d\u044b\u0445 \u043b\u043e\u043a\u0430\u0446\u0438\u0439:<br>";
		}
		append += "[scripts_Util:SGK -41567 209463 -5080 100000|Necropolis of Sacrifice (20-30)]<br1>";
		append += "[scripts_Util:SGK 45248 124223 -5408 100000|The Pilgrim's Necropolis (30-40)]<br1>";
		append += "[scripts_Util:SGK 110911 174013 -5439 100000|Necropolis of Worship (40-50)]<br1>";
		append += "[scripts_Util:SGK -22101 77383 -5173 100000|The Patriot's Necropolis (50-60)]<br1>";
		append += "[scripts_Util:SGK -52654 79149 -4741 100000|Necropolis of Devotion (60-70)]<br1>";
		append += "[scripts_Util:SGK 117884 132796 -4831 100000|Necropolis of Martyrdom (60-70)]<br1>";
		append += "[scripts_Util:SGK 82750 209250 -5401 100000|The Saint's Necropolis (70-80)]<br1>";
		append += "[scripts_Util:SGK 171897 -17606 -4901 100000|Disciples Necropolis(70-80)]<br>";
		append += "[scripts_Util:SGK 42322 143927 -5381 100000|Catacomb of the Heretic (30-40)]<br1>";
		append += "[scripts_Util:SGK 45841 170307 -4981 100000|Catacomb of the Branded (40-50)]<br1>";
		append += "[scripts_Util:SGK 77348 78445 -5125 100000|Catacomb of the Apostate (50-60)]<br1>";
		append += "[scripts_Util:SGK 139955 79693 -5429 100000|Catacomb of the Witch (60-70)]<br1>";
		append += "[scripts_Util:SGK -19827 13509 -4901 100000|Catacomb of Dark Omens (70-80)]<br1>";
		append += "[scripts_Util:SGK 113573 84513 -6541 100000|Catacomb of the Forbidden Path (70-80)]";
		return append;
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Teleport to catacombs");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
