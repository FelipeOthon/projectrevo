package commands.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.Announcements;
import l2s.gameserver.Bonus;
import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.database.mysql;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.PlayerManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.ExPCCafePointInfo;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.network.l2.s2c.PartySmallWindowUpdate;
import l2s.gameserver.network.l2.s2c.SkillList;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.tables.SkillTree;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.Log;
import l2s.gameserver.utils.VariationUtils;
import services.SkillLS;

public class AdminEditChar implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(activeChar.getPlayerAccess().CanRename)
			if(command.startsWith("admin_settitle"))
				try
				{
					final String val = command.substring(15);
					final GameObject target = activeChar.getTarget();
					Player player = null;
					if(target == null || !target.isPlayer())
						return false;
					player = (Player) target;
					player.setTitle(val);
					player.sendMessage("Your title has been changed by a GM");
					player.sendChanges();
					Log.addLog(activeChar.toString() + " change title for player " + player.toString() + " to " + val, "gm_actions");
				}
				catch(StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("You need to specify the new title.");
				}
			else if(command.startsWith("admin_setname"))
				try
				{
					final String val = command.substring(14);
					final GameObject target = activeChar.getTarget();
					if(target == null || !target.isPlayer())
						return false;
					final Player player = (Player) target;
					if(PlayerManager.getObjectIdByName(val) > 0)
					{
						activeChar.sendMessage("Name already exist.");
						return false;
					}
					final String oldName = player.getName();
					player.reName(val, true);
					Log.addLog("Character " + oldName + " renamed to " + val + " by GM " + activeChar.getName(), "renames");
					Log.addLog(activeChar.toString() + " set name for player " + oldName + " to " + val, "gm_actions");
					if(player.isInParty())
						player.getParty().broadcastToPartyMembers(player, new PartySmallWindowUpdate(player));
				}
				catch(StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("You need to specify the new name.");
				}
		if(!activeChar.getPlayerAccess().CanEditChar && !activeChar.getPlayerAccess().CanViewChar)
			return false;
		if(command.equals("admin_current_player"))
			showCharacterList(activeChar, null);
		else if(command.startsWith("admin_character_list"))
			try
			{
				final String val = command.substring(21);
				final Player target2 = GameObjectsStorage.getPlayer(val);
				showCharacterList(activeChar, target2);
			}
			catch(StringIndexOutOfBoundsException ex)
			{}
		else if(command.startsWith("admin_show_characters"))
			try
			{
				final String val = command.substring(22);
				final int page = Integer.parseInt(val);
				listCharacters(activeChar, page);
			}
			catch(StringIndexOutOfBoundsException ex2)
			{}
		else if(command.startsWith("admin_find_character"))
			try
			{
				final String val = command.substring(21);
				findCharacter(activeChar, val.toLowerCase());
			}
			catch(StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("You didn't enter a character name to find.");
				listCharacters(activeChar, 0);
			}
		else if(command.startsWith("admin_find_char_ip"))
			try
			{
				final String[] wordList = command.split(" ");
				findCharacterIpHwid(activeChar, Integer.parseInt(wordList[1]), wordList[2], false);
			}
			catch(Exception e2)
			{
				activeChar.sendMessage("You didn't enter a character ip to find.");
				listCharacters(activeChar, 0);
			}
		else if(command.startsWith("admin_find_char_hwid"))
			try
			{
				final String[] wordList = command.split(" ");
				findCharacterIpHwid(activeChar, Integer.parseInt(wordList[1]), wordList[2], true);
			}
			catch(Exception e2)
			{
				activeChar.sendMessage("You didn't enter a character hwid to find.");
				listCharacters(activeChar, 0);
			}
		else
		{
			if(!activeChar.getPlayerAccess().CanEditChar)
				return false;
			if(command.equals("admin_edit_character"))
				editCharacter(activeChar);
			else if(command.equals("admin_character_actions"))
				showCharacterActions(activeChar);
			else if(command.equals("admin_nokarma"))
				setTargetKarma(activeChar, 0);
			else if(command.startsWith("admin_setkarma"))
				try
				{
					final String val = command.substring(15);
					final int karma = Integer.parseInt(val);
					setTargetKarma(activeChar, karma);
				}
				catch(StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("Please specify new karma value.");
				}
			else if(command.startsWith("admin_save_modifications"))
				try
				{
					final String val = command.substring(24);
					adminModifyCharacter(activeChar, val);
					Log.addLog(activeChar.toString() + " save modifications " + val, "gm_actions");
				}
				catch(StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("Error while modifying character.");
					listCharacters(activeChar, 0);
				}
			else if(command.equals("admin_rec"))
			{
				final GameObject target3 = activeChar.getTarget();
				Player player2 = null;
				if(target3 == null || !target3.isPlayer())
					return false;
				player2 = (Player) target3;
				player2.setRecomHave(player2.getRecomHave() + 1);
				player2.sendMessage("You have been recommended by a GM");
				player2.sendChanges();
				Log.addLog(activeChar.toString() + " recommend player " + player2.toString() + " + 1", "gm_actions");
			}
			else if(command.startsWith("admin_rec"))
				try
				{
					final String val = command.substring(10);
					final int recVal = Integer.parseInt(val);
					final GameObject target4 = activeChar.getTarget();
					Player player3 = null;
					if(target4 == null || !target4.isPlayer())
						return false;
					player3 = (Player) target4;
					player3.setRecomHave(player3.getRecomHave() + recVal);
					player3.sendMessage("You have been recommended by a GM");
					player3.sendChanges();
					Log.addLog(activeChar.toString() + " recommend player " + player3.toString() + " + " + recVal, "gm_actions");
				}
				catch(NumberFormatException e3)
				{
					activeChar.sendMessage("Command format is //rec <number>");
				}
			else if(command.startsWith("admin_setclass"))
			{
				String val = null;
				try
				{
					val = command.substring(15);
				}
				catch(Exception e4)
				{
					activeChar.sendMessage("\u041f\u0440\u0438 \u0432\u044b\u0437\u043e\u0432\u0435 \u043a\u043e\u043c\u0430\u043d\u0434\u044b //setclass \u0412\u044b \u0443\u043a\u0430\u0437\u0430\u043b\u0438 \u043d\u0435\u0432\u0435\u0440\u043d\u044b\u0439 \u043f\u0430\u0440\u0430\u043c\u0435\u0442\u0440 CLASS_ID");
					return false;
				}
				final short classId = Short.parseShort(val);
				final GameObject target4 = activeChar.getTarget();
				Player player3 = null;
				if(target4 == null || !target4.isPlayer())
				{
					activeChar.sendMessage("\u041f\u0440\u0438 \u0432\u044b\u0437\u043e\u0432\u0435 \u043a\u043e\u043c\u0430\u043d\u0434\u044b //setclass \u0446\u0435\u043b\u044c \u043d\u0435 \u0432\u044b\u0431\u0440\u0430\u043d\u0430 \u043b\u0438\u0431\u043e \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0435\u043c");
					return false;
				}
				player3 = (Player) target4;
				player3.sendMessage("\u0412\u0430\u0448 \u043a\u043b\u0430\u0441\u0441 \u0431\u044b\u043b \u0438\u0437\u043c\u0435\u043d\u0435\u043d \u0413\u0435\u0439\u043c \u041c\u0430\u0441\u0442\u0435\u0440\u043e\u043c");
				player3.setClassId(classId, true, false);
				player3.sendChanges();
			}
			else if(command.startsWith("admin_setsubclass"))
			{
				final GameObject target3 = activeChar.getTarget();
				if(target3 == null || !target3.isPlayer())
				{
					activeChar.sendPacket(new SystemMessage(242));
					return false;
				}
				final Player player2 = (Player) target3;
				final StringTokenizer st = new StringTokenizer(command);
				if(st.countTokens() > 1)
				{
					st.nextToken();
					final short classId2 = Short.parseShort(st.nextToken());
					if(!player2.addSubClass(classId2, true))
					{
						activeChar.sendMessage("\u0421\u0430\u0431 \u043a\u043b\u0430\u0441\u0441 \u043d\u0435 \u043c\u043e\u0436\u0435\u0442 \u0431\u044b\u0442\u044c \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d");
						return false;
					}
					player2.sendPacket(Msg.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS);
				}
			}
			else if(command.startsWith("admin_sethero"))
			{
				final String[] wordList = command.split(" ");
				final GameObject target = activeChar.getTarget();
				Player player;
				if(wordList.length > 1 && wordList[1] != null)
				{
					player = GameObjectsStorage.getPlayer(wordList[1]);
					if(player == null)
					{
						activeChar.sendMessage("Character " + wordList[1] + " not found in game.");
						return false;
					}
				}
				else
				{
					if(target == null || !target.isPlayer())
					{
						activeChar.sendMessage("You must specify the name or target character.");
						return false;
					}
					player = (Player) target;
				}
				if(player.isHero())
				{
					player.setHero(false);
					Hero.removeSkills(player);
				}
				else
				{
					player.setHero(true);
					Hero.addSkills(player);
				}
				player.sendPacket(new SkillList(player));
				if(player.isHero())
					Announcements.getInstance().announceToAll(player.getName() + " \u0441\u0442\u0430\u043b \u0433\u0435\u0440\u043e\u0435\u043c \u0434\u043e \u0440\u0435\u043b\u043e\u0433\u0430.");
				player.sendMessage("Admin changed your hero status.");
				player.broadcastUserInfo(true);
				if(player.isHero())
					Log.addLog(activeChar.toString() + " add hero status to player " + player.toString(), "gm_actions");
			}
			else if(command.startsWith("admin_setnoble"))
			{
				final String[] wordList = command.split(" ");
				final GameObject target = activeChar.getTarget();
				Player player;
				if(wordList.length > 1 && wordList[1] != null)
				{
					player = GameObjectsStorage.getPlayer(wordList[1]);
					if(player == null)
					{
						activeChar.sendMessage("Character " + wordList[1] + " not found in game.");
						return false;
					}
				}
				else
				{
					if(target == null || !target.isPlayer())
					{
						activeChar.sendMessage("You must specify the name or target character.");
						return false;
					}
					player = (Player) target;
				}
				if(player.isNoble())
				{
					activeChar.sendMessage(player.getName() + " already Nobless.");
					return false;
				}
				if(ClassId.values()[player.getBaseClassId()].getLevel() < 3)
				{
					activeChar.sendMessage("Need second class first.");
					return false;
				}
				player.setNoble();
				player.sendMessage("Admin changed your noble status, now you are Nobless.");
				activeChar.sendMessage("Noble status added to " + player.getName());
				Log.addLog(activeChar.toString() + " add noble status to player " + player.toString(), "gm_actions");
			}
			else if(command.startsWith("admin_delnoble"))
			{
				final String[] wordList = command.split(" ");
				final GameObject target = activeChar.getTarget();
				Player player;
				if(wordList.length > 1 && wordList[1] != null)
				{
					player = GameObjectsStorage.getPlayer(wordList[1]);
					if(player == null)
					{
						activeChar.sendMessage("Character " + wordList[1] + " not found in game.");
						return false;
					}
				}
				else
				{
					if(target == null || !target.isPlayer())
					{
						activeChar.sendMessage("You must specify the name or target character.");
						return false;
					}
					player = (Player) target;
				}
				if(!player.isNoble())
				{
					activeChar.sendMessage(player.getName() + " not Nobless.");
					return false;
				}
				Olympiad.removeNoble(player);
				player.setNoble(false);
				player.sendMessage("Admin changed your noble status, now you are not Nobless.");
				activeChar.sendMessage("Noble status removed from " + player.getName());
				Log.addLog(activeChar.toString() + " remove noble status from player " + player.toString(), "gm_actions");
			}
			else if(command.startsWith("admin_setsex"))
			{
				final GameObject target3 = activeChar.getTarget();
				Player player2 = null;
				if(target3 == null || !target3.isPlayer())
					return false;
				player2 = (Player) target3;
				player2.changeSex();
				player2.sendMessage("Your gender has been changed by a GM");
				player2.broadcastUserInfo(true);
				Log.addLog(activeChar.toString() + " change gender to player " + player2.toString(), "gm_actions");
			}
			else if(command.startsWith("admin_setcolor"))
				try
				{
					final String val = command.substring(15);
					final GameObject target = activeChar.getTarget();
					Player player = null;
					if(target == null || !target.isPlayer())
						return false;
					player = (Player) target;
					player.setNameColor(Integer.decode("0x" + val), true);
					player.sendMessage("Your name color has been changed by a GM");
					player.broadcastUserInfo(true);
					Log.addLog(activeChar.toString() + " change name color for player " + player.toString(), "gm_actions");
				}
				catch(StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("You need to specify the new color.");
				}
			else if(command.startsWith("admin_settcolor"))
				try
				{
					final String val = command.substring(16);
					final GameObject target = activeChar.getTarget();
					Player player = null;
					if(target == null || !target.isPlayer())
						return false;
					player = (Player) target;
					player.setTitleColor(Integer.decode("0x" + val));
					player.sendMessage("Your title color has been changed by a GM");
					player.broadcastUserInfo(true);
					Log.addLog(activeChar.toString() + " change title color for player " + player.toString(), "gm_actions");
				}
				catch(StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("You need to specify the new color.");
				}
			else if(command.startsWith("admin_add_exp_sp_to_character"))
				addExpSp(activeChar);
			else if(command.startsWith("admin_add_exp_sp"))
				try
				{
					final String val = command.substring(16);
					adminAddExpSp(activeChar, val);
				}
				catch(StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("Error while adding Exp-Sp.");
				}
			else if(command.startsWith("admin_givebonus"))
			{
				final GameObject target3 = activeChar.getTarget();
				if(target3 != null && target3.isPlayer())
				{
					final Player p = (Player) target3;
					if(!p.isConnected())
					{
						activeChar.sendMessage(p.getName() + " not online.");
						return false;
					}
					if(p.getNetConnection().getBonusExpire() >= System.currentTimeMillis() / 1000L)
					{
						activeChar.sendMessage(p.getName() + " already has bonus.");
						return false;
					}
					final StringTokenizer st = new StringTokenizer(command);
					if(st.countTokens() > 2)
					{
						st.nextToken();
						float rate = 0.0f;
						int hours = 0;
						try
						{
							rate = Float.parseFloat(st.nextToken());
							hours = Integer.parseInt(st.nextToken());
						}
						catch(Exception e5)
						{
							activeChar.sendMessage("Specify a valid value.");
							return false;
						}
						Bonus.giveBonus(p, rate, hours);
						activeChar.sendMessage("Premium: " + p.getName() + " | Rate: " + rate + " | Hours: " + hours);
						Log.addLog(activeChar.toString() + " give bonus Rate: " + rate + " | Hours: " + hours + " for player " + p.toString(), "gm_actions");
					}
					else
						activeChar.sendMessage("Usage: //givebonus <rate> <hours>");
				}
			}
			else if(command.startsWith("admin_givepb"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				if(st2.countTokens() > 1)
				{
					st2.nextToken();
					long hours2 = 0L;
					try
					{
						hours2 = Long.parseLong(st2.nextToken());
					}
					catch(Exception e6)
					{
						activeChar.sendMessage("Specify a valid value.");
						return false;
					}
					final GameObject target5 = activeChar.getTarget();
					if(target5 == null || !target5.isPlayer())
					{
						activeChar.sendPacket(Msg.INCORRECT_TARGET);
						return false;
					}
					final Player p2 = (Player) target5;
					Bonus.givePB(p2, hours2);
					activeChar.sendMessage("PA to buff: " + p2.getName() + " | Hours: " + hours2);
					Log.addLog(activeChar.toString() + " give premium access to buff on " + hours2 + " hours to player: " + p2.toString(), "gm_actions");
				}
				else
					activeChar.sendMessage("Usage: //givepb <hours>");
			}
			else if(command.startsWith("admin_givehs"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				if(st2.countTokens() > 1)
				{
					st2.nextToken();
					long hours2 = 0L;
					try
					{
						hours2 = Long.parseLong(st2.nextToken());
					}
					catch(Exception e6)
					{
						activeChar.sendMessage("Specify a valid value.");
						return false;
					}
					final GameObject target5 = activeChar.getTarget();
					if(target5 == null || !target5.isPlayer())
					{
						activeChar.sendPacket(Msg.INCORRECT_TARGET);
						return false;
					}
					final Player p2 = (Player) target5;
					Bonus.giveHS(p2, hours2);
					activeChar.sendMessage("Hero status: " + p2.getName() + " | Hours: " + hours2);
					Log.addLog(activeChar.toString() + " give hero status on " + hours2 + " hours to player: " + p2.toString(), "gm_actions");
				}
				else
					activeChar.sendMessage("Usage: //givehs <hours>");
			}
			else if(command.startsWith("admin_exchange_names"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				if(st2.countTokens() > 2)
				{
					st2.nextToken();
					final String name1 = st2.nextToken();
					String name2 = st2.nextToken();
					final int id1 = PlayerManager.getObjectIdByName(name1);
					if(id1 <= 0)
					{
						activeChar.sendMessage(name1 + " don't exist.");
						return false;
					}
					final int id2 = PlayerManager.getObjectIdByName(name2);
					if(id2 <= 0)
					{
						activeChar.sendMessage(name2 + " don't exist.");
						return false;
					}
					final Player p3 = GameObjectsStorage.getPlayer(id1);
					final Player p4 = GameObjectsStorage.getPlayer(id2);
					name2 = "l" + name1;
					if(p3 != null)
						p3.reName(name2, true);
					else
					{
						PlayerManager.saveCharNameToDB(id1, name2);
						Olympiad.changeNobleName(id1, name2);
						Hero.changeHeroName(id1, name2);
					}
					if(p4 != null)
						p4.reName(name1, true);
					else
					{
						PlayerManager.saveCharNameToDB(id2, name1);
						Olympiad.changeNobleName(id2, name1);
						Hero.changeHeroName(id2, name1);
					}
					activeChar.sendMessage("Names exchanged successfully.");
				}
				else
					activeChar.sendMessage("Usage: //exchange_names <name1> <name2>");
			}
			else if(command.startsWith("admin_setcphpmp"))
			{
				final GameObject target3 = activeChar.getTarget();
				if(target3 != null && target3.isPlayer())
				{
					final Player p = (Player) target3;
					final StringTokenizer st = new StringTokenizer(command);
					if(st.countTokens() > 3)
					{
						st.nextToken();
						try
						{
							int cp = Integer.parseInt(st.nextToken());
							int hp = Integer.parseInt(st.nextToken());
							int mp = Integer.parseInt(st.nextToken());
							if(cp < 1)
								cp = p.getMaxCp();
							if(hp < 1)
								hp = p.getMaxHp();
							if(mp < 1)
								mp = p.getMaxMp();
							p.setCurrentHpMp(hp, mp, false);
							p.setCurrentCp(cp);
							activeChar.sendMessage("CP HP MP for: " + p.getName());
						}
						catch(Exception e6)
						{
							activeChar.sendMessage("Usage: //setcphpmp <CP> <HP> <MP>");
							return false;
						}
					}
				}
			}
			else if(command.startsWith("admin_setgiran"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				if(st2.countTokens() > 1)
				{
					st2.nextToken();
					String name3 = st2.nextToken();
					final Player p5 = GameObjectsStorage.getPlayer(name3);
					if(p5 != null)
					{
						name3 = p5.getName();
						p5.kick(true);
					}
					else if(PlayerManager.getObjectIdByName(name3) <= 0)
					{
						activeChar.sendMessage("Player " + name3 + " not exist.");
						return false;
					}
					mysql.set("UPDATE `characters` SET `x`='82520', `y`='148615', `z`='-3470', `heading`='0' WHERE `char_name`='" + name3 + "' LIMIT 1");
					activeChar.sendMessage("Done for: " + name3);
				}
				else
					activeChar.sendMessage("Usage: //setgiran <char_name>");
			}
			else if(command.startsWith("admin_clanonline"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				Player p = null;
				if(st2.countTokens() > 1)
				{
					st2.nextToken();
					final String name4 = st2.nextToken();
					p = GameObjectsStorage.getPlayer(name4);
				}
				else
				{
					final GameObject target4 = activeChar.getTarget();
					if(target4 != null && target4.isPlayer())
						p = (Player) target4;
				}
				if(p == null)
				{
					activeChar.sendMessage("Player not found.");
					return true;
				}
				if(p.getClan() == null)
				{
					activeChar.sendMessage("Player is not in clan.");
					return true;
				}
				List<String> hwids = new ArrayList<String>();
				for(final Player clanMember : p.getClan().getOnlineMembers(0))
					if(!clanMember.isInOfflineMode() && clanMember.isConnected())
						if(!clanMember.getHWID().equals(""))
							if(!hwids.contains(clanMember.getHWID()))
								hwids.add(clanMember.getHWID());
				activeChar.sendMessage("\u041e\u043d\u043b\u0430\u0439\u043d \u043a\u043b\u0430\u043d\u0430 " + p.getClan().getName() + ": " + hwids.size());
				hwids = null;
			}
			else if(command.startsWith("admin_clansize"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				Player p = null;
				if(st2.countTokens() > 1)
				{
					st2.nextToken();
					final String name4 = st2.nextToken();
					p = GameObjectsStorage.getPlayer(name4);
				}
				else
				{
					final GameObject target4 = activeChar.getTarget();
					if(target4 != null && target4.isPlayer())
						p = (Player) target4;
				}
				if(p == null)
				{
					activeChar.sendMessage("Player not found.");
					return true;
				}
				if(p.getClan() == null)
				{
					activeChar.sendMessage("Player is not in clan.");
					return true;
				}
				List<String> hwids = new ArrayList<String>();
				Connection con = null;
				PreparedStatement statement = null;
				ResultSet rset = null;
				try
				{
					con = DatabaseFactory.getInstance().getConnection();
					statement = con.prepareStatement("SELECT last_hwid FROM characters WHERE clanid=?");
					statement.setInt(1, p.getClanId());
					rset = statement.executeQuery();
					while(rset.next())
					{
						final String hwid = rset.getString("last_hwid");
						if(!hwid.equals("") && !hwids.contains(hwid))
							hwids.add(hwid);
					}
				}
				catch(Exception ex3)
				{}
				finally
				{
					DbUtils.closeQuietly(con, statement, rset);
				}
				activeChar.sendMessage("\u041e\u043d\u043b\u0430\u0439\u043d \u043a\u043b\u0430\u043d\u0430 " + p.getClan().getName() + ": " + hwids.size());
				hwids = null;
			}
			else if(command.startsWith("admin_droppk"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				Player p = null;
				if(st2.countTokens() > 1)
				{
					st2.nextToken();
					final String name4 = st2.nextToken();
					p = GameObjectsStorage.getPlayer(name4);
				}
				else
				{
					final GameObject target4 = activeChar.getTarget();
					if(target4 != null && target4.isPlayer())
						p = (Player) target4;
				}
				if(p == null)
				{
					activeChar.sendMessage("\u0418\u0433\u0440\u043e\u043a \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d.");
					return true;
				}
				if(p.getVarBoolean("NoDropPK"))
				{
					p.unsetVar("NoDropPK");
					activeChar.sendMessage("\u0412\u043a\u043b\u044e\u0447\u0435\u043d \u0434\u0440\u043e\u043f \u0441 \u041f\u041a \u0434\u043b\u044f \u0438\u0433\u0440\u043e\u043a\u0430: " + p.getName());
				}
				else
				{
					p.setVar("NoDropPK", "1");
					activeChar.sendMessage("\u0412\u044b\u043a\u043b\u044e\u0447\u0435\u043d \u0434\u0440\u043e\u043f \u0441 \u041f\u041a \u0434\u043b\u044f \u0438\u0433\u0440\u043e\u043a\u0430: " + p.getName());
				}
			}
			else if(command.startsWith("admin_augment"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				final GameObject target = activeChar.getTarget();
				if(target != null && target.isPlayer())
				{
					final Player p5 = (Player) target;
					final ItemInstance wpn = p5.getActiveWeaponInstance();
					if(!SkillLS.check(wpn, activeChar))
						return false;
					try
					{
						st2.nextToken();
						final int variation1Id = Integer.parseInt(st2.nextToken());
						final int variation2Id = Integer.parseInt(st2.nextToken());
						final int variationStoneId = Integer.parseInt(st2.nextToken());

						VariationUtils.setVariation(activeChar, wpn, variationStoneId, variation1Id, variation2Id);

						activeChar.sendMessage("\u041e\u0440\u0443\u0436\u0438\u0435 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 " + p5.getName() + " \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u043e!");
						Log.addLog(activeChar.toString() + " augment " + wpn.getName() + "[" + wpn.getObjectId() + "] for player " + p5.toString(), "gm_actions");
					}
					catch(Exception e7)
					{
						activeChar.sendMessage("Command syntax: //augment <variaion1_id> <variaion2_id> <variation_stone_id>");
						return false;
					}
				}
			}
			else if(command.equals("admin_delaugment"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				final GameObject target = activeChar.getTarget();
				if(target != null && target.isPlayer())
				{
					final Player p5 = (Player) target;
					final ItemInstance wpn = p5.getActiveWeaponInstance();
					if(wpn == null || !wpn.isAugmented())
					{
						activeChar.sendMessage("\u0412 \u0440\u0443\u043a\u0430\u0445 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 \u0434\u043e\u043b\u0436\u043d\u043e \u0431\u044b\u0442\u044c \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u043d\u043e\u0435 \u043e\u0440\u0443\u0436\u0438\u0435.");
						return false;
					}

					VariationUtils.setVariation(activeChar, wpn, 0, 0, 0);

					activeChar.sendMessage("\u0410\u0443\u0433\u043c\u0435\u043d\u0442\u0430\u0446\u0438\u044f \u0441 \u043e\u0440\u0443\u0436\u0438\u044f \u0432 \u0440\u0443\u043a\u0430\u0445 " + p5.getName() + " \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0443\u0434\u0430\u043b\u0435\u043d\u0430.");
					if(activeChar.getObjectId() != p5.getObjectId())
						p5.sendMessage("\u0413\u041c \u0443\u0434\u0430\u043b\u0438\u043b \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0430\u0446\u0438\u044e \u0441 \u0412\u0430\u0448\u0435\u0433\u043e \u043e\u0440\u0443\u0436\u0438\u044f.");
				}
			}
			else if(command.equals("admin_fullcs"))
			{
				final GameObject target3 = activeChar.getTarget();
				Player player2 = null;
				if(target3 == null || !target3.isPlayer())
					return false;
				player2 = (Player) target3;
				if(player2.getClan() == null)
				{
					activeChar.sendMessage("\u042d\u0442\u043e\u0442 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436 \u043d\u0435 \u0432 \u043a\u043b\u0430\u043d\u0435.");
					return false;
				}
				if(player2.getClan().getLevel() < 8)
				{
					activeChar.sendMessage("\u0423\u0440\u043e\u0432\u0435\u043d\u044c \u043a\u043b\u0430\u043d\u0430 \u0434\u043e\u043b\u0436\u0435\u043d \u0431\u044b\u0442\u044c \u043d\u0435 \u043d\u0438\u0436\u0435 8.");
					return false;
				}
				if(SkillTree.getInstance().getPledgeSkillsMax(player2).isEmpty())
				{
					activeChar.sendMessage("\u041d\u0435\u0442 \u043d\u0435\u0438\u0437\u0443\u0447\u0435\u043d\u043d\u044b\u0445 \u0441\u043a\u0438\u043b\u043e\u0432.");
					return false;
				}
				HashMap<Integer, Integer> _skills = SkillTree.getInstance().getPledgeSkillsMax(player2);
				if(!_skills.isEmpty())
					for(final int id3 : _skills.keySet())
					{
						final Skill sk = SkillTable.getInstance().getInfo(id3, _skills.get(id3));
						if(sk == null)
							continue;
						player2.getClan().addNewSkill(sk, true);
					}
				_skills = null;
				activeChar.sendMessage("\u041a\u043b\u0430\u043d\u0443 " + player2.getClan().getName() + " \u0432\u044b\u0434\u0430\u043d\u044b \u0432\u0441\u0435 \u043a\u043b\u0430\u043d\u043e\u0432\u044b\u0435 \u0441\u043a\u0438\u043b\u044b.");
				Log.addLog(activeChar.toString() + " give all clan skills to player " + player2.toString(), "gm_actions");
			}
			else if(command.startsWith("admin_delvar"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				if(st2.countTokens() > 1)
				{
					final GameObject target = activeChar.getTarget();
					Player player = null;
					if(target == null || !target.isPlayer())
						return false;
					player = (Player) target;
					st2.nextToken();
					final String v = st2.nextToken();
					player.unsetVar(v);
					activeChar.sendMessage("unsetVar [" + v + "] for player " + player.getName());
					Log.addLog(activeChar.toString() + " unsetVar [" + v + "] for player " + player.toString(), "gm_actions");
				}
			}
			else if(command.startsWith("admin_setinst"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				if(st2.countTokens() > 1)
				{
					final GameObject target = activeChar.getTarget();
					if(target == null)
						return false;
					st2.nextToken();
					final int instId = Integer.parseInt(st2.nextToken());
					target.setReflectionId(instId);
					activeChar.sendMessage("InstanceId [" + instId + "] for object " + target.getName());
				}
			}
			else if(command.startsWith("admin_add_bang"))
			{
				if(!Config.PCBANG_POINTS_ENABLED)
				{
					activeChar.sendMessage("Error! Pc Bang Points service disabled!");
					return true;
				}
				final StringTokenizer st2 = new StringTokenizer(command);
				if(st2.countTokens() < 2)
				{
					activeChar.sendMessage("Usage: //add_bang <count>");
					return false;
				}
				st2.nextToken();
				final int count = Integer.parseInt(st2.nextToken());
				if(count < 1 || activeChar.getTarget() == null || !activeChar.getTarget().isPlayer())
				{
					activeChar.sendMessage("Usage: //add_bang <count>");
					return false;
				}
				final Player target6 = activeChar.getTarget().getPlayer();
				target6.addPcBangPoints(count, false);
				activeChar.sendMessage("You have added " + count + " Pc Bang Points to " + target6.getName());
			}
			else if(command.startsWith("admin_set_bang"))
			{
				if(!Config.PCBANG_POINTS_ENABLED)
				{
					activeChar.sendMessage("Error! Pc Bang Points service disabled!");
					return true;
				}
				final StringTokenizer st2 = new StringTokenizer(command);
				if(st2.countTokens() < 2)
				{
					activeChar.sendMessage("Usage: //set_bang <count>");
					return false;
				}
				st2.nextToken();
				final int count = Integer.parseInt(st2.nextToken());
				if(count < 1 || activeChar.getTarget() == null || !activeChar.getTarget().isPlayer())
				{
					activeChar.sendMessage("Usage: //set_bang <count>");
					return false;
				}
				final Player target6 = activeChar.getTarget().getPlayer();
				target6.setPcBangPoints(count);
				target6.sendMessage("Your Pc Bang Points count is now " + count);
				target6.sendPacket(new ExPCCafePointInfo(target6, count, 1, 2, 12));
				activeChar.sendMessage("You have set " + target6.getName() + "'s Pc Bang Points to " + count);
			}
			else if(command.startsWith("admin_deleteitems"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				int id4 = 0;
				try
				{
					st2.nextToken();
					id4 = Integer.parseInt(st2.nextToken());
				}
				catch(Exception e8)
				{
					activeChar.sendMessage("Usage: //deleteitems <item_id>");
					return false;
				}
				ItemInstance.deleteItems(id4);
				activeChar.sendMessage("Deleted all items with id: " + id4);
			}
			else if(command.startsWith("admin_delcharitem"))
			{
				final StringTokenizer st2 = new StringTokenizer(command);
				String name3;
				int id5;
				try
				{
					st2.nextToken();
					name3 = st2.nextToken();
					id5 = Integer.parseInt(st2.nextToken());
				}
				catch(Exception e6)
				{
					activeChar.sendMessage("Usage: //delcharitem <name> <item_id> <count>");
					return false;
				}
				int cn;
				try
				{
					cn = Integer.parseInt(st2.nextToken());
				}
				catch(Exception e7)
				{
					cn = 0;
				}
				final ItemTemplate it = ItemTable.getInstance().getTemplate(id5);
				if(it == null)
				{
					activeChar.sendMessage("Item [" + id5 + "] not exist!");
					return false;
				}
				final Player player4 = GameObjectsStorage.getPlayer(name3);
				if(player4 != null)
				{
					if(it.isStackable())
					{
						final ItemInstance item = player4.getInventory().getItemByItemId(id5);
						if(item != null)
							player4.getInventory().destroyItem(item, cn > 0 ? (long) cn : item.getCount(), false);
					}
					else
					{
						int nn = 0;
						for(final ItemInstance item2 : player4.getInventory().getAllItemsById(id5))
						{
							++nn;
							player4.getInventory().destroyItem(item2, 1L, false);
							if(cn > 0 && nn >= cn)
								break;
						}
					}
					activeChar.sendMessage("Deleted item [" + id5 + "] for player " + name3);
				}
				else
				{
					final int oid = PlayerManager.getObjectIdByName(name3);
					if(oid <= 0)
					{
						activeChar.sendMessage("Player " + name3 + " not exist!");
						return false;
					}
					activeChar.sendMessage("Player " + name3 + " not online!");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminEditChar._adminCommands;
	}

	private void listCharacters(final Player activeChar, int page)
	{
		final List<Player> players = new ArrayList<Player>(GameObjectsStorage.getPlayers());
		final int MaxCharactersPerPage = 20;
		int MaxPages = players.size() / MaxCharactersPerPage;
		if(players.size() > MaxCharactersPerPage * MaxPages)
			++MaxPages;
		if(page > MaxPages)
			page = MaxPages;
		final int CharactersStart = MaxCharactersPerPage * page;
		int CharactersEnd = players.size();
		if(CharactersEnd - CharactersStart > MaxCharactersPerPage)
			CharactersEnd = CharactersStart + MaxCharactersPerPage;
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final StringBuffer replyMSG = new StringBuffer("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<br>");
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td width=270>You can find a character by writing his name and</td></tr>");
		replyMSG.append("<tr><td width=270>clicking Find bellow.<br></td></tr>");
		replyMSG.append("</table><br>");
		replyMSG.append("<center><table><tr><td>");
		replyMSG.append("<edit var=\"character_name\" width=130></td><td><button value=\"Find\" action=\"bypass -h admin_find_character $character_name\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
		replyMSG.append("</td></tr></table></center><br>");
		String pages = "<center><table width=300><tr>";
		for(int x = 0; x < MaxPages; ++x)
		{
			final int pagenr = x + 1;
			pages = pages + "<td><a action=\"bypass -h admin_show_characters " + x + "\">" + (x == page ? "<font color=\"ffffff\">" + pagenr + "</font>" : String.valueOf(pagenr)) + "</a></td>";
			if(pagenr == 20)
				break;
		}
		pages += "</tr></table></center>";
		replyMSG.append(pages);
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td width=130>Name:</td><td width=110>Class:</td><td width=20>Lvl:</td></tr>");
		for(int i = CharactersStart; i < CharactersEnd; ++i)
		{
			final Player p = players.get(i);
			replyMSG.append("<tr><td width=130><a action=\"bypass -h admin_character_list " + p.getName() + "\">" + p.getName().replaceAll("<", "-") + "</a></td><td width=110>" + p.getTemplate().className + "</td><td width=20>" + p.getLevel() + "</td></tr>");
		}
		replyMSG.append("</table>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	public static void showCharacterList(final Player activeChar, Player player)
	{
		if(player == null)
		{
			final GameObject target = activeChar.getTarget();
			if(target == null || !target.isPlayer())
				return;
			player = (Player) target;
		}
		else
			activeChar.setTarget(player);
		String clanName = "No Clan";
		if(player.getClan() != null)
			clanName = player.getClan().getName() + "/" + player.getClan().getLevel();
		final NumberFormat df = NumberFormat.getNumberInstance(Locale.ENGLISH);
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(1);
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final StringBuffer replyMSG = new StringBuffer("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_show_characters 0\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td width=100>Account/IP:</td><td>" + player.getAccountName() + "/<font color=\"ffffff\"><a action=\"bypass -h admin_find_char_ip 0 " + player.getIP() + "\">" + player.getIP() + "</a></font></td></tr>");
		replyMSG.append("<tr><td width=100>Name/Level:</td><td>" + player.getName() + "/" + player.getLevel() + "</td></tr>");

		String hwid = player.getHWID();
		if(hwid != null)
			replyMSG.append("<tr><td width=100>HWID:</td><td><font color=\"ffffff\"><a action=\"bypass -h admin_find_char_hwid 0 " + hwid + "\">" + (hwid.length() > 12 ? hwid.substring(10) + ".." : hwid) + "</a></font></td></tr>");

		replyMSG.append("<tr><td width=100>Class/Id:</td><td>" + player.getTemplate().className + "/" + player.getClassId().getId() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Clan/Level:</td><td>" + clanName + "</td></tr>");
		replyMSG.append("<tr><td width=100>Exp/Sp:</td><td>" + player.getExp() + "/" + player.getSp() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Cur/Max Hp:</td><td>" + (int) player.getCurrentHp() + "/" + player.getMaxHp() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Cur/Max Load:</td><td>" + player.getCurrentLoad() + "/" + player.getMaxLoad() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Patk/Matk:</td><td>" + player.getPAtk((Creature) null) + "/" + player.getMAtk((Creature) null, (Skill) null) + "</td></tr>");
		replyMSG.append("<tr><td width=100>Pdef/Mdef:</td><td>" + player.getPDef((Creature) null) + "/" + player.getMDef((Creature) null, (Skill) null) + "</td></tr>");
		replyMSG.append("<tr><td width=100>PAtkSpd/MAtkSpd:</td><td>" + player.getPAtkSpd() + "/" + player.getMAtkSpd() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Acc/Evas:</td><td>" + player.getAccuracy() + "/" + player.getEvasionRate((Creature) null) + "</td></tr>");
		replyMSG.append("<tr><td width=100>Crit/MCrit:</td><td>" + player.getCriticalHit((Creature) null, (Skill) null) + "/" + df.format(player.getCriticalMagic((Creature) null, (Skill) null) / 10.0) + "%</td></tr>");
		replyMSG.append("<tr><td width=100>Walk/Run:</td><td>" + player.getWalkSpeed() + "/" + player.getRunSpeed() + "</td></tr>");
		replyMSG.append("<tr><td width=100>PvP/PK/Karma:</td><td>" + player.getPvpKills() + "/" + player.getPkKills() + "/" + player.getKarma() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Coordinates:</td><td>" + player.getX() + "," + player.getY() + "," + player.getZ() + " " + player.getHeading() + "</td></tr>");
		replyMSG.append("</table><br>");
		replyMSG.append("<table<tr>");
		replyMSG.append("<td><button value=\"Go To\" action=\"bypass -h admin_goto_char_menu " + player.getName() + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Recall\" action=\"bypass -h admin_recall_char_menu " + player.getName() + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Actions\" action=\"bypass -h admin_character_actions\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("</tr><tr>");
		replyMSG.append("<td><button value=\"Skills\" action=\"bypass -h admin_show_skills\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Renewal Skills\" action=\"bypass -h admin_rs\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Heal\" action=\"bypass -h admin_heal " + player.getName() + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("</tr><tr>");
		replyMSG.append("<td><button value=\"Stats\" action=\"bypass -h admin_edit_character\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Exp & Sp\" action=\"bypass -h admin_add_exp_sp_to_character\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Class\" action=\"bypass -h admin_show_html setclass.htm\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("</tr></table></body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	private void setTargetKarma(final Player activeChar, final int newKarma)
	{
		final GameObject target = activeChar.getTarget();
		if(target == null)
		{
			activeChar.sendPacket(Msg.INCORRECT_TARGET);
			return;
		}
		if(target.isPlayer())
		{
			final Player player = (Player) target;
			if(newKarma >= 0)
			{
				final int oldKarma = player.getKarma();
				player.setKarma(newKarma);
				player.sendMessage("Admin has changed your karma from " + oldKarma + " to " + newKarma + ".");
				activeChar.sendMessage("Successfully Changed karma for " + player.getName() + " from (" + oldKarma + ") to (" + newKarma + ").");
				Log.addLog(activeChar.toString() + " changed karma for player " + player.getName() + " from (" + oldKarma + ") to (" + newKarma + ")", "gm_actions");
			}
			else
				activeChar.sendMessage("You must enter a value for karma greater than or equal to 0.");
		}
	}

	private void adminModifyCharacter(final Player activeChar, final String modifications)
	{
		final GameObject target = activeChar.getTarget();
		if(target.isPlayer())
		{
			final Player player = (Player) target;
			final StringTokenizer st = new StringTokenizer(modifications);
			if(st.countTokens() != 6)
				editCharacter(player);
			else
			{
				final String hp = st.nextToken();
				final String mp = st.nextToken();
				final String karma = st.nextToken();
				final String pkkills = st.nextToken();
				final String pvpkills = st.nextToken();
				final String classid = st.nextToken();
				final int hpval = Integer.parseInt(hp);
				final int mpval = Integer.parseInt(mp);
				final int karmaval = Integer.parseInt(karma);
				final int pkkillsval = Integer.parseInt(pkkills);
				final int pvpkillsval = Integer.parseInt(pvpkills);
				final short classidval = Short.parseShort(classid);
				player.sendMessage("Admin has changed your stats. Hp: " + hpval + " Mp: " + mpval + " Karma: " + karmaval + " PK/PvP: " + pkkillsval + " / " + pvpkillsval + " ClassId: " + classidval);
				player.setCurrentHp(hpval, false);
				player.setCurrentMp(mpval);
				player.setKarma(karmaval);
				player.setPkKills(pkkillsval);
				player.setPvpKills(pvpkillsval);
				player.setClassId(classidval, true, false);
				player.sendChanges();
				activeChar.sendMessage("Changed stats of " + player.getName() + ".  Hp: " + hpval + " Mp: " + mpval + " Karma: " + karmaval + " PK/PvP: " + pkkillsval + " / " + pvpkillsval + " ClassId: " + classidval);
				showCharacterList(activeChar, null);
			}
		}
	}

	private void editCharacter(final Player activeChar)
	{
		final GameObject target = activeChar.getTarget();
		if(target != null && target.isPlayer())
		{
			final Player player = (Player) target;
			final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			final StringBuffer replyMSG = new StringBuffer("<html><body>");
			replyMSG.append("<table width=260><tr>");
			replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
			replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("</tr></table>");
			replyMSG.append("<br><br>");
			replyMSG.append("<center>Editing character: " + player.getName() + "</center><br>");
			replyMSG.append("<table width=250>");
			replyMSG.append("<tr><td width=40></td><td width=70>Curent:</td><td width=70>Max:</td><td width=70></td></tr>");
			replyMSG.append("<tr><td width=40>HP:</td><td width=70>" + player.getCurrentHp() + "</td><td width=70>" + player.getMaxHp() + "</td><td width=70>Karma: " + player.getKarma() + "</td></tr>");
			replyMSG.append("<tr><td width=40>MP:</td><td width=70>" + player.getCurrentMp() + "</td><td width=70>" + player.getMaxMp() + "</td><td width=70>PvP Kills: " + player.getPvpKills() + "</td></tr>");
			replyMSG.append("<tr><td width=40>Load:</td><td width=70>" + player.getCurrentLoad() + "</td><td width=70>" + player.getMaxLoad() + "</td><td width=70>PK Kills: " + player.getPkKills() + "</td></tr>");
			replyMSG.append("</table>");
			replyMSG.append("<table width=270><tr><td>Class Template Id: " + player.getClassId() + "/" + player.getClassId().getId() + "</td></tr></table><br>");
			replyMSG.append("<table width=270>");
			replyMSG.append("<tr><td>Note: Fill all values before saving the modifications.</td></tr>");
			replyMSG.append("</table><br>");
			replyMSG.append("<table width=270>");
			replyMSG.append("<tr><td width=50>Hp:</td><td><edit var=\"hp\" width=50></td><td width=50>Mp:</td><td><edit var=\"mp\" width=50></td></tr>");
			replyMSG.append("<tr><td width=50>PK Kills:</td><td><edit var=\"pkkills\" width=50></td><td width=50>Karma:</td><td><edit var=\"karma\" width=50></td></tr>");
			replyMSG.append("<tr><td width=50>Class Id:</td><td><edit var=\"classid\" width=50></td><td width=50>PvP Kills:</td><td><edit var=\"pvpkills\" width=50></td></tr>");
			replyMSG.append("</table><br>");
			replyMSG.append("<center><button value=\"Save Changes\" action=\"bypass -h admin_save_modifications $hp $mp $karma $pkkills $pvpkills $classid\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center><br>");
			replyMSG.append("</body></html>");
			adminReply.setHtml(replyMSG.toString());
			activeChar.sendPacket(adminReply);
		}
	}

	private void showCharacterActions(final Player activeChar)
	{
		final GameObject target = activeChar.getTarget();
		if(target != null && target.isPlayer())
		{
			final Player player = (Player) target;
			final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			final StringBuffer replyMSG = new StringBuffer("<html><body>");
			replyMSG.append("<table width=270><tr>");
			replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
			replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("</tr></table><br>");
			replyMSG.append("<center>Admin Actions for: <font color=\"LEVEL\">" + player.getName() + "</font></center><br>");
			replyMSG.append("<center><table width=240><tr><td>Time:</td><td><edit var=\"time\" width=150 height=10></td></tr><br><tr><td>Reason:</td><td><multiedit var=\"reason\" width=150 height=41></td></tr><br></table></center><br>");
			replyMSG.append("<center><table>");
			replyMSG.append("<tr><td><button value=\"Info Ban Char\" action=\"bypass -h admin_isban " + player.getName() + "\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td><button value=\"Show IP Char\" action=\"bypass -h admin_charip " + player.getName() + "\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			replyMSG.append("<tr><td><button value=\"Ban IP Char days\" action=\"bypass -h admin_tele " + player.getName() + " $time $reason\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td><button value=\"Ban Char days\" action=\"bypass -h admin_old_ban " + player.getName() + " $time $reason\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			replyMSG.append("<tr><td><button value=\"Ban Acc Char days\" action=\"bypass -h admin_acc_ban " + player.getName() + " $time\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td><button value=\"BanHWID Char days\" action=\"bypass -h admin_hnban " + player.getName() + " $time $reason\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			replyMSG.append("<tr><td><button value=\"BanAccsHWID days\" action=\"bypass -h admin_accsbanhwid " + player.getName() + " $time $reason\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td></td></tr>");
			replyMSG.append("<tr><td><button value=\"Jail Char min\" action=\"bypass -h admin_jail " + player.getName() + " $time\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td><button value=\"Unjail Char\" action=\"bypass -h admin_unjail " + player.getName() + "\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			replyMSG.append("<tr><td><button value=\"Ban Chat Char min\" action=\"bypass -h admin_n " + player.getName() + " $time $reason\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td><button value=\"Unban Chat Char\" action=\"bypass -h admin_n " + player.getName() + " 0\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			replyMSG.append("</table></center></body></html>");
			adminReply.setHtml(replyMSG.toString());
			activeChar.sendPacket(adminReply);
		}
	}

	private void findCharacter(final Player activeChar, final String CharacterToFind)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		int CharactersFound = 0;
		final StringBuffer replyMSG = new StringBuffer("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_show_characters 0\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<br><br>");
		for(final Player element : GameObjectsStorage.getPlayers())
			if(element.getName().toLowerCase().contains(CharacterToFind))
			{
				++CharactersFound;
				replyMSG.append("<table width=270>");
				replyMSG.append("<tr><td width=80>Name</td><td width=110>Class</td><td width=40>Level</td></tr>");
				replyMSG.append("<tr><td width=80><a action=\"bypass -h admin_character_list " + element.getName() + "\">" + element.getName().replaceAll("<", "-") + "</a></td><td width=110>" + element.getTemplate().className + "</td><td width=40>" + element.getLevel() + "</td></tr>");
				replyMSG.append("</table>");
			}
		if(CharactersFound == 0)
		{
			replyMSG.append("<table width=270>");
			replyMSG.append("<tr><td width=270>Your search did not find any characters.</td></tr>");
			replyMSG.append("<tr><td width=270>Please try again.<br></td></tr>");
			replyMSG.append("</table><br>");
			replyMSG.append("<center><table><tr><td>");
			replyMSG.append("<edit var=\"character_name\" width=130></td><td><button value=\"Find\" action=\"bypass -h admin_find_character $character_name\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
			replyMSG.append("</td></tr></table></center>");
		}
		else
		{
			replyMSG.append("<center><br>Found " + CharactersFound + " character");
			if(CharactersFound == 1)
				replyMSG.append(".");
			else if(CharactersFound > 1)
				replyMSG.append("s.");
			replyMSG.append("</center>");
		}
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	private void findCharacterIpHwid(final Player activeChar, int page, final String param, final boolean hd)
	{
		final List<Player> players = new ArrayList<Player>();
		for(final Player pr : GameObjectsStorage.getPlayers())
		{
			if(hd)
			{
				if(!pr.getHWID().equals(param))
					continue;
			}
			else if(!pr.getIP().equals(param))
				continue;
			players.add(pr);
		}
		final int MaxCharactersPerPage = 20;
		int MaxPages = players.size() / MaxCharactersPerPage;
		if(players.size() > MaxCharactersPerPage * MaxPages)
			++MaxPages;
		if(page > MaxPages)
			page = MaxPages;
		final int CharactersStart = MaxCharactersPerPage * page;
		int CharactersEnd = players.size();
		if(CharactersEnd - CharactersStart > MaxCharactersPerPage)
			CharactersEnd = CharactersStart + MaxCharactersPerPage;
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final StringBuffer replyMSG = new StringBuffer("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<br>");
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td width=270><center><font color=\"LEVEL\">Characters with " + (hd ? "HWID" : "IP") + "<br1>" + param + "</font></center></td></tr>");
		replyMSG.append("</table><br>");
		String pages = "<center><table width=300><tr>";
		for(int x = 0; x < MaxPages; ++x)
		{
			final int pagenr = x + 1;
			pages = pages + "<td><a action=\"bypass -h admin_find_char_" + (hd ? "hwid" : "ip") + " " + x + " " + param + "\">" + (x == page ? "<font color=\"ffffff\">" + pagenr + "</font>" : String.valueOf(pagenr)) + "</a></td>";
			if(pagenr == 20)
				break;
		}
		pages += "</tr></table></center>";
		replyMSG.append(pages);
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td width=130>Name:</td><td width=110>Class:</td><td width=20>Lvl:</td></tr>");
		for(int i = CharactersStart; i < CharactersEnd; ++i)
		{
			final Player p = players.get(i);
			replyMSG.append("<tr><td width=130><a action=\"bypass -h admin_character_list " + p.getName() + "\">" + p.getName().replaceAll("<", "-") + "</a></td><td width=110>" + p.getTemplate().className + "</td><td width=20>" + p.getLevel() + "</td></tr>");
		}
		replyMSG.append("</table>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	private void addExpSp(final Player activeChar)
	{
		final GameObject target = activeChar.getTarget();
		if(target != null && target.isPlayer() && (activeChar == target || activeChar.getPlayerAccess().CanEditCharAll))
		{
			final Player player = (Player) target;
			final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			final StringBuffer replyMSG = new StringBuffer("<html><body>");
			replyMSG.append("<table width=260><tr>");
			replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
			replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("</tr></table>");
			replyMSG.append("<br><br>");
			replyMSG.append("<table width=270><tr><td>Name: " + player.getName() + "</td></tr>");
			replyMSG.append("<tr><td>Lv: " + player.getLevel() + " " + player.getTemplate().className + "</td></tr>");
			replyMSG.append("<tr><td>Exp: " + player.getExp() + "</td></tr>");
			replyMSG.append("<tr><td>Sp: " + player.getSp() + "</td></tr></table>");
			replyMSG.append("<br><table width=270><tr><td>Note: Dont forget that modifying players skills can</td></tr>");
			replyMSG.append("<tr><td>ruin the game...</td></tr></table><br>");
			replyMSG.append("<table width=270><tr><td>Note: Fill all values before saving the modifications.,</td></tr>");
			replyMSG.append("<tr><td>Note: Use 0 if no changes are needed.</td></tr></table><br>");
			replyMSG.append("<center><table><tr>");
			replyMSG.append("<td>Exp: <edit var=\"exp_to_add\" width=50></td>");
			replyMSG.append("<td>Sp:  <edit var=\"sp_to_add\" width=50></td>");
			replyMSG.append("<td>&nbsp;<button value=\"Save Changes\" action=\"bypass -h admin_add_exp_sp $exp_to_add $sp_to_add\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("</tr>");
			replyMSG.append("<tr><td height=15></td></tr><tr>");
			replyMSG.append("<td>Level:  <edit var=\"level_to_add\" width=50></td>");
			replyMSG.append("<td>&nbsp;<button value=\"Set Level\" action=\"bypass -h admin_setlevel $level_to_add\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("</tr>");
			replyMSG.append("</table></center>");
			replyMSG.append("</body></html>");
			adminReply.setHtml(replyMSG.toString());
			activeChar.sendPacket(adminReply);
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	private void adminAddExpSp(final Player activeChar, final String ExpSp)
	{
		if(!activeChar.getPlayerAccess().CanEditCharAll)
		{
			activeChar.sendMessage("You have not enough privileges, for use this function.");
			return;
		}
		final GameObject target = activeChar.getTarget();
		if(target == null || !target.isPlayer())
		{
			activeChar.sendPacket(new SystemMessage(242));
			return;
		}
		final Player player = (Player) target;
		final StringTokenizer st = new StringTokenizer(ExpSp);
		if(st.countTokens() != 2)
			addExpSp(activeChar);
		else
		{
			final String exp = st.nextToken();
			final String sp = st.nextToken();
			long expval = 0L;
			long spval = 0L;
			try
			{
				expval = Long.parseLong(exp);
				spval = Long.parseLong(sp);
			}
			catch(NumberFormatException e)
			{
				activeChar.sendMessage("Wrong number format.");
			}
			if(expval != 0L || spval != 0L)
			{
				player.sendMessage("Admin is adding you " + expval + " exp and " + spval + " SP.");
				player.addExpAndSp(expval, spval, false, false);
				activeChar.sendMessage("Added " + expval + " exp and " + spval + " SP to " + player.getName() + ".");
				Log.addLog(activeChar.toString() + " added " + expval + " exp and " + spval + " SP to player" + player.toString(), "gm_actions");
			}
		}
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
		AdminEditChar._adminCommands = new String[] {
				"admin_edit_character",
				"admin_character_actions",
				"admin_current_player",
				"admin_nokarma",
				"admin_setkarma",
				"admin_character_list",
				"admin_show_characters",
				"admin_find_character",
				"admin_find_char_ip",
				"admin_find_char_hwid",
				"admin_save_modifications",
				"admin_rec",
				"admin_setclass",
				"admin_settitle",
				"admin_setname",
				"admin_setsex",
				"admin_setcolor",
				"admin_settcolor",
				"admin_add_exp_sp_to_character",
				"admin_add_exp_sp",
				"admin_sethero",
				"admin_setnoble",
				"admin_delnoble",
				"admin_setsubclass",
				"admin_givebonus",
				"admin_givepb",
				"admin_givehs",
				"admin_exchange_names",
				"admin_setcphpmp",
				"admin_setgiran",
				"admin_clanonline",
				"admin_clansize",
				"admin_droppk",
				"admin_augment",
				"admin_delaugment",
				"admin_fullcs",
				"admin_delvar",
				"admin_setinst",
				"admin_add_bang",
				"admin_set_bang",
				"admin_deleteitems",
				"admin_delcharitem" };
	}
}
