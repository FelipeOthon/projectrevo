package quests;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Util;

public class _509_TheClansPrestige extends Quest implements ScriptFile
{
	private static final int GRAND_MAGISTER_VALDIS = 31331;
	private static final int DAIMONS_EYES = 8489;
	private static final int HESTIAS_FAIRY_STONE = 8490;
	private static final int NUCLEUS_OF_LESSER_GOLEM = 8491;
	private static final int FALSTONS_FANG = 8492;
	private static final int DAIMON_THE_WHITE_EYED = 25290;
	private static final int HESTIA_GUARDIAN_DEITY = 25293;
	private static final int PLAGUE_GOLEM = 25523;
	private static final int DEMONS_AGENT_FALSTON = 25322;
	private static final int CLAN_POINTS_REWARD = 150;
	private static final int[][] REWARDS_LIST;
	private static final int[][] RADAR;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _509_TheClansPrestige()
	{
		super(2);
		this.addStartNpc(31331);
		for(final int[] i : _509_TheClansPrestige.REWARDS_LIST)
		{
			if(i[0] > 0)
				this.addKillId(new int[] { i[0] });
			if(i[1] > 0)
				addQuestItem(new int[] { i[1] });
		}
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int cond = st.getInt("cond");
		String htmltext = event;
		if(event.equalsIgnoreCase("31331-0.htm") && cond == 0)
		{
			st.set("cond", "1");
			st.setState(2);
		}
		else if(Util.isNumber(event))
		{
			final int evt = Integer.parseInt(event);
			st.set("raid", event);
			htmltext = "31331-" + event + ".htm";
			final int x = _509_TheClansPrestige.RADAR[evt][0];
			final int y = _509_TheClansPrestige.RADAR[evt][1];
			final int z = _509_TheClansPrestige.RADAR[evt][2];
			if(x + y + z > 0)
				st.addRadar(x, y, z);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("31331-6.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final Clan clan = st.getPlayer().getClan();
		if(clan == null)
		{
			st.exitCurrentQuest(true);
			htmltext = "31331-0a.htm";
		}
		else if(clan.getLeader().getPlayer() != st.getPlayer())
		{
			st.exitCurrentQuest(true);
			htmltext = "31331-0a.htm";
		}
		else if(clan.getLevel() < 6)
		{
			st.exitCurrentQuest(true);
			htmltext = "31331-0b.htm";
		}
		else
		{
			final int cond = st.getInt("cond");
			final int raid = st.getInt("raid");
			final int id = st.getState();
			if(id == 1 && cond == 0)
				htmltext = "31331-0c.htm";
			else if(id == 2 && cond == 1)
			{
				final int item = _509_TheClansPrestige.REWARDS_LIST[raid][1];
				final long count = st.getQuestItemsCount(item);
				if(count == 0L)
					htmltext = "31331-" + raid + "a.htm";
				else if(count == 1L)
				{
					htmltext = "31331-" + raid + "b.htm";
					st.getPlayer().sendPacket(new SystemMessage(1777).addNumber(clan.incReputation(150, true, "_509_TheClansPrestige")));
					st.takeItems(item, 1L);
				}
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		QuestState id = null;
		final Clan clan = st.getPlayer().getClan();
		if(clan == null)
			return null;
		final Player clan_leader = clan.getLeader().getPlayer();
		if(clan_leader == null)
			return null;
		if(clan_leader.equals(st.getPlayer()) || clan_leader.getDistance(npc) <= 1600.0)
			id = clan_leader.getQuestState(getId());
		if(id == null)
			return null;
		if(st.getInt("cond") == 1 && st.getState() == 2)
		{
			final int raid = _509_TheClansPrestige.REWARDS_LIST[st.getInt("raid")][0];
			final int item = _509_TheClansPrestige.REWARDS_LIST[st.getInt("raid")][1];
			final int npcId = npc.getNpcId();
			if(npcId == raid && st.getQuestItemsCount(item) == 0L)
			{
				st.giveItems(item, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		return null;
	}

	static
	{
		REWARDS_LIST = new int[][] { { 0, 0 }, { 25290, 8489 }, { 25293, 8490 }, { 25523, 8491 }, { 25322, 8492 } };
		RADAR = new int[][] { { 0, 0, 0 }, { 186304, -43744, -3193 }, { 134672, -115600, -1216 }, { 168641, -60417, -3888 }, { 93296, -75104, -1824 } };
	}
}
