package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _412_PathToDarkwizard extends Quest implements ScriptFile
{
	public final int CHARKEREN = 30415;
	public final int ANNIKA = 30418;
	public final int ARKENIA = 30419;
	public final int VARIKA = 30421;
	public final int MARSH_ZOMBIE = 20015;
	public final int MARSH_ZOMBIE_LORD = 20020;
	public final int MISERY_SKELETON = 20022;
	public final int SKELETON_SCOUT = 20045;
	public final int SKELETON_HUNTER = 20517;
	public final int SKELETON_HUNTER_ARCHER = 20518;
	public final int SEEDS_OF_DESPAIR_ID = 1254;
	public final int SEEDS_OF_ANGER_ID = 1253;
	public final int SEEDS_OF_HORROR_ID = 1255;
	public final int SEEDS_OF_LUNACY_ID = 1256;
	public final int FAMILYS_ASHES_ID = 1257;
	public final int KNEE_BONE_ID = 1259;
	public final int HEART_OF_LUNACY_ID = 1260;
	public final int JEWEL_OF_DARKNESS_ID = 1261;
	public final int LUCKY_KEY_ID = 1277;
	public final int CANDLE_ID = 1278;
	public final int HUB_SCENT_ID = 1279;
	public final int[][] DROPLIST;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _412_PathToDarkwizard()
	{
		super(false);
		DROPLIST = new int[][] {
				{ 20015, 1277, 1257, 3 },
				{ 20020, 1277, 1257, 3 },
				{ 20517, 1278, 1259, 2 },
				{ 20518, 1278, 1259, 2 },
				{ 20022, 1278, 1259, 2 },
				{ 20045, 1279, 1260, 3 } };
		this.addStartNpc(30421);
		this.addTalkId(new int[] { 30415 });
		this.addTalkId(new int[] { 30418 });
		this.addTalkId(new int[] { 30419 });
		addQuestItem(new int[] { 1253 });
		addQuestItem(new int[] { 1277 });
		addQuestItem(new int[] { 1255 });
		addQuestItem(new int[] { 1278 });
		addQuestItem(new int[] { 1256 });
		addQuestItem(new int[] { 1279 });
		addQuestItem(new int[] { 1254 });
		addQuestItem(new int[] { 1257 });
		addQuestItem(new int[] { 1259 });
		addQuestItem(new int[] { 1260 });
		for(final int[] element : DROPLIST)
			this.addKillId(new int[] { element[0] });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("1"))
		{
			if(st.getPlayer().getLevel() >= 18 && st.getPlayer().getClassId().getId() == 38 && st.getQuestItemsCount(1261) < 1L)
			{
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
				st.giveItems(1254, 1L);
				htmltext = "varika_q0412_05.htm";
			}
			else if(st.getPlayer().getClassId().getId() != 38)
			{
				if(st.getPlayer().getClassId().getId() == 39)
					htmltext = "varika_q0412_02a.htm";
				else
					htmltext = "varika_q0412_03.htm";
			}
			else if(st.getPlayer().getLevel() < 18 && st.getPlayer().getClassId().getId() == 38)
				htmltext = "varika_q0412_02.htm";
			else if(st.getPlayer().getLevel() >= 18 && st.getPlayer().getClassId().getId() == 38 && st.getQuestItemsCount(1261) > 0L)
				htmltext = "varika_q0412_04.htm";
		}
		else if(event.equalsIgnoreCase("412_1"))
		{
			if(st.getQuestItemsCount(1253) > 0L)
				htmltext = "varika_q0412_06.htm";
			else
				htmltext = "varika_q0412_07.htm";
		}
		else if(event.equalsIgnoreCase("412_2"))
		{
			if(st.getQuestItemsCount(1255) > 0L)
				htmltext = "varika_q0412_09.htm";
			else
				htmltext = "varika_q0412_10.htm";
		}
		else if(event.equalsIgnoreCase("412_3"))
		{
			if(st.getQuestItemsCount(1256) > 0L)
				htmltext = "varika_q0412_12.htm";
			else if(st.getQuestItemsCount(1256) < 1L && st.getQuestItemsCount(1254) > 0L)
				htmltext = "varika_q0412_13.htm";
		}
		else if(event.equalsIgnoreCase("412_4"))
		{
			htmltext = "charkeren_q0412_03.htm";
			st.giveItems(1277, 1L);
		}
		else if(event.equalsIgnoreCase("30418_1"))
		{
			htmltext = "annsery_q0412_02.htm";
			st.giveItems(1278, 1L);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30421)
		{
			if(cond < 1)
			{
				if(st.getQuestItemsCount(1261) < 1L)
					htmltext = "varika_q0412_01.htm";
				else
					htmltext = "varika_q0412_04.htm";
			}
			else if(st.getQuestItemsCount(1254) > 0L && st.getQuestItemsCount(1255) > 0L && st.getQuestItemsCount(1256) > 0L && st.getQuestItemsCount(1253) > 0L)
			{
				htmltext = "varika_q0412_16.htm";
				if(st.getPlayer().getClassId().getLevel() == 1)
				{
					st.giveItems(1261, 1L);
					if(!st.getPlayer().getVarBoolean("prof1"))
					{
						st.getPlayer().setVar("prof1", "1");
						st.addExpAndSp(295862L, 17664L, true);
						st.giveItems(57, 81900L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
					}
				}
				st.exitCurrentQuest(true);
				st.playSound(Quest.SOUND_FINISH);
			}
			else if(st.getQuestItemsCount(1254) > 0L)
				if(st.getQuestItemsCount(1257) < 1L && st.getQuestItemsCount(1277) < 1L && st.getQuestItemsCount(1278) < 1L && st.getQuestItemsCount(1279) < 1L && st.getQuestItemsCount(1259) < 1L && st.getQuestItemsCount(1260) < 1L)
					htmltext = "varika_q0412_17.htm";
				else if(st.getQuestItemsCount(1253) < 1L)
					htmltext = "varika_q0412_08.htm";
				else if(st.getQuestItemsCount(1255) > 0L)
					htmltext = "varika_q0412_19.htm";
				else if(st.getQuestItemsCount(1260) < 1L)
					htmltext = "varika_q0412_13.htm";
		}
		else if(npcId == 30419 && cond > 0 && st.getQuestItemsCount(1256) < 1L)
		{
			if(st.getQuestItemsCount(1279) < 1L && st.getQuestItemsCount(1260) < 1L)
			{
				htmltext = "arkenia_q0412_01.htm";
				st.giveItems(1279, 1L);
			}
			else if(st.getQuestItemsCount(1279) > 0L && st.getQuestItemsCount(1260) < 3L)
				htmltext = "arkenia_q0412_02.htm";
			else if(st.getQuestItemsCount(1279) > 0L && st.getQuestItemsCount(1260) >= 3L)
			{
				htmltext = "arkenia_q0412_03.htm";
				st.giveItems(1256, 1L);
				st.takeItems(1260, -1L);
				st.takeItems(1279, -1L);
			}
		}
		else if(npcId == 30415 && cond > 0)
		{
			if(st.getQuestItemsCount(1253) < 1L)
			{
				if(st.getQuestItemsCount(1254) > 0L && st.getQuestItemsCount(1257) < 1L && st.getQuestItemsCount(1277) < 1L)
					htmltext = "charkeren_q0412_01.htm";
				else if(st.getQuestItemsCount(1254) > 0L && st.getQuestItemsCount(1257) < 3L && st.getQuestItemsCount(1277) > 0L)
					htmltext = "charkeren_q0412_04.htm";
				else if(st.getQuestItemsCount(1254) > 0L && st.getQuestItemsCount(1257) >= 3L && st.getQuestItemsCount(1277) > 0L)
				{
					htmltext = "charkeren_q0412_05.htm";
					st.giveItems(1253, 1L);
					st.takeItems(1257, -1L);
					st.takeItems(1277, -1L);
				}
			}
			else
				htmltext = "charkeren_q0412_06.htm";
		}
		else if(npcId == 30418 && cond > 0 && st.getQuestItemsCount(1255) < 1L)
			if(st.getQuestItemsCount(1254) > 0L && st.getQuestItemsCount(1278) < 1L && st.getQuestItemsCount(1259) < 1L)
				htmltext = "annsery_q0412_01.htm";
			else if(st.getQuestItemsCount(1254) > 0L && st.getQuestItemsCount(1278) > 0L && st.getQuestItemsCount(1259) < 2L)
				htmltext = "annsery_q0412_03.htm";
			else if(st.getQuestItemsCount(1254) > 0L && st.getQuestItemsCount(1278) > 0L && st.getQuestItemsCount(1259) >= 2L)
			{
				htmltext = "annsery_q0412_04.htm";
				st.giveItems(1255, 1L);
				st.takeItems(1278, -1L);
				st.takeItems(1259, -1L);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		for(final int[] element : DROPLIST)
			if(st.getInt("cond") == 1 && npc.getNpcId() == element[0] && st.getQuestItemsCount(element[1]) > 0L && Rnd.chance(50) && st.getQuestItemsCount(element[2]) < element[3])
			{
				st.giveItems(element[2], 1L);
				if(st.getQuestItemsCount(element[2]) == element[3])
					st.playSound(Quest.SOUND_MIDDLE);
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
		return null;
	}
}
