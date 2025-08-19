package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _661_TheHarvestGroundsSafe extends Quest implements ScriptFile
{
	private static int NORMAN;
	private static int GIANT_POISON_BEE;
	private static int CLOYDY_BEAST;
	private static int YOUNG_ARANEID;
	private static int STING_OF_GIANT_POISON;
	private static int TALON_OF_YOUNG_ARANEID;
	private static int CLOUDY_GEM;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _661_TheHarvestGroundsSafe()
	{
		super(false);
		this.addStartNpc(_661_TheHarvestGroundsSafe.NORMAN);
		this.addKillId(new int[] { _661_TheHarvestGroundsSafe.GIANT_POISON_BEE });
		this.addKillId(new int[] { _661_TheHarvestGroundsSafe.CLOYDY_BEAST });
		this.addKillId(new int[] { _661_TheHarvestGroundsSafe.YOUNG_ARANEID });
		addQuestItem(new int[] { _661_TheHarvestGroundsSafe.STING_OF_GIANT_POISON });
		addQuestItem(new int[] { _661_TheHarvestGroundsSafe.TALON_OF_YOUNG_ARANEID });
		addQuestItem(new int[] { _661_TheHarvestGroundsSafe.CLOUDY_GEM });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("warehouse_keeper_norman_q0661_0103.htm") || event.equalsIgnoreCase("warehouse_keeper_norman_q0661_0201.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("warehouse_keeper_norman_q0661_0205.htm"))
		{
			final long STING = st.getQuestItemsCount(_661_TheHarvestGroundsSafe.STING_OF_GIANT_POISON);
			final long TALON = st.getQuestItemsCount(_661_TheHarvestGroundsSafe.TALON_OF_YOUNG_ARANEID);
			final long GEM = st.getQuestItemsCount(_661_TheHarvestGroundsSafe.CLOUDY_GEM);
			if(STING + GEM + TALON >= 10L)
			{
				st.giveItems(57, STING * 50L + GEM * 60L + TALON * 70L + 2800L);
				st.takeItems(_661_TheHarvestGroundsSafe.STING_OF_GIANT_POISON, -1L);
				st.takeItems(_661_TheHarvestGroundsSafe.TALON_OF_YOUNG_ARANEID, -1L);
				st.takeItems(_661_TheHarvestGroundsSafe.CLOUDY_GEM, -1L);
			}
			else
			{
				st.giveItems(57, STING * 50L + GEM * 60L + TALON * 70L);
				st.takeItems(_661_TheHarvestGroundsSafe.STING_OF_GIANT_POISON, -1L);
				st.takeItems(_661_TheHarvestGroundsSafe.TALON_OF_YOUNG_ARANEID, -1L);
				st.takeItems(_661_TheHarvestGroundsSafe.CLOUDY_GEM, -1L);
			}
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("warehouse_keeper_norman_q0661_0204.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
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
			if(st.getPlayer().getLevel() >= 21)
				htmltext = "warehouse_keeper_norman_q0661_0101.htm";
			else
			{
				htmltext = "warehouse_keeper_norman_q0661_0102.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 1)
			if(st.getQuestItemsCount(_661_TheHarvestGroundsSafe.STING_OF_GIANT_POISON) + st.getQuestItemsCount(_661_TheHarvestGroundsSafe.TALON_OF_YOUNG_ARANEID) + st.getQuestItemsCount(_661_TheHarvestGroundsSafe.CLOUDY_GEM) > 0L)
				htmltext = "warehouse_keeper_norman_q0661_0105.htm";
			else
				htmltext = "warehouse_keeper_norman_q0661_0206.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		if(st.getInt("cond") == 1)
		{
			if(npcId == _661_TheHarvestGroundsSafe.GIANT_POISON_BEE && Rnd.chance(75))
			{
				st.giveItems(_661_TheHarvestGroundsSafe.STING_OF_GIANT_POISON, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			if(npcId == _661_TheHarvestGroundsSafe.CLOYDY_BEAST && Rnd.chance(71))
			{
				st.giveItems(_661_TheHarvestGroundsSafe.CLOUDY_GEM, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			if(npcId == _661_TheHarvestGroundsSafe.YOUNG_ARANEID && Rnd.chance(67))
			{
				st.giveItems(_661_TheHarvestGroundsSafe.TALON_OF_YOUNG_ARANEID, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		return null;
	}

	static
	{
		_661_TheHarvestGroundsSafe.NORMAN = 30210;
		_661_TheHarvestGroundsSafe.GIANT_POISON_BEE = 21095;
		_661_TheHarvestGroundsSafe.CLOYDY_BEAST = 21096;
		_661_TheHarvestGroundsSafe.YOUNG_ARANEID = 21097;
		_661_TheHarvestGroundsSafe.STING_OF_GIANT_POISON = 8283;
		_661_TheHarvestGroundsSafe.TALON_OF_YOUNG_ARANEID = 8285;
		_661_TheHarvestGroundsSafe.CLOUDY_GEM = 8284;
	}
}
