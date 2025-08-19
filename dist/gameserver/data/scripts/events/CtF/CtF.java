package events.CtF;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.database.mysql;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.listener.ZoneEnterLeaveListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.model.entity.olympiad.OlympiadGame;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.RadarControl;
import l2s.gameserver.network.l2.s2c.Revive;
import l2s.gameserver.network.l2.s2c.Say2;
import l2s.gameserver.network.l2.s2c.SocialAction;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.EffectType;
import l2s.gameserver.tables.DoorTable;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Util;
import npc.model.CtFInstance;

public class CtF extends Functions implements ScriptFile
{
	private static Logger _log;
	private static List<Integer> players_list1;
	private static List<Integer> players_list2;
	private static Map<String, Integer> _restrictIP;
	private static Map<String, Integer> _restrictHWID;
	private static final int[] removeEffects;
	private static int flags1;
	private static int flags2;
	private static boolean noRedFlag;
	private static boolean noBlueFlag;
	private static boolean _clearing;
	private static final int _instanceID;
	private static final boolean useBC;
	private static CtFInstance redFlag;
	private static CtFInstance blueFlag;
	private static CtFInstance redThrone;
	private static CtFInstance blueThrone;
	private static boolean _isRegistrationActive;
	private static int _status;
	private static int _time_to_start;
	private static int _category;
	private static int _pre_category;
	private static int _minLevel;
	private static int _maxLevel;
	private static ScheduledFuture<?> _endTask;
	private static Zone _zone;
	private static ZoneListener _zoneListener;
	private static final Location BlueTeamLoc;
	private static final Location RedTeamLoc;
	private static final Location BlueTeamResLoc;
	private static final Location RedTeamResLoc;
	private static final Location BlueFlagLoc;
	private static final Location RedFlagLoc;
	private static final Location ClearLoc;
	private static Calendar _date;
	private static Calendar date;
	private static boolean BS_Active;
	private static boolean _active;

	@Override
	public void onLoad()
	{
		_zone.getListenerEngine().addMethodInvokedListener(_zoneListener);
		_active = ServerVariables.getString("CtF", "off").equalsIgnoreCase("on");
		if(_active)
			executeTask("events.CtF.CtF", "preLoad", new Object[0], 80000L);
		_date = Calendar.getInstance();
		(date = (Calendar) _date.clone()).set(11, 0);
		date.set(12, 0);
		date.set(13, 30);
		date.add(6, 1);
		if(!Config.CtF_Allow_Calendar_Day && date.getTimeInMillis() > System.currentTimeMillis())
			executeTask("events.CtF.CtF", "addDay", new Object[0], (int) (date.getTimeInMillis() - System.currentTimeMillis()));
		_log.info("Loaded Event: CtF");
	}

	@Override
	public void onReload()
	{
		_zone.getListenerEngine().removeMethodInvokedListener(_zoneListener);
	}

	@Override
	public void onShutdown()
	{
		onReload();
	}

	public static void activateBS()
	{
		BS_Active = true;
	}

	public void activateEvent()
	{
		final Player player = getSelf();
		if(player == null || !player.getPlayerAccess().IsEventGm)
			return;
		if(!_active)
		{
			executeTask("events.CtF.CtF", "preLoad", new Object[0], 10000L);
			ServerVariables.set("CtF", "on");
			_log.info("Event 'CtF' activated.");
			player.sendMessage(new CustomMessage("scripts.events.CtF.AnnounceEventStarted"));
		}
		else
			player.sendMessage(player.isLangRus() ? "'CtF' \u044d\u0432\u0435\u043d\u0442 \u0443\u0436\u0435 \u0430\u043a\u0442\u0438\u0432\u0435\u043d." : "Event 'CtF' already active.");
		_active = true;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void deactivateEvent()
	{
		final Player player = getSelf();
		if(player == null || !player.getPlayerAccess().IsEventGm)
			return;
		if(_active)
		{
			ServerVariables.unset("CtF");
			_log.info("Event 'CtF' deactivated.");
			player.sendMessage(new CustomMessage("scripts.events.CtF.AnnounceEventStoped"));
		}
		else
			player.sendMessage("Event 'CtF' not active.");
		_active = false;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public static boolean isRunned(final String name)
	{
		return (_isRegistrationActive || _status > 0) && _instanceID == 0 && name.equals(Config.CtF_Zone);
	}

	public String DialogAppend_31225(final Integer val)
	{
		if(val != 0)
			return "";
		final Player player = getSelf();
		if(player == null)
			return "";
		return HtmCache.getInstance().getHtml("scripts/events/CtF/31225.html", player);
	}

	private static void dialog(final Player player)
	{
		if(player.getTeam() > 0 && !player.isDead())
		{
			final GameObject target = player.getTarget();
			NpcInstance npc = null;
			if(target != null && target.isNpc())
				npc = (NpcInstance) target;
			if(npc == null)
				return;
			if(!npc.isInRange(player, 150L))
			{
				if(player.getAI().getIntention() != CtrlIntention.AI_INTENTION_INTERACT)
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, npc, null);
				return;
			}
			player.turn(npc, 3000);
			if(player.isFlagEquipped())
			{
				if(player.getTeam() == 1 && (npc == blueFlag || npc == blueThrone) && noRedFlag)
				{
					++flags1;
					if(flags1 >= Config.CtF_Flags)
						endBattle(1);
					else
					{
						dropFlag(player);
						final String ms = player.getName() + " brought to Blue team 1 point!";
						for(final Player p : getPlayers(players_list1, players_list2))
							if(p != null)
								p.sendPacket(new ExShowScreenMessage(ms, 3000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
					}
				}
				else if(player.getTeam() == 2 && (npc == redFlag || npc == redThrone) && noBlueFlag)
				{
					++flags2;
					if(flags2 >= Config.CtF_Flags)
						endBattle(2);
					else
					{
						dropFlag(player);
						final String ms = player.getName() + " brought to Red team 1 point!";
						for(final Player p : getPlayers(players_list1, players_list2))
							if(p != null)
								p.sendPacket(new ExShowScreenMessage(ms, 3000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
					}
				}
			}
			else if(player.getTeam() == 1 && npc == redFlag)
			{
				if(redFlag.isVisible() && !noRedFlag)
				{
					noRedFlag = true;
					redFlag.decayMe();
					addFlag(player, true);
				}
			}
			else if(player.getTeam() == 2 && npc == blueFlag && blueFlag.isVisible() && !noBlueFlag)
			{
				noBlueFlag = true;
				blueFlag.decayMe();
				addFlag(player, false);
			}
		}
	}

	public boolean OnAction_CtFInstance(final Player player, final GameObject object)
	{
		if(player == null || object == null)
			return false;
		if(_status > 1 && (object.getName().contains("Red") || object.getName().contains("Blue")))
		{
			dialog(player);
			return true;
		}
		return false;
	}

	public static int getMinLevelForCategory(final int category)
	{
		switch(category)
		{
			case 1:
			{
				return 20;
			}
			case 2:
			{
				return 30;
			}
			case 3:
			{
				return 40;
			}
			case 4:
			{
				return 52;
			}
			case 5:
			{
				return 62;
			}
			case 6:
			{
				return 76;
			}
			default:
			{
				return 0;
			}
		}
	}

	public static int getMaxLevelForCategory(final int category)
	{
		switch(category)
		{
			case 1:
			{
				return 29;
			}
			case 2:
			{
				return 39;
			}
			case 3:
			{
				return 51;
			}
			case 4:
			{
				return 61;
			}
			case 5:
			{
				return 75;
			}
			case 6:
			{
				return 80;
			}
			default:
			{
				return 0;
			}
		}
	}

	public static int getCategory(final int level)
	{
		if(level >= 20 && level <= 29)
			return 1;
		if(level >= 30 && level <= 39)
			return 2;
		if(level >= 40 && level <= 51)
			return 3;
		if(level >= 52 && level <= 61)
			return 4;
		if(level >= 62 && level <= 75)
			return 5;
		if(level >= 76)
			return 6;
		return 0;
	}

	public void start(final String[] var)
	{
		final Player player = getSelf();
		if(player == null || !player.getPlayerAccess().IsEventGm)
			return;
		startOk(var);
	}

	private static void startOk(final String[] var)
	{
		if(var.length < 1)
			return;
		if(_instanceID == 0 && (isEventStarted("events.TvT.TvT", Config.CtF_Zone) || isEventStarted("events.lastHero.LastHero", Config.CtF_Zone)))
		{
			_log.info("CtF not started: another event is already running");
			return;
		}
		if(_isRegistrationActive || _status > 0)
			return;
		try
		{
			_category = Integer.valueOf(var[0]);
		}
		catch(Exception e)
		{
			_log.info("CtF not started: can't parse category");
			return;
		}
		if(_category == -1)
		{
			_minLevel = 1;
			_maxLevel = 80;
		}
		else
		{
			_minLevel = getMinLevelForCategory(_category);
			_maxLevel = getMaxLevelForCategory(_category);
		}
		if(_endTask != null)
			return;
		players_list1 = new CopyOnWriteArrayList<Integer>();
		players_list2 = new CopyOnWriteArrayList<Integer>();
		_restrictIP = new HashMap<String, Integer>();
		_restrictHWID = new HashMap<String, Integer>();
		_status = 0;
		_isRegistrationActive = true;
		_clearing = false;
		_time_to_start = Config.CtF_Time;
		flags1 = 0;
		flags2 = 0;
		noRedFlag = false;
		noBlueFlag = false;
		if(redFlag != null)
			redFlag.deleteMe();
		if(blueFlag != null)
			blueFlag.deleteMe();
		if(redThrone != null)
			redThrone.deleteMe();
		if(blueThrone != null)
			blueThrone.deleteMe();
		redFlag = spawnNpc(RedFlagLoc, 35062, _instanceID);
		blueFlag = spawnNpc(BlueFlagLoc, 35062, _instanceID);
		redThrone = spawnNpc(RedFlagLoc, 32027, _instanceID);
		blueThrone = spawnNpc(BlueFlagLoc, 32027, _instanceID);
		redThrone.setHasChatWindow(false);
		blueThrone.setHasChatWindow(false);
		redFlag.setHasChatWindow(false);
		blueFlag.setHasChatWindow(false);
		redFlag.setName("Red Flag");
		blueFlag.setName("Blue Flag");
		redThrone.setName("Red Base");
		blueThrone.setName("Blue Base");
		redFlag.decayMe();
		blueFlag.decayMe();
		redThrone.decayMe();
		blueThrone.decayMe();
		final String[] param = { String.valueOf(_time_to_start), String.valueOf(_minLevel), String.valueOf(_maxLevel) };
		sayToAll("scripts.events.CtF.AnnouncePreStart", param);
		sayToAll("scripts.events.CtF.AnnounceReg", null);
		executeTask("events.CtF.CtF", "question", new Object[0], 5000L);
		executeTask("events.CtF.CtF", "announce", new Object[0], 60000L);
	}

	private static void sayToAll(final String address, final String[] replacements)
	{
		for(final Player player : GameObjectsStorage.getPlayers())
			if(player != null)
			{
				final CustomMessage cm = new CustomMessage(address);
				if(replacements != null)
					for(final String s : replacements)
						cm.addString(s);
				player.sendPacket(new Say2(0, ChatType.CRITICAL_ANNOUNCE, "CtF", "CtF: " + cm.toString(player)));
			}
	}

	private static void sayToParticipants(final String address, final String[] replacements)
	{
		for(final Player player : getPlayers(players_list1, players_list2))
			if(player != null)
			{
				final CustomMessage cm = new CustomMessage(address);
				if(replacements != null)
					for(final String s : replacements)
						cm.addString(s);
				player.sendPacket(new Say2(0, ChatType.CRITICAL_ANNOUNCE, "CtF", "CtF: " + cm.toString(player)));
			}
	}

	public static void question()
	{
		for(final Player player : GameObjectsStorage.getPlayers())
			if(player != null && player.getLevel() >= _minLevel && player.getLevel() <= _maxLevel && player.getReflectionId() == 0 && !player.isInOlympiadMode() && !player.isCursedWeaponEquipped() && !player.inObserverMode() && !player.inEvent() && !player.getVarBoolean("NoEventAsk", Config.EVENT_NO_ASK))
				player.scriptRequest(new CustomMessage("scripts.events.CtF.AskPlayer").toString(player), "events.CtF.CtF:addPlayer", new Object[0]);
	}

	public static void announce()
	{
		if(_time_to_start > 1)
		{
			--_time_to_start;
			final String[] param = { String.valueOf(_time_to_start), String.valueOf(_minLevel), String.valueOf(_maxLevel) };
			sayToAll("scripts.events.CtF.AnnouncePreStart", param);
			executeTask("events.CtF.CtF", "announce", new Object[0], 60000L);
		}
		else
		{
			if(players_list1.isEmpty() || players_list2.isEmpty() || players_list1.size() + players_list2.size() < Config.CtF_MinPlayers)
			{
				sayToAll("scripts.events.CtF.AnnounceEventCancelled", null);
				_isRegistrationActive = false;
				_status = 0;
				executeTask("events.CtF.CtF", "preLoad", new Object[0], 10000L);
				return;
			}
			_status = 1;
			_isRegistrationActive = false;
			sayToAll("scripts.events.CtF.AnnounceEventStarting", null);
			executeTask("events.CtF.CtF", "prepare", new Object[0], 5000L);
		}
	}

	public void addPlayer()
	{
		final Player player = getSelf();
		if(player == null || !checkPlayer(player, true))
			return;
		if(players_list1.size() + players_list2.size() >= Config.CtF_MaxPlayers)
		{
			player.sendMessage("\u0414\u043e\u0441\u0442\u0438\u0433\u043d\u0443\u0442 \u043b\u0438\u043c\u0438\u0442 \u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u043e\u0433\u043e \u043a\u043e\u043b-\u0432\u0430 \u0443\u0447\u0430\u0441\u0442\u043d\u0438\u043a\u043e\u0432.");
			return;
		}
		if(Config.CtF_IP)
		{
			int cn = 0;
			if(_restrictIP.containsKey(player.getIP()))
			{
				cn = _restrictIP.get(player.getIP());
				if(cn >= Config.CtF_IP_Max)
				{
					player.sendMessage("\u0418\u0433\u0440\u043e\u043a \u0441 \u0434\u0430\u043d\u043d\u044b\u043c IP \u0443\u0436\u0435 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d.");
					return;
				}
			}
			_restrictIP.put(player.getIP(), cn + 1);
		}
		if(Config.CtF_HWID)
		{
			int cn = 0;
			if(_restrictHWID.containsKey(player.getHWID()))
			{
				cn = _restrictHWID.get(player.getHWID());
				if(cn >= Config.CtF_HWID_Max)
				{
					player.sendMessage("\u0418\u0433\u0440\u043e\u043a \u0441 \u0434\u0430\u043d\u043d\u044b\u043c \u0436\u0435\u043b\u0435\u0437\u043e\u043c \u0443\u0436\u0435 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d.");
					return;
				}
			}
			_restrictHWID.put(player.getHWID(), cn + 1);
		}
		int team = 0;
		final int size1 = players_list1.size();
		final int size2 = players_list2.size();
		if(size1 > size2)
			team = 2;
		else if(size1 < size2)
			team = 1;
		else
			team = Rnd.get(1, 2);
		if(team == 1)
		{
			players_list1.add(player.getObjectId());
			show(new CustomMessage("scripts.events.CtF.Registered"), player);
		}
		else if(team == 2)
		{
			players_list2.add(player.getObjectId());
			show(new CustomMessage("scripts.events.CtF.Registered"), player);
		}
		else
			_log.info("WTF??? Command id 0 in CtF...");
	}

	public static boolean checkPlayer(final Player player, final boolean first)
	{
		if(first && !_isRegistrationActive)
		{
			show(new CustomMessage("scripts.events.Late"), player);
			return false;
		}
		if(first && (players_list1.contains(player.getObjectId()) || players_list2.contains(player.getObjectId())))
		{
			show(new CustomMessage("scripts.events.CtF.Cancelled"), player);
			return false;
		}
		if(player.getLevel() < _minLevel || player.getLevel() > _maxLevel)
		{
			show(new CustomMessage("scripts.events.CtF.CancelledLevel"), player);
			return false;
		}
		if(player.isMounted())
		{
			player.sendMessage(player.isLangRus() ? "\u0412\u0435\u0440\u0445\u043e\u043c \u043d\u0430 \u044d\u0432\u0435\u043d\u0442 \u043d\u0435\u043b\u044c\u0437\u044f." : "You can't do it while riding.");
			return false;
		}
		if(player.isInDuel())
		{
			show(new CustomMessage("scripts.events.CtF.CancelledDuel"), player);
			return false;
		}
		if(player.getTeam() != 0)
		{
			show(new CustomMessage("scripts.events.CtF.CancelledOtherEvent"), player);
			return false;
		}
		if(player.getOlympiadGameId() > 0 || player.isInZoneOlympiad() || first && Olympiad.isRegistered(player))
		{
			show(new CustomMessage("scripts.events.CtF.CancelledOlympiad"), player);
			return false;
		}
		if(player.isInParty() && player.getParty().isInDimensionalRift())
		{
			show(new CustomMessage("scripts.events.CtF.CancelledOtherEvent"), player);
			return false;
		}
		if(player.isTeleporting())
		{
			show(new CustomMessage("scripts.events.CtF.CancelledTeleport"), player);
			return false;
		}
		if(player.isCursedWeaponEquipped())
		{
			player.sendMessage(player.isLangRus() ? "\u0421 \u043f\u0440\u043e\u043a\u043b\u044f\u0442\u044b\u043c \u043e\u0440\u0443\u0436\u0438\u0435\u043c \u043d\u0430 \u044d\u0432\u0435\u043d\u0442 \u043d\u0435\u043b\u044c\u0437\u044f." : "You can't do it with cursed weapon.");
			return false;
		}
		if(player.inObserverMode())
		{
			player.sendMessage(player.isLangRus() ? "\u0412 \u0440\u0435\u0436\u0438\u043c\u0435 \u043f\u0440\u043e\u0441\u043c\u043e\u0442\u0440\u0430 \u043d\u0430 \u044d\u0432\u0435\u043d\u0442 \u043d\u0435\u043b\u044c\u0437\u044f." : "You can't do it while observing.");
			return false;
		}
		if(player.getVar("jailed") != null)
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0435 \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u044c\u0441\u044f \u0441\u0431\u0435\u0436\u0430\u0442\u044c." : "You can't do it in jail.");
			return false;
		}
		return true;
	}

	public static void prepare()
	{
		if(_instanceID == 0 && _zone.getIndex() == 4 && _zone.getType() == Zone.ZoneType.battle_zone)
		{
			DoorTable.getInstance().getDoor(24190002).closeMe();
			DoorTable.getInstance().getDoor(24190003).closeMe();
		}
		cleanPlayers();
		if(players_list1.isEmpty() || players_list2.isEmpty() || players_list1.size() + players_list2.size() < Config.CtF_MinPlayers)
		{
			sayToAll("scripts.events.CtF.AnnounceEventCancelled", null);
			_isRegistrationActive = false;
			_status = 0;
			executeTask("events.CtF.CtF", "preLoad", new Object[0], 10000L);
			return;
		}
		clearArena();
		redThrone.spawnMe();
		blueThrone.spawnMe();
		redFlag.spawnMe();
		blueFlag.spawnMe();
		executeTask("events.CtF.CtF", "paralyzePlayers", new Object[0], 100L);
		executeTask("events.CtF.CtF", "teleportPlayersToColiseum", new Object[0], 3000L);
		executeTask("events.CtF.CtF", "go", new Object[0], Config.CtF_Time_Paralyze * 1000);
		sayToParticipants("scripts.events.CtF.AnnounceFinalCountdown", new String[] { String.valueOf(Config.CtF_Time_Paralyze) });
	}

	public static void go()
	{
		_status = 2;
		if(Config.CtF_CancelAllBuff)
			removeBuff();
		else
			upParalyzePlayers();
		if(checkInZone())
		{
			clearArena();
			return;
		}
		clearArena();
		buffPlayers();
		sayToParticipants("scripts.events.CtF.AnnounceFight", null);
		_endTask = executeTask("events.CtF.CtF", "endOfTime", new Object[0], Config.CtF_Time_Battle * 60000);
	}

	private static void buffPlayers()
	{
		if(Config.EVENT_BUFFS_FIGHTER.length < 2 && Config.EVENT_BUFFS_MAGE.length < 2)
			return;
		for(final Player player : getPlayers(players_list1, players_list2))
			if(player != null)
				if(player.isMageClass())
				{
					if(Config.EVENT_BUFFS_MAGE.length <= 1)
						continue;
					int n = 0;
					for(int i = 0; i < Config.EVENT_BUFFS_MAGE.length; i += 2)
						OlympiadGame.giveBuff(player, SkillTable.getInstance().getInfo(Config.EVENT_BUFFS_MAGE[i], Config.EVENT_BUFFS_MAGE[i + 1]), n++);
				}
				else
				{
					if(Config.EVENT_BUFFS_FIGHTER.length <= 1)
						continue;
					int n = 0;
					for(int i = 0; i < Config.EVENT_BUFFS_FIGHTER.length; i += 2)
						OlympiadGame.giveBuff(player, SkillTable.getInstance().getInfo(Config.EVENT_BUFFS_FIGHTER[i], Config.EVENT_BUFFS_FIGHTER[i + 1]), n++);
				}
	}

	private static boolean checkInZone()
	{
		for(final Player player : getPlayers(players_list1, players_list2))
			if(player != null && !player.isInZone(_zone))
			{
				removePlayer(player);
				if(useBC && player.getVar("CtF_backCoords") == null)
					player.teleToLocation(ClearLoc);
				else
					backPlayer(player);
			}
		if(players_list1.size() < 1)
		{
			endBattle(2);
			return true;
		}
		if(players_list2.size() < 1)
		{
			endBattle(1);
			return true;
		}
		return false;
	}

	private static void removeBuff()
	{
		for(final Player player : getPlayers(players_list1, players_list2))
			if(player != null)
				try
				{
					player.getAbnormalList().stopAll();
					final Servitor summon = player.getServitor();
					if(summon == null)
						continue;
					summon.getAbnormalList().stopAll();
					if(!summon.isPet() && (Config.EVENT_RESTRICTED_SUMMONS.length <= 0 || !ArrayUtils.contains(Config.EVENT_RESTRICTED_SUMMONS, summon.getNpcId())))
						continue;
					summon.unSummon();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
	}

	public static void endOfTime()
	{
		endBattle(flags1 > flags2 ? 1 : flags2 > flags1 ? 2 : 3);
	}

	private static void endBattle(final int win)
	{
		if(_clearing)
			return;
		_clearing = true;
		try
		{
			if(_endTask != null)
			{
				_endTask.cancel(false);
				_endTask = null;
			}
		}
		catch(Exception ex)
		{}
		removeFlags();
		if(redFlag != null)
		{
			redFlag.deleteMe();
			redFlag = null;
		}
		if(blueFlag != null)
		{
			blueFlag.deleteMe();
			blueFlag = null;
		}
		if(redThrone != null)
		{
			redThrone.deleteMe();
			redThrone = null;
		}
		if(blueThrone != null)
		{
			blueThrone.deleteMe();
			blueThrone = null;
		}
		if(_status == 0)
			return;
		_status = 0;
		removeAura();
		if(_instanceID == 0 && _zone.getIndex() == 4 && _zone.getType() == Zone.ZoneType.battle_zone)
		{
			DoorTable.getInstance().getDoor(24190002).openMe();
			DoorTable.getInstance().getDoor(24190003).openMe();
		}
		final String[] param = { String.valueOf(flags1), String.valueOf(flags2) };
		switch(win)
		{
			case 1:
			{
				sayToAll("scripts.events.CtF.AnnounceFinishedBlueWins", param);
				giveItemsToWinner(1);
				break;
			}
			case 2:
			{
				sayToAll("scripts.events.CtF.AnnounceFinishedRedWins", param);
				giveItemsToWinner(2);
				break;
			}
			case 3:
			{
				sayToAll("scripts.events.CtF.AnnounceFinishedDraw", null);
				if(Config.CtF_DrawReward)
				{
					giveItemsToWinner(0);
					break;
				}
				break;
			}
		}
		sayToParticipants("scripts.events.CtF.AnnounceEnd", new String[] { String.valueOf(Config.EVENTS_TIME_BACK) });
		executeTask("events.CtF.CtF", "end", new Object[0], Config.EVENTS_TIME_BACK * 1000);
		_isRegistrationActive = false;
		flags1 = 0;
		flags2 = 0;
		_clearing = false;
	}

	public static void end()
	{
		ressurectPlayers();
		executeTask("events.CtF.CtF", "teleportPlayersToSavedCoords", new Object[0], 100L);
		executeTask("events.CtF.CtF", "preLoad", new Object[0], 10000L);
	}

	private static void giveItemsToWinner(final int team)
	{
		for(final Player player : team == 1 ? getPlayers(players_list1) : team == 2 ? getPlayers(players_list2) : getPlayers(players_list1, players_list2))
			if(player != null)
			{
				if(player.eventKills >= Config.CtF_MinKills)
					for(int i = 0; i < Config.CtF_reward_final.length; i += 2)
						addItem(player, Config.CtF_reward_final[i], Config.CtF_reward_final[i + 1]);
				if(!BS_Active)
					continue;
				Functions.callScripts("services.BonusStats", "event", new Object[] { player });
			}
		if(team != 0 && Config.CtF_reward_losers.length > 1)
			for(final Player player : getPlayers(team == 1 ? players_list2 : players_list1))
				if(player != null && player.eventKills >= Config.CtF_LosersMinKills)
					for(int i = 0; i < Config.CtF_reward_losers.length; i += 2)
						addItem(player, Config.CtF_reward_losers[i], Config.CtF_reward_losers[i + 1]);
	}

	public static void teleportPlayersToSavedCoords()
	{
		for(final Player player : getPlayers(players_list1, players_list2))
			if(player != null)
				backPlayer(player);
		unsetLastCoords();
	}

	public static void teleportPlayersToColiseum()
	{
		for(final Player player : getPlayers(players_list1, players_list2))
		{
			if(player == null)
				continue;
			player.eventKills = 0;
			unRide(player);
			unSummonPet(player, true);
			if(useBC)
				player.setVar("CtF_backCoords", player.isInZone(Zone.ZoneType.no_restart) || player.isInZone(Zone.ZoneType.epic) ? ClearLoc.getX() + " " + ClearLoc.getY() + " " + ClearLoc.getZ() : player.getX() + " " + player.getY() + " " + player.getZ());
			if(player.getParty() != null)
				player.getParty().oustPartyMember(player);
			player.teleToLocation(Location.findAroundPosition(player.getTeam() == 1 ? BlueTeamLoc : RedTeamLoc, 0, 150, player.getGeoIndex()), _instanceID);
		}
	}

	public static void paralyzePlayers()
	{
		final Skill revengeSkill = SkillTable.getInstance().getInfo(4515, 1);
		for(final Player player : getPlayers(players_list1, players_list2))
		{
			if(player == null)
				continue;
			healPlayer(player);
			player.inEvent = true;
			if(Config.EVENT_RESTRICTED_ITEMS.length > 0)
				for(final ItemInstance item : player.getInventory().getItems())
					if(item != null && item.isEquipped() && ArrayUtils.contains(Config.EVENT_RESTRICTED_ITEMS, item.getItemId()))
						player.getInventory().unEquipItem(item);
			if(Config.EVENT_RESTRICTED_SKILLS.length > 0)
				for(final Skill skill : player.getAllSkills())
					if(ArrayUtils.contains(Config.EVENT_RESTRICTED_SKILLS, skill.getId()))
						player.addUnActiveSkill(skill);
			stopEffects(player, removeEffects);
			player.getAbnormalList().stop(EffectType.Paralyze);
			player.getAbnormalList().stop(EffectType.Petrification);
			revengeSkill.getEffects(player, player, false, false);
			if(player.getServitor() == null)
				continue;
			revengeSkill.getEffects(player, player.getServitor(), false, false);
		}
	}

	public static void upParalyzePlayers()
	{
		for(final Player player : getPlayers(players_list1, players_list2))
			if(player != null)
			{
				player.getAbnormalList().stop(4515);
				if(player.getServitor() == null)
					continue;
				player.getServitor().getAbnormalList().stop(4515);
			}
	}

	private static void ressurectPlayers()
	{
		for(final Player player : getPlayers(players_list1, players_list2))
		{
			healPlayer(player);
			if(Config.EVENT_RESTRICTED_SKILLS.length > 0)
				for(final Skill skill : player.getAllSkills())
					if(ArrayUtils.contains(Config.EVENT_RESTRICTED_SKILLS, skill.getId()))
						player.removeUnActiveSkill(skill);
		}
	}

	private static void cleanPlayers()
	{
		for(final Player player : getPlayers(players_list1))
			if(player != null)
				if(!checkPlayer(player, false))
					removePlayer(player);
				else
					player.setTeam(1, true);
		for(final Player player : getPlayers(players_list2))
			if(player != null)
				if(!checkPlayer(player, false))
					removePlayer(player);
				else
					player.setTeam(2, true);
	}

	private static void removeAura()
	{
		for(final Player player : getPlayers(players_list1, players_list2))
			if(player != null)
			{
				player.setTeam(0, false);
				player.inEvent = false;
			}
	}

	public static void clearArena()
	{
		for(final GameObject obj : _zone.getObjects())
			if(obj != null)
			{
				final Player player = obj.getPlayer();
				if(player != null && !players_list1.contains(player.getObjectId()) && !players_list2.contains(player.getObjectId()) && !player.isGM() && player.getReflectionId() == _instanceID)
					player.teleToLocation(ClearLoc);
			}
	}

	public static void OnDie(final Creature self, final Creature killer)
	{
		if(_status > 1 && self != null && self.isPlayer() && self.getTeam() > 0 && (players_list1.contains(self.getObjectId()) || players_list2.contains(self.getObjectId())))
		{
			dropFlag((Player) self);
			final Player pk = killer.getPlayer();
			if(pk != null)
			{
				boolean rew = false;
				if(players_list1.contains(pk.getObjectId()) && players_list2.contains(self.getObjectId()))
					rew = true;
				else if(players_list2.contains(pk.getObjectId()) && players_list1.contains(self.getObjectId()))
					rew = true;
				if(rew)
				{
					final Player l2Player = pk;
					++l2Player.eventKills;
					if(Config.CtF_reward.length > 1)
						for(int i = 0; i < Config.CtF_reward.length; i += 2)
							addItem(pk, Config.CtF_reward[i], Math.round((Config.CtF_rate ? pk.getLevel() : 1) * Config.CtF_reward[i + 1]));
				}
			}
			ThreadPoolManager.getInstance().schedule(new TeleportRes(self.getObjectId()), Config.CtFResDelay * 1000L);
			self.sendMessage(new CustomMessage("scripts.events.CtF.Ressurection").addNumber(Config.CtFResDelay));
		}
	}

	public static Location OnEscape(final Player player)
	{
		if(_status > 1 && player != null && player.getTeam() > 0 && (players_list1.contains(player.getObjectId()) || players_list2.contains(player.getObjectId())))
			removePlayer(player);
		return null;
	}

	public static void OnPlayerExit(final Player player)
	{
		if(player == null || player.getTeam() < 1)
			return;
		if(_status == 0 && _isRegistrationActive && player.getTeam() > 0 && (players_list1.contains(player.getObjectId()) || players_list2.contains(player.getObjectId())))
		{
			removePlayer(player);
			return;
		}
		if(_status == 1 && (players_list1.contains(player.getObjectId()) || players_list2.contains(player.getObjectId())))
		{
			removePlayer(player);
			backPlayer(player);
			return;
		}
		OnEscape(player);
	}

	private static void backPlayer(final Player player)
	{
		if(player == null)
			return;
		if(useBC)
			try
			{
				final String var = player.getVar("CtF_backCoords");
				if(var == null)
					return;
				if(var.equals(""))
				{
					player.unsetVar("CtF_backCoords");
					return;
				}
				final String[] coords = var.split(" ");
				if(coords.length < 3)
					return;
				player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
				player.unsetVar("CtF_backCoords");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		else
			player.teleToLocation(Location.findAroundPosition(Config.CtF_ReturnPoint[0], Config.CtF_ReturnPoint[1], Config.CtF_ReturnPoint[2], 0, 150, player.getGeoIndex()));
	}

	private static void unsetLastCoords()
	{
		if(useBC)
			mysql.set("DELETE FROM `character_variables` WHERE `name`='CtF_backCoords'");
	}

	private static void removePlayer(final Player player)
	{
		if(player != null)
		{
			players_list1.remove(player.getObjectId());
			players_list2.remove(player.getObjectId());
			player.setTeam(0, false);
			player.inEvent = false;
			dropFlag(player);
		}
	}

	private static void addFlag(final Player player, final boolean red)
	{
		final ItemInstance item = ItemTable.getInstance().createItem(6718);
		item.setCustomFlags(102, false);
		if(red)
			item.setPriceToSell(1);
		player.getInventory().addItem(item);
		player.getInventory().equipItem(item, false);
		player.sendChanges();
		player.sendMessage("\u0412\u044b \u0434\u043e\u0431\u044b\u043b\u0438 \u044d\u0432\u0435\u043d\u0442\u043e\u0432\u044b\u0439 \u0444\u043b\u0430\u0433! \u0421\u043a\u043e\u0440\u0435\u0435 \u0432\u0435\u0440\u043d\u0438\u0442\u0435\u0441\u044c \u043a \u0441\u0432\u043e\u0435\u0439 \u0431\u0430\u0437\u0435!");
		player.broadcastPacket(new L2GameServerPacket[] { new SocialAction(player.getObjectId(), 16) });
		player.sendPacket(new RadarControl(0, 2, player.getTeam() == 1 ? BlueFlagLoc : RedFlagLoc));
	}

	private static void removeFlags()
	{
		for(final Player player : getPlayers(players_list1, players_list2))
			removeFlag(player);
	}

	private static void removeFlag(final Player player)
	{
		if(player != null && player.isFlagEquipped())
		{
			final ItemInstance flag = player.getActiveWeaponInstance();
			if(flag != null)
			{
				flag.setCustomFlags(0, false);
				player.getInventory().destroyItem(flag, 1L, false);
				player.sendPacket(new RadarControl(1, 2, 0, 0, 0));
				player.broadcastUserInfo(true);
			}
		}
	}

	private static void dropFlag(final Player player)
	{
		if(player != null && player.isFlagEquipped())
		{
			final ItemInstance flag = player.getActiveWeaponInstance();
			if(flag != null)
			{
				final boolean red = flag.getPriceToSell() == 1;
				flag.setCustomFlags(0, false);
				player.getInventory().destroyItem(flag, 1L, false);
				player.sendPacket(new RadarControl(1, 2, 0, 0, 0));
				player.broadcastUserInfo(true);
				if(red)
				{
					if(redFlag != null && !redFlag.isVisible())
					{
						redFlag.spawnMe();
						noRedFlag = false;
					}
				}
				else if(blueFlag != null && !blueFlag.isVisible())
				{
					blueFlag.spawnMe();
					noBlueFlag = false;
				}
			}
		}
	}

	private static CtFInstance spawnNpc(final Location loc, final int npcId, final int instanceId)
	{
		try
		{
			final CtFInstance npc = (CtFInstance) NpcTable.getTemplate(npcId).getNewInstance();
			npc.setSpawnedLoc(loc.correctGeoZ(npc.getGeoIndex()));
			npc.setReflectionId(instanceId);
			npc.spawnMe(npc.getSpawnedLoc());
			return npc;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void preLoad()
	{
		int day;
		if(Config.CtF_Allow_Calendar_Day)
			day = 4;
		else
			day = 3;
		for(int i = 0; i < Config.CtF_Time_Start.length; i += day)
		{
			if(Config.CtF_Allow_Calendar_Day)
			{
				_date.set(5, Config.CtF_Time_Start[i]);
				_date.set(11, Config.CtF_Time_Start[i + 1]);
				_date.set(12, Config.CtF_Time_Start[i + 2]);
			}
			else
			{
				_date.set(11, Config.CtF_Time_Start[i]);
				_date.set(12, Config.CtF_Time_Start[i + 1]);
			}
			if(_date.getTimeInMillis() > System.currentTimeMillis() + 2000L)
			{
				if(Config.CtF_Allow_Calendar_Day)
					_pre_category = Config.CtF_Time_Start[i + 3];
				else
					_pre_category = Config.CtF_Time_Start[i + 2];
				executeTask("events.CtF.CtF", "preStartTask", new Object[0], (int) (_date.getTimeInMillis() - System.currentTimeMillis()));
				break;
			}
		}
	}

	public static void addDay()
	{
		_date.add(6, 1);
		(date = Calendar.getInstance()).set(11, 0);
		date.set(12, 0);
		date.set(13, 30);
		date.add(6, 1);
		if(date.getTimeInMillis() > System.currentTimeMillis())
			executeTask("events.CtF.CtF", "addDay", new Object[0], (int) (date.getTimeInMillis() - System.currentTimeMillis()));
		preLoad();
	}

	public static void preStartTask()
	{
		if(_active)
			startOk(new String[] { String.valueOf(_pre_category) });
	}

	static
	{
		_log = LoggerFactory.getLogger(CtF.class);
		players_list1 = new CopyOnWriteArrayList<Integer>();
		players_list2 = new CopyOnWriteArrayList<Integer>();
		removeEffects = new int[] { 442, 443, 1411, 1418, 1427 };
		flags1 = 0;
		flags2 = 0;
		_instanceID = Config.CtF_Instance ? 103 : 0;
		useBC = Config.CtF_ReturnPoint.length < 3;
		redFlag = null;
		blueFlag = null;
		redThrone = null;
		blueThrone = null;
		_isRegistrationActive = false;
		_status = 0;
		_zone = ZoneManager.getInstance().getZone(Config.CtF_Zone);
		_zoneListener = new ZoneListener();
		BlueTeamLoc = Location.parseLoc(Config.CtF_BlueTeamLoc);
		RedTeamLoc = Location.parseLoc(Config.CtF_RedTeamLoc);
		BlueTeamResLoc = Location.parseLoc(Config.CtF_BlueTeamResLoc);
		RedTeamResLoc = Location.parseLoc(Config.CtF_RedTeamResLoc);
		BlueFlagLoc = Location.parseLoc(Config.CtF_BlueFlagLoc);
		RedFlagLoc = Location.parseLoc(Config.CtF_RedFlagLoc);
		ClearLoc = Location.parseLoc(Config.CtF_ClearLoc);
		BS_Active = false;
		_active = false;
	}

	private static class ZoneListener extends ZoneEnterLeaveListener
	{
		@Override
		public void objectEntered(final Zone zone, final GameObject object)
		{
			if(object == null)
				return;
			final Player player = object.getPlayer();
			if(_status > 0 && player != null && !players_list1.contains(player.getObjectId()) && !players_list2.contains(player.getObjectId()) && !player.isGM() && player.getReflectionId() == _instanceID)
				ThreadPoolManager.getInstance().schedule(new TeleportTask((Creature) object, ClearLoc), 3000L);
		}

		@Override
		public void objectLeaved(final Zone zone, final GameObject object)
		{
			if(object == null)
				return;
			final Player player = object.getPlayer();
			if(_status > 1 && player != null && player.getTeam() > 0 && (players_list1.contains(player.getObjectId()) || players_list2.contains(player.getObjectId())) && player.getReflectionId() == _instanceID)
			{
				final double angle = Util.convertHeadingToDegree(object.getHeading());
				final double radian = Math.toRadians(angle - 90.0);
				final int x = (int) (object.getX() + 50.0 * Math.sin(radian));
				final int y = (int) (object.getY() - 50.0 * Math.cos(radian));
				final int z = object.getZ();
				ThreadPoolManager.getInstance().schedule(new TeleportTask((Creature) object, new Location(x, y, z)), 3000L);
			}
		}
	}

	public static class TeleportTask implements Runnable
	{
		Location loc;
		Creature target;

		public TeleportTask(final Creature target, final Location loc)
		{
			this.target = target;
			this.loc = loc;
			target.startStunning();
		}

		@Override
		public void run()
		{
			target.stopStunning();
			target.teleToLocation(loc, _instanceID);
		}
	}

	private static class TeleportRes implements Runnable
	{
		private final int playerObjectId;

		public TeleportRes(int id)
		{
			playerObjectId = id;
		}

		@Override
		public void run()
		{
			final Player player = GameObjectsStorage.getPlayer(playerObjectId);
			if(player != null && _status > 1 && player.getTeam() > 0)
			{
				if(player.isDead())
				{
					player.restoreExp();
					player.broadcastPacket(new L2GameServerPacket[] { new Revive(player) });
					player.setPendingRevive(true);
				}
				player.teleToLocation(player.getTeam() == 1 ? BlueTeamResLoc : RedTeamResLoc, _instanceID);
				if(player.isMageClass())
				{
					if(Config.EVENT_BUFFS_MAGE.length > 1)
					{
						int n = 0;
						for(int i = 0; i < Config.EVENT_BUFFS_MAGE.length; i += 2)
							OlympiadGame.giveBuff(player, SkillTable.getInstance().getInfo(Config.EVENT_BUFFS_MAGE[i], Config.EVENT_BUFFS_MAGE[i + 1]), n++);
					}
				}
				else if(Config.EVENT_BUFFS_FIGHTER.length > 1)
				{
					int n = 0;
					for(int i = 0; i < Config.EVENT_BUFFS_FIGHTER.length; i += 2)
						OlympiadGame.giveBuff(player, SkillTable.getInstance().getInfo(Config.EVENT_BUFFS_FIGHTER[i], Config.EVENT_BUFFS_FIGHTER[i + 1]), n++);
				}
			}
		}
	}
}
