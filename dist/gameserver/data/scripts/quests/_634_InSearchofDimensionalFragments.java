package quests;

import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _634_InSearchofDimensionalFragments extends Quest implements ScriptFile
{
	int DIMENSION_FRAGMENT_ID;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _634_InSearchofDimensionalFragments()
	{
		super(true);
		DIMENSION_FRAGMENT_ID = 7079;
		for(int npcId = 31494; npcId < 31508; ++npcId)
		{
			this.addTalkId(new int[] { npcId });
			this.addStartNpc(npcId);
		}
		for(int mobs = 21208; mobs < 21256; ++mobs)
			this.addKillId(new int[] { mobs });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "dimension_keeper_1_q0634_03.htm";
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.set("cond", "1");
		}
		else if(event.equalsIgnoreCase("634_2"))
		{
			htmltext = "dimension_keeper_1_q0634_06.htm";
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int id = st.getState();
		if(id == 1)
		{
			if(st.getPlayer().getLevel() > 20)
				htmltext = "dimension_keeper_1_q0634_01.htm";
			else
			{
				htmltext = "dimension_keeper_1_q0634_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(id == 2)
			htmltext = "dimension_keeper_1_q0634_04.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		st.rollAndGive(DIMENSION_FRAGMENT_ID, 1, 60.0 * Experience.penaltyModifier(st.calculateLevelDiffForDrop(npc.getLevel(), st.getPlayer().getLevel()), 9.0) * npc.getTemplate().rateHp / 4.0);
		return null;
	}
}
