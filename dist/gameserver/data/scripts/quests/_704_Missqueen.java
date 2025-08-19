package quests;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _704_Missqueen extends Quest implements ScriptFile
{
	public final int m_q = 31760;
	public final int item_1 = 7832;
	public final int item_2 = 7833;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _704_Missqueen()
	{
		super("Miss Queen", false);
		this.addStartNpc(31760);
		this.addTalkId(new int[] { 31760 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = "noquest";
		if(event.equals("31760-02.htm"))
		{
			if(st.getInt("cond") == 0 && st.getPlayer().getLevel() <= 20 && st.getPlayer().getLevel() >= 6 && st.getPlayer().getPkKills() == 0)
			{
				st.giveItems(7832, 1L);
				st.set("cond", "1");
				htmltext = "c_1.htm";
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
				htmltext = "fail-01.htm";
		}
		else if(event.equals("31760-03.htm"))
			if(st.getInt("m_scond") == 0 && st.getPlayer().getLevel() <= 25 && st.getPlayer().getLevel() >= 20 && st.getPlayer().getPkKills() == 0)
			{
				st.giveItems(7833, 1L);
				st.set("m_scond", "1");
				htmltext = "c_2.htm";
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
				htmltext = "fail-02.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		if(npcId == 31760)
			htmltext = "31760-01.htm";
		return htmltext;
	}

	@Override
	public boolean isVisible(Player player)
	{
		return false;
	}
}
