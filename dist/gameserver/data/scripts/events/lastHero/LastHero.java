package events.lastHero;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Bonus;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.database.mysql;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.listener.ZoneEnterLeaveListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.model.entity.olympiad.OlympiadGame;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.Say2;
import l2s.gameserver.network.l2.s2c.SkillList;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.EffectType;
import l2s.gameserver.tables.DoorTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Util;

public class LastHero extends Functions implements ScriptFile
{
	private static Logger _log;
	private static List<Integer> players_list;
	private static List<Integer> live_list;
	private static List<String> _restrict;
	private static final int[] removeEffects;
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
	private static final Location _enter;
	private static final Location ClearLoc;
	private static Calendar _date;
	private static Calendar date;
	private static boolean _active;

	@Override
	public void onLoad()
	{
		LastHero._zone.getListenerEngine().addMethodInvokedListener(LastHero._zoneListener);
		LastHero._active = ServerVariables.getString("LastHero", "off").equalsIgnoreCase("on");
		if(LastHero._active)
			executeTask("events.lastHero.LastHero", "preLoad", new Object[0], 90000L);
		LastHero._date = Calendar.getInstance();
		(LastHero.date = (Calendar) LastHero._date.clone()).set(11, 0);
		LastHero.date.set(12, 0);
		LastHero.date.set(13, 30);
		LastHero.date.add(6, 1);
		if(!Config.LastHero_Allow_Calendar_Day && LastHero.date.getTimeInMillis() > System.currentTimeMillis())
			executeTask("events.lastHero.LastHero", "addDay", new Object[0], (int) (LastHero.date.getTimeInMillis() - System.currentTimeMillis()));
		LastHero._log.info("Loaded Event: Last Hero");
	}

	@Override
	public void onReload()
	{
		LastHero._zone.getListenerEngine().removeMethodInvokedListener(LastHero._zoneListener);
	}

	@Override
	public void onShutdown()
	{
		onReload();
	}

	public void activateEvent()
	{
		final Player player = getSelf();
		if(player == null || !player.getPlayerAccess().IsEventGm)
			return;
		if(!LastHero._active)
		{
			executeTask("events.lastHero.LastHero", "preLoad", new Object[0], 10000L);
			ServerVariables.set("LastHero", "on");
			LastHero._log.info("Event 'Last Hero' activated.");
			player.sendMessage(new CustomMessage("scripts.events.LastHero.AnnounceEventStarted"));
		}
		else
			player.sendMessage(player.isLangRus() ? "'Last Hero' \u044d\u0432\u0435\u043d\u0442 \u0443\u0436\u0435 \u0430\u043a\u0442\u0438\u0432\u0435\u043d." : "Event 'Last Hero' already active.");
		LastHero._active = true;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void deactivateEvent()
	{
		final Player player = getSelf();
		if(player == null || !player.getPlayerAccess().IsEventGm)
			return;
		if(LastHero._active)
		{
			ServerVariables.unset("LastHero");
			LastHero._log.info("Event 'Last Hero' deactivated.");
			player.sendMessage(new CustomMessage("scripts.events.LastHero.AnnounceEventStoped"));
		}
		else
			player.sendMessage("Event 'LastHero' not active.");
		LastHero._active = false;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public static boolean isRunned(final String name)
	{
		return (LastHero._isRegistrationActive || LastHero._status > 0) && LastHero._instanceID == 0 && name.equals(Config.LastHero_Zone);
	}

	public String DialogAppend_31225(final Integer val)
	{
		if(val != 0)
			return "";
		final Player player = getSelf();
		if(player == null)
			return "";
		return HtmCache.getInstance().getHtml("scripts/events/lastHero/31225.html", player);
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
		if(LastHero._instanceID == 0 && (isEventStarted("events.TvT.TvT", Config.LastHero_Zone) || isEventStarted("events.CtF.CtF", Config.LastHero_Zone)))
		{
			LastHero._log.info("Last Hero not started: another event is already running");
			return;
		}
		if(LastHero._isRegistrationActive || LastHero._status > 0)
			return;
		try
		{
			LastHero._category = Integer.valueOf(var[0]);
		}
		catch(Exception e)
		{
			LastHero._log.info("Last Hero not started: can't parse category");
			return;
		}
		if(LastHero._category == -1)
		{
			LastHero._minLevel = 1;
			LastHero._maxLevel = 80;
		}
		else
		{
			LastHero._minLevel = getMinLevelForCategory(LastHero._category);
			LastHero._maxLevel = getMaxLevelForCategory(LastHero._category);
		}
		if(LastHero._endTask != null)
			return;
		LastHero._status = 0;
		LastHero._isRegistrationActive = true;
		LastHero._clearing = false;
		LastHero._time_to_start = Config.LastHero_Time;
		LastHero.players_list = new CopyOnWriteArrayList<Integer>();
		LastHero.live_list = new CopyOnWriteArrayList<Integer>();
		LastHero._restrict = new ArrayList<String>();
		final String[] param = { String.valueOf(LastHero._time_to_start), String.valueOf(LastHero._minLevel), String.valueOf(LastHero._maxLevel) };
		sayToAll("scripts.events.LastHero.AnnouncePreStart", param);
		sayToAll("scripts.events.LastHero.AnnounceReg", null);
		executeTask("events.lastHero.LastHero", "question", new Object[0], 5000L);
		executeTask("events.lastHero.LastHero", "announce", new Object[0], 60000L);
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
				player.sendPacket(new Say2(0, ChatType.CRITICAL_ANNOUNCE, "Last Hero", "Last Hero: " + cm.toString(player)));
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

		for(final Player player : getPlayers(LastHero.players_list))
		{
			if(player != null)
				player.sendPacket(new Say2(0, ChatType.CRITICAL_ANNOUNCE, "Last Hero", "Last Hero: " + cm.toString(player)));
		}
	}

	public static void question()
	{
		for(final Player player : GameObjectsStorage.getPlayers())
			if(player != null && player.getLevel() >= LastHero._minLevel && player.getLevel() <= LastHero._maxLevel && player.getReflectionId() == 0 && !player.isInOlympiadMode() && !player.isCursedWeaponEquipped() && !player.inObserverMode() && !player.inEvent() && !player.getVarBoolean("NoEventAsk", Config.EVENT_NO_ASK))
				player.scriptRequest(new CustomMessage("scripts.events.LastHero.AskPlayer").toString(player), "events.lastHero.LastHero:addPlayer", new Object[0]);
	}

	public static void announce()
	{
		if(LastHero._time_to_start > 1)
		{
			--LastHero._time_to_start;
			final String[] param = { String.valueOf(LastHero._time_to_start), String.valueOf(LastHero._minLevel), String.valueOf(LastHero._maxLevel) };
			sayToAll("scripts.events.LastHero.AnnouncePreStart", param);
			executeTask("events.lastHero.LastHero", "announce", new Object[0], 60000L);
		}
		else
		{
			if(LastHero.players_list.isEmpty() || LastHero.players_list.size() < Config.LastHero_MinPlayers)
			{
				sayToAll("scripts.events.LastHero.AnnounceEventCancelled", null);
				LastHero._isRegistrationActive = false;
				LastHero._status = 0;
				executeTask("events.lastHero.LastHero", "preLoad", new Object[0], 10000L);
				return;
			}
			LastHero._status = 1;
			LastHero._isRegistrationActive = false;
			sayToAll("scripts.events.LastHero.AnnounceEventStarting", null);
			executeTask("events.lastHero.LastHero", "prepare", new Object[0], 5000L);
		}
	}

	public void addPlayer()
	{
		final Player player = getSelf();
		if(player == null || !checkPlayer(player, true))
			return;
		if(LastHero.players_list.size() >= Config.LastHero_MaxPlayers)
		{
			player.sendMessage("\u0414\u043e\u0441\u0442\u0438\u0433\u043d\u0443\u0442 \u043b\u0438\u043c\u0438\u0442 \u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u043e\u0433\u043e \u043a\u043e\u043b-\u0432\u0430 \u0443\u0447\u0430\u0441\u0442\u043d\u0438\u043a\u043e\u0432.");
			return;
		}
		if(Config.LastHero_IP)
		{
			if(LastHero._restrict.contains(player.getIP()))
			{
				player.sendMessage("\u0418\u0433\u0440\u043e\u043a \u0441 \u0434\u0430\u043d\u043d\u044b\u043c IP \u0443\u0436\u0435 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d.");
				return;
			}
			LastHero._restrict.add(player.getIP());
		}
		if(Config.LastHero_HWID)
		{
			if(LastHero._restrict.contains(player.getHWID()))
			{
				player.sendMessage("\u0418\u0433\u0440\u043e\u043a \u0441 \u0434\u0430\u043d\u043d\u044b\u043c \u0436\u0435\u043b\u0435\u0437\u043e\u043c \u0443\u0436\u0435 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d.");
				return;
			}
			LastHero._restrict.add(player.getHWID());
		}
		LastHero.players_list.add(player.getObjectId());
		LastHero.live_list.add(player.getObjectId());
		show(new CustomMessage("scripts.events.LastHero.Registered"), player);
	}

	private static boolean checkPlayer(final Player player, final boolean first)
	{
		if(first && !LastHero._isRegistrationActive)
		{
			show(new CustomMessage("scripts.events.Late"), player);
			return false;
		}
		if(first && LastHero.players_list.contains(player.getObjectId()))
		{
			show(new CustomMessage("scripts.events.LastHero.Cancelled"), player);
			return false;
		}
		if(player.getLevel() < LastHero._minLevel || player.getLevel() > LastHero._maxLevel)
		{
			show(new CustomMessage("scripts.events.LastHero.CancelledLevel"), player);
			return false;
		}
		if(player.isMounted())
		{
			player.sendMessage(player.isLangRus() ? "\u0412\u0435\u0440\u0445\u043e\u043c \u043d\u0430 \u044d\u0432\u0435\u043d\u0442 \u043d\u0435\u043b\u044c\u0437\u044f." : "You can't do it while riding.");
			return false;
		}
		if(player.isInDuel())
		{
			show(new CustomMessage("scripts.events.LastHero.CancelledDuel"), player);
			return false;
		}
		if(player.getTeam() != 0)
		{
			show(new CustomMessage("scripts.events.LastHero.CancelledOtherEvent"), player);
			return false;
		}
		if(player.getOlympiadGameId() > 0 || player.isInOlympiadMode() || first && Olympiad.isRegistered(player))
		{
			show(new CustomMessage("scripts.events.LastHero.CancelledOlympiad"), player);
			return false;
		}
		if(player.isInParty() && player.getParty().isInDimensionalRift())
		{
			show(new CustomMessage("scripts.events.LastHero.CancelledOtherEvent"), player);
			return false;
		}
		if(player.isTeleporting())
		{
			show(new CustomMessage("scripts.events.LastHero.CancelledTeleport"), player);
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
		if(Config.LastHero_NoHero && player.isHero())
		{
			player.sendMessage(player.isLangRus() ? "\u0413\u0435\u0440\u043e\u044f\u043c \u0437\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u043e \u0443\u0447\u0430\u0441\u0442\u0432\u043e\u0432\u0430\u0442\u044c." : "Heroes can't participate.");
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
		if(LastHero._instanceID == 0 && LastHero._zone.getIndex() == 4 && LastHero._zone.getType() == Zone.ZoneType.battle_zone)
		{
			DoorTable.getInstance().getDoor(24190002).closeMe();
			DoorTable.getInstance().getDoor(24190003).closeMe();
		}
		cleanPlayers();
		if(LastHero.players_list.isEmpty() || LastHero.players_list.size() < Config.LastHero_MinPlayers)
		{
			sayToAll("scripts.events.LastHero.AnnounceEventCancelled", null);
			LastHero._isRegistrationActive = false;
			LastHero._status = 0;
			executeTask("events.lastHero.LastHero", "preLoad", new Object[0], 10000L);
			return;
		}
		clearArena();
		executeTask("events.lastHero.LastHero", "paralyzePlayers", new Object[0], 100L);
		executeTask("events.lastHero.LastHero", "teleportPlayersToColiseum", new Object[0], 3000L);
		executeTask("events.lastHero.LastHero", "go", new Object[0], Config.LastHero_Time_Paralyze * 1000);
		sayToParticipants("scripts.events.LastHero.AnnounceFinalCountdown", new String[] { String.valueOf(Config.LastHero_Time_Paralyze) });
	}

	public static void go()
	{
		LastHero._status = 2;
		if(Config.LastHero_Cancel)
			removeBuff();
		else
			upParalyzePlayers();
		if(checkLive())
		{
			clearArena();
			return;
		}
		clearArena();
		buffPlayers();
		sayToParticipants("scripts.events.LastHero.AnnounceFight", null);
		LastHero._endTask = executeTask("events.lastHero.LastHero", "endBattle", new Object[0], Config.LastHero_Time_Battle * 60000);
	}

	private static void buffPlayers()
	{
		if(Config.LH_BUFFS_FIGHTER.length < 2 && Config.LH_BUFFS_MAGE.length < 2)
			return;
		for(final Player player : getPlayers(LastHero.players_list))
			if(player != null)
				if(player.isMageClass())
				{
					int n = 0;
					if(Config.LH_BUFFS_MAGE.length <= 1)
						continue;
					for(int i = 0; i < Config.LH_BUFFS_MAGE.length; i += 2)
						OlympiadGame.giveBuff(player, SkillTable.getInstance().getInfo(Config.LH_BUFFS_MAGE[i], Config.LH_BUFFS_MAGE[i + 1]), n++);
				}
				else
				{
					if(Config.LH_BUFFS_FIGHTER.length <= 1)
						continue;
					int n = 0;
					for(int i = 0; i < Config.LH_BUFFS_FIGHTER.length; i += 2)
						OlympiadGame.giveBuff(player, SkillTable.getInstance().getInfo(Config.LH_BUFFS_FIGHTER[i], Config.LH_BUFFS_FIGHTER[i + 1]), n++);
				}
	}

	private static void removeBuff()
	{
		for(final Player player : getPlayers(LastHero.players_list))
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

	public static void endBattle()
	{
		if(LastHero._clearing)
			return;
		LastHero._clearing = true;
		try
		{
			if(LastHero._endTask != null)
			{
				LastHero._endTask.cancel(false);
				LastHero._endTask = null;
			}
		}
		catch(Exception ex)
		{}
		if(LastHero._status == 0)
			return;
		LastHero._status = 0;
		removeAura();
		if(LastHero._instanceID == 0 && LastHero._zone.getIndex() == 4 && LastHero._zone.getType() == Zone.ZoneType.battle_zone)
		{
			DoorTable.getInstance().getDoor(24190002).openMe();
			DoorTable.getInstance().getDoor(24190003).openMe();
		}
		if(LastHero.live_list.size() == 1)
			for(final Player player : getPlayers(LastHero.live_list))
				if(player != null)
				{
					sayToAll("scripts.events.LastHero.AnnounceWiner", new String[] { player.getName() });
					for(int i = 0; i < Config.LastHero_RewardFinal.length; i += 2)
						addItem(player, Config.LastHero_RewardFinal[i], Math.round(Config.LastHero_RateFinal ? (float) (player.getLevel() * Config.LastHero_RewardFinal[i + 1]) : (float) (1 * Config.LastHero_RewardFinal[i + 1])));
					if(!Config.LastHero_SetHero)
						break;
					if(Config.LastHero_HeroTime > 0)
					{
						Bonus.giveHS(player, Config.LastHero_HeroTime);
						break;
					}
					setHero(player);
					break;
				}
		sayToParticipants("scripts.events.LastHero.AnnounceEnd", new String[] { String.valueOf(Config.EVENTS_TIME_BACK) });
		executeTask("events.lastHero.LastHero", "end", new Object[0], Config.EVENTS_TIME_BACK * 1000);
		LastHero._isRegistrationActive = false;
		LastHero._clearing = false;
	}

	private static void setHero(final Player player)
	{
		if(player.isHero())
			return;
		player.setHero(true);
		Hero.addSkills(player);
		player.updatePledgeClass();
		player.sendPacket(new SkillList(player));
		player.broadcastUserInfo(true);
		sayToAll("scripts.events.LastHero.BecomeHero", new String[] { player.getName() });
	}

	public static void end()
	{
		ressurectPlayers();
		executeTask("events.lastHero.LastHero", "teleportPlayersToSavedCoords", new Object[0], 100L);
		executeTask("events.lastHero.LastHero", "preLoad", new Object[0], 10000L);
	}

	public static void teleportPlayersToColiseum()
	{
		for(final Player player : getPlayers(LastHero.players_list))
		{
			if(player == null)
				continue;
			unRide(player);
			unSummonPet(player, true);
			if(LastHero.useBC)
				player.setVar("LastHero_backCoords", player.isInZone(Zone.ZoneType.no_restart) || player.isInZone(Zone.ZoneType.epic) ? LastHero.ClearLoc.getX() + " " + LastHero.ClearLoc.getY() + " " + LastHero.ClearLoc.getZ() : player.getX() + " " + player.getY() + " " + player.getZ());
			if(player.getParty() != null)
				player.getParty().oustPartyMember(player);
			player.teleToLocation(Location.findAroundPosition(LastHero._enter, 150, 500, player.getGeoIndex()), LastHero._instanceID);
		}
	}

	public static void teleportPlayersToSavedCoords()
	{
		for(final Player player : getPlayers(LastHero.players_list))
			if(player != null)
				backPlayer(player);
		unsetLastCoords();
	}

	public static void paralyzePlayers()
	{
		final Skill revengeSkill = SkillTable.getInstance().getInfo(4515, 1);
		for(final Player player : getPlayers(LastHero.players_list))
		{
			if(player == null)
				continue;
			healPlayer(player);
			player.inLH = true;
			if(Config.LH_RESTRICTED_ITEMS.length > 0)
				for(final ItemInstance item : player.getInventory().getItems())
					if(item != null && item.isEquipped() && ArrayUtils.contains(Config.LH_RESTRICTED_ITEMS, item.getItemId()))
						player.getInventory().unEquipItem(item);
			if(Config.LH_RESTRICTED_SKILLS.length > 0)
				for(final Skill skill : player.getAllSkills())
					if(ArrayUtils.contains(Config.LH_RESTRICTED_SKILLS, skill.getId()))
						player.addUnActiveSkill(skill);
			stopEffects(player, LastHero.removeEffects);
			player.getAbnormalList().stop(EffectType.Paralyze);
			player.getAbnormalList().stop(EffectType.Petrification);
			revengeSkill.getEffects(player, player, false, false);
			if(player.getServitor() != null)
				revengeSkill.getEffects(player, player.getServitor(), false, false);
			if(!player.isSalvation())
				continue;
			for(final Abnormal e : player.getAbnormalList().values())
				if(e.getSkill().isSalvation())
					e.exit();
		}
	}

	public static void upParalyzePlayers()
	{
		for(final Player player : getPlayers(LastHero.players_list))
		{
			if(player == null)
				continue;
			player.getAbnormalList().stop(4515);
			if(player.getServitor() == null)
				continue;
			player.getServitor().getAbnormalList().stop(4515);
		}
	}

	private static void ressurectPlayers()
	{
		for(final Player player : getPlayers(LastHero.players_list))
		{
			healPlayer(player);
			if(Config.LH_RESTRICTED_SKILLS.length > 0)
				for(final Skill skill : player.getAllSkills())
					if(ArrayUtils.contains(Config.LH_RESTRICTED_SKILLS, skill.getId()))
						player.removeUnActiveSkill(skill);
		}
	}

	private static void cleanPlayers()
	{
		for(final Player player : getPlayers(LastHero.players_list))
			if(player != null)
				if(!checkPlayer(player, false))
					removePlayer(player);
				else
					player.setTeam(2, false);
	}

	private static boolean checkLive()
	{
		final List<Integer> new_live_list = new CopyOnWriteArrayList<Integer>();
		for(final Integer objId : LastHero.live_list)
		{
			final Player player = GameObjectsStorage.getPlayer(objId);
			if(player != null)
				new_live_list.add(objId);
		}
		LastHero.live_list = new_live_list;
		for(final Player player2 : getPlayers(LastHero.live_list))
			if(player2 != null)
			{
				final boolean inZone = player2.isInZone(LastHero._zone);
				if(inZone && !player2.isDead() && player2.isConnected() && !player2.isLogoutStarted())
					player2.setTeam(2, false);
				else
				{
					loosePlayer(player2);
					if(inZone)
						continue;
					if(LastHero.useBC && player2.getVar("LastHero_backCoords") == null)
						player2.teleToLocation(LastHero.ClearLoc);
					else
						backPlayer(player2);
				}
			}
		if(LastHero.live_list.size() <= 1)
		{
			endBattle();
			return true;
		}
		return false;
	}

	private static void removeAura()
	{
		for(final Player player : getPlayers(LastHero.live_list))
			if(player != null)
			{
				player.setTeam(0, false);
				player.inLH = false;
			}
	}

	private static void clearArena()
	{
		for(final GameObject obj : LastHero._zone.getObjects())
			if(obj != null)
			{
				final Player player = obj.getPlayer();
				if(player != null && !LastHero.live_list.contains(player.getObjectId()) && !player.isGM() && player.getReflectionId() == LastHero._instanceID)
					player.teleToLocation(LastHero.ClearLoc);
			}
	}

	public static void OnDie(final Creature self, final Creature killer)
	{
		if(LastHero._status > 1 && self != null && self.isPlayer() && self.getTeam() > 0 && LastHero.live_list.contains(self.getObjectId()))
		{
			final Player player = (Player) self;
			loosePlayer(player);
			checkLive();
			if(killer != null && killer.isPlayer())
				for(int i = 0; i < Config.LastHero_Reward.length; i += 2)
					addItem((Playable) killer, Config.LastHero_Reward[i], (long) Math.round(Config.LastHero_Rate ? (float) (player.getLevel() * Config.LastHero_Reward[i + 1]) : (float) (1 * Config.LastHero_Reward[i + 1])));
		}
	}

	public static Location OnEscape(final Player player)
	{
		if(LastHero._status > 1 && player != null && player.getTeam() > 0 && LastHero.live_list.contains(player.getObjectId()))
		{
			removePlayer(player);
			checkLive();
		}
		return null;
	}

	public static void OnPlayerExit(final Player player)
	{
		if(player == null)
			return;
		if(player.getTeam() < 1)
			return;
		if(LastHero._status == 0 && LastHero._isRegistrationActive && LastHero.live_list.contains(player.getObjectId()))
		{
			removePlayer(player);
			return;
		}
		if(LastHero._status == 1 && LastHero.live_list.contains(player.getObjectId()))
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
		if(LastHero.useBC)
			try
			{
				final String var = player.getVar("LastHero_backCoords");
				if(var == null)
					return;
				if(var.equals(""))
				{
					player.unsetVar("LastHero_backCoords");
					return;
				}
				final String[] coords = var.split(" ");
				if(coords.length < 3)
					return;
				player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
				player.unsetVar("LastHero_backCoords");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		else
			player.teleToLocation(Location.findAroundPosition(Config.LastHero_ReturnPoint[0], Config.LastHero_ReturnPoint[1], Config.LastHero_ReturnPoint[2], 0, 150, player.getGeoIndex()));
	}

	private static void unsetLastCoords()
	{
		if(LastHero.useBC)
			mysql.set("DELETE FROM `character_variables` WHERE `name`='LastHero_backCoords'");
	}

	private static void loosePlayer(final Player player)
	{
		if(player != null)
		{
			LastHero.live_list.remove(player.getObjectId());
			player.setTeam(0, false);
			player.inLH = false;
			show(new CustomMessage("scripts.events.LastHero.YouLose"), player);
		}
	}

	private static void removePlayer(final Player player)
	{
		if(player != null)
		{
			LastHero.live_list.remove(player.getObjectId());
			LastHero.players_list.remove(player.getObjectId());
			player.setTeam(0, false);
			player.inLH = false;
		}
	}

	public static void preLoad()
	{
		int day;
		if(Config.LastHero_Allow_Calendar_Day)
			day = 4;
		else
			day = 3;
		for(int i = 0; i < Config.LastHero_Time_Start.length; i += day)
		{
			if(Config.LastHero_Allow_Calendar_Day)
			{
				LastHero._date.set(5, Config.LastHero_Time_Start[i]);
				LastHero._date.set(11, Config.LastHero_Time_Start[i + 1]);
				LastHero._date.set(12, Config.LastHero_Time_Start[i + 2]);
			}
			else
			{
				LastHero._date.set(11, Config.LastHero_Time_Start[i]);
				LastHero._date.set(12, Config.LastHero_Time_Start[i + 1]);
			}
			if(LastHero._date.getTimeInMillis() > System.currentTimeMillis() + 2000L)
			{
				if(Config.LastHero_Allow_Calendar_Day)
					LastHero._pre_category = Config.LastHero_Time_Start[i + 3];
				else
					LastHero._pre_category = Config.LastHero_Time_Start[i + 2];
				executeTask("events.lastHero.LastHero", "preStartTask", new Object[0], (int) (LastHero._date.getTimeInMillis() - System.currentTimeMillis()));
				break;
			}
		}
	}

	public static void addDay()
	{
		LastHero._date.add(6, 1);
		(LastHero.date = Calendar.getInstance()).set(11, 0);
		LastHero.date.set(12, 0);
		LastHero.date.set(13, 30);
		LastHero.date.add(6, 1);
		if(LastHero.date.getTimeInMillis() > System.currentTimeMillis())
			executeTask("events.lastHero.LastHero", "addDay", new Object[0], (int) (LastHero.date.getTimeInMillis() - System.currentTimeMillis()));
		preLoad();
	}

	public static void preStartTask()
	{
		if(LastHero._active)
			startOk(new String[] { String.valueOf(LastHero._pre_category) });
	}

	static
	{
		LastHero._log = LoggerFactory.getLogger(LastHero.class);
		LastHero.players_list = new CopyOnWriteArrayList<Integer>();
		LastHero.live_list = new CopyOnWriteArrayList<Integer>();
		LastHero._restrict = new ArrayList<String>();
		removeEffects = new int[] { 442, 443, 1411, 1418, 1427 };
		_instanceID = Config.LastHero_Instance ? 102 : 0;
		useBC = Config.LastHero_ReturnPoint.length < 3;
		LastHero._isRegistrationActive = false;
		LastHero._status = 0;
		LastHero._zone = ZoneManager.getInstance().getZone(Config.LastHero_Zone);
		LastHero._zoneListener = new ZoneListener();
		_enter = Location.parseLoc(Config.LastHero_Loc);
		ClearLoc = Location.parseLoc(Config.LastHero_ClearLoc);
		LastHero._active = false;
	}

	private static class ZoneListener extends ZoneEnterLeaveListener
	{
		@Override
		public void objectEntered(final Zone zone, final GameObject object)
		{
			if(object == null)
				return;
			final Player player = object.getPlayer();
			if(LastHero._status > 0 && player != null && !LastHero.live_list.contains(player.getObjectId()) && !player.isGM() && player.getReflectionId() == LastHero._instanceID)
				ThreadPoolManager.getInstance().schedule(new TeleportTask((Creature) object, LastHero.ClearLoc), 3000L);
		}

		@Override
		public void objectLeaved(final Zone zone, final GameObject object)
		{
			if(object == null)
				return;
			final Player player = object.getPlayer();
			if(LastHero._status > 1 && player != null && player.getTeam() > 0 && LastHero.live_list.contains(player.getObjectId()) && player.getReflectionId() == LastHero._instanceID)
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
			target.teleToLocation(loc, LastHero._instanceID);
		}
	}
}
