package events.TvT;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import l2s.commons.geometry.Polygon;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.data.xml.XMLDocumentFactory;
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
import l2s.gameserver.model.Territory;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.model.entity.olympiad.OlympiadGame;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.Revive;
import l2s.gameserver.network.l2.s2c.Say2;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.EffectType;
import l2s.gameserver.skills.Env;
import l2s.gameserver.tables.DoorTable;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.tables.TerritoryTable;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Util;

public class TvT extends Functions implements ScriptFile
{
	private static Logger _log;
	private static List<Integer> players_list1;
	private static List<Integer> players_list2;
	private static List<Integer> live_list1;
	private static List<Integer> live_list2;
	private static List<String> _restrict;
	private static final int[] removeEffects;
	private static int count1;
	private static int count2;
	private static boolean _clearing;
	private static final int _instanceID;
	private static final boolean useBC;
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
	private static final Location ClearLoc;
	private static Calendar _date;
	private static Calendar date;
	private static boolean BS_Active;
	private static Territory team1loc;
	private static Territory team2loc;
	private static Map<Integer, Future<?>> _tasks;
	private static Map<Integer, List<Integer>> _equip;
	private static Map<Integer, List<Integer>> _destroy;
	private static Map<Integer, List<Abnormal>> _sbuffs;
	private static boolean _active;

	@Override
	public void onLoad()
	{
		TvT._zone.getListenerEngine().addMethodInvokedListener(TvT._zoneListener);
		TvT._active = ServerVariables.getString("TvT", "off").equalsIgnoreCase("on");
		loadCustomItems();
		if(TvT._active)
			executeTask("events.TvT.TvT", "preLoad", new Object[0], 70000L);
		TvT._date = Calendar.getInstance();
		(TvT.date = (Calendar) TvT._date.clone()).set(11, 0);
		TvT.date.set(12, 0);
		TvT.date.set(13, 30);
		TvT.date.add(6, 1);
		if(!Config.TvT_Allow_Calendar_Day && TvT.date.getTimeInMillis() > System.currentTimeMillis())
			executeTask("events.TvT.TvT", "addDay", new Object[0], (int) (TvT.date.getTimeInMillis() - System.currentTimeMillis()));
		TvT._log.info("Loaded Event: TvT");
	}

	@Override
	public void onReload()
	{
		TvT._zone.getListenerEngine().removeMethodInvokedListener(TvT._zoneListener);
	}

	@Override
	public void onShutdown()
	{
		onReload();
	}

	public static void activateBS()
	{
		TvT.BS_Active = true;
	}

	public void activateEvent()
	{
		final Player player = getSelf();
		if(player == null || !player.getPlayerAccess().IsEventGm)
			return;
		if(!TvT._active)
		{
			executeTask("events.TvT.TvT", "preLoad", new Object[0], 10000L);
			ServerVariables.set("TvT", "on");
			TvT._log.info("Event 'TvT' activated.");
			player.sendMessage(new CustomMessage("scripts.events.TvT.AnnounceEventStarted"));
		}
		else
			player.sendMessage(player.isLangRus() ? "'TvT' Эвент уже активен." : "Event 'TvT' already active.");
		TvT._active = true;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void deactivateEvent()
	{
		final Player player = getSelf();
		if(player == null || !player.getPlayerAccess().IsEventGm)
			return;
		if(TvT._active)
		{
			ServerVariables.unset("TvT");
			TvT._log.info("Event 'TvT' deactivated.");
			player.sendMessage(new CustomMessage("scripts.events.TvT.AnnounceEventStoped"));
		}
		else
			player.sendMessage("Event 'TvT' not active.");
		TvT._active = false;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public static boolean isRunned(final String name)
	{
		return (TvT._isRegistrationActive || TvT._status > 0) && TvT._instanceID == 0 && name.equals(Config.TvT_Zone);
	}

	public String DialogAppend_31225(final Integer val)
	{
		if(val != 0)
			return "";
		final Player player = getSelf();
		if(player == null)
			return "";
		return HtmCache.getInstance().getHtml("scripts/events/TvT/31225.html", player);
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
		if(TvT._instanceID == 0 && (isEventStarted("events.CtF.CtF", Config.TvT_Zone) || isEventStarted("events.lastHero.LastHero", Config.TvT_Zone)))
		{
			TvT._log.info("TvT not started: another event is already running");
			return;
		}
		if(TvT._isRegistrationActive || TvT._status > 0)
			return;
		try
		{
			TvT._category = Integer.valueOf(var[0]);
		}
		catch(Exception e)
		{
			TvT._log.info("TvT not started: can't parse category");
			return;
		}
		if(TvT._category == -1)
		{
			TvT._minLevel = 1;
			TvT._maxLevel = 80;
		}
		else
		{
			TvT._minLevel = getMinLevelForCategory(TvT._category);
			TvT._maxLevel = getMaxLevelForCategory(TvT._category);
		}
		if(TvT._endTask != null)
			return;
		TvT._status = 0;
		TvT._isRegistrationActive = true;
		TvT._clearing = false;
		TvT._time_to_start = Config.TvT_Time;
		TvT.players_list1 = new CopyOnWriteArrayList<Integer>();
		TvT.players_list2 = new CopyOnWriteArrayList<Integer>();
		TvT.live_list1 = new CopyOnWriteArrayList<Integer>();
		TvT.live_list2 = new CopyOnWriteArrayList<Integer>();
		TvT._restrict = new ArrayList<String>();
		TvT.count1 = 0;
		TvT.count2 = 0;
		final String[] param = { String.valueOf(TvT._time_to_start), String.valueOf(TvT._minLevel), String.valueOf(TvT._maxLevel) };
		sayToAll("scripts.events.TvT.AnnouncePreStart", param);
		sayToAll("scripts.events.TvT.AnnounceReg", null);
		executeTask("events.TvT.TvT", "question", new Object[0], 5000L);
		executeTask("events.TvT.TvT", "announce", new Object[0], 60000L);
	}

	private static void sayToAll(final String address, final String[] replacements)
	{
		final CustomMessage cm = new CustomMessage(address);
		if(replacements != null)
		{
			for(final String s : replacements)
				cm.addString(s);
		}

		for(final Player player : GameObjectsStorage.getPlayers())
		{
			if(player != null)
				player.sendPacket(new Say2(0, ChatType.CRITICAL_ANNOUNCE, "TvT", "TvT: " + cm.toString(player)));
		}
	}

	private static void sayToParticipants(final String address, final String[] replacements)
	{
		final CustomMessage cm = new CustomMessage(address);
		if(replacements != null)
		{
			for(final String s : replacements)
				cm.addString(s);
		}

		for(final Player player : getPlayers(TvT.live_list1, TvT.live_list2))
		{
			if(player != null)
				player.sendPacket(new Say2(0, ChatType.CRITICAL_ANNOUNCE, "TvT", "TvT: " + cm.toString(player)));
		}
	}

	public static void question()
	{
		for(final Player player : GameObjectsStorage.getPlayers())
			if(player != null && player.getLevel() >= TvT._minLevel && player.getLevel() <= TvT._maxLevel && player.getReflectionId() == 0 && !player.isInOlympiadMode() && !player.isCursedWeaponEquipped() && !player.inObserverMode() && !player.inEvent() && !player.getVarBoolean("NoEventAsk", Config.EVENT_NO_ASK))
				player.scriptRequest(new CustomMessage("scripts.events.TvT.AskPlayer").toString(player), "events.TvT.TvT:addPlayer", new Object[0]);
	}

	public static void announce()
	{
		if(TvT._time_to_start > 1)
		{
			--TvT._time_to_start;
			final String[] param = { String.valueOf(TvT._time_to_start), String.valueOf(TvT._minLevel), String.valueOf(TvT._maxLevel) };
			sayToAll("scripts.events.TvT.AnnouncePreStart", param);
			executeTask("events.TvT.TvT", "announce", new Object[0], 60000L);
		}
		else
		{
			if(TvT.players_list1.isEmpty() || TvT.players_list2.isEmpty() || TvT.players_list1.size() + TvT.players_list2.size() < Config.TvT_MinPlayers)
			{
				sayToAll("scripts.events.TvT.AnnounceEventCancelled", null);
				TvT._isRegistrationActive = false;
				TvT._status = 0;
				executeTask("events.TvT.TvT", "preLoad", new Object[0], 10000L);
				return;
			}
			TvT._status = 1;
			TvT._isRegistrationActive = false;
			sayToAll("scripts.events.TvT.AnnounceEventStarting", null);
			executeTask("events.TvT.TvT", "prepare", new Object[0], 5000L);
		}
	}

	public void addPlayer()
	{
		final Player player = getSelf();
		if(player == null || !checkPlayer(player, true))
			return;
		if(TvT.players_list1.size() + TvT.players_list2.size() >= Config.TvT_MaxPlayers)
		{
			player.sendMessage("Достигнут лимит допустимого кол-ва участников.");
			return;
		}
		if(Config.TvT_IP)
		{
			if(TvT._restrict.contains(player.getIP()))
			{
				player.sendMessage("Игрок с данным IP уже зарегистрирован.");
				return;
			}
			TvT._restrict.add(player.getIP());
		}
		if(Config.TvT_HWID)
		{
			if(TvT._restrict.contains(player.getHWID()))
			{
				player.sendMessage("Игрок с данным железом уже зарегистрирован.");
				return;
			}
			TvT._restrict.add(player.getHWID());
		}
		int team = 0;
		final int size1 = TvT.players_list1.size();
		final int size2 = TvT.players_list2.size();
		if(size1 > size2)
			team = 2;
		else if(size1 < size2)
			team = 1;
		else
			team = Rnd.get(1, 2);
		if(team == 1)
		{
			TvT.players_list1.add(player.getObjectId());
			TvT.live_list1.add(player.getObjectId());
			show(new CustomMessage("scripts.events.TvT.Registered"), player);
		}
		else if(team == 2)
		{
			TvT.players_list2.add(player.getObjectId());
			TvT.live_list2.add(player.getObjectId());
			show(new CustomMessage("scripts.events.TvT.Registered"), player);
		}
		else
			TvT._log.info("WTF??? Command id 0 in TvT...");
	}

	public static boolean checkPlayer(final Player player, final boolean first)
	{
		if(first && !TvT._isRegistrationActive)
		{
			show(new CustomMessage("scripts.events.Late"), player);
			return false;
		}
		if(first && (TvT.players_list1.contains(player.getObjectId()) || TvT.players_list2.contains(player.getObjectId())))
		{
			show(new CustomMessage("scripts.events.TvT.Cancelled"), player);
			return false;
		}
		if(player.getLevel() < TvT._minLevel || player.getLevel() > TvT._maxLevel)
		{
			show(new CustomMessage("scripts.events.TvT.CancelledLevel"), player);
			return false;
		}
		if(player.isMounted())
		{
			player.sendMessage(player.isLangRus() ? "\u0412\u0435\u0440\u0445\u043e\u043c \u043d\u0430 \u044d\u0432\u0435\u043d\u0442 \u043d\u0435\u043b\u044c\u0437\u044f." : "You can't do it while riding.");
			return false;
		}
		if(player.isInDuel())
		{
			show(new CustomMessage("scripts.events.TvT.CancelledDuel"), player);
			return false;
		}
		if(player.getTeam() != 0)
		{
			show(new CustomMessage("scripts.events.TvT.CancelledOtherEvent"), player);
			return false;
		}
		if(player.getOlympiadGameId() > 0 || player.isInOlympiadMode() || first && Olympiad.isRegistered(player))
		{
			show(new CustomMessage("scripts.events.TvT.CancelledOlympiad"), player);
			return false;
		}
		if(player.isInParty() && player.getParty().isInDimensionalRift())
		{
			show(new CustomMessage("scripts.events.TvT.CancelledOtherEvent"), player);
			return false;
		}
		if(player.isTeleporting())
		{
			show(new CustomMessage("scripts.events.TvT.CancelledTeleport"), player);
			return false;
		}
		if(player.isCursedWeaponEquipped())
		{
			player.sendMessage(player.isLangRus() ? "С проклятым оружием на эвент нельзя." : "You can't do it with cursed weapon.");
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
		if(Config.TvT_CustomItems && player.getActiveClassId() < 88)
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u0430 \u0442\u0440\u0435\u0442\u044c\u044f \u043f\u0440\u043e\u0444\u0435\u0441\u0441\u0438\u044f." : "You must have 3rd class.");
			return false;
		}
		return true;
	}

	public static void prepare()
	{
		if(TvT._instanceID == 0 && TvT._zone.getIndex() == 4 && TvT._zone.getType() == Zone.ZoneType.battle_zone)
		{
			DoorTable.getInstance().getDoor(24190002).closeMe();
			DoorTable.getInstance().getDoor(24190003).closeMe();
		}
		cleanPlayers();
		final int size = TvT.players_list1.size() + TvT.players_list2.size();
		if(TvT.players_list1.isEmpty() || TvT.players_list2.isEmpty() || size < Config.TvT_MinPlayers)
		{
			sayToAll("scripts.events.TvT.AnnounceEventCancelled", null);
			TvT._isRegistrationActive = false;
			TvT._status = 0;
			executeTask("events.TvT.TvT", "preLoad", new Object[0], 10000L);
			return;
		}
		clearArena();
		TvT._sbuffs = new ConcurrentHashMap<Integer, List<Abnormal>>(size);
		if(Config.TvT_CustomItems)
		{
			TvT._equip = new ConcurrentHashMap<Integer, List<Integer>>(size);
			TvT._destroy = new ConcurrentHashMap<Integer, List<Integer>>(size);
		}
		executeTask("events.TvT.TvT", "paralyzePlayers", new Object[0], 100L);
		executeTask("events.TvT.TvT", "teleportPlayersToColiseum", new Object[0], 3000L);
		executeTask("events.TvT.TvT", "go", new Object[0], Config.TvT_Time_Paralyze * 1000);
		sayToParticipants("scripts.events.TvT.AnnounceFinalCountdown", new String[] { String.valueOf(Config.TvT_Time_Paralyze) });
	}

	public static void go()
	{
		TvT._status = 2;
		if(Config.TvT_CancelAllBuff)
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
		sayToParticipants("scripts.events.TvT.AnnounceFight", null);
		TvT._endTask = executeTask("events.TvT.TvT", "endBattle", new Object[0], Config.TvT_Time_Battle * 60000);
	}

	private static void saveBuffs(final Player player)
	{
		final List<Abnormal> effectList = player.getAbnormalList().values();
		final List<Abnormal> effects = new ArrayList<Abnormal>(effectList.size());
		for(final Abnormal e : effectList)
			if(!e.getSkill().isToggle())
			{
				final Abnormal effect = e.getTemplate().getEffect(new Env(e.getEffector(), e.getEffected(), e.getSkill()));
				effect.setCount(e.getCount());
				effect.setPeriod(e.getCount() == 1 ? e.getPeriod() - e.getTime() : e.getPeriod());
				effects.add(effect);
			}
		if(!effects.isEmpty())
			TvT._sbuffs.put(player.getObjectId(), effects);
	}

	private static void buffPlayers()
	{
		if(Config.EVENT_BUFFS_FIGHTER.length < 2 && Config.EVENT_BUFFS_MAGE.length < 2)
			return;
		for(final Player player : getPlayers(TvT.players_list1, TvT.players_list2))
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
		for(final Player player : getPlayers(TvT.players_list1, TvT.players_list2))
			if(player != null && !player.isInZone(TvT._zone))
			{
				removePlayer(player);
				if(TvT.useBC && player.getVar("TvT_backCoords") == null)
					player.teleToLocation(TvT.ClearLoc);
				else
					backPlayer(player);
			}
		if(TvT.players_list1.size() < 1 || TvT.players_list2.size() < 1)
		{
			endBattle();
			return true;
		}
		return false;
	}

	private static void removeBuff()
	{
		for(final Player player : getPlayers(TvT.players_list1, TvT.players_list2))
			if(player != null)
				try
				{
					player.getAbnormalList().stopAll();
					final Servitor summon = player.getServitor();
					if(summon != null)
					{
						summon.getAbnormalList().stopAll();
						if(summon.isPet() || Config.EVENT_RESTRICTED_SUMMONS.length > 0 && ArrayUtils.contains(Config.EVENT_RESTRICTED_SUMMONS, summon.getNpcId()))
							summon.unSummon();
					}
					if(Config.TvT_NonActionDelay <= 0)
						continue;
					player.eventAct = false;
					TvT._tasks.put(player.getObjectId(), ThreadPoolManager.getInstance().schedule(new ActionCheck(player.getObjectId()), Config.TvT_NonActionDelay * 1000));
				}
				catch(Exception e)
				{
					TvT._log.error("on removeBuff", e);
				}
	}

	public static void endBattle()
	{
		if(TvT._clearing)
			return;
		TvT._clearing = true;
		try
		{
			if(TvT._endTask != null)
			{
				TvT._endTask.cancel(false);
				TvT._endTask = null;
			}
		}
		catch(Exception ex)
		{}
		if(TvT._status == 0)
			return;
		TvT._status = 0;
		removeAura();
		if(TvT._instanceID == 0 && TvT._zone.getIndex() == 4 && TvT._zone.getType() == Zone.ZoneType.battle_zone)
		{
			DoorTable.getInstance().getDoor(24190002).openMe();
			DoorTable.getInstance().getDoor(24190003).openMe();
		}
		final String[] param = { String.valueOf(TvT.count1), String.valueOf(TvT.count2) };
		if(TvT.live_list1.isEmpty() && TvT.live_list2.isEmpty())
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedDraw", null);
			if(Config.TvT_DrawReward)
				giveItemsToWinner(0);
		}
		else if(TvT.live_list1.isEmpty())
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedRedWins", param);
			giveItemsToWinner(2);
		}
		else if(TvT.live_list2.isEmpty())
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedBlueWins", param);
			giveItemsToWinner(1);
		}
		else if(TvT.count1 > TvT.count2)
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedBlueWins", param);
			giveItemsToWinner(1);
		}
		else if(TvT.count2 > TvT.count1)
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedRedWins", param);
			giveItemsToWinner(2);
		}
		else
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedDraw", null);
			if(Config.TvT_DrawReward)
				giveItemsToWinner(0);
		}
		if(Config.TvT_NonActionDelay > 0)
		{
			for(final Future<?> task : TvT._tasks.values())
				try
				{
					task.cancel(true);
				}
				catch(Exception e)
				{}
			TvT._tasks.clear();
		}
		sayToParticipants("scripts.events.TvT.AnnounceEnd", new String[] { String.valueOf(Config.EVENTS_TIME_BACK) });
		executeTask("events.TvT.TvT", "end", new Object[0], Config.EVENTS_TIME_BACK * 1000);
		TvT._isRegistrationActive = false;
		TvT._sbuffs.clear();
		TvT.count1 = 0;
		TvT.count2 = 0;
		TvT._clearing = false;
	}

	public static void end()
	{
		ressurectPlayers();
		executeTask("events.TvT.TvT", "teleportPlayersToSavedCoords", new Object[0], 100L);
		executeTask("events.TvT.TvT", "preLoad", new Object[0], 10000L);
	}

	private static void giveItemsToWinner(final int team)
	{
		for(final Player player : team == 1 ? getPlayers(TvT.players_list1) : team == 2 ? getPlayers(TvT.players_list2) : getPlayers(TvT.players_list1, TvT.players_list2))
			if(player != null)
			{
				if(player.eventKills >= Config.TvT_MinKills)
					for(int i = 0; i < Config.TvT_reward_final.length; i += 2)
						addItem(player, Config.TvT_reward_final[i], Config.TvT_reward_final[i + 1]);
				if(!TvT.BS_Active)
					continue;
				Functions.callScripts("services.BonusStats", "event", new Object[] { player });
			}
		if(team != 0 && Config.TvT_reward_losers.length > 1)
			for(final Player player : getPlayers(team == 1 ? TvT.players_list2 : TvT.players_list1))
				if(player != null && player.eventKills >= Config.TvT_LosersMinKills)
					for(int i = 0; i < Config.TvT_reward_losers.length; i += 2)
						addItem(player, Config.TvT_reward_losers[i], Config.TvT_reward_losers[i + 1]);
	}

	public static void teleportPlayersToColiseum()
	{
		for(final Player player : getPlayers(TvT.players_list1, TvT.players_list2))
		{
			if(player == null)
				continue;
			player.eventKills = 0;
			unRide(player);
			unSummonPet(player, true);
			if(TvT.useBC)
				player.setVar("TvT_backCoords", player.isInZone(Zone.ZoneType.no_restart) || player.isInZone(Zone.ZoneType.epic) ? TvT.ClearLoc.getX() + " " + TvT.ClearLoc.getY() + " " + TvT.ClearLoc.getZ() : player.getX() + " " + player.getY() + " " + player.getZ());
			if(player.getParty() != null)
				player.getParty().oustPartyMember(player);
			if(Config.TvT_Zone.equals("[colosseum_battle]"))
			{
				final Location pos = player.getTeam() == 1 ? TvT.team1loc.getRandomLoc(player.getGeoIndex()) : TvT.team2loc.getRandomLoc(player.getGeoIndex());
				player.teleToLocation(pos.x, pos.y, -3410, TvT._instanceID);
			}
			else
				player.teleToLocation(Location.findAroundPosition(player.getTeam() == 1 ? TvT.BlueTeamLoc : TvT.RedTeamLoc, 0, 150, player.getGeoIndex()), TvT._instanceID);
		}
	}

	public static void teleportPlayersToSavedCoords()
	{
		for(final Player player : getPlayers(TvT.players_list1, TvT.players_list2))
			if(player != null)
				backPlayer(player);
		unsetLastCoords();
	}

	public static void paralyzePlayers()
	{
		final Skill sk = SkillTable.getInstance().getInfo(4515, 1);
		for(final Player player : getPlayers(TvT.players_list1, TvT.players_list2))
		{
			if(player == null)
				continue;
			healPlayer(player);
			player.inEvent = true;
			player.inTvT = true;
			saveBuffs(player);
			if(Config.TvT_CustomItems)
			{
				final List<Integer> items = new ArrayList<Integer>();
				for(final ItemInstance item : player.getInventory().getPaperdollItems())
					if(item != null)
					{
						player.getInventory().unEquipItem(item);
						items.add(item.getObjectId());
					}
				if(!items.isEmpty())
					TvT._equip.put(player.getObjectId(), items);
				final List<Integer> cm = new ArrayList<Integer>();
				final ItemTable itemTable = ItemTable.getInstance();
				for(final int id : Config.TVT_CUSTOM_ITEMS.get(player.getActiveClassId()))
				{
					final ItemInstance item2 = itemTable.createItem(id);
					if(item2.canBeEnchanted())
						item2.setEnchantLevel(Config.TvT_CustomItemsEnchant);
					player.getInventory().addItem(item2);
					if(item2.isEquipable() && !item2.isArrow())
						player.getInventory().equipItem(item2, false);
					cm.add(item2.getObjectId());
				}
				if(!cm.isEmpty())
					TvT._destroy.put(player.getObjectId(), cm);
			}
			if(Config.EVENT_RESTRICTED_ITEMS.length > 0)
				for(final ItemInstance item3 : player.getInventory().getItems())
					if(item3 != null && item3.isEquipped() && ArrayUtils.contains(Config.EVENT_RESTRICTED_ITEMS, item3.getItemId()))
						player.getInventory().unEquipItem(item3);
			if(Config.EVENT_RESTRICTED_SKILLS.length > 0)
				for(final Skill skill : player.getAllSkills())
					if(ArrayUtils.contains(Config.EVENT_RESTRICTED_SKILLS, skill.getId()))
						player.addUnActiveSkill(skill);
			stopEffects(player, TvT.removeEffects);
			player.getAbnormalList().stop(EffectType.Paralyze);
			player.getAbnormalList().stop(EffectType.Petrification);
			sk.getEffects(player, player, false, false);
			if(player.getServitor() == null)
				continue;
			sk.getEffects(player, player.getServitor(), false, false);
		}
	}

	public static void upParalyzePlayers()
	{
		for(final Player player : getPlayers(TvT.players_list1, TvT.players_list2))
			if(player != null)
			{
				player.getAbnormalList().stop(4515);
				if(player.getServitor() != null)
					player.getServitor().getAbnormalList().stop(4515);
				if(Config.TvT_NonActionDelay <= 0)
					continue;
				player.eventAct = false;
				TvT._tasks.put(player.getObjectId(), ThreadPoolManager.getInstance().schedule(new ActionCheck(player.getObjectId()), Config.TvT_NonActionDelay * 1000));
			}
	}

	private static void ressurectPlayers()
	{
		for(final Player player : getPlayers(TvT.players_list1, TvT.players_list2))
		{
			healPlayer(player);
			if(Config.EVENT_RESTRICTED_SKILLS.length > 0 && player != null)
				for(final Skill skill : player.getAllSkills())
					if(ArrayUtils.contains(Config.EVENT_RESTRICTED_SKILLS, skill.getId()))
						player.removeUnActiveSkill(skill);
		}
	}

	private static void cleanPlayers()
	{
		for(final Player player : getPlayers(TvT.players_list1))
			if(player != null)
				if(!checkPlayer(player, false))
					removePlayer(player);
				else
					player.setTeam(1, true);
		for(final Player player : getPlayers(TvT.players_list2))
			if(player != null)
				if(!checkPlayer(player, false))
					removePlayer(player);
				else
					player.setTeam(2, true);
	}

	private static void checkLive()
	{
		final List<Integer> new_live_list1 = new CopyOnWriteArrayList<Integer>();
		final List<Integer> new_live_list2 = new CopyOnWriteArrayList<Integer>();
		for(final Integer stId : TvT.live_list1)
		{
			final Player player = GameObjectsStorage.getPlayer(stId);
			if(player != null)
				new_live_list1.add(player.getObjectId());
		}
		for(final Integer stId : TvT.live_list2)
		{
			final Player player = GameObjectsStorage.getPlayer(stId);
			if(player != null)
				new_live_list2.add(player.getObjectId());
		}
		TvT.live_list1 = new_live_list1;
		TvT.live_list2 = new_live_list2;
		if(TvT.live_list1.size() < 1 || TvT.live_list2.size() < 1)
			endBattle();
	}

	private static void removeAura()
	{
		for(final Player player : getPlayers(TvT.live_list1, TvT.live_list2))
			if(player != null)
			{
				buffsItems(player);
				player.setTeam(0, false);
				player.inEvent = false;
				player.inTvT = false;
			}
	}

	private static void clearArena()
	{
		for(final GameObject obj : TvT._zone.getObjects())
			if(obj != null)
			{
				final Player player = obj.getPlayer();
				if(player != null && !TvT.live_list1.contains(player.getObjectId()) && !TvT.live_list2.contains(player.getObjectId()) && !player.isGM() && player.getReflectionId() == TvT._instanceID)
					player.teleToLocation(TvT.ClearLoc);
			}
	}

	public static void OnDie(final Creature self, final Creature killer)
	{
		if(TvT._status == 2 && self != null && self.isPlayer() && self.getTeam() > 0 && (TvT.live_list1.contains(self.getObjectId()) || TvT.live_list2.contains(self.getObjectId())))
		{
			final Player pk = killer.getPlayer();
			if(pk != null)
			{
				boolean rew = false;
				if(TvT.live_list1.contains(pk.getObjectId()) && TvT.live_list2.contains(self.getObjectId()))
				{
					++TvT.count1;
					rew = true;
				}
				else if(TvT.live_list2.contains(pk.getObjectId()) && TvT.live_list1.contains(self.getObjectId()))
				{
					++TvT.count2;
					rew = true;
				}
				if(rew)
				{
					final Player l2Player = pk;
					++l2Player.eventKills;
					if(Config.TvT_reward.length > 1)
						for(int i = 0; i < Config.TvT_reward.length; i += 2)
							addItem(pk, Config.TvT_reward[i], Math.round((Config.TvT_rate ? pk.getLevel() : 1) * Config.TvT_reward[i + 1]));
				}
			}
			ThreadPoolManager.getInstance().schedule(new TeleportRes(self.getObjectId()), Config.TvTResDelay * 1000L);
			self.sendMessage(new CustomMessage("scripts.events.TvT.Ressurection").addNumber(Config.TvTResDelay));
			checkLive();
		}
	}

	public static Location OnEscape(final Player player)
	{
		if(TvT._status == 2 && player != null && player.getTeam() > 0 && (TvT.live_list1.contains(player.getObjectId()) || TvT.live_list2.contains(player.getObjectId())))
		{
			removePlayer(player);
			checkLive();
		}
		return null;
	}

	public static void OnPlayerExit(final Player player)
	{
		if(player == null || player.getTeam() < 1)
			return;
		if(TvT._status == 0 && TvT._isRegistrationActive && player.getTeam() > 0 && (TvT.live_list1.contains(player.getObjectId()) || TvT.live_list2.contains(player.getObjectId())))
		{
			removePlayer(player);
			return;
		}
		if(TvT._status == 1 && (TvT.live_list1.contains(player.getObjectId()) || TvT.live_list2.contains(player.getObjectId())))
		{
			removePlayer(player);
			backPlayer(player);
			return;
		}
		OnEscape(player);
	}

	private static void backPlayer(final Player player)
	{
		if(TvT.useBC)
			try
			{
				final String var = player.getVar("TvT_backCoords");
				if(var == null)
					return;
				if(var.equals(""))
				{
					player.unsetVar("TvT_backCoords");
					return;
				}
				final String[] coords = var.split(" ");
				if(coords.length < 3)
					return;
				player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
				player.unsetVar("TvT_backCoords");
			}
			catch(Exception e)
			{
				TvT._log.error("on backPlayer", e);
			}
		else
			player.teleToLocation(Location.findAroundPosition(Config.TvT_ReturnPoint[0], Config.TvT_ReturnPoint[1], Config.TvT_ReturnPoint[2], 0, 150, player.getGeoIndex()));
	}

	private static void unsetLastCoords()
	{
		if(TvT.useBC)
			mysql.set("DELETE FROM `character_variables` WHERE `name`='TvT_backCoords'");
	}

	private static void removePlayer(final Player player)
	{
		if(player != null)
		{
			TvT.live_list1.remove(player.getObjectId());
			TvT.live_list2.remove(player.getObjectId());
			TvT.players_list1.remove(player.getObjectId());
			TvT.players_list2.remove(player.getObjectId());
			buffsItems(player);
			player.setTeam(0, false);
			player.inEvent = false;
			player.inTvT = false;
			if(Config.TvT_NonActionDelay > 0)
				TvT._tasks.remove(player.getObjectId());
		}
	}

	private static void buffsItems(final Player player)
	{
		try
		{
			if(Config.TvT_CustomItems)
			{
				List<Integer> items = TvT._destroy.remove(player.getObjectId());
				if(items != null)
					for(final int id : items)
					{
						final ItemInstance item = player.getInventory().getItemByObjectId(id);
						if(item != null && !item.isArrow())
							player.getInventory().destroyItem(item);
					}
				items = TvT._equip.remove(player.getObjectId());
				if(items != null)
					for(final int id : items)
					{
						final ItemInstance item = player.getInventory().getItemByObjectId(id);
						if(item != null && item.isEquipable() && !item.isArrow())
							player.getInventory().equipItem(item, false);
					}
			}
			player.getAbnormalList().stopAll();
			final List<Abnormal> effectList = TvT._sbuffs.remove(player.getObjectId());
			if(effectList != null)
				for(final Abnormal e : effectList)
					player.getAbnormalList().add(e);
		}
		catch(Exception e2)
		{
			TvT._log.error("on buffsItems", e2);
		}
	}

	private static void loadCustomItems()
	{
		if(Config.TvT_CustomItems)
			try
			{
				final File file = new File("./config/Advanced/tvt_items.xml");
				if(!file.exists())
				{
					TvT._log.error("not found config/Advanced/tvt_items.xml !!!");
					return;
				}
				Config.TVT_CUSTOM_ITEMS = new ConcurrentHashMap<Integer, List<Integer>>(31);
				final Document doc = XMLDocumentFactory.getInstance().loadDocument(file);
				final Node n = doc.getFirstChild();
				for(Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					if("class".equalsIgnoreCase(d.getNodeName()))
					{
						NamedNodeMap attrs = d.getAttributes();
						final int classId = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
						List<Integer> is = null;
						for(Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling())
							if("equipment".equalsIgnoreCase(cd.getNodeName()))
							{
								attrs = cd.getAttributes();
								final String items = attrs.getNamedItem("items").getNodeValue().trim();
								if(items != null)
								{
									final String[] itemsSplit = items.split(",");
									is = new ArrayList<Integer>(itemsSplit.length);
									for(final String element : itemsSplit)
										is.add(Integer.parseInt(element));
								}
							}
						if(is != null)
							Config.TVT_CUSTOM_ITEMS.put(classId, is);
					}
			}
			catch(Exception e)
			{
				TvT._log.error("on tvt_items.xml", e);
			}
	}

	public static void preLoad()
	{
		int day;
		if(Config.TvT_Allow_Calendar_Day)
			day = 4;
		else
			day = 3;
		for(int i = 0; i < Config.TvT_Time_Start.length; i += day)
		{
			if(Config.TvT_Allow_Calendar_Day)
			{
				TvT._date.set(5, Config.TvT_Time_Start[i]);
				TvT._date.set(11, Config.TvT_Time_Start[i + 1]);
				TvT._date.set(12, Config.TvT_Time_Start[i + 2]);
			}
			else
			{
				TvT._date.set(11, Config.TvT_Time_Start[i]);
				TvT._date.set(12, Config.TvT_Time_Start[i + 1]);
			}
			if(TvT._date.getTimeInMillis() > System.currentTimeMillis() + 2000L)
			{
				if(Config.TvT_Allow_Calendar_Day)
					TvT._pre_category = Config.TvT_Time_Start[i + 3];
				else
					TvT._pre_category = Config.TvT_Time_Start[i + 2];
				executeTask("events.TvT.TvT", "preStartTask", new Object[0], (int) (TvT._date.getTimeInMillis() - System.currentTimeMillis()));
				break;
			}
		}
	}

	public static void addDay()
	{
		TvT._date.add(6, 1);
		(TvT.date = Calendar.getInstance()).set(11, 0);
		TvT.date.set(12, 0);
		TvT.date.set(13, 30);
		TvT.date.add(6, 1);
		if(TvT.date.getTimeInMillis() > System.currentTimeMillis())
			executeTask("events.TvT.TvT", "addDay", new Object[0], (int) (TvT.date.getTimeInMillis() - System.currentTimeMillis()));
		preLoad();
	}

	public static void preStartTask()
	{
		if(TvT._active)
			startOk(new String[] { String.valueOf(TvT._pre_category) });
	}

	static
	{
		TvT._log = LoggerFactory.getLogger(TvT.class);
		removeEffects = new int[] { 442, 443, 1411, 1418, 1427 };
		TvT.count1 = 0;
		TvT.count2 = 0;
		_instanceID = Config.TvT_Instance ? 101 : 0;
		useBC = Config.TvT_ReturnPoint.length < 3;
		TvT._isRegistrationActive = false;
		TvT._status = 0;
		TvT._zone = ZoneManager.getInstance().getZone(Config.TvT_Zone);
		TvT._zoneListener = new ZoneListener();
		BlueTeamLoc = Location.parseLoc(Config.TvT_BlueTeamLoc);
		RedTeamLoc = Location.parseLoc(Config.TvT_RedTeamLoc);
		BlueTeamResLoc = Location.parseLoc(Config.TvT_BlueTeamResLoc);
		RedTeamResLoc = Location.parseLoc(Config.TvT_RedTeamResLoc);
		ClearLoc = Location.parseLoc(Config.TvT_ClearLoc);
		TvT.BS_Active = false;
		TvT.team1loc = new Territory(TerritoryTable.locId++).add(new Polygon().add(149878, 47505).add(150262, 47513).add(150502, 47233).add(150507, 46300).add(150256, 46002).add(149903, 46005).setZmin(-3408).setZmax(-3330));
		TvT.team2loc = new Territory(TerritoryTable.locId++).add(new Polygon().add(149027, 46005).add(148686, 46003).add(148448, 46302).add(148449, 47231).add(148712, 47516).add(149014, 47527).setZmin(-3408).setZmax(-3330));
		TvT._tasks = new ConcurrentHashMap<Integer, Future<?>>();
		TvT._equip = new ConcurrentHashMap<Integer, List<Integer>>();
		TvT._destroy = new ConcurrentHashMap<Integer, List<Integer>>();
		TvT._sbuffs = new ConcurrentHashMap<Integer, List<Abnormal>>();
		TvT._active = false;
	}

	private static class ZoneListener extends ZoneEnterLeaveListener
	{
		@Override
		public void objectEntered(final Zone zone, final GameObject object)
		{
			if(object == null)
				return;
			final Player player = object.getPlayer();
			if(TvT._status > 0 && player != null && !TvT.live_list1.contains(player.getObjectId()) && !TvT.live_list2.contains(player.getObjectId()) && !player.isGM() && player.getReflectionId() == TvT._instanceID)
				ThreadPoolManager.getInstance().schedule(new TeleportTask((Creature) object, TvT.ClearLoc), 3000L);
		}

		@Override
		public void objectLeaved(final Zone zone, final GameObject object)
		{
			if(object == null)
				return;
			final Player player = object.getPlayer();
			if(TvT._status == 2 && player != null && player.getTeam() > 0 && (TvT.live_list1.contains(player.getObjectId()) || TvT.live_list2.contains(player.getObjectId())) && player.getReflectionId() == TvT._instanceID)
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
			target.teleToLocation(loc, TvT._instanceID);
		}
	}

	private static class TeleportRes implements Runnable
	{
		private final int playerObjectId;

		public TeleportRes(final int id)
		{
			playerObjectId = id;
		}

		@Override
		public void run()
		{
			final Player player = GameObjectsStorage.getPlayer(playerObjectId);
			if(player != null && TvT._status == 2 && player.getTeam() > 0)
			{
				if(player.isDead())
				{
					player.restoreExp();
					player.broadcastPacket(new L2GameServerPacket[] { new Revive(player) });
					player.setPendingRevive(true);
				}
				player.teleToLocation(player.getTeam() == 1 ? TvT.BlueTeamResLoc : TvT.RedTeamResLoc, TvT._instanceID);
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

	private static class ActionCheck implements Runnable
	{
		private final int _id;
		private int state;

		public ActionCheck(final int id)
		{
			state = 0;
			_id = id;
		}

		@Override
		public void run()
		{
			if(TvT._status != 2)
				return;
			final Player player = GameObjectsStorage.getPlayer(_id);
			if(player == null)
			{
				TvT._tasks.remove(_id);
				return;
			}
			if(state == 0)
			{
				if(!player.eventAct)
				{
					Functions.show("<br><font color=\"LEVEL\">Внимание! В случае бездействия в течении " + Config.TvT_NonActionDelay + " сек, Вы будете исключены с эвента!</font>", player);
					state = 1;
				}
			}
			else
			{
				if(!player.eventAct)
				{
					TvT.OnEscape(player);
					backPlayer(player);
					Functions.show("<br><font color=\"LEVEL\">Вы исключены с эвента за бездействие!</font>", player);
					return;
				}
				state = 0;
			}
			player.eventAct = false;
			TvT._tasks.put(_id, ThreadPoolManager.getInstance().schedule(this, Config.TvT_NonActionDelay * 1000));
		}
	}
}
