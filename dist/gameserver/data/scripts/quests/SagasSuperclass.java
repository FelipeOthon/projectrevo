package quests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

import l2s.commons.dbcp.DbUtils;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.model.quest.QuestTimer;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;

public abstract class SagasSuperclass extends Quest
{
	protected int id;
	protected int classid;
	protected int prevclass;
	protected int[] NPC;
	public int[] Items;
	protected int[] Mob;
	protected int[] X;
	protected int[] Y;
	protected int[] Z;
	public String[] Text;
	protected List<Spawn> Spawn_List;
	protected int[] Archon_Minions;
	protected int[] Guardian_Angels;
	protected int[] Archon_Hellisha_Norm;
	protected static IntSet Quests;
	protected static int[][] QuestClass;

	private void cleanTempVars()
	{
		Connection con = null;
		PreparedStatement st = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			st = con.prepareStatement("DELETE FROM character_quests WHERE id=? AND (var='spawned' OR var='kills' OR var='Archon' OR var LIKE 'Mob_%')");
			st.setInt(1, getId());
			st.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, st);
		}
	}

	private void FinishQuest(final QuestState st, final Player player)
	{
		st.addExpAndSp(2299404L, 0L, true);
		st.giveItems(57, 5000000L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
		st.giveItems(6622, 1L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
		st.exitCurrentQuest(true);
		player.setClassId(getClassId(player), false, true);
		if(!player.isSubClassActive() && player.getBaseClassId() == getPrevClass(player))
			player.setBaseClass(getClassId(player));
		player.broadcastUserInfo(true);
		Cast(st.findTemplate(NPC[0]), player, 4339, 1);
	}

	public SagasSuperclass(final boolean party)
	{
		super(party);
		id = 0;
		classid = 0;
		prevclass = 0;
		NPC = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		Items = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		Mob = new int[] { 0, 1, 2 };
		X = new int[] { 0, 1, 2 };
		Y = new int[] { 0, 1, 2 };
		Z = new int[] { 0, 1, 2 };
		Text = new String[18];
		Spawn_List = new ArrayList<Spawn>();
		Archon_Minions = new int[] { 21646, 21647, 21648, 21649, 21650, 21651 };
		Guardian_Angels = new int[] { 27214, 27215, 27216 };
		Archon_Hellisha_Norm = new int[] { 18212, 18213, 18214, 18215, 18216, 18217, 18218, 18219 };
		cleanTempVars();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new SpawnCleaner(), 60000L, 10000L);
	}

	protected void registerNPCs()
	{
		this.addStartNpc(NPC[0]);
		addAttackId(new int[] { Mob[2] });
		addFirstTalkId(new int[] { NPC[4] });
		for(final int npc : NPC)
			this.addTalkId(new int[] { npc });
		for(final int mobid : Mob)
			this.addKillId(new int[] { mobid });
		for(final int mobid : Archon_Minions)
			this.addKillId(new int[] { mobid });
		for(final int mobid : Guardian_Angels)
			this.addKillId(new int[] { mobid });
		for(final int mobid : Archon_Hellisha_Norm)
			this.addKillId(new int[] { mobid });
		for(final int ItemId : Items)
			if(ItemId != 0 && ItemId != 7080 && ItemId != 7081 && ItemId != 6480 && ItemId != 6482)
				addQuestItem(new int[] { ItemId });
	}

	protected int getClassId(final Player player)
	{
		return classid;
	}

	protected int getPrevClass(final Player player)
	{
		return prevclass;
	}

	protected void Cast(final NpcInstance npc, final Creature target, final int skillId, final int level)
	{
		target.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(target, target, skillId, level, 6000, 1L) });
		target.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(npc, npc, skillId, level, 6000, 1L) });
	}

	protected void AddSpawn(final Player player, final NpcInstance mob, final int TimeToLive)
	{
		synchronized (Spawn_List)
		{
			Spawn_List.add(new Spawn(mob, player.getObjectId(), TimeToLive));
		}
	}

	protected NpcInstance FindMySpawn(final Player player, final int npcId)
	{
		if(npcId == 0 || player == null)
			return null;
		final long charObjectId = player.getObjectId();
		synchronized (Spawn_List)
		{
			for(final Spawn spawn : Spawn_List)
				if(spawn.charObjectId == charObjectId && spawn.npcId == npcId)
					return spawn.getNPC();
		}
		return null;
	}

	protected void DeleteSpawn(final long charObjectId, final int npcId)
	{
		if(npcId == 0 || charObjectId == 0L)
			return;
		synchronized (Spawn_List)
		{
			final Iterator<Spawn> it = Spawn_List.iterator();
			while(it.hasNext())
			{
				final Spawn spawn = it.next();
				if(spawn.charObjectId == charObjectId && spawn.npcId == npcId)
				{
					final NpcInstance npc = spawn.getNPC();
					if(npc != null)
						npc.deleteMe();
					it.remove();
				}
			}
		}
	}

	protected void DeleteMySpawn(final Player player, final int npcId)
	{
		if(npcId > 0 && player != null)
			DeleteSpawn(player.getObjectId(), npcId);
	}

	protected NpcInstance spawn(final int id, final Location loc)
	{
		final NpcTemplate template = NpcTable.getTemplate(id);
		l2s.gameserver.model.Spawn spawn;
		try
		{
			spawn = new l2s.gameserver.model.Spawn(template);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		spawn.setLoc(loc);
		final NpcInstance npc = spawn.doSpawn(true);
		spawn.stopRespawn();
		return npc;
	}

	public void giveHallishaMark(final QuestState st2)
	{
		final Player player = st2.getPlayer();
		if(player == null)
			return;
		if(GameObjectsStorage.getNpc(st2.getInt("Archon")) != null)
			return;
		QuestTimer qt = st2.getQuestTimer("Archon Hellisha has despawned");
		if(qt != null)
		{
			qt.cancel();
			qt = null;
		}
		if(st2.getQuestItemsCount(Items[3]) < 700L)
			st2.giveItems(Items[3], Rnd.get(1, 4));
		else
		{
			st2.takeItems(Items[3], 20L);
			final NpcInstance Archon = spawn(Mob[1], player.getLoc());
			AddSpawn(player, Archon, 600000);
			final int ArchonId = Archon.getObjectId();
			st2.set("Archon", str(ArchonId));
			this.startQuestTimer("Archon Hellisha has despawned", 600000L, Archon, player);
			Archon.setRunning();
			Archon.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, 100000);
			AutoChat(Archon, Text[13].replace("PLAYERNAME", player.getName()));
		}
	}

	protected QuestState findRightState(Player player, final NpcInstance npc)
	{
		if(player == null || npc == null)
			return null;
		final long npcObjectId = npc.getObjectId();
		final long charObjectId = player.getObjectId();
		synchronized (Spawn_List)
		{
			for(final Spawn spawn : Spawn_List)
				if(spawn.charObjectId == charObjectId && spawn.npcObjectId == npcObjectId)
					return player.getQuestState(getId());
			for(final Spawn spawn : Spawn_List)
				if(spawn.npcObjectId == npcObjectId)
				{
					player = GameObjectsStorage.getPlayer(spawn.charObjectId);
					return player == null ? null : player.getQuestState(getId());
				}
		}
		return null;
	}

	public static QuestState findQuest(final Player player)
	{
		QuestState st = null;
		for(final int q : Quests.toArray())
		{
			st = player.getQuestState(q);
			if(st != null)
			{
				final int[] array;
				final int[] qc = array = QuestClass[q - 67];
				for(final int c : array)
					if(player.getClassId().getId() == c)
						return st;
			}
		}
		return null;
	}

	public static void process_step_15to16(final QuestState st)
	{
		if(st == null || st.getInt("cond") != 15)
			return;
		final int Halishas_Mark = ((SagasSuperclass) st.getQuest()).Items[3];
		final int Resonance_Amulet = ((SagasSuperclass) st.getQuest()).Items[8];
		st.takeItems(Halishas_Mark, -1L);
		if(st.getQuestItemsCount(Resonance_Amulet) == 0L)
			st.giveItems(Resonance_Amulet, 1L);
		st.set("cond", "16");
		st.playSound(Quest.SOUND_MIDDLE);
	}

	protected void AutoChat(final NpcInstance npc, final String text)
	{
		if(npc != null)
			Functions.npcSay(npc, text);
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = "";
		final Player player = st.getPlayer();
		if(event.equalsIgnoreCase("0-011.htm") || event.equalsIgnoreCase("0-012.htm") || event.equalsIgnoreCase("0-013.htm") || event.equalsIgnoreCase("0-014.htm") || event.equalsIgnoreCase("0-015.htm"))
			htmltext = event;
		else if(event.equalsIgnoreCase("accept"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.giveItems(Items[10], 1L);
			htmltext = "0-03.htm";
		}
		else if(event.equalsIgnoreCase("0-1"))
		{
			if(player.getLevel() < 76)
			{
				htmltext = "0-02.htm";
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "0-05.htm";
		}
		else if(event.equalsIgnoreCase("0-2"))
		{
			if(player.getLevel() >= 76)
			{
				htmltext = "0-07.htm";
				st.takeItems(Items[10], -1L);
				FinishQuest(st, player);
			}
			else
			{
				st.takeItems(Items[10], -1L);
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "20");
				htmltext = "0-08.htm";
			}
		}
		else if(event.equalsIgnoreCase("1-3"))
		{
			st.set("cond", "3");
			htmltext = "1-05.htm";
		}
		else if(event.equalsIgnoreCase("1-4"))
		{
			st.set("cond", "4");
			st.takeItems(Items[0], 1L);
			if(Items[11] != 0)
				st.takeItems(Items[11], 1L);
			st.giveItems(Items[1], 1L);
			htmltext = "1-06.htm";
		}
		else if(event.equalsIgnoreCase("2-1"))
		{
			st.set("cond", "2");
			htmltext = "2-05.htm";
		}
		else if(event.equalsIgnoreCase("2-2"))
		{
			st.set("cond", "5");
			st.takeItems(Items[1], 1L);
			st.giveItems(Items[4], 1L);
			htmltext = "2-06.htm";
		}
		else if(event.equalsIgnoreCase("3-5"))
			htmltext = "3-07.htm";
		else if(event.equalsIgnoreCase("3-6"))
		{
			st.set("cond", "11");
			htmltext = "3-02.htm";
		}
		else if(event.equalsIgnoreCase("3-7"))
		{
			st.set("cond", "12");
			htmltext = "3-03.htm";
		}
		else if(event.equalsIgnoreCase("3-8"))
		{
			st.set("cond", "13");
			st.takeItems(Items[2], 1L);
			st.giveItems(Items[7], 1L);
			htmltext = "3-08.htm";
		}
		else if(event.equalsIgnoreCase("4-1"))
			htmltext = "4-010.htm";
		else if(event.equalsIgnoreCase("4-2"))
		{
			st.giveItems(Items[9], 1L);
			st.set("cond", "18");
			st.playSound(Quest.SOUND_MIDDLE);
			htmltext = "4-011.htm";
		}
		else
		{
			if(event.equalsIgnoreCase("4-3"))
			{
				st.giveItems(Items[9], 1L);
				st.set("cond", "18");
				st.set("Quest0", "0");
				st.playSound(Quest.SOUND_MIDDLE);
				final NpcInstance Mob_2 = FindMySpawn(player, NPC[4]);
				if(Mob_2 != null)
				{
					AutoChat(Mob_2, Text[13].replace("PLAYERNAME", player.getName()));
					DeleteMySpawn(player, NPC[4]);
					QuestTimer qt = st.getQuestTimer("Mob_2 has despawned");
					if(qt != null)
						qt.cancel();
					qt = st.getQuestTimer("NPC_4 Timer");
					if(qt != null)
						qt.cancel();
				}
				return null;
			}
			if(event.equalsIgnoreCase("5-1"))
			{
				st.set("cond", "6");
				st.takeItems(Items[4], 1L);
				Cast(st.findTemplate(NPC[5]), player, 4546, 1);
				st.playSound(Quest.SOUND_MIDDLE);
				htmltext = "5-02.htm";
			}
			else if(event.equalsIgnoreCase("6-1"))
			{
				st.set("cond", "8");
				st.takeItems(Items[5], 1L);
				Cast(st.findTemplate(NPC[6]), player, 4546, 1);
				st.playSound(Quest.SOUND_MIDDLE);
				htmltext = "6-03.htm";
			}
			else if(event.equalsIgnoreCase("7-1"))
			{
				if(FindMySpawn(player, Mob[0]) == null)
				{
					final NpcInstance Mob_3 = spawn(Mob[0], new Location(X[0], Y[0], Z[0]));
					AddSpawn(player, Mob_3, 180000);
					this.startQuestTimer("Mob_0 Timer", 500L, Mob_3, player);
					this.startQuestTimer("Mob_1 has despawned", 120000L, Mob_3, player);
					htmltext = "7-02.htm";
				}
				else
					htmltext = "7-03.htm";
			}
			else if(event.equalsIgnoreCase("7-2"))
			{
				st.set("cond", "10");
				st.takeItems(Items[6], 1L);
				Cast(st.findTemplate(NPC[7]), player, 4546, 1);
				st.playSound(Quest.SOUND_MIDDLE);
				htmltext = "7-06.htm";
			}
			else if(event.equalsIgnoreCase("8-1"))
			{
				st.set("cond", "14");
				st.takeItems(Items[7], 1L);
				Cast(st.findTemplate(NPC[8]), player, 4546, 1);
				st.playSound(Quest.SOUND_MIDDLE);
				htmltext = "8-02.htm";
			}
			else if(event.equalsIgnoreCase("9-1"))
			{
				st.set("cond", "17");
				st.takeItems(Items[8], 1L);
				Cast(st.findTemplate(NPC[9]), player, 4546, 1);
				st.playSound(Quest.SOUND_MIDDLE);
				htmltext = "9-03.htm";
			}
			else if(event.equalsIgnoreCase("10-1"))
			{
				if(st.getInt("Quest0") == 0 || FindMySpawn(player, NPC[4]) == null)
				{
					DeleteMySpawn(player, NPC[4]);
					DeleteMySpawn(player, Mob[2]);
					st.set("Quest0", "1");
					st.set("Quest1", "45");
					final NpcInstance NPC_4 = spawn(NPC[4], new Location(X[2], Y[2], Z[2]));
					final NpcInstance Mob_4 = spawn(Mob[2], new Location(X[1], Y[1], Z[1]));
					AddSpawn(player, Mob_4, 300000);
					AddSpawn(player, NPC_4, 300000);
					this.startQuestTimer("Mob_2 Timer", 1000L, Mob_4, player);
					this.startQuestTimer("Mob_2 despawn", 59000L, Mob_4, player);
					this.startQuestTimer("NPC_4 Timer", 500L, NPC_4, player);
					this.startQuestTimer("NPC_4 despawn", 60000L, NPC_4, player);
					htmltext = "10-02.htm";
				}
				else if(st.getInt("Quest1") == 45)
					htmltext = "10-03.htm";
				else if(st.getInt("Tab") == 1)
				{
					NpcInstance Mob_2 = FindMySpawn(player, NPC[4]);
					if(Mob_2 == null || !st.getPlayer().knowsObject(Mob_2))
					{
						DeleteMySpawn(player, NPC[4]);
						Mob_2 = spawn(NPC[4], new Location(X[2], Y[2], Z[2]));
						AddSpawn(player, Mob_2, 300000);
						st.set("Quest0", "1");
						st.set("Quest1", "0");
						QuestTimer qt = st.getQuestTimer("NPC_4 despawn");
						if(qt != null)
						{
							qt.cancel();
							qt = null;
						}
						this.startQuestTimer("NPC_4 despawn", 180000L, Mob_2, player);
					}
					htmltext = "10-04.htm";
				}
			}
			else if(event.equalsIgnoreCase("10-2"))
			{
				st.set("cond", "19");
				st.takeItems(Items[9], 1L);
				Cast(st.findTemplate(NPC[10]), player, 4546, 1);
				st.playSound(Quest.SOUND_MIDDLE);
				htmltext = "10-06.htm";
			}
			else if(event.equalsIgnoreCase("11-9"))
			{
				st.set("cond", "15");
				htmltext = "11-03.htm";
			}
			else
			{
				if(event.equalsIgnoreCase("Mob_0 Timer"))
				{
					AutoChat(FindMySpawn(player, Mob[0]), Text[0].replace("PLAYERNAME", player.getName()));
					return null;
				}
				if(event.equalsIgnoreCase("Mob_1 has despawned"))
				{
					AutoChat(FindMySpawn(player, Mob[0]), Text[1].replace("PLAYERNAME", player.getName()));
					DeleteMySpawn(player, Mob[0]);
					return null;
				}
				if(event.equalsIgnoreCase("Archon Hellisha has despawned"))
				{
					AutoChat(npc, Text[6].replace("PLAYERNAME", player.getName()));
					DeleteMySpawn(player, Mob[1]);
					return null;
				}
				if(event.equalsIgnoreCase("Mob_2 Timer"))
				{
					final NpcInstance NPC_4 = FindMySpawn(player, NPC[4]);
					final NpcInstance Mob_4 = FindMySpawn(player, Mob[2]);
					if(NPC_4.knowsObject(Mob_4))
					{
						NPC_4.setRunning();
						NPC_4.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, Mob_4, null);
						Mob_4.setRunning();
						Mob_4.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, NPC_4, null);
						AutoChat(Mob_4, Text[14].replace("PLAYERNAME", player.getName()));
					}
					else
						this.startQuestTimer("Mob_2 Timer", 1000L, npc, player);
					return null;
				}
				if(event.equalsIgnoreCase("Mob_2 despawn"))
				{
					final NpcInstance Mob_2 = FindMySpawn(player, Mob[2]);
					AutoChat(Mob_2, Text[15].replace("PLAYERNAME", player.getName()));
					st.set("Quest0", "2");
					if(Mob_2 != null)
						Mob_2.reduceCurrentHp(9999999.0, Mob_2, (Skill) null, 0, false, true, true, false, false, false, false, false);
					DeleteMySpawn(player, Mob[2]);
					return null;
				}
				if(event.equalsIgnoreCase("NPC_4 Timer"))
				{
					AutoChat(FindMySpawn(player, NPC[4]), Text[7].replace("PLAYERNAME", player.getName()));
					this.startQuestTimer("NPC_4 Timer 2", 1500L, npc, player);
					if(st.getInt("Quest1") == 45)
						st.set("Quest1", "0");
					return null;
				}
				if(event.equalsIgnoreCase("NPC_4 Timer 2"))
				{
					AutoChat(FindMySpawn(player, NPC[4]), Text[8].replace("PLAYERNAME", player.getName()));
					this.startQuestTimer("NPC_4 Timer 3", 10000L, npc, player);
					return null;
				}
				if(event.equalsIgnoreCase("NPC_4 Timer 3"))
				{
					if(st.getInt("Quest0") == 0)
					{
						this.startQuestTimer("NPC_4 Timer 3", 13000L, npc, player);
						AutoChat(FindMySpawn(player, NPC[4]), Text[Rnd.get(9, 10)].replace("PLAYERNAME", player.getName()));
					}
					return null;
				}
				if(event.equalsIgnoreCase("NPC_4 despawn"))
				{
					st.set("Quest1", str(st.getInt("Quest1") + 1));
					final NpcInstance NPC_4 = FindMySpawn(player, NPC[4]);
					if(st.getInt("Quest0") == 1 || st.getInt("Quest0") == 2 || st.getInt("Quest1") > 3)
					{
						st.set("Quest0", "0");
						AutoChat(NPC_4, Text[Rnd.get(11, 12)].replace("PLAYERNAME", player.getName()));
						if(NPC_4 != null)
							NPC_4.reduceCurrentHp(9999999.0, NPC_4, (Skill) null, 0, false, true, true, false, false, false, false, false);
						DeleteMySpawn(player, NPC[4]);
					}
					else
						this.startQuestTimer("NPC_4 despawn", 1000L, npc, player);
					return null;
				}
			}
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		final Player player = st.getPlayer();
		if(player.getClassId().getId() != getPrevClass(player))
		{
			st.exitCurrentQuest(true);
			return htmltext;
		}
		if(cond == 0)
		{
			if(npcId == NPC[0])
				htmltext = "0-01.htm";
		}
		else if(cond == 1)
		{
			if(npcId == NPC[0])
				htmltext = "0-04.htm";
			else if(npcId == NPC[2])
				htmltext = "2-01.htm";
		}
		else if(cond == 2)
		{
			if(npcId == NPC[2])
				htmltext = "2-02.htm";
			else if(npcId == NPC[1])
				htmltext = "1-01.htm";
		}
		else if(cond == 3)
		{
			if(npcId == NPC[1])
				if(st.getQuestItemsCount(Items[0]) > 0L)
				{
					if(Items[11] == 0)
						htmltext = "1-03.htm";
					else if(st.getQuestItemsCount(Items[11]) > 0L)
						htmltext = "1-03.htm";
					else
						htmltext = "1-02.htm";
				}
				else
					htmltext = "1-02.htm";
		}
		else if(cond == 4)
		{
			if(npcId == NPC[1])
				htmltext = "1-04.htm";
			else if(npcId == NPC[2])
				htmltext = "2-03.htm";
		}
		else if(cond == 5)
		{
			if(npcId == NPC[2])
				htmltext = "2-04.htm";
			else if(npcId == NPC[5])
				htmltext = "5-01.htm";
		}
		else if(cond == 6)
		{
			if(npcId == NPC[5])
				htmltext = "5-03.htm";
			else if(npcId == NPC[6])
				htmltext = "6-01.htm";
		}
		else if(cond == 7)
		{
			if(npcId == NPC[6])
				htmltext = "6-02.htm";
		}
		else if(cond == 8)
		{
			if(npcId == NPC[6])
				htmltext = "6-04.htm";
			else if(npcId == NPC[7])
				htmltext = "7-01.htm";
		}
		else if(cond == 9)
		{
			if(npcId == NPC[7])
				htmltext = "7-05.htm";
		}
		else if(cond == 10)
		{
			if(npcId == NPC[7])
				htmltext = "7-07.htm";
			else if(npcId == NPC[3])
				htmltext = "3-01.htm";
		}
		else if(cond == 11 || cond == 12)
		{
			if(npcId == NPC[3])
				if(st.getQuestItemsCount(Items[2]) > 0L)
					htmltext = "3-05.htm";
				else
					htmltext = "3-04.htm";
		}
		else if(cond == 13)
		{
			if(npcId == NPC[3])
				htmltext = "3-06.htm";
			else if(npcId == NPC[8])
				htmltext = "8-01.htm";
		}
		else if(cond == 14)
		{
			if(npcId == NPC[8])
				htmltext = "8-03.htm";
			else if(npcId == NPC[11])
				htmltext = "11-01.htm";
		}
		else if(cond == 15)
		{
			if(npcId == NPC[11])
				htmltext = "11-02.htm";
			else if(npcId == NPC[9])
				htmltext = "9-01.htm";
		}
		else if(cond == 16)
		{
			if(npcId == NPC[9])
				htmltext = "9-02.htm";
		}
		else if(cond == 17)
		{
			if(npcId == NPC[9])
				htmltext = "9-04.htm";
			else if(npcId == NPC[10])
				htmltext = "10-01.htm";
		}
		else if(cond == 18)
		{
			if(npcId == NPC[10])
				htmltext = "10-05.htm";
		}
		else if(cond == 19)
		{
			if(npcId == NPC[10])
				htmltext = "10-07.htm";
			if(npcId == NPC[0])
				htmltext = "0-06.htm";
		}
		else if(cond == 20 && npcId == NPC[0])
			if(player.getLevel() >= 76)
			{
				htmltext = "0-09.htm";
				if(getClassId(player) < 131 || getClassId(player) > 135)
					FinishQuest(st, player);
			}
			else
				htmltext = "0-010.htm";
		return htmltext;
	}

	@Override
	public String onFirstTalk(final NpcInstance npc, final Player player)
	{
		String htmltext = "";
		final QuestState st = player.getQuestState(getId());
		if(st == null)
			return htmltext;
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == NPC[4])
			if(cond == 17)
			{
				final QuestState st2 = findRightState(player, npc);
				if(st2 != null)
					if(st == st2)
					{
						if(st.getInt("Tab") == 1)
						{
							if(st.getInt("Quest0") == 0)
								htmltext = "4-04.htm";
							else if(st.getInt("Quest0") == 1)
								htmltext = "4-06.htm";
						}
						else if(st.getInt("Quest0") == 0)
							htmltext = "4-01.htm";
						else if(st.getInt("Quest0") == 1)
							htmltext = "4-03.htm";
					}
					else if(st.getInt("Tab") == 1)
					{
						if(st.getInt("Quest0") == 0)
							htmltext = "4-05.htm";
						else if(st.getInt("Quest0") == 1)
							htmltext = "4-07.htm";
					}
					else if(st.getInt("Quest0") == 0)
						htmltext = "4-02.htm";
			}
			else if(cond == 18)
				htmltext = "4-08.htm";
		return htmltext;
	}

	@Override
	public String onAttack(final NpcInstance npc, final QuestState st)
	{
		final Player player = st.getPlayer();
		if(st.getInt("cond") == 17 && npc.getNpcId() == Mob[2])
		{
			final QuestState st2 = findRightState(player, npc);
			if(st == st2)
			{
				st.set("Quest0", str(st.getInt("Quest0") + 1));
				if(st.getInt("Quest0") == 1)
					AutoChat(npc, Text[16].replace("PLAYERNAME", player.getName()));
				if(st.getInt("Quest0") > 15)
				{
					st.set("Quest0", "1");
					AutoChat(npc, Text[17].replace("PLAYERNAME", player.getName()));
					npc.reduceCurrentHp(9999999.0, npc, (Skill) null, 0, false, true, true, false, false, false, false, false);
					DeleteMySpawn(player, Mob[2]);
					QuestTimer qt = st.getQuestTimer("Mob_2 despawn");
					if(qt != null)
					{
						qt.cancel();
						qt = null;
					}
					st.set("Tab", "1");
				}
			}
		}
		return null;
	}

	protected boolean isArchonMinions(final int npcId)
	{
		for(final int id : Archon_Minions)
			if(id == npcId)
				return true;
		return false;
	}

	protected boolean isArchonHellishaNorm(final int npcId)
	{
		for(final int id : Archon_Hellisha_Norm)
			if(id == npcId)
				return true;
		return false;
	}

	protected boolean isGuardianAngels(final int npcId)
	{
		for(final int id : Guardian_Angels)
			if(id == npcId)
				return true;
		return false;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final Player player = st.getPlayer();
		if(player.getActiveClassId() != getPrevClass(player))
			return null;
		if(isArchonMinions(npcId))
		{
			final Party party = player.getParty();
			if(party != null)
			{
				for(final Player player2 : party.getPartyMembers())
					if(player2.getDistance(player) <= Config.ALT_PARTY_DISTRIBUTION_RANGE)
					{
						final QuestState st2 = findQuest(player2);
						if(st2 == null || st2.getCond() != 15)
							continue;
						((SagasSuperclass) st2.getQuest()).giveHallishaMark(st2);
					}
			}
			else
			{
				final QuestState st3 = findQuest(player);
				if(st3 != null && st3.getCond() == 15)
					((SagasSuperclass) st3.getQuest()).giveHallishaMark(st3);
			}
		}
		else if(isArchonHellishaNorm(npcId))
		{
			final QuestState st4 = findQuest(player);
			if(st4 != null && st4.getInt("cond") == 15)
			{
				AutoChat(npc, ((SagasSuperclass) st4.getQuest()).Text[4].replace("PLAYERNAME", st4.getPlayer().getName()));
				process_step_15to16(st4);
			}
		}
		else if(isGuardianAngels(npcId))
		{
			final QuestState st4 = findQuest(player);
			if(st4 != null && st4.getInt("cond") == 6)
				if(st4.getInt("kills") < 9)
					st4.set("kills", str(st4.getInt("kills") + 1));
				else
				{
					st4.playSound(Quest.SOUND_MIDDLE);
					st4.giveItems(((SagasSuperclass) st4.getQuest()).Items[5], 1L);
					st4.set("cond", "7");
				}
		}
		else
		{
			final int cond = st.getInt("cond");
			if(npcId == Mob[0] && cond == 8)
			{
				final QuestState st5 = findRightState(player, npc);
				if(st5 != null)
				{
					if(!player.isInParty() && st == st5)
					{
						AutoChat(npc, Text[12].replace("PLAYERNAME", player.getName()));
						st.giveItems(Items[6], 1L);
						st.set("cond", "9");
						st.playSound(Quest.SOUND_MIDDLE);
					}
					QuestTimer qt = st.getQuestTimer("Mob_1 has despawned");
					if(qt != null)
					{
						qt.cancel();
						qt = null;
					}
					DeleteMySpawn(st5.getPlayer(), Mob[0]);
				}
			}
			else if(npcId == Mob[1] && cond == 15)
			{
				final QuestState st5 = findRightState(player, npc);
				if(st5 != null)
				{
					if(!player.isInParty())
						if(st == st5)
						{
							AutoChat(npc, Text[4].replace("PLAYERNAME", player.getName()));
							process_step_15to16(st);
						}
						else
							AutoChat(npc, Text[5].replace("PLAYERNAME", player.getName()));
					QuestTimer qt = st.getQuestTimer("Archon Hellisha has despawned");
					if(qt != null)
					{
						qt.cancel();
						qt = null;
					}
					DeleteMySpawn(st5.getPlayer(), Mob[1]);
				}
			}
			else if(npcId == Mob[2] && cond == 17)
			{
				final QuestState st5 = findRightState(player, npc);
				if(st == st5)
				{
					st.set("Quest0", "1");
					AutoChat(npc, Text[17].replace("PLAYERNAME", player.getName()));
					npc.reduceCurrentHp(9999999.0, npc, (Skill) null, 0, false, true, true, false, false, false, false, false);
					DeleteMySpawn(player, Mob[2]);
					QuestTimer qt = st.getQuestTimer("Mob_2 despawn");
					if(qt != null)
					{
						qt.cancel();
						qt = null;
					}
					st.set("Tab", "1");
				}
			}
		}
		return null;
	}

	static
	{
		Quests = new HashIntSet();
		Quests.add(70);
		Quests.add(71);
		Quests.add(72);
		Quests.add(73);
		Quests.add(74);
		Quests.add(75);
		Quests.add(76);
		Quests.add(77);
		Quests.add(78);
		Quests.add(79);
		Quests.add(80);
		Quests.add(81);
		Quests.add(82);
		Quests.add(83);
		Quests.add(84);
		Quests.add(85);
		Quests.add(86);
		Quests.add(87);
		Quests.add(88);
		Quests.add(89);
		Quests.add(90);
		Quests.add(91);
		Quests.add(92);
		Quests.add(93);
		Quests.add(94);
		Quests.add(95);
		Quests.add(96);
		Quests.add(97);
		Quests.add(98);
		Quests.add(99);
		Quests.add(100);
		QuestClass = new int[][] {
				{ 127 },
				{ 128, 129 },
				{ 130 },
				{ 5 },
				{ 20 },
				{ 21 },
				{ 2 },
				{ 3 },
				{ 46 },
				{ 48 },
				{ 51 },
				{ 52 },
				{ 8 },
				{ 23 },
				{ 36 },
				{ 9 },
				{ 24 },
				{ 37 },
				{ 16 },
				{ 17 },
				{ 30 },
				{ 12 },
				{ 27 },
				{ 40 },
				{ 14 },
				{ 28 },
				{ 41 },
				{ 13 },
				{ 6 },
				{ 34 },
				{ 33 },
				{ 43 },
				{ 55 },
				{ 57 } };
	}

	private class Spawn
	{
		public final int npcId;
		public final int TimeToLive;
		public final long spawned_at;
		public final int charObjectId;
		public final int npcObjectId;

		public Spawn(NpcInstance npc, int charObjectId, int TimeToLive)
		{
			npcId = npc.getNpcId();
			npcObjectId = npc.getObjectId();
			this.charObjectId = charObjectId;
			this.TimeToLive = TimeToLive;
			spawned_at = System.currentTimeMillis();
		}

		public NpcInstance getNPC()
		{
			return GameObjectsStorage.getNpc(npcObjectId);
		}
	}

	public class SpawnCleaner implements Runnable
	{
		@Override
		public void run()
		{
			synchronized (Spawn_List)
			{
				final long curr_time = System.currentTimeMillis();
				for(final Spawn spawn : Spawn_List)
				{
					final NpcInstance npc = spawn.getNPC();
					if(curr_time - spawn.spawned_at > spawn.TimeToLive || npc == null)
					{
						if(npc != null)
							npc.deleteMe();
						Spawn_List.remove(spawn);
					}
				}
			}
		}
	}
}
