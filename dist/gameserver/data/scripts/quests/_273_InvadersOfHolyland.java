package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.ScriptFile;

public class _273_InvadersOfHolyland extends Quest implements ScriptFile
{
	public final int BLACK_SOULSTONE = 1475;
	public final int RED_SOULSTONE = 1476;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _273_InvadersOfHolyland()
	{
		super(false);
		this.addStartNpc(30566);
		this.addKillId(new int[] { 20311, 20312, 20313 });
		addQuestItem(new int[] { 1475, 1476 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("atuba_chief_varkees_q0273_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("atuba_chief_varkees_q0273_07.htm"))
		{
			st.set("cond", "0");
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equals("atuba_chief_varkees_q0273_08.htm"))
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
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getRace() != Race.orc)
			{
				htmltext = "atuba_chief_varkees_q0273_00.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				if(st.getPlayer().getLevel() >= 6)
				{
					htmltext = "atuba_chief_varkees_q0273_02.htm";
					return htmltext;
				}
				htmltext = "atuba_chief_varkees_q0273_01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond > 0)
			if(st.getQuestItemsCount(1475) == 0L && st.getQuestItemsCount(1476) == 0L)
				htmltext = "atuba_chief_varkees_q0273_04.htm";
			else
			{
				long adena = 0L;
				if(st.getQuestItemsCount(1475) > 0L)
				{
					htmltext = "atuba_chief_varkees_q0273_05.htm";
					adena += st.getQuestItemsCount(1475) * 5L;
				}
				if(st.getQuestItemsCount(1476) > 0L)
				{
					htmltext = "atuba_chief_varkees_q0273_06.htm";
					adena += st.getQuestItemsCount(1476) * 50L;
				}
				st.takeAllItems(new int[] { 1475, 1476 });
				st.giveItems(57, adena);
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
				st.exitCurrentQuest(true);
				st.playSound(Quest.SOUND_FINISH);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 20311)
		{
			if(cond == 1)
			{
				if(Rnd.chance(90))
					st.giveItems(1475, 1L);
				else
					st.giveItems(1476, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if(npcId == 20312)
		{
			if(cond == 1)
			{
				if(Rnd.chance(87))
					st.giveItems(1475, 1L);
				else
					st.giveItems(1476, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if(npcId == 20313 && cond == 1)
		{
			if(Rnd.chance(77))
				st.giveItems(1475, 1L);
			else
				st.giveItems(1476, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
