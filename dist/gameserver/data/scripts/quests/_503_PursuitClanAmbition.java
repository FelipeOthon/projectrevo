package quests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.commons.dbcp.DbUtils;
import l2s.commons.util.Rnd;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.quest.Drop;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class _503_PursuitClanAmbition extends Quest implements ScriptFile
{
	private final short G_Let_Martien = 3866;
	private final short Th_Wyrm_Eggs = 3842;
	private final short Drake_Eggs = 3841;
	private final short Bl_Wyrm_Eggs = 3840;
	private final short Mi_Drake_Eggs = 3839;
	private final short Brooch = 3843;
	private final short Bl_Anvil_Coin = 3871;
	private final short G_Let_Balthazar = 3867;
	private final short Recipe_Power_Stone = 3838;
	private final short Power_Stones = 3846;
	private final short Nebulite_Crystals = 3844;
	private final short Broke_Power_Stone = 3845;
	private final short G_Let_Rodemai = 3868;
	private final short Imp_Keys = 3847;
	private final short Scepter_Judgement = 3869;
	private final short Proof_Aspiration = 3870;
	private final int[] EggList;
	private final int[] NPC;
	private final String[] STATS;
	private final Map<Integer, Drop> DROPLIST;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _503_PursuitClanAmbition()
	{
		super(true);
		EggList = new int[4];
		NPC = new int[12];
		STATS = new String[5];
		DROPLIST = new HashMap<Integer, Drop>();
		EggList[0] = 3839;
		EggList[1] = 3840;
		EggList[2] = 3841;
		EggList[3] = 3842;
		NPC[0] = 30645;
		NPC[1] = 30758;
		NPC[2] = 30759;
		NPC[3] = 30760;
		NPC[4] = 30761;
		NPC[5] = 30762;
		NPC[6] = 30763;
		NPC[7] = 30512;
		NPC[8] = 30764;
		NPC[9] = 30868;
		NPC[10] = 30765;
		NPC[11] = 30766;
		STATS[0] = "cond";
		STATS[1] = "Fritz";
		STATS[2] = "Lutz";
		STATS[3] = "Kurtz";
		STATS[4] = "ImpGraveKeeper";
		Drop drop = new Drop(2, 10, 20);
		drop.addItem((short) 3842);
		DROPLIST.put(20282, drop);
		drop = new Drop(2, 10, 15);
		drop.addItem((short) 3842);
		DROPLIST.put(20243, drop);
		drop = new Drop(2, 10, 20);
		drop.addItem((short) 3841);
		DROPLIST.put(20137, drop);
		drop = new Drop(2, 10, 25);
		drop.addItem((short) 3841);
		DROPLIST.put(20285, drop);
		drop = new Drop(2, 10, 100);
		drop.addItem((short) 3840);
		DROPLIST.put(27178, drop);
		drop = new Drop(5, 10, 25);
		drop.addItem((short) 3845);
		drop.addItem((short) 3846);
		drop.addItem((short) 3844);
		DROPLIST.put(20654, drop);
		drop = new Drop(5, 10, 35);
		drop.addItem((short) 3845);
		drop.addItem((short) 3846);
		drop.addItem((short) 3844);
		DROPLIST.put(20656, drop);
		drop = new Drop(10, 0, 15);
		DROPLIST.put(20668, drop);
		drop = new Drop(10, 6, 80);
		drop.addItem((short) 3847);
		DROPLIST.put(27179, drop);
		drop = new Drop(10, 0, 100);
		DROPLIST.put(27181, drop);
		this.addStartNpc(NPC[3]);
		this.addTalkId(new int[] { NPC[3] });
		for(final int npcId : NPC)
			this.addTalkId(new int[] { npcId });
		for(final int mobId : DROPLIST.keySet())
			this.addKillId(new int[] { mobId });
		addAttackId(new int[] { 27181 });
		for(int i = 3839; i <= 3848; ++i)
			addQuestItem(new int[] { i });
		for(int i = 3866; i <= 3869; ++i)
			addQuestItem(new int[] { i });
	}

	public void suscribe_members(final QuestState st)
	{
		final int clan = st.getPlayer().getClan().getClanId();
		Connection con = null;
		PreparedStatement offline = null;
		PreparedStatement insertion = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("SELECT obj_Id FROM characters WHERE clanid=? AND online=0");
			insertion = con.prepareStatement("REPLACE INTO character_quests (char_id,id,var,value) VALUES (?,?,?,?)");
			offline.setInt(1, clan);
			rs = offline.executeQuery();
			while(rs.next())
			{
				final int char_id = rs.getInt("obj_Id");
				try
				{
					insertion.setInt(1, char_id);
					insertion.setInt(2, getId());
					insertion.setString(3, "<state>");
					insertion.setString(4, "Progress");
					insertion.executeUpdate();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch(Exception e2)
		{
			e2.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(insertion);
			DbUtils.closeQuietly(con, offline, rs);
		}
	}

	public void offlineMemberExit(final QuestState st)
	{
		final int clan = st.getPlayer().getClan().getClanId();
		Connection con = null;
		PreparedStatement offline = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("DELETE FROM character_quests WHERE id=? AND char_id IN (SELECT obj_id FROM characters WHERE clanId=? AND online=0)");
			offline.setInt(1, getId());
			offline.setInt(2, clan);
			offline.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, offline);
		}
	}

	public int getLeaderVar(final QuestState st, final String var)
	{
		try
		{
			final Clan clan = st.getPlayer().getClan();
			if(clan == null)
				return -1;
			final Player leader = clan.getLeader().getPlayer();
			if(leader != null)
				return leader.getQuestState(getId()).getInt(var);
		}
		catch(Exception e2)
		{
			return -1;
		}
		final int leaderId = st.getPlayer().getClan().getLeaderId();
		Connection con = null;
		PreparedStatement offline = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("SELECT value FROM character_quests WHERE char_id=? AND var=? AND id=?");
			offline.setInt(1, leaderId);
			offline.setString(2, var);
			offline.setInt(3, getId());
			int val = -1;
			rs = offline.executeQuery();
			if(rs.next())
				val = rs.getInt("value");
			return val;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		finally
		{
			DbUtils.closeQuietly(con, offline, rs);
		}
	}

	public void setLeaderVar(final QuestState st, final String var, final String value)
	{
		final Clan clan = st.getPlayer().getClan();
		if(clan == null)
			return;
		final Player leader = clan.getLeader().getPlayer();
		if(leader != null)
			leader.getQuestState(getId()).set(var, value);
		else
		{
			final int leaderId = st.getPlayer().getClan().getLeaderId();
			Connection con = null;
			PreparedStatement offline = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				offline = con.prepareStatement("UPDATE character_quests SET value=? WHERE char_id=? AND var=? AND id=?");
				offline.setString(1, value);
				offline.setInt(2, leaderId);
				offline.setString(3, var);
				offline.setInt(4, getId());
				offline.executeUpdate();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				DbUtils.closeQuietly(con, offline);
			}
		}
	}

	public boolean checkEggs(final QuestState st)
	{
		int count = 0;
		for(final int item : EggList)
			if(st.getQuestItemsCount(item) > 9L)
				++count;
		return count > 3;
	}

	public void giveItem(final short item, final long maxcount, final QuestState st)
	{
		final long count = st.getQuestItemsCount(item);
		if(count < maxcount)
		{
			st.giveItems(item, 1L);
			if(count == maxcount - 1L)
				st.playSound(Quest.SOUND_MIDDLE);
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
	}

	public String exit503(final boolean completed, final QuestState st)
	{
		if(completed)
		{
			st.giveItems(3870, 1L);
			st.addExpAndSp(0L, 250000L);
			for(final String var : STATS)
				st.unset(var);
			st.exitCurrentQuest(false);
		}
		else
			st.exitCurrentQuest(true);
		st.takeItems(3869, -1L);
		try
		{
			final Player[] onlineMembers;
			final Player[] members = onlineMembers = st.getPlayer().getClan().getOnlineMembers(0);
			for(final Player player : onlineMembers)
				if(player != null)
				{
					final QuestState qs = player.getQuestState(getId());
					if(qs != null)
						qs.exitCurrentQuest(true);
				}
			offlineMemberExit(st);
		}
		catch(Exception e)
		{
			return "You dont have any members in your Clan, so you can't finish the Pursuit of Aspiration";
		}
		return "Congratulations, you have finished the Pursuit of Clan Ambition";
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30760-08.htm"))
		{
			st.giveItems(3866, 1L);
			for(final String var : STATS)
				st.set(var, "1");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30760-12.htm"))
		{
			st.giveItems(3867, 1L);
			st.set("cond", "4");
		}
		else if(event.equalsIgnoreCase("30760-16.htm"))
		{
			st.giveItems(3868, 1L);
			st.set("cond", "7");
		}
		else if(event.equalsIgnoreCase("30760-20.htm"))
			exit503(true, st);
		else if(event.equalsIgnoreCase("30760-22.htm"))
			st.set("cond", "13");
		else if(event.equalsIgnoreCase("30760-23.htm"))
			exit503(true, st);
		else if(event.equalsIgnoreCase("30645-03.htm"))
		{
			st.takeItems(3866, -1L);
			st.set("cond", "2");
			suscribe_members(st);
			final Player[] onlineMembers;
			final Player[] members = onlineMembers = st.getPlayer().getClan().getOnlineMembers(st.getPlayer().getObjectId());
			for(final Player player : onlineMembers)
				newQuestState(player, 2);
		}
		else if(event.equalsIgnoreCase("30763-03.htm"))
		{
			if(st.getInt("Kurtz") == 1)
			{
				htmltext = "30763-02.htm";
				st.giveItems(3839, 6L);
				st.giveItems(3843, 1L);
				st.set("Kurtz", "2");
			}
		}
		else if(event.equalsIgnoreCase("30762-03.htm"))
		{
			final int lutz = st.getInt("Lutz");
			if(lutz == 1)
			{
				htmltext = "30762-02.htm";
				st.giveItems(3839, 4L);
				st.giveItems(3840, 3L);
				st.set("Lutz", "2");
			}
			st.addSpawn(27178, 112268, 112761, -2770, 120000);
			st.addSpawn(27178, 112234, 112705, -2770, 120000);
		}
		else if(event.equalsIgnoreCase("30761-03.htm"))
		{
			final int fritz = st.getInt("Fritz");
			if(fritz == 1)
			{
				htmltext = "30761-02.htm";
				st.giveItems(3840, 3L);
				st.set("Fritz", "2");
			}
			st.addSpawn(27178, 103841, 116809, -3025, 120000);
			st.addSpawn(27178, 103848, 116910, -3020, 120000);
		}
		else if(event.equalsIgnoreCase("30512-03.htm"))
		{
			st.takeItems(3843, -1L);
			st.giveItems(3871, 1L);
			st.set("Kurtz", "3");
		}
		else if(event.equalsIgnoreCase("30764-03.htm"))
		{
			st.takeItems(3867, -1L);
			st.set("cond", "5");
			st.set("Kurtz", "3");
		}
		else if(event.equalsIgnoreCase("30764-05.htm"))
		{
			st.takeItems(3867, -1L);
			st.set("cond", "5");
		}
		else if(event.equalsIgnoreCase("30764-06.htm"))
		{
			st.takeItems(3871, -1L);
			st.set("Kurtz", "4");
			st.giveItems(3838, 1L);
		}
		else if(event.equalsIgnoreCase("30868-04.htm"))
		{
			st.takeItems(3868, -1L);
			st.set("cond", "8");
		}
		else if(event.equalsIgnoreCase("30868-06a.htm"))
			st.set("cond", "10");
		else if(event.equalsIgnoreCase("30868-10.htm"))
			st.set("cond", "12");
		else if(event.equalsIgnoreCase("30766-04.htm"))
		{
			st.set("cond", "9");
			NpcInstance n = st.findTemplate(30766);
			if(n != null)
				Functions.npcSay(n, "Blood and Honour");
			n = st.findTemplate(30759);
			if(n != null)
				Functions.npcSay(n, "Ambition and Power");
			n = st.findTemplate(30758);
			if(n != null)
				Functions.npcSay(n, "War and Death");
		}
		else if(event.equalsIgnoreCase("30766-08.htm"))
		{
			st.takeItems(3869, -1L);
			exit503(false, st);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int Martien = 30645;
		final int Athrea = 30758;
		final int Kalis = 30759;
		final int Gustaf = 30760;
		final int Fritz = 30761;
		final int Lutz = 30762;
		final int Kurtz = 30763;
		final int Kusto = 30512;
		final int Balthazar = 30764;
		final int Rodemai = 30868;
		final int Coffer = 30765;
		final int Cleo = 30766;
		String htmltext = "noquest";
		final boolean isLeader = st.getPlayer().isClanLeader();
		if(id == 1 && npcId == Gustaf)
		{
			for(final String var : STATS)
				st.set(var, "0");
			if(st.getPlayer().getClan() != null)
			{
				if(isLeader)
				{
					final int clanLevel = st.getPlayer().getClan().getLevel();
					if(st.getQuestItemsCount(3870) > 0L)
					{
						htmltext = "30760-03.htm";
						st.exitCurrentQuest(true);
					}
					else if(clanLevel > 3)
						htmltext = "30760-04.htm";
					else
					{
						htmltext = "30760-02.htm";
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "30760-04t.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "30760-01.htm";
				st.exitCurrentQuest(true);
			}
			return htmltext;
		}
		if(st.getPlayer().getClan() != null && st.getPlayer().getClan().getLevel() == 5)
			return "completed";
		if(isLeader)
		{
			if(st.get("cond") == null)
				st.set("cond", "1");
			if(st.get("Kurtz") == null)
				st.set("Kurtz", "1");
			if(st.get("Lutz") == null)
				st.set("Lutz", "1");
			if(st.get("Fritz") == null)
				st.set("Fritz", "1");
			final int cond = st.getInt("cond");
			final int kurtz = st.getInt("Kurtz");
			final int lutz = st.getInt("Lutz");
			final int fritz = st.getInt("Fritz");
			if(npcId == Gustaf)
			{
				if(cond == 1)
					htmltext = "30760-09.htm";
				else if(cond == 2)
					htmltext = "30760-10.htm";
				else if(cond == 3)
					htmltext = "30760-11.htm";
				else if(cond == 4)
					htmltext = "30760-13.htm";
				else if(cond == 5)
					htmltext = "30760-14.htm";
				else if(cond == 6)
					htmltext = "30760-15.htm";
				else if(cond == 7)
					htmltext = "30760-17.htm";
				else if(cond == 12)
					htmltext = "30760-19.htm";
				else if(cond == 13)
					htmltext = "30760-24.htm";
				else
					htmltext = "30760-18.htm";
			}
			else if(npcId == Martien)
			{
				if(cond == 1)
					htmltext = "30645-02.htm";
				else if(cond == 2)
				{
					if(checkEggs(st) && kurtz > 1 && lutz > 1 && fritz > 1)
					{
						htmltext = "30645-05.htm";
						st.set("cond", "3");
						for(final int item : EggList)
							st.takeItems(item, -1L);
					}
					else
						htmltext = "30645-04.htm";
				}
				else if(cond == 3)
					htmltext = "30645-07.htm";
				else
					htmltext = "30645-08.htm";
			}
			else if(npcId == Lutz && cond == 2)
				htmltext = "30762-01.htm";
			else if(npcId == Kurtz && cond == 2)
				htmltext = "30763-01.htm";
			else if(npcId == Fritz && cond == 2)
				htmltext = "30761-01.htm";
			else if(npcId == Kusto)
			{
				if(kurtz == 1)
					htmltext = "30512-01.htm";
				else if(kurtz == 2)
					htmltext = "30512-02.htm";
				else
					htmltext = "30512-04.htm";
			}
			else if(npcId == Balthazar)
			{
				if(cond == 4)
				{
					if(kurtz > 2)
						htmltext = "30764-04.htm";
					else
						htmltext = "30764-02.htm";
				}
				else if(cond == 5)
				{
					if(st.getQuestItemsCount(3846) > 9L && st.getQuestItemsCount(3844) > 9L)
					{
						htmltext = "30764-08.htm";
						st.takeItems(3846, -1L);
						st.takeItems(3844, -1L);
						st.takeItems(3843, -1L);
						st.set("cond", "6");
					}
					else
						htmltext = "30764-07.htm";
				}
				else if(cond == 6)
					htmltext = "30764-09.htm";
			}
			else if(npcId == Rodemai)
			{
				if(cond == 7)
					htmltext = "30868-02.htm";
				else if(cond == 8)
					htmltext = "30868-05.htm";
				else if(cond == 9)
					htmltext = "30868-06.htm";
				else if(cond == 10)
					htmltext = "30868-08.htm";
				else if(cond == 11)
					htmltext = "30868-09.htm";
				else if(cond == 12)
					htmltext = "30868-11.htm";
			}
			else if(npcId == Cleo)
			{
				if(cond == 8)
					htmltext = "30766-02.htm";
				else if(cond == 9)
					htmltext = "30766-05.htm";
				else if(cond == 10)
					htmltext = "30766-06.htm";
				else if(cond == 11 || cond == 12 || cond == 13)
					htmltext = "30766-07.htm";
			}
			else if(npcId == Coffer)
			{
				if(st.getInt("cond") == 10)
				{
					if(st.getQuestItemsCount(3847) < 6L)
						htmltext = "30765-03a.htm";
					else if(st.getInt("ImpGraveKeeper") == 3)
					{
						htmltext = "30765-02.htm";
						st.set("cond", "11");
						st.takeItems(3847, 6L);
						st.giveItems(3869, 1L);
					}
					else
						htmltext = "<html><head><body>(You and your Clan didn't kill the Imperial Gravekeeper by your own, do it try again.)</body></html>";
				}
				else
					htmltext = "<html><head><body>(You already have the Scepter of Judgement.)</body></html>";
			}
			else if(npcId == Kalis)
				htmltext = "30759-01.htm";
			else if(npcId == Athrea)
				htmltext = "30758-01.htm";
			return htmltext;
		}
		final int cond = getLeaderVar(st, "cond");
		if(npcId == Martien && (cond == 1 || cond == 2 || cond == 3))
			htmltext = "30645-01.htm";
		else if(npcId == Rodemai)
		{
			if(cond == 9 || cond == 10)
				htmltext = "30868-07.htm";
			else if(cond == 7)
				htmltext = "30868-01.htm";
		}
		else if(npcId == Balthazar && cond == 4)
			htmltext = "30764-01.htm";
		else if(npcId == Cleo && cond == 8)
			htmltext = "30766-01.htm";
		else if(npcId == Kusto && cond > 2 && cond < 6)
			htmltext = "30512-01a.htm";
		else if(npcId == Coffer && cond == 10)
			htmltext = "30765-01.htm";
		else if(npcId == Gustaf)
			if(cond == 3)
				htmltext = "30760-11t.htm";
			else if(cond == 4)
				htmltext = "30760-15t.htm";
			else if(cond == 12)
				htmltext = "30760-19t.htm";
			else if(cond == 13)
				htmltext = "30766-24t.htm";
		return htmltext;
	}

	@Override
	public String onAttack(final NpcInstance npc, final QuestState st)
	{
		if(npc.getMaxHp() / 2 > npc.getCurrentHp() && Rnd.chance(4))
		{
			final int ImpGraveKepperStat = getLeaderVar(st, "ImpGraveKeeper");
			if(ImpGraveKepperStat == 1)
			{
				for(int i = 1; i <= 4; ++i)
					st.addSpawn(27180, 120000);
				setLeaderVar(st, "ImpGraveKeeper", "2");
			}
			else
			{
				final List<Player> players = World.getAroundPlayers(npc, 900, 200);
				if(players.size() > 0)
				{
					final Player player = players.get(Rnd.get(players.size()));
					if(player != null)
						player.teleToLocation(185462, 20342, -3250);
				}
			}
		}
		return null;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final Drop drop = DROPLIST.get(npcId);
		final int condition = drop.condition;
		int maxcount = drop.maxcount;
		final int chance = drop.chance;
		final List<Short> itemList = drop.itemList;
		final int random = Rnd.get(100);
		final int cond = getLeaderVar(st, "cond");
		if(cond == condition && random < chance)
			if(itemList.size() > 1)
			{
				final int stoneRandom = Rnd.get(3);
				if(stoneRandom == 0)
				{
					if(getLeaderVar(st, "Kurtz") < 4)
						return null;
					maxcount *= 4;
				}
				giveItem(itemList.get(stoneRandom), maxcount, st);
			}
			else if(itemList.size() > 0)
				giveItem(itemList.get(0), maxcount, st);
			else if(npcId == 27181)
			{
				final NpcInstance spawnedNpc = st.addSpawn(30765, 120000);
				Functions.npcSay(spawnedNpc, "Curse of the gods on the one that defiles the property of the empire!");
				setLeaderVar(st, "ImpGraveKeeper", "3");
			}
			else
				st.addSpawn(27179, 120000);
		return null;
	}
}
