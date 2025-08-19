package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _038_DragonFangs extends Quest implements ScriptFile
{
	public final int ROHMER = 30344;
	public final int LUIS = 30386;
	public final int IRIS = 30034;
	public final int FEATHER_ORNAMENT = 7173;
	public final int TOOTH_OF_TOTEM = 7174;
	public final int LETTER_OF_IRIS = 7176;
	public final int LETTER_OF_ROHMER = 7177;
	public final int TOOTH_OF_DRAGON = 7175;
	public final int LANGK_LIZARDMAN_LIEUTENANT = 20357;
	public final int LANGK_LIZARDMAN_SENTINEL = 21100;
	public final int LANGK_LIZARDMAN_LEADER = 20356;
	public final int LANGK_LIZARDMAN_SHAMAN = 21101;
	public final int CHANCE_FOR_QUEST_ITEMS = 100;
	public final int BONE_HELMET = 45;
	public final int ASSAULT_BOOTS = 1125;
	public final int BLUE_BUCKSKIN_BOOTS = 1123;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _038_DragonFangs()
	{
		super(false);
		this.addStartNpc(30386);
		this.addTalkId(new int[] { 30034 });
		this.addTalkId(new int[] { 30344 });
		this.addKillId(new int[] { 20356 });
		this.addKillId(new int[] { 21101 });
		this.addKillId(new int[] { 21100 });
		this.addKillId(new int[] { 20357 });
		addQuestItem(new int[] { 7174, 7176, 7177, 7175, 7173 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int cond = st.getInt("cond");
		if(event.equals("guard_luis_q0038_0104.htm") && cond == 0)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		if(event.equals("guard_luis_q0038_0201.htm") && cond == 2)
		{
			st.set("cond", "3");
			st.takeItems(7173, 100L);
			st.giveItems(7174, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		if(event.equals("iris_q0038_0301.htm") && cond == 3)
		{
			st.set("cond", "4");
			st.takeItems(7174, 1L);
			st.giveItems(7176, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		if(event.equals("magister_roh_q0038_0401.htm") && cond == 4)
		{
			st.set("cond", "5");
			st.takeItems(7176, 1L);
			st.giveItems(7177, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		if(event.equals("iris_q0038_0501.htm") && cond == 5)
		{
			st.set("cond", "6");
			st.takeItems(7177, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		if(event.equals("iris_q0038_0601.htm") && cond == 7)
		{
			st.takeItems(7175, 50L);
			final int luck = Rnd.get(3);
			if(luck == 0)
			{
				st.giveItems(1123, 1L);
				st.giveItems(57, 1500L);
			}
			if(luck == 1)
			{
				st.giveItems(45, 1L);
				st.giveItems(57, 5200L);
			}
			if(luck == 2)
			{
				st.giveItems(1125, 1L);
				st.giveItems(57, 1500L);
			}
			st.addExpAndSp(435117L, 23977L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30386 && cond == 0)
			if(st.getPlayer().getLevel() < 19)
			{
				htmltext = "guard_luis_q0038_0102.htm";
				st.exitCurrentQuest(true);
			}
			else if(st.getPlayer().getLevel() >= 19)
				htmltext = "guard_luis_q0038_0101.htm";
		if(npcId == 30386 && cond == 1)
			htmltext = "guard_luis_q0038_0202.htm";
		if(npcId == 30386 && cond == 2 && st.getQuestItemsCount(7173) == 100L)
			htmltext = "guard_luis_q0038_0105.htm";
		if(npcId == 30386 && cond == 3)
			htmltext = "guard_luis_q0038_0203.htm";
		if(npcId == 30034 && cond == 3 && st.getQuestItemsCount(7174) == 1L)
			htmltext = "iris_q0038_0201.htm";
		if(npcId == 30034 && cond == 4)
			htmltext = "iris_q0038_0303.htm";
		if(npcId == 30034 && cond == 5 && st.getQuestItemsCount(7177) == 1L)
			htmltext = "iris_q0038_0401.htm";
		if(npcId == 30034 && cond == 6)
			htmltext = "iris_q0038_0602.htm";
		if(npcId == 30034 && cond == 7 && st.getQuestItemsCount(7175) == 50L)
			htmltext = "iris_q0038_0503.htm";
		if(npcId == 30344 && cond == 4 && st.getQuestItemsCount(7176) == 1L)
			htmltext = "magister_roh_q0038_0301.htm";
		if(npcId == 30344 && cond == 5)
			htmltext = "magister_roh_q0038_0403.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final boolean chance = Rnd.chance(100);
		final int cond = st.getInt("cond");
		if((npcId == 20357 || npcId == 21100) && cond == 1 && chance && st.getQuestItemsCount(7173) < 100L)
		{
			st.giveItems(7173, 1L);
			if(st.getQuestItemsCount(7173) == 100L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		if((npcId == 20356 || npcId == 21101) && cond == 6 && chance && st.getQuestItemsCount(7175) < 50L)
		{
			st.giveItems(7175, 1L);
			if(st.getQuestItemsCount(7175) == 50L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "7");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
