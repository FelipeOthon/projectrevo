package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AddonsConfig;

public class _639_GuardiansOfTheHolyGrail extends Quest implements ScriptFile
{
	private static final int DROP_CHANCE = 10;
	private static final int DOMINIC = 31350;
	private static final int GREMORY = 32008;
	private static final int GRAIL = 32028;
	private static final int SCRIPTURES = 8069;
	private static final int WATER_BOTTLE = 8070;
	private static final int HOLY_WATER_BOTTLE = 8071;
	private static final int EAS = 960;
	private static final int EWS = 959;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _639_GuardiansOfTheHolyGrail()
	{
		super(true);
		this.addStartNpc(31350);
		this.addTalkId(new int[] { 32008 });
		this.addTalkId(new int[] { 32028 });
		addQuestItem(new int[] { 8069 });
		for(int i = 22122; i <= 22136; ++i)
			this.addKillId(new int[] { i });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("falsepriest_dominic_q0639_04.htm"))
		{
			st.setCond(1);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("falsepriest_dominic_q0639_09.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equals("falsepriest_dominic_q0639_08.htm"))
			st.giveItems(57, st.takeAllItems(8069) * 1625L, false);
		else if(event.equals("falsepriest_gremory_q0639_05.htm"))
		{
			st.setCond(2);
			st.playSound(Quest.SOUND_MIDDLE);
			st.giveItems(8070, 1L, false);
		}
		else if(event.equals("holy_grail_q0639_02.htm"))
		{
			st.setCond(3);
			st.playSound(Quest.SOUND_MIDDLE);
			st.takeItems(8070, -1L);
			st.giveItems(8071, 1L);
		}
		else if(event.equals("falsepriest_gremory_q0639_09.htm"))
		{
			st.setCond(4);
			st.playSound(Quest.SOUND_MIDDLE);
			st.takeItems(8071, -1L);
		}
		else if(event.equals("falsepriest_gremory_q0639_11.htm"))
		{
			st.takeItems(8069, 4000L);
			st.giveItems(959, (int) AddonsConfig.getQuestRewardRates(this), false);
		}
		else if(event.equals("falsepriest_gremory_q0639_13.htm"))
		{
			st.takeItems(8069, 400L);
			st.giveItems(960, (int) AddonsConfig.getQuestRewardRates(this), false);
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
		if(npcId == 31350)
		{
			if(id == 1)
			{
				if(st.getPlayer().getLevel() >= 73)
					htmltext = "falsepriest_dominic_q0639_01.htm";
				else
					htmltext = "falsepriest_dominic_q0639_02.htm";
				st.exitCurrentQuest(true);
			}
			else if(st.getQuestItemsCount(8069) >= 1L)
				htmltext = "falsepriest_dominic_q0639_05.htm";
			else
				htmltext = "falsepriest_dominic_q0639_06.htm";
		}
		else if(npcId == 32008)
		{
			if(cond == 1)
				htmltext = "falsepriest_gremory_q0639_01.htm";
			else if(cond == 2)
				htmltext = "falsepriest_gremory_q0639_06.htm";
			else if(cond == 3)
				htmltext = "falsepriest_gremory_q0639_08.htm";
			else if(cond == 4 && st.getQuestItemsCount(8069) < 400L)
				htmltext = "falsepriest_gremory_q0639_09.htm";
			else if(cond == 4 && st.getQuestItemsCount(8069) >= 4000L)
				htmltext = "falsepriest_gremory_q0639_10.htm";
			else if(cond == 4 && st.getQuestItemsCount(8069) >= 400L && st.getQuestItemsCount(8069) < 4000L)
				htmltext = "falsepriest_gremory_q0639_14.htm";
		}
		else if(npcId == 32028 && cond == 2)
			htmltext = "holy_grail_q0639_01.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		st.rollAndGive(8069, (int) Config.RATE_QUESTS_DROP, 10.0 * npc.getTemplate().rateHp * AddonsConfig.getQuestDropRates(this));
		return null;
	}
}
