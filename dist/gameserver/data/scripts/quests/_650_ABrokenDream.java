package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _650_ABrokenDream extends Quest implements ScriptFile
{
	private static final int RailroadEngineer = 32054;
	private static final int ForgottenCrewman = 22027;
	private static final int VagabondOfTheRuins = 22028;
	private static final int RemnantsOfOldDwarvesDreams = 8514;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _650_ABrokenDream()
	{
		super(false);
		this.addStartNpc(32054);
		this.addKillId(new int[] { 22027 });
		this.addKillId(new int[] { 22028 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "ghost_of_railroadman_q0650_0103.htm";
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.set("cond", "1");
		}
		else if(event.equalsIgnoreCase("650_4"))
		{
			htmltext = "ghost_of_railroadman_q0650_0205.htm";
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
			st.unset("cond");
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getInt("cond");
		String htmltext = "noquest";
		if(cond == 0)
		{
			final QuestState OceanOfDistantStar = st.getPlayer().getQuestState(117);
			if(OceanOfDistantStar != null)
			{
				if(OceanOfDistantStar.isCompleted())
				{
					if(st.getPlayer().getLevel() < 39)
					{
						st.exitCurrentQuest(true);
						htmltext = "ghost_of_railroadman_q0650_0102.htm";
					}
					else
						htmltext = "ghost_of_railroadman_q0650_0101.htm";
				}
				else
				{
					htmltext = "ghost_of_railroadman_q0650_0104.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "ghost_of_railroadman_q0650_0104.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 1)
			htmltext = "ghost_of_railroadman_q0650_0202.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		st.rollAndGive(8514, 1, 1, 68.0);
		return null;
	}
}
