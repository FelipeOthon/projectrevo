package events.glitmedal;

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

public class glitmedal extends Functions implements ScriptFile
{
	private static int EVENT_MANAGER_ID1;
	private static int EVENT_MANAGER_ID2;
	private static int MEDAL_CHANCE;
	private static int GLITTMEDAL_CHANCE;
	private static boolean EnableRate;
	private int isTalker;
	private static int EVENT_MEDAL;
	private static int EVENT_GLITTMEDAL;
	private static int Badge_of_Rabbit;
	private static int Badge_of_Hyena;
	private static int Badge_of_Fox;
	private static int Badge_of_Wolf;
	private static ArrayList<Spawn> _spawns;
	private static boolean _active;

	@Override
	public void onLoad()
	{
		if(isActive())
		{
			glitmedal._active = true;
			spawnEventManagers();
			ScriptFile._log.info("Loaded Event: Medal Collection Event [state: activated]");
			if(glitmedal.MEDAL_CHANCE > 80 || glitmedal.GLITTMEDAL_CHANCE > 50)
				ScriptFile._log.info("Event Medal Collection: << W A R N I N G >> RATES IS TO HIGH!!!");
		}
		else
			ScriptFile._log.info("Loaded Event: Medal Collection Event [state: deactivated]");
	}

	private static boolean isActive()
	{
		return ServerVariables.getString("glitter", "off").equalsIgnoreCase("on");
	}

	public void startEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(!isActive())
		{
			ServerVariables.set("glitter", "on");
			spawnEventManagers();
			ScriptFile._log.info("Event 'Medal Collection Event' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.glitmedal.AnnounceEventStarted", (String[]) null);
		}
		else
			player.sendMessage("Event 'Medal Collection Event' already started.");
		glitmedal._active = true;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void stopEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(isActive())
		{
			ServerVariables.unset("glitter");
			unSpawnEventManagers();
			ScriptFile._log.info("Event 'Medal Collection Event' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.glitmedal.AnnounceEventStoped", (String[]) null);
		}
		else
			player.sendMessage("Event 'Medal Collection Event' not started.");
		glitmedal._active = false;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public static void OnPlayerEnter(final Player player)
	{
		if(glitmedal._active)
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.glitmedal.AnnounceEventStarted", (String[]) null);
	}

	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS1 = {
				{ 147893, -56622, -2776, 0 },
				{ -81070, 149960, -3040, 0 },
				{ 82882, 149332, -3464, 49000 },
				{ 44176, -48732, -800, 33000 },
				{ 147920, 25664, -2000, 16384 },
				{ 117498, 76630, -2695, 38000 },
				{ 111776, 221104, -3543, 16384 },
				{ -84516, 242971, -3730, 34000 },
				{ -13073, 122801, -3117, 0 },
				{ -44337, -113669, -224, 0 },
				{ 11281, 15652, -4584, 25000 },
				{ 44122, 50784, -3059, 57344 },
				{ 80986, 54504, -1525, 32768 },
				{ 114733, -178691, -821, 0 },
				{ 18178, 145149, -3054, 7400 } };
		NpcTemplate template = NpcTable.getTemplate(glitmedal.EVENT_MANAGER_ID1);
		for(final int[] element : EVENT_MANAGERS1)
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
				glitmedal._spawns.add(sp);
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		final int[][] EVENT_MANAGERS2 = {
				{ 147960, -56584, -2776, 0 },
				{ -81070, 149860, -3040, 0 },
				{ 82798, 149332, -3464, 49000 },
				{ 44176, -48688, -800, 33000 },
				{ 147985, 25664, -2000, 16384 },
				{ 117459, 76664, -2695, 38000 },
				{ 111724, 221111, -3543, 16384 },
				{ -84516, 243015, -3730, 34000 },
				{ -13073, 122841, -3117, 0 },
				{ -44342, -113726, -240, 0 },
				{ 11327, 15682, -4584, 25000 },
				{ 44157, 50827, -3059, 57344 },
				{ 80986, 54452, -1525, 32768 },
				{ 114719, -178742, -821, 0 },
				{ 18154, 145192, -3054, 7400 } };
		template = NpcTable.getTemplate(glitmedal.EVENT_MANAGER_ID2);
		for(final int[] element2 : EVENT_MANAGERS2)
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
				glitmedal._spawns.add(sp2);
			}
			catch(ClassNotFoundException e2)
			{
				e2.printStackTrace();
			}
	}

	private void unSpawnEventManagers()
	{
		for(final Spawn sp : glitmedal._spawns)
		{
			sp.stopRespawn();
			sp.getLastSpawn().deleteMe();
		}
		glitmedal._spawns.clear();
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
		if(glitmedal._active && cha.isMonster() && !cha.isRaid() && killer != null && killer.getPlayer() != null && Math.abs(cha.getLevel() - killer.getLevel()) < 10)
		{
			if(Rnd.chance(glitmedal.MEDAL_CHANCE * killer.getPlayer().getRateItems() * (glitmedal.EnableRate ? Config.RATE_DROP_ITEMS : 1.0f)))
			{
				final ItemInstance item = ItemTable.getInstance().createItem(glitmedal.EVENT_MEDAL);
				((NpcInstance) cha).dropItem(killer.getPlayer(), item);
			}
			if(killer.getPlayer().getInventory().getCountOf(glitmedal.Badge_of_Wolf) == 0L && Rnd.chance(glitmedal.GLITTMEDAL_CHANCE * killer.getPlayer().getRateItems() * (glitmedal.EnableRate ? Config.RATE_DROP_ITEMS : 1.0f)))
			{
				final ItemInstance item = ItemTable.getInstance().createItem(glitmedal.EVENT_GLITTMEDAL);
				((NpcInstance) cha).dropItem(killer.getPlayer(), item);
			}
		}
	}

	public void glitchang()
	{
		final Player player = getSelf();
		if(getItemCount(player, glitmedal.EVENT_MEDAL) >= 1000L)
		{
			removeItem(player, glitmedal.EVENT_MEDAL, 1000L);
			addItem(player, glitmedal.EVENT_GLITTMEDAL, 10L);
			return;
		}
		player.sendPacket(new SystemMessage(701));
	}

	public void medal()
	{
		final Player player = getSelf();
		if(getItemCount(player, glitmedal.Badge_of_Wolf) >= 1L)
		{
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent1_q0996_05.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.Badge_of_Fox) >= 1L)
		{
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent1_q0996_04.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.Badge_of_Hyena) >= 1L)
		{
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent1_q0996_03.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.Badge_of_Rabbit) >= 1L)
		{
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent1_q0996_02.htm", player), player);
			return;
		}
		show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent1_q0996_01.htm", player), player);
	}

	public void medalb()
	{
		final Player player = getSelf();
		if(getItemCount(player, glitmedal.Badge_of_Wolf) >= 1L)
		{
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_05.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.Badge_of_Fox) >= 1L)
		{
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_04.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.Badge_of_Hyena) >= 1L)
		{
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_03.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.Badge_of_Rabbit) >= 1L)
		{
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_02.htm", player), player);
			return;
		}
		show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_01.htm", player), player);
	}

	public void game()
	{
		final Player player = getSelf();
		if(getItemCount(player, glitmedal.Badge_of_Fox) >= 1L)
		{
			if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 40L)
			{
				show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_11.htm", player), player);
				return;
			}
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_12.htm", player), player);
		}
		else if(getItemCount(player, glitmedal.Badge_of_Hyena) >= 1L)
		{
			if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 20L)
			{
				show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_11.htm", player), player);
				return;
			}
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_12.htm", player), player);
		}
		else if(getItemCount(player, glitmedal.Badge_of_Rabbit) >= 1L)
		{
			if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 10L)
			{
				show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_11.htm", player), player);
				return;
			}
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_12.htm", player), player);
		}
		else
		{
			if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 5L)
			{
				show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_11.htm", player), player);
				return;
			}
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_12.htm", player), player);
		}
	}

	public void gamea()
	{
		final Player player = getSelf();
		isTalker = Rnd.get(2);
		if(getItemCount(player, glitmedal.Badge_of_Fox) >= 1L)
		{
			if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 40L)
			{
				if(isTalker == 1)
				{
					removeItem(player, glitmedal.Badge_of_Fox, 1L);
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 40L);
					addItem(player, glitmedal.Badge_of_Wolf, 1L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_24.htm", player), player);
					return;
				}
				if(isTalker == 0)
				{
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 40L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_25.htm", player), player);
					return;
				}
			}
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.Badge_of_Hyena) >= 1L)
		{
			if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 20L)
			{
				if(isTalker == 1)
				{
					removeItem(player, glitmedal.Badge_of_Hyena, 1L);
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 20L);
					addItem(player, glitmedal.Badge_of_Fox, 1L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_23.htm", player), player);
					return;
				}
				if(isTalker == 0)
				{
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 20L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_25.htm", player), player);
					return;
				}
			}
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.Badge_of_Rabbit) >= 1L)
		{
			if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 10L)
			{
				if(isTalker == 1)
				{
					removeItem(player, glitmedal.Badge_of_Rabbit, 1L);
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 10L);
					addItem(player, glitmedal.Badge_of_Hyena, 1L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_22.htm", player), player);
					return;
				}
				if(isTalker == 0)
				{
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 10L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_25.htm", player), player);
					return;
				}
			}
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 5L)
		{
			if(isTalker == 1)
			{
				removeItem(player, glitmedal.EVENT_GLITTMEDAL, 5L);
				addItem(player, glitmedal.Badge_of_Rabbit, 1L);
				show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_21.htm", player), player);
				return;
			}
			if(isTalker == 0)
			{
				removeItem(player, glitmedal.EVENT_GLITTMEDAL, 5L);
				show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_25.htm", player), player);
				return;
			}
		}
		show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player), player);
	}

	public void gameb()
	{
		final Player player = getSelf();
		isTalker = Rnd.get(2);
		if(getItemCount(player, glitmedal.Badge_of_Fox) >= 1L)
		{
			if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 40L)
			{
				if(isTalker == 1)
				{
					removeItem(player, glitmedal.Badge_of_Fox, 1L);
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 40L);
					addItem(player, glitmedal.Badge_of_Wolf, 1L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_34.htm", player), player);
					return;
				}
				if(isTalker == 0)
				{
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 40L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_35.htm", player), player);
					return;
				}
			}
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.Badge_of_Hyena) >= 1L)
		{
			if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 20L)
			{
				if(isTalker == 1)
				{
					removeItem(player, glitmedal.Badge_of_Hyena, 1L);
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 20L);
					addItem(player, glitmedal.Badge_of_Fox, 1L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_33.htm", player), player);
					return;
				}
				if(isTalker == 0)
				{
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 20L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_35.htm", player), player);
					return;
				}
			}
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.Badge_of_Rabbit) >= 1L)
		{
			if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 10L)
			{
				if(isTalker == 1)
				{
					removeItem(player, glitmedal.Badge_of_Rabbit, 1L);
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 10L);
					addItem(player, glitmedal.Badge_of_Hyena, 1L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_32.htm", player), player);
					return;
				}
				if(isTalker == 0)
				{
					removeItem(player, glitmedal.EVENT_GLITTMEDAL, 10L);
					show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_35.htm", player), player);
					return;
				}
			}
			show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player), player);
			return;
		}
		if(getItemCount(player, glitmedal.EVENT_GLITTMEDAL) >= 5L)
		{
			if(isTalker == 1)
			{
				removeItem(player, glitmedal.EVENT_GLITTMEDAL, 5L);
				addItem(player, glitmedal.Badge_of_Rabbit, 1L);
				show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_31.htm", player), player);
				return;
			}
			if(isTalker == 0)
			{
				removeItem(player, glitmedal.EVENT_GLITTMEDAL, 5L);
				show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_35.htm", player), player);
				return;
			}
		}
		show(HtmCache.getInstance().getHtml("scripts/events/glitmedal/event_col_agent2_q0996_26.htm", player), player);
	}

	static
	{
		glitmedal.EVENT_MANAGER_ID1 = 31228;
		glitmedal.EVENT_MANAGER_ID2 = 31229;
		glitmedal.MEDAL_CHANCE = Config.GLIT_MEDAL_CHANCE;
		glitmedal.GLITTMEDAL_CHANCE = Config.GLIT_GLITTMEDAL_CHANCE;
		glitmedal.EnableRate = Config.GLIT_EnableRate;
		glitmedal.EVENT_MEDAL = 6392;
		glitmedal.EVENT_GLITTMEDAL = 6393;
		glitmedal.Badge_of_Rabbit = 6399;
		glitmedal.Badge_of_Hyena = 6400;
		glitmedal.Badge_of_Fox = 6401;
		glitmedal.Badge_of_Wolf = 6402;
		glitmedal._spawns = new ArrayList<Spawn>();
		glitmedal._active = false;
	}
}
