package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _266_PleaOfPixies extends Quest implements ScriptFile
{
	private static final int PREDATORS_FANG = 1334;
	private static final int EMERALD = 1337;
	private static final int BLUE_ONYX = 1338;
	private static final int ONYX = 1339;
	private static final int GLASS_SHARD = 1336;
	private static final int REC_LEATHER_BOOT = 2176;
	private static final int REC_SPIRITSHOT = 3032;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _266_PleaOfPixies()
	{
		super(false);
		this.addStartNpc(31852);
		this.addKillId(new int[] { 20525, 20530, 20534, 20537 });
		addQuestItem(new int[] { 1334 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("pixy_murika_q0266_03.htm"))
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
		String htmltext = "noquest";
		if(st.getInt("cond") == 0)
		{
			if(st.getPlayer().getRace() != Race.elf)
			{
				htmltext = "pixy_murika_q0266_00.htm";
				st.exitCurrentQuest(true);
			}
			else if(st.getPlayer().getLevel() < 3)
			{
				htmltext = "pixy_murika_q0266_01.htm";
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "pixy_murika_q0266_02.htm";
		}
		else if(st.getQuestItemsCount(1334) < 100L)
			htmltext = "pixy_murika_q0266_04.htm";
		else
		{
			st.takeItems(1334, -1L);
			final int n = Rnd.get(100);
			if(n < 2)
			{
				st.giveItems(1337, 1L);
				st.giveItems(3032, 1L);
				st.playSound(Quest.SOUND_JACKPOT);
			}
			else if(n < 20)
			{
				st.giveItems(1338, 1L);
				st.giveItems(2176, 1L);
			}
			else if(n < 45)
				st.giveItems(1339, 1L);
			else
				st.giveItems(1336, 1L);
			htmltext = "pixy_murika_q0266_05.htm";
			st.exitCurrentQuest(true);
			st.playSound(Quest.SOUND_FINISH);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1)
			st.rollAndGive(1334, 1, 1, 100, 60 + npc.getLevel() * 5);
		return null;
	}
}
