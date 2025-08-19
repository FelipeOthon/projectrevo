package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _320_BonesTellFuture extends Quest implements ScriptFile
{
	public final int BONE_FRAGMENT = 809;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _320_BonesTellFuture()
	{
		super(false);
		this.addStartNpc(30359);
		this.addTalkId(new int[] { 30359 });
		this.addKillId(new int[] { 20517 });
		this.addKillId(new int[] { 20518 });
		addQuestItem(new int[] { 809 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("tetrarch_kaitar_q0320_04.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getRace() != Race.darkelf)
			{
				htmltext = "tetrarch_kaitar_q0320_00.htm";
				st.exitCurrentQuest(true);
			}
			else if(st.getPlayer().getLevel() >= 10)
				htmltext = "tetrarch_kaitar_q0320_03.htm";
			else
			{
				htmltext = "tetrarch_kaitar_q0320_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(st.getQuestItemsCount(809) < 10L)
			htmltext = "tetrarch_kaitar_q0320_05.htm";
		else
		{
			htmltext = "tetrarch_kaitar_q0320_06.htm";
			st.giveItems(57, 8470L, true);
			st.takeItems(809, -1L);
			st.exitCurrentQuest(true);
			st.unset("cond");
			st.playSound(Quest.SOUND_FINISH);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		st.rollAndGive(809, 1, 1, 10, 10.0);
		if(st.getQuestItemsCount(809) >= 10L)
			st.set("cond", "2");
		st.setState(2);
		return null;
	}
}
