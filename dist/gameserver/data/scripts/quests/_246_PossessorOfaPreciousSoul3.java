package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AddonsConfig;

public class _246_PossessorOfaPreciousSoul3 extends Quest implements ScriptFile
{
	private static final int CARADINES_LETTER_2_PART = 7678;
	private static final int RING_OF_GODDESS_WATERBINDER = 7591;
	private static final int NECKLACE_OF_GODDESS_EVERGREEN = 7592;
	private static final int STAFF_OF_GODDESS_RAIN_SONG = 7593;
	private static final int CARADINES_LETTER = 7679;
	private static final int RELIC_BOX = 7594;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _246_PossessorOfaPreciousSoul3()
	{
		super(true);
		this.addStartNpc(31740);
		this.addTalkId(new int[] { 31741 });
		this.addTalkId(new int[] { 30721 });
		this.addKillId(new int[] { 21541 });
		this.addKillId(new int[] { 21544 });
		this.addKillId(new int[] { 25325 });
		addQuestItem(new int[] { 7591, 7592, 7593 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("caradine_q0246_0104.htm"))
		{
			st.set("cond", "1");
			st.takeItems(7678, 1L);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("ossian_q0246_0201.htm"))
		{
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equals("ossian_q0246_0301.htm"))
		{
			st.set("cond", "4");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equals("ossian_q0246_0401.htm"))
		{
			st.takeItems(7591, 1L);
			st.takeItems(7592, 1L);
			st.takeItems(7593, 1L);
			st.set("cond", "6");
			st.giveItems(7594, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equals("magister_ladd_q0246_0501.htm"))
		{
			st.takeItems(7594, 1L);
			st.giveItems(7679, 1L);
			st.addExpAndSp(719843L, 0L);
			st.unset("cond");
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(!st.getPlayer().isSubClassActive())
			return "Subclass only!";
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 31740)
		{
			if(cond == 0)
			{
				final QuestState previous = st.getPlayer().getQuestState(242);
				if(previous != null && previous.getState() == 3 && st.getPlayer().getLevel() >= 65)
					htmltext = "caradine_q0246_0101.htm";
				else
				{
					htmltext = "caradine_q0246_0102.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "caradine_q0246_0105.htm";
		}
		else if(npcId == 31741)
		{
			if(cond == 1)
				htmltext = "ossian_q0246_0101.htm";
			else if((cond == 2 || cond == 3) && (st.getQuestItemsCount(7591) < 1L || st.getQuestItemsCount(7592) < 1L))
				htmltext = "ossian_q0246_0203.htm";
			else if(cond == 3 && st.getQuestItemsCount(7591) > 0L && st.getQuestItemsCount(7592) > 0L)
				htmltext = "ossian_q0246_0202.htm";
			else if(cond == 4)
				htmltext = "ossian_q0246_0301.htm";
			else if(cond == 5)
			{
				if(st.getQuestItemsCount(7593) < 1L)
					htmltext = "ossian_q0246_0402.htm";
				else if(st.getQuestItemsCount(7591) > 0L && st.getQuestItemsCount(7592) > 0L)
					htmltext = "ossian_q0246_0303.htm";
			}
			else if(cond == 6)
				htmltext = "ossian_q0246_0403.htm";
		}
		else if(npcId == 30721 && cond == 6 && st.getQuestItemsCount(7594) > 0L)
			htmltext = "magister_ladd_q0246_0401.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(!st.getPlayer().isSubClassActive())
			return null;
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 2)
		{
			if(Rnd.chance(15.0f * AddonsConfig.getQuestDropRates(this)))
				if(npcId == 21541 && st.getQuestItemsCount(7591) == 0L)
				{
					st.giveItems(7591, 1L);
					if(st.getQuestItemsCount(7592) > 0L)
						st.set("cond", "3");
					st.playSound(Quest.SOUND_ITEMGET);
				}
				else if(npcId == 21544 && st.getQuestItemsCount(7592) == 0L)
				{
					st.giveItems(7592, 1L);
					if(st.getQuestItemsCount(7591) > 0L)
						st.set("cond", "3");
					st.playSound(Quest.SOUND_ITEMGET);
				}
		}
		else if(cond == 4 && npcId == 25325 && st.getQuestItemsCount(7593) == 0L)
		{
			st.giveItems(7593, 1L);
			st.set("cond", "5");
			st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
