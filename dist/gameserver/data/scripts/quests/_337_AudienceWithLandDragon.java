package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _337_AudienceWithLandDragon extends Quest implements ScriptFile
{
	public final int MOKE = 30498;
	public final int HELTON = 30678;
	public final int CHAKIRIS = 30705;
	public final int KAIENA = 30720;
	public final int GABRIELLE = 30753;
	public final int GILMORE = 30754;
	public final int THEODRIC = 30755;
	public final int KENDRA = 30851;
	public final int ORVEN = 30857;
	public final int MARSH_STALKER = 20679;
	public final int MARSH_DRAKE = 20680;
	public final int BLOOD_QUEEN = 18002;
	public final int HARIT_LIZARDMAN_SHAMAN = 20644;
	public final int HARIT_LIZARDMAN_MATRIARCH = 20645;
	public final int HAMRUT = 20649;
	public final int KRANROT = 20650;
	public final int CAVE_MAIDEN = 20134;
	public final int CAVE_KEEPER = 20246;
	public final int ABYSSAL_JEWEL_1 = 27165;
	public final int ABYSSAL_JEWEL_2 = 27166;
	public final int ABYSSAL_JEWEL_3 = 27167;
	public final int JEWEL_GUARDIAN_MARA = 27168;
	public final int JEWEL_GUARDIAN_MUSFEL = 27169;
	public final int JEWEL_GUARDIAN_PYTON = 27170;
	public final int SACRIFICE_OF_THE_SACRIFICED = 27171;
	public final int HARIT_LIZARDMAN_ZEALOT = 27172;
	public final int FEATHER_OF_GABRIELLE_ID = 3852;
	public final int STALKER_HORN_ID = 3853;
	public final int DRAKE_TALON_ID = 3854;
	public final int REMAINS_OF_SACRIFICED_ID = 3857;
	public final int TOTEM_OF_LAND_DRAGON_ID = 3858;
	public final int HAMRUT_LEG_ID = 3856;
	public final int KRANROT_SKIN_ID = 3855;
	public final int MARA_FANG_ID = 3862;
	public final int MUSFEL_FANG_ID = 3863;
	public final int FIRST_ABYSS_FRAGMENT_ID = 3859;
	public final int SECOND_ABYSS_FRAGMENT_ID = 3860;
	public final int THIRD_ABYSS_FRAGMENT_ID = 3861;
	public final int HERALD_OF_SLAYER_ID = 3890;
	public final int PORTAL_STONE_ID = 3865;
	public final int MARK_OF_WATCHMAN_ID = 3864;
	public final int[][] DROPLIST;
	public final int[][] SPAWNLIST;
	public final int[][] JEWEL_DROP;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _337_AudienceWithLandDragon()
	{
		super(false);
		DROPLIST = new int[][] {
				{ 2, 20679, 3853, 1, 50, 1 },
				{ 2, 20680, 3854, 1, 50, 1 },
				{ 4, 27171, 3857, 1, 50, 1 },
				{ 6, 27172, 3858, 1, 70, 1 },
				{ 8, 20649, 3856, 1, 50, 1 },
				{ 8, 20650, 3855, 1, 50, 1 },
				{ 11, 27168, 3862, 1, 50, 1 },
				{ 13, 27169, 3863, 1, 70, 1 } };
		SPAWNLIST = new int[][] {
				{ 4, 18002, 27171, 6 },
				{ 6, 20644, 27172, 1 },
				{ 6, 20645, 27172, 1 },
				{ 11, 27165, 27168, 4 },
				{ 13, 27166, 27169, 4 },
				{ 16, 20246, 27167, 1 },
				{ 16, 20134, 27167, 1 },
				{ 16, 27167, 27170, 6 } };
		JEWEL_DROP = new int[][] { { 11, 27165, 3859, 1, 50, 1 }, { 13, 27166, 3860, 1, 50, 1 } };
		this.addStartNpc(30753);
		this.addTalkId(new int[] { 30498 });
		this.addTalkId(new int[] { 30678 });
		this.addTalkId(new int[] { 30705 });
		this.addTalkId(new int[] { 30720 });
		this.addTalkId(new int[] { 30753 });
		this.addTalkId(new int[] { 30754 });
		this.addTalkId(new int[] { 30755 });
		this.addTalkId(new int[] { 30851 });
		this.addTalkId(new int[] { 30857 });
		this.addKillId(new int[] { 18002 });
		this.addKillId(new int[] { 20679 });
		this.addKillId(new int[] { 20680 });
		this.addKillId(new int[] { 27171 });
		this.addKillId(new int[] { 20644 });
		this.addKillId(new int[] { 20645 });
		this.addKillId(new int[] { 27172 });
		this.addKillId(new int[] { 20649 });
		this.addKillId(new int[] { 20650 });
		this.addKillId(new int[] { 27165 });
		this.addKillId(new int[] { 27166 });
		this.addKillId(new int[] { 20246 });
		this.addKillId(new int[] { 20134 });
		this.addKillId(new int[] { 27167 });
		this.addKillId(new int[] { 27168 });
		this.addKillId(new int[] { 27169 });
		this.addKillId(new int[] { 27170 });
		addAttackId(new int[] { 27165 });
		addAttackId(new int[] { 27166 });
		addAttackId(new int[] { 27167 });
		addQuestItem(new int[] { 3852, 3890, 3853, 3854, 3857, 3858, 3856, 3855, 3862, 3859, 3863, 3860, 3861, 3864 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("1"))
		{
			st.set("step", "1");
			st.set("cond", "1");
			st.set("stage2", "0");
			st.set("stage4", "0");
			st.set("stage6", "0");
			st.set("stage8", "0");
			st.set("stage9", "0");
			st.set("start1", "0");
			st.set("start2", "0");
			st.set("start3", "0");
			st.set("start4", "0");
			st.setState(2);
			st.giveItems(3852, 1L);
			htmltext = "30753-02.htm";
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("2"))
		{
			st.set("step", "2");
			st.set("start1", "1");
			htmltext = "30720-02.htm";
		}
		else if(event.equalsIgnoreCase("4"))
		{
			st.set("step", "4");
			st.set("start2", "1");
			htmltext = "30857-02.htm";
		}
		else if(event.equalsIgnoreCase("6"))
		{
			st.set("step", "6");
			st.set("start3", "1");
			htmltext = "30851-02.htm";
		}
		else if(event.equalsIgnoreCase("8"))
		{
			st.set("step", "8");
			st.set("start4", "1");
			htmltext = "30705-02.htm";
		}
		else if(event.equalsIgnoreCase("10"))
		{
			st.takeItems(3864, -1L);
			st.set("step", "10");
			st.set("cond", "2");
			htmltext = "30753-05.htm";
		}
		else if(event.equalsIgnoreCase("11"))
		{
			st.set("step", "11");
			htmltext = "30498-02.htm";
		}
		else if(event.equalsIgnoreCase("13"))
		{
			st.set("step", "13");
			htmltext = "30678-02.htm";
		}
		else if(event.equalsIgnoreCase("15"))
		{
			st.set("step", "15");
			st.set("cond", "3");
			htmltext = "30753-06.htm";
			st.takeItems(3864, -1L);
			st.takeItems(3852, -1L);
			st.giveItems(3890, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("16"))
		{
			st.set("step", "16");
			st.set("cond", "4");
			htmltext = "30754-02.htm";
			st.takeItems(3890, -1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int step = st.getInt("step");
		final int cond = st.getInt("cond");
		if(npcId == 30753)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 50)
				{
					htmltext = "30753-00.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "30753-01.htm";
			}
			else if(st.getInt("stage2") == 0 || st.getInt("stage4") == 0 || st.getInt("stage6") == 0 || st.getInt("stage8") == 0)
				htmltext = "30753-02.htm";
			else if(cond == 1 && st.getInt("stage2") > 0 && st.getInt("stage4") > 0 && st.getInt("stage6") > 0 && st.getInt("stage8") > 0)
				htmltext = "30753-03.htm";
			else if(step > 9 && step < 14)
				htmltext = "30753-05.htm";
			else if(step == 14)
				htmltext = "30753-04.htm";
			else if(step > 14)
				htmltext = "30753-06.htm";
		}
		else if(npcId == 30720 && cond == 1 && step > 0)
		{
			if((st.getQuestItemsCount(3853) < 1L || st.getQuestItemsCount(3854) < 1L) && st.getInt("start1") < 1)
				htmltext = "30720-01.htm";
			else if(st.getQuestItemsCount(3853) > 0L && st.getQuestItemsCount(3854) > 0L)
			{
				htmltext = "30720-03.htm";
				st.takeItems(3853, -1L);
				st.takeItems(3854, -1L);
				st.giveItems(3864, 1L);
				st.set("step", "3");
				st.set("stage2", "2");
				st.set("start1", "1");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(st.getInt("stage2") == 0)
				htmltext = "30720-02.htm";
			else if(step == 3)
				htmltext = "30720-03.htm";
		}
		else if(npcId == 30857 && cond == 1 && step > 0)
		{
			if(st.getQuestItemsCount(3857) < 1L && st.getInt("start2") < 1)
				htmltext = "30857-01.htm";
			else if(st.getQuestItemsCount(3857) > 0L)
			{
				htmltext = "30857-03.htm";
				st.takeItems(3857, -1L);
				st.giveItems(3864, 1L);
				st.set("step", "5");
				st.set("stage4", "4");
				st.set("start2", "1");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(st.getInt("stage4") == 0)
				htmltext = "30857-02.htm";
			else if(step == 5)
				htmltext = "30857-03.htm";
		}
		else if(npcId == 30851 && cond == 1 && step > 0)
		{
			if(st.getQuestItemsCount(3858) < 1L && st.getInt("start3") < 1)
				htmltext = "30851-01.htm";
			else if(st.getQuestItemsCount(3858) > 0L)
			{
				htmltext = "30851-03.htm";
				st.takeItems(3858, -1L);
				st.giveItems(3864, 1L);
				st.set("step", "7");
				st.set("stage6", "6");
				st.set("start3", "1");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(st.getInt("stage6") == 0)
				htmltext = "30851-02.htm";
			else if(step == 7)
				htmltext = "30851-03.htm";
		}
		else if(npcId == 30705 && cond == 1 && step > 0)
		{
			if((st.getQuestItemsCount(3856) < 1L || st.getQuestItemsCount(3855) < 1L) && st.getInt("start4") < 1)
				htmltext = "30705-01.htm";
			else if(st.getQuestItemsCount(3856) > 0L && st.getQuestItemsCount(3855) > 0L)
			{
				htmltext = "30705-03.htm";
				st.takeItems(3856, -1L);
				st.takeItems(3855, -1L);
				st.giveItems(3864, 1L);
				st.set("step", "9");
				st.set("stage8", "8");
				st.set("start4", "1");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(st.getInt("stage8") == 0)
				htmltext = "30705-02.htm";
			else if(st.getInt("stage2") > 0 && st.getInt("stage4") > 0 && st.getInt("stage6") > 0 && st.getInt("stage8") > 0)
				htmltext = "30705-03.htm";
		}
		else if(npcId == 30498 && cond == 2 && step < 13)
		{
			if(st.getQuestItemsCount(3862) < 1L && st.getQuestItemsCount(3859) < 1L && step == 10)
				htmltext = "30498-01.htm";
			else if(st.getQuestItemsCount(3862) > 0L && st.getQuestItemsCount(3859) > 0L)
			{
				htmltext = "30498-03.htm";
				st.takeItems(3862, -1L);
				st.takeItems(3859, -1L);
				st.giveItems(3864, 1L);
				st.set("step", "12");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(step == 11)
				htmltext = "30498-02.htm";
			else if(step == 12)
				htmltext = "30498-03.htm";
		}
		else if(npcId == 30678 && cond == 2 && step > 11 && step < 15)
		{
			if(st.getQuestItemsCount(3863) < 1L && st.getQuestItemsCount(3860) < 1L && step == 12)
				htmltext = "30678-01.htm";
			else if(st.getQuestItemsCount(3863) > 0L && st.getQuestItemsCount(3860) > 0L)
			{
				htmltext = "30678-03.htm";
				st.takeItems(3863, -1L);
				st.takeItems(3860, -1L);
				st.giveItems(3864, 1L);
				st.set("step", "14");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(step == 13)
				htmltext = "30678-02.htm";
			else if(step == 14)
				htmltext = "30678-03.htm";
		}
		else if(npcId == 30754 && step < 17)
		{
			if(st.getQuestItemsCount(3890) > 0L && cond == 3)
				htmltext = "30754-01.htm";
			else if(cond == 4)
				htmltext = "30754-02.htm";
		}
		else if(npcId == 30755 && cond == 4 && step == 16)
			if(st.getQuestItemsCount(3861) < 1L)
				htmltext = "30755-02.htm";
			else
			{
				htmltext = "30755-01.htm";
				st.takeItems(3861, -1L);
				st.unset("step");
				st.unset("cond");
				st.unset("stage2");
				st.unset("stage4");
				st.unset("stage6");
				st.unset("stage8");
				st.unset("stage9");
				st.unset("start1");
				st.unset("start2");
				st.unset("start3");
				st.unset("start4");
				st.exitCurrentQuest(true);
				st.giveItems(3865, 1L);
				st.playSound(Quest.SOUND_FINISH);
			}
		return htmltext;
	}

	@Override
	public String onAttack(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int step = st.getInt("step");
		for(final int[] element : SPAWNLIST)
			if(npcId == element[1] && (step > 9 && step == element[0] || step >= 1 && step <= 9 && element[0] != st.getInt("stage2") && element[0] != st.getInt("stage4") && element[0] != st.getInt("stage6") && element[0] != st.getInt("stage8")) && npc.getCurrentHpPercents() < 60.0 && !npc.questis.contains(st.getPlayer().getObjectId()))
			{
				if(npcId == 27167)
					st.set("stage9", "1");
				npc.questis.add(st.getPlayer().getObjectId());
				for(int j = 0; j < element[3]; ++j)
					st.addSpawn(element[2]);
				st.playSound(Quest.SOUND_BEFORE_BATTLE);
			}
		for(final int[] element : JEWEL_DROP)
			if(npcId == element[1] && step == element[0] && st.getQuestItemsCount(element[2]) < element[3] && (npc.getCurrentHpPercents() <= 85.0 && Rnd.chance(element[4]) || npc.getCurrentHpPercents() <= 30.0))
			{
				st.giveItems(element[2], element[5]);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		return null;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int step = st.getInt("step");
		if(step == 16 && st.getInt("cond") == 4 && npcId == 27170 && st.getInt("stage9") > 0 && st.getQuestItemsCount(3861) < 1L && Rnd.chance(50))
		{
			st.giveItems(3861, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		for(final int[] element : DROPLIST)
			if(npcId == element[1] && (step > 9 && step == element[0] || step >= 1 && step <= 9 && element[0] != st.getInt("stage2") && element[0] != st.getInt("stage4") && element[0] != st.getInt("stage6") && element[0] != st.getInt("stage8")) && st.getQuestItemsCount(element[2]) < element[3] && Rnd.chance(element[4]))
			{
				st.giveItems(element[2], element[5]);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		for(final int[] element : SPAWNLIST)
			if(npcId == element[1] && (step > 9 && step == element[0] || step >= 1 && step <= 9 && element[0] != st.getInt("stage2") && element[0] != st.getInt("stage4") && element[0] != st.getInt("stage6") && element[0] != st.getInt("stage8")) && Rnd.chance(50) && !npc.questis.contains(st.getPlayer().getObjectId()))
			{
				for(int j = 0; j < element[3]; ++j)
					st.addSpawn(element[2]);
				st.playSound(Quest.SOUND_BEFORE_BATTLE);
			}
		return null;
	}
}
