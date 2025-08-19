package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _375_WhisperOfDreams2 extends Quest implements ScriptFile
{
	private static final int MANAKIA = 30515;
	private static final int MSTONE = 5887;
	private static final int K_HORN = 5888;
	private static final int CH_SKULL = 5889;
	private static final int DROP_CHANCE_ITEMS = 20;
	private static final int[] REWARDS;
	private static final int[] REWARDS2;
	private static final int CAVEHOWLER = 20624;
	private static final int KARIK = 20629;
	private static final String _default = "noquest";

	public _375_WhisperOfDreams2()
	{
		super(true);
		this.addStartNpc(30515);
		this.addKillId(new int[] { 20624, 20629 });
		addQuestItem(new int[] { 5888, 5889 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30515-6.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30515-7.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("30515-8.htm"))
		{}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int id = st.getState();
		if(id == 1)
		{
			htmltext = "30515-1.htm";
			if(st.getPlayer().getLevel() < 60)
			{
				htmltext = "30515-2.htm";
				st.exitCurrentQuest(true);
			}
			else if(st.getQuestItemsCount(5887) < 1L)
			{
				htmltext = "30515-3.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(id == 2)
			if(st.getQuestItemsCount(5889) == 100L && st.getQuestItemsCount(5888) == 100L)
			{
				st.takeItems(5889, -1L);
				st.takeItems(5888, -1L);
				final int item = Config.ALT_100_RECIPES_A ? _375_WhisperOfDreams2.REWARDS2[Rnd.get(_375_WhisperOfDreams2.REWARDS2.length)] : _375_WhisperOfDreams2.REWARDS[Rnd.get(_375_WhisperOfDreams2.REWARDS.length)];
				st.giveItems(item, 1L);
				htmltext = "30515-4.htm";
			}
			else
				htmltext = "30515-5.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcid = npc.getNpcId();
		if(npcid == 20629)
			st.rollAndGive(5888, 1, 1, 100, 20.0);
		else if(npcid == 20624)
			st.rollAndGive(5889, 1, 1, 100, 20.0);
		return null;
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		REWARDS = new int[] { 5348, 5352, 5350 };
		REWARDS2 = new int[] { 5349, 5353, 5351 };
	}
}
