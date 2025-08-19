package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _432_BirthdayPartySong extends Quest implements ScriptFile
{
	private static int MELODY_MAESTRO_OCTAVIA;
	private static int ROUGH_HEWN_ROCK_GOLEMS;
	private static int RED_CRYSTALS;
	private static int BIRTHDAY_ECHO_CRYSTAL;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _432_BirthdayPartySong()
	{
		super(false);
		this.addStartNpc(_432_BirthdayPartySong.MELODY_MAESTRO_OCTAVIA);
		this.addKillId(new int[] { _432_BirthdayPartySong.ROUGH_HEWN_ROCK_GOLEMS });
		addQuestItem(new int[] { _432_BirthdayPartySong.RED_CRYSTALS });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("muzyko_q0432_0104.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("muzyko_q0432_0201.htm"))
			if(st.getQuestItemsCount(_432_BirthdayPartySong.RED_CRYSTALS) == 50L)
			{
				st.takeItems(_432_BirthdayPartySong.RED_CRYSTALS, -1L);
				st.giveItems(_432_BirthdayPartySong.BIRTHDAY_ECHO_CRYSTAL, 25L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "muzyko_q0432_0202.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int condition = st.getInt("cond");
		final int npcId = npc.getNpcId();
		if(npcId == _432_BirthdayPartySong.MELODY_MAESTRO_OCTAVIA)
			if(condition == 0)
			{
				if(st.getPlayer().getLevel() >= 31)
					htmltext = "muzyko_q0432_0101.htm";
				else
				{
					htmltext = "muzyko_q0432_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(condition == 1)
				htmltext = "muzyko_q0432_0106.htm";
			else if(condition == 2 && st.getQuestItemsCount(_432_BirthdayPartySong.RED_CRYSTALS) == 50L)
				htmltext = "muzyko_q0432_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		if(npcId == _432_BirthdayPartySong.ROUGH_HEWN_ROCK_GOLEMS && st.getInt("cond") == 1 && st.getQuestItemsCount(_432_BirthdayPartySong.RED_CRYSTALS) < 50L)
		{
			st.giveItems(_432_BirthdayPartySong.RED_CRYSTALS, 1L);
			if(st.getQuestItemsCount(_432_BirthdayPartySong.RED_CRYSTALS) == 50L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}

	static
	{
		_432_BirthdayPartySong.MELODY_MAESTRO_OCTAVIA = 31043;
		_432_BirthdayPartySong.ROUGH_HEWN_ROCK_GOLEMS = 21103;
		_432_BirthdayPartySong.RED_CRYSTALS = 7541;
		_432_BirthdayPartySong.BIRTHDAY_ECHO_CRYSTAL = 7061;
	}
}
