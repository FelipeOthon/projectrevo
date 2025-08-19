package events.TheFallHarvest;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class TheFallHarvest extends Functions implements ScriptFile
{
	private static int EVENT_MANAGER_ID;
	private static List<Spawn> _spawns;
	private static boolean _active;

	@Override
	public void onLoad()
	{
		if(isActive())
		{
			TheFallHarvest._active = true;
			spawnEventManagers();
			ScriptFile._log.info("Loaded Event: The Fall Harvest [state: activated]");
		}
		else
			ScriptFile._log.info("Loaded Event: The Fall Harvest [state: deactivated]");
	}

	private static boolean isActive()
	{
		return IsActive("TheFallHarvest");
	}

	public void startEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(SetActive("TheFallHarvest", true))
		{
			spawnEventManagers();
			ScriptFile._log.info("Event 'The Fall Harvest' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.TheFallHarvest.AnnounceEventStarted", (String[]) null);
		}
		else
			player.sendMessage("Event 'The Fall Harvest' already started.");
		TheFallHarvest._active = true;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void stopEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(SetActive("TheFallHarvest", false))
		{
			unSpawnEventManagers();
			ScriptFile._log.info("Event 'The Fall Harvest' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.TheFallHarvest.AnnounceEventStoped", (String[]) null);
		}
		else
			player.sendMessage("Event 'The Fall Harvest' not started.");
		TheFallHarvest._active = false;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS = {
				{ 81921, 148921, -3467, 16384 },
				{ 146405, 28360, -2269, 49648 },
				{ 19319, 144919, -3103, 31135 },
				{ -82805, 149890, -3129, 33202 },
				{ -12347, 122549, -3104, 32603 },
				{ 110642, 220165, -3655, 61898 },
				{ 116619, 75463, -2721, 20881 },
				{ 85513, 16014, -3668, 23681 },
				{ 81999, 53793, -1496, 61621 },
				{ 148159, -55484, -2734, 44315 },
				{ 44185, -48502, -797, 27479 },
				{ 86899, -143229, -1293, 22021 } };
		SpawnNPCs(TheFallHarvest.EVENT_MANAGER_ID, EVENT_MANAGERS, TheFallHarvest._spawns);
	}

	private void unSpawnEventManagers()
	{
		deSpawnNPCs(TheFallHarvest._spawns);
	}

	@Override
	public void onReload()
	{
		unSpawnEventManagers();
	}

	@Override
	public void onShutdown()
	{
		unSpawnEventManagers();
	}

	public static void OnDie(final Creature cha, final Creature killer)
	{
		if(TheFallHarvest._active && SimpleCheckDrop(cha, killer) && Rnd.get(1000) <= Config.TFH_POLLEN_CHANCE * killer.getPlayer().getRateItems() * Config.RATE_DROP_ITEMS * ((NpcInstance) cha).getTemplate().rateHp)
			((NpcInstance) cha).dropItem(killer.getPlayer(), 6391, 1L);
	}

	public static void OnPlayerEnter(final Player player)
	{
		if(TheFallHarvest._active)
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.TheFallHarvest.AnnounceEventStarted", (String[]) null);
	}

	static
	{
		TheFallHarvest.EVENT_MANAGER_ID = 31255;
		TheFallHarvest._spawns = new ArrayList<Spawn>();
		TheFallHarvest._active = false;
	}
}
