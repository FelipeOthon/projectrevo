package commands.voiced;

import java.util.Map;

import l2s.gameserver.Config;
import l2s.gameserver.database.mysql;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class Reloc extends Functions implements IVoicedCommandHandler, ScriptFile
{
	private final String[] _commandList;

	public Reloc()
	{
		_commandList = new String[] { "reloc" };
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String target)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		show("scripts/commands/voiced/reloc.htm", activeChar);
		return true;
	}

	public void reloc(final String[] var)
	{
		if(var.length == 1)
		{
			final Player activeChar = getSelf();
			if(activeChar == null)
				return;
			final String name = var[0];
			if(activeChar.getName().equalsIgnoreCase(name))
			{
				show("<br>" + (activeChar.isLangRus() ? "\u041d\u0435\u043b\u044c\u0437\u044f \u043f\u0435\u0440\u0435\u043c\u0435\u0441\u0442\u0438\u0442\u044c \u0441\u0435\u0431\u044f!" : "You can't relocate yourself!"), activeChar);
				return;
			}
			if(!activeChar.getAccountChars().containsValue(name))
			{
				show("<br>" + (activeChar.isLangRus() ? "\u0412\u044b \u043d\u0435 \u043c\u043e\u0436\u0435\u0442\u0435 \u043f\u0435\u0440\u0435\u043c\u0435\u0441\u0442\u0438\u0442\u044c \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 \u0441 \u0434\u0440\u0443\u0433\u043e\u0433\u043e \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u0430!" : "You can't relocate character not on same account!"), activeChar);
				return;
			}
			if(GameObjectsStorage.getPlayer(name) != null)
			{
				show("<br>" + (activeChar.isLangRus() ? "\u041d\u0435\u043b\u044c\u0437\u044f \u043f\u0435\u0440\u0435\u043c\u0435\u0441\u0442\u0438\u0442\u044c \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 \u043d\u0430\u0445\u043e\u0434\u044f\u0449\u0435\u0433\u043e\u0441\u044f \u0432 \u0438\u0433\u0440\u0435!" : "You can't relocate character in game!"), activeChar);
				return;
			}
			for(final Map.Entry<Integer, String> entry : activeChar.getAccountChars().entrySet())
			{
				final int obj_id = entry.getKey();
				final String char_name = entry.getValue();
				if(!name.equalsIgnoreCase(char_name))
					continue;
				final int karma = mysql.simple_get_int("karma", "characters", "`obj_Id`=" + obj_id);
				if(karma > 0)
					mysql.set("UPDATE `characters` SET `x`='17144', `y`='170156', `z`='-3502', `heading`='0' WHERE `obj_Id`='" + obj_id + "' LIMIT 1");
				else
					mysql.set("UPDATE `characters` SET `x`='-71338', `y`='258271', `z`='-3104', `heading`='0' WHERE `obj_Id`='" + obj_id + "' LIMIT 1");
				mysql.set("DELETE FROM `character_variables` WHERE `obj_id`='" + obj_id + "' AND `type`='user-var' AND `name`='LastHero_backCoords' LIMIT 1");
				mysql.set("DELETE FROM `character_variables` WHERE `obj_id`='" + obj_id + "' AND `type`='user-var' AND `name`='TvT_backCoords' LIMIT 1");
				mysql.set("DELETE FROM `character_variables` WHERE `obj_id`='" + obj_id + "' AND `type`='user-var' AND `name`='CtF_backCoords' LIMIT 1");
				mysql.set("DELETE FROM `character_variables` WHERE `obj_id`='" + obj_id + "' AND `type`='user-var' AND `name`='Tournament_backCoords' LIMIT 1");
				mysql.set("DELETE FROM `character_variables` WHERE `obj_id`='" + obj_id + "' AND `type`='user-var' AND `name`='backCoords' LIMIT 1");
				show("<br>" + (activeChar.isLangRus() ? "\u041f\u0435\u0440\u0441\u043e\u043d\u0430\u0436 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0435\u0440\u0435\u043c\u0435\u0449\u0435\u043d." : "Character sucessfully relocated."), activeChar);
				break;
			}
		}
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
}
