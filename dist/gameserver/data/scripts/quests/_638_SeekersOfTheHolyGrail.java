package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AddonsConfig;

public class _638_SeekersOfTheHolyGrail extends Quest implements ScriptFile
{
	private static final int DROP_CHANCE = 10;
	private static final int INNOCENTIN = 31328;
	private static final int TOTEM = 8068;
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

	public _638_SeekersOfTheHolyGrail()
	{
		super(true);
		this.addStartNpc(31328);
		addQuestItem(new int[] { 8068 });
		for(int i = 22137; i <= 22176; ++i)
			this.addKillId(new int[] { i });
		this.addKillId(new int[] { 22194 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("highpriest_innocentin_q0638_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("highpriest_innocentin_q0638_09.htm"))
		{
			st.playSound(Quest.SOUND_GIVEUP);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int id = st.getState();
		if(id == 1)
		{
			if(st.getPlayer().getLevel() >= 73)
				htmltext = "highpriest_innocentin_q0638_01.htm";
			else
				htmltext = "highpriest_innocentin_q0638_02.htm";
		}
		else
			htmltext = tryRevard(st);
		return htmltext;
	}

	private String tryRevard(final QuestState st)
	{
		boolean ok = false;
		while(st.getQuestItemsCount(8068) >= 2000L)
		{
			st.takeItems(8068, 2000L);
			final int chance = Rnd.get(3);
			if(chance == 0)
				st.giveItems(959, (int) AddonsConfig.getQuestRewardRates(this), false);
			else if(chance == 1)
				st.giveItems(960, (int) AddonsConfig.getQuestRewardRates(this), false);
			else
				st.giveItems(57, 3576000L, false);
			ok = true;
		}
		if(ok)
		{
			st.playSound(Quest.SOUND_MIDDLE);
			return "highpriest_innocentin_q0638_10.htm";
		}
		return "highpriest_innocentin_q0638_05.htm";
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		st.rollAndGive(8068, 1, 10.0 * npc.getTemplate().rateHp * AddonsConfig.getQuestDropRates(this));
		return null;
	}
}
