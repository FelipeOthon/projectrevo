package quests;

import events.FirstNobless.FirstNobless;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _247_PossessorOfaPreciousSoul4 extends Quest implements ScriptFile
{
	private static int CARADINE;
	private static int LADY_OF_LAKE;
	private static int CARADINE_LETTER_LAST;
	private static int NOBLESS_TIARA;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _247_PossessorOfaPreciousSoul4()
	{
		super(false);
		this.addStartNpc(_247_PossessorOfaPreciousSoul4.CARADINE);
		this.addTalkId(new int[] { _247_PossessorOfaPreciousSoul4.LADY_OF_LAKE });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int cond = st.getInt("cond");
		if(cond == 0 && event.equals("caradine_q0247_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(cond == 1)
		{
			if(event.equals("caradine_q0247_04.htm"))
				return htmltext;
			if(event.equals("caradine_q0247_05.htm"))
			{
				st.set("cond", "2");
				st.takeItems(_247_PossessorOfaPreciousSoul4.CARADINE_LETTER_LAST, 1L);
				st.getPlayer().teleToLocation(143230, 44030, -3030);
				return htmltext;
			}
		}
		else if(cond == 2)
		{
			if(event.equals("caradine_q0247_06.htm"))
				return htmltext;
			if(event.equals("caradine_q0247_05.htm"))
			{
				st.getPlayer().teleToLocation(143230, 44030, -3030);
				return htmltext;
			}
			if(event.equals("lady_of_the_lake_q0247_02.htm"))
				return htmltext;
			if(event.equals("lady_of_the_lake_q0247_03.htm"))
				return htmltext;
			if(event.equals("lady_of_the_lake_q0247_04.htm"))
				return htmltext;
			if(event.equals("lady_of_the_lake_q0247_05.htm"))
				if(st.getPlayer().getLevel() >= 75)
				{
					st.giveItems(_247_PossessorOfaPreciousSoul4.NOBLESS_TIARA, 1L);
					st.addExpAndSp(93836L, 0L);
					st.playSound(Quest.SOUND_FINISH);
					st.unset("cond");
					st.exitCurrentQuest(false);
					st.getPlayer().setNoble();
					FirstNobless.reward(st.getPlayer());
				}
				else
					htmltext = "lady_of_the_lake_q0247_06.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(!st.getPlayer().isSubClassActive())
			return "Subclass only!";
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int cond = st.getInt("cond");
		if(npcId == _247_PossessorOfaPreciousSoul4.CARADINE)
		{
			if(id == 1 && st.getQuestItemsCount(_247_PossessorOfaPreciousSoul4.CARADINE_LETTER_LAST) == 1L)
			{
				if(st.getPlayer().getLevel() < 75)
				{
					htmltext = "caradine_q0247_02.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "caradine_q0247_01.htm";
			}
			else if(cond == 1)
				htmltext = "caradine_q0247_03.htm";
			else if(cond == 2)
				htmltext = "caradine_q0247_06.htm";
		}
		else if(npcId == _247_PossessorOfaPreciousSoul4.LADY_OF_LAKE && cond == 2)
			if(st.getPlayer().getLevel() >= 75)
				htmltext = "lady_of_the_lake_q0247_01.htm";
			else
				htmltext = "lady_of_the_lake_q0247_06.htm";
		return htmltext;
	}

	static
	{
		_247_PossessorOfaPreciousSoul4.CARADINE = 31740;
		_247_PossessorOfaPreciousSoul4.LADY_OF_LAKE = 31745;
		_247_PossessorOfaPreciousSoul4.CARADINE_LETTER_LAST = 7679;
		_247_PossessorOfaPreciousSoul4.NOBLESS_TIARA = 7694;
	}
}
