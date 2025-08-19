package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _326_VanquishRemnants extends Quest implements ScriptFile
{
	private static final int Leopold = 30435;
	private static final int RedCrossBadge = 1359;
	private static final int BlueCrossBadge = 1360;
	private static final int BlackCrossBadge = 1361;
	private static final int BlackLionMark = 1369;
	private static final int OlMahumPatrol = 30425;
	private static final int OlMahumGuard = 20058;
	private static final int OlMahumStraggler = 20061;
	private static final int OlMahumShooter = 20063;
	private static final int OlMahumCaptain = 20066;
	private static final int OlMahumCommander = 20076;
	private static final int OlMahumSupplier = 20436;
	private static final int OlMahumRecruit = 20437;
	private static final int OlMahumGeneral = 20438;
	public final int[][] DROPLIST_COND;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _326_VanquishRemnants()
	{
		super(false);
		DROPLIST_COND = new int[][] {
				{ 30425, 1359 },
				{ 20058, 1359 },
				{ 20437, 1359 },
				{ 20061, 1360 },
				{ 20063, 1360 },
				{ 20436, 1360 },
				{ 20066, 1361 },
				{ 20438, 1361 },
				{ 20076, 1361 } };
		this.addStartNpc(30435);
		this.addTalkId(new int[] { 30435 });
		for(int i = 0; i < DROPLIST_COND.length; ++i)
			this.addKillId(new int[] { DROPLIST_COND[i][0] });
		addQuestItem(new int[] { 1359 });
		addQuestItem(new int[] { 1360 });
		addQuestItem(new int[] { 1361 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("leopold_q0326_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("leopold_q0326_03.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30435)
			if(st.getPlayer().getLevel() < 21)
			{
				htmltext = "leopold_q0326_01.htm";
				st.exitCurrentQuest(true);
			}
			else if(cond == 0)
				htmltext = "leopold_q0326_02.htm";
			else if(cond == 1 && st.getQuestItemsCount(1359) == 0L && st.getQuestItemsCount(1360) == 0L && st.getQuestItemsCount(1361) == 0L)
				htmltext = "leopold_q0326_04.htm";
			else if(cond == 1)
			{
				if(st.getQuestItemsCount(1359) + st.getQuestItemsCount(1360) + st.getQuestItemsCount(1361) >= 100L)
				{
					if(st.getQuestItemsCount(1369) == 0L)
					{
						htmltext = "leopold_q0326_09.htm";
						st.giveItems(1369, 1L);
					}
					else
						htmltext = "leopold_q0326_06.htm";
				}
				else
					htmltext = "leopold_q0326_05.htm";
				st.giveItems(57, st.getQuestItemsCount(1359) * 89L + st.getQuestItemsCount(1360) * 95L + st.getQuestItemsCount(1361) * 101L, true);
				st.takeItems(1359, -1L);
				st.takeItems(1360, -1L);
				st.takeItems(1361, -1L);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() == 2)
			for(int i = 0; i < DROPLIST_COND.length; ++i)
				if(npc.getNpcId() == DROPLIST_COND[i][0])
					st.giveItems(DROPLIST_COND[i][1], 1L);
		return null;
	}
}
