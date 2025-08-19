package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _431_WeddingMarch extends Quest implements ScriptFile
{
	private static int MELODY_MAESTRO_KANTABILON;
	private static int SILVER_CRYSTAL;
	private static int WEDDING_ECHO_CRYSTAL;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _431_WeddingMarch()
	{
		super(false);
		this.addStartNpc(_431_WeddingMarch.MELODY_MAESTRO_KANTABILON);
		this.addKillId(new int[] { 20786 });
		this.addKillId(new int[] { 20787 });
		addQuestItem(new int[] { _431_WeddingMarch.SILVER_CRYSTAL });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "muzyk_q0431_0104.htm";
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("431_3"))
			if(st.getQuestItemsCount(_431_WeddingMarch.SILVER_CRYSTAL) == 50L)
			{
				htmltext = "muzyk_q0431_0201.htm";
				st.takeItems(_431_WeddingMarch.SILVER_CRYSTAL, -1L);
				st.giveItems(_431_WeddingMarch.WEDDING_ECHO_CRYSTAL, 25L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "muzyk_q0431_0202.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int condition = st.getInt("cond");
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		if(npcId == _431_WeddingMarch.MELODY_MAESTRO_KANTABILON)
			if(id != 2)
			{
				if(st.getPlayer().getLevel() < 38)
				{
					htmltext = "muzyk_q0431_0103.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "muzyk_q0431_0101.htm";
			}
			else if(condition == 1)
				htmltext = "muzyk_q0431_0106.htm";
			else if(condition == 2 && st.getQuestItemsCount(_431_WeddingMarch.SILVER_CRYSTAL) == 50L)
				htmltext = "muzyk_q0431_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		if((npcId == 20786 || npcId == 20787) && st.getInt("cond") == 1 && st.getQuestItemsCount(_431_WeddingMarch.SILVER_CRYSTAL) < 50L)
		{
			st.giveItems(_431_WeddingMarch.SILVER_CRYSTAL, 1L);
			if(st.getQuestItemsCount(_431_WeddingMarch.SILVER_CRYSTAL) == 50L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}

	static
	{
		_431_WeddingMarch.MELODY_MAESTRO_KANTABILON = 31042;
		_431_WeddingMarch.SILVER_CRYSTAL = 7540;
		_431_WeddingMarch.WEDDING_ECHO_CRYSTAL = 7062;
	}
}
