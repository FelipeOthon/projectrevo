package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _013_ParcelDelivery extends Quest implements ScriptFile
{
	private static final int PACKAGE = 7263;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _013_ParcelDelivery()
	{
		super(false);
		this.addStartNpc(31274);
		this.addTalkId(new int[] { 31274 });
		this.addTalkId(new int[] { 31539 });
		addQuestItem(new int[] { 7263 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("mineral_trader_fundin_q0013_0104.htm"))
		{
			st.set("cond", "1");
			st.giveItems(7263, 1L);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("warsmith_vulcan_q0013_0201.htm"))
		{
			st.takeItems(7263, -1L);
			st.giveItems(57, 157834L, true);
			st.addExpAndSp(589092L, 58794L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 31274)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 74)
					htmltext = "mineral_trader_fundin_q0013_0101.htm";
				else
				{
					htmltext = "mineral_trader_fundin_q0013_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "mineral_trader_fundin_q0013_0105.htm";
		}
		else if(npcId == 31539 && cond == 1 && st.getQuestItemsCount(7263) == 1L)
			htmltext = "warsmith_vulcan_q0013_0101.htm";
		return htmltext;
	}
}
