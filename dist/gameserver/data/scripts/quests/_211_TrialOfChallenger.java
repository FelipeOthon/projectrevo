package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _211_TrialOfChallenger extends Quest implements ScriptFile
{
	private static final int Filaur = 30535;
	private static final int Kash = 30644;
	private static final int Martien = 30645;
	private static final int Raldo = 30646;
	private static final int ChestOfShyslassys = 30647;
	private static final int Shyslassys = 27110;
	private static final int CaveBasilisk = 27111;
	private static final int Gorr = 27112;
	private static final int Baraham = 27113;
	private static final int SuccubusQueen = 27114;
	private static final int LETTER_OF_KASH_ID = 2628;
	private static final int SCROLL_OF_SHYSLASSY_ID = 2631;
	private static final int WATCHERS_EYE1_ID = 2629;
	private static final int BROKEN_KEY_ID = 2632;
	private static final int MITHRIL_SCALE_GAITERS_MATERIAL_ID = 2918;
	private static final int BRIGANDINE_GAUNTLET_PATTERN_ID = 2927;
	private static final int MANTICOR_SKIN_GAITERS_PATTERN_ID = 1943;
	private static final int GAUNTLET_OF_REPOSE_OF_THE_SOUL_PATTERN_ID = 1946;
	private static final int IRON_BOOTS_DESIGN_ID = 1940;
	private static final int TOME_OF_BLOOD_PAGE_ID = 2030;
	private static final int ELVEN_NECKLACE_BEADS_ID = 1904;
	private static final int WHITE_TUNIC_PATTERN_ID = 1936;
	private static final int MARK_OF_CHALLENGER_ID = 2627;
	private static final int WATCHERS_EYE2_ID = 2630;
	private static final int RewardExp = 533803;
	private static final int RewardSP = 34621;
	private static final int RewardAdena = 97278;
	public NpcInstance Raldo_Spawn;

	private void Spawn_Raldo(final QuestState st)
	{
		if(Raldo_Spawn != null)
			Raldo_Spawn.deleteMe();
		Raldo_Spawn = this.addSpawn(30646, st.getPlayer().getLoc(), 100, 300000);
	}

	public _211_TrialOfChallenger()
	{
		super(false);
		this.addStartNpc(30644);
		this.addTalkId(new int[] { 30535 });
		this.addTalkId(new int[] { 30645 });
		this.addTalkId(new int[] { 30646 });
		this.addTalkId(new int[] { 30647 });
		this.addKillId(new int[] { 27110 });
		this.addKillId(new int[] { 27111 });
		this.addKillId(new int[] { 27112 });
		this.addKillId(new int[] { 27113 });
		this.addKillId(new int[] { 27114 });
		addQuestItem(new int[] { 2631, 2628, 2629, 2632, 2630 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("1"))
		{
			htmltext = "kash_q0211_05.htm";
			st.set("cond", "1");
			st.setState(2);
			if(!st.getPlayer().getVarBoolean("dd1"))
			{
				st.giveItems(7562, 64L);
				st.getPlayer().setVar("dd1", "1");
			}
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30644_1"))
			htmltext = "kash_q0211_04.htm";
		else if(event.equalsIgnoreCase("30645_1"))
		{
			htmltext = "martian_q0211_02.htm";
			st.takeItems(2628, 1L);
			st.set("cond", "4");
		}
		else if(event.equalsIgnoreCase("30647_1"))
		{
			if(st.getQuestItemsCount(2632) > 0L)
			{
				st.giveItems(2631, 1L);
				if(Rnd.chance(22))
				{
					htmltext = "chest_of_shyslassys_q0211_03.htm";
					st.takeItems(2632, 1L);
					st.playSound(Quest.SOUND_JACKPOT);
					final int n = Rnd.get(100);
					if(n > 90)
					{
						st.giveItems(2918, 1L);
						st.giveItems(2927, 1L);
						st.giveItems(1943, 1L);
						st.giveItems(1946, 1L);
						st.giveItems(1940, 1L);
					}
					else if(n > 70)
					{
						st.giveItems(2030, 1L);
						st.giveItems(1904, 1L);
					}
					else if(n > 40)
						st.giveItems(1936, 1L);
					else
						st.giveItems(1940, 1L);
				}
				else
				{
					htmltext = "chest_of_shyslassys_q0211_02.htm";
					st.takeItems(2632, -1L);
					st.giveItems(57, Rnd.get(1000) + 1);
				}
			}
			else
				htmltext = "chest_of_shyslassys_q0211_04.htm";
		}
		else if(event.equalsIgnoreCase("30646_1"))
			htmltext = "raldo_q0211_02.htm";
		else if(event.equalsIgnoreCase("30646_2"))
			htmltext = "raldo_q0211_03.htm";
		else if(event.equalsIgnoreCase("30646_3"))
		{
			htmltext = "raldo_q0211_04.htm";
			st.set("cond", "8");
			st.takeItems(2630, 1L);
		}
		else if(event.equalsIgnoreCase("30646_4"))
		{
			htmltext = "raldo_q0211_06.htm";
			st.set("cond", "8");
			st.takeItems(2630, 1L);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(2627) > 0L)
		{
			st.exitCurrentQuest(true);
			return "completed";
		}
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int cond = st.getInt("cond");
		if(id == 1)
		{
			st.set("cond", "0");
			if(npcId == 30644)
				if(st.getPlayer().getClassId().ordinal() == 1 || st.getPlayer().getClassId().ordinal() == 19 || st.getPlayer().getClassId().ordinal() == 32 || st.getPlayer().getClassId().ordinal() == 45 || st.getPlayer().getClassId().ordinal() == 47)
				{
					if(st.getPlayer().getLevel() >= 35)
						htmltext = "kash_q0211_03.htm";
					else
					{
						htmltext = "kash_q0211_01.htm";
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "kash_q0211_02.htm";
					st.exitCurrentQuest(true);
				}
		}
		else if(npcId == 30644 && cond == 1)
			htmltext = "kash_q0211_06.htm";
		else if(npcId == 30644 && cond == 2 && st.getQuestItemsCount(2631) == 1L)
		{
			htmltext = "kash_q0211_07.htm";
			st.takeItems(2631, 1L);
			st.giveItems(2628, 1L);
			st.set("cond", "3");
		}
		else if(npcId == 30644 && cond == 1 && st.getQuestItemsCount(2628) == 1L)
			htmltext = "kash_q0211_08.htm";
		else if(npcId == 30644 && cond >= 7)
			htmltext = "kash_q0211_09.htm";
		else if(npcId == 30645 && cond == 3 && st.getQuestItemsCount(2628) == 1L)
			htmltext = "martian_q0211_01.htm";
		else if(npcId == 30645 && cond == 4 && st.getQuestItemsCount(2629) == 0L)
			htmltext = "martian_q0211_03.htm";
		else if(npcId == 30645 && cond == 5 && st.getQuestItemsCount(2629) > 0L)
		{
			htmltext = "martian_q0211_04.htm";
			st.takeItems(2629, 1L);
			st.set("cond", "6");
		}
		else if(npcId == 30645 && cond == 6)
			htmltext = "martian_q0211_05.htm";
		else if(npcId == 30645 && cond >= 7)
			htmltext = "martian_q0211_06.htm";
		else if(npcId == 30647 && cond == 2)
			htmltext = "chest_of_shyslassys_q0211_01.htm";
		else if(npcId == 30646 && cond == 7 && st.getQuestItemsCount(2630) > 0L)
			htmltext = "raldo_q0211_01.htm";
		else if(npcId == 30646 && cond == 8)
			htmltext = "raldo_q0211_06a.htm";
		else if(npcId == 30646 && cond == 10)
		{
			htmltext = "raldo_q0211_07.htm";
			st.takeItems(2632, -1L);
			st.giveItems(2627, 1L);
			if(!st.getPlayer().getVarBoolean("prof2.1"))
			{
				st.addExpAndSp(533803L, 34621L, true);
				st.giveItems(57, 97278L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
				st.getPlayer().setVar("prof2.1", "1");
			}
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		else if(npcId == 30535 && cond == 8)
		{
			if(st.getPlayer().getLevel() >= 36)
			{
				htmltext = "elder_filaur_q0211_01.htm";
				st.addRadar(176560, -184969, -3729);
				st.set("cond", "9");
			}
			else
				htmltext = "elder_filaur_q0211_03.htm";
		}
		else if(npcId == 30535 && cond == 9)
		{
			htmltext = "elder_filaur_q0211_02.htm";
			st.addRadar(176560, -184969, -3729);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 27110 && cond == 1 && st.getQuestItemsCount(2631) == 0L && st.getQuestItemsCount(2632) == 0L)
		{
			st.giveItems(2632, 1L);
			st.addSpawn(30647);
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "2");
		}
		else if(npcId == 27112 && cond == 4 && st.getQuestItemsCount(2629) == 0L)
		{
			st.giveItems(2629, 1L);
			st.set("cond", "5");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(npcId == 27113 && (cond == 6 || cond == 7))
		{
			if(st.getQuestItemsCount(2630) == 0L)
				st.giveItems(2630, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "7");
			Spawn_Raldo(st);
		}
		else if(npcId == 27114 && (cond == 9 || cond == 10))
		{
			st.set("cond", "10");
			st.playSound(Quest.SOUND_MIDDLE);
			Spawn_Raldo(st);
		}
		return null;
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
