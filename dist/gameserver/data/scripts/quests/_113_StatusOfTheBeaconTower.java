package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _113_StatusOfTheBeaconTower extends Quest implements ScriptFile
{
	private static final int MOIRA = 31979;
	private static final int TORRANT = 32016;
	private static final int BOX = 8086;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _113_StatusOfTheBeaconTower()
	{
		super(false);
		this.addStartNpc(31979);
		this.addTalkId(new int[] { 32016 });
		addQuestItem(new int[] { 8086 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("seer_moirase_q0113_0104.htm"))
		{
			st.set("cond", "1");
			st.giveItems(8086, 1L);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("torant_q0113_0201.htm"))
		{
			st.giveItems(57, 154800L);
			st.addExpAndSp(619300L, 44200L);
			st.takeItems(8086, 1L);
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
		final int id = st.getState();
		final int cond = st.getInt("cond");
		if(id == 3)
			htmltext = "completed";
		else if(npcId == 31979)
		{
			if(id == 1)
			{
				if(st.getPlayer().getLevel() >= 80)
					htmltext = "seer_moirase_q0113_0101.htm";
				else
				{
					htmltext = "seer_moirase_q0113_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "seer_moirase_q0113_0105.htm";
		}
		else if(npcId == 32016 && st.getQuestItemsCount(8086) == 1L)
			htmltext = "torant_q0113_0101.htm";
		return htmltext;
	}
}
