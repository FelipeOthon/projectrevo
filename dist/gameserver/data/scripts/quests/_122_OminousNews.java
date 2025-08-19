package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _122_OminousNews extends Quest implements ScriptFile
{
	int MOIRA;
	int KARUDA;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _122_OminousNews()
	{
		super(false);
		MOIRA = 31979;
		KARUDA = 32017;
		this.addStartNpc(MOIRA);
		this.addTalkId(new int[] { KARUDA });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int cond = st.getInt("cond");
		String htmltext = event;
		if(htmltext.equalsIgnoreCase("seer_moirase_q0122_0104.htm") && cond == 0)
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(htmltext.equalsIgnoreCase("karuda_q0122_0201.htm"))
			if(cond == 1)
			{
				st.giveItems(57, 8923L);
				st.addExpAndSp(45151L, 2310L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
			else
				htmltext = "noquest";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == MOIRA)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 20)
					htmltext = "seer_moirase_q0122_0101.htm";
				else
				{
					htmltext = "seer_moirase_q0122_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
				htmltext = "seer_moirase_q0122_0104.htm";
		}
		else if(npcId == KARUDA && cond == 1)
			htmltext = "karuda_q0122_0101.htm";
		return htmltext;
	}
}
