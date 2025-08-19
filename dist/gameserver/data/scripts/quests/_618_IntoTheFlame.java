package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _618_IntoTheFlame extends Quest implements ScriptFile
{
	private static final int KLEIN = 31540;
	private static final int HILDA = 31271;
	private static final int VACUALITE_ORE = 7265;
	private static final int VACUALITE = 7266;
	private static final int FLOATING_STONE = 7267;
	private static final int CHANCE_FOR_QUEST_ITEMS = 50;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _618_IntoTheFlame()
	{
		super(false);
		this.addStartNpc(31540);
		this.addTalkId(new int[] { 31271 });
		this.addKillId(new int[] { 21274, 21275, 21276, 21277 });
		this.addKillId(new int[] { 21282, 21283, 21284, 21285 });
		this.addKillId(new int[] { 21290, 21291, 21292, 21293 });
		addQuestItem(new int[] { 7265 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("watcher_valakas_klein_q0618_0104.htm") && cond == 0)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("watcher_valakas_klein_q0618_0401.htm"))
		{
			if(st.getQuestItemsCount(7266) > 0L && cond == 4)
			{
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
				st.takeItems(7266, 1L);
				st.giveItems(7267, 1L);
			}
			else
				htmltext = "watcher_valakas_klein_q0618_0104.htm";
		}
		else if(event.equalsIgnoreCase("blacksmith_hilda_q0618_0201.htm") && cond == 1)
		{
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("blacksmith_hilda_q0618_0301.htm"))
			if(cond == 3 && st.getQuestItemsCount(7265) >= 50L)
			{
				st.takeItems(7265, -1L);
				st.giveItems(7266, 1L);
				st.set("cond", "4");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				htmltext = "blacksmith_hilda_q0618_0203.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 31540)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 60)
				{
					htmltext = "watcher_valakas_klein_q0618_0103.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "watcher_valakas_klein_q0618_0101.htm";
			}
			else if(cond == 4 && st.getQuestItemsCount(7266) > 0L)
				htmltext = "watcher_valakas_klein_q0618_0301.htm";
			else
				htmltext = "watcher_valakas_klein_q0618_0104.htm";
		}
		else if(npcId == 31271)
			if(cond == 1)
				htmltext = "blacksmith_hilda_q0618_0101.htm";
			else if(cond == 3 && st.getQuestItemsCount(7265) >= 50L)
				htmltext = "blacksmith_hilda_q0618_0202.htm";
			else if(cond == 4)
				htmltext = "blacksmith_hilda_q0618_0303.htm";
			else
				htmltext = "blacksmith_hilda_q0618_0203.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 2 && st.getQuestItemsCount(7265) < 50L)
		{
			st.dropItems(7265, 1, 1, 50L, 50.0, true);
			if(st.getQuestItemsCount(7265) >= 50L)
				st.set("cond", "3");
		}
		return null;
	}
}
