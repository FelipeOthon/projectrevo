package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _613_ProveYourCourage extends Quest implements ScriptFile
{
	private static final int DURAI = 31377;
	private static final int KETRAS_HERO_HEKATON = 25299;
	private static final int HEAD_OF_HEKATON = 7240;
	private static final int FEATHER_OF_VALOR = 7229;
	private static final int MARK_OF_VARKA_ALLIANCE1 = 7221;
	private static final int MARK_OF_VARKA_ALLIANCE2 = 7222;
	private static final int MARK_OF_VARKA_ALLIANCE3 = 7223;
	private static final int MARK_OF_VARKA_ALLIANCE4 = 7224;
	private static final int MARK_OF_VARKA_ALLIANCE5 = 7225;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _613_ProveYourCourage()
	{
		super(true);
		this.addStartNpc(31377);
		this.addKillId(new int[] { 25299 });
		addQuestItem(new int[] { 7240 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("quest_accept"))
		{
			htmltext = "elder_ashas_barka_durai_q0613_0104.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("613_3"))
			if(st.getQuestItemsCount(7240) >= 1L)
			{
				htmltext = "elder_ashas_barka_durai_q0613_0201.htm";
				st.takeItems(7240, -1L);
				st.giveItems(7229, 1L);
				st.addExpAndSp(0L, 10000L);
				st.unset("cond");
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "elder_ashas_barka_durai_q0613_0106.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() >= 75)
			{
				if(st.getQuestItemsCount(7223) == 1L || st.getQuestItemsCount(7224) == 1L || st.getQuestItemsCount(7225) == 1L)
					htmltext = "elder_ashas_barka_durai_q0613_0101.htm";
				else
				{
					htmltext = "elder_ashas_barka_durai_q0613_0102.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "elder_ashas_barka_durai_q0613_0103.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 1 && st.getQuestItemsCount(7240) == 0L)
			htmltext = "elder_ashas_barka_durai_q0613_0106.htm";
		else if(cond == 2 && st.getQuestItemsCount(7240) >= 1L)
			htmltext = "elder_ashas_barka_durai_q0613_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(npcId == 25299 && st.getInt("cond") == 1)
		{
			st.giveItems(7240, 1L);
			st.set("cond", "2");
			st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
