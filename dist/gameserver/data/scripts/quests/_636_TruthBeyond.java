package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _636_TruthBeyond extends Quest implements ScriptFile
{
	public final int ELIYAH = 31329;
	public final int FLAURON = 32010;
	public final int VISITORSMARK = 8064;
	public final int FADED_MARK = 8065;
	public final int PERMANENT_MARK = 8067;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _636_TruthBeyond()
	{
		super(false);
		this.addStartNpc(31329);
		this.addTalkId(new int[] { 32010 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("priest_eliyah_q0636_05.htm"))
		{
			st.setCond(1);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("falsepriest_flauron_q0636_02.htm"))
		{
			st.setCond(2);
			st.playSound(Quest.SOUND_FINISH);
			st.giveItems(8064, 1L);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getCond();
		if(npcId == 31329 && cond == 0)
		{
			if(st.getQuestItemsCount(new int[] { 8064, 8065, 8067 }) == 0L)
			{
				if(st.getPlayer().getLevel() > 72)
					htmltext = "priest_eliyah_q0636_01.htm";
				else
				{
					htmltext = "priest_eliyah_q0636_03.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
				htmltext = "priest_eliyah_q0636_06.htm";
		}
		else if(npcId == 32010)
			if(cond == 1 || st.getQuestItemsCount(new int[] { 8064, 8065, 8067 }) == 0L)
				htmltext = "falsepriest_flauron_q0636_01.htm";
			else
				htmltext = "falsepriest_flauron_q0636_03.htm";
		return htmltext;
	}
}
