package events.l2day;

import java.util.ArrayList;
import java.util.HashMap;

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

public class l2day extends Functions implements ScriptFile
{
	private static int EVENT_MANAGER_ID;
	private static int[] A;
	private static int[] C;
	private static int[] E;
	private static int[] F;
	private static int[] G;
	private static int[] H;
	private static int[] I;
	private static int[] L;
	private static int[] N;
	private static int[] O;
	private static int[] R;
	private static int[] S;
	private static int[] T;
	private static int[] II;
	private static int[][] letters;
	private static int BSOE;
	private static int BSOR;
	private static int GUIDANCE;
	private static int WHISPER;
	private static int FOCUS;
	private static int ACUMEN;
	private static int HASTE;
	private static int AGILITY;
	private static int EMPOWER;
	private static int MIGHT;
	private static int WINDWALK;
	private static int SHIELD;
	private static int ENCH_WPN_D;
	private static int ENCH_WPN_C;
	private static int ENCH_WPN_B;
	private static int ENCH_WPN_A;
	private static int RABBIT_EARS;
	private static int FEATHERED_HAT;
	private static int FAIRY_ANTENNAE;
	private static int ARTISANS_GOOGLES;
	private static int LITTLE_ANGEL_WING;
	private static int RING_OF_ANT_QUIEEN;
	private static int RING_OF_CORE;
	private static HashMap<String, Integer[][]> _words;
	private static ArrayList<Spawn> _spawns;
	private static boolean _active;

	@Override
	public void onLoad()
	{
		if(isActive())
		{
			l2day._active = true;
			spawnEventManagers();
			ScriptFile._log.info("Loaded Event: l2day [state: activated]");
		}
		else
			ScriptFile._log.info("Loaded Event: l2day [state: deactivated]");
	}

	private static boolean isActive()
	{
		return ServerVariables.getString("l2day", "off").equalsIgnoreCase("on");
	}

	public void startEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(!isActive())
		{
			ServerVariables.set("l2day", "on");
			spawnEventManagers();
			ScriptFile._log.info("Event 'l2day' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.l2day.AnnounceEventStarted", (String[]) null);
		}
		else
			player.sendMessage("Event 'l2day' already started.");
		l2day._active = true;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void stopEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(isActive())
		{
			ServerVariables.unset("l2day");
			unSpawnEventManagers();
			ScriptFile._log.info("Event 'l2day' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.l2day.AnnounceEventStoped", (String[]) null);
		}
		else
			player.sendMessage("Event 'l2day' not started.");
		l2day._active = false;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS = {
				{ 19541, 145419, -3103, 30419 },
				{ 147485, -59049, -2980, 9138 },
				{ 109947, 218176, -3543, 63079 },
				{ -81363, 151611, -3121, 42910 },
				{ 144741, 28846, -2453, 2059 },
				{ 44192, -48481, -796, 23331 },
				{ -13889, 122999, -3109, 40099 },
				{ 116278, 75498, -2713, 12022 },
				{ 82029, 55936, -1519, 58708 },
				{ 147142, 28555, -2261, 59402 },
				{ 82153, 148390, -3466, 57344 } };
		final NpcTemplate template = NpcTable.getTemplate(l2day.EVENT_MANAGER_ID);
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
				l2day._spawns.add(sp);
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
	}

	private void unSpawnEventManagers()
	{
		for(final Spawn sp : l2day._spawns)
		{
			sp.stopRespawn();
			sp.getLastSpawn().deleteMe();
		}
		l2day._spawns.clear();
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
		if(l2day._active && cha.isMonster() && !cha.isRaid() && killer != null && killer.getPlayer() != null && killer.getLevel() - cha.getLevel() < 10)
		{
			final int[] letter = l2day.letters[Rnd.get(l2day.letters.length)];
			if(Rnd.chance(letter[1] * Config.EVENT_L2DAY_LETTER_CHANCE * ((NpcTemplate) cha.getTemplate()).rateHp))
			{
				final ItemInstance item = ItemTable.getInstance().createItem(letter[0]);
				((NpcInstance) cha).dropItem(killer.getPlayer(), item);
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
		final Integer[][] array;
		final Integer[][] mss = array = l2day._words.get(var[0]);
		for(final Integer[] l : array)
			if(getItemCount(player, l[0]) < l[1])
			{
				player.sendPacket(new SystemMessage(701));
				return;
			}
		for(final Integer[] l : mss)
			removeItem(player, l[0], l[1]);
		final int chance = Rnd.get(10000);
		if(var[0].equalsIgnoreCase("LineageII"))
		{
			if(chance < 8500)
				addItem(player, Rnd.get(3926, 3935), 3L);
			else if(chance < 9020)
				addItem(player, l2day.BSOE, 1L);
			else if(chance < 9540)
				addItem(player, l2day.BSOR, 1L);
			else if(chance < 9680)
				addItem(player, l2day.ENCH_WPN_C, 3L);
			else if(chance < 9750)
				addItem(player, l2day.ENCH_WPN_B, 2L);
			else if(chance < 9820)
				addItem(player, l2day.ENCH_WPN_A, 1L);
			else if(chance < 9870)
				addItem(player, l2day.RABBIT_EARS, 1L);
			else if(chance < 9920)
				addItem(player, l2day.FEATHERED_HAT, 1L);
			else if(chance < 9998)
				addItem(player, l2day.FAIRY_ANTENNAE, 1L);
			else if(chance == 9998)
				addItem(player, l2day.RING_OF_ANT_QUIEEN, 1L);
			else if(chance == 9999)
				addItem(player, l2day.RING_OF_CORE, 1L);
		}
		else if(var[0].equalsIgnoreCase("Throne"))
		{
			if(chance < 8500)
				addItem(player, Rnd.get(3926, 3935), 2L);
			else if(chance < 9020)
				addItem(player, l2day.BSOE, 1L);
			else if(chance < 9540)
				addItem(player, l2day.BSOR, 1L);
			else if(chance < 9700)
				addItem(player, l2day.ENCH_WPN_D, 4L);
			else if(chance < 9810)
				addItem(player, l2day.ENCH_WPN_C, 3L);
			else if(chance < 9870)
				addItem(player, l2day.ENCH_WPN_B, 2L);
			else if(chance < 9930)
				addItem(player, l2day.ARTISANS_GOOGLES, 1L);
			else if(chance < 9998)
				addItem(player, l2day.LITTLE_ANGEL_WING, 1L);
			else if(chance == 9998)
				addItem(player, l2day.RING_OF_ANT_QUIEEN, 1L);
			else if(chance == 9999)
				addItem(player, l2day.RING_OF_CORE, 1L);
		}
		else if(var[0].equalsIgnoreCase("NCSOFT"))
			if(chance < 8500)
				addItem(player, Rnd.get(3926, 3935), 2L);
			else if(chance < 9020)
				addItem(player, l2day.BSOE, 1L);
			else if(chance < 9540)
				addItem(player, l2day.BSOR, 1L);
			else if(chance < 9700)
				addItem(player, l2day.ENCH_WPN_D, 4L);
			else if(chance < 9810)
				addItem(player, l2day.ENCH_WPN_C, 3L);
			else if(chance < 9870)
				addItem(player, l2day.ENCH_WPN_B, 2L);
			else if(chance < 9930)
				addItem(player, l2day.ARTISANS_GOOGLES, 1L);
			else if(chance < 9998)
				addItem(player, l2day.LITTLE_ANGEL_WING, 1L);
			else if(chance == 9998)
				addItem(player, l2day.RING_OF_ANT_QUIEEN, 1L);
			else if(chance == 9999)
				addItem(player, l2day.RING_OF_CORE, 1L);
	}

	public static void OnPlayerEnter(final Player player)
	{
		if(l2day._active)
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.l2day.AnnounceEventStarted", (String[]) null);
	}

	static
	{
		l2day.EVENT_MANAGER_ID = 31230;
		l2day.A = new int[] { 3875, 3 };
		l2day.C = new int[] { 3876, 3 };
		l2day.E = new int[] { 3877, 9 };
		l2day.F = new int[] { 3878, 3 };
		l2day.G = new int[] { 3879, 3 };
		l2day.H = new int[] { 3880, 3 };
		l2day.I = new int[] { 3881, 3 };
		l2day.L = new int[] { 3882, 3 };
		l2day.N = new int[] { 3883, 9 };
		l2day.O = new int[] { 3884, 6 };
		l2day.R = new int[] { 3885, 3 };
		l2day.S = new int[] { 3886, 3 };
		l2day.T = new int[] { 3887, 6 };
		l2day.II = new int[] { 3888, 3 };
		l2day.letters = new int[][] {
				l2day.A,
				l2day.C,
				l2day.E,
				l2day.F,
				l2day.G,
				l2day.H,
				l2day.I,
				l2day.L,
				l2day.N,
				l2day.O,
				l2day.R,
				l2day.S,
				l2day.T,
				l2day.II };
		l2day.BSOE = 3958;
		l2day.BSOR = 3959;
		l2day.GUIDANCE = 3926;
		l2day.WHISPER = 3927;
		l2day.FOCUS = 3928;
		l2day.ACUMEN = 3929;
		l2day.HASTE = 3930;
		l2day.AGILITY = 3931;
		l2day.EMPOWER = 3932;
		l2day.MIGHT = 3933;
		l2day.WINDWALK = 3934;
		l2day.SHIELD = 3935;
		l2day.ENCH_WPN_D = 955;
		l2day.ENCH_WPN_C = 951;
		l2day.ENCH_WPN_B = 947;
		l2day.ENCH_WPN_A = 729;
		l2day.RABBIT_EARS = 8947;
		l2day.FEATHERED_HAT = 8950;
		l2day.FAIRY_ANTENNAE = 8949;
		l2day.ARTISANS_GOOGLES = 8951;
		l2day.LITTLE_ANGEL_WING = 8948;
		l2day.RING_OF_ANT_QUIEEN = 6660;
		l2day.RING_OF_CORE = 6662;
		(l2day._words = new HashMap<String, Integer[][]>()).put("LineageII", new Integer[][] {
				{ l2day.L[0], 1 },
				{ l2day.I[0], 1 },
				{ l2day.N[0], 1 },
				{ l2day.E[0], 2 },
				{ l2day.A[0], 1 },
				{ l2day.G[0], 1 },
				{ l2day.II[0], 1 } });
		l2day._words.put("THRONE", new Integer[][] {
				{ l2day.T[0], 1 },
				{ l2day.H[0], 1 },
				{ l2day.R[0], 1 },
				{ l2day.O[0], 1 },
				{ l2day.N[0], 1 },
				{ l2day.E[0], 1 } });
		l2day._words.put("NCSOFT", new Integer[][] {
				{ l2day.N[0], 1 },
				{ l2day.C[0], 1 },
				{ l2day.S[0], 1 },
				{ l2day.O[0], 1 },
				{ l2day.F[0], 1 },
				{ l2day.T[0], 1 } });
		l2day._spawns = new ArrayList<Spawn>();
		l2day._active = false;
	}
}
