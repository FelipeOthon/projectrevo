package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _350_EnhanceYourWeapon extends Quest implements ScriptFile
{
	private static final int RED_SOUL_CRYSTAL0_ID = 4629;
	private static final int GREEN_SOUL_CRYSTAL0_ID = 4640;
	private static final int BLUE_SOUL_CRYSTAL0_ID = 4651;
	private static final int Jurek = 30115;
	private static final int Gideon = 30194;
	private static final int Winonin = 30856;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _350_EnhanceYourWeapon()
	{
		super(false);
		this.addStartNpc(30115);
		this.addStartNpc(30194);
		this.addStartNpc(30856);
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30115-04.htm") || event.equalsIgnoreCase("30194-04.htm") || event.equalsIgnoreCase("30856-04.htm"))
		{
			st.setCond(1);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		if(event.equalsIgnoreCase("30115-09.htm") || event.equalsIgnoreCase("30194-09.htm") || event.equalsIgnoreCase("30856-09.htm"))
			st.giveItems(4629, 1L);
		if(event.equalsIgnoreCase("30115-10.htm") || event.equalsIgnoreCase("30194-10.htm") || event.equalsIgnoreCase("30856-10.htm"))
			st.giveItems(4640, 1L);
		if(event.equalsIgnoreCase("30115-11.htm") || event.equalsIgnoreCase("30194-11.htm") || event.equalsIgnoreCase("30856-11.htm"))
			st.giveItems(4651, 1L);
		if(event.equalsIgnoreCase("exit.htm"))
			st.exitCurrentQuest(true);
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final String npcId = str(npc.getNpcId());
		String htmltext = "noquest";
		final int id = st.getState();
		if(st.getQuestItemsCount(4629) == 0L && st.getQuestItemsCount(4640) == 0L && st.getQuestItemsCount(4651) == 0L)
		{
			if(id == 1)
				htmltext = npcId + "-01.htm";
			else
				htmltext = npcId + "-21.htm";
		}
		else
		{
			if(id == 1)
			{
				st.setCond(1);
				st.setState(2);
			}
			htmltext = npcId + "-03.htm";
		}
		return htmltext;
	}
}
