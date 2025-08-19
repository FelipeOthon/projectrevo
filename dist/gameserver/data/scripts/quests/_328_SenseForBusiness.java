package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _328_SenseForBusiness extends Quest implements ScriptFile
{
	private int SARIEN;
	private int MONSTER_EYE_CARCASS;
	private int MONSTER_EYE_LENS;
	private int BASILISK_GIZZARD;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _328_SenseForBusiness()
	{
		super(false);
		SARIEN = 30436;
		MONSTER_EYE_CARCASS = 1347;
		MONSTER_EYE_LENS = 1366;
		BASILISK_GIZZARD = 1348;
		this.addStartNpc(SARIEN);
		this.addKillId(new int[] { 20055 });
		this.addKillId(new int[] { 20059 });
		this.addKillId(new int[] { 20067 });
		this.addKillId(new int[] { 20068 });
		this.addKillId(new int[] { 20070 });
		this.addKillId(new int[] { 20072 });
		addQuestItem(new int[] { MONSTER_EYE_CARCASS });
		addQuestItem(new int[] { MONSTER_EYE_LENS });
		addQuestItem(new int[] { BASILISK_GIZZARD });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("trader_salient_q0328_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("trader_salient_q0328_06.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int id = st.getState();
		if(id == 1)
			st.set("cond", "0");
		String htmltext;
		if(st.getInt("cond") == 0)
		{
			if(st.getPlayer().getLevel() >= 21)
			{
				htmltext = "trader_salient_q0328_02.htm";
				return htmltext;
			}
			htmltext = "trader_salient_q0328_01.htm";
			st.exitCurrentQuest(true);
		}
		else
		{
			final long carcass = st.getQuestItemsCount(MONSTER_EYE_CARCASS);
			final long lenses = st.getQuestItemsCount(MONSTER_EYE_LENS);
			final long gizzard = st.getQuestItemsCount(BASILISK_GIZZARD);
			if(carcass + lenses + gizzard > 0L)
			{
				st.giveItems(57, 30L * carcass + 2000L * lenses + 75L * gizzard);
				st.takeItems(MONSTER_EYE_CARCASS, -1L);
				st.takeItems(MONSTER_EYE_LENS, -1L);
				st.takeItems(BASILISK_GIZZARD, -1L);
				htmltext = "trader_salient_q0328_05.htm";
			}
			else
				htmltext = "trader_salient_q0328_04.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int n = Rnd.get(1, 100);
		if(npcId == 20055)
		{
			if(n < 47)
			{
				st.giveItems(MONSTER_EYE_CARCASS, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			else if(n < 49)
			{
				st.giveItems(MONSTER_EYE_LENS, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if(npcId == 20059)
		{
			if(n < 51)
			{
				st.giveItems(MONSTER_EYE_CARCASS, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			else if(n < 53)
			{
				st.giveItems(MONSTER_EYE_LENS, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if(npcId == 20067)
		{
			if(n < 67)
			{
				st.giveItems(MONSTER_EYE_CARCASS, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			else if(n < 69)
			{
				st.giveItems(MONSTER_EYE_LENS, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if(npcId == 20068)
		{
			if(n < 75)
			{
				st.giveItems(MONSTER_EYE_CARCASS, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			else if(n < 77)
			{
				st.giveItems(MONSTER_EYE_LENS, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if(npcId == 20070)
		{
			if(n < 50)
			{
				st.giveItems(BASILISK_GIZZARD, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if(npcId == 20072 && n < 51)
		{
			st.giveItems(BASILISK_GIZZARD, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
