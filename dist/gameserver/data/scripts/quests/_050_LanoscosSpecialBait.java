package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _050_LanoscosSpecialBait extends Quest implements ScriptFile
{
	int Lanosco;
	int SingingWind;
	int EssenceofWind;
	int WindFishingLure;
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

	public _050_LanoscosSpecialBait()
	{
		super(false);
		Lanosco = 31570;
		SingingWind = 21026;
		EssenceofWind = 7621;
		WindFishingLure = 7610;
		FishSkill = 1315;
		this.addStartNpc(Lanosco);
		this.addTalkId(new int[] { Lanosco });
		this.addKillId(new int[] { SingingWind });
		addQuestItem(new int[] { EssenceofWind });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("fisher_lanosco_q0050_0104.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("fisher_lanosco_q0050_0201.htm"))
			if(st.getQuestItemsCount(EssenceofWind) < 100L)
				htmltext = "fisher_lanosco_q0050_0202.htm";
			else
			{
				st.unset("cond");
				st.takeItems(EssenceofWind, -1L);
				st.giveItems(WindFishingLure, 4L);
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
		if(npcId == Lanosco)
			if(id == 1)
			{
				if(st.getPlayer().getLevel() < 27)
				{
					htmltext = "fisher_lanosco_q0050_0103.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getSkillLevel(FishSkill) >= 8)
					htmltext = "fisher_lanosco_q0050_0101.htm";
				else
				{
					htmltext = "fisher_lanosco_q0050_0102.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1 || cond == 2)
				if(st.getQuestItemsCount(EssenceofWind) < 100L)
				{
					htmltext = "fisher_lanosco_q0050_0106.htm";
					st.set("cond", "1");
				}
				else
					htmltext = "fisher_lanosco_q0050_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(npcId == SingingWind && st.getInt("cond") == 1 && st.getQuestItemsCount(EssenceofWind) < 100L && Rnd.chance(30))
		{
			st.giveItems(EssenceofWind, 1L);
			if(st.getQuestItemsCount(EssenceofWind) == 100L)
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
