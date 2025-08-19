package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _052_WilliesSpecialBait extends Quest implements ScriptFile
{
	private static final int Willie = 31574;
	private static final int[] TarlkBasilisks;
	private static final int EyeOfTarlkBasilisk = 7623;
	private static final int EarthFishingLure = 7612;
	private static final Integer FishSkill;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _052_WilliesSpecialBait()
	{
		super(false);
		this.addStartNpc(31574);
		this.addKillId(_052_WilliesSpecialBait.TarlkBasilisks);
		addQuestItem(new int[] { 7623 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("fisher_willeri_q0052_0104.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("fisher_willeri_q0052_0201.htm"))
			if(st.getQuestItemsCount(7623) < 100L)
				htmltext = "fisher_willeri_q0052_0202.htm";
			else
			{
				st.unset("cond");
				st.takeItems(7623, -1L);
				st.giveItems(7612, 4L);
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
		if(npcId == 31574)
			if(id == 1)
			{
				if(st.getPlayer().getLevel() < 48)
				{
					htmltext = "fisher_willeri_q0052_0103.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getSkillLevel(_052_WilliesSpecialBait.FishSkill) >= 16)
					htmltext = "fisher_willeri_q0052_0101.htm";
				else
				{
					htmltext = "fisher_willeri_q0052_0102.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1 || cond == 2)
				if(st.getQuestItemsCount(7623) < 100L)
				{
					htmltext = "fisher_willeri_q0052_0106.htm";
					st.set("cond", "1");
				}
				else
					htmltext = "fisher_willeri_q0052_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if((npcId == _052_WilliesSpecialBait.TarlkBasilisks[0] || npcId == _052_WilliesSpecialBait.TarlkBasilisks[1] && st.getInt("cond") == 1) && st.getQuestItemsCount(7623) < 100L && Rnd.chance(30))
		{
			st.giveItems(7623, 1L);
			if(st.getQuestItemsCount(7623) == 100L)
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
		TarlkBasilisks = new int[] { 20573, 20574 };
		FishSkill = 1315;
	}
}
