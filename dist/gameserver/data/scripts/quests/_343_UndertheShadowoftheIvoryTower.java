package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _343_UndertheShadowoftheIvoryTower extends Quest implements ScriptFile
{
	public final int CEMA = 30834;
	public final int ICARUS = 30835;
	public final int MARSHA = 30934;
	public final int TRUMPIN = 30935;
	public final int[] MOBS;
	public final int ORB = 4364;
	public final int ECTOPLASM = 4365;
	public final int[] AllowClass;
	public final int CHANCE = 50;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _343_UndertheShadowoftheIvoryTower()
	{
		super(false);
		MOBS = new int[] { 20563, 20564, 20565, 20566 };
		AllowClass = new int[] { 11, 12, 13, 14, 26, 27, 28, 39, 40, 41 };
		this.addStartNpc(30834);
		this.addTalkId(new int[] { 30834 });
		this.addTalkId(new int[] { 30835 });
		this.addTalkId(new int[] { 30934 });
		this.addTalkId(new int[] { 30935 });
		for(final int i : MOBS)
			this.addKillId(new int[] { i });
		addQuestItem(new int[] { 4364 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int random1 = Rnd.get(3);
		final int random2 = Rnd.get(2);
		final long orbs = st.getQuestItemsCount(4364);
		if(event.equalsIgnoreCase("30834-03.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30834-08.htm"))
		{
			if(orbs > 0L)
			{
				st.giveItems(57, orbs * 120L);
				st.takeItems(4364, -1L);
			}
			else
				htmltext = "30834-08.htm";
		}
		else if(event.equalsIgnoreCase("30834-09.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("30934-02.htm") || event.equalsIgnoreCase("30934-03.htm"))
		{
			if(orbs < 10L)
				htmltext = "noorbs.htm";
			else if(event.equalsIgnoreCase("30934-03.htm"))
				if(orbs >= 10L)
				{
					st.takeItems(4364, 10L);
					st.set("playing", "1");
				}
				else
					htmltext = "noorbs.htm";
		}
		else if(event.equalsIgnoreCase("30934-04.htm"))
		{
			if(st.getInt("playing") > 0)
			{
				if(random1 == 0)
				{
					htmltext = "30934-05.htm";
					st.giveItems(4364, 10L);
				}
				else if(random1 == 1)
					htmltext = "30934-06.htm";
				else
				{
					htmltext = "30934-04.htm";
					st.giveItems(4364, 20L);
				}
				st.unset("playing");
			}
			else
			{
				htmltext = "Player is cheating";
				st.takeItems(4364, -1L);
				st.exitCurrentQuest(true);
			}
		}
		else if(event.equalsIgnoreCase("30934-05.htm"))
		{
			if(st.getInt("playing") > 0)
			{
				if(random1 == 0)
				{
					htmltext = "30934-04.htm";
					st.giveItems(4364, 20L);
				}
				else if(random1 == 1)
				{
					htmltext = "30934-05.htm";
					st.giveItems(4364, 10L);
				}
				else
					htmltext = "30934-06.htm";
				st.unset("playing");
			}
			else
			{
				htmltext = "Player is cheating";
				st.takeItems(4364, -1L);
				st.exitCurrentQuest(true);
			}
		}
		else if(event.equalsIgnoreCase("30934-06.htm"))
		{
			if(st.getInt("playing") > 0)
			{
				if(random1 == 0)
				{
					htmltext = "30934-04.htm";
					st.giveItems(4364, 20L);
				}
				else if(random1 == 1)
					htmltext = "30934-06.htm";
				else
				{
					htmltext = "30934-05.htm";
					st.giveItems(4364, 10L);
				}
				st.unset("playing");
			}
			else
			{
				htmltext = "Player is cheating";
				st.takeItems(4364, -1L);
				st.exitCurrentQuest(true);
			}
		}
		else if(event.equalsIgnoreCase("30935-02.htm") || event.equalsIgnoreCase("30935-03.htm"))
		{
			st.unset("toss");
			if(orbs < 10L)
				htmltext = "noorbs.htm";
		}
		else if(event.equalsIgnoreCase("30935-05.htm"))
		{
			if(orbs >= 10L)
			{
				if(random2 == 0)
				{
					final int toss = st.getInt("toss");
					if(toss == 4)
					{
						st.unset("toss");
						st.giveItems(4364, 150L);
						htmltext = "30935-07.htm";
					}
					else
					{
						st.set("toss", String.valueOf(toss + 1));
						htmltext = "30935-04.htm";
					}
				}
				else
				{
					st.unset("toss");
					st.takeItems(4364, 10L);
				}
			}
			else
				htmltext = "noorbs.htm";
		}
		else if(event.equalsIgnoreCase("30935-06.htm"))
		{
			if(orbs >= 10L)
			{
				final int toss = st.getInt("toss");
				st.unset("toss");
				if(toss == 1)
					st.giveItems(4364, 10L);
				else if(toss == 2)
					st.giveItems(4364, 30L);
				else if(toss == 3)
					st.giveItems(4364, 70L);
				else if(toss == 4)
					st.giveItems(4364, 150L);
			}
			else
				htmltext = "noorbs.htm";
		}
		else if(event.equalsIgnoreCase("30835-02.htm"))
			if(st.getQuestItemsCount(4365) > 0L)
			{
				st.takeItems(4365, 1L);
				final int random3 = Rnd.get(1000);
				if(random3 <= 119)
					st.giveItems(955, 1L);
				else if(random3 <= 169)
					st.giveItems(951, 1L);
				else if(random3 <= 329)
					st.giveItems(2511, Rnd.get(200) + 401);
				else if(random3 <= 559)
					st.giveItems(2510, Rnd.get(200) + 401);
				else if(random3 <= 561)
					st.giveItems(316, 1L);
				else if(random3 <= 578)
					st.giveItems(630, 1L);
				else if(random3 <= 579)
					st.giveItems(188, 1L);
				else if(random3 <= 581)
					st.giveItems(885, 1L);
				else if(random3 <= 582)
					st.giveItems(103, 1L);
				else if(random3 <= 584)
					st.giveItems(917, 1L);
				else
					st.giveItems(736, 1L);
			}
			else
				htmltext = "30835-03.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		if(npcId == 30834)
		{
			if(id != 2)
			{
				for(final int i : AllowClass)
					if(st.getPlayer().getClassId().getId() == i && st.getPlayer().getLevel() >= 40)
						htmltext = "30834-01.htm";
				if(htmltext != "30834-01.htm")
				{
					htmltext = "30834-07.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(st.getQuestItemsCount(4364) > 0L)
				htmltext = "30834-06.htm";
			else
				htmltext = "30834-05.htm";
		}
		else if(npcId == 30835)
			htmltext = "30835-01.htm";
		else if(npcId == 30934)
			htmltext = "30934-01.htm";
		else if(npcId == 30935)
			htmltext = "30935-01.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(Rnd.chance(50))
		{
			st.giveItems(4364, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
