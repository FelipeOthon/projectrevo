package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _602_ShadowofLight extends Quest implements ScriptFile
{
	private static final int ARGOS = 31683;
	private static final int EYE_OF_DARKNESS = 7189;
	private static final int[][] REWARDS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _602_ShadowofLight()
	{
		super(true);
		this.addStartNpc(31683);
		this.addKillId(new int[] { 21299 });
		this.addKillId(new int[] { 21304 });
		addQuestItem(new int[] { 7189 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("eye_of_argos_q0602_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("eye_of_argos_q0602_0201.htm"))
		{
			st.takeItems(7189, -1L);
			final int random = Rnd.get(100) + 1;
			for(int i = 0; i < _602_ShadowofLight.REWARDS.length; ++i)
				if(_602_ShadowofLight.REWARDS[i][4] <= random && random <= _602_ShadowofLight.REWARDS[i][5])
				{
					st.giveItems(57, _602_ShadowofLight.REWARDS[i][1], true);
					st.addExpAndSp(_602_ShadowofLight.REWARDS[i][2], _602_ShadowofLight.REWARDS[i][3]);
					if(_602_ShadowofLight.REWARDS[i][0] != 0)
						st.giveItems(_602_ShadowofLight.REWARDS[i][0], 3L, true);
				}
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
		final int id = st.getState();
		int cond = 0;
		if(id != 1)
			cond = st.getInt("cond");
		if(npcId == 31683)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 68)
				{
					htmltext = "eye_of_argos_q0602_0103.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "eye_of_argos_q0602_0101.htm";
			}
			else if(cond == 1)
				htmltext = "eye_of_argos_q0602_0106.htm";
			else if(cond == 2 && st.getQuestItemsCount(7189) == 100L)
				htmltext = "eye_of_argos_q0602_0105.htm";
			else
			{
				htmltext = "eye_of_argos_q0602_0106.htm";
				st.set("cond", "1");
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1)
		{
			final long count = st.getQuestItemsCount(7189);
			if(count < 100L && Rnd.chance(npc.getNpcId() == 21299 ? 35 : 40))
			{
				st.giveItems(7189, 1L);
				if(count == 99L)
				{
					st.set("cond", "2");
					st.playSound(Quest.SOUND_MIDDLE);
				}
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		return null;
	}

	static
	{
		REWARDS = new int[][] {
				{ 6699, 40000, 120000, 20000, 1, 19 },
				{ 6698, 60000, 110000, 15000, 20, 39 },
				{ 6700, 40000, 150000, 10000, 40, 49 },
				{ 0, 100000, 140000, 11250, 50, 100 } };
	}
}
