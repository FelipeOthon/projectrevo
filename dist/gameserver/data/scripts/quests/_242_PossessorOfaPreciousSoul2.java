package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _242_PossessorOfaPreciousSoul2 extends Quest implements ScriptFile
{
	private static final int VIRGILS_LETTER_1_PART = 7677;
	private static final int BLONDE_STRAND = 7590;
	private static final int SORCERY_INGREDIENT = 7596;
	private static final int CARADINE_LETTER = 7678;
	private static final int ORB_OF_BINDING = 7595;
	private static final int PureWhiteUnicorn = 31747;
	private NpcInstance PureWhiteUnicornSpawn;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _242_PossessorOfaPreciousSoul2()
	{
		super(false);
		PureWhiteUnicornSpawn = null;
		this.addStartNpc(31742);
		this.addTalkId(new int[] { 31743 });
		this.addTalkId(new int[] { 31751 });
		this.addTalkId(new int[] { 31752 });
		this.addTalkId(new int[] { 30759 });
		this.addTalkId(new int[] { 30738 });
		this.addTalkId(new int[] { 31744 });
		this.addTalkId(new int[] { 31748 });
		this.addTalkId(new int[] { 31747 });
		this.addTalkId(new int[] { 31746 });
		this.addKillId(new int[] { 27317 });
		addQuestItem(new int[] { 7595, 7596, 7590 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("31742-2.htm"))
		{
			st.setCond(1);
			st.set("CoRObjId", "0");
			st.takeItems(7677, 1L);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("31743-5.htm"))
		{
			st.setCond(2);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("31744-2.htm"))
		{
			st.setCond(3);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("31751-2.htm"))
		{
			st.setCond(4);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30759-2.htm"))
		{
			st.takeItems(7590, 1L);
			st.setCond(7);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30759-4.htm"))
		{
			st.setCond(9);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30738-2.htm"))
		{
			st.setCond(8);
			st.giveItems(7596, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("31748-2.htm"))
		{
			st.takeItems(7595, 1L);
			st.killNpcByObjectId(st.getInt("CoRObjId"));
			st.set("talk", "0");
			if(st.getInt("prog") < 4)
			{
				st.set("prog", str(st.getInt("prog") + 1));
				st.playSound(Quest.SOUND_MIDDLE);
			}
			if(st.getInt("prog") == 4)
			{
				st.setCond(10);
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(!st.getPlayer().isSubClassActive())
			return "Subclass only!";
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getCond();
		if(npcId == 31742)
		{
			if(cond == 0)
			{
				final QuestState previous = st.getPlayer().getQuestState(241);
				if(previous != null && previous.getState() == 3 && st.getPlayer().getLevel() >= 60)
					htmltext = "31742-1.htm";
				else
				{
					htmltext = "31742-0.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "31742-2r.htm";
		}
		else if(npcId == 31743)
		{
			if(cond == 1)
				htmltext = "31743-1.htm";
			else if(cond == 2)
				htmltext = "31743-2r.htm";
			else if(cond == 11)
			{
				htmltext = "31743-6.htm";
				st.giveItems(7678, 1L);
				st.addExpAndSp(455764L, 0L);
				st.unset("cond");
				st.unset("CoRObjId");
				st.unset("prog");
				st.unset("talk");
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 31744)
		{
			if(cond == 2)
				htmltext = "31744-1.htm";
			else if(cond == 3)
				htmltext = "31744-2r.htm";
		}
		else if(npcId == 31751)
		{
			if(cond == 3)
				htmltext = "31751-1.htm";
			else if(cond == 4)
				htmltext = "31751-2r.htm";
			else if(cond == 5 && st.getQuestItemsCount(7590) == 1L)
			{
				st.set("cond", "6");
				htmltext = "31751-3.htm";
			}
			else if(cond == 6 && st.getQuestItemsCount(7590) == 1L)
				htmltext = "31751-3r.htm";
		}
		else if(npcId == 31752)
		{
			if(cond == 4)
			{
				st.giveItems(7590, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
				st.set("cond", "5");
				htmltext = "31752-2.htm";
			}
			else
				htmltext = "31752-n.htm";
		}
		else if(npcId == 30759)
		{
			if(cond == 6 && st.getQuestItemsCount(7590) == 1L)
				htmltext = "30759-1.htm";
			else if(cond == 7)
				htmltext = "30759-2r.htm";
			else if(cond == 8 && st.getQuestItemsCount(7596) == 1L)
				htmltext = "30759-3.htm";
		}
		else if(npcId == 30738)
		{
			if(cond == 7)
				htmltext = "30738-1.htm";
			else if(cond == 8)
				htmltext = "30738-2r.htm";
		}
		else if(npcId == 31748)
		{
			if(cond == 9)
				if(st.getQuestItemsCount(7595) >= 1L)
				{
					if(npc.getObjectId() != st.getInt("CoRObjId"))
					{
						st.set("CoRObjId", str(npc.getObjectId()));
						st.set("talk", "1");
						htmltext = "31748-1.htm";
					}
					else if(st.getInt("talk") == 1)
						htmltext = "31748-1.htm";
					else
						htmltext = "noquest";
				}
				else
					htmltext = "31748-0.htm";
		}
		else if(npcId == 31746)
		{
			if(st.getCond() == 9)
				htmltext = "31746-1.htm";
			else if(st.getCond() == 10)
			{
				htmltext = "31746-1.htm";
				npc.doDie(npc);
				if(PureWhiteUnicornSpawn == null || !st.getPlayer().knowsObject(PureWhiteUnicornSpawn) || !PureWhiteUnicornSpawn.isVisible())
					PureWhiteUnicornSpawn = st.addSpawn(31747, npc.getX() + 10, npc.getY(), npc.getZ(), 120000);
			}
			else
				htmltext = "noquest";
		}
		else if(npcId == 31747)
			if(st.getCond() == 10)
			{
				htmltext = "31747-1.htm";
				st.setCond(11);
			}
			else if(st.getCond() == 11)
				htmltext = "31747-2.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(!st.getPlayer().isSubClassActive())
			return null;
		if(st.getCond() == 9 && st.getQuestItemsCount(7595) < 4L)
			st.giveItems(7595, 1L);
		if(st.getQuestItemsCount(7595) < 4L)
			st.playSound(Quest.SOUND_ITEMGET);
		else
			st.playSound(Quest.SOUND_MIDDLE);
		return null;
	}
}
