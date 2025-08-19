package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _344_1000YearsEndofLamentation extends Quest implements ScriptFile
{
	private static final int ARTICLES_DEAD_HEROES = 4269;
	private static final int OLD_KEY = 4270;
	private static final int OLD_HILT = 4271;
	private static final int OLD_TOTEM = 4272;
	private static final int CRUCIFIX = 4273;
	private static final int CHANCE = 36;
	private static final int SPECIAL = 1000;
	private static final int GILMORE = 30754;
	private static final int RODEMAI = 30756;
	private static final int ORVEN = 30857;
	private static final int KAIEN = 30623;
	private static final int GARVARENTZ = 30704;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _344_1000YearsEndofLamentation()
	{
		super(true);
		this.addStartNpc(30754);
		this.addTalkId(new int[] { 30756 });
		this.addTalkId(new int[] { 30857 });
		this.addTalkId(new int[] { 30704 });
		this.addTalkId(new int[] { 30623 });
		for(int mob = 20236; mob < 20241; ++mob)
			this.addKillId(new int[] { mob });
		addQuestItem(new int[] { 4269, 4270, 4271, 4272, 4273 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final long amount = st.getQuestItemsCount(4269);
		final int cond = st.getInt("cond");
		final int level = st.getPlayer().getLevel();
		if(event.equalsIgnoreCase("30754-04.htm"))
		{
			if(level >= 48 && cond == 0)
			{
				st.setState(2);
				st.set("cond", "1");
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
			{
				htmltext = "noquest";
				st.exitCurrentQuest(true);
			}
		}
		else if(event.equalsIgnoreCase("30754-08.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(Quest.SOUND_FINISH);
		}
		else if(event.equalsIgnoreCase("30754-06.htm") && cond == 1)
		{
			if(amount == 0L)
				htmltext = "30754-06a.htm";
			else
			{
				if(Rnd.get((int) (1000.0f / st.getRateQuestsReward())) >= amount)
					st.giveItems(57, amount * 60L);
				else
				{
					htmltext = "30754-10.htm";
					st.set("ok", "1");
					st.set("amount", str(amount));
				}
				st.takeItems(4269, -1L);
			}
		}
		else if(event.equalsIgnoreCase("30754-11.htm") && cond == 1)
			if(st.getInt("ok") != 1)
				htmltext = "noquest";
			else
			{
				final int random = Rnd.get(100);
				st.set("cond", "2");
				st.unset("ok");
				if(random < 25)
				{
					htmltext = "30754-12.htm";
					st.giveItems(4270, 1L);
				}
				else if(random < 50)
				{
					htmltext = "30754-13.htm";
					st.giveItems(4271, 1L);
				}
				else if(random < 75)
				{
					htmltext = "30754-14.htm";
					st.giveItems(4272, 1L);
				}
				else
					st.giveItems(4273, 1L);
			}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int cond = st.getInt("cond");
		final long amount = st.getQuestItemsCount(4269);
		if(id == 1)
		{
			if(st.getPlayer().getLevel() >= 48)
				htmltext = "30754-02.htm";
			else
			{
				htmltext = "30754-01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == 30754 && cond == 1)
		{
			if(amount > 0L)
				htmltext = "30754-05.htm";
			else
				htmltext = "30754-09.htm";
		}
		else if(cond == 2)
		{
			if(npcId == 30754)
				htmltext = "30754-15.htm";
			else if(rewards(st, npcId))
			{
				htmltext = str(npcId) + "-01.htm";
				st.set("cond", "3");
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		else if(cond == 3)
			if(npcId == 30754)
			{
				final int amt = st.getInt("amount");
				final int mission = st.getInt("mission");
				int bonus = 0;
				if(mission == 1)
					bonus = 1500;
				else if(mission == 2)
					st.giveItems(4044, 1L);
				else if(mission == 3)
					st.giveItems(4043, 1L);
				else if(mission == 4)
					st.giveItems(4042, 1L);
				if(amt > 0)
				{
					st.unset("amount");
					st.giveItems(57, amt * 50 + bonus, true);
				}
				htmltext = "30754-16.htm";
				st.set("cond", "1");
				st.unset("mission");
			}
			else
				htmltext = str(npcId) + "-02.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1)
			st.rollAndGive(4269, 1, 36 + (npc.getNpcId() - 20234) * 2);
		return null;
	}

	private boolean rewards(final QuestState st, final int npcId)
	{
		boolean state = false;
		final int chance = Rnd.get(100);
		if(npcId == 30857 && st.getQuestItemsCount(4273) > 0L)
		{
			st.set("mission", "1");
			st.takeItems(4273, -1L);
			state = true;
			if(chance < 50)
				st.giveItems(1875, 19L);
			else if(chance < 70)
				st.giveItems(952, 5L);
			else
				st.giveItems(2437, 1L);
		}
		else if(npcId == 30704 && st.getQuestItemsCount(4272) > 0L)
		{
			st.set("mission", "2");
			st.takeItems(4272, -1L);
			state = true;
			if(chance < 45)
				st.giveItems(1882, 70L);
			else if(chance < 95)
				st.giveItems(1881, 50L);
			else
				st.giveItems(191, 1L);
		}
		else if(npcId == 30623 && st.getQuestItemsCount(4271) > 0L)
		{
			st.set("mission", "3");
			st.takeItems(4271, -1L);
			state = true;
			if(chance < 50)
				st.giveItems(1874, 25L);
			else if(chance < 75)
				st.giveItems(1887, 10L);
			else if(chance < 99)
				st.giveItems(951, 1L);
			else
				st.giveItems(133, 1L);
		}
		else if(npcId == 30756 && st.getQuestItemsCount(4270) > 0L)
		{
			st.set("mission", "4");
			st.takeItems(4270, -1L);
			state = true;
			if(chance < 40)
				st.giveItems(1879, 55L);
			else if(chance < 90)
				st.giveItems(951, 1L);
			else
				st.giveItems(885, 1L);
		}
		return state;
	}
}
