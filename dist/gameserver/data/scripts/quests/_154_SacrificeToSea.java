package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _154_SacrificeToSea extends Quest implements ScriptFile
{
	private static final int FOX_FUR_ID = 1032;
	private static final int FOX_FUR_YARN_ID = 1033;
	private static final int MAIDEN_DOLL_ID = 1034;
	private static final int MYSTICS_EARRING_ID = 113;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _154_SacrificeToSea()
	{
		super(false);
		this.addStartNpc(30312);
		this.addTalkId(new int[] { 30051 });
		this.addTalkId(new int[] { 30055 });
		this.addKillId(new int[] { 20481 });
		this.addKillId(new int[] { 20544 });
		this.addKillId(new int[] { 20545 });
		addQuestItem(new int[] { 1032, 1033, 1034 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("1"))
		{
			st.set("id", "0");
			htmltext = "rockswell_q0154_04.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		if(id == 1)
		{
			st.setState(2);
			st.set("cond", "0");
			st.set("id", "0");
		}
		if(npcId == 30312 && st.getInt("cond") == 0)
		{
			if(st.getInt("cond") < 15)
			{
				if(st.getPlayer().getLevel() >= 2)
				{
					htmltext = "rockswell_q0154_03.htm";
					return htmltext;
				}
				htmltext = "rockswell_q0154_02.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "rockswell_q0154_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == 30312 && st.getInt("cond") == 0)
			htmltext = "completed";
		else if(npcId == 30312 && st.getInt("cond") == 1 && st.getQuestItemsCount(1033) == 0L && st.getQuestItemsCount(1034) == 0L && st.getQuestItemsCount(1032) < 10L)
			htmltext = "rockswell_q0154_05.htm";
		else if(npcId == 30312 && st.getInt("cond") == 1 && st.getQuestItemsCount(1032) >= 10L)
			htmltext = "rockswell_q0154_08.htm";
		else if(npcId == 30051 && st.getInt("cond") == 1 && st.getQuestItemsCount(1032) < 10L && st.getQuestItemsCount(1032) > 0L)
			htmltext = "cristel_q0154_01.htm";
		else if(npcId == 30051 && st.getInt("cond") == 1 && st.getQuestItemsCount(1032) >= 10L && st.getQuestItemsCount(1033) == 0L && st.getQuestItemsCount(1034) == 0L && st.getQuestItemsCount(1034) < 10L)
		{
			htmltext = "cristel_q0154_02.htm";
			st.giveItems(1033, 1L);
			st.takeItems(1032, st.getQuestItemsCount(1032));
		}
		else if(npcId == 30051 && st.getInt("cond") == 1 && st.getQuestItemsCount(1033) >= 1L)
			htmltext = "cristel_q0154_03.htm";
		else if(npcId == 30051 && st.getInt("cond") == 1 && st.getQuestItemsCount(1034) == 1L)
			htmltext = "cristel_q0154_04.htm";
		else if(npcId == 30312 && st.getInt("cond") == 1 && st.getQuestItemsCount(1033) >= 1L)
			htmltext = "rockswell_q0154_06.htm";
		else if(npcId == 30055 && st.getInt("cond") == 1 && st.getQuestItemsCount(1033) >= 1L)
		{
			htmltext = "rollfnan_q0154_01.htm";
			st.giveItems(1034, 1L);
			st.takeItems(1033, st.getQuestItemsCount(1033));
		}
		else if(npcId == 30055 && st.getInt("cond") == 1 && st.getQuestItemsCount(1034) >= 1L)
			htmltext = "rollfnan_q0154_02.htm";
		else if(npcId == 30055 && st.getInt("cond") == 1 && st.getQuestItemsCount(1033) == 0L && st.getQuestItemsCount(1034) == 0L)
			htmltext = "rollfnan_q0154_03.htm";
		else if(npcId == 30312 && st.getInt("cond") == 1 && st.getQuestItemsCount(1034) >= 1L && st.getInt("id") != 154)
		{
			st.set("id", "154");
			htmltext = "rockswell_q0154_07.htm";
			st.takeItems(1034, st.getQuestItemsCount(1034));
			st.giveItems(113, 1L);
			st.addExpAndSp(1000L, 0L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1 && st.getQuestItemsCount(1033) == 0L)
			st.rollAndGive(1032, 1, 1, 10, 14.0);
		return null;
	}
}
