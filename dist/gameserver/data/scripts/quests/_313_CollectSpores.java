package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _313_CollectSpores extends Quest implements ScriptFile
{
	public final int Herbiel = 30150;
	public final int SporeFungus = 20509;
	public final int SporeSac = 1118;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _313_CollectSpores()
	{
		super(false);
		this.addStartNpc(30150);
		this.addTalkId(new int[] { 30150 });
		this.addKillId(new int[] { 20509 });
		addQuestItem(new int[] { 1118 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("green_q0313_05.htm"))
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
			if(st.getPlayer().getLevel() >= 8)
				htmltext = "green_q0313_03.htm";
			else
			{
				htmltext = "green_q0313_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 1)
			htmltext = "green_q0313_06.htm";
		else if(cond == 2)
			if(st.getQuestItemsCount(1118) < 10L)
			{
				st.set("cond", "1");
				htmltext = "green_q0313_06.htm";
			}
			else
			{
				st.takeItems(1118, -1L);
				st.giveItems(57, 3500L, true);
				st.playSound(Quest.SOUND_FINISH);
				htmltext = "green_q0313_07.htm";
				st.exitCurrentQuest(true);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 1 && npcId == 20509 && Rnd.chance(70))
		{
			st.giveItems(1118, 1L);
			if(st.getQuestItemsCount(1118) < 10L)
				st.playSound(Quest.SOUND_ITEMGET);
			else
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
				st.setState(2);
			}
		}
		return null;
	}
}
