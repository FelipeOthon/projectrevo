package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.ScriptFile;

public class _260_HuntTheOrcs extends Quest implements ScriptFile
{
	private static final int ORC_AMULET = 1114;
	private static final int ORC_NECKLACE = 1115;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _260_HuntTheOrcs()
	{
		super(false);
		this.addStartNpc(30221);
		this.addKillId(new int[] { 20468, 20469, 20470, 20471, 20472, 20473 });
		addQuestItem(new int[] { 1114, 1115 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("sentinel_rayjien_q0260_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("sentinel_rayjien_q0260_06.htm"))
		{
			st.set("cond", "0");
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
		if(npcId == 30221)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 6 && st.getPlayer().getRace() == Race.elf)
				{
					htmltext = "sentinel_rayjien_q0260_02.htm";
					return htmltext;
				}
				if(st.getPlayer().getRace() != Race.elf)
				{
					htmltext = "sentinel_rayjien_q0260_00.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() < 6)
				{
					htmltext = "sentinel_rayjien_q0260_01.htm";
					st.exitCurrentQuest(true);
				}
				else if(cond == 1 && st.getQuestItemsCount(1114) == 0L && st.getQuestItemsCount(1115) == 0L)
					htmltext = "sentinel_rayjien_q0260_04.htm";
			}
			else if(cond == 1 && (st.getQuestItemsCount(1114) > 0L || st.getQuestItemsCount(1115) > 0L))
			{
				htmltext = "sentinel_rayjien_q0260_05.htm";
				int adenaPay = 0;
				if(st.getQuestItemsCount(1114) >= 40L)
					adenaPay += (int) (st.getQuestItemsCount(1114) * 14L);
				else
					adenaPay += (int) (st.getQuestItemsCount(1114) * 12L);
				if(st.getQuestItemsCount(1115) >= 40L)
					adenaPay += (int) (st.getQuestItemsCount(1115) * 40L);
				else
					adenaPay += (int) (st.getQuestItemsCount(1115) * 30L);
				st.giveItems(57, adenaPay, false);
				st.takeItems(1114, -1L);
				st.takeItems(1115, -1L);
				if(st.getPlayer().getClassId().getLevel() == 1 && !st.getPlayer().getVarBoolean("p1q2"))
				{
					st.getPlayer().setVar("p1q2", "1");
					st.getPlayer().sendPacket(new ExShowScreenMessage("Acquisition of Soulshot for beginners complete.\n                  Go find the Newbie Guide.", 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
					final QuestState qs = st.getPlayer().getQuestState(255);
					if(qs != null && qs.getInt("Ex") != 10)
					{
						st.showQuestionMark(26);
						qs.set("Ex", "10");
						if(st.getPlayer().isMageClass())
						{
							st.playTutorialVoice("tutorial_voice_027");
							st.giveItems(5790, 3000L);
						}
						else
						{
							st.playTutorialVoice("tutorial_voice_026");
							st.giveItems(5789, 6000L);
						}
					}
				}
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(st.getInt("cond") > 0)
			if(npcId == 20468 || npcId == 20469 || npcId == 20470)
				st.rollAndGive(1114, 1, 14.0);
			else if(npcId == 20471 || npcId == 20472 || npcId == 20473)
				st.rollAndGive(1115, 1, 14.0);
		return null;
	}
}
