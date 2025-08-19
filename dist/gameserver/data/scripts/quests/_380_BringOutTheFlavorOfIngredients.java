package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _380_BringOutTheFlavorOfIngredients extends Quest implements ScriptFile
{
	private static final int Rollant = 30069;
	private static final int RitronsFruit = 5895;
	private static final int MoonFaceFlower = 5896;
	private static final int LeechFluids = 5897;
	private static final int Antidote = 1831;
	private static final int RitronsDessertRecipe = 5959;
	private static final int RitronJelly = 5960;
	private static final int RitronsDessertRecipeChance = 55;
	private static final int DireWolf = 20205;
	private static final int KadifWerewolf = 20206;
	private static final int GiantMistLeech = 20225;
	private static final int[][] DROPLIST_COND;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _380_BringOutTheFlavorOfIngredients()
	{
		super(false);
		this.addStartNpc(30069);
		for(int i = 0; i < _380_BringOutTheFlavorOfIngredients.DROPLIST_COND.length; ++i)
		{
			this.addKillId(new int[] { _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][2] });
			addQuestItem(new int[] { _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][4] });
		}
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("rollant_q0380_05.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("rollant_q0380_12.htm"))
		{
			st.giveItems(5959, 1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30069)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 24)
					htmltext = "rollant_q0380_02.htm";
				else
				{
					htmltext = "rollant_q0380_01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "rollant_q0380_06.htm";
			else if(cond == 2 && st.getQuestItemsCount(1831) >= 2L)
			{
				st.takeItems(1831, 2L);
				st.takeItems(5895, -1L);
				st.takeItems(5896, -1L);
				st.takeItems(5897, -1L);
				htmltext = "rollant_q0380_07.htm";
				st.set("cond", "3");
				st.setState(2);
			}
			else if(cond == 2)
				htmltext = "rollant_q0380_06.htm";
			else if(cond == 3)
			{
				htmltext = "rollant_q0380_08.htm";
				st.set("cond", "4");
			}
			else if(cond == 4)
			{
				htmltext = "rollant_q0380_09.htm";
				st.set("cond", "5");
			}
			if(cond == 5)
			{
				htmltext = "rollant_q0380_10.htm";
				st.set("cond", "6");
			}
			if(cond == 6)
			{
				st.giveItems(5960, 1L);
				if(Rnd.chance(55))
					htmltext = "rollant_q0380_11.htm";
				else
				{
					htmltext = "rollant_q0380_14.htm";
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(true);
				}
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		for(int i = 0; i < _380_BringOutTheFlavorOfIngredients.DROPLIST_COND.length; ++i)
			if(cond == _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][0] && npcId == _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][2] && (_380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(_380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][3]) > 0L))
				if(_380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][5] == 0)
					st.rollAndGive(_380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][4], _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][7], _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][6]);
				else if(st.rollAndGive(_380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][4], _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][7], _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][7], _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][5], _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][6]) && _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][1] != cond && _380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(_380_BringOutTheFlavorOfIngredients.DROPLIST_COND[i][1]));
					st.setState(2);
				}
		if(cond == 1 && st.getQuestItemsCount(5895) >= 4L && st.getQuestItemsCount(5896) >= 20L && st.getQuestItemsCount(5897) >= 10L)
		{
			st.set("cond", "2");
			st.setState(2);
		}
		return null;
	}

	static
	{
		DROPLIST_COND = new int[][] { { 1, 0, 20205, 0, 5895, 4, 10, 1 }, { 1, 0, 20206, 0, 5896, 20, 50, 1 }, { 1, 0, 20225, 0, 5897, 10, 50, 1 } };
	}
}
