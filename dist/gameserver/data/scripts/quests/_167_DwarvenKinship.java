package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _167_DwarvenKinship extends Quest implements ScriptFile
{
	private static final int Carlon = 30350;
	private static final int Haprock = 30255;
	private static final int Norman = 30210;
	private static final int CarlonsLetter = 1076;
	private static final int NormansLetter = 1106;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _167_DwarvenKinship()
	{
		super(false);
		this.addStartNpc(30350);
		this.addTalkId(new int[] { 30255 });
		this.addTalkId(new int[] { 30210 });
		addQuestItem(new int[] { 1076, 1106 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30350-04.htm"))
		{
			st.giveItems(1076, 1L);
			st.playSound(Quest.SOUND_ACCEPT);
			st.set("cond", "1");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30255-03.htm"))
		{
			st.takeItems(1076, -1L);
			st.giveItems(57, 2000L);
			st.giveItems(1106, 1L);
			st.set("cond", "2");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30255-04.htm"))
		{
			st.takeItems(1076, -1L);
			st.giveItems(57, 2000L);
			st.playSound(Quest.SOUND_GIVEUP);
			st.exitCurrentQuest(false);
		}
		else if(event.equalsIgnoreCase("30210-02.htm"))
		{
			st.takeItems(1106, -1L);
			st.giveItems(57, 20000L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30350)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 15)
					htmltext = "30350-03.htm";
				else
				{
					htmltext = "30350-02.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond > 0)
				htmltext = "30350-05.htm";
		}
		else if(npcId == 30255)
		{
			if(cond == 1)
				htmltext = "30255-01.htm";
			else if(cond > 1)
				htmltext = "30255-05.htm";
		}
		else if(npcId == 30210 && cond == 2)
			htmltext = "30210-01.htm";
		return htmltext;
	}
}
