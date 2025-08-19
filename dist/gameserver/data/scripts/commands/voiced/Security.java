package commands.voiced;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.ScheduledFuture;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.database.mysql;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.instancemanager.PlayerManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.authcomm.AuthServerCommunication;
import l2s.gameserver.network.authcomm.gs2as.ChangeAllowedHwid;
import l2s.gameserver.network.authcomm.gs2as.ChangeAllowedIp;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Files;
import l2s.gameserver.utils.Util;

public class Security extends Functions implements IVoicedCommandHandler, ScriptFile
{
	private String[] _commandList;
	private static String defaultPage;
	private static ScheduledFuture<?> _saveTask;
	private static final String DTF = "yyyy-MM-dd_HH-mm-ss";

	public Security()
	{
		_commandList = new String[] {
				"lock",
				"lockIp",
				"unlockIp",
				"lockchar",
				"lockchar2",
				"lockacc",
				"lockacc2",
				"charkey",
				"charkeyset",
				"charkeydel" };
	}

	private static void showDefaultPage(final Player activeChar)
	{
		final boolean en = !activeChar.isLangRus();
		String html = HtmCache.getInstance().getHtml(Security.defaultPage, activeChar);
		html = html.replace("%ip_block%", IpBlockStatus(en));
		html = html.replace("%hwid_block%", HwidBlockStatus(en));
		html = html.replace("%hwid_block_char%", HwidBlockCharStatus(en));
		html = html.replace("%char_key%", CharKey(activeChar.getObjectId(), en));
		html = html.replace("%curIP%", activeChar.getIP());
		show(html, activeChar);
	}

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String args)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		if(command.equalsIgnoreCase("lock"))
		{
			showDefaultPage(activeChar);
			return true;
		}
		if(command.equalsIgnoreCase("unlockIp"))
		{
			AuthServerCommunication.getInstance().sendPacket(new ChangeAllowedIp(activeChar.getAccountName(), ""));
			if(!activeChar.isLangRus())
				activeChar.sendMessage("Account unlocked.");
			else
				activeChar.sendMessage("\u041e\u0433\u0440\u0430\u043d\u0438\u0447\u0435\u043d\u0438\u0435 \u0432\u0445\u043e\u0434\u0430 \u043f\u043e IP \u0430\u0434\u0440\u0435\u0441\u0443 \u0441\u043d\u044f\u0442\u043e.");
			String html = HtmCache.getInstance().getHtml("scripts/commands/voiced/lock_ip.html", activeChar);
			html = html.replaceFirst("%curIP%", activeChar.getIP());
			show(html, activeChar);
			return true;
		}
		if(command.equalsIgnoreCase("lockIp"))
		{
			if(!Config.SERVICES_LOCK_ACCOUNT_IP)
			{
				activeChar.sendMessage(activeChar.isLangRus() ? "Сервис отключен." : "Service disabled.");
				return true;
			}
			AuthServerCommunication.getInstance().sendPacket(new ChangeAllowedIp(activeChar.getAccountName(), activeChar.getIP()));
			if(!activeChar.isLangRus())
				activeChar.sendMessage("Account locked.");
			else
				activeChar.sendMessage("\u0410\u043a\u0442\u0438\u0432\u0438\u0440\u043e\u0432\u0430\u043d\u043e. \u0420\u0430\u0437\u0440\u0435\u0448\u0435\u043d\u043d\u044b\u0439 IP: " + activeChar.getIP());
			String html = HtmCache.getInstance().getHtml("scripts/commands/voiced/lock_ip.html", activeChar);
			html = html.replaceFirst("%curIP%", activeChar.getIP());
			show(html, activeChar);
			return true;
		}
		else
		{
			if(command.equalsIgnoreCase("lockchar"))
			{
				if(Config.SERVICES_LOCK_CHAR_HWID)
				{
					if(!activeChar.getHWID().isEmpty())
					{
						if(!activeChar.lockChar1.isEmpty())
						{
							if(activeChar.lockChar2.isEmpty())
								mysql.set("DELETE FROM `hwid_locks` WHERE `obj_Id`=" + activeChar.getObjectId() + " LIMIT 1");
							else
								mysql.set("REPLACE INTO `hwid_locks` (obj_Id, Lock1, Lock2) values(" + activeChar.getObjectId() + ",'','" + activeChar.lockChar2 + "')");
							activeChar.lockChar1 = "";
							if(!activeChar.isLangRus())
								activeChar.sendMessage("Your char unlocked from PC #1.");
							else
								activeChar.sendMessage("\u041f\u0440\u0438\u0432\u044f\u0437\u043a\u0430 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 \u043a \u0436\u0435\u043b\u0435\u0437\u0443 #1 \u0443\u0431\u0440\u0430\u043d\u0430.");
						}
						else
						{
							mysql.set("REPLACE INTO `hwid_locks` (obj_Id, Lock1, Lock2) values(" + activeChar.getObjectId() + ",'" + activeChar.getHWID() + "','" + activeChar.lockChar2 + "')");
							activeChar.lockChar1 = activeChar.getHWID();
							if(!activeChar.isLangRus())
								activeChar.sendMessage("Your char locked to PC #1 successfully.");
							else
								activeChar.sendMessage("\u0412\u0430\u0448 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0440\u0438\u0432\u044f\u0437\u0430\u043d \u043a \u0436\u0435\u043b\u0435\u0437\u0443 #1.");
						}
					}
					else
						activeChar.sendMessage(activeChar.isLangRus() ? "\u0412\u0430\u0448\u0435 \u0436\u0435\u043b\u0435\u0437\u043e \u043d\u0435 \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0435\u043d\u043e! \u041f\u0440\u0438\u0432\u044f\u0437\u043a\u0430 \u043d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u0430." : "Your PC is not identified! Lock impossible.");
				}
				else
					activeChar.sendMessage(activeChar.isLangRus() ? "Сервис отключен." : "Service disabled.");
				return true;
			}
			if(command.equalsIgnoreCase("lockchar2"))
			{
				if(Config.SERVICES_LOCK_CHAR_HWID)
				{
					String hwid = "";
					if(args == null || args.split(" ").length <= 0 || args.split(" ")[0].isEmpty())
					{
						if(!activeChar.lockChar2.isEmpty())
						{
							if(activeChar.lockChar1.isEmpty())
								mysql.set("DELETE FROM `hwid_locks` WHERE `obj_Id`=" + activeChar.getObjectId() + " LIMIT 1");
							else
								mysql.set("REPLACE INTO `hwid_locks` (obj_Id, Lock1, Lock2) values(" + activeChar.getObjectId() + ",'" + activeChar.lockChar1 + "','')");
							activeChar.lockChar2 = "";
							if(!activeChar.isLangRus())
								activeChar.sendMessage("Your char unlocked from PC #2.");
							else
								activeChar.sendMessage("\u041f\u0440\u0438\u0432\u044f\u0437\u043a\u0430 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 \u043a \u0436\u0435\u043b\u0435\u0437\u0443 #2 \u0443\u0431\u0440\u0430\u043d\u0430.");
						}
						else
							activeChar.sendMessage(activeChar.isLangRus() ? "\u041f\u0440\u0438\u0432\u044f\u0437\u043a\u0430 \u043e\u0442\u0441\u0443\u0442\u0441\u0442\u0432\u0443\u0435\u0442." : "Not locked.");
						return true;
					}
					hwid = PlayerManager.getLastHWIDByName(args.split(" ")[0]);
					if(!hwid.isEmpty())
					{
						mysql.set("REPLACE INTO `hwid_locks` (obj_Id, Lock1, Lock2) values(" + activeChar.getObjectId() + ",'" + activeChar.lockChar1 + "','" + hwid + "')");
						activeChar.lockChar2 = hwid;
						if(!activeChar.isLangRus())
							activeChar.sendMessage("Your char locked to PC #2 successfully.");
						else
							activeChar.sendMessage("\u0412\u0430\u0448 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0440\u0438\u0432\u044f\u0437\u0430\u043d \u043a \u0436\u0435\u043b\u0435\u0437\u0443 #2.");
					}
					else
						activeChar.sendMessage(activeChar.isLangRus() ? "\u0412\u0430\u0448\u0435 \u0436\u0435\u043b\u0435\u0437\u043e \u043d\u0435 \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0435\u043d\u043e! \u041f\u0440\u0438\u0432\u044f\u0437\u043a\u0430 \u043d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u0430." : "Your PC is not identified! Lock impossible.");
				}
				else
					activeChar.sendMessage(activeChar.isLangRus() ? "Сервис отключен." : "Service disabled.");
				return true;
			}
			if(command.equalsIgnoreCase("lockacc"))
			{
				if(Config.SERVICES_LOCK_ACC_HWID)
				{
					if(!activeChar.getHWID().isEmpty())
					{
						if(!activeChar.getNetConnection().getAllowedHwid().isEmpty())
						{
							AuthServerCommunication.getInstance().sendPacket(new ChangeAllowedHwid(activeChar.getAccountName(), activeChar.getNetConnection().getAllowedHwidSecond()));
							activeChar.getNetConnection().setAllowedHwid("");
							if(!activeChar.isLangRus())
								activeChar.sendMessage("Your account unlocked from PC #1.");
							else
								activeChar.sendMessage("\u041f\u0440\u0438\u0432\u044f\u0437\u043a\u0430 \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u0430 \u043a \u0436\u0435\u043b\u0435\u0437\u0443 #1 \u0443\u0431\u0440\u0430\u043d\u0430.");
						}
						else
						{
							AuthServerCommunication.getInstance().sendPacket(new ChangeAllowedHwid(activeChar.getAccountName(), activeChar.getNetConnection().getAllowedHwidSecond()));
							activeChar.getNetConnection().setAllowedHwid(activeChar.getHWID());
							if(!activeChar.isLangRus())
								activeChar.sendMessage("Your account locked to PC #1 successfully.");
							else
								activeChar.sendMessage("\u0412\u0430\u0448 \u0430\u043a\u043a\u0430\u0443\u043d\u0442 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0440\u0438\u0432\u044f\u0437\u0430\u043d \u043a \u0436\u0435\u043b\u0435\u0437\u0443 #1.");
						}
					}
					else
						activeChar.sendMessage(activeChar.isLangRus() ? "\u0412\u0430\u0448\u0435 \u0436\u0435\u043b\u0435\u0437\u043e \u043d\u0435 \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0435\u043d\u043e! \u041f\u0440\u0438\u0432\u044f\u0437\u043a\u0430 \u043d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u0430." : "Your PC is not identified! Lock impossible.");
				}
				else
					activeChar.sendMessage(activeChar.isLangRus() ? "Сервис отключен." : "Service disabled.");
				return true;
			}
			if(command.equalsIgnoreCase("lockacc2"))
			{
				if(Config.SERVICES_LOCK_ACC_HWID)
				{
					String hwid = "";
					if(args == null || args.split(" ").length <= 0 || args.split(" ")[0].isEmpty())
					{
						if(!activeChar.getNetConnection().getAllowedHwidSecond().isEmpty())
						{
							AuthServerCommunication.getInstance().sendPacket(new ChangeAllowedHwid(activeChar.getAccountName(), ""));
							activeChar.getNetConnection().setAllowedHwidSecond("");
							if(!activeChar.isLangRus())
								activeChar.sendMessage("Your account unlocked from PC #2.");
							else
								activeChar.sendMessage("\u041f\u0440\u0438\u0432\u044f\u0437\u043a\u0430 \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u0430 \u043a \u0436\u0435\u043b\u0435\u0437\u0443 #2 \u0443\u0431\u0440\u0430\u043d\u0430.");
						}
						else
							activeChar.sendMessage(activeChar.isLangRus() ? "\u041f\u0440\u0438\u0432\u044f\u0437\u043a\u0430 \u043e\u0442\u0441\u0443\u0442\u0441\u0442\u0432\u0443\u0435\u0442." : "Not locked.");
						return true;
					}
					hwid = PlayerManager.getLastHWIDByName(args.split(" ")[0]);
					if(!hwid.isEmpty())
					{
						AuthServerCommunication.getInstance().sendPacket(new ChangeAllowedHwid(activeChar.getAccountName(), hwid));
						activeChar.getNetConnection().setAllowedHwidSecond(hwid);
						if(!activeChar.isLangRus())
							activeChar.sendMessage("Your account locked to PC #2 successfully.");
						else
							activeChar.sendMessage("\u0412\u0430\u0448 \u0430\u043a\u043a\u0430\u0443\u043d\u0442 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0440\u0438\u0432\u044f\u0437\u0430\u043d \u043a \u0436\u0435\u043b\u0435\u0437\u0443 #2.");
					}
					else
						activeChar.sendMessage(activeChar.isLangRus() ? "\u0412\u0430\u0448\u0435 \u0436\u0435\u043b\u0435\u0437\u043e \u043d\u0435 \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0435\u043d\u043e! \u041f\u0440\u0438\u0432\u044f\u0437\u043a\u0430 \u043d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u0430." : "Your PC is not identified! Lock impossible.");
				}
				else
					activeChar.sendMessage(activeChar.isLangRus() ? "Сервис отключен." : "Service disabled.");
				return true;
			}
			if(command.equalsIgnoreCase("charkey"))
			{
				if(!Config.SERVICES_CHAR_KEY)
				{
					activeChar.sendMessage(activeChar.isLangRus() ? "Сервис отключен." : "Service disabled.");
					return true;
				}
				String html = HtmCache.getInstance().getHtml("scripts/commands/voiced/charKey/char_key.html", activeChar);
				html = html.replaceFirst("%stat%", CharKey(activeChar.getObjectId(), !activeChar.isLangRus()));
				show(html, activeChar);
				return true;
			}
			else
			{
				if(command.equalsIgnoreCase("charkeyset"))
				{
					if(Config.SERVICES_CHAR_KEY)
					{
						if(Config.CHAR_KEYS.containsKey(activeChar.getObjectId()))
						{
							show("<br>" + (activeChar.isLangRus() ? "\u041d\u0430 \u0442\u0435\u043a\u0443\u0449\u0435\u043c \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0435 \u0443\u0436\u0435 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u043a\u043b\u044e\u0447. \u0414\u043b\u044f \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0438 \u043d\u043e\u0432\u043e\u0433\u043e \u043a\u043b\u044e\u0447\u0430, \u0443\u0434\u0430\u043b\u0438\u0442\u0435 \u0442\u0435\u043a\u0443\u0449\u0438\u0439 ( \u043a\u043e\u043c\u0430\u043d\u0434\u0430" : "Current character already have the key. To install the new key, remove the current ( command") + " <font color=\"LEVEL\">.charkeydel</font> )<br><br><br><a action=\"bypass -h user_charkey\">" + (activeChar.isLangRus() ? "\u041d\u0430\u0437\u0430\u0434" : "Back") + "</a>", activeChar);
							return true;
						}
						if(args != null && args.split(" ").length > 0 && !args.split(" ")[0].isEmpty())
						{
							final String param = args.split(" ")[0].trim();
							if(!Util.isMatchingRegexp(param, "[A-Za-z0-9]{4,16}"))
							{
								show("<br>" + (activeChar.isLangRus() ? "\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e \u0432\u0432\u043e\u0434\u0438\u0442\u044c \u043e\u0442 4 \u0434\u043e 16 \u043b\u0430\u0442\u0438\u043d\u0441\u043a\u0438\u0445 \u0441\u0438\u043c\u0432\u043e\u043b\u043e\u0432" : "Allowed 4-16 latin symbols only") + ".<br><br><br><a action=\"bypass -h user_charkey\">" + (activeChar.isLangRus() ? "\u041d\u0430\u0437\u0430\u0434" : "Back") + "</a>", activeChar);
								return true;
							}
							Config.CHAR_KEYS.put(activeChar.getObjectId(), param);
							if(activeChar.isKeyForced())
							{
								activeChar.setKeyForced(false);
								activeChar.sendMessage(new CustomMessage("l2s.KeyUnFrozen"));
							}
							String html2 = HtmCache.getInstance().getHtml("scripts/commands/voiced/charKey/char_key_set.html", activeChar);
							html2 = html2.replaceFirst("%key%", param);
							show(html2, activeChar);
						}
						else
							activeChar.sendMessage(activeChar.isLangRus() ? "\u041d\u0443\u0436\u043d\u043e \u0437\u0430\u043f\u043e\u043b\u043d\u0438\u0442\u044c \u0432\u0441\u0435 \u043f\u043e\u043b\u044f." : "You must to fill in all fields.");
					}
					else
						activeChar.sendMessage(activeChar.isLangRus() ? "Сервис отключен." : "Service disabled.");
					return true;
				}
				if(command.equalsIgnoreCase("charkeydel"))
				{
					if(Config.SERVICES_CHAR_KEY)
					{
						if(!Config.CHAR_KEYS.containsKey(activeChar.getObjectId()))
						{
							show("<br>" + (activeChar.isLangRus() ? "\u041a\u043b\u044e\u0447 \u043d\u0435 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d" : "Key not installed") + ".<br><br><br><a action=\"bypass -h user_charkey\">" + (activeChar.isLangRus() ? "\u041d\u0430\u0437\u0430\u0434" : "Back") + "</a>", activeChar);
							return true;
						}
						Config.CHAR_KEYS.remove(activeChar.getObjectId());
						if(Config.CHAR_KEY_SAVE_DB)
							mysql.set("DELETE FROM `char_keys` WHERE `obj_Id`=" + activeChar.getObjectId() + " LIMIT 1");
						show("<br>" + (activeChar.isLangRus() ? "\u041a\u043b\u044e\u0447 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0443\u0434\u0430\u043b\u0435\u043d" : "Key deleted successfully") + "!<br><br><br><a action=\"bypass -h user_charkey\">" + (activeChar.isLangRus() ? "\u041d\u0430\u0437\u0430\u0434" : "Back") + "</a>", activeChar);
					}
					else
						activeChar.sendMessage(activeChar.isLangRus() ? "Сервис отключен." : "Service disabled.");
					return true;
				}
				return false;
			}
		}
	}

	private static String IpBlockStatus(final boolean en)
	{
		if(Config.SERVICES_LOCK_ACCOUNT_IP)
			return en ? "Allowed" : "\u0420\u0430\u0437\u0440\u0435\u0448\u0435\u043d\u0430";
		return en ? "Disabled" : "\u0417\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u0430";
	}

	private static String HwidBlockStatus(final boolean en)
	{
		if(Config.SERVICES_LOCK_ACC_HWID)
			return en ? "Allowed" : "\u0420\u0430\u0437\u0440\u0435\u0448\u0435\u043d\u0430";
		return en ? "Disabled" : "\u0417\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u0430";
	}

	private static String HwidBlockCharStatus(final boolean en)
	{
		if(Config.SERVICES_LOCK_CHAR_HWID)
			return en ? "Allowed" : "\u0420\u0430\u0437\u0440\u0435\u0448\u0435\u043d\u0430";
		return en ? "Disabled" : "\u0417\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u0430";
	}

	private static String CharKey(final int id, final boolean en)
	{
		if(!Config.SERVICES_CHAR_KEY)
			return en ? "Disabled" : "\u0417\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u0430";
		if(Config.CHAR_KEYS.containsKey(id))
			return en ? "Installed" : "\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430";
		return en ? "Not installed" : "\u041d\u0435 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430";
	}

	@Override
	public void onLoad()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
		if(Config.SERVICES_CHAR_KEY)
		{
			if(Config.CHAR_KEY_SAVE_DB)
			{
				if(!Config.CHAR_KEYS.isEmpty())
					Config.CHAR_KEYS.clear();
				Connection con = null;
				Statement statement = null;
				ResultSet rset = null;
				try
				{
					con = DatabaseFactory.getInstance().getConnection();
					statement = con.createStatement();
					rset = statement.executeQuery("SELECT * FROM `char_keys`");
					while(rset.next())
						Config.CHAR_KEYS.put(rset.getInt("obj_Id"), rset.getString("password"));
				}
				catch(Exception e)
				{
					ScriptFile._log.error("", e);
				}
				finally
				{
					DbUtils.closeQuietly(con, statement, rset);
				}
				ScriptFile._log.info("Loaded " + Config.CHAR_KEYS.size() + " characters keys.");
			}
			else
			{
				final File file = new File("data/char_keys.txt");
				if(file.exists())
				{
					if(!Config.CHAR_KEYS.isEmpty())
						Config.CHAR_KEYS.clear();
					LineNumberReader lnr = null;
					try
					{
						lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
						String line;
						while((line = lnr.readLine()) != null)
						{
							final StringTokenizer st = new StringTokenizer(line, "\n");
							if(st.hasMoreTokens())
							{
								final String[] tab = st.nextToken().split("\t");
								if(tab.length <= 1)
									continue;
								Config.CHAR_KEYS.put(Integer.parseInt(tab[0]), tab[1].trim());
							}
						}
						ScriptFile._log.info("Loaded " + Config.CHAR_KEYS.size() + " characters keys.");
					}
					catch(IOException e2)
					{
						ScriptFile._log.error("Error reading char_keys " + e2);
					}
					finally
					{
						try
						{
							if(lnr != null)
								lnr.close();
						}
						catch(Exception ex)
						{}
					}
				}
			}
			if(Security._saveTask != null)
			{
				Security._saveTask.cancel(false);
				Security._saveTask = null;
			}
			if(Config.CHAR_KEY_SAVE_DELAY > 0)
				Security._saveTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new saveTask(), Config.CHAR_KEY_SAVE_DELAY * 60000L, Config.CHAR_KEY_SAVE_DELAY * 60000L);
		}
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{
		if(Security._saveTask != null)
		{
			Security._saveTask.cancel(false);
			Security._saveTask = null;
		}
		save();
	}

	public static void save()
	{
		if(Config.SERVICES_CHAR_KEY && !Config.CHAR_KEYS.isEmpty())
			if(Config.CHAR_KEY_SAVE_DB)
				for(final int i : Config.CHAR_KEYS.keySet())
					mysql.set("REPLACE INTO `char_keys` (`obj_Id`, `password`) VALUES ('" + i + "','" + Config.CHAR_KEYS.get(i) + "')");
			else
			{
				final File file = new File("data/char_keys.txt");
				if(file.exists())
				{
					if(Config.CHAR_KEY_BACKUP)
						Files.copyFile("data/char_keys.txt", "data/backup/char_keys_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".txt");
					file.delete();
				}
				try
				{
					file.createNewFile();
				}
				catch(IOException e)
				{
					ScriptFile._log.error("Creating char_keys failed: " + e);
					return;
				}
				FileWriter save = null;
				try
				{
					save = new FileWriter(file, true);
					for(final int j : Config.CHAR_KEYS.keySet())
						save.write(j + "\t" + Config.CHAR_KEYS.get(j) + "\n");
				}
				catch(IOException e2)
				{
					ScriptFile._log.error("Saving char_keys failed: " + e2);
					e2.printStackTrace();
				}
				finally
				{
					try
					{
						if(save != null)
							save.close();
					}
					catch(Exception ex)
					{}
				}
			}
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}

	static
	{
		Security.defaultPage = "scripts/commands/voiced/lock.htm";
	}

	private static class saveTask implements Runnable
	{
		@Override
		public void run()
		{
			Security.save();
		}
	}
}
