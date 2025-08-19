package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _215_TrialOfPilgrim extends Quest implements ScriptFile
{
	private static final int MARK_OF_PILGRIM_ID = 2721;
	private static final int BOOK_OF_SAGE_ID = 2722;
	private static final int VOUCHER_OF_TRIAL_ID = 2723;
	private static final int SPIRIT_OF_FLAME_ID = 2724;
	private static final int ESSENSE_OF_FLAME_ID = 2725;
	private static final int BOOK_OF_GERALD_ID = 2726;
	private static final int GREY_BADGE_ID = 2727;
	private static final int PICTURE_OF_NAHIR_ID = 2728;
	private static final int HAIR_OF_NAHIR_ID = 2729;
	private static final int STATUE_OF_EINHASAD_ID = 2730;
	private static final int BOOK_OF_DARKNESS_ID = 2731;
	private static final int DEBRIS_OF_WILLOW_ID = 2732;
	private static final int TAG_OF_RUMOR_ID = 2733;
	private static final int ADENA_ID = 57;
	private static final int RewardExp = 629125;
	private static final int RewardSP = 40803;
	private static final int RewardAdena = 114649;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _215_TrialOfPilgrim()
	{
		super(false);
		this.addStartNpc(30648);
		this.addTalkId(new int[] { 30036 });
		this.addTalkId(new int[] { 30117 });
		this.addTalkId(new int[] { 30362 });
		this.addTalkId(new int[] { 30550 });
		this.addTalkId(new int[] { 30571 });
		this.addTalkId(new int[] { 30612 });
		this.addTalkId(new int[] { 30648 });
		this.addTalkId(new int[] { 30649 });
		this.addTalkId(new int[] { 30650 });
		this.addTalkId(new int[] { 30651 });
		this.addTalkId(new int[] { 30652 });
		this.addKillId(new int[] { 27116 });
		this.addKillId(new int[] { 27117 });
		this.addKillId(new int[] { 27118 });
		addQuestItem(new int[] { 2722, 2723, 2725, 2726, 2733, 2728, 2729, 2731, 2732, 2727, 2724, 2730 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("1"))
		{
			htmltext = "hermit_santiago_q0215_04.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.giveItems(2723, 1L);
		}
		if(!st.getPlayer().getVarBoolean("dd1"))
		{
			st.giveItems(7562, 64L);
			st.getPlayer().setVar("dd1", "1");
		}
		else if(event.equals("30648_1"))
			htmltext = "hermit_santiago_q0215_05.htm";
		else if(event.equals("30648_2"))
			htmltext = "hermit_santiago_q0215_06.htm";
		else if(event.equals("30648_3"))
			htmltext = "hermit_santiago_q0215_07.htm";
		else if(event.equals("30648_4"))
			htmltext = "hermit_santiago_q0215_08.htm";
		else if(event.equals("30648_5"))
			htmltext = "hermit_santiago_q0215_05.htm";
		else if(event.equals("30649_1"))
		{
			htmltext = "ancestor_martankus_q0215_04.htm";
			st.giveItems(2724, 1L);
			st.takeItems(2725, 1L);
			st.set("cond", "5");
		}
		else if(event.equals("30650_1"))
		{
			if(st.getQuestItemsCount(57) >= 100000L)
			{
				htmltext = "gerald_priest_of_earth_q0215_02.htm";
				st.giveItems(2726, 1L);
				st.takeItems(57, 100000L);
				st.set("cond", "7");
			}
			else
				htmltext = "gerald_priest_of_earth_q0215_03.htm";
		}
		else if(event.equals("30650_2"))
			htmltext = "gerald_priest_of_earth_q0215_03.htm";
		else if(event.equals("30362_1"))
		{
			htmltext = "andellria_q0215_05.htm";
			st.takeItems(2731, 1L);
			st.set("cond", "16");
		}
		else if(event.equals("30362_2"))
		{
			htmltext = "andellria_q0215_04.htm";
			st.set("cond", "16");
		}
		else if(event.equals("30652_1"))
		{
			htmltext = "uruha_q0215_02.htm";
			st.giveItems(2731, 1L);
			st.takeItems(2732, 1L);
			st.set("cond", "15");
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(2721) > 0L)
		{
			st.exitCurrentQuest(true);
			return "completed";
		}
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		if(id == 1)
		{
			st.setState(2);
			st.set("cond", "0");
			st.set("id", "0");
		}
		if(npcId == 30648 && st.getInt("cond") == 0)
		{
			if(st.getPlayer().getClassId().getId() == 15 || st.getPlayer().getClassId().getId() == 29 || st.getPlayer().getClassId().getId() == 42 || st.getPlayer().getClassId().getId() == 50)
			{
				if(st.getPlayer().getLevel() >= 35)
					htmltext = "hermit_santiago_q0215_03.htm";
				else
				{
					htmltext = "hermit_santiago_q0215_01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "hermit_santiago_q0215_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == 30648 && st.getInt("cond") == 1 && st.getQuestItemsCount(2723) > 0L)
			htmltext = "hermit_santiago_q0215_09.htm";
		else if(npcId == 30648 && st.getInt("cond") == 17 && st.getQuestItemsCount(2722) > 0L)
		{
			htmltext = "hermit_santiago_q0215_10.htm";
			st.takeItems(2722, -1L);
			st.giveItems(2721, 1L);
			if(!st.getPlayer().getVarBoolean("prof2.1"))
			{
				st.addExpAndSp(629125L, 40803L, true);
				st.giveItems(57, 114649L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
				st.getPlayer().setVar("prof2.1", "1");
			}
			st.playSound(Quest.SOUND_FINISH);
			st.unset("cond");
			st.exitCurrentQuest(false);
		}
		else if(npcId == 30571 && st.getInt("cond") == 1 && st.getQuestItemsCount(2723) > 0L)
		{
			htmltext = "seer_tanapi_q0215_01.htm";
			st.takeItems(2723, 1L);
			st.set("cond", "2");
		}
		else if(npcId == 30571 && st.getInt("cond") == 2)
			htmltext = "seer_tanapi_q0215_02.htm";
		else if(npcId == 30571 && st.getInt("cond") == 5 && st.getQuestItemsCount(2724) > 0L)
			htmltext = "seer_tanapi_q0215_03.htm";
		else if(npcId == 30649 && st.getInt("cond") == 2)
		{
			htmltext = "ancestor_martankus_q0215_01.htm";
			st.set("cond", "3");
		}
		else if(npcId == 30649 && st.getInt("cond") == 3)
			htmltext = "ancestor_martankus_q0215_02.htm";
		else if(npcId == 30649 && st.getInt("cond") == 4 && st.getQuestItemsCount(2725) > 0L)
			htmltext = "ancestor_martankus_q0215_03.htm";
		else if(npcId == 30550 && st.getInt("cond") == 5 && st.getQuestItemsCount(2724) > 0L)
		{
			htmltext = "gauri_twinklerock_q0215_01.htm";
			st.giveItems(2733, 1L);
			st.set("cond", "6");
		}
		else if(npcId == 30550 && st.getInt("cond") == 6)
			htmltext = "gauri_twinklerock_q0215_02.htm";
		else if(npcId == 30650 && st.getInt("cond") == 6 && st.getQuestItemsCount(2733) > 0L)
			htmltext = "gerald_priest_of_earth_q0215_01.htm";
		else if(npcId == 30650 && st.getInt("cond") >= 8 && st.getQuestItemsCount(2727) > 0L && st.getQuestItemsCount(2726) > 0L)
		{
			htmltext = "gerald_priest_of_earth_q0215_04.htm";
			st.giveItems(57, 100000L, false);
			st.takeItems(2726, 1L);
		}
		else if(npcId == 30651 && st.getInt("cond") == 6 && st.getQuestItemsCount(2733) > 0L)
		{
			htmltext = "wanderer_dorf_q0215_01.htm";
			st.giveItems(2727, 1L);
			st.takeItems(2733, 1L);
			st.set("cond", "8");
		}
		else if(npcId == 30651 && st.getInt("cond") == 7 && st.getQuestItemsCount(2733) > 0L)
		{
			htmltext = "wanderer_dorf_q0215_02.htm";
			st.giveItems(2727, 1L);
			st.takeItems(2733, 1L);
			st.set("cond", "8");
		}
		else if(npcId == 30651 && st.getInt("cond") == 8)
			htmltext = "wanderer_dorf_q0215_03.htm";
		else if(npcId == 30117 && st.getInt("cond") == 8)
		{
			htmltext = "primoz_q0215_01.htm";
			st.set("cond", "9");
		}
		else if(npcId == 30117 && st.getInt("cond") == 9)
			htmltext = "primoz_q0215_02.htm";
		else if(npcId == 30036 && st.getInt("cond") == 9)
		{
			htmltext = "potter_q0215_01.htm";
			st.giveItems(2728, 1L);
			st.set("cond", "10");
		}
		else if(npcId == 30036 && st.getInt("cond") == 10)
			htmltext = "potter_q0215_02.htm";
		else if(npcId == 30036 && st.getInt("cond") == 11)
		{
			htmltext = "potter_q0215_03.htm";
			st.giveItems(2730, 1L);
			st.takeItems(2728, 1L);
			st.takeItems(2729, 1L);
			st.set("cond", "12");
		}
		else if(npcId == 30036 && st.getInt("cond") == 12 && st.getQuestItemsCount(2730) > 0L)
			htmltext = "potter_q0215_04.htm";
		else if(npcId == 30362 && st.getInt("cond") == 12)
		{
			htmltext = "andellria_q0215_01.htm";
			st.set("cond", "13");
		}
		else if(npcId == 30362 && st.getInt("cond") == 13)
			htmltext = "andellria_q0215_02.htm";
		else if(npcId == 30362 && st.getInt("cond") == 15 && st.getQuestItemsCount(2731) > 0L)
			htmltext = "andellria_q0215_03.htm";
		else if(npcId == 30362 && st.getInt("cond") == 16)
			htmltext = "andellria_q0215_06.htm";
		else if(npcId == 30362 && st.getInt("cond") == 15 && st.getQuestItemsCount(2731) == 0L)
			htmltext = "andellria_q0215_07.htm";
		else if(npcId == 30652 && st.getInt("cond") == 14 && st.getQuestItemsCount(2732) > 0L)
			htmltext = "uruha_q0215_01.htm";
		else if(npcId == 30652 && st.getInt("cond") == 15 && st.getQuestItemsCount(2731) > 0L)
			htmltext = "uruha_q0215_03.htm";
		else if(npcId == 30612 && st.getInt("cond") == 16)
		{
			htmltext = "sage_kasian_q0215_01.htm";
			st.giveItems(2722, 1L);
			if(st.getQuestItemsCount(2731) > 0L)
				st.takeItems(2731, 1L);
			if(st.getQuestItemsCount(2726) > 0L)
				st.takeItems(2726, 1L);
			st.set("cond", "17");
			st.takeItems(2727, 1L);
			st.takeItems(2724, 1L);
			st.takeItems(2730, 1L);
		}
		else if(npcId == 30612 && st.getInt("cond") == 17)
			htmltext = "sage_kasian_q0215_02.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(npcId == 27116)
		{
			if(st.getInt("cond") == 3 && st.getQuestItemsCount(2725) == 0L && Rnd.chance(30))
			{
				st.giveItems(2725, 1L);
				st.set("cond", "4");
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		else if(npcId == 27117)
		{
			if(st.getInt("cond") == 10 && st.getQuestItemsCount(2729) == 0L)
			{
				st.giveItems(2729, 1L);
				st.set("cond", "11");
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		else if(npcId == 27118 && st.getInt("cond") == 13 && st.getQuestItemsCount(2732) == 0L && Rnd.chance(20))
		{
			st.giveItems(2732, 1L);
			st.set("cond", "14");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		return null;
	}
}
