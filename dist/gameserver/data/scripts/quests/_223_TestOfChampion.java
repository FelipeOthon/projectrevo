package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _223_TestOfChampion extends Quest implements ScriptFile
{
	private static final int COND1 = 1;
	private static final int COND2 = 2;
	private static final int COND3 = 3;
	private static final int COND4 = 4;
	private static final int COND5 = 5;
	private static final int COND6 = 6;
	private static final int COND7 = 7;
	private static final int COND8 = 8;
	private static final int COND9 = 9;
	private static final int COND10 = 10;
	private static final int COND11 = 11;
	private static final int COND12 = 12;
	private static final int COND13 = 13;
	private static final int COND14 = 14;
	private static final int RewardExp = 635371;
	private static final int RewardSP = 43600;
	private static final int RewardAdena = 114882;
	private static final int MARK_OF_CHAMPION = 3276;
	private static final int ASCALONS_LETTER1 = 3277;
	private static final int MASONS_LETTER = 3278;
	private static final int IRON_ROSE_RING = 3279;
	private static final int ASCALONS_LETTER2 = 3280;
	private static final int WHITE_ROSE_INSIGNIA = 3281;
	private static final int GROOTS_LETTER = 3282;
	private static final int ASCALONS_LETTER3 = 3283;
	private static final int MOUENS_ORDER1 = 3284;
	private static final int MOUENS_ORDER2 = 3285;
	private static final int MOUENS_LETTER = 3286;
	private static final int HARPYS_EGG = 3287;
	private static final int MEDUSA_VENOM = 3288;
	private static final int WINDSUS_BILE = 3289;
	private static final int BLOODY_AXE_HEAD = 3290;
	private static final int ROAD_RATMAN_HEAD = 3291;
	private static final int LETO_LIZARDMAN_FANG = 3292;
	private static final int Ascalon = 30624;
	private static final int Groot = 30093;
	private static final int Mouen = 30196;
	private static final int Mason = 30625;
	private static final int Harpy = 20145;
	private static final int HarpyMatriarch = 27088;
	private static final int Medusa = 20158;
	private static final int Windsus = 20553;
	private static final int RoadScavenger = 20551;
	private static final int LetoLizardman = 20577;
	private static final int LetoLizardmanArcher = 20578;
	private static final int LetoLizardmanSoldier = 20579;
	private static final int LetoLizardmanWarrior = 20580;
	private static final int LetoLizardmanShaman = 20581;
	private static final int LetoLizardmanOverlord = 20582;
	private static final int BloodyAxeElite = 20780;
	private static final int[][] DROPLIST;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _223_TestOfChampion()
	{
		super(false);
		this.addStartNpc(30624);
		this.addTalkId(new int[] { 30093 });
		this.addTalkId(new int[] { 30196 });
		this.addTalkId(new int[] { 30625 });
		this.addKillId(new int[] { 20145, 20158, 27088, 20551, 20553, 20577, 20578, 20579, 20580, 20581, 20582, 20780 });
		addQuestItem(new int[] { 3278, 3288, 3289, 3281, 3287, 3282, 3286, 3277, 3279, 3290, 3280, 3283, 3284, 3291, 3285, 3292 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("1"))
		{
			htmltext = "30624-06.htm";
			st.setCond(1);
			st.setState(2);
			if(!st.getPlayer().getVarBoolean("dd3"))
			{
				st.giveItems(7562, 72L);
				st.getPlayer().setVar("dd3", "1");
			}
			st.playSound(Quest.SOUND_ACCEPT);
			st.giveItems(3277, 1L);
		}
		else if(event.equals("30624_1"))
			htmltext = "30624-05.htm";
		else if(event.equals("30624_2"))
		{
			htmltext = "30624-10.htm";
			st.setCond(5);
			st.takeItems(3278, -1L);
			st.giveItems(3280, 1L);
		}
		else if(event.equals("30624_3"))
		{
			htmltext = "30624-14.htm";
			st.setCond(9);
			st.takeItems(3282, -1L);
			st.giveItems(3283, 1L);
		}
		else if(event.equals("30625_1"))
			htmltext = "30625-02.htm";
		else if(event.equals("30625_2"))
		{
			htmltext = "30625-03.htm";
			st.setCond(2);
			st.takeItems(3277, -1L);
			st.giveItems(3279, 1L);
		}
		else if(event.equals("30093_1"))
		{
			htmltext = "30093-02.htm";
			st.setCond(6);
			st.takeItems(3280, -1L);
			st.giveItems(3281, 1L);
		}
		else if(event.equals("30196_1"))
			htmltext = "30196-02.htm";
		else if(event.equals("30196_2"))
		{
			htmltext = "30196-03.htm";
			st.setCond(10);
			st.takeItems(3283, -1L);
			st.giveItems(3284, 1L);
		}
		else if(event.equals("30196_3"))
		{
			htmltext = "30196-06.htm";
			st.setCond(12);
			st.takeItems(3284, -1L);
			st.takeItems(3291, -1L);
			st.giveItems(3285, 1L);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(3276) > 0L)
		{
			st.exitCurrentQuest(true);
			return "completed";
		}
		final int npcId = npc.getNpcId();
		final int cond = st.getCond();
		String htmltext = "noquest";
		if(npcId == 30624)
		{
			if(cond == 0)
			{
				final ClassId class_id = st.getPlayer().getClassId();
				if(class_id != ClassId.Warrior && class_id != ClassId.OrcRaider)
				{
					st.exitCurrentQuest(true);
					return "30624-01.htm";
				}
				if(st.getPlayer().getLevel() < 39)
				{
					st.exitCurrentQuest(true);
					return "30624-02.htm";
				}
				return class_id == ClassId.Warrior ? "30624-03.htm" : "30624-04.htm";
			}
			else if(cond == 1)
				htmltext = "30624-07.htm";
			else if(cond == 2 || cond == 3)
				htmltext = "30624-08.htm";
			else if(cond == 4)
				htmltext = "30624-09.htm";
			else if(cond == 5)
				htmltext = "30624-11.htm";
			else if(cond == 6 || cond == 7)
				htmltext = "30624-12.htm";
			else if(cond == 8)
				htmltext = "30624-13.htm";
			else if(cond == 9)
				htmltext = "30624-15.htm";
			else if(cond > 9 && cond < 14)
				htmltext = "30624-16.htm";
			else if(cond == 14)
			{
				htmltext = "30624-17.htm";
				st.takeItems(3286, -1L);
				st.giveItems(3276, 1L);
				if(!st.getPlayer().getVarBoolean("prof2.3"))
				{
					st.addExpAndSp(635371L, 43600L, true);
					st.giveItems(57, 114882L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
					st.getPlayer().setVar("prof2.3", "1");
				}
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else
		{
			if(cond == 0)
				return "noquest";
			if(npcId == 30625)
			{
				if(cond == 1)
					htmltext = "30625-01.htm";
				else if(cond == 2)
					htmltext = "30625-04.htm";
				else if(cond == 3)
				{
					htmltext = "30625-05.htm";
					st.takeItems(3290, -1L);
					st.takeItems(3279, -1L);
					st.giveItems(3278, 1L);
					st.setCond(4);
				}
				else if(cond == 4)
					htmltext = "30625-06.htm";
				else
					htmltext = "30625-07.htm";
			}
			else if(npcId == 30093)
			{
				if(cond == 5)
					htmltext = "30093-01.htm";
				else if(cond == 6)
					htmltext = "30093-03.htm";
				else if(cond == 7)
				{
					htmltext = "30093-04.htm";
					st.takeItems(3281, -1L);
					st.takeItems(3287, -1L);
					st.takeItems(3288, -1L);
					st.takeItems(3289, -1L);
					st.giveItems(3282, 1L);
					st.setCond(8);
				}
				else if(cond == 8)
					htmltext = "30093-05.htm";
				else if(cond > 8)
					htmltext = "30093-06.htm";
			}
			else if(npcId == 30196)
				if(cond == 9)
					htmltext = "30196-01.htm";
				else if(cond == 10)
					htmltext = "30196-04.htm";
				else if(cond == 11)
					htmltext = "30196-05.htm";
				else if(cond == 12)
					htmltext = "30196-07.htm";
				else if(cond == 13)
				{
					htmltext = "30196-08.htm";
					st.takeItems(3285, -1L);
					st.takeItems(3292, -1L);
					st.giveItems(3286, 1L);
					st.setCond(14);
				}
				else if(cond == 13)
					htmltext = "30196-09.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getCond();
		if(cond == 0)
			return null;
		final int npcId = npc.getNpcId();
		for(final int[] drop : _223_TestOfChampion.DROPLIST)
			if(drop[2] == npcId && drop[0] == cond)
			{
				st.rollAndGive(drop[3], 1, 1, drop[5], drop[4], true);
				for(final int[] drop2 : _223_TestOfChampion.DROPLIST)
					if(drop2[0] == cond && st.getQuestItemsCount(drop2[3]) < drop2[5])
						return null;
				st.setCond(cond + 1);
				return null;
			}
		return null;
	}

	static
	{
		DROPLIST = new int[][] {
				{ 2, 3, 20780, 3290, 20, 10 },
				{ 6, 7, 20145, 3287, 100, 30 },
				{ 6, 7, 27088, 3287, 100, 30 },
				{ 6, 7, 20158, 3288, 50, 30 },
				{ 6, 7, 20553, 3289, 50, 30 },
				{ 10, 11, 20551, 3291, 20, 10 },
				{ 12, 13, 20577, 3292, 20, 10 },
				{ 12, 13, 20578, 3292, 22, 10 },
				{ 12, 13, 20579, 3292, 24, 10 },
				{ 12, 13, 20580, 3292, 26, 10 },
				{ 12, 13, 20581, 3292, 28, 10 },
				{ 12, 13, 20582, 3292, 30, 10 } };
	}
}
