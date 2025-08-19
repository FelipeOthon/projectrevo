package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.model.quest.QuestTimer;
import l2s.gameserver.scripts.ScriptFile;

public class _024_InhabitantsOfTheForestOfTheDead extends Quest implements ScriptFile
{
	private final int DORIAN = 31389;
	private final int TOMBSTONE = 31531;
	private final int MAID_OF_LIDIA = 31532;
	private final int MYSTERIOUS_WIZARD = 31522;
	private final int LIDIA_HAIR_PIN = 7148;
	private final int SUSPICIOUS_TOTEM_DOLL = 7151;
	private final int FLOWER_BOUQUET = 7152;
	private final int SILVER_CROSS_OF_EINHASAD = 7153;
	private final int BROKEN_SILVER_CROSS_OF_EINHASAD = 7154;
	private final int SUSPICIOUS_TOTEM_DOLL1 = 7156;
	private final int[] MOBS;
	private final int[] VAMPIRE;

	public _024_InhabitantsOfTheForestOfTheDead()
	{
		super(false);
		MOBS = new int[] { 21557, 21558, 21560, 21561, 21562, 21563, 21564, 21565, 21566, 21567 };
		VAMPIRE = new int[] { 25328, 25329, 25330, 25331, 25332 };
		this.addStartNpc(31389);
		this.addTalkId(new int[] { 31531 });
		this.addTalkId(new int[] { 31532 });
		this.addTalkId(new int[] { 31522 });
		for(final int npcId : MOBS)
			this.addKillId(new int[] { npcId });
		addQuestItem(new int[] { 7151 });
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState qs)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = qs.getInt("cond");
		if(npcId == 31389)
		{
			if(cond == 0)
			{
				final QuestState LidiasHeart = qs.getPlayer().getQuestState(23);
				if(LidiasHeart != null)
				{
					if(LidiasHeart.isCompleted())
						htmltext = "31389-01.htm";
					else
						htmltext = "31389-02.htm";
				}
				else
					htmltext = "31389-02.htm";
			}
			else if(cond == 1 && qs.getQuestItemsCount(7152) == 1L)
				htmltext = "31389-04.htm";
			else if(cond == 2 && qs.getQuestItemsCount(7152) == 0L)
				htmltext = "31389-05.htm";
			else if(cond == 3 && qs.getQuestItemsCount(7153) == 1L)
				htmltext = "31389-14.htm";
			else if(cond == 4 && qs.getQuestItemsCount(7154) == 1L)
			{
				htmltext = "31389-15.htm";
				qs.takeItems(7154, -1L);
			}
			else if(cond == 7 && qs.getQuestItemsCount(7148) == 0L)
			{
				htmltext = "31389-21.htm";
				qs.giveItems(7148, 1L);
				qs.set("cond", "8");
			}
		}
		else if(npcId == 31531)
		{
			if(cond == 1 && qs.getQuestItemsCount(7152) == 1L)
				htmltext = "31531-01.htm";
			else if(cond == 2 && qs.getQuestItemsCount(7152) == 0L)
				htmltext = "31531-03.htm";
		}
		else if(npcId == 31532)
		{
			if(cond == 5)
				htmltext = "31532-01.htm";
			else if(cond == 6)
			{
				if(qs.getQuestItemsCount(7148) == 0L)
				{
					htmltext = "31532-07.htm";
					qs.set("cond", "7");
				}
				else
					htmltext = "31532-05.htm";
			}
			else if(cond == 8 && qs.getQuestItemsCount(7148) != 0L)
				htmltext = "31532-10.htm";
			qs.takeItems(7148, -1L);
		}
		else if(npcId == 31522)
		{
			final QuestTimer timer_speak = qs.getQuestTimer("To talk with Mystik");
			if(cond == 10 && qs.getQuestItemsCount(7151) != 0L)
				htmltext = "31522-01.htm";
			else if(cond == 11 && timer_speak == null && qs.getQuestItemsCount(7156) == 0L)
				htmltext = "31522-09.htm";
			else if(cond == 11 && qs.getQuestItemsCount(7156) != 0L)
				htmltext = "31522-22.htm";
		}
		return htmltext;
	}

	@Override
	public String onEvent(String event, final QuestState qs, final NpcInstance npc)
	{
		if(event.startsWith("playerInMobRange"))
		{
			if(qs.getInt("cond") == 3)
			{
				final String[] arr = event.split("_");
				final int id = Integer.parseInt(arr[1]);
				if(arrayContains(VAMPIRE, id))
				{
					qs.takeItems(7148, -1L);
					qs.takeItems(7153, -1L);
					qs.giveItems(7154, 1L);
					qs.playSound(Quest.SOUND_HORROR2);
					qs.set("cond", "4");
				}
			}
			event = null;
		}
		else if(event.equalsIgnoreCase("31389-03.htm"))
		{
			qs.giveItems(7152, 1L);
			qs.set("cond", "1");
			qs.setState(2);
			qs.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("31531-02.htm"))
		{
			qs.takeItems(7152, -1L);
			qs.set("cond", "2");
		}
		else if(event.equalsIgnoreCase("31389-13.htm"))
		{
			qs.giveItems(7153, 1L);
			qs.set("cond", "3");
		}
		else if(event.equalsIgnoreCase("31389-19.htm"))
			qs.set("cond", "5");
		else if(event.equalsIgnoreCase("31532-04.htm"))
		{
			qs.set("cond", "6");
			qs.startQuestTimer("Lidias Letter", 7000L);
		}
		else
		{
			if(event.equalsIgnoreCase("Lidias Letter"))
				return "lidias_letter.htm";
			if(event.equalsIgnoreCase("31532-06.htm"))
				qs.takeItems(7148, -1L);
			else if(event.equalsIgnoreCase("31532-19.htm"))
				qs.set("cond", "9");
			else if(event.equalsIgnoreCase("31522-03.htm"))
				qs.takeItems(7151, -1L);
			else if(event.equalsIgnoreCase("31522-08.htm"))
			{
				qs.set("cond", "11");
				qs.startQuestTimer("To talk with Mystik", 600000L);
			}
			else
			{
				if(event.equalsIgnoreCase("31522-21.htm"))
				{
					qs.giveItems(7156, 1L);
					qs.startQuestTimer("html", 5L);
					return "Congratulations! You are completed this quest! \n The Quest \"Hiding Behind the Truth\" become available.\n Show Suspicious Totem Doll to  Priest Benedict.";
				}
				if(event.equalsIgnoreCase("html"))
				{
					qs.playSound(Quest.SOUND_FINISH);
					qs.addExpAndSp(242105L, 22529L, true);
					qs.exitCurrentQuest(false);
					return "31522-22.htm";
				}
			}
		}
		return event;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs == null)
			return null;
		if(qs.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		final int cond = qs.getInt("cond");
		if(arrayContains(MOBS, npcId) && cond == 9 && Rnd.chance(70))
		{
			qs.giveItems(7151, 1L);
			qs.playSound(Quest.SOUND_MIDDLE);
			qs.set("cond", "10");
		}
		return null;
	}

	private boolean arrayContains(final int[] array, final int id)
	{
		for(final int i : array)
			if(i == id)
				return true;
		return false;
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	@Override
	public void onAbort(final QuestState st)
	{
		if(st.getQuestItemsCount(7148) == 0L)
			st.giveItems(7148, 1L);
	}
}
