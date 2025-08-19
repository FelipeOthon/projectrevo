package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _617_GatherTheFlames extends Quest implements ScriptFile
{
	private static final int VULCAN = 31539;
	private static final int HILDA = 31271;
	private static final int TORCH = 7264;
	private static final int[][] DROPLIST;
	public static final int[] Recipes;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _617_GatherTheFlames()
	{
		super(true);
		this.addStartNpc(31539);
		this.addStartNpc(31271);
		for(final int[] element : _617_GatherTheFlames.DROPLIST)
			this.addKillId(new int[] { element[0] });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("31539-03.htm"))
		{
			if(st.getPlayer().getLevel() < 74)
				return "31539-02.htm";
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.set("cond", "1");
		}
		else if(event.equalsIgnoreCase("31271-03.htm"))
		{
			if(st.getPlayer().getLevel() < 74)
				return "31271-01.htm";
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.set("cond", "1");
		}
		else if(event.equalsIgnoreCase("31539-08.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.takeItems(7264, -1L);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("31539-07.htm"))
		{
			if(st.getQuestItemsCount(7264) < 1000L)
				return "31539-05.htm";
			st.takeItems(7264, 1000L);
			st.giveItems(_617_GatherTheFlames.Recipes[Rnd.get(_617_GatherTheFlames.Recipes.length)] + (Config.ALT_100_RECIPES_S ? 1 : 0), 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 31539)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 74)
				{
					htmltext = "31539-02.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "31539-01.htm";
			}
			else
				htmltext = st.getQuestItemsCount(7264) < 1000L ? "31539-05.htm" : "31539-04.htm";
		}
		else if(npcId == 31271)
			if(cond < 1)
				htmltext = st.getPlayer().getLevel() < 74 ? "31271-01.htm" : "31271-02.htm";
			else
				htmltext = "31271-04.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		for(final int[] element : _617_GatherTheFlames.DROPLIST)
			if(npc.getNpcId() == element[0])
			{
				st.rollAndGive(7264, 1, element[1]);
				return null;
			}
		return null;
	}

	static
	{
		DROPLIST = new int[][] {
				{ 21376, 48 },
				{ 21377, 48 },
				{ 21378, 48 },
				{ 21652, 48 },
				{ 21380, 48 },
				{ 21381, 51 },
				{ 21653, 51 },
				{ 21383, 51 },
				{ 21394, 51 },
				{ 21385, 51 },
				{ 21386, 51 },
				{ 21388, 53 },
				{ 21655, 53 },
				{ 21387, 53 },
				{ 21390, 56 },
				{ 21656, 56 },
				{ 21395, 56 },
				{ 21389, 56 },
				{ 21391, 56 },
				{ 21392, 56 },
				{ 21393, 58 },
				{ 21657, 58 },
				{ 21382, 60 },
				{ 21379, 60 },
				{ 21654, 64 },
				{ 21384, 64 } };
		Recipes = new int[] { 6881, 6883, 6885, 6887, 7580, 6891, 6893, 6895, 6897, 6899 };
	}
}
