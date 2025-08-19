package events.arena;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.listener.ZoneEnterLeaveListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.s2c.Say2;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Util;

public abstract class ArenaTemplate extends Functions
{
	protected int _managerId;
	protected String _className;
	protected int _creatorId;
	protected NpcInstance _manager;
	protected int _status;
	protected int _battleType;
	protected int _team1exp;
	protected int _team2exp;
	protected int _price;
	protected int _team1count;
	protected int _team2count;
	protected int _team1min;
	protected int _team1max;
	protected int _team2min;
	protected int _team2max;
	protected int _timeToStart;
	protected boolean _timeOutTask;
	protected List<Location> _team1points;
	protected List<Location> _team2points;
	protected List<Integer> _team1list;
	protected List<Integer> _team2list;
	protected List<Integer> _team1live;
	protected List<Integer> _team2live;
	protected HashMap<Integer, Integer> _expToReturn;
	protected HashMap<Integer, Integer> _classToReturn;
	protected Zone _zone;
	protected ZoneListener _zoneListener;

	public ArenaTemplate()
	{
		_status = 0;
		_battleType = 1;
		_team1exp = 0;
		_team2exp = 0;
		_price = 10000;
		_team1count = 1;
		_team2count = 1;
		_team1min = 1;
		_team1max = 85;
		_team2min = 1;
		_team2max = 85;
		_timeToStart = 10;
		_team1list = new CopyOnWriteArrayList<Integer>();
		_team2list = new CopyOnWriteArrayList<Integer>();
		_team1live = new CopyOnWriteArrayList<Integer>();
		_team2live = new CopyOnWriteArrayList<Integer>();
		_expToReturn = new HashMap<Integer, Integer>();
		_classToReturn = new HashMap<Integer, Integer>();
	}

	protected abstract void onLoad();

	protected abstract void onReload();

	public void template_stop()
	{
		say("\u0411\u043e\u0439 \u043f\u0440\u0435\u0440\u0432\u0430\u043d \u043f\u043e \u0442\u0435\u0445\u043d\u0438\u0447\u0435\u0441\u043a\u0438\u043c \u043f\u0440\u0438\u0447\u0438\u043d\u0430\u043c, \u0441\u0442\u0430\u0432\u043a\u0438 \u0432\u043e\u0437\u0432\u0440\u0430\u0449\u0435\u043d\u044b");
		if(_battleType == 1)
			returnAdenaToTeams();
		else if(_battleType == 2)
			returnExpToTeams();
		unParalyzeTeams();
		clearTeams();
		_status = 0;
		_timeOutTask = false;
	}

	public void template_create1(final Player player)
	{
		if(_status > 0)
			show("\u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0431\u043e\u044f", player);
		else
			show("scripts/events/arena/" + _managerId + "-1.html", player);
	}

	public void template_create2(final Player player)
	{
		if(_status > 0)
			show("\u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0431\u043e\u044f", player);
		else
			show("scripts/events/arena/" + _managerId + "-2.html", player);
	}

	public void template_register(final Player player)
	{
		if(_status > 1)
			show("\u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0431\u043e\u044f", player);
		else
			show("scripts/events/arena/" + _managerId + "-3.html", player);
	}

	public void template_check1(final Player player, final NpcInstance manager, final String[] var)
	{
		if(player.isDead())
			return;
		if(var.length != 8)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435", player);
			return;
		}
		if(_status > 0)
		{
			show("\u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0431\u043e\u044f", player);
			return;
		}
		if(manager == null || !manager.isNpc())
		{
			show("Hacker? :) " + manager, player);
			return;
		}
		_manager = manager;
		try
		{
			_price = Integer.valueOf(var[0]);
			_team1count = Integer.valueOf(var[1]);
			_team2count = Integer.valueOf(var[2]);
			_team1min = Integer.valueOf(var[3]);
			_team1max = Integer.valueOf(var[4]);
			_team2min = Integer.valueOf(var[5]);
			_team2max = Integer.valueOf(var[6]);
			_timeToStart = Integer.valueOf(var[7]);
		}
		catch(Exception e)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435", player);
			return;
		}
		if(_price < 10000 || _price > 100000000)
		{
			show("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u0430\u044f \u0441\u0442\u0430\u0432\u043a\u0430", player);
			return;
		}
		if(_team1count < 1 || _team1count > 5 || _team2count < 1 || _team2count > 5)
		{
			show("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u0440\u0430\u0437\u043c\u0435\u0440 \u043a\u043e\u043c\u0430\u043d\u0434\u044b", player);
			return;
		}
		if(_team1min < 1 || _team1min > 85 || _team2min < 1 || _team2min > 85 || _team1max < 1 || _team1max > 85 || _team2max < 1 || _team2max > 85 || _team1min > _team1max || _team2min > _team2max)
		{
			show("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u0443\u0440\u043e\u0432\u0435\u043d\u044c", player);
			return;
		}
		if(player.getLevel() < _team1min || player.getLevel() > _team1max)
		{
			show("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u0443\u0440\u043e\u0432\u0435\u043d\u044c", player);
			return;
		}
		if(_timeToStart < 1 || _timeToStart > 10)
		{
			show("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e\u0435 \u0432\u0440\u0435\u043c\u044f", player);
			return;
		}
		if(player.getAdena() < _price)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		_battleType = 1;
		_creatorId = player.getObjectId();
		player.reduceAdena(_price, true);
		_status = 1;
		_team1list.clear();
		_team2list.clear();
		_team1live.clear();
		_team2live.clear();
		_team1list.add(player.getObjectId());
		say(player.getName() + " \u0441\u043e\u0437\u0434\u0430\u043b \u0431\u043e\u0439 " + _team1count + "\u0445" + _team2count + ", " + _team1min + "-" + _team1max + "lv vs " + _team2min + "-" + _team2max + "lv, \u0441\u0442\u0430\u0432\u043a\u0430 " + _price + "\u0430, \u043d\u0430\u0447\u0430\u043b\u043e \u0447\u0435\u0440\u0435\u0437 " + _timeToStart + " \u043c\u0438\u043d");
		executeTask("events.arena." + _className, "announce", new Object[0], 60000L);
	}

	public void template_check2(final Player player, final NpcInstance manager, final String[] var)
	{
		if(!Config.ALT_ARENA_EXP)
		{
			show("\u042d\u0442\u0430 \u043e\u043f\u0446\u0438\u044f \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u0430", player);
			return;
		}
		if(player.isDead())
			return;
		if(var.length != 7)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435", player);
			return;
		}
		if(_status > 0)
		{
			show("\u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0431\u043e\u044f", player);
			return;
		}
		if(manager == null || !manager.isNpc())
		{
			show("Hacker? :) " + manager, player);
			return;
		}
		_manager = manager;
		try
		{
			_team1count = Integer.valueOf(var[0]);
			_team2count = Integer.valueOf(var[1]);
			_team1min = Integer.valueOf(var[2]);
			_team1max = Integer.valueOf(var[3]);
			_team2min = Integer.valueOf(var[4]);
			_team2max = Integer.valueOf(var[5]);
			_timeToStart = Integer.valueOf(var[6]);
		}
		catch(Exception e)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435", player);
			return;
		}
		if(_team1count < 1 || _team1count > 5 || _team2count < 1 || _team2count > 5)
		{
			show("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u0440\u0430\u0437\u043c\u0435\u0440 \u043a\u043e\u043c\u0430\u043d\u0434\u044b", player);
			return;
		}
		if(_team1min < 1 || _team1min > 82 || _team2min < 1 || _team2min > 82 || _team1max < 1 || _team1max > 82 || _team2max < 1 || _team2max > 82 || _team1min > _team1max || _team2min > _team2max)
		{
			show("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u0443\u0440\u043e\u0432\u0435\u043d\u044c", player);
			return;
		}
		if(player.getLevel() - _team1min > 10 || _team1max - player.getLevel() > 10 || player.getLevel() - _team2min > 10 || _team2max - player.getLevel() > 10)
		{
			show("\u0420\u0430\u0437\u043d\u0438\u0446\u0430 \u0432 \u0443\u0440\u043e\u0432\u043d\u044f\u0445 \u043d\u0435 \u043c\u043e\u0436\u0435\u0442 \u0431\u044b\u0442\u044c \u0431\u043e\u043b\u0435\u0435 10", player);
			return;
		}
		if(player.getLevel() < _team1min || player.getLevel() > _team1max)
		{
			show("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u0443\u0440\u043e\u0432\u0435\u043d\u044c", player);
			return;
		}
		if(_timeToStart < 1 || _timeToStart > 10)
		{
			show("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e\u0435 \u0432\u0440\u0435\u043c\u044f", player);
			return;
		}
		_battleType = 2;
		_creatorId = player.getObjectId();
		_team1exp = 0;
		_team2exp = 0;
		_expToReturn.clear();
		_classToReturn.clear();
		removeExp(player, 1);
		_status = 1;
		_team1list.clear();
		_team2list.clear();
		_team1live.clear();
		_team2live.clear();
		_team1list.add(player.getObjectId());
		say(player.getName() + " \u0441\u043e\u0437\u0434\u0430\u043b \u0431\u043e\u0439 " + _team1count + "\u0445" + _team2count + ", " + _team1min + "-" + _team1max + "lv vs " + _team2min + "-" + _team2max + "lv, \u0441\u0442\u0430\u0432\u043a\u0430 \u043e\u043f\u044b\u0442, \u043d\u0430\u0447\u0430\u043b\u043e \u0447\u0435\u0440\u0435\u0437 " + _timeToStart + " \u043c\u0438\u043d");
		executeTask("events.arena." + _className, "announce", new Object[0], 60000L);
	}

	public void template_register_check(final Player player, final String[] var)
	{
		if(player.isDead())
			return;
		if(_status > 1)
		{
			show("\u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0431\u043e\u044f", player);
			return;
		}
		if(var.length != 1)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435", player);
			return;
		}
		int _regTeam;
		try
		{
			_regTeam = Integer.valueOf(var[0]);
		}
		catch(Exception e)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435", player);
			return;
		}
		if(_regTeam != 1 && _regTeam != 2)
		{
			show("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u043d\u043e\u043c\u0435\u0440 \u043a\u043e\u043c\u0430\u043d\u0434\u044b, \u0432\u0432\u0435\u0434\u0438\u0442\u0435 1 \u0438\u043b\u0438 2", player);
			return;
		}
		if(_team1list.contains(player.getObjectId()) || _team2list.contains(player.getObjectId()))
		{
			show("\u0412\u044b \u0443\u0436\u0435 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u044b", player);
			return;
		}
		if(_battleType == 1 && player.getAdena() < _price)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		if(_regTeam == 1)
		{
			if(player.getLevel() < _team1min || player.getLevel() > _team1max)
			{
				show("\u0412\u044b \u043d\u0435 \u043f\u043e\u0434\u0445\u043e\u0434\u0438\u0442\u0435 \u043f\u043e \u0443\u0440\u043e\u0432\u043d\u044e", player);
				return;
			}
			if(_team1list.size() >= _team1count)
			{
				show("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 1 \u043f\u0435\u0440\u0435\u043f\u043e\u043b\u043d\u0435\u043d\u0430", player);
				return;
			}
			if(_battleType == 1)
				player.reduceAdena(_price, true);
			else if(_battleType == 2)
				removeExp(player, 1);
			_team1list.add(player.getObjectId());
			say(player.getName() + " \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043b\u0441\u044f \u0437\u0430 1 \u043a\u043e\u043c\u0430\u043d\u0434\u0443");
			if(_team1list.size() >= _team1count && _team2list.size() >= _team2count)
			{
				say("\u041a\u043e\u043c\u0430\u043d\u0434\u044b \u0433\u043e\u0442\u043e\u0432\u044b, \u0441\u0442\u0430\u0440\u0442 \u0447\u0435\u0440\u0435\u0437 1 \u043c\u0438\u043d\u0443\u0442\u0443.");
				_timeToStart = 1;
			}
		}
		else
		{
			if(player.getLevel() < _team2min || player.getLevel() > _team2max)
			{
				show("\u0412\u044b \u043d\u0435 \u043f\u043e\u0434\u0445\u043e\u0434\u0438\u0442\u0435 \u043f\u043e \u0443\u0440\u043e\u0432\u043d\u044e", player);
				return;
			}
			if(_team2list.size() >= _team2count)
			{
				show("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 2 \u043f\u0435\u0440\u0435\u043f\u043e\u043b\u043d\u0435\u043d\u0430", player);
				return;
			}
			if(_battleType == 1)
				player.reduceAdena(_price, true);
			else if(_battleType == 2)
				removeExp(player, 2);
			_team2list.add(player.getObjectId());
			say(player.getName() + " \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043b\u0441\u044f \u0437\u0430 2 \u043a\u043e\u043c\u0430\u043d\u0434\u0443");
			if(_team1list.size() >= _team1count && _team2list.size() >= _team2count)
			{
				say("\u041a\u043e\u043c\u0430\u043d\u0434\u044b \u0433\u043e\u0442\u043e\u0432\u044b, \u0441\u0442\u0430\u0440\u0442 \u0447\u0435\u0440\u0435\u0437 1 \u043c\u0438\u043d\u0443\u0442\u0443.");
				_timeToStart = 1;
			}
		}
	}

	public void template_announce()
	{
		final Player creator = GameObjectsStorage.getPlayer(_creatorId);
		if(_status != 1 || creator == null)
			return;
		if(_timeToStart > 1)
		{
			--_timeToStart;
			say(creator.getName() + " \u0441\u043e\u0437\u0434\u0430\u043b \u0431\u043e\u0439 " + _team1count + "\u0445" + _team2count + ", " + _team1min + "-" + _team1max + "lv vs " + _team2min + "-" + _team2max + "lv, \u0441\u0442\u0430\u0432\u043a\u0430 " + (_battleType == 1 ? _price + "\u0430" : "\u043e\u043f\u044b\u0442") + ", \u043d\u0430\u0447\u0430\u043b\u043e \u0447\u0435\u0440\u0435\u0437 " + _timeToStart + " \u043c\u0438\u043d");
			executeTask("events.arena." + _className, "announce", new Object[0], 60000L);
		}
		else if(_team2list.size() > 0)
		{
			say("\u041f\u043e\u0434\u0433\u043e\u0442\u043e\u0432\u043a\u0430 \u043a \u0431\u043e\u044e");
			executeTask("events.arena." + _className, "prepare", new Object[0], 5000L);
		}
		else
		{
			say("\u0411\u043e\u0439 \u043d\u0435 \u0441\u043e\u0441\u0442\u043e\u044f\u043b\u0441\u044f, \u043d\u0435\u0442 \u043f\u0440\u043e\u0442\u0438\u0432\u043d\u0438\u043a\u043e\u0432");
			_status = 0;
			if(_battleType == 1)
				returnAdenaToTeams();
			else if(_battleType == 2)
				returnExpToTeams();
			clearTeams();
		}
	}

	public void template_prepare()
	{
		if(_status != 1)
			return;
		_status = 2;
		for(final Player player : getPlayers(_team1list))
			if(!player.isDead())
				_team1live.add(player.getObjectId());
		for(final Player player : getPlayers(_team2list))
			if(!player.isDead())
				_team2live.add(player.getObjectId());
		if(!checkTeams())
			return;
		clearArena();
		paralyzeTeams();
		teleportTeamsToArena();
		say("\u0411\u043e\u0439 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 15 \u0441\u0435\u043a\u0443\u043d\u0434");
		executeTask("events.arena." + _className, "start", new Object[0], 15000L);
	}

	public void template_start()
	{
		if(_status != 2)
			return;
		if(!checkTeams())
			return;
		say("Go!!!");
		unParalyzeTeams();
		_status = 3;
		executeTask("events.arena." + _className, "timeOut", new Object[0], 180000L);
		_timeOutTask = true;
	}

	public void clearArena()
	{
		for(final Player player : World.getAroundPlayers(_manager, 2000, 200))
			if(player.isInZone(Zone.ZoneType.battle_zone))
				player.teleToLocation(_zone.getSpawn());
	}

	public boolean checkTeams()
	{
		if(_team1live.isEmpty())
		{
			teamHasLost(1);
			return false;
		}
		if(_team2live.isEmpty())
		{
			teamHasLost(2);
			return false;
		}
		return true;
	}

	public void paralyzeTeams()
	{
		final Skill revengeSkill = SkillTable.getInstance().getInfo(4515, 1);
		for(final Player player : getPlayers(_team1live))
		{
			player.getAbnormalList().stop(1411);
			revengeSkill.getEffects(player, player, false, false);
			if(player.getServitor() != null)
				revengeSkill.getEffects(player, player.getServitor(), false, false);
		}
		for(final Player player : getPlayers(_team2live))
		{
			player.getAbnormalList().stop(1411);
			revengeSkill.getEffects(player, player, false, false);
			if(player.getServitor() != null)
				revengeSkill.getEffects(player, player.getServitor(), false, false);
		}
	}

	public void unParalyzeTeams()
	{
		for(final Player player : getPlayers(_team1list))
		{
			player.getAbnormalList().stop(4515);
			if(player.getServitor() != null)
				player.getServitor().getAbnormalList().stop(4515);
		}
		for(final Player player : getPlayers(_team2list))
		{
			player.getAbnormalList().stop(4515);
			if(player.getServitor() != null)
				player.getServitor().getAbnormalList().stop(4515);
		}
	}

	public void teleportTeamsToArena()
	{
		Integer n = 0;
		for(final Player player : getPlayers(_team1live))
		{
			player.teleToLocation(_team1points.get(n));
			if(player.getServitor() != null)
				player.getServitor().teleToLocation(_team1points.get(n));
			player.setTeam(1, true);
			++n;
		}
		n = 0;
		for(final Player player : getPlayers(_team2live))
		{
			player.teleToLocation(_team2points.get(n));
			if(player.getServitor() != null)
				player.getServitor().teleToLocation(_team2points.get(n));
			player.setTeam(2, true);
			++n;
		}
	}

	public boolean playerHasLost(final Player player)
	{
		_team1live.remove(player.getObjectId());
		_team2live.remove(player.getObjectId());
		final Skill revengeSkill = SkillTable.getInstance().getInfo(4515, 1);
		player.getAbnormalList().stop(1411);
		revengeSkill.getEffects(player, player, false, false);
		return !checkTeams();
	}

	public void teamHasLost(final Integer team_id)
	{
		if(team_id == 1)
		{
			say("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 2 \u043f\u043e\u0431\u0435\u0434\u0438\u043b\u0430");
			if(_battleType == 1)
				payAdenaToTeam(2);
			else if(_battleType == 2)
				payExpToTeam(2);
		}
		else
		{
			say("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 1 \u043f\u043e\u0431\u0435\u0434\u0438\u043b\u0430");
			if(_battleType == 1)
				payAdenaToTeam(1);
			else if(_battleType == 2)
				payExpToTeam(1);
		}
		unParalyzeTeams();
		clearTeams();
		_status = 0;
		_timeOutTask = false;
	}

	public void template_timeOut()
	{
		if(_timeOutTask && _status == 3)
		{
			say("\u0412\u0440\u0435\u043c\u044f \u0438\u0441\u0442\u0435\u043a\u043b\u043e, \u043d\u0438\u0447\u044c\u044f!");
			if(_battleType == 1)
				returnAdenaToTeams();
			else if(_battleType == 2)
				returnExpToTeams();
			unParalyzeTeams();
			clearTeams();
			_status = 0;
			_timeOutTask = false;
		}
	}

	public void payAdenaToTeam(final Integer team_id)
	{
		if(team_id == 1)
			for(final Player player : getPlayers(_team1list))
				player.addAdena(_price + _team2list.size() * _price / _team1list.size());
		else
			for(final Player player : getPlayers(_team2list))
				player.addAdena(_price + _team1list.size() * _price / _team2list.size());
	}

	public void payExpToTeam(final Integer team_id)
	{
		if(team_id == 1)
			for(final Player player : getPlayers(_team1list))
			{
				returnExp(player);
				addExp(player, _team2exp / _team1list.size() / 2);
			}
		else
			for(final Player player : getPlayers(_team2list))
			{
				returnExp(player);
				addExp(player, _team1exp / _team2list.size() / 2);
			}
	}

	public void returnAdenaToTeams()
	{
		for(final Player player : getPlayers(_team1list))
			player.addAdena(_price);
		for(final Player player : getPlayers(_team2list))
			player.addAdena(_price);
	}

	public void returnExpToTeams()
	{
		for(final Player player : getPlayers(_team1list))
			returnExp(player);
		for(final Player player : getPlayers(_team2list))
			returnExp(player);
	}

	public void clearTeams()
	{
		for(final Player player : getPlayers(_team1list))
			player.setTeam(0, true);
		for(final Player player : getPlayers(_team2list))
			player.setTeam(0, true);
		_team1list.clear();
		_team2list.clear();
		_team1live.clear();
		_team2live.clear();
	}

	public void removeExp(final Player player, final int team)
	{
		final int lostExp = Math.round((Experience.LEVEL[player.getLevel() + 1] - Experience.LEVEL[player.getLevel()]) * 4L / 100L);
		player.addExpAndSp(-1 * lostExp, 0L, false, false);
		_expToReturn.put(player.getObjectId(), lostExp);
		_classToReturn.put(player.getObjectId(), player.getActiveClassId());
		if(team == 1)
			_team1exp += lostExp;
		else if(team == 2)
			_team2exp += lostExp;
	}

	public void returnExp(final Player player)
	{
		final int addExp = _expToReturn.get(player.getObjectId());
		final int classId = _classToReturn.get(player.getObjectId());
		if(addExp > 0 && player.getActiveClassId() == classId)
			player.addExpAndSp(addExp, 0L, false, false);
	}

	public void addExp(final Player player, final int exp)
	{
		final int classId = _classToReturn.get(player.getObjectId());
		if(player.getActiveClassId() == classId)
			player.addExpAndSp(exp, 0L, false, false);
	}

	public void onDie(final GameObject self, final Creature killer)
	{
		if(_status >= 2 && self != null && self.isPlayer() && (_team1list.contains(self.getObjectId()) || _team2list.contains(self.getObjectId())))
		{
			final Player player = self.getPlayer();
			final Player kplayer = killer.getPlayer();
			if(kplayer != null)
			{
				say(kplayer.getName() + " \u0443\u0431\u0438\u043b " + player.getName());
				if(player.getTeam() == kplayer.getTeam() || !_team1list.contains(kplayer.getObjectId()) && !_team2list.contains(kplayer.getObjectId()))
				{
					say("\u041d\u0430\u0440\u0443\u0448\u0435\u043d\u0438\u0435 \u043f\u0440\u0430\u0432\u0438\u043b, \u0438\u0433\u0440\u043e\u043a " + kplayer.getName() + " \u043e\u0448\u0442\u0440\u0430\u0444\u043e\u0432\u0430\u043d \u043d\u0430 " + _price);
					kplayer.reduceAdena(_price, true);
				}
				playerHasLost(player);
			}
			else
			{
				say(player.getName() + " \u0443\u0431\u0438\u0442");
				playerHasLost(player);
			}
		}
	}

	public void onPlayerExit(final Player player)
	{
		if(player != null && _status > 0 && (_team1list.contains(player.getObjectId()) || _team2list.contains(player.getObjectId())))
			switch(_status)
			{
				case 1:
				{
					removePlayer(player);
					say(player.getName() + " \u0434\u0438\u0441\u043a\u0432\u0430\u043b\u0438\u0444\u0438\u0446\u0438\u0440\u043e\u0432\u0430\u043d");
					if(player.getObjectId() == _creatorId)
					{
						say("\u0411\u043e\u0439 \u043f\u0440\u0435\u0440\u0432\u0430\u043d, \u0441\u0442\u0430\u0432\u043a\u0438 \u0432\u043e\u0437\u0432\u0440\u0430\u0449\u0435\u043d\u044b");
						if(_battleType == 1)
							returnAdenaToTeams();
						else if(_battleType == 2)
							returnExpToTeams();
						unParalyzeTeams();
						clearTeams();
						_status = 0;
						_timeOutTask = false;
						break;
					}
					break;
				}
				case 2:
				{
					removePlayer(player);
					say(player.getName() + " \u0434\u0438\u0441\u043a\u0432\u0430\u043b\u0438\u0444\u0438\u0446\u0438\u0440\u043e\u0432\u0430\u043d");
					checkTeams();
					break;
				}
				case 3:
				{
					removePlayer(player);
					say(player.getName() + " \u0434\u0438\u0441\u043a\u0432\u0430\u043b\u0438\u0444\u0438\u0446\u0438\u0440\u043e\u0432\u0430\u043d");
					checkTeams();
					break;
				}
			}
	}

	public Location onEscape(final Player player)
	{
		if(player != null && _status > 1 && player.isInZone(_zone))
			onPlayerExit(player);
		return null;
	}

	private void removePlayer(final Player player)
	{
		if(player != null)
		{
			_team1list.remove(player.getObjectId());
			_team2list.remove(player.getObjectId());
			_team1live.remove(player.getObjectId());
			_team2live.remove(player.getObjectId());
			player.setTeam(0, true);
		}
	}

	public void say(final String text)
	{
		if(_manager == null)
			return;
		final Say2 cs = new Say2(0, ChatType.SHOUT, _manager.getName(), text);
		for(final Player player : World.getAroundPlayers(_manager, 4000, 200))
			if(player != null && !player.isBlockAll())
				player.sendPacket(cs);
	}

	public class ZoneListener extends ZoneEnterLeaveListener
	{
		@Override
		public void objectEntered(final Zone zone, final GameObject object)
		{
			final Player player = object.getPlayer();
			if(_status >= 2 && player != null && !_team1list.contains(player.getObjectId()) && !_team2list.contains(player.getObjectId()))
				ThreadPoolManager.getInstance().schedule(new TeleportTask((Creature) object, _zone.getSpawn()), 3000L);
		}

		@Override
		public void objectLeaved(final Zone zone, final GameObject object)
		{
			final Player player = object.getPlayer();
			if(_status >= 2 && player != null && (_team1list.contains(player.getObjectId()) || _team2list.contains(player.getObjectId())))
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

	public class TeleportTask implements Runnable
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
			target.teleToLocation(loc);
		}
	}
}
