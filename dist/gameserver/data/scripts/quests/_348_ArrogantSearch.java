package quests;

import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.RadarControl;
import l2s.gameserver.scripts.ScriptFile;

public class _348_ArrogantSearch extends Quest implements ScriptFile
{
	private static final int ARK_GUARDIAN_ELBEROTH = 27182;
	private static final int ARK_GUARDIAN_SHADOWFANG = 27183;
	private static final int ANGEL_KILLER = 27184;
	private static final int LESSER_GIANT_MAGE = 20657;
	private static final int LESSER_GIANT_ELDER = 20658;
	private static final int GUARDIAN_ANGEL_1 = 20830;
	private static final int GUARDIAN_ANGEL_2 = 20859;
	private static final int SEAL_ANGEL_1 = 20831;
	private static final int SEAL_ANGEL_2 = 20860;
	private static final int HANELLIN = 30864;
	private static final int HOLY_ARK_OF_SECRECY_1 = 30977;
	private static final int HOLY_ARK_OF_SECRECY_2 = 30978;
	private static final int HOLY_ARK_OF_SECRECY_3 = 30979;
	private static final int ARK_GUARDIANS_CORPSE = 30980;
	private static final int HARNE = 30144;
	private static final int CLAUDIA_ATHEBALT = 31001;
	private static final int MARTIEN = 30645;
	private static final int GUSTAV_ATHEBALDT = 30760;
	private static final int HARDIN = 30832;
	private static final int HEINE = 30969;
	private static final int TITANS_POWERSTONE = 4287;
	private static final int HANELLINS_FIRST_LETTER = 4288;
	private static final int HANELLINS_SECOND_LETTER = 4289;
	private static final int HANELLINS_THIRD_LETTER = 4290;
	private static final int FIRST_KEY_OF_ARK = 4291;
	private static final int SECOND_KEY_OF_ARK = 4292;
	private static final int THIRD_KEY_OF_ARK = 4293;
	private static final int WHITE_FABRIC_1 = 4294;
	private static final int BLOODED_FABRIC = 4295;
	private static final int HANELLINS_WHITE_FLOWER = 4394;
	private static final int HANELLINS_RED_FLOWER = 4395;
	private static final int HANELLINS_YELLOW_FLOWER = 4396;
	private static final int BOOK_OF_SAINT = 4397;
	private static final int BLOOD_OF_SAINT = 4398;
	private static final int BRANCH_OF_SAINT = 4399;
	private static final int WHITE_FABRIC_0 = 4400;
	private static final int WHITE_FABRIC_2 = 5232;
	private static final int ANTIDOTE = 1831;
	private static final int HEALING_POTION = 1061;
	private static final int ANIMAL_BONE = 1872;
	private static final int SYNTHETIC_COKES = 1888;
	private static final HashMap<Integer, Integer[]> ARKS;
	private static final HashMap<Integer, String[]> ARKS_TEXT;
	private static final HashMap<Integer, Integer[]> ARK_OWNERS;
	private static final HashMap<Integer, String[]> ARK_OWNERS_TEXT;
	private static final HashMap<Integer, String[]> BLOODY_OWNERS;
	private static final HashMap<Integer, Integer[]> DROPS;
	private static final int[] DROPS_29;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _348_ArrogantSearch()
	{
		super(true);
		this.addStartNpc(30864);
		this.addTalkId(new int[] { 30980 });
		for(final int i : _348_ArrogantSearch.ARK_OWNERS.keySet())
			this.addTalkId(new int[] { i });
		for(final int i : _348_ArrogantSearch.ARKS.keySet())
			this.addTalkId(new int[] { i });
		for(final int i : _348_ArrogantSearch.BLOODY_OWNERS.keySet())
			this.addTalkId(new int[] { i });
		for(final int i : _348_ArrogantSearch.DROPS.keySet())
			this.addKillId(new int[] { i });
		addQuestItem(new int[] { 4288, 4289, 4290, 4394, 4395, 4396, 4397, 4294, 4398, 4399, 4400, 5232, 4291, 4292, 4293 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("30864_02"))
		{
			st.setState(2);
			st.set("cond", "2");
			htmltext = "30864-03.htm";
		}
		else if(event.equals("30864_04a"))
		{
			st.set("cond", "4");
			st.takeItems(4287, -1L);
			htmltext = "30864-04c.htm";
			st.set("companions", "0");
		}
		else if(event.equals("30864_04b"))
		{
			st.set("cond", "3");
			st.set("companions", "1");
			st.takeItems(4287, -1L);
			htmltext = "not yet implemented";
		}
		else if(event.equals("30864_07"))
			htmltext = "30864-07b.htm";
		else if(event.equals("30864_07b"))
			htmltext = "30864-07c.htm";
		else if(event.equals("30864_07c"))
			htmltext = "30864-07d.htm";
		else if(event.equals("30864_07meet"))
		{
			htmltext = "30864-07meet.htm";
			st.set("cond", "24");
			st.set("reward1", "1");
		}
		else if(event.equals("30864_07money"))
		{
			htmltext = "30864-07money.htm";
			st.set("cond", "25");
			st.set("reward1", "1");
		}
		else if(event.equals("30864_08"))
			htmltext = "30864-08b.htm";
		else if(event.equals("30864_08b"))
		{
			htmltext = "30864-08c.htm";
			st.giveItems(4294, 9L);
			st.set("cond", "26");
		}
		else if(event.equals("30864_09"))
		{
			st.set("cond", "27");
			htmltext = "30864-09c.htm";
		}
		else if(event.equals("30864_10continue"))
		{
			htmltext = "30864-08c.htm";
			st.giveItems(4294, 10L);
			for(final int i : _348_ArrogantSearch.BLOODY_OWNERS.keySet())
				st.set(_348_ArrogantSearch.BLOODY_OWNERS.get(i)[1], "0");
			st.set("cond", "29");
		}
		else if(event.equals("30864_10quit"))
		{
			htmltext = "30864-10c.htm";
			st.takeItems(4294, -1L);
			st.takeItems(4295, -1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		final int cond = st.getCond();
		final int reward1 = st.getInt("reward1");
		if(npcId == 30864)
		{
			if(id == 1)
			{
				if(st.getQuestItemsCount(4295) == 1L)
				{
					htmltext = "30864-Baium.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					st.set("cond", "0");
					if(st.getPlayer().getLevel() < 60)
					{
						htmltext = "30864-01.htm";
						st.exitCurrentQuest(true);
					}
					else if(cond == 0)
					{
						st.set("cond", "1");
						st.set("reward1", "0");
						for(final int i : _348_ArrogantSearch.BLOODY_OWNERS.keySet())
							st.set(_348_ArrogantSearch.BLOODY_OWNERS.get(i)[1], "0");
						htmltext = "30864-02.htm";
					}
				}
			}
			else if(cond == 1)
				htmltext = "30864-02.htm";
			else if(cond == 2 && st.getQuestItemsCount(4287) == 0L)
				htmltext = "30864-03a.htm";
			else if(cond == 2)
				htmltext = "30864-04.htm";
			else if(cond == 4)
			{
				st.set("cond", "5");
				st.giveItems(4288, 1L);
				st.giveItems(4289, 1L);
				st.giveItems(4290, 1L);
				htmltext = "30864-05.htm";
			}
			else if(cond == 5 && st.getQuestItemsCount(4397) + st.getQuestItemsCount(4398) + st.getQuestItemsCount(4399) < 3L)
				htmltext = "30864-05.htm";
			else if(cond == 5)
			{
				htmltext = "30864-06.htm";
				st.takeItems(4397, -1L);
				st.takeItems(4398, -1L);
				st.takeItems(4399, -1L);
				st.set("cond", "22");
			}
			else if(cond == 22 && st.getQuestItemsCount(1831) < 5L && st.getQuestItemsCount(1061) < 1L)
				htmltext = "30864-06a.htm";
			else if(cond == 22 && st.getQuestItemsCount(4294) > 0L)
				htmltext = "30864-07c.htm";
			else if(cond == 22)
			{
				st.takeItems(1831, 5L);
				st.takeItems(1061, 1L);
				if(st.getInt("companions") == 0)
				{
					htmltext = "30864-07.htm";
					st.giveItems(4294, 1L);
				}
				else
				{
					st.set("cond", "23");
					htmltext = "not implemented yet";
					st.giveItems(4400, 3L);
				}
			}
			else if((cond == 24 || cond == 25) && reward1 < 2)
				htmltext = "30864-07a.htm";
			else if(cond == 25 && reward1 > 2)
				htmltext = "30864-08b.htm";
			else if(cond == 25)
			{
				htmltext = "30864-08.htm";
				st.giveItems(1872, 2L);
				st.giveItems(1888, 2L);
				st.giveItems(Rnd.get(10) + 4103, 1L);
				st.set("reward1", "3");
			}
			else if(cond == 26 && st.getQuestItemsCount(4294) > 0L)
				htmltext = "30864-09a.htm";
			else if(cond == 26 && st.getQuestItemsCount(4295) < 10L)
			{
				htmltext = "30864-09b.htm";
				st.giveItems(57, 5000L);
				st.takeItems(4295, -1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else if(cond == 26)
				htmltext = "30864-09.htm";
			else if(cond == 27 && st.getInt(_348_ArrogantSearch.BLOODY_OWNERS.get(30760)[1]) + st.getInt(_348_ArrogantSearch.BLOODY_OWNERS.get(30832)[1]) + st.getInt(_348_ArrogantSearch.BLOODY_OWNERS.get(30969)[1]) < 3)
				htmltext = "30864-10a.htm";
			else if(cond == 27)
			{
				htmltext = "30864-10.htm";
				st.giveItems(1872, 5L);
				st.giveItems(Rnd.get(8) + 4113, 1L);
				st.set("cond", "28");
			}
			else if(cond == 28)
				htmltext = "30864-10b.htm";
			else if(cond == 29 && st.getQuestItemsCount(4294) > 0L)
				htmltext = "30864-09a.htm";
			else if(cond == 29 && st.getQuestItemsCount(4295) < 10L)
			{
				htmltext = "30864-09b.htm";
				st.giveItems(57, 5000L);
				st.takeItems(4295, -1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else if(cond == 29)
				htmltext = "30864-09.htm";
		}
		else if(cond == 5)
		{
			if(_348_ArrogantSearch.ARK_OWNERS.containsKey(npcId))
			{
				if(st.getQuestItemsCount(_348_ArrogantSearch.ARK_OWNERS.get(npcId)[0]) == 1L)
				{
					st.takeItems(_348_ArrogantSearch.ARK_OWNERS.get(npcId)[0], 1L);
					htmltext = _348_ArrogantSearch.ARK_OWNERS_TEXT.get(npcId)[0];
					st.getPlayer().sendPacket(new RadarControl(0, 1, _348_ArrogantSearch.ARK_OWNERS.get(npcId)[2], _348_ArrogantSearch.ARK_OWNERS.get(npcId)[3], _348_ArrogantSearch.ARK_OWNERS.get(npcId)[4]));
				}
				else if(st.getQuestItemsCount(_348_ArrogantSearch.ARK_OWNERS.get(npcId)[1]) < 1L)
				{
					htmltext = _348_ArrogantSearch.ARK_OWNERS_TEXT.get(npcId)[1];
					st.getPlayer().sendPacket(new RadarControl(0, 1, _348_ArrogantSearch.ARK_OWNERS.get(npcId)[2], _348_ArrogantSearch.ARK_OWNERS.get(npcId)[3], _348_ArrogantSearch.ARK_OWNERS.get(npcId)[4]));
				}
				else
					htmltext = _348_ArrogantSearch.ARK_OWNERS_TEXT.get(npcId)[2];
			}
			else if(_348_ArrogantSearch.ARKS.containsKey(npcId))
			{
				if(st.getQuestItemsCount(_348_ArrogantSearch.ARKS.get(npcId)[0]) == 0L)
				{
					if(_348_ArrogantSearch.ARKS.get(npcId)[1] != 0)
						st.addSpawn(_348_ArrogantSearch.ARKS.get(npcId)[1], 120000);
					return _348_ArrogantSearch.ARKS_TEXT.get(npcId)[0];
				}
				if(st.getQuestItemsCount(_348_ArrogantSearch.ARKS.get(npcId)[2]) == 1L)
					htmltext = _348_ArrogantSearch.ARKS_TEXT.get(npcId)[2];
				else
				{
					htmltext = _348_ArrogantSearch.ARKS_TEXT.get(npcId)[1];
					st.takeItems(_348_ArrogantSearch.ARKS.get(npcId)[0], 1L);
					st.giveItems(_348_ArrogantSearch.ARKS.get(npcId)[2], 1L);
				}
			}
			else if(npcId == 30980)
				if(st.getQuestItemsCount(4291) == 0L && st.getInt("angelKillerIsDefeated") == 0)
				{
					st.addSpawn(27184, 120000);
					htmltext = "30980-01.htm";
				}
				else if(st.getQuestItemsCount(4291) == 0L && st.getInt("angelKillerIsDefeated") == 1)
				{
					st.giveItems(4291, 1L);
					htmltext = "30980-02.htm";
				}
				else
					htmltext = "30980-03.htm";
		}
		else if(cond == 27 && _348_ArrogantSearch.BLOODY_OWNERS.containsKey(npcId))
			if(st.getInt(_348_ArrogantSearch.BLOODY_OWNERS.get(npcId)[1]) < 1)
			{
				if(st.getQuestItemsCount(4295) >= Integer.parseInt(_348_ArrogantSearch.BLOODY_OWNERS.get(npcId)[0]))
				{
					st.takeItems(4295, Integer.parseInt(_348_ArrogantSearch.BLOODY_OWNERS.get(npcId)[0]));
					st.set(_348_ArrogantSearch.BLOODY_OWNERS.get(npcId)[1], "1");
					htmltext = _348_ArrogantSearch.BLOODY_OWNERS.get(npcId)[2];
				}
				else
					htmltext = _348_ArrogantSearch.BLOODY_OWNERS.get(npcId)[3];
			}
			else
				htmltext = _348_ArrogantSearch.BLOODY_OWNERS.get(npcId)[4];
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final Integer[] drop = _348_ArrogantSearch.DROPS.get(npcId);
		if(drop != null)
		{
			final int cond = drop[0];
			final int item = drop[1];
			final int max = drop[2];
			final int chance = drop[3];
			final int take = drop[4];
			if(st.getCond() == cond && st.getQuestItemsCount(item) < max && (take == 0 || st.getQuestItemsCount(take) > 0L) && Rnd.chance(chance))
			{
				st.giveItems(item, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
				if(take != 0)
					st.takeItems(take, 1L);
			}
		}
		if(ArrayUtils.contains(_348_ArrogantSearch.DROPS_29, npcId) && st.getCond() == 29 && st.getQuestItemsCount(4295) < 10L && st.getQuestItemsCount(4294) > 0L && Rnd.chance(25))
		{
			st.giveItems(4295, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
			st.takeItems(4294, 1L);
		}
		if(npcId == 27184)
			return "Ha, that was fun! If you wish to find the key, search the corpse";
		return null;
	}

	static
	{
		ARKS = new HashMap<Integer, Integer[]>();
		ARKS_TEXT = new HashMap<Integer, String[]>();
		ARK_OWNERS = new HashMap<Integer, Integer[]>();
		ARK_OWNERS_TEXT = new HashMap<Integer, String[]>();
		BLOODY_OWNERS = new HashMap<Integer, String[]>();
		DROPS = new HashMap<Integer, Integer[]>();
		DROPS_29 = new int[] { 20830, 20859, 20831, 20860 };
		_348_ArrogantSearch.ARKS.put(30977, new Integer[] { 4291, 0, 4398 });
		_348_ArrogantSearch.ARKS.put(30978, new Integer[] { 4292, 27182, 4397 });
		_348_ArrogantSearch.ARKS.put(30979, new Integer[] { 4293, 27183, 4399 });
		_348_ArrogantSearch.ARKS_TEXT.put(30977, new String[] { "30977-01.htm", "30977-02.htm", "30977-03.htm" });
		_348_ArrogantSearch.ARKS_TEXT.put(30978, new String[] { "That doesn't belong to you.  Don't touch it!", "30978-02.htm", "30978-03.htm" });
		_348_ArrogantSearch.ARKS_TEXT.put(30979, new String[] { "Get off my sight, you infidels!", "30979-02.htm", "30979-03.htm" });
		_348_ArrogantSearch.ARK_OWNERS.put(30144, new Integer[] { 4288, 4398, -418, 44174, -3568 });
		_348_ArrogantSearch.ARK_OWNERS.put(31001, new Integer[] { 4289, 4397, 181472, 7158, -2725 });
		_348_ArrogantSearch.ARK_OWNERS.put(30645, new Integer[] { 4290, 4399, 50693, 158674, 376 });
		_348_ArrogantSearch.ARK_OWNERS_TEXT.put(30144, new String[] { "30144-01.htm", "30144-02.htm", "30144-03.htm" });
		_348_ArrogantSearch.ARK_OWNERS_TEXT.put(31001, new String[] { "31001-01.htm", "31001-02.htm", "31001-03.htm" });
		_348_ArrogantSearch.ARK_OWNERS_TEXT.put(30645, new String[] { "30645-01.htm", "30645-02.htm", "30645-03.htm" });
		_348_ArrogantSearch.BLOODY_OWNERS.put(30760, new String[] { "3", "athebaldt_delivery", "30760-01.htm", "30760-01a.htm", "30760-01b.htm" });
		_348_ArrogantSearch.BLOODY_OWNERS.put(30832, new String[] { "1", "hardin_delivery", "30832-01.htm", "30832-01a.htm", "30832-01b.htm" });
		_348_ArrogantSearch.BLOODY_OWNERS.put(30969, new String[] { "6", "heine_delivery", "30969-01.htm", "30969-01a.htm", "30969-01b.htm" });
		_348_ArrogantSearch.DROPS.put(20657, new Integer[] { 2, 4287, 1, 10, 0 });
		_348_ArrogantSearch.DROPS.put(20658, new Integer[] { 2, 4287, 1, 10, 0 });
		_348_ArrogantSearch.DROPS.put(27184, new Integer[] { 5, 4291, 1, 100, 0 });
		_348_ArrogantSearch.DROPS.put(27182, new Integer[] { 5, 4292, 1, 100, 0 });
		_348_ArrogantSearch.DROPS.put(27183, new Integer[] { 5, 4293, 1, 100, 0 });
		_348_ArrogantSearch.DROPS.put(20830, new Integer[] { 26, 4295, 10, 25, 4294 });
		_348_ArrogantSearch.DROPS.put(20859, new Integer[] { 26, 4295, 10, 25, 4294 });
		_348_ArrogantSearch.DROPS.put(20831, new Integer[] { 26, 4295, 10, 25, 4294 });
		_348_ArrogantSearch.DROPS.put(20860, new Integer[] { 26, 4295, 10, 25, 4294 });
	}
}
