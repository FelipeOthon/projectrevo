package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _043_HelpTheSister extends Quest implements ScriptFile
{
	private static final int COOPER = 30829;
	private static final int GALLADUCCI = 30097;
	private static final int CRAFTED_DAGGER = 220;
	private static final int MAP_PIECE = 7550;
	private static final int MAP = 7551;
	private static final int PET_TICKET = 7584;
	private static final int SPECTER = 20171;
	private static final int SORROW_MAIDEN = 20197;
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

	public _043_HelpTheSister()
	{
		super(false);
		this.addStartNpc(30829);
		this.addTalkId(new int[] { 30097 });
		this.addKillId(new int[] { 20171 });
		this.addKillId(new int[] { 20197 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("1"))
		{
			htmltext = "pet_manager_cooper_q0043_0104.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("3") && st.getQuestItemsCount(220) > 0L)
		{
			htmltext = "pet_manager_cooper_q0043_0201.htm";
			st.takeItems(220, 1L);
			st.set("cond", "2");
		}
		else if(event.equals("4") && st.getQuestItemsCount(7550) >= 30L)
		{
			htmltext = "pet_manager_cooper_q0043_0301.htm";
			st.takeItems(7550, 30L);
			st.giveItems(7551, 1L);
			st.set("cond", "4");
		}
		else if(event.equals("5") && st.getQuestItemsCount(7551) > 0L)
		{
			htmltext = "galladuchi_q0043_0401.htm";
			st.takeItems(7551, 1L);
			st.set("cond", "5");
		}
		else if(event.equals("7"))
		{
			htmltext = "pet_manager_cooper_q0043_0501.htm";
			st.giveItems(7584, 1L);
			st.set("cond", "0");
			st.exitCurrentQuest(false);
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
			if(st.getPlayer().getLevel() >= 26)
				htmltext = "pet_manager_cooper_q0043_0101.htm";
			else
			{
				st.exitCurrentQuest(true);
				htmltext = "pet_manager_cooper_q0043_0103.htm";
			}
		}
		else if(id == 2)
		{
			final int cond = st.getInt("cond");
			if(npcId == 30829)
			{
				if(cond == 1)
				{
					if(st.getQuestItemsCount(220) == 0L)
						htmltext = "pet_manager_cooper_q0043_0106.htm";
					else
						htmltext = "pet_manager_cooper_q0043_0105.htm";
				}
				else if(cond == 2)
					htmltext = "pet_manager_cooper_q0043_0204.htm";
				else if(cond == 3)
					htmltext = "pet_manager_cooper_q0043_0203.htm";
				else if(cond == 4)
					htmltext = "pet_manager_cooper_q0043_0303.htm";
				else if(cond == 5)
					htmltext = "pet_manager_cooper_q0043_0401.htm";
			}
			else if(npcId == 30097 && cond == 4 && st.getQuestItemsCount(7551) > 0L)
				htmltext = "galladuchi_q0043_0301.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getInt("cond");
		if(cond == 2)
		{
			final long pieces = st.getQuestItemsCount(7550);
			if(pieces < 30L)
			{
				st.giveItems(7550, 1L);
				if(pieces < 29L)
					st.playSound(Quest.SOUND_ITEMGET);
				else
				{
					st.playSound(Quest.SOUND_MIDDLE);
					st.set("cond", "3");
				}
			}
		}
		return null;
	}
}
