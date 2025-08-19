package events.tournament;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;

import l2s.gameserver.Announcements;
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
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Util;

public class Tournament_battle extends Functions implements ScriptFile
{
	public static NpcInstance _manager;
	private static Zone _zone;
	ZoneListener _zoneListener;
	private static boolean _hooks;
	private static boolean _die_hooks;
	private static boolean _intention_hooks;
	private static int _current_cycle;
	private static int _category;
	private static int _time_to_start;
	private static ArrayList<Location> team1_points;
	private static ArrayList<Location> team2_points;
	public static Team team1;
	public static Team team2;
	public static ArrayList<Player> team1_live_list;
	public static ArrayList<Player> team2_live_list;
	private static ScheduledFuture<?> _startBattleTask;
	private static ScheduledFuture<?> _endBattleTask;
	private static ArrayList<Spawn> _spawns;
	private static boolean _active;

	public Tournament_battle()
	{
		_zoneListener = new ZoneListener();
	}

	public String DialogAppend_32130(final Integer val)
	{
		final Player player = getSelf();
		if(val != 0)
			return "";
		if(player.isGM())
			return HtmCache.getInstance().getHtml("scripts/events/tournament/32130.html", player) + HtmCache.getInstance().getHtml("scripts/events/tournament/32130-gm.html", player);
		return HtmCache.getInstance().getHtml("scripts/events/tournament/32130.html", player);
	}

	public void bypass_begin(final String[] var)
	{
		if(var.length != 1)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435", getSelf());
			return;
		}
		final NpcInstance npc = getNpc();
		if(npc == null)
		{
			show("Hacker? :) " + npc, getSelf());
			return;
		}
		try
		{
			Tournament_battle._category = Integer.valueOf(var[0]);
		}
		catch(Exception e)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435", getSelf());
			return;
		}
		Tournament_battle._manager = npc;
		Tournament_battle._time_to_start = 10;
		Tournament_battle._current_cycle = 1;
		Tournament_battle.team1_live_list.clear();
		Tournament_battle.team2_live_list.clear();
		Tournament_register.endRegistration();
		mysql.set("UPDATE tournament_teams SET status = 1");
		announce("\u0420\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044f \u043d\u0430 \u0442\u0443\u0440\u043d\u0438\u0440 \u043e\u043a\u043e\u043d\u0447\u0435\u043d\u0430.");
		if(Tournament_data.createTournamentTable(Tournament_battle._category))
			return;
		announce("\u0422\u0443\u0440\u043d\u0438\u0440 \u0434\u043b\u044f \u043a\u0430\u0442\u0435\u0433\u043e\u0440\u0438\u0438 " + Tournament_battle._category + " \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 10 \u043c\u0438\u043d\u0443\u0442!");
		addAnnounce("\u0422\u0443\u0440\u043d\u0438\u0440 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 9 \u043c\u0438\u043d\u0443\u0442.", 60000);
		addAnnounce("\u0422\u0443\u0440\u043d\u0438\u0440 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 8 \u043c\u0438\u043d\u0443\u0442.", 120000);
		addAnnounce("\u0422\u0443\u0440\u043d\u0438\u0440 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 7 \u043c\u0438\u043d\u0443\u0442.", 180000);
		addAnnounce("\u0422\u0443\u0440\u043d\u0438\u0440 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 6 \u043c\u0438\u043d\u0443\u0442.", 240000);
		addAnnounce("\u0422\u0443\u0440\u043d\u0438\u0440 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 5 \u043c\u0438\u043d\u0443\u0442.", 300000);
		addAnnounce("\u0422\u0443\u0440\u043d\u0438\u0440 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 4 \u043c\u0438\u043d\u0443\u0442\u044b.", 360000);
		addAnnounce("\u0422\u0443\u0440\u043d\u0438\u0440 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 3 \u043c\u0438\u043d\u0443\u0442\u044b.", 420000);
		addAnnounce("\u0422\u0443\u0440\u043d\u0438\u0440 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 2 \u043c\u0438\u043d\u0443\u0442\u044b.", 480000);
		addAnnounce("\u0422\u0443\u0440\u043d\u0438\u0440 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 1 \u043c\u0438\u043d\u0443\u0442\u0443.", 540000);
		addAnnounce("\u0422\u0443\u0440\u043d\u0438\u0440 \u043f\u0440\u043e\u0432\u043e\u0434\u0438\u0442\u0441\u044f \u0432 Coliseum.", 121000);
		addAnnounce("\u0412\u0441\u0435 \u0436\u0435\u043b\u0430\u044e\u0449\u0438\u0435 \u043c\u043e\u0433\u0443\u0442 \u043d\u0430\u0431\u043b\u044e\u0434\u0430\u0442\u044c \u0437\u0430 \u0431\u043e\u044f\u043c\u0438 \u0447\u0435\u0440\u0435\u0437 \u043e\u0431\u0437\u043e\u0440\u043d\u044b\u0435 \u043a\u0440\u0438\u0441\u0442\u0430\u043b\u043b\u044b, \u043b\u0438\u0431\u043e \u043a\u0443\u043f\u0438\u0432 \u043c\u0435\u0441\u0442\u0430 \u043d\u0430 \u0442\u0440\u0438\u0431\u0443\u043d\u0435. \u041c\u0435\u0441\u0442\u0430 \u043f\u0440\u043e\u0434\u0430\u044e\u0442\u0441\u044f \u0443 Arena Director.", 181000);
		addAnnounce("\u041a\u043e\u043c\u0430\u043d\u0434\u044b \u0431\u0443\u0434\u0443\u0442 \u0432\u044b\u0437\u044b\u0432\u0430\u0442\u044c\u0441\u044f \u043f\u043e-\u043e\u0447\u0435\u0440\u0435\u0434\u0438, \u0432 \u043f\u043e\u0440\u044f\u0434\u043a\u0435, \u0443\u043a\u0430\u0437\u0430\u043d\u043d\u043e\u043c \u0432 \u0442\u0443\u0440\u043d\u0438\u0440\u043d\u043e\u0439 \u0442\u0430\u0431\u043b\u0438\u0446\u0435. \u041f\u0440\u043e\u0441\u044c\u0431\u0430 \u0437\u0430\u0440\u0430\u043d\u0435\u0435 \u043f\u0440\u0438\u0433\u043e\u0442\u043e\u0432\u0438\u0442\u044c\u0441\u044f.", 241000);
		addAnnounce("\u041d\u0430 \u043a\u0430\u0436\u0434\u044b\u0439 \u0431\u043e\u0439 \u043e\u0442\u0432\u043e\u0434\u0438\u0442\u0441\u044f 2 \u043c\u0438\u043d\u0443\u0442\u044b. \u0415\u0441\u043b\u0438 \u0437\u0430 2 \u043c\u0438\u043d\u0443\u0442\u044b \u043d\u0435 \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0438\u043b\u0441\u044f \u043f\u043e\u0431\u0435\u0434\u0438\u0442\u0435\u043b\u044c, \u043f\u0440\u043e\u0438\u0433\u0440\u0430\u0432\u0448\u0435\u0439 \u0441\u0447\u0438\u0442\u0430\u0435\u0442\u0441\u044f \u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u0441 \u043c\u0435\u043d\u044c\u0448\u0438\u043c \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e\u043c \u0436\u0438\u0437\u043d\u0435\u0439.", 301000);
		addAnnounce("\u0415\u0441\u043b\u0438 \u0432\u0435\u0441\u044c \u0441\u043e\u0441\u0442\u0430\u0432 \u043a\u043e\u043c\u0430\u043d\u0434\u044b \u043e\u0442\u0441\u0443\u0442\u0441\u0442\u0432\u0443\u0435\u0442 \u0432 \u0438\u0433\u0440\u0435, \u043a\u043e\u043c\u0430\u043d\u0434\u0435 \u0437\u0430\u0441\u0447\u0438\u0442\u044b\u0432\u0430\u0435\u0442\u0441\u044f \u043f\u043e\u0440\u0430\u0436\u0435\u043d\u0438\u0435.", 361000);
		addAnnounce("\u0420\u0435\u043a\u043e\u043c\u0435\u043d\u0434\u0443\u0435\u0442\u0441\u044f \u0432\u0441\u0435\u043c \u0443\u0447\u0430\u0441\u0442\u043d\u0438\u043a\u0430\u043c \u0437\u0430\u0440\u0430\u043d\u0435\u0435 \u043f\u0440\u0438\u0439\u0442\u0438 \u0432 Coliseum, \u0447\u0442\u043e\u0431\u044b \u0432 \u043d\u0430\u0447\u0430\u043b\u0435 \u0431\u043e\u044f \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044f \u043d\u0435 \u0437\u0430\u043d\u044f\u043b\u0430 \u043c\u043d\u043e\u0433\u043e \u0432\u0440\u0435\u043c\u0435\u043d\u0438.", 421000);
		addTask("nextBattle", 600000);
	}

	public static void addAnnounce(final String text, final Integer time)
	{
		final Object[] args = { text };
		executeTask(Tournament_battle._manager, "events.tournament.Tournament_battle", "announce", args, time);
	}

	public static ScheduledFuture<?> addTask(final String task, final Integer time)
	{
		return executeTask(Tournament_battle._manager, "events.tournament.Tournament_battle", task, new Object[0], time);
	}

	public static void announce(final String text)
	{
		npcShout(Tournament_battle._manager, text, 0);
	}

	public static void nextBattle()
	{
		resurrectTeams();
		healTeams();
		teleportPlayersToSavedCoords();
		if(!fillNextTeams())
			return;
		Tournament_battle.team1_live_list.clear();
		Tournament_battle.team2_live_list.clear();
		Tournament_battle.team1_live_list.addAll(Tournament_battle.team1.getOnlineMembers());
		Tournament_battle.team2_live_list.addAll(Tournament_battle.team2.getOnlineMembers());
		Tournament_battle._hooks = true;
		announce("\u0411\u043e\u0439 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 2 \u043c\u0438\u043d\u0443\u0442\u044b.");
		clearArena();
		saveBackCoords();
		teleportTeams();
		root();
		deBuffTeams();
		resurrectTeams();
		healTeams();
		Tournament_battle._startBattleTask = addTask("startBattle", 120000);
		Tournament_battle._endBattleTask = addTask("endBattle", 240000);
	}

	public static void startBattle()
	{
		endRoot();
		announce("FIGHT!!!");
	}

	public static void endBattle()
	{
		Tournament_battle._hooks = false;
		announce("\u0412\u0440\u0435\u043c\u044f \u0438\u0441\u0442\u0435\u043a\u043b\u043e.");
		calculateWinner();
		announce("\u0421\u043b\u0435\u0434\u0443\u044e\u0449\u0438\u0439 \u0431\u043e\u0439 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 1 \u043c\u0438\u043d\u0443\u0442\u0443.");
		executeTask(Tournament_battle._manager, "events.tournament.Tournament_battle", "nextBattle", new Object[0], 60000L);
	}

	public static boolean fillNextTeams()
	{
		announce(Tournament_battle._current_cycle + " \u044d\u0442\u0430\u043f.");
		while(!Tournament_data.fillNextTeams(Tournament_battle._category))
		{
			if(Tournament_data.createTournamentTable(Tournament_battle._category))
				return false;
			++Tournament_battle._current_cycle;
		}
		return true;
	}

	public static void endTournament()
	{
		Tournament_register.endTournament();
	}

	public static void calculateWinner()
	{
		int hp1 = 0;
		int hp2 = 0;
		for(final Player player : Tournament_battle.team1_live_list)
			if(player != null && !player.isDead())
				hp1 += (int) player.getCurrentHp();
		for(final Player player : Tournament_battle.team2_live_list)
			if(player != null && !player.isDead())
				hp2 += (int) player.getCurrentHp();
		if(hp2 > hp1)
		{
			Tournament_data.teamWin(Tournament_battle.team2.getId());
			Tournament_data.teamLost(Tournament_battle.team1.getId());
			Tournament_data.removeRecordFromTournamentTable(Tournament_battle.team1.getId());
		}
		else
		{
			Tournament_data.teamWin(Tournament_battle.team1.getId());
			Tournament_data.teamLost(Tournament_battle.team2.getId());
			Tournament_data.removeRecordFromTournamentTable(Tournament_battle.team1.getId());
		}
	}

	public static void clearArena()
	{
		for(final Player player : GameObjectsStorage.getPlayers())
			if(player != null && Tournament_battle._zone.getLoc().isInside(player.getX(), player.getY()))
				teleportToColiseumSpawn(player);
	}

	public static void teleportToColiseumSpawn(final Player player)
	{
		player.teleToLocation(Tournament_battle._zone.getSpawn());
	}

	public static void teleportTeams()
	{
		int i = 0;
		for(final Player player : Tournament_battle.team1_live_list)
			if(player != null)
			{
				player.teleToLocation(Tournament_battle.team1_points.get(i));
				player.setTeam(1, true);
				++i;
			}
		i = 0;
		for(final Player player : Tournament_battle.team2_live_list)
			if(player != null)
			{
				player.teleToLocation(Tournament_battle.team2_points.get(i));
				player.setTeam(2, true);
				++i;
			}
	}

	public static void resurrectTeams()
	{
		for(final Player player : Tournament_battle.team1_live_list)
			if(player != null)
			{
				player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp(), true);
				player.setCurrentCp(player.getMaxCp());
				player.restoreExp();
				player.doRevive(true);
			}
		for(final Player player : Tournament_battle.team2_live_list)
			if(player != null)
			{
				player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp(), true);
				player.setCurrentCp(player.getMaxCp());
				player.restoreExp();
				player.doRevive(true);
			}
	}

	public static void healTeams()
	{
		for(final Player player : Tournament_battle.team1_live_list)
			if(player != null)
			{
				player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp(), false);
				player.setCurrentCp(player.getMaxCp());
				if(player.getServitor() == null)
					continue;
				player.getServitor().setCurrentHpMp(player.getServitor().getMaxHp(), player.getServitor().getMaxMp(), false);
				player.getServitor().setCurrentCp(player.getServitor().getMaxCp());
			}
		for(final Player player : Tournament_battle.team2_live_list)
			if(player != null)
			{
				player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp(), false);
				player.setCurrentCp(player.getMaxCp());
				if(player.getServitor() == null)
					continue;
				player.getServitor().setCurrentHpMp(player.getServitor().getMaxHp(), player.getServitor().getMaxMp(), false);
				player.getServitor().setCurrentCp(player.getServitor().getMaxCp());
			}
	}

	public static void deBuffTeams()
	{
		for(final Player player : Tournament_battle.team1_live_list)
			if(player != null)
			{
				player.getAbnormalList().stopAll();
				if(player.getServitor() == null)
					continue;
				player.getServitor().getAbnormalList().stopAll();
			}
		for(final Player player : Tournament_battle.team2_live_list)
			if(player != null)
			{
				player.getAbnormalList().stopAll();
				if(player.getServitor() == null)
					continue;
				player.getServitor().getAbnormalList().stopAll();
			}
	}

	public static void root()
	{
		for(final Player player : Tournament_battle.team1_live_list)
			if(player != null)
			{
				player.setImmobilized(true);
				if(player.getServitor() == null)
					continue;
				player.getServitor().setImmobilized(true);
			}
		for(final Player player : Tournament_battle.team2_live_list)
			if(player != null)
			{
				player.setImmobilized(true);
				if(player.getServitor() == null)
					continue;
				player.getServitor().setImmobilized(true);
			}
	}

	public static void endRoot()
	{
		for(final Player player : Tournament_battle.team1_live_list)
			if(player != null)
			{
				player.setImmobilized(false);
				if(player.getServitor() == null)
					continue;
				player.getServitor().setImmobilized(false);
			}
		for(final Player player : Tournament_battle.team2_live_list)
			if(player != null)
			{
				player.setImmobilized(false);
				if(player.getServitor() == null)
					continue;
				player.getServitor().setImmobilized(false);
			}
	}

	public static void saveBackCoords()
	{
		for(final Player player : Tournament_battle.team1_live_list)
			if(player != null)
				player.setVar("Tournament_backCoords", player.getX() + " " + player.getY() + " " + player.getZ());
		for(final Player player : Tournament_battle.team1_live_list)
			if(player != null)
				player.setVar("Tournament_backCoords", player.getX() + " " + player.getY() + " " + player.getZ());
	}

	public static void teleportPlayersToSavedCoords()
	{
		for(final Player player : Tournament_battle.team1_live_list)
			if(player != null)
			{
				final String var = player.getVar("Tournament_backCoords");
				if(var == null)
					continue;
				if(var.equals(""))
					continue;
				final String[] coords = var.split(" ");
				if(coords.length != 3)
					continue;
				final Location pos = new Location(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
				player.teleToLocation(pos);
				player.unsetVar("Tournament_backCoords");
				player.setTeam(0, false);
			}
		for(final Player player : Tournament_battle.team2_live_list)
			if(player != null)
			{
				final String var = player.getVar("Tournament_backCoords");
				if(var == null)
					continue;
				if(var.equals(""))
					continue;
				final String[] coords = var.split(" ");
				if(coords.length != 3)
					continue;
				player.teleToLocation(new Location(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])));
				player.unsetVar("Tournament_backCoords");
				player.setTeam(0, false);
			}
	}

	public static void giveItemsToWinner(final Team team)
	{}

	public static Location onEscape(final Player player)
	{
		if(Tournament_battle._hooks && player != null)
			OnPlayerExit(player);
		return null;
	}

	public static void OnDie(final GameObject self, final Creature killer)
	{
		if(Tournament_battle._hooks && self != null && self.isPlayer())
			OnPlayerExit((Player) self);
	}

	public static void OnPlayerExit(final Player player)
	{
		if(Tournament_battle._hooks && player != null)
			if(Tournament_battle.team1_live_list.contains(player))
			{
				Tournament_battle.team1_live_list.remove(player);
				player.setTeam(0, false);
				if(Tournament_battle.team1_live_list.size() == 0)
				{
					Tournament_data.teamWin(Tournament_battle.team2.getId());
					Tournament_data.teamLost(Tournament_battle.team1.getId());
					Tournament_data.removeRecordFromTournamentTable(Tournament_battle.team1.getId());
					if(Tournament_battle._endBattleTask != null)
						Tournament_battle._endBattleTask.cancel(true);
					Tournament_battle._endBattleTask = null;
					Tournament_battle._hooks = false;
					announce("\u0421\u043b\u0435\u0434\u0443\u044e\u0449\u0438\u0439 \u0431\u043e\u0439 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 1 \u043c\u0438\u043d\u0443\u0442\u0443.");
					executeTask(Tournament_battle._manager, "events.tournament.Tournament_battle", "nextBattle", new Object[0], 60000L);
				}
			}
			else if(Tournament_battle.team2_live_list.contains(player))
			{
				Tournament_battle.team2_live_list.remove(player);
				player.setTeam(0, false);
				if(Tournament_battle.team2_live_list.size() == 0)
				{
					Tournament_data.teamWin(Tournament_battle.team1.getId());
					Tournament_data.teamLost(Tournament_battle.team2.getId());
					Tournament_data.removeRecordFromTournamentTable(Tournament_battle.team1.getId());
					if(Tournament_battle._endBattleTask != null)
						Tournament_battle._endBattleTask.cancel(true);
					Tournament_battle._endBattleTask = null;
					Tournament_battle._hooks = false;
					announce("\u0421\u043b\u0435\u0434\u0443\u044e\u0449\u0438\u0439 \u0431\u043e\u0439 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 1 \u043c\u0438\u043d\u0443\u0442\u0443.");
					executeTask(Tournament_battle._manager, "events.tournament.Tournament_battle", "nextBattle", new Object[0], 60000L);
				}
			}
	}

	private static boolean isActive()
	{
		return ServerVariables.getString("Tournament", "off").equalsIgnoreCase("on");
	}

	public void startEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(!isActive())
		{
			ServerVariables.set("Tournament", "on");
			spawnEventManagers();
			ScriptFile._log.info("Event 'Tournament' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.Tournament.AnnounceEventStarted", (String[]) null);
		}
		else
			player.sendMessage("Event 'Tournament' already started.");
		Tournament_battle._active = true;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void stopEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(isActive())
		{
			ServerVariables.unset("Tournament");
			unSpawnEventManagers();
			ScriptFile._log.info("Event 'Tournament' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.Tournament.AnnounceEventStoped", (String[]) null);
		}
		else
			player.sendMessage("Event 'Tournament' not started.");
		Tournament_battle._active = false;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS = { { 82545, 148600, -3505, -3395 } };
		final NpcTemplate template = NpcTable.getTemplate(32130);
		for(final int[] element : EVENT_MANAGERS)
			try
			{
				final Spawn sp = new Spawn(template);
				sp.setLocx(element[0]);
				sp.setLocy(element[1]);
				sp.setLocz(element[2]);
				sp.setAmount(1);
				sp.setHeading(element[3]);
				sp.setRespawnDelay(0);
				sp.init();
				sp.getLastSpawn().setAI(new Tournament_ai(sp.getLastSpawn()));
				Tournament_battle._spawns.add(sp);
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
	}

	private void unSpawnEventManagers()
	{
		for(final Spawn sp : Tournament_battle._spawns)
		{
			sp.stopRespawn();
			sp.getLastSpawn().deleteMe();
		}
		Tournament_battle._spawns.clear();
	}

	@Override
	public void onLoad()
	{
		Tournament_battle.team1_points.add(new Location(148780, 46719, -3448));
		Tournament_battle.team1_points.add(new Location(148789, 46568, -3448));
		Tournament_battle.team1_points.add(new Location(148785, 46907, -3448));
		Tournament_battle.team2_points.add(new Location(150117, 46725, -3448));
		Tournament_battle.team2_points.add(new Location(150129, 46901, -3448));
		Tournament_battle.team2_points.add(new Location(150128, 46546, -3448));
		Tournament_battle._zone.getListenerEngine().addMethodInvokedListener(_zoneListener);
		if(isActive())
		{
			Tournament_battle._active = true;
			spawnEventManagers();
			ScriptFile._log.info("Loaded Event: Tournament [state: activated]");
		}
		else
			ScriptFile._log.info("Loaded Event: Tournament [state: deactivated]");
	}

	@Override
	public void onReload()
	{
		Tournament_battle._zone.getListenerEngine().removeMethodInvokedListener(_zoneListener);
		unSpawnEventManagers();
	}

	@Override
	public void onShutdown()
	{
		onReload();
	}

	static
	{
		Tournament_battle._zone = ZoneManager.getInstance().getZoneByIndex(Zone.ZoneType.battle_zone, 4, true);
		Tournament_battle._hooks = false;
		Tournament_battle._die_hooks = false;
		Tournament_battle._intention_hooks = false;
		Tournament_battle.team1_points = new ArrayList<Location>();
		Tournament_battle.team2_points = new ArrayList<Location>();
		Tournament_battle.team1_live_list = new ArrayList<Player>();
		Tournament_battle.team2_live_list = new ArrayList<Player>();
		Tournament_battle._spawns = new ArrayList<Spawn>();
		Tournament_battle._active = false;
	}

	private class ZoneListener extends ZoneEnterLeaveListener
	{
		@Override
		public void objectEntered(final Zone zone, final GameObject object)
		{
			if(Tournament_battle._hooks && object != null && object.getPlayer() != null && !Tournament_battle.team1_live_list.contains(object.getPlayer()) && !Tournament_battle.team2_live_list.contains(object.getPlayer()))
				((Creature) object).teleToLocation(147451, 46728, -3410);
		}

		@Override
		public void objectLeaved(final Zone zone, final GameObject object)
		{
			final Player player = object.getPlayer();
			if(Tournament_battle._hooks && player != null && (Tournament_battle.team1_live_list.contains(player) || Tournament_battle.team2_live_list.contains(player)))
			{
				final Playable playable = (Playable) object;
				final double angle = Util.convertHeadingToDegree(playable.getHeading());
				final double radian = Math.toRadians(angle - 90.0);
				playable.teleToLocation((int) (playable.getX() + 50.0 * Math.sin(radian)), (int) (playable.getY() - 50.0 * Math.cos(radian)), playable.getZ());
			}
		}
	}
}
