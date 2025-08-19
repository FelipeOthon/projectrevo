package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _351_BlackSwan extends Quest implements ScriptFile
{
	private static final int Gosta = 30916;
	private static final int Heine = 30969;
	private static final int Roman = 30897;
	private static final int ORDER_OF_GOSTA = 4296;
	private static final int LIZARD_FANG = 4297;
	private static final int BARREL_OF_LEAGUE = 4298;
	private static final int BILL_OF_IASON_HEINE = 4310;
	private static final int CHANCE = 100;
	private static final int CHANCE2 = 5;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _351_BlackSwan()
	{
		super(false);
		this.addStartNpc(30916);
		this.addTalkId(new int[] { 30969 });
		this.addTalkId(new int[] { 30897 });
		this.addKillId(new int[] { 20784, 20785, 21639, 21640, 21642, 21643 });
		addQuestItem(new int[] { 4296, 4297, 4298 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final long amount = st.getQuestItemsCount(4297);
		final long amount2 = st.getQuestItemsCount(4298);
		if(event.equalsIgnoreCase("30916-03.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.giveItems(4296, 1L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30969-02a.htm") && amount > 0L)
		{
			htmltext = "30969-02.htm";
			st.giveItems(57, amount * 30L, false);
			st.takeItems(4297, -1L);
		}
		else if(event.equalsIgnoreCase("30969-03a.htm") && amount2 > 0L)
		{
			htmltext = "30969-03.htm";
			st.set("cond", "2");
			st.giveItems(57, amount2 * 500L, false);
			st.giveItems(4310, amount2, false);
			st.takeItems(4298, -1L);
		}
		else if(event.equalsIgnoreCase("30969-01.htm") && st.getInt("cond") == 2)
			htmltext = "30969-04.htm";
		else if(event.equalsIgnoreCase("5"))
		{
			st.exitCurrentQuest(true);
			st.playSound(Quest.SOUND_FINISH);
			htmltext = "";
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30916)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 32)
					htmltext = "30916-01.htm";
				else
				{
					htmltext = "30916-00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond >= 1)
				htmltext = "30916-04.htm";
		if(npcId == 30969)
		{
			if(cond == 1)
				htmltext = "30969-01.htm";
			if(cond == 2)
				htmltext = "30969-04.htm";
		}
		if(npcId == 30897)
			htmltext = "30897.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final double mod = Experience.penaltyModifier(st.calculateLevelDiffForDrop(npc.getLevel(), st.getPlayer().getLevel()), 9.0);
		st.rollAndGive(4297, (int) Config.RATE_QUESTS_DROP, 100.0 * mod);
		st.rollAndGive(4298, (int) Config.RATE_QUESTS_DROP, 5.0 * mod);
		return null;
	}
}
