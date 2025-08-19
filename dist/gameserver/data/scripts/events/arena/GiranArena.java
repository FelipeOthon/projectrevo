package events.arena;

import java.util.ArrayList;

import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Zone;
import l2s.gameserver.utils.Location;

public class GiranArena extends ArenaTemplate
{
	private static GiranArena _instance;

	public static GiranArena getInstance()
	{
		if(GiranArena._instance == null)
			GiranArena._instance = new GiranArena();
		return GiranArena._instance;
	}

	public static void OnDie(final Creature cha, final Creature killer)
	{
		getInstance().onDie(cha, killer);
	}

	public void OnPlayerExit(final Player player)
	{
		getInstance().onPlayerExit(player);
	}

	public Location OnEscape(final Player player)
	{
		return getInstance().onEscape(player);
	}

	public void loadArena()
	{
		_managerId = 22220001;
		_className = "GiranArena";
		_status = 0;
		_zoneListener = new ZoneListener();
		_zone = ZoneManager.getInstance().getZoneByIndex(Zone.ZoneType.battle_zone, 3, true);
		_zone.getListenerEngine().addMethodInvokedListener(_zoneListener);
		_team1points = new ArrayList<Location>();
		_team2points = new ArrayList<Location>();
		_team1points.add(new Location(72609, 142346, -3798));
		_team1points.add(new Location(72809, 142346, -3798));
		_team1points.add(new Location(73015, 142346, -3798));
		_team1points.add(new Location(73215, 142346, -3798));
		_team1points.add(new Location(73407, 142346, -3798));
		_team2points.add(new Location(73407, 143186, -3798));
		_team2points.add(new Location(73215, 143186, -3798));
		_team2points.add(new Location(73015, 143186, -3798));
		_team2points.add(new Location(72809, 143186, -3798));
		_team2points.add(new Location(72609, 143186, -3798));
	}

	public void unLoadArena()
	{
		if(_status > 0)
			stop();
		_zone.getListenerEngine().removeMethodInvokedListener(_zoneListener);
		GiranArena._instance = null;
	}

	@Override
	public void onLoad()
	{
		getInstance().loadArena();
	}

	@Override
	public void onReload()
	{
		getInstance().unLoadArena();
	}

	public void onShutdown()
	{
		onReload();
	}

	public String DialogAppend_22220001(final Integer val)
	{
		if(val != 0)
			return "";
		final Player player = getSelf();
		if(player.isGM())
			return HtmCache.getInstance().getHtml("scripts/events/arena/22220001.html", player) + HtmCache.getInstance().getHtml("scripts/events/arena/22220001-4.html", player);
		return HtmCache.getInstance().getHtml("scripts/events/arena/22220001.html", player);
	}

	public String DialogAppend_22220002(final Integer val)
	{
		return DialogAppend_22220001(val);
	}

	public void create1()
	{
		getInstance().template_create1(getSelf());
	}

	public void create2()
	{
		getInstance().template_create2(getSelf());
	}

	public void register()
	{
		getInstance().template_register(getSelf());
	}

	public void check1(final String[] var)
	{
		getInstance().template_check1(getSelf(), getNpc(), var);
	}

	public void check2(final String[] var)
	{
		getInstance().template_check2(getSelf(), getNpc(), var);
	}

	public void register_check(final String[] var)
	{
		getInstance().template_register_check(getSelf(), var);
	}

	public void stop()
	{
		getInstance().template_stop();
	}

	public void announce()
	{
		getInstance().template_announce();
	}

	public void prepare()
	{
		getInstance().template_prepare();
	}

	public void start()
	{
		getInstance().template_start();
	}

	public static void timeOut()
	{
		getInstance().template_timeOut();
	}
}
