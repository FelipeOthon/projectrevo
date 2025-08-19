package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.ScriptFile;

public class _102_SeaofSporesFever extends Quest implements ScriptFile
{
	int ALBERRYUS_LETTER;
	int EVERGREEN_AMULET;
	int DRYAD_TEARS;
	int LBERRYUS_LIST;
	int COBS_MEDICINE1;
	int COBS_MEDICINE2;
	int COBS_MEDICINE3;
	int COBS_MEDICINE4;
	int COBS_MEDICINE5;
	int SWORD_OF_SENTINEL;
	int STAFF_OF_SENTINEL;
	int ALBERRYUS_LIST;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _102_SeaofSporesFever()
	{
		super(false);
		ALBERRYUS_LETTER = 964;
		EVERGREEN_AMULET = 965;
		DRYAD_TEARS = 966;
		LBERRYUS_LIST = 746;
		COBS_MEDICINE1 = 1130;
		COBS_MEDICINE2 = 1131;
		COBS_MEDICINE3 = 1132;
		COBS_MEDICINE4 = 1133;
		COBS_MEDICINE5 = 1134;
		SWORD_OF_SENTINEL = 743;
		STAFF_OF_SENTINEL = 744;
		ALBERRYUS_LIST = 746;
		this.addStartNpc(30284);
		this.addTalkId(new int[] { 30156 });
		this.addTalkId(new int[] { 30217 });
		this.addTalkId(new int[] { 30219 });
		this.addTalkId(new int[] { 30221 });
		this.addTalkId(new int[] { 30284 });
		this.addTalkId(new int[] { 30285 });
		this.addKillId(new int[] { 20013 });
		this.addKillId(new int[] { 20019 });
		addQuestItem(new int[] {
				ALBERRYUS_LETTER,
				EVERGREEN_AMULET,
				DRYAD_TEARS,
				COBS_MEDICINE1,
				COBS_MEDICINE2,
				COBS_MEDICINE3,
				COBS_MEDICINE4,
				COBS_MEDICINE5,
				ALBERRYUS_LIST });
	}

	private void check(final QuestState st)
	{
		if(st.getQuestItemsCount(COBS_MEDICINE2) == 0L && st.getQuestItemsCount(COBS_MEDICINE3) == 0L && st.getQuestItemsCount(COBS_MEDICINE4) == 0L && st.getQuestItemsCount(COBS_MEDICINE5) == 0L)
			st.set("cond", "6");
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("alberryus_q0102_02.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.giveItems(ALBERRYUS_LETTER, 1L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30284)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getRace() != Race.elf)
				{
					htmltext = "alberryus_q0102_00.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					if(st.getPlayer().getLevel() >= 12)
					{
						htmltext = "alberryus_q0102_07.htm";
						return htmltext;
					}
					htmltext = "alberryus_q0102_08.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1 && st.getQuestItemsCount(ALBERRYUS_LETTER) == 1L)
				htmltext = "alberryus_q0102_03.htm";
			else if(cond == 2 && st.getQuestItemsCount(EVERGREEN_AMULET) == 1L)
				htmltext = "alberryus_q0102_09.htm";
			else if(cond == 4 && st.getQuestItemsCount(COBS_MEDICINE1) == 1L)
			{
				st.set("cond", "5");
				st.takeItems(COBS_MEDICINE1, 1L);
				st.giveItems(ALBERRYUS_LIST, 1L);
				htmltext = "alberryus_q0102_04.htm";
			}
			else if(cond == 5)
				htmltext = "alberryus_q0102_05.htm";
			else if(cond == 6 && st.getQuestItemsCount(ALBERRYUS_LIST) == 1L)
			{
				st.takeItems(ALBERRYUS_LIST, 1L);
				st.giveItems(57, 6331L, false);
				st.getPlayer().addExpAndSp(30202L, 1339L, false, false);
				if(st.getPlayer().isMageClass())
					st.giveItems(STAFF_OF_SENTINEL, 1L);
				else
					st.giveItems(SWORD_OF_SENTINEL, 1L);
				if(st.getPlayer().getClassId().getLevel() == 1 && !st.getPlayer().getVarBoolean("p1q3"))
				{
					st.getPlayer().setVar("p1q3", "1");
					st.getPlayer().sendPacket(new ExShowScreenMessage("Now go find the Newbie Guide.", 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
					st.giveItems(1060, 100L);
					for(int item = 4412; item <= 4417; ++item)
						st.giveItems(item, 10L);
					if(st.getPlayer().isMageClass())
					{
						st.playTutorialVoice("tutorial_voice_027");
						st.giveItems(5790, 3000L);
					}
					else
					{
						st.playTutorialVoice("tutorial_voice_026");
						st.giveItems(5789, 6000L);
					}
				}
				htmltext = "alberryus_q0102_06.htm";
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 30156)
		{
			if(cond == 1 && st.getQuestItemsCount(ALBERRYUS_LETTER) == 1L)
			{
				st.takeItems(ALBERRYUS_LETTER, 1L);
				st.giveItems(EVERGREEN_AMULET, 1L);
				st.set("cond", "2");
				htmltext = "cob_q0102_03.htm";
			}
			else if(cond == 2 && st.getQuestItemsCount(EVERGREEN_AMULET) > 0L && st.getQuestItemsCount(DRYAD_TEARS) < 10L)
				htmltext = "cob_q0102_04.htm";
			else if(cond > 3 && st.getQuestItemsCount(ALBERRYUS_LIST) > 0L)
				htmltext = "cob_q0102_07.htm";
			else if(cond == 3 && st.getQuestItemsCount(EVERGREEN_AMULET) > 0L && st.getQuestItemsCount(DRYAD_TEARS) >= 10L)
			{
				st.takeItems(EVERGREEN_AMULET, 1L);
				st.takeItems(DRYAD_TEARS, -1L);
				st.giveItems(COBS_MEDICINE1, 1L);
				st.giveItems(COBS_MEDICINE2, 1L);
				st.giveItems(COBS_MEDICINE3, 1L);
				st.giveItems(COBS_MEDICINE4, 1L);
				st.giveItems(COBS_MEDICINE5, 1L);
				st.set("cond", "4");
				htmltext = "cob_q0102_05.htm";
			}
			else if(cond == 4)
				htmltext = "cob_q0102_06.htm";
		}
		else if(npcId == 30217 && cond == 5 && st.getQuestItemsCount(ALBERRYUS_LIST) == 1L && st.getQuestItemsCount(COBS_MEDICINE2) == 1L)
		{
			st.takeItems(COBS_MEDICINE2, 1L);
			htmltext = "sentinel_berryos_q0102_01.htm";
			check(st);
		}
		else if(npcId == 30219 && cond == 5 && st.getQuestItemsCount(ALBERRYUS_LIST) == 1L && st.getQuestItemsCount(COBS_MEDICINE3) == 1L)
		{
			st.takeItems(COBS_MEDICINE3, 1L);
			htmltext = "sentinel_veltress_q0102_01.htm";
			check(st);
		}
		else if(npcId == 30221 && cond == 5 && st.getQuestItemsCount(ALBERRYUS_LIST) == 1L && st.getQuestItemsCount(COBS_MEDICINE4) == 1L)
		{
			st.takeItems(COBS_MEDICINE4, 1L);
			htmltext = "sentinel_rayjien_q0102_01.htm";
			check(st);
		}
		else if(npcId == 30285 && cond == 5 && st.getQuestItemsCount(ALBERRYUS_LIST) == 1L && st.getQuestItemsCount(COBS_MEDICINE5) == 1L)
		{
			st.takeItems(COBS_MEDICINE5, 1L);
			htmltext = "sentinel_gartrandell_q0102_01.htm";
			check(st);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if((npcId == 20013 || npcId == 20019) && Rnd.chance(33) && st.getQuestItemsCount(EVERGREEN_AMULET) > 0L && st.getQuestItemsCount(DRYAD_TEARS) < 10L)
		{
			st.giveItems(DRYAD_TEARS, 1L);
			if(st.getQuestItemsCount(DRYAD_TEARS) == 10L)
			{
				st.set("cond", "3");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
