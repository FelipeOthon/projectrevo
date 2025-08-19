package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _116_BeyondtheHillsofWinter extends Quest implements ScriptFile
{
	public final int FILAUR = 30535;
	public final int OBI = 32052;
	public final int Supplying_Goods_for_Railroad_Worker = 8098;
	public final int Bandage = 1833;
	public final int Energy_Stone = 5589;
	public final int Thief_Key = 1661;
	public final int SSD = 1463;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _116_BeyondtheHillsofWinter()
	{
		super(false);
		this.addStartNpc(30535);
		this.addTalkId(new int[] { 32052 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("elder_filaur_q0116_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("elder_filaur_q0116_0201.htm"))
		{
			if(st.getQuestItemsCount(1833) >= 20L && st.getQuestItemsCount(5589) >= 5L && st.getQuestItemsCount(1661) >= 10L)
			{
				st.takeItems(1833, 20L);
				st.takeItems(5589, 5L);
				st.takeItems(1661, 10L);
				st.giveItems(8098, 1L);
				st.set("cond", "2");
				st.setState(2);
			}
			else
				htmltext = "elder_filaur_q0116_0104.htm";
		}
		else if(event.equalsIgnoreCase("materials"))
		{
			htmltext = "railman_obi_q0116_0302.htm";
			st.takeItems(8098, 1L);
			st.giveItems(1463, 1740L);
			st.addExpAndSp(82792L, 4981L);
			st.exitCurrentQuest(false);
		}
		else if(event.equalsIgnoreCase("adena"))
		{
			htmltext = "railman_obi_q0116_0302.htm";
			st.takeItems(8098, 1L);
			st.giveItems(57, 17387L);
			st.addExpAndSp(82792L, 4981L);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		int cond = 0;
		if(id != 1)
			cond = st.getInt("cond");
		if(npcId == 30535)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 30)
				{
					htmltext = "elder_filaur_q0116_0103.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "elder_filaur_q0116_0101.htm";
			}
			else if(cond == 1)
				htmltext = "elder_filaur_q0116_0105.htm";
			else if(cond == 2)
				htmltext = "elder_filaur_q0116_0201.htm";
		}
		else if(npcId == 32052 && cond == 2 && st.getQuestItemsCount(8098) > 0L)
			htmltext = "railman_obi_q0116_0201.htm";
		return htmltext;
	}
}
