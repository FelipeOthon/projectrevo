package services;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class Pushkin extends Functions implements ScriptFile
{
	public String DialogAppend_30300(final Integer val)
	{
		if(val != 0 || !Config.ALT_SIMPLE_SIGNS)
			return "";
		final Player player = getSelf();
		if(player == null)
			return "";
		final StringBuilder append = new StringBuilder();
		if(player.isLangRus())
		{
			append.append("<br>*\u041e\u043f\u0446\u0438\u0438 \u0441\u0435\u043c\u0438 \u043f\u0435\u0447\u0430\u0442\u0435\u0439:*<br>");
			append.append("[npc_%objectId%_Multisell 10061|\u0421\u0434\u0435\u043b\u0430\u0442\u044c S-\u0433\u0440\u0435\u0439\u0434 \u043c\u0435\u0447]<br1>");
			append.append("[npc_%objectId%_Multisell 40011|\u0412\u0441\u0442\u0430\u0432\u0438\u0442\u044c SA \u0432 \u043e\u0440\u0443\u0436\u0438\u0435 S-\u0433\u0440\u0435\u0439\u0434\u0430]<br1>");
			append.append("[npc_%objectId%_Multisell 1008|\u0420\u0430\u0441\u043f\u0435\u0447\u0430\u0442\u0430\u0442\u044c \u0431\u0440\u043e\u043d\u044e S-\u0433\u0440\u0435\u0439\u0434\u0430]<br1>");
			append.append("[npc_%objectId%_Multisell 10091|\u0420\u0430\u0441\u043f\u0435\u0447\u0430\u0442\u0430\u0442\u044c \u0431\u0438\u0436\u0443\u0442\u0435\u0440\u0438\u044e S-\u0433\u0440\u0435\u0439\u0434\u0430]<br1>");
			append.append("[npc_%objectId%_Multisell 1006|\u0421\u0434\u0435\u043b\u0430\u0442\u044c A-\u0433\u0440\u0435\u0439\u0434 \u043c\u0435\u0447]<br1>");
			append.append("[npc_%objectId%_Multisell 4001|\u0412\u0441\u0442\u0430\u0432\u0438\u0442\u044c SA \u0432 \u043e\u0440\u0443\u0436\u0438\u0435 A-\u0433\u0440\u0435\u0439\u0434\u0430]<br1>");
			append.append("[npc_%objectId%_Multisell 1005|\u0420\u0430\u0441\u043f\u0435\u0447\u0430\u0442\u0430\u0442\u044c \u0431\u0440\u043e\u043d\u044e A-\u0433\u0440\u0435\u0439\u0434\u0430]<br1>");
			append.append("[npc_%objectId%_Multisell 1009|\u0420\u0430\u0441\u043f\u0435\u0447\u0430\u0442\u0430\u0442\u044c \u0431\u0438\u0436\u0443\u0442\u0435\u0440\u0438\u044e A-\u0433\u0440\u0435\u0439\u0434\u0430]<br1>");
			append.append("[npc_%objectId%_Multisell 1007|\u0417\u0430\u043f\u0435\u0447\u0430\u0442\u0430\u0442\u044c \u0431\u0440\u043e\u043d\u044e A-\u0433\u0440\u0435\u0439\u0434\u0430]<br1>");
			append.append("[npc_%objectId%_Multisell 4002|\u0423\u0434\u0430\u043b\u0438\u0442\u044c SA \u0438\u0437 \u043e\u0440\u0443\u0436\u0438\u044f]<br1>");
			append.append("[npc_%objectId%_Multisell 9998|\u041e\u0431\u043c\u0435\u043d\u044f\u0442\u044c \u043e\u0440\u0443\u0436\u0438\u0435 \u0441 \u0434\u043e\u043f\u043b\u0430\u0442\u043e\u0439]<br1>");
			append.append("[npc_%objectId%_Multisell 9999|\u041e\u0431\u043c\u0435\u043d\u044f\u0442\u044c \u043e\u0440\u0443\u0436\u0438\u0435 \u043d\u0430 \u0440\u0430\u0432\u043d\u043e\u0446\u0435\u043d\u043d\u043e\u0435]<br1>");
			append.append("[npc_%objectId%_Multisell 501|\u041a\u0443\u043f\u0438\u0442\u044c \u0447\u0442\u043e-\u043d\u0438\u0431\u0443\u0434\u044c]<br1>");
			append.append("[npc_%objectId%_Multisell 400|\u041e\u0431\u043c\u0435\u043d\u044f\u0442\u044c \u043a\u0430\u043c\u043d\u0438]<br1>");
			append.append("[npc_%objectId%_Multisell 500|\u041f\u0440\u0438\u043e\u0431\u0440\u0435\u0441\u0442\u0438 \u0440\u0430\u0441\u0445\u043e\u0434\u043d\u044b\u0435 \u043c\u0430\u0442\u0435\u0440\u0438\u0430\u043b\u044b]");
		}
		else
		{
			append.append("<br>*Seven Signs options:*<br>");
			append.append("[npc_%objectId%_Multisell 10061|Manufacture an S-grade sword]<br1>");
			append.append("[npc_%objectId%_Multisell 40011|Bestow the special S-grade weapon some abilities]<br1>");
			append.append("[npc_%objectId%_Multisell 1008|Release the S-grade armor seal]<br1>");
			append.append("[npc_%objectId%_Multisell 10091|Release the S-grade accessory seal]<br1>");
			append.append("[npc_%objectId%_Multisell 1006|Manufacture an A-grade sword]<br1>");
			append.append("[npc_%objectId%_Multisell 4001|Bestow the special A-grade weapon some abilities]<br1>");
			append.append("[npc_%objectId%_Multisell 1005|Release the A-grade armor seal]<br1>");
			append.append("[npc_%objectId%_Multisell 1009|Release the A-grade accessory seal]<br1>");
			append.append("[npc_%objectId%_Multisell 1007|Seal the A-grade armor again]<br1>");
			append.append("[npc_%objectId%_Multisell 4002|Remove the special abilities from a weapon]<br1>");
			append.append("[npc_%objectId%_Multisell 9998|Upgrade weapon]<br1>");
			append.append("[npc_%objectId%_Multisell 9999|Make an even exchange of weapons]<br1>");
			append.append("[npc_%objectId%_Multisell 501|Buy Something]<br1>");
			append.append("[npc_%objectId%_Multisell 400|Exchange Seal Stones]<br1>");
			append.append("[npc_%objectId%_Multisell 500|Purchase consumable items]");
		}
		return append.toString();
	}

	public String DialogAppend_30086(final Integer val)
	{
		return DialogAppend_30300(val);
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Pushkin");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
