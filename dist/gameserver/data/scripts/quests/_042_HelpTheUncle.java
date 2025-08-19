package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _042_HelpTheUncle extends Quest implements ScriptFile
{
	private static final int WATERS = 30828;
	private static final int SOPHYA = 30735;
	private static final int TRIDENT = 291;
	private static final int MAP_PIECE = 7548;
	private static final int MAP = 7549;
	private static final int PET_TICKET = 7583;
	private static final int MONSTER_EYE_DESTROYER = 20068;
	private static final int MONSTER_EYE_GAZER = 20266;
	private static final int MAX_COUNT = 30;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _042_HelpTheUncle()
	{
		super(false);
		this.addStartNpc(30828);
		this.addTalkId(new int[] { 30828 });
		this.addTalkId(new int[] { 30735 });
		this.addKillId(new int[] { 20068 });
		this.addKillId(new int[] { 20266 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("1"))
		{
			htmltext = "pet_manager_waters_q0042_0104.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("3") && st.getQuestItemsCount(291) > 0L)
		{
			htmltext = "pet_manager_waters_q0042_0201.htm";
			st.takeItems(291, 1L);
			st.set("cond", "2");
		}
		else if(event.equals("4") && st.getQuestItemsCount(7548) >= 30L)
		{
			htmltext = "pet_manager_waters_q0042_0301.htm";
			st.takeItems(7548, 30L);
			st.giveItems(7549, 1L);
			st.set("cond", "4");
		}
		else if(event.equals("5") && st.getQuestItemsCount(7549) > 0L)
		{
			htmltext = "sophia_q0042_0401.htm";
			st.takeItems(7549, 1L);
			st.set("cond", "5");
		}
		else if(event.equals("7"))
		{
			htmltext = "pet_manager_waters_q0042_0501.htm";
			st.giveItems(7583, 1L);
			st.unset("cond");
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int cond = st.getInt("cond");
		if(id == 1)
		{
			if(st.getPlayer().getLevel() >= 25)
				htmltext = "pet_manager_waters_q0042_0101.htm";
			else
			{
				htmltext = "pet_manager_waters_q0042_0103.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(id == 2)
			if(npcId == 30828)
			{
				if(cond == 1)
				{
					if(st.getQuestItemsCount(291) == 0L)
						htmltext = "pet_manager_waters_q0042_0106.htm";
					else
						htmltext = "pet_manager_waters_q0042_0105.htm";
				}
				else if(cond == 2)
					htmltext = "pet_manager_waters_q0042_0204.htm";
				else if(cond == 3)
					htmltext = "pet_manager_waters_q0042_0203.htm";
				else if(cond == 4)
					htmltext = "pet_manager_waters_q0042_0303.htm";
				else if(cond == 5)
					htmltext = "pet_manager_waters_q0042_0401.htm";
			}
			else if(npcId == 30735)
				if(cond == 4 && st.getQuestItemsCount(7549) > 0L)
					htmltext = "sophia_q0042_0301.htm";
				else if(cond == 5)
					htmltext = "sophia_q0042_0402.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getInt("cond");
		if(cond == 2)
		{
			final long pieces = st.getQuestItemsCount(7548);
			if(pieces < 29L)
			{
				st.giveItems(7548, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			else if(pieces == 29L)
			{
				st.giveItems(7548, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "3");
			}
		}
		return null;
	}
}
