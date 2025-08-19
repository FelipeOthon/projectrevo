package events.heart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.PrintfFormat;
import l2s.gameserver.utils.Util;

public class heart extends Functions implements ScriptFile
{
	private static boolean _active;
	private static final List<Spawn> _spawns;
	private static final HashMap<Integer, Integer> Guesses;
	private static String links_en;
	private static String links_ru;
	private static final String[][] variants;
	private static final int EVENT_MANAGER_ID = 31227;
	private static final int[] hearts;
	private static final int[] potions;
	private static final int[] scrolls;

	public void startEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(SetActive("heart", true))
		{
			spawnEventManagers();
			ScriptFile._log.info("Event 'Change of Heart' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.ChangeofHeart.AnnounceEventStarted", (String[]) null);
		}
		else
			player.sendMessage("Event 'Change of Heart' already started.");
		heart._active = true;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void stopEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(SetActive("heart", false))
		{
			unSpawnEventManagers();
			ScriptFile._log.info("Event 'Change of Heart' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.ChangeofHeart.AnnounceEventStoped", (String[]) null);
		}
		else
			player.sendMessage("Event 'Change of Heart' not started.");
		heart._active = false;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void letsplay()
	{
		final Player player = getSelf();
		if(!player.isQuestContinuationPossible(true))
			return;
		zeroGuesses(player);
		if(haveAllHearts(player))
			show(link(HtmCache.getInstance().getHtml("scripts/events/heart/hearts_01.htm", player), isRus(player)), player);
		else
			show(HtmCache.getInstance().getHtml("scripts/events/heart/hearts_00.htm", player), player);
	}

	public void play(final String[] var)
	{
		final Player player = getSelf();
		if(!player.isQuestContinuationPossible(true) || var.length == 0)
			return;
		if(!haveAllHearts(player))
		{
			if(var[0].equalsIgnoreCase("Quit"))
				show(HtmCache.getInstance().getHtml("scripts/events/heart/hearts_00b.htm", player), player);
			else
				show(HtmCache.getInstance().getHtml("scripts/events/heart/hearts_00a.htm", player), player);
			return;
		}
		if(var[0].equalsIgnoreCase("Quit"))
		{
			final int curr_guesses = getGuesses(player);
			takeHeartsSet(player);
			reward(player, curr_guesses);
			show(HtmCache.getInstance().getHtml("scripts/events/heart/hearts_reward_" + curr_guesses + ".htm", player), player);
			zeroGuesses(player);
			return;
		}
		final int var_cat = Rnd.get(heart.variants.length);
		int var_player = 0;
		try
		{
			var_player = Integer.parseInt(var[0]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		if(var_player == var_cat)
		{
			show(fillvars(HtmCache.getInstance().getHtml("scripts/events/heart/hearts_same.htm", player), var_player, var_cat, player), player);
			return;
		}
		if(playerWins(var_player, var_cat))
		{
			incGuesses(player);
			final int curr_guesses2 = getGuesses(player);
			if(curr_guesses2 == 10)
			{
				takeHeartsSet(player);
				reward(player, curr_guesses2);
				zeroGuesses(player);
			}
			show(fillvars(HtmCache.getInstance().getHtml("scripts/events/heart/hearts_level_" + curr_guesses2 + ".htm", player), var_player, var_cat, player), player);
			return;
		}
		takeHeartsSet(player);
		reward(player, getGuesses(player) - 1);
		show(fillvars(HtmCache.getInstance().getHtml("scripts/events/heart/hearts_loose.htm", player), var_player, var_cat, player), player);
		zeroGuesses(player);
	}

	private void reward(final Player player, final int guesses)
	{
		switch(guesses)
		{
			case -1:
			case 0:
			{
				addItem(player, heart.scrolls[Rnd.get(heart.scrolls.length)], 1L);
				break;
			}
			case 1:
			{
				addItem(player, heart.potions[Rnd.get(heart.potions.length)], 10L);
				break;
			}
			case 2:
			{
				addItem(player, 1538, 1L);
				break;
			}
			case 3:
			{
				addItem(player, 3936, 1L);
				break;
			}
			case 4:
			{
				addItem(player, 951, 2L);
				break;
			}
			case 5:
			{
				addItem(player, 948, 4L);
				break;
			}
			case 6:
			{
				addItem(player, 947, 1L);
				break;
			}
			case 7:
			{
				addItem(player, 730, 3L);
				break;
			}
			case 8:
			{
				addItem(player, 729, 1L);
				break;
			}
			case 9:
			{
				addItem(player, 960, 2L);
				break;
			}
			case 10:
			{
				addItem(player, 959, 1L);
				break;
			}
		}
	}

	private String fillvars(final String s, final int var_player, final int var_cat, final Player player)
	{
		final boolean rus = isRus(player);
		return link(s.replaceFirst("Player", player.getName()).replaceFirst("%var_payer%", heart.variants[var_player][rus ? 1 : 0]).replaceFirst("%var_cat%", heart.variants[var_cat][rus ? 1 : 0]), rus);
	}

	private boolean isRus(final Player player)
	{
		return player.isLangRus();
	}

	private String link(final String s, final boolean rus)
	{
		return s.replaceFirst("%links%", rus ? heart.links_ru : heart.links_en);
	}

	private boolean playerWins(final int var_player, final int var_cat)
	{
		if(var_player == 0)
			return var_cat == 1;
		if(var_player == 1)
			return var_cat == 2;
		return var_player == 2 && var_cat == 0;
	}

	private int getGuesses(final Player player)
	{
		return heart.Guesses.containsKey(player.getObjectId()) ? heart.Guesses.get(player.getObjectId()) : 0;
	}

	private void incGuesses(final Player player)
	{
		int val = 1;
		if(heart.Guesses.containsKey(player.getObjectId()))
			val = heart.Guesses.remove(player.getObjectId()) + 1;
		heart.Guesses.put(player.getObjectId(), val);
	}

	private void zeroGuesses(final Player player)
	{
		if(heart.Guesses.containsKey(player.getObjectId()))
			heart.Guesses.remove(player.getObjectId());
	}

	private void takeHeartsSet(final Player player)
	{
		for(final int heart_id : heart.hearts)
			removeItem(player, heart_id, 1L);
	}

	private boolean haveAllHearts(final Player player)
	{
		for(final int heart_id : heart.hearts)
			if(player.getInventory().getCountOf(heart_id) < 1L)
				return false;
		return true;
	}

	public static void OnDie(final Creature cha, final Creature killer)
	{
		if(heart._active && SimpleCheckDrop(cha, killer))
			((NpcInstance) cha).dropItem(killer.getPlayer(), heart.hearts[Rnd.get(heart.hearts.length)], Util.rollDrop(1L, 1L, Config.EVENT_CHANGE_OF_HEART_CHANCE * killer.getPlayer().getRateItems() * ((MonsterInstance) cha).getTemplate().rateHp * 10000.0, true));
	}

	public static void OnPlayerEnter(final Player player)
	{
		if(heart._active)
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.ChangeofHeart.AnnounceEventStarted", (String[]) null);
	}

	private static boolean isActive()
	{
		return IsActive("heart");
	}

	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS = {
				{ 146936, 26654, -2208, 16384 },
				{ 82168, 148842, -3464, 7806 },
				{ 82204, 53259, -1488, 16384 },
				{ 18924, 145782, -3088, 44034 },
				{ 111794, 218967, -3536, 20780 },
				{ -14539, 124066, -3112, 50874 },
				{ 147271, -55573, -2736, 60304 },
				{ 87801, -143150, -1296, 28800 },
				{ -80684, 149458, -3040, 16384 } };
		SpawnNPCs(31227, EVENT_MANAGERS, heart._spawns);
	}

	private void unSpawnEventManagers()
	{
		deSpawnNPCs(heart._spawns);
	}

	@Override
	public void onLoad()
	{
		if(isActive())
		{
			heart._active = true;
			spawnEventManagers();
			ScriptFile._log.info("Loaded Event: Change of Heart [state: activated]");
		}
		else
			ScriptFile._log.info("Loaded Event: Change of Heart[state: deactivated]");
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

	static
	{
		heart._active = false;
		_spawns = new ArrayList<Spawn>();
		Guesses = new HashMap<Integer, Integer>();
		heart.links_en = "";
		heart.links_ru = "";
		variants = new String[][] {
				{ "Rock", "\u041a\u0430\u043c\u0435\u043d\u044c" },
				{ "Scissors", "\u041d\u043e\u0436\u043d\u0438\u0446\u044b" },
				{ "Paper", "\u0411\u0443\u043c\u0430\u0433\u0430" } };
		final PrintfFormat fmt = new PrintfFormat("<br><a action=\"bypass -h scripts_events.heart.heart:play %d\">\"%s!\"</a>");
		for(int i = 0; i < heart.variants.length; ++i)
		{
			heart.links_en += fmt.sprintf(new Object[] { i, heart.variants[i][0] });
			heart.links_ru += fmt.sprintf(new Object[] { i, heart.variants[i][1] });
		}
		hearts = new int[] { 4209, 4210, 4211, 4212, 4213, 4214, 4215, 4216, 4217 };
		potions = new int[] { 1374, 1375, 6036, 1539 };
		scrolls = new int[] { 3926, 3927, 3928, 3929, 3930, 3931, 3932, 3933, 3934, 3935 };
	}
}
