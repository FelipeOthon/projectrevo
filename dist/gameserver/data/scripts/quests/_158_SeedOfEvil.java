package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _158_SeedOfEvil extends Quest implements ScriptFile
{
	int CLAY_TABLET_ID;
	int ENCHANT_ARMOR_D;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _158_SeedOfEvil()
	{
		super(false);
		CLAY_TABLET_ID = 1025;
		ENCHANT_ARMOR_D = 956;
		this.addStartNpc(30031);
		this.addKillId(new int[] { 27016 });
		addQuestItem(new int[] { CLAY_TABLET_ID });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("quest_accept"))
		{
			st.set("id", "0");
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			htmltext = "quilt_q0158_04.htm";
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
			st.set("id", "0");
		}
		if(npcId == 30031 && st.getInt("cond") == 0)
		{
			if(st.getInt("cond") < 15)
			{
				if(st.getPlayer().getLevel() >= 21)
				{
					htmltext = "quilt_q0158_03.htm";
					return htmltext;
				}
				htmltext = "quilt_q0158_02.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "quilt_q0158_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == 30031 && st.getInt("cond") == 0)
			htmltext = "completed";
		else if(npcId == 30031 && st.getInt("cond") != 0 && st.getQuestItemsCount(CLAY_TABLET_ID) == 0L)
			htmltext = "quilt_q0158_05.htm";
		else if(npcId == 30031 && st.getInt("cond") != 0 && st.getQuestItemsCount(CLAY_TABLET_ID) != 0L)
		{
			st.takeItems(CLAY_TABLET_ID, st.getQuestItemsCount(CLAY_TABLET_ID));
			st.playSound(Quest.SOUND_FINISH);
			st.giveItems(57, 1495L);
			st.addExpAndSp(17818L, 927L);
			st.giveItems(ENCHANT_ARMOR_D, 1L);
			htmltext = "quilt_q0158_06.htm";
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(CLAY_TABLET_ID) == 0L)
		{
			st.giveItems(CLAY_TABLET_ID, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "2");
		}
		return null;
	}
}
