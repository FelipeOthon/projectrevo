package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _378_MagnificentFeast extends Quest implements ScriptFile
{
	private static int RANSPO;
	private static int WINE_15;
	private static int WINE_30;
	private static int WINE_60;
	private static int Musical_Score__Theme_of_the_Feast;
	private static int Ritrons_Dessert_Recipe;
	private static int Jonass_Salad_Recipe;
	private static int Jonass_Sauce_Recipe;
	private static int Jonass_Steak_Recipe;
	private Map<Integer, int[]> rewards;

	public _378_MagnificentFeast()
	{
		super(false);
		rewards = new HashMap<Integer, int[]>();
		this.addStartNpc(_378_MagnificentFeast.RANSPO);
		rewards.put(9, new int[] { 847, 1, 5700 });
		rewards.put(10, new int[] { 846, 2, 0 });
		rewards.put(12, new int[] { 909, 1, 25400 });
		rewards.put(17, new int[] { 846, 2, 1200 });
		rewards.put(18, new int[] { 879, 1, 6900 });
		rewards.put(20, new int[] { 890, 2, 8500 });
		rewards.put(33, new int[] { 879, 1, 8100 });
		rewards.put(34, new int[] { 910, 1, 0 });
		rewards.put(36, new int[] { 910, 1, 0 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int _state = st.getState();
		final int cond = st.getInt("cond");
		final int score = st.getInt("score");
		if(event.equalsIgnoreCase("quest_accept") && _state == 1)
		{
			htmltext = "warehouse_chief_ranspo_q0378_03.htm";
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("378_1") && _state == 2)
		{
			if(cond == 1 && st.getQuestItemsCount(_378_MagnificentFeast.WINE_15) > 0L)
			{
				htmltext = "warehouse_chief_ranspo_q0378_05.htm";
				st.takeItems(_378_MagnificentFeast.WINE_15, 1L);
				st.set("cond", "2");
				st.set("score", String.valueOf(score + 1));
			}
			else
				htmltext = "warehouse_chief_ranspo_q0378_08.htm";
		}
		else if(event.equalsIgnoreCase("378_2") && _state == 2)
		{
			if(cond == 1 && st.getQuestItemsCount(_378_MagnificentFeast.WINE_30) > 0L)
			{
				htmltext = "warehouse_chief_ranspo_q0378_06.htm";
				st.takeItems(_378_MagnificentFeast.WINE_30, 1L);
				st.set("cond", "2");
				st.set("score", String.valueOf(score + 2));
			}
			else
				htmltext = "warehouse_chief_ranspo_q0378_08.htm";
		}
		else if(event.equalsIgnoreCase("378_3") && _state == 2)
		{
			if(cond == 1 && st.getQuestItemsCount(_378_MagnificentFeast.WINE_60) > 0L)
			{
				htmltext = "warehouse_chief_ranspo_q0378_07.htm";
				st.takeItems(_378_MagnificentFeast.WINE_60, 1L);
				st.set("cond", "2");
				st.set("score", String.valueOf(score + 4));
			}
			else
				htmltext = "warehouse_chief_ranspo_q0378_08.htm";
		}
		else if(event.equalsIgnoreCase("378_5") && _state == 2)
		{
			if(cond == 2 && st.getQuestItemsCount(_378_MagnificentFeast.Musical_Score__Theme_of_the_Feast) > 0L)
			{
				htmltext = "warehouse_chief_ranspo_q0378_12.htm";
				st.takeItems(_378_MagnificentFeast.Musical_Score__Theme_of_the_Feast, 1L);
				st.set("cond", "3");
			}
			else
				htmltext = "warehouse_chief_ranspo_q0378_10.htm";
		}
		else if(event.equalsIgnoreCase("378_6") && _state == 2)
		{
			if(cond == 3 && st.getQuestItemsCount(_378_MagnificentFeast.Jonass_Salad_Recipe) > 0L)
			{
				htmltext = "warehouse_chief_ranspo_q0378_14.htm";
				st.takeItems(_378_MagnificentFeast.Jonass_Salad_Recipe, 1L);
				st.set("cond", "4");
				st.set("score", String.valueOf(score + 8));
			}
			else
				htmltext = "warehouse_chief_ranspo_q0378_17.htm";
		}
		else if(event.equalsIgnoreCase("378_7") && _state == 2)
		{
			if(cond == 3 && st.getQuestItemsCount(_378_MagnificentFeast.Jonass_Sauce_Recipe) > 0L)
			{
				htmltext = "warehouse_chief_ranspo_q0378_15.htm";
				st.takeItems(_378_MagnificentFeast.Jonass_Sauce_Recipe, 1L);
				st.set("cond", "4");
				st.set("score", String.valueOf(score + 16));
			}
			else
				htmltext = "warehouse_chief_ranspo_q0378_17.htm";
		}
		else if(event.equalsIgnoreCase("378_8") && _state == 2)
			if(cond == 3 && st.getQuestItemsCount(_378_MagnificentFeast.Jonass_Steak_Recipe) > 0L)
			{
				htmltext = "warehouse_chief_ranspo_q0378_16.htm";
				st.takeItems(_378_MagnificentFeast.Jonass_Steak_Recipe, 1L);
				st.set("cond", "4");
				st.set("score", String.valueOf(score + 32));
			}
			else
				htmltext = "warehouse_chief_ranspo_q0378_17.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _378_MagnificentFeast.RANSPO)
			return htmltext;
		final int _state = st.getState();
		final int cond = st.getInt("cond");
		if(_state == 1)
		{
			if(st.getPlayer().getLevel() < 20)
			{
				htmltext = "warehouse_chief_ranspo_q0378_01.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "warehouse_chief_ranspo_q0378_02.htm";
				st.set("cond", "0");
			}
		}
		else if(cond == 1 && _state == 2)
			htmltext = "warehouse_chief_ranspo_q0378_04.htm";
		else if(cond == 2 && _state == 2)
			htmltext = st.getQuestItemsCount(_378_MagnificentFeast.Musical_Score__Theme_of_the_Feast) > 0L ? "warehouse_chief_ranspo_q0378_11.htm" : "warehouse_chief_ranspo_q0378_10.htm";
		else if(cond == 3 && _state == 2)
			htmltext = "warehouse_chief_ranspo_q0378_13.htm";
		else if(cond == 4 && _state == 2)
		{
			final int[] reward = rewards.get(st.getInt("score"));
			if(st.getQuestItemsCount(_378_MagnificentFeast.Ritrons_Dessert_Recipe) > 0L && reward != null)
			{
				htmltext = "warehouse_chief_ranspo_q0378_20.htm";
				st.takeItems(_378_MagnificentFeast.Ritrons_Dessert_Recipe, 1L);
				st.giveItems(reward[0], reward[1]);
				if(reward[2] > 0)
					st.giveItems(57, reward[2]);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "warehouse_chief_ranspo_q0378_19.htm";
		}
		return htmltext;
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

	static
	{
		_378_MagnificentFeast.RANSPO = 30594;
		_378_MagnificentFeast.WINE_15 = 5956;
		_378_MagnificentFeast.WINE_30 = 5957;
		_378_MagnificentFeast.WINE_60 = 5958;
		_378_MagnificentFeast.Musical_Score__Theme_of_the_Feast = 4421;
		_378_MagnificentFeast.Ritrons_Dessert_Recipe = 5959;
		_378_MagnificentFeast.Jonass_Salad_Recipe = 1455;
		_378_MagnificentFeast.Jonass_Sauce_Recipe = 1456;
		_378_MagnificentFeast.Jonass_Steak_Recipe = 1457;
	}
}
