package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _632_NecromancersRequest extends Quest implements ScriptFile
{
	private static final int WIZARD = 31522;
	private static final int V_HEART = 7542;
	private static final int Z_BRAIN = 7543;
	private static final int ADENA_AMOUNT = 120000;
	private static final int[] VAMPIRES;
	private static final int[] UNDEADS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _632_NecromancersRequest()
	{
		super(true);
		this.addStartNpc(31522);
		this.addKillId(_632_NecromancersRequest.VAMPIRES);
		this.addKillId(_632_NecromancersRequest.UNDEADS);
		addQuestItem(new int[] { 7542, 7543 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("632_4"))
		{
			st.playSound(Quest.SOUND_FINISH);
			htmltext = "shadow_hardin_q0632_0204.htm";
			st.exitCurrentQuest(true);
		}
		else if(event.equals("632_1"))
			htmltext = "shadow_hardin_q0632_0104.htm";
		else if(event.equals("632_3"))
		{
			if(st.getInt("cond") == 2 && st.getQuestItemsCount(7542) > 199L)
			{
				st.takeItems(7542, 200L);
				st.giveItems(57, 120000L, true);
				st.playSound(Quest.SOUND_FINISH);
				st.set("cond", "1");
				htmltext = "shadow_hardin_q0632_0202.htm";
			}
		}
		else if(event.equals("quest_accept"))
			if(st.getPlayer().getLevel() > 62)
			{
				htmltext = "shadow_hardin_q0632_0104.htm";
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
			{
				htmltext = "shadow_hardin_q0632_0103.htm";
				st.exitCurrentQuest(true);
			}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 0 && npcId == 31522)
			htmltext = "shadow_hardin_q0632_0101.htm";
		if(cond == 1)
			htmltext = "shadow_hardin_q0632_0202.htm";
		if(cond == 2 && st.getQuestItemsCount(7542) > 199L)
			htmltext = "shadow_hardin_q0632_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		for(final int i : _632_NecromancersRequest.VAMPIRES)
			if(i == npc.getNpcId())
			{
				if(st.getInt("cond") < 2 && Rnd.chance(50))
				{
					st.giveItems(7542, (long) Config.RATE_QUESTS_DROP, false);
					if(st.getQuestItemsCount(7542) > 199L)
						st.set("cond", "2");
				}
				return null;
			}
		st.rollAndGive(7543, 1, 33.0);
		return null;
	}

	static
	{
		VAMPIRES = new int[] { 21568, 21573, 21582, 21585, 21586, 21587, 21588, 21589, 21590, 21591, 21592, 21593, 21594, 21595 };
		UNDEADS = new int[] { 21547, 21548, 21549, 21550, 21551, 21552, 21555, 21556, 21562, 21571, 21576, 21577, 21579 };
	}
}
