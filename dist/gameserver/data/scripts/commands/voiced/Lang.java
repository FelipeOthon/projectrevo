package commands.voiced;

import org.apache.commons.lang3.math.NumberUtils;

import l2s.gameserver.Config;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.PrintfFormat;

public class Lang extends Functions implements IVoicedCommandHandler, ScriptFile
{
	private String[] _commandList;
	public static final PrintfFormat menu_row;
	public static final PrintfFormat menu_button;

	public Lang()
	{
		_commandList = new String[] { Config.COMMAND_LANG, "cfg", "lang", "menu"};
	}

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String args)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		else if((command.equals("cfg") || command.equals("menu")|| command.equals("lang")) && args != null)
		{

			String[] param = args.split(" ");
			if(param.length > 0)
			{
				if(param.length == 1){
					if(param[0].equalsIgnoreCase("en"))
						activeChar.setLanguage("en");
					else if(param[0].equalsIgnoreCase("ru"))
						activeChar.setLanguage("ru");
				}
			if(param.length == 2){
				if(param[0].equalsIgnoreCase("ssc"))
					if(param[1].equalsIgnoreCase("of"))
						activeChar.unsetVar("SkillsChance");
					else if(param[1].equalsIgnoreCase("on") && (Config.SKILLS_SHOW_CHANCE || activeChar.isGM()))
						activeChar.setVar("SkillsChance", "1");
				if(Config.BLOCK_EXP && param[0].equalsIgnoreCase("noe"))
					if(param[1].equalsIgnoreCase("on"))
						activeChar.setVar("NoExp", "1");
					else if(param[1].equalsIgnoreCase("of"))
						activeChar.unsetVar("NoExp");
				if(param[0].equalsIgnoreCase("pf"))
					if(param[1].equalsIgnoreCase("of"))
						activeChar.setVar("no_pf", "1");
					else if(param[1].equalsIgnoreCase("on"))
						activeChar.unsetVar("no_pf");
				if(param[0].equalsIgnoreCase("notraders"))
					if(param[1].equalsIgnoreCase("on"))
						activeChar.setVar("notraders", "1");
					else if(param[1].equalsIgnoreCase("of"))
						activeChar.unsetVar("notraders");
				if(Config.SERVICES_ENABLE_NO_CARRIER && param[0].equalsIgnoreCase("noCarrier"))
				{
					int time = NumberUtils.toInt(param[1], Config.SERVICES_NO_CARRIER_DEFAULT_TIME);
					if(time > Config.SERVICES_NO_CARRIER_MAX_TIME)
						time = Config.SERVICES_NO_CARRIER_MAX_TIME;
					else if(time < Config.SERVICES_NO_CARRIER_MIN_TIME)
						time = Config.SERVICES_NO_CARRIER_MIN_TIME;
					activeChar.setVar("noCarrier", String.valueOf(time));
				}
				if(param[0].equalsIgnoreCase("noShift"))
					if(param[1].equalsIgnoreCase("on"))
						activeChar.setVar("noShift", "1");
					else if(param[1].equalsIgnoreCase("of"))
						activeChar.unsetVar("noShift");
				if(param[0].equalsIgnoreCase("translit"))
					if(param[1].equalsIgnoreCase("on"))
						activeChar.setVar("translit", "tl");
					else if(param[1].equalsIgnoreCase("la"))
						activeChar.setVar("translit", "tc");
					else if(param[1].equalsIgnoreCase("of"))
						activeChar.unsetVar("translit");
				if(param[0].equalsIgnoreCase("noeventask"))
					if(param[1].equalsIgnoreCase("on"))
						activeChar.setVar("NoEventAsk", "1");
					else if(param[1].equalsIgnoreCase("of"))
						activeChar.setVar("NoEventAsk", "0");
				if(param[0].equalsIgnoreCase("autoloota"))
					activeChar.setAutoLootAdena(Boolean.parseBoolean(param[1]));
				if(param[0].equalsIgnoreCase("autoloot"))
					activeChar.setAutoLootItems(Boolean.parseBoolean(param[1]));
				if(param[0].equalsIgnoreCase("autolooth"))
					activeChar.setAutoLootHerbs(Boolean.parseBoolean(param[1]));
				if(param[0].equalsIgnoreCase("autolootl"))
					activeChar.setAutoLootList(Boolean.parseBoolean(param[1]));
			}
			}
			show(HtmCache.getInstance().getHtml("scripts/commands/voiced/lang.htm", activeChar), activeChar);
			//return true;
		}
		String dialog = HtmCache.getInstance().getHtml("scripts/commands/voiced/lang.htm", activeChar);
		dialog = dialog.replaceFirst("%lang%", activeChar.getVar("lang@").toUpperCase());
		dialog = dialog.replaceFirst("%noe%", activeChar.getVarBoolean("NoExp") ? "<font color=\"00ff00\">On</font>" : "<font color=\"ff0000\">Off</font>");
		dialog = dialog.replaceFirst("%pf%", activeChar.getVarBoolean("no_pf") ? "<font color=\"ff0000\">Off</font>" : "<font color=\"00ff00\">On</font>");
		dialog = dialog.replaceFirst("%notraders%", activeChar.getVarBoolean("notraders") ? "<font color=\"00ff00\">On</font>" : "<font color=\"ff0000\">Off</font>");
		dialog = dialog.replaceFirst("%noShift%", activeChar.getVarBoolean("noShift") ? "<font color=\"00ff00\">On</font>" : "<font color=\"ff0000\">Off</font>");
		dialog = dialog.replaceFirst("%noeventask%", activeChar.getVarBoolean("NoEventAsk", Config.EVENT_NO_ASK) ? "<font color=\"00ff00\">On</font>" : "<font color=\"ff0000\">Off</font>");
		dialog = dialog.replaceFirst("%noCarrier%", Config.SERVICES_ENABLE_NO_CARRIER ? activeChar.getVar("noCarrier") != null ? "<font color=\"00ff00\">" + activeChar.getVar("noCarrier") + "</font>" : "<font color=\"00ff00\">" + Config.SERVICES_NO_CARRIER_DEFAULT_TIME + "</font>" : "<font color=\"ff0000\">N/A</font>");
		if(!Config.SKILLS_SHOW_CHANCE)
			dialog = dialog.replaceFirst("%ssc%", "<font color=\"ff0000\">N/A</font>");
		else if(!activeChar.getVarBoolean("SkillsChance"))
			dialog = dialog.replaceFirst("%ssc%", "<font color=\"ff0000\">Off</font>");
		else
			dialog = dialog.replaceFirst("%ssc%", "<font color=\"00ff00\">On</font>");
		final String tl = activeChar.getVar("translit");
		if(tl == null)
			dialog = dialog.replaceFirst("%translit%", "<font color=\"ff0000\">Off</font>");
		else if(tl.equals("tl"))
			dialog = dialog.replaceFirst("%translit%", "<font color=\"00ff00\">On</font>");
		else
			dialog = dialog.replaceFirst("%translit%", "<font color=\"00ff00\">Lt</font>");
		String additional = "";
		if(Config.AUTO_LOOT_INDIVIDUAL_ADENA)
		{
			String bt;
			if(activeChar.isAutoLootAdenaEnabled())
				bt = Lang.menu_button.sprintf(new Object[] {
						100,
						"autoloota false",
						new CustomMessage("scripts.commands.voiced.Lang.Disable").toString(activeChar) });
			else
				bt = Lang.menu_button.sprintf(new Object[] {
						100,
						"autoloota true",
						new CustomMessage("scripts.commands.voiced.Lang.Enable").toString(activeChar) });
			additional += Lang.menu_row.sprintf(new Object[] {
					"<font color=\"ffff33\">" + (activeChar.isLangRus() ? "Автолут Адена" : "Auto-Loot Adena"),
					bt });
		}
		if(Config.AUTO_LOOT_INDIVIDUAL_ITEMS)
		{
			String bt;
			if(activeChar.isAutoLootItemsEnabled())
				bt = Lang.menu_button.sprintf(new Object[] {
						100,
						"autoloot false",
						new CustomMessage("scripts.commands.voiced.Lang.Disable").toString(activeChar) });
			else
				bt = Lang.menu_button.sprintf(new Object[] {
						100,
						"autoloot true",
						new CustomMessage("scripts.commands.voiced.Lang.Enable").toString(activeChar) });
			additional += Lang.menu_row.sprintf(new Object[] {
					"<font color=\"ffff33\">" + (activeChar.isLangRus() ? "Автолут предметов" : "Auto-Loot Items"),
					bt });
		}
		if(Config.AUTO_LOOT_INDIVIDUAL_HERBS)
		{
			String bt;
			if(activeChar.isAutoLootHerbsEnabled())
				bt = Lang.menu_button.sprintf(new Object[] {
						100,
						"autolooth false",
						new CustomMessage("scripts.commands.voiced.Lang.Disable").toString(activeChar) });
			else
				bt = Lang.menu_button.sprintf(new Object[] {
						100,
						"autolooth true",
						new CustomMessage("scripts.commands.voiced.Lang.Enable").toString(activeChar) });
			additional += Lang.menu_row.sprintf(new Object[] {
					"<font color=\"ffff33\">" + (activeChar.isLangRus() ? "Автолут хербов" : "Auto-Loot Herbs"),
					bt });
		}
		if(Config.AUTO_LOOT_INDIVIDUAL_LIST)
		{
			String bt;
			if(activeChar.isAutoLootListEnabled())
				bt = Lang.menu_button.sprintf(new Object[] {
						100,
						"autolootl false",
						new CustomMessage("scripts.commands.voiced.Lang.Disable").toString(activeChar) });
			else
				bt = Lang.menu_button.sprintf(new Object[] {
						100,
						"autolootl true",
						new CustomMessage("scripts.commands.voiced.Lang.Enable").toString(activeChar) });
			additional += Lang.menu_row.sprintf(new Object[] {
					"<font color=\"ffff33\">" + (activeChar.isLangRus() ? "Автолут листа" : "Auto-Loot List"),
					bt });
		}
		if(!Config.ALLOW_CLASS_MASTERS_LIST.isEmpty())
		{
			final String bt = "<button width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\" action=\"bypass -h scripts_services.ClassMaster:list\" value=\"" + (activeChar.isLangRus() ? "Открыть" : "Open") + "\">";
			additional += Lang.menu_row.sprintf(new Object[] {
					"<font color=\"cc9900\">" + (activeChar.isLangRus() ? "Классмастер" : "Classmaster"),
					bt });
		}
		if(Config.SERVICES_LOCK_ACCOUNT_IP || Config.SERVICES_LOCK_ACC_HWID || Config.SERVICES_LOCK_CHAR_HWID)
		{
			final String bt = "<button width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\" action=\"bypass -h user_lock\" value=\"" + (activeChar.isLangRus() ? "Войти" : "Enter") + "\">";
			additional += Lang.menu_row.sprintf(new Object[] {
					"<font color=\"00ccff\">" + (activeChar.isLangRus() ? "Система безопасности" : "Security system"),
					bt });
		}
		if(Config.ALLOW_OLY_HENNA)
		{
			final String bt = "<button width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\" action=\"bypass -h user_olyhenna\" value=\"" + (activeChar.isLangRus() ? "Настройка" : "Setting") + "\">";
			additional += Lang.menu_row.sprintf(new Object[] {
					"<font color=\"99ff33\">" + (activeChar.isLangRus() ? "Краски Олимпиады" : "Olympiad symbols"),
					bt });
		}
		dialog = dialog.replaceFirst("%additional%", additional);
		show(dialog, activeChar);
		return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
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
		menu_row = new PrintfFormat("<table><tr><td width=5></td><td width=140>%s:</td><td width=100>%s</td></tr></table>");
		menu_button = new PrintfFormat("<button width=%d height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\" action=\"bypass -h user_menu %s\" value=\"%s\">");
	}
}
