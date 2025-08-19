package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AddonsConfig;

public class _623_TheFinestFood extends Quest implements ScriptFile
{
	public final int JEREMY = 31521;
	public static final int HOT_SPRINGS_BUFFALO = 21315;
	public static final int HOT_SPRINGS_FLAVA = 21316;
	public static final int HOT_SPRINGS_ANTELOPE = 21318;
	public static final int LEAF_OF_FLAVA = 7199;
	public static final int BUFFALO_MEAT = 7200;
	public static final int ANTELOPE_HORN = 7201;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _623_TheFinestFood()
	{
		super(true);
		this.addStartNpc(JEREMY);
		this.addTalkId(new int[] { 31521 });
		this.addKillId(new int[] { 21315 });
		this.addKillId(new int[] { 21316 });
		this.addKillId(new int[] { 21318 });
		addQuestItem(new int[] { 7200 });
		addQuestItem(new int[] { 7199 });
		addQuestItem(new int[] { 7201 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "jeremy_q0623_0104.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("623_3"))
		{
			htmltext = "jeremy_q0623_0201.htm";
			st.takeItems(7199, -1L);
			st.takeItems(7200, -1L);
			st.takeItems(7201, -1L);
			final int luck = Rnd.get(100);
			if(luck < 11)
			{
				st.giveItems(57, (long) (25000.0f * Config.RATE_QUESTS_REWARD));
				st.giveItems(6849, 1L, false);
			}
			else if(luck < 23)
			{
				st.giveItems(57, (long) (65000.0f * Config.RATE_QUESTS_REWARD));
				st.giveItems(6847, 1L, false);
			}
			else if(luck < 33)
			{
				st.giveItems(57, (long) (25000.0f * Config.RATE_QUESTS_REWARD));
				st.giveItems(6851, 1L, false);
			}
			else
			{
				st.giveItems(57, (long) (73000.0f * Config.RATE_QUESTS_REWARD));
				st.addExpAndSp((long) (230000.0f * Config.RATE_QUESTS_REWARD), (long) (18250.0f * Config.RATE_QUESTS_REWARD));
			}
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		if(id == 1)
			st.set("cond", "0");
		if(summ(st))
			st.set("cond", "2");
		final int cond = st.getInt("cond");
		if(npcId == 31521)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 71)
					htmltext = "jeremy_q0623_0101.htm";
				else
				{
					htmltext = "jeremy_q0623_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1 && !summ(st))
				htmltext = "jeremy_q0623_0106.htm";
			else if(cond == 2 && summ(st))
				htmltext = "jeremy_q0623_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getInt("cond");
		if(cond == 1)
		{
			final int npcId = npc.getNpcId();
			if(npcId == 21315)
			{
				if(st.getQuestItemsCount(7200) < 100L)
				{
					final int cnt = (int) (Config.RATE_QUESTS_DROP * AddonsConfig.getQuestDropRates(this));
					st.dropItems(7200, cnt, cnt, 100L, 100.0, false);
					if(summ(st))
						st.set("cond", "2");
				}
			}
			else if(npcId == 21316)
			{
				if(st.getQuestItemsCount(7199) < 100L)
				{
					final int cnt = (int) (Config.RATE_QUESTS_DROP * AddonsConfig.getQuestDropRates(this));
					st.dropItems(7199, cnt, cnt, 100L, 100.0, false);
					if(summ(st))
						st.set("cond", "2");
				}
			}
			else if(npcId == 21318 && st.getQuestItemsCount(7201) < 100L)
			{
				final int cnt = (int) (Config.RATE_QUESTS_DROP * AddonsConfig.getQuestDropRates(this));
				st.dropItems(7201, cnt, cnt, 100L, 100.0, false);
				if(summ(st))
					st.set("cond", "2");
			}
		}
		return null;
	}

	private boolean summ(final QuestState st)
	{
		return st.getQuestItemsCount(7199) >= 100L && st.getQuestItemsCount(7200) >= 100L && st.getQuestItemsCount(7201) >= 100L;
	}
}
