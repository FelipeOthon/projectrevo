package commands.voiced;

import l2s.gameserver.Config;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.instances.HennaInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.HennaTable;
import l2s.gameserver.tables.HennaTreeTable;
import l2s.gameserver.tables.ItemTable;

public class olyHenna extends Functions implements IVoicedCommandHandler, ScriptFile
{
	private String[] _commandList;

	public olyHenna()
	{
		_commandList = new String[] { "olyhenna", "setolyhenna", "delolyhenna", "showolyhenna", "olyhennainfo" };
	}

	@Override
	public void onLoad()
	{
		if(Config.ALLOW_OLY_HENNA)
			VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	@Override
	public boolean useVoicedCommand(final String command, final Player player, final String args)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		if(command.equalsIgnoreCase("olyhenna"))
		{
			show(HtmCache.getInstance().getHtml("scripts/commands/voiced/olyHenna.htm", player), player);
			return true;
		}
		if(command.equalsIgnoreCase("setolyhenna"))
		{
			if(player.getLevel() < 76)
			{
				player.sendMessage("\u0414\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0442\u043e\u043b\u044c\u043a\u043e \u0441 76-\u0433\u043e \u0443\u0440\u043e\u0432\u043d\u044f.");
				return false;
			}
			if(player.isInOlympiadMode())
			{
				player.sendMessage("\u041d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0432 \u0440\u0435\u0436\u0438\u043c\u0435 \u041e\u043b\u0438\u043c\u043f\u0438\u0430\u0434\u044b.");
				return false;
			}
			final String he = player.getVar("OlyHenna");
			int count = he == null ? 0 : he.split(";").length;
			if(count >= 3)
			{
				player.sendMessage("\u0423 \u0412\u0430\u0441 \u0437\u0430\u043d\u044f\u0442\u044b \u0432\u0441\u0435 3 \u044f\u0447\u0435\u0439\u043a\u0438.");
				return false;
			}
			String save = "";
			for(final HennaInstance h : HennaTreeTable.getInstance().getAvailableHenna(ClassId.values()[player.getBaseClassId()]))
				if(h.getItemIdDye() < 9000 && player.getInventory().getItemByItemId(h.getItemIdDye()) != null)
				{
					if(count > 0)
						save += ";";
					save += String.valueOf(h.getSymbolId());
					++count;
					player.sendMessage("\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u043e: " + ItemTable.getInstance().getTemplate(h.getItemIdDye()).getName());
					if(count > 2)
						break;
				}
			if(!save.equals(""))
			{
				if(he != null)
					save = he + save;
				player.setVar("OlyHenna", save, -1L);
			}
			else
				player.sendMessage("\u0412 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435 \u043d\u0435\u0442 \u0434\u043e\u0441\u0442\u0443\u043f\u043d\u044b\u0445 \u043a\u0440\u0430\u0441\u043e\u043a.");
			return true;
		}
		else if(command.equalsIgnoreCase("delolyhenna"))
		{
			if(player.getLevel() < 76)
			{
				player.sendMessage("\u0414\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0442\u043e\u043b\u044c\u043a\u043e \u0441 76-\u0433\u043e \u0443\u0440\u043e\u0432\u043d\u044f.");
				return false;
			}
			if(player.isInOlympiadMode())
			{
				player.sendMessage("\u041d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0432 \u0440\u0435\u0436\u0438\u043c\u0435 \u041e\u043b\u0438\u043c\u043f\u0438\u0430\u0434\u044b.");
				return false;
			}
			if(player.getVar("OlyHenna") != null)
			{
				player.unsetVar("OlyHenna");
				player.sendMessage("\u0412\u0441\u0435 \u043a\u0440\u0430\u0441\u043a\u0438 \u0434\u043b\u044f \u041e\u043b\u0438\u043c\u043f\u0438\u0430\u0434\u044b \u0443\u0434\u0430\u043b\u0435\u043d\u044b.");
			}
			else
				player.sendMessage("\u0423 \u0412\u0430\u0441 \u043d\u0435\u0442 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u043d\u044b\u0445 \u043a\u0440\u0430\u0441\u043e\u043a.");
			return true;
		}
		else
		{
			if(command.equalsIgnoreCase("showolyhenna"))
			{
				if(player.getVar("OlyHenna") != null)
				{
					final String[] ids = player.getVar("OlyHenna").split(";");
					int c = 0;
					for(final String id : ids)
						if(!id.equals(""))
						{
							++c;
							player.sendMessage("\u041a\u0440\u0430\u0441\u043a\u0430 " + c + ": " + ItemTable.getInstance().getTemplate(HennaTable.getInstance().getTemplate(Integer.parseInt(id)).getDyeId()).getName());
						}
				}
				else
					player.sendMessage("\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u043d\u044b\u0445 \u043a\u0440\u0430\u0441\u043e\u043a.");
				return true;
			}
			if(command.equalsIgnoreCase("olyhennainfo"))
			{
				show(HtmCache.getInstance().getHtml("scripts/commands/voiced/olyHennaInfo.htm", player), player);
				return true;
			}
			return false;
		}
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
}
