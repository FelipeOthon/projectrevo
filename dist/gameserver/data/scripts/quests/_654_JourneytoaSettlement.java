package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _654_JourneytoaSettlement extends Quest implements ScriptFile
{
	private static final int NamelessSpirit = 31453;
	private static final int CanyonAntelope = 21294;
	private static final int CanyonAntelopeSlave = 21295;
	private static final int AntelopeSkin = 8072;
	private static final int FrintezzasMagicForceFieldRemovalScroll = 8073;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _654_JourneytoaSettlement()
	{
		super(true);
		this.addStartNpc(31453);
		this.addKillId(new int[] { 21294, 21295 });
		addQuestItem(new int[] { 8072 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("printessa_spirit_q0654_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		if(event.equalsIgnoreCase("printessa_spirit_q0654_04.htm"))
			st.set("cond", "2");
		if(event.equalsIgnoreCase("printessa_spirit_q0654_07.htm"))
		{
			st.giveItems(8073, 1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final QuestState q = st.getPlayer().getQuestState(119);
		if(q == null)
			return htmltext;
		if(st.getPlayer().getLevel() < 74)
		{
			htmltext = "printessa_spirit_q0654_02.htm";
			st.exitCurrentQuest(true);
			return htmltext;
		}
		if(!q.isCompleted())
		{
			htmltext = "noquest";
			st.exitCurrentQuest(true);
			return htmltext;
		}
		final int cond = st.getCond();
		if(npc.getNpcId() == 31453)
		{
			if(cond == 0)
				return "printessa_spirit_q0654_01.htm";
			if(cond == 1)
				return "printessa_spirit_q0654_03.htm";
			if(cond == 3)
				return "printessa_spirit_q0654_06.htm";
		}
		else
			htmltext = "noquest";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getCond() == 2 && Rnd.chance(5))
		{
			st.setCond(3);
			st.giveItems(8072, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		return null;
	}
}
