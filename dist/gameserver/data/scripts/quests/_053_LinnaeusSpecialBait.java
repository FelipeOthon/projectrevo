package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _053_LinnaeusSpecialBait extends Quest implements ScriptFile
{
	int Linnaeu;
	int CrimsonDrake;
	int HeartOfCrimsonDrake;
	int FlameFishingLure;
	Integer FishSkill;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _053_LinnaeusSpecialBait()
	{
		super(false);
		Linnaeu = 31577;
		CrimsonDrake = 20670;
		HeartOfCrimsonDrake = 7624;
		FlameFishingLure = 7613;
		FishSkill = 1315;
		this.addStartNpc(Linnaeu);
		this.addTalkId(new int[] { Linnaeu });
		this.addKillId(new int[] { CrimsonDrake });
		addQuestItem(new int[] { HeartOfCrimsonDrake });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("fisher_linneaus_q0053_0104.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("fisher_linneaus_q0053_0201.htm"))
			if(st.getQuestItemsCount(HeartOfCrimsonDrake) < 100L)
				htmltext = "fisher_linneaus_q0053_0202.htm";
			else
			{
				st.unset("cond");
				st.takeItems(HeartOfCrimsonDrake, -1L);
				st.giveItems(FlameFishingLure, 4L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		final int id = st.getState();
		if(npcId == Linnaeu)
			if(id == 1)
			{
				if(st.getPlayer().getLevel() < 60)
				{
					htmltext = "fisher_linneaus_q0053_0103.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getSkillLevel(FishSkill) >= 21)
					htmltext = "fisher_linneaus_q0053_0101.htm";
				else
				{
					htmltext = "fisher_linneaus_q0053_0102.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1 || cond == 2)
				if(st.getQuestItemsCount(HeartOfCrimsonDrake) < 100L)
				{
					htmltext = "fisher_linneaus_q0053_0106.htm";
					st.set("cond", "1");
				}
				else
					htmltext = "fisher_linneaus_q0053_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(npcId == CrimsonDrake && st.getInt("cond") == 1 && st.getQuestItemsCount(HeartOfCrimsonDrake) < 100L && Rnd.chance(30))
		{
			st.giveItems(HeartOfCrimsonDrake, 1L);
			if(st.getQuestItemsCount(HeartOfCrimsonDrake) == 100L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
