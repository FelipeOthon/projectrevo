package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _018_MeetingwiththeGoldenRam extends Quest implements ScriptFile
{
	private static final int SUPPLY_BOX = 7245;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _018_MeetingwiththeGoldenRam()
	{
		super(false);
		this.addStartNpc(31314);
		this.addTalkId(new int[] { 31314 });
		this.addTalkId(new int[] { 31315 });
		this.addTalkId(new int[] { 31555 });
		addQuestItem(new int[] { 7245 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("warehouse_chief_donal_q0018_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("freighter_daisy_q0018_0201.htm"))
		{
			st.set("cond", "2");
			st.giveItems(7245, 1L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("supplier_abercrombie_q0018_0301.htm"))
		{
			st.takeItems(7245, -1L);
			st.addExpAndSp(126668L, 11731L);
			st.giveItems(57, 40000L);
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
		if(npcId == 31314)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 66)
					htmltext = "warehouse_chief_donal_q0018_0101.htm";
				else
				{
					htmltext = "warehouse_chief_donal_q0018_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "warehouse_chief_donal_q0018_0105.htm";
		}
		else if(npcId == 31315)
		{
			if(cond == 1)
				htmltext = "freighter_daisy_q0018_0101.htm";
			else if(cond == 2)
				htmltext = "freighter_daisy_q0018_0202.htm";
		}
		else if(npcId == 31555 && cond == 2 && st.getQuestItemsCount(7245) == 1L)
			htmltext = "supplier_abercrombie_q0018_0201.htm";
		return htmltext;
	}
}
