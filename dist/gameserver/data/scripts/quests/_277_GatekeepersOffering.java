package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _277_GatekeepersOffering extends Quest implements ScriptFile
{
	private static final int STARSTONE1_ID = 1572;
	private static final int GATEKEEPER_CHARM_ID = 1658;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _277_GatekeepersOffering()
	{
		super(false);
		this.addStartNpc(30576);
		this.addKillId(new int[] { 20333 });
		addQuestItem(new int[] { 1572 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("1"))
			if(st.getPlayer().getLevel() >= 15)
			{
				htmltext = "gatekeeper_tamil_q0277_03.htm";
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
				htmltext = "gatekeeper_tamil_q0277_01.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30576 && cond == 0)
			htmltext = "gatekeeper_tamil_q0277_02.htm";
		else if(npcId == 30576 && cond == 1 && st.getQuestItemsCount(1572) < 20L)
			htmltext = "gatekeeper_tamil_q0277_04.htm";
		else if(npcId == 30576 && cond == 2 && st.getQuestItemsCount(1572) < 20L)
			htmltext = "gatekeeper_tamil_q0277_04.htm";
		else if(npcId == 30576 && cond == 2 && st.getQuestItemsCount(1572) >= 20L)
		{
			htmltext = "gatekeeper_tamil_q0277_05.htm";
			st.takeItems(1572, -1L);
			st.giveItems(1658, 2L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		st.rollAndGive(1572, 1, 1, 20, 33.0);
		if(st.getQuestItemsCount(1572) >= 20L)
			st.set("cond", "2");
		return null;
	}
}
