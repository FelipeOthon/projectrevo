package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _626_ADarkTwilight extends Quest implements ScriptFile
{
	private static final int Hierarch = 31517;
	private static int BloodOfSaint;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _626_ADarkTwilight()
	{
		super(true);
		this.addStartNpc(31517);
		for(int npcId = 21520; npcId <= 21542; ++npcId)
			this.addKillId(new int[] { npcId });
		addQuestItem(new int[] { _626_ADarkTwilight.BloodOfSaint });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("dark_presbyter_q0626_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("dark_presbyter_q0626_0201.htm"))
		{
			if(st.getQuestItemsCount(_626_ADarkTwilight.BloodOfSaint) < 300L)
				htmltext = "dark_presbyter_q0626_0203.htm";
		}
		else if(event.equalsIgnoreCase("rew_exp"))
		{
			st.takeItems(_626_ADarkTwilight.BloodOfSaint, -1L);
			st.addExpAndSp((long) (162773.0f * Config.RATE_QUESTS_REWARD), (long) (12500.0f * Config.RATE_QUESTS_REWARD), true);
			htmltext = "dark_presbyter_q0626_0202.htm";
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("rew_adena"))
		{
			st.takeItems(_626_ADarkTwilight.BloodOfSaint, -1L);
			st.giveItems(57, (long) (100000.0f * Config.RATE_QUESTS_REWARD), true);
			htmltext = "dark_presbyter_q0626_0202.htm";
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		final int npcId = npc.getNpcId();
		if(npcId == 31517)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 60)
				{
					htmltext = "dark_presbyter_q0626_0103.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "dark_presbyter_q0626_0101.htm";
			}
			else if(cond == 1)
				htmltext = "dark_presbyter_q0626_0106.htm";
			else if(cond == 2)
				htmltext = "dark_presbyter_q0626_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1 && Rnd.chance(70))
		{
			st.giveItems(_626_ADarkTwilight.BloodOfSaint, (long) Config.RATE_QUESTS_DROP);
			if(st.getQuestItemsCount(_626_ADarkTwilight.BloodOfSaint) == 300L)
				st.set("cond", "2");
		}
		return null;
	}

	static
	{
		_626_ADarkTwilight.BloodOfSaint = 7169;
	}
}
