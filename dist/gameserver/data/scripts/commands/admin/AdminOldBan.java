package commands.admin;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.Announcements;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.database.mysql;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.PlayerManager;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.ManufactureItem;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.TradeItem;
import l2s.gameserver.model.World;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.authcomm.AuthServerCommunication;
import l2s.gameserver.network.authcomm.gs2as.ChangeAccessLevel;
import l2s.gameserver.network.authcomm.gs2as.ChangeAllowedHwid;
import l2s.gameserver.network.authcomm.gs2as.ChangeAllowedIp;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AutoBan;
import l2s.gameserver.utils.Log;
import l2s.gameserver.utils.SpamFilter;
import l2s.gameserver.utils.TimeUtils;
import l2s.gameserver.utils.Util;

public class AdminOldBan implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanBan)
			return false;
		if(command.startsWith("admin_old_ban"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			try
			{
				st.nextToken();
				final String player = st.nextToken();
				int time = 0;
				String bmsg = "";
				String msg = "";
				if(st.hasMoreTokens())
					time = Integer.parseInt(st.nextToken());
				if(st.hasMoreTokens())
				{
					bmsg = "admin_old_ban " + player + " " + time + " ";
					msg = command.substring(bmsg.length(), command.length());
				}
				final Player plyr = World.getPlayer(player);
				if(plyr != null)
				{
					plyr.sendMessage(new CustomMessage("scripts.commands.admin.AdminBan.YoureBannedByGM").addString(activeChar.getName()));
					plyr.setAccessLevel(-100);
					AutoBan.Banned(plyr, time, msg, activeChar.getName());
					if(plyr.isInOfflineMode())
						plyr.setOfflineMode(false);
					plyr.kick(true);
					activeChar.sendMessage("You banned " + plyr.getName());
				}
				else if(AutoBan.Banned(player, -100, time, msg, activeChar.getName()))
					activeChar.sendMessage("You banned " + player);
				else
					activeChar.sendMessage("Can't find char: " + player);
			}
			catch(Exception e)
			{
				activeChar.sendMessage("Command syntax: //ban char_name period reason");
			}
		}
		else if(command.startsWith("admin_old_unban"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				final String player = st.nextToken();
				if(AutoBan.Banned(player, 0, 0, "", activeChar.getName()))
					activeChar.sendMessage("You unbanned " + player);
				else
					activeChar.sendMessage("Can't find char: " + player);
			}
		}
		else if(command.startsWith("admin_acc_ban"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				int time2 = 0;
				final String name = st.nextToken();
				final String account = PlayerManager.getAccNameByName(name);
				if(account.isEmpty())
					activeChar.sendMessage("Character " + name + " not exist.");
				else
				{
					if(st.hasMoreTokens())
						time2 = 86400 * Integer.parseInt(st.nextToken()) + (int) (System.currentTimeMillis() / 1000L);
					AuthServerCommunication.getInstance().sendPacket(new ChangeAccessLevel(account, time2 > 0 ? 0 : -100, time2));
					activeChar.sendMessage("You banned account: " + account);
					final GameClient client = AuthServerCommunication.getInstance().getAuthedClient(account);
					if(client != null)
					{
						final Player player2 = client.getActiveChar();
						if(player2 != null)
						{
							if(player2.isInOfflineMode())
								player2.setOfflineMode(false);
							player2.kick(true);
							activeChar.sendMessage("Player " + player2.getName() + " kicked.");
						}
					}
				}
			}
		}
		else if(command.startsWith("admin_accsbanhwid"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				final String name2 = st.nextToken();
				final String hwid = PlayerManager.getLastHWIDByName(name2);
				if(hwid.isEmpty())
				{
					activeChar.sendMessage("No hwid or char " + name2 + " not exist.");
					return true;
				}
				List<String> accs = new ArrayList<String>();
				Connection con = null;
				PreparedStatement statement = null;
				ResultSet rset = null;
				try
				{
					con = DatabaseFactory.getInstance().getConnection();
					statement = con.prepareStatement("SELECT account_name FROM characters WHERE last_hwid=?");
					statement.setString(1, hwid);
					rset = statement.executeQuery();
					while(rset.next())
					{
						final String ac = rset.getString("account_name");
						if(!accs.contains(ac))
							accs.add(ac);
					}
				}
				catch(Exception ex)
				{}
				finally
				{
					DbUtils.closeQuietly(con, statement, rset);
				}
				if(accs.isEmpty())
				{
					activeChar.sendMessage("No accounts!");
					return true;
				}
				String reason = "";
				long time3 = 0L;
				if(st.hasMoreTokens())
					time3 = Integer.parseInt(st.nextToken()) * 1000L * 60L * 60L * 24L + System.currentTimeMillis();
				if(st.countTokens() >= 1)
				{
					reason = st.nextToken();
					while(st.hasMoreTokens())
						reason = reason + " " + st.nextToken();
				}
				if(AutoBan.addHwidBan(name2, hwid, reason, time3, activeChar.getName()))
					activeChar.sendMessage("You banned " + name2 + " by hwid.");
				reason = "";
				for(final String i : accs)
				{
					AuthServerCommunication.getInstance().sendPacket(new ChangeAccessLevel(i, -100, 0));
					reason = reason + i + " ";
				}
				activeChar.sendMessage("Banned accounts: " + reason);
				accs = null;
				final Player p = World.getPlayer(name2);
				if(p != null)
				{
					if(p.isInOfflineMode())
						p.setOfflineMode(false);
					p.kick(true);
				}
			}
		}
		else if(command.startsWith("admin_acc_unban"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				final String name2 = st.nextToken();
				final String account2 = PlayerManager.getAccNameByName(name2);
				if(account2.isEmpty())
					activeChar.sendMessage("Character " + name2 + " not exist.");
				else
				{
					AuthServerCommunication.getInstance().sendPacket(new ChangeAccessLevel(account2, 0, 0));
					activeChar.sendMessage("You unbanned account: " + account2);
				}
			}
		}
		else
		{
			if(command.startsWith("admin_trade_ban"))
				return tradeBan(new StringTokenizer(command), activeChar);
			if(command.startsWith("admin_trade_unban"))
				return tradeUnban(new StringTokenizer(command), activeChar);
			if(command.startsWith("admin_chatban"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				try
				{
					st.nextToken();
					final String player = st.nextToken();
					final String srok = st.nextToken();
					final String bmsg = "admin_chatban " + player + " " + srok + " ";
					final String msg = command.substring(bmsg.length(), command.length());
					if(AutoBan.ChatBan(player, Integer.parseInt(srok), msg, activeChar.getName()))
						activeChar.sendMessage("You ban chat for " + player + ".");
					else
						activeChar.sendMessage("Can't find char " + player + ".");
				}
				catch(Exception e)
				{
					activeChar.sendMessage("Command syntax: //chatban char_name period reason");
				}
			}
			else if(command.startsWith("admin_chatunban"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				try
				{
					st.nextToken();
					final String player = st.nextToken();
					if(AutoBan.ChatUnBan(player, activeChar.getName()))
						activeChar.sendMessage("You unban chat for " + player + ".");
					else
						activeChar.sendMessage("Can't find char " + player + ".");
				}
				catch(Exception e)
				{
					activeChar.sendMessage("Command syntax: //chatunban char_name");
					e.printStackTrace();
				}
			}
			else if(command.startsWith("admin_jail"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				try
				{
					st.nextToken();
					final String player = st.nextToken();
					final int srok2 = Integer.parseInt(st.nextToken());
					final Player target = World.getPlayer(player);
					if(target != null)
					{
						Util.jail(target, srok2);
						activeChar.sendMessage("You jailed " + target.getName());
					}
					else
						activeChar.sendMessage("Can't find char " + player + ".");
				}
				catch(Exception e)
				{
					activeChar.sendMessage("Command syntax: //jail char_name period");
				}
			}
			else if(command.startsWith("admin_unjail"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				try
				{
					st.nextToken();
					final String player = st.nextToken();
					final Player target2 = World.getPlayer(player);
					if(target2 != null)
					{
						if(target2.getVar("jailed") != null)
						{
							final String[] re = target2.getVar("jailedFrom").split(";");
							target2.teleToLocation(Integer.parseInt(re[0]), Integer.parseInt(re[1]), Integer.parseInt(re[2]));
							target2._unjailTask.cancel(true);
						}
						target2.unsetVar("jailedFrom");
						target2.unsetVar("jailed");
						activeChar.sendMessage("You unjailed " + player + ".");
					}
					else
						activeChar.sendMessage("Can't find char " + player + ".");
				}
				catch(Exception e)
				{
					activeChar.sendMessage("Command syntax: //unjail char_name");
					e.printStackTrace();
				}
			}
			else if(command.startsWith("admin_ckarma"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				try
				{
					st.nextToken();
					final String player = st.nextToken();
					final String srok = st.nextToken();
					final String bmsg = "admin_ckarma " + player + " " + srok + " ";
					final String msg = command.substring(bmsg.length(), command.length());
					final Player plyr = World.getPlayer(player);
					if(plyr != null)
					{
						final int newKarma = Integer.parseInt(srok) + plyr.getKarma();
						plyr.setKarma(newKarma);
						plyr.sendMessage("You get karma(" + srok + ") by GM " + activeChar.getName());
						AutoBan.Karma(plyr, Integer.parseInt(srok), msg, activeChar.getName());
						activeChar.sendMessage("You set karma(" + srok + ") " + plyr.getName());
					}
					else if(AutoBan.Karma(player, Integer.parseInt(srok), msg, activeChar.getName()))
						activeChar.sendMessage("You set karma(" + srok + ") " + player);
					else
						activeChar.sendMessage("Can't find char: " + player);
				}
				catch(Exception e)
				{
					activeChar.sendMessage("Command syntax: //ckarma char_name karma reason");
				}
			}
			else if(command.startsWith("admin_hban"))
			{
				final GameObject t = activeChar.getTarget();
				if(t != null && t != activeChar && t.isPlayer())
				{
					final StringTokenizer st2 = new StringTokenizer(command);
					String reason2 = "";
					long time4 = 0L;
					if(st2.countTokens() > 1)
					{
						st2.nextToken();
						time4 = System.currentTimeMillis() + 86400000L * Long.parseLong(st2.nextToken());
						if(st2.countTokens() >= 1)
						{
							reason2 = st2.nextToken();
							while(st2.hasMoreTokens())
								reason2 = reason2 + " " + st2.nextToken();
						}
					}
					final Player p2 = (Player) t;
					if(AutoBan.addHwidBan(p2.getName(), p2.getHWID(), reason2, time4, activeChar.getName()))
					{
						AuthServerCommunication.getInstance().sendPacket(new ChangeAccessLevel(p2.getAccountName(), -100, 0));
						activeChar.sendMessage("You banned " + p2.getName() + " by hwid and acc [" + p2.getAccountName() + "]");
						p2.sendMessage("Admin banned you!");
						if(p2.isInOfflineMode())
							p2.setOfflineMode(false);
						p2.kick(true);
					}
					else
						activeChar.sendMessage("Impossible!");
				}
				else
					activeChar.sendMessage("Incorrect target.");
			}
			else if(command.startsWith("admin_hnban"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				if(st.countTokens() > 1)
				{
					st.nextToken();
					String name2 = st.nextToken();
					String reason2 = "";
					long time4 = 0L;
					if(st.hasMoreTokens())
						time4 = System.currentTimeMillis() + 86400000L * Long.parseLong(st.nextToken());
					if(st.countTokens() >= 1)
					{
						reason2 = st.nextToken();
						while(st.hasMoreTokens())
							reason2 = reason2 + " " + st.nextToken();
					}
					final Player p2 = World.getPlayer(name2);
					if(p2 != null)
					{
						if(AutoBan.addHwidBan(p2.getName(), p2.getHWID(), reason2, time4, activeChar.getName()))
						{
							AuthServerCommunication.getInstance().sendPacket(new ChangeAccessLevel(p2.getAccountName(), -100, 0));
							activeChar.sendMessage("You banned " + p2.getName() + " by hwid and acc [" + p2.getAccountName() + "]");
							p2.sendMessage("Admin banned you!");
							if(p2.isInOfflineMode())
								p2.setOfflineMode(false);
							p2.kick(true);
						}
						else
							activeChar.sendMessage("Impossible!");
					}
					else
					{
						Connection con2 = null;
						PreparedStatement statement2 = null;
						ResultSet rset2 = null;
						try
						{
							con2 = DatabaseFactory.getInstance().getConnection();
							statement2 = con2.prepareStatement("SELECT last_hwid,char_name,account_name FROM characters WHERE char_name=? LIMIT 1");
							statement2.setString(1, name2);
							rset2 = statement2.executeQuery();
							if(rset2.next())
							{
								final String hwid2 = rset2.getString("last_hwid");
								name2 = rset2.getString("char_name");
								final String acc = rset2.getString("account_name");
								if(hwid2.isEmpty())
									activeChar.sendMessage("No char or hwid for name: " + name2);
								else if(AutoBan.addHwidBan(name2, hwid2, reason2, time4, activeChar.getName()))
								{
									AuthServerCommunication.getInstance().sendPacket(new ChangeAccessLevel(acc, -100, 0));
									activeChar.sendMessage("You banned " + name2 + " by hwid and acc [" + acc + "]");
								}
							}
							else
								activeChar.sendMessage("Player " + name2 + " not exist.");
						}
						catch(Exception ex2)
						{}
						finally
						{
							DbUtils.closeQuietly(con2, statement2, rset2);
						}
					}
				}
			}
			else if(command.startsWith("admin_unhban"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				if(st.countTokens() > 1)
				{
					st.nextToken();
					String name2 = st.nextToken();
					Connection con3 = null;
					PreparedStatement statement3 = null;
					ResultSet rset3 = null;
					try
					{
						con3 = DatabaseFactory.getInstance().getConnection();
						statement3 = con3.prepareStatement("SELECT char_name,last_hwid FROM characters WHERE char_name=? LIMIT 1");
						statement3.setString(1, name2);
						rset3 = statement3.executeQuery();
						if(rset3.next())
						{
							name2 = rset3.getString("char_name");
							final String hwid3 = rset3.getString("last_hwid");
							DbUtils.closeQuietly(statement3, rset3);
							statement3 = con3.prepareStatement("DELETE FROM hwid_bans WHERE HWID=? OR player=?");
							statement3.setString(1, hwid3);
							statement3.setString(2, name2);
							statement3.execute();
							activeChar.sendMessage("Deleted from hwid_bans: " + name2 + " HWID: " + hwid3);
						}
						else
							activeChar.sendMessage("Can't find char: " + name2);
					}
					catch(Exception ex3)
					{}
					finally
					{
						DbUtils.closeQuietly(con3, statement3, rset3);
					}
				}
			}
			else if(command.startsWith("admin_lgban"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				Player p3 = null;
				if(st.countTokens() > 1)
				{
					st.nextToken();
					p3 = World.getPlayer(st.nextToken());
				}
				else
				{
					final GameObject t2 = activeChar.getTarget();
					if(t2 != null && t2.isPlayer())
						p3 = (Player) t2;
				}
				if(p3 != null)
				{
					try
					{
						final File f = new File("./lameguard/banned_hwid.txt");
						if(!f.exists())
							f.createNewFile();
						final FileWriter writer = new FileWriter(f, true);
						writer.write("\n" + p3.getHWID() + "        # added: " + TimeUtils.toSimpleFormat(Calendar.getInstance().getTimeInMillis()) + " | account: " + p3.getAccountName() + " | player: " + p3.getName());
						writer.close();
					}
					catch(Exception e2)
					{
						activeChar.sendMessage("Impossible!");
						return false;
					}
					if(p3.isInOfflineMode())
						p3.setOfflineMode(false);
					p3.kick(true);
					activeChar.sendMessage("You banned " + p3.getName() + " by hwid in LG.");
				}
			}
			else if(command.startsWith("admin_isban"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				if(st.countTokens() > 1)
				{
					st.nextToken();
					String name2 = st.nextToken();
					final int id = PlayerManager.getObjectIdByName(name2);
					if(id <= 0)
					{
						activeChar.sendMessage("Player " + name2 + " not exist.");
						return false;
					}
					name2 = PlayerManager.getNameByObjectId(id);
					Connection con4 = null;
					PreparedStatement statement4 = null;
					ResultSet rset4 = null;
					final String hwid4 = PlayerManager.getLastHWIDByName(name2);
					if(hwid4.isEmpty())
						activeChar.sendMessage("No hwid for char: " + name2);
					else
						try
						{
							con4 = DatabaseFactory.getInstance().getConnection();
							statement4 = con4.prepareStatement("SELECT reason,end_date FROM hwid_bans WHERE HWID=? LIMIT 1");
							statement4.setString(1, hwid4);
							rset4 = statement4.executeQuery();
							if(rset4.next())
							{
								final long time5 = rset4.getLong("end_date");
								activeChar.sendMessage(name2 + " banned by hwid." + (time5 > 0L ? "EndDate: " + TimeUtils.toSimpleFormat(time5) : "") + " Reason: " + rset4.getString("reason"));
							}
							else
								activeChar.sendMessage("No hwid ban for char: " + name2);
						}
						catch(Exception ex4)
						{}
						finally
						{
							DbUtils.closeQuietly(con4, statement4, rset4);
						}
					final String account3 = PlayerManager.getAccNameByName(name2);
					try
					{
						con4 = DatabaseFactory.getInstanceLogin().getConnection();
						statement4 = con4.prepareStatement("SELECT access_level,last_ip FROM accounts WHERE login=? LIMIT 1");
						statement4.setString(1, account3);
						rset4 = statement4.executeQuery();
						String lastIp = "";
						if(rset4.next())
						{
							if(rset4.getInt("access_level") < 0)
								activeChar.sendMessage(name2 + " banned by account.");
							else
								activeChar.sendMessage("No account ban for char: " + name2);
							lastIp = rset4.getString("last_ip");
						}
						if(!lastIp.isEmpty())
						{
							DbUtils.closeQuietly(statement4, rset4);
							statement4 = con4.prepareStatement("SELECT * FROM banned_ips WHERE ip=? LIMIT 1");
							statement4.setString(1, lastIp);
							rset4 = statement4.executeQuery();
							if(rset4.next())
								activeChar.sendMessage(name2 + " banned by IP: " + lastIp);
							else
								activeChar.sendMessage("No IP ban for char: " + name2);
						}
					}
					catch(Exception ex5)
					{}
					finally
					{
						DbUtils.closeQuietly(con4, statement4, rset4);
					}
					if(AutoBan.isBanned(id))
						activeChar.sendMessage(name2 + " is banned.");
					else
						activeChar.sendMessage("No ban for char: " + name2);
				}
			}
			else if(command.startsWith("admin_unlockip"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				if(st.countTokens() > 1)
				{
					st.nextToken();
					final String name2 = st.nextToken();
					final String account2 = PlayerManager.getAccNameByName(name2);
					if(account2.isEmpty())
					{
						activeChar.sendMessage("Character " + name2 + " not exist.");
						return true;
					}
					AuthServerCommunication.getInstance().sendPacket(new ChangeAllowedIp(account2, ""));
					activeChar.sendMessage("Unlocked IP for char: " + name2 + " | account: " + account2);
				}
			}
			else if(command.startsWith("admin_unlockhwid"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				if(st.countTokens() > 1)
				{
					st.nextToken();
					String name2 = st.nextToken();
					final Player p4 = World.getPlayer(name2);
					if(p4 != null)
					{
						p4.lockChar1 = "";
						p4.lockChar2 = "";
						mysql.set("DELETE FROM `hwid_locks` WHERE `obj_Id`=" + p4.getObjectId() + " LIMIT 1");
						AuthServerCommunication.getInstance().sendPacket(new ChangeAllowedHwid(p4.getAccountName(), ""));
						activeChar.sendMessage("Unlocked hwid for online char: " + p4.getName() + " | account: " + p4.getAccountName());
						return true;
					}
					final int id2 = PlayerManager.getObjectIdByName(name2);
					if(id2 <= 0)
					{
						activeChar.sendMessage("Player " + name2 + " not exist.");
						return true;
					}
					name2 = PlayerManager.getNameByObjectId(id2);
					final String account4 = PlayerManager.getAccNameByName(name2);
					mysql.set("DELETE FROM `hwid_locks` WHERE `obj_Id`=" + id2 + " LIMIT 1");
					AuthServerCommunication.getInstance().sendPacket(new ChangeAllowedHwid(account4, ""));
					activeChar.sendMessage("Unlocked hwid for char: " + name2 + " | account: " + account4);
				}
			}
			else if(command.startsWith("admin_shwid"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				if(st.countTokens() > 1)
				{
					st.nextToken();
					String name2 = st.nextToken();
					final int id = PlayerManager.getObjectIdByName(name2);
					if(id <= 0)
					{
						activeChar.sendMessage("Player " + name2 + " not exist.");
						return true;
					}
					final String hwid5 = PlayerManager.getLastHWIDByName(name2);
					name2 = PlayerManager.getNameByObjectId(id);
					activeChar.sendMessage(name2 + " hwid: " + hwid5);
				}
				else if(activeChar.getTarget() != null && activeChar.getTarget().isPlayer())
				{
					final Player p3 = (Player) activeChar.getTarget();
					activeChar.sendMessage(p3.getName() + " hwid: " + p3.getHWID());
				}
			}
			else if(command.startsWith("admin_cban"))
				AdminHelpPage.showHelpPage(activeChar, "cban.htm");
			else if(command.startsWith("admin_spam"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				if(st.countTokens() > 1)
				{
					st.nextToken();
					String name2 = st.nextToken();
					final Player player3 = World.getPlayer(name2);
					if(player3 != null)
					{
						if(SpamFilter.getInstance().isSpamer(player3.getAccountName()))
						{
							activeChar.sendMessage(player3.getName() + " already spamer.");
							return true;
						}
						SpamFilter.getInstance().setBlockTime(player3.getAccountName(), -1L);
						mysql.set("REPLACE INTO `account_spamers` (`account`, `expire`) VALUES ('" + player3.getAccountName() + "'," + -1 + ")");
						activeChar.sendMessage(player3.getName() + " added to spamers.");
						Log.addLog("(" + player3.getAccountName() + ")" + player3.toString() + " - add by GM: " + activeChar.toString(), "spam");
						return true;
					}
					else
					{
						final int id2 = PlayerManager.getObjectIdByName(name2);
						if(id2 <= 0)
						{
							activeChar.sendMessage("Player " + name2 + " not exist.");
							return true;
						}
						name2 = PlayerManager.getNameByObjectId(id2);
						final String account4 = PlayerManager.getAccNameByName(name2);
						if(SpamFilter.getInstance().isSpamer(account4))
						{
							activeChar.sendMessage(name2 + " already spamer.");
							return true;
						}
						SpamFilter.getInstance().setBlockTime(account4, -1L);
						mysql.set("REPLACE INTO `account_spamers` (`account`, `expire`) VALUES ('" + account4 + "'," + -1 + ")");
						activeChar.sendMessage(name2 + " added to spamers in offline.");
						Log.addLog("(" + account4 + ")" + name2 + "[" + id2 + "] - add by GM: " + activeChar.toString(), "spam");
					}
				}
				else if(activeChar.getTarget() != null && activeChar.getTarget().isPlayer())
				{
					final Player player4 = (Player) activeChar.getTarget();
					if(SpamFilter.getInstance().isSpamer(player4.getAccountName()))
					{
						activeChar.sendMessage(player4.getName() + " already spamer.");
						return true;
					}
					SpamFilter.getInstance().setBlockTime(player4.getAccountName(), -1L);
					mysql.set("REPLACE INTO `account_spamers` (`account`, `expire`) VALUES ('" + player4.getAccountName() + "'," + -1 + ")");
					activeChar.sendMessage(player4.getName() + " added to spamers.");
					Log.addLog("(" + player4.getAccountName() + ")" + player4.toString() + " - add by GM: " + activeChar.toString(), "spam");
				}
			}
			else if(command.startsWith("admin_acc_spam"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				if(st.countTokens() > 1)
				{
					st.nextToken();
					final String account5 = st.nextToken();
					if(PlayerManager.accountCharNumber(account5) < 1)
					{
						activeChar.sendMessage("Account " + account5 + " not exist or has no chars.");
						return true;
					}
					if(SpamFilter.getInstance().isSpamer(account5))
					{
						activeChar.sendMessage("Account " + account5 + " already in spamers.");
						return true;
					}
					long time6 = -1L;
					if(st.hasMoreTokens())
						try
						{
							time6 = Integer.parseInt(st.nextToken()) * 60000L + System.currentTimeMillis();
						}
						catch(Exception e3)
						{
							time6 = -1L;
						}
					SpamFilter.getInstance().setBlockTime(account5, time6);
					mysql.set("REPLACE INTO `account_spamers` (`account`, `expire`) VALUES ('" + account5 + "'," + time6 + ")");
					final String period = time6 < 0L ? "Infinite" : TimeUtils.toSimpleFormat(time6);
					activeChar.sendMessage("Account " + account5 + " added to spamers | period: " + period);
					Log.addLog("(" + account5 + ") - add by GM: " + activeChar.toString() + " | period: " + period, "spam");
				}
			}
			else if(command.startsWith("admin_unspam"))
			{
				final StringTokenizer st = new StringTokenizer(command);
				if(st.countTokens() > 1)
				{
					st.nextToken();
					String name2 = st.nextToken();
					final Player player3 = World.getPlayer(name2);
					if(player3 != null)
					{
						if(!SpamFilter.getInstance().isSpamer(player3.getAccountName()))
						{
							activeChar.sendMessage(player3.getName() + " not spamer.");
							return true;
						}
						SpamFilter.getInstance().setBlockTime(player3.getAccountName(), 0L);
						mysql.set("DELETE FROM `account_spamers` WHERE `account`='" + player3.getAccountName() + "' LIMIT 1");
						activeChar.sendMessage(player3.getName() + " removed from spamers.");
						Log.addLog("(" + player3.getAccountName() + ")" + player3.toString() + " - remove by GM: " + activeChar.toString(), "spam");
						return true;
					}
					else
					{
						final int id2 = PlayerManager.getObjectIdByName(name2);
						if(id2 <= 0)
						{
							activeChar.sendMessage("Player " + name2 + " not exist.");
							return true;
						}
						name2 = PlayerManager.getNameByObjectId(id2);
						final String account4 = PlayerManager.getAccNameByName(name2);
						if(!SpamFilter.getInstance().isSpamer(account4))
						{
							activeChar.sendMessage(name2 + " not spamer.");
							return true;
						}
						SpamFilter.getInstance().setBlockTime(account4, 0L);
						mysql.set("DELETE FROM `account_spamers` WHERE `account`='" + account4 + "' LIMIT 1");
						activeChar.sendMessage(name2 + " removed from spamers in offline.");
						Log.addLog("(" + account4 + ")" + name2 + "[" + id2 + "] - remove by GM: " + activeChar.toString(), "spam");
					}
				}
				else if(activeChar.getTarget() != null && activeChar.getTarget().isPlayer())
				{
					final Player player4 = (Player) activeChar.getTarget();
					if(!SpamFilter.getInstance().isSpamer(player4.getAccountName()))
					{
						activeChar.sendMessage(player4.getName() + " not spamer.");
						return true;
					}
					SpamFilter.getInstance().setBlockTime(player4.getAccountName(), 0L);
					mysql.set("DELETE FROM `account_spamers` WHERE `account`='" + player4.getAccountName() + "' LIMIT 1");
					activeChar.sendMessage(player4.getName() + " removed from spamers.");
					Log.addLog("(" + player4.getAccountName() + ")" + player4.toString() + " - remove by GM: " + activeChar.toString(), "spam");
				}
			}
			else if(command.startsWith("admin_clear_spamers"))
			{
				SpamFilter.getInstance().clear();
				mysql.set("DELETE FROM `account_spamers`");
				activeChar.sendMessage("All statuses of spamers removed.");
				Log.addLog("All accounts removed from spamers by GM: " + activeChar.toString(), "spam");
			}
		}
		return true;
	}

	private static boolean isSpamer(final int id)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT `value` FROM `character_variables` WHERE `obj_id`=" + id + " AND `type`='user-var' AND `name`='spamer' LIMIT 1");
			rset = statement.executeQuery();
			if(rset.next() && rset.getString("value").equals("true"))
				return true;
		}
		catch(Exception ex)
		{}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return false;
	}

	private boolean tradeBan(final StringTokenizer st, final Player activeChar)
	{
		if(activeChar.getTarget() == null || !activeChar.getTarget().isPlayer())
			return false;
		st.nextToken();
		final Player targ = (Player) activeChar.getTarget();
		long days = -1L;
		long time = -1L;
		if(st.hasMoreTokens())
		{
			days = Long.parseLong(st.nextToken());
			time = days * 24L * 60L * 60L * 1000L + System.currentTimeMillis();
		}
		targ.setVar("tradeBan", String.valueOf(time));
		final String msg = "\u0417\u0430\u0431\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u0430\u043d\u0430 \u0442\u043e\u0440\u0433\u043e\u0432\u043b\u044f \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0443 " + targ.getName() + (days == -1L ? " \u043d\u0430 \u0431\u0435\u0441\u0441\u0440\u043e\u0447\u043d\u044b\u0439 \u043f\u0435\u0440\u0438\u043e\u0434." : " \u043d\u0430 " + days + " \u0434\u043d.");
		Log.add(targ.toString() + ":" + days + tradeToString(targ, targ.getPrivateStoreType()), "tradeBan", activeChar);
		if(targ.isInOfflineMode())
		{
			targ.setOfflineMode(false);
			targ.kick(false);
		}
		else if(targ.isInStoreMode())
		{
			targ.setPrivateStoreType((short) 0);
			targ.broadcastUserInfo(true);
			targ.getBuyList().clear();
		}
		Announcements.getInstance().announceToAll(msg);
		return true;
	}

	private static String tradeToString(final Player targ, final int trade)
	{
		switch(trade)
		{
			case 3:
			{
				final Collection<TradeItem> list = targ.getBuyList();
				if(list == null || list.isEmpty())
					return "";
				String ret = ":buy:";
				for(final TradeItem i : list)
					ret = ret + i.getItemId() + ";" + i.getCount() + ";" + i.getOwnersPrice() + ":";
				return ret;
			}
			case 1:
			case 8:
			{
				final Collection<TradeItem> list = targ.getSellList();
				if(list == null || list.isEmpty())
					return "";
				String ret = ":sell:";
				for(final TradeItem i : list)
					ret = ret + i.getItemId() + ";" + i.getCount() + ";" + i.getOwnersPrice() + ":";
				return ret;
			}
			case 5:
			{
				final Collection<ManufactureItem> list = targ.getCreateList().getList();
				if(list == null || list.isEmpty())
					return "";
				String ret = ":mf:";
				for(final ManufactureItem j : list)
					ret = ret + j.getRecipeId() + ";" + j.getCost() + ":";
				return ret;
			}
			default:
			{
				return "";
			}
		}
	}

	private boolean tradeUnban(final StringTokenizer st, final Player activeChar)
	{
		if(activeChar.getTarget() == null || !activeChar.getTarget().isPlayer())
			return false;
		final Player targ = (Player) activeChar.getTarget();
		targ.unsetVar("tradeBan");
		Announcements.getInstance().announceToAll("\u0420\u0430\u0437\u0431\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u0430\u043d\u0430 \u0442\u043e\u0440\u0433\u043e\u0432\u043b\u044f \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0443 " + targ.getName() + ".");
		Log.add(activeChar + " \u0440\u0430\u0437\u0431\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u0430\u043b \u0442\u043e\u0440\u0433\u043e\u0432\u043b\u044e \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0443 " + targ.toString() + ".", "tradeBan", activeChar);
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminOldBan._adminCommands;
	}

	@Override
	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		AdminOldBan._adminCommands = new String[] {
				"admin_old_ban",
				"admin_old_unban",
				"admin_chatban",
				"admin_ckarma",
				"admin_cban",
				"admin_chatunban",
				"admin_acc_ban",
				"admin_accsbanhwid",
				"admin_acc_unban",
				"admin_trade_ban",
				"admin_trade_unban",
				"admin_jail",
				"admin_unjail",
				"admin_hban",
				"admin_hnban",
				"admin_unhban",
				"admin_lgban",
				"admin_isban",
				"admin_unlockip",
				"admin_unlockhwid",
				"admin_shwid",
				"admin_spam",
				"admin_acc_spam",
				"admin_unspam",
				"admin_clear_spamers" };
	}
}
