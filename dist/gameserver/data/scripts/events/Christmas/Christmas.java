package events.Christmas;

import java.util.ArrayList;

import l2s.commons.util.Rnd;
import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;

public class Christmas extends Functions implements ScriptFile
{
	private static int EVENT_MANAGER_ID;
	private static int CTREE_ID;
	private static int[][] _dropdata;
	private static ArrayList<Spawn> _spawns;
	private static boolean _active;

	@Override
	public void onLoad()
	{
		if(isActive())
		{
			Christmas._active = true;
			spawnEventManagers();
			ScriptFile._log.info("Loaded Event: Christmas [state: activated]");
		}
		else
			ScriptFile._log.info("Loaded Event: Christmas [state: deactivated]");
	}

	private static boolean isActive()
	{
		return ServerVariables.getString("Christmas", "off").equalsIgnoreCase("on");
	}

	public void startEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(!isActive())
		{
			ServerVariables.set("Christmas", "on");
			spawnEventManagers();
			ScriptFile._log.info("Event 'Christmas' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.Christmas.AnnounceEventStarted", (String[]) null);
		}
		else
			player.sendMessage("Event 'Christmas' already started.");
		Christmas._active = true;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void stopEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(isActive())
		{
			ServerVariables.unset("Christmas");
			unSpawnEventManagers();
			ScriptFile._log.info("Event 'Christmas' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.Christmas.AnnounceEventStoped", (String[]) null);
		}
		else
			player.sendMessage("Event 'Christmas' not started.");
		Christmas._active = false;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS = {
				{ 81921, 148921, -3467, 16384 },
				{ 146405, 28360, -2269, 49648 },
				{ 19319, 144919, -3103, 31135 },
				{ -82805, 149890, -3129, 16384 },
				{ -12347, 122549, -3104, 16384 },
				{ 110642, 220165, -3655, 61898 },
				{ 116619, 75463, -2721, 20881 },
				{ 85513, 16014, -3668, 23681 },
				{ 81999, 53793, -1496, 61621 },
				{ 148159, -55484, -2734, 44315 },
				{ 44185, -48502, -797, 27479 },
				{ 86899, -143229, -1293, 8192 } };
		NpcTemplate template = NpcTable.getTemplate(Christmas.EVENT_MANAGER_ID);
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
				Christmas._spawns.add(sp);
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		final int[][] CTREES = {
				{ 81961, 148921, -3467, 0 },
				{ 146445, 28360, -2269, 0 },
				{ 19319, 144959, -3103, 0 },
				{ -82845, 149890, -3129, 0 },
				{ -12387, 122549, -3104, 0 },
				{ 110602, 220165, -3655, 0 },
				{ 116659, 75463, -2721, 0 },
				{ 85553, 16014, -3668, 0 },
				{ 81999, 53743, -1496, 0 },
				{ 148199, -55484, -2734, 0 },
				{ 44185, -48542, -797, 0 },
				{ 86859, -143229, -1293, 0 } };
		template = NpcTable.getTemplate(Christmas.CTREE_ID);
		for(final int[] element2 : CTREES)
			try
			{
				final Spawn sp2 = new Spawn(template);
				sp2.setLocx(element2[0]);
				sp2.setLocy(element2[1]);
				sp2.setLocz(element2[2]);
				sp2.setAmount(1);
				sp2.setHeading(element2[3]);
				sp2.setRespawnDelay(0);
				sp2.init();
				Christmas._spawns.add(sp2);
			}
			catch(ClassNotFoundException e2)
			{
				e2.printStackTrace();
			}
	}

	private void unSpawnEventManagers()
	{
		for(final Spawn sp : Christmas._spawns)
		{
			sp.stopRespawn();
			sp.getLastSpawn().deleteMe();
		}
		Christmas._spawns.clear();
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
		if(Christmas._active && cha.isMonster() && !cha.isRaid() && killer != null && killer.getPlayer() != null && Math.abs(cha.getLevel() - killer.getLevel()) < 10)
		{
			int dropCounter = 0;
			for(final int[] drop : Christmas._dropdata)
				if(Rnd.get(1000) <= drop[1] * killer.getPlayer().getRateItems() * Config.RATE_DROP_ITEMS)
				{
					++dropCounter;
					final ItemInstance item = ItemTable.getInstance().createItem(drop[0]);
					((NpcInstance) cha).dropItem(killer.getPlayer(), item);
					if(dropCounter > 2)
						break;
				}
		}
	}

	public void exchange(final String[] var)
	{
		final Player player = getSelf();
		if(!player.isQuestContinuationPossible(true))
			return;
		if(player.isActionsDisabled() || player.isSitting() || player.getLastNpc().getDistance(player) > 300.0)
			return;
		if(var[0].equalsIgnoreCase("0"))
		{
			if(getItemCount(player, 5556) >= 4L && getItemCount(player, 5557) >= 4L && getItemCount(player, 5558) >= 10L && getItemCount(player, 5559) >= 1L)
			{
				removeItem(player, 5556, 4L);
				removeItem(player, 5557, 4L);
				removeItem(player, 5558, 10L);
				removeItem(player, 5559, 1L);
				addItem(player, 5560, 1L);
				return;
			}
			player.sendPacket(new SystemMessage(701));
		}
		if(var[0].equalsIgnoreCase("1"))
		{
			if(getItemCount(player, 5560) >= 10L)
			{
				removeItem(player, 5560, 10L);
				addItem(player, 5561, 1L);
				return;
			}
			player.sendPacket(new SystemMessage(701));
		}
		if(var[0].equalsIgnoreCase("2"))
		{
			if(getItemCount(player, 5560) >= 10L)
			{
				removeItem(player, 5560, 10L);
				addItem(player, 7836, 1L);
				return;
			}
			player.sendPacket(new SystemMessage(701));
		}
		if(var[0].equalsIgnoreCase("3"))
		{
			if(getItemCount(player, 5560) >= 10L)
			{
				removeItem(player, 5560, 10L);
				addItem(player, 8936, 1L);
				return;
			}
			player.sendPacket(new SystemMessage(701));
		}
	}

	public static void OnPlayerEnter(final Player player)
	{
		if(Christmas._active)
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.Christmas.AnnounceEventStarted", (String[]) null);
	}

	static
	{
		Christmas.EVENT_MANAGER_ID = 31863;
		Christmas.CTREE_ID = 13006;
		Christmas._dropdata = new int[][] { { 5556, 10 }, { 5557, 20 }, { 5558, 20 }, { 5559, 5 } };
		Christmas._spawns = new ArrayList<Spawn>();
		Christmas._active = false;
	}
}
