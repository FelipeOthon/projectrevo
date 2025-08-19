package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _124_MeetingTheElroki extends Quest implements ScriptFile
{
	public final int Marquez = 32113;
	public final int Mushika = 32114;
	public final int Asamah = 32115;
	public final int Karakawei = 32117;
	public final int Mantarasa = 32118;
	public final int Mushika_egg = 8778;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _124_MeetingTheElroki()
	{
		super(false);
		this.addStartNpc(32113);
		this.addTalkId(new int[] { 32114 });
		this.addTalkId(new int[] { 32115 });
		this.addTalkId(new int[] { 32117 });
		this.addTalkId(new int[] { 32118 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int cond = st.getInt("cond");
		if(event.equals("marquez_q0124_03.htm"))
			st.setState(2);
		if(event.equals("marquez_q0124_04.htm") && cond == 0)
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		if(event.equals("marquez_q0124_06.htm") && cond == 1)
		{
			st.set("cond", "2");
			st.playSound(Quest.SOUND_ITEMGET);
		}
		if(event.equals("mushika_q0124_03.htm") && cond == 2)
		{
			st.set("cond", "3");
			st.playSound(Quest.SOUND_ITEMGET);
		}
		if(event.equals("asama_q0124_06.htm") && cond == 3)
		{
			st.set("cond", "4");
			st.playSound(Quest.SOUND_ITEMGET);
		}
		if(event.equals("shaman_caracawe_q0124_03.htm") && cond == 4)
			st.set("id", "1");
		if(event.equals("shaman_caracawe_q0124_05.htm") && cond == 4)
		{
			st.set("cond", "5");
			st.playSound(Quest.SOUND_ITEMGET);
		}
		if(event.equals("egg_of_mantarasa_q0124_02.htm") && cond == 5)
		{
			st.giveItems(8778, 1L);
			st.set("cond", "6");
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
		if(npcId == 32113)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 75)
				{
					htmltext = "marquez_q0124_02.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "marquez_q0124_01.htm";
			}
			else if(cond == 1)
				htmltext = "marquez_q0124_04.htm";
			else if(cond == 2)
				htmltext = "marquez_q0124_07.htm";
		}
		else if(npcId == 32114 && cond == 2)
			htmltext = "mushika_q0124_01.htm";
		else if(npcId == 32115)
		{
			if(cond == 3)
				htmltext = "asama_q0124_03.htm";
			else if(cond == 6)
			{
				htmltext = "asama_q0124_08.htm";
				st.takeItems(8778, 1L);
				st.addExpAndSp(301922L, 30294L);
				st.giveItems(57, 100013L);
				st.set("cond", "0");
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 32117)
		{
			if(cond == 4)
			{
				htmltext = "shaman_caracawe_q0124_01.htm";
				if(st.getInt("id") == 1)
					htmltext = "shaman_caracawe_q0124_03.htm";
				else if(cond == 5)
					htmltext = "shaman_caracawe_q0124_07.htm";
			}
		}
		else if(npcId == 32118 && cond == 5)
			htmltext = "egg_of_mantarasa_q0124_01.htm";
		return htmltext;
	}
}
