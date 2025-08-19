package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _112_WalkOfFate extends Quest implements ScriptFile
{
	private static final int Livina = 30572;
	private static final int Karuda = 32017;
	private static final int EnchantD = 956;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _112_WalkOfFate()
	{
		super(false);
		this.addStartNpc(30572);
		this.addTalkId(new int[] { 32017 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("karuda_q0112_0201.htm"))
		{
			st.addExpAndSp(112876L, 5774L);
			st.giveItems(57, (long) (22308.0f + 6000.0f * (st.getRateQuestsReward() - 1.0f)), true);
			st.giveItems(956, 1L, false);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		else if(event.equalsIgnoreCase("seer_livina_q0112_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30572)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 20)
					htmltext = "seer_livina_q0112_0101.htm";
				else
				{
					htmltext = "seer_livina_q0112_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "seer_livina_q0112_0105.htm";
		}
		else if(npcId == 32017 && cond == 1)
			htmltext = "karuda_q0112_0101.htm";
		return htmltext;
	}
}
