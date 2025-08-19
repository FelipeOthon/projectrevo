package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.ScriptFile;

public class _103_SpiritOfCraftsman extends Quest implements ScriptFile
{
	public final int KAROYDS_LETTER_ID = 968;
	public final int CECKTINONS_VOUCHER1_ID = 969;
	public final int CECKTINONS_VOUCHER2_ID = 970;
	public final int BONE_FRAGMENT1_ID = 1107;
	public final int SOUL_CATCHER_ID = 971;
	public final int PRESERVE_OIL_ID = 972;
	public final int ZOMBIE_HEAD_ID = 973;
	public final int STEELBENDERS_HEAD_ID = 974;
	public final int BLOODSABER_ID = 975;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _103_SpiritOfCraftsman()
	{
		super(false);
		this.addStartNpc(30307);
		this.addTalkId(new int[] { 30132 });
		this.addTalkId(new int[] { 30144 });
		this.addKillId(new int[] { 20015 });
		this.addKillId(new int[] { 20020 });
		this.addKillId(new int[] { 20455 });
		this.addKillId(new int[] { 20517 });
		this.addKillId(new int[] { 20518 });
		addQuestItem(new int[] { 968, 969, 970, 1107, 971, 972, 973, 974 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("blacksmith_karoyd_q0103_05.htm"))
		{
			st.giveItems(968, 1L);
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		if(id == 1)
			st.set("cond", "0");
		if(npcId == 30307 && st.getInt("cond") == 0)
		{
			if(st.getPlayer().getRace() != Race.darkelf)
				htmltext = "blacksmith_karoyd_q0103_00.htm";
			else
			{
				if(st.getPlayer().getLevel() >= 10)
				{
					htmltext = "blacksmith_karoyd_q0103_03.htm";
					return htmltext;
				}
				htmltext = "blacksmith_karoyd_q0103_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == 30307 && st.getInt("cond") == 0)
			htmltext = "completed";
		else if(id == 2)
			if(npcId == 30307 && st.getInt("cond") >= 1 && (st.getQuestItemsCount(968) >= 1L || st.getQuestItemsCount(969) >= 1L || st.getQuestItemsCount(970) >= 1L))
				htmltext = "blacksmith_karoyd_q0103_06.htm";
			else if(npcId == 30132 && st.getInt("cond") == 1 && st.getQuestItemsCount(968) == 1L)
			{
				htmltext = "cecon_q0103_01.htm";
				st.set("cond", "2");
				st.takeItems(968, 1L);
				st.giveItems(969, 1L);
			}
			else if(npcId == 30132 && st.getInt("cond") >= 2 && (st.getQuestItemsCount(969) >= 1L || st.getQuestItemsCount(970) >= 1L))
				htmltext = "cecon_q0103_02.htm";
			else if(npcId == 30144 && st.getInt("cond") == 2 && st.getQuestItemsCount(969) >= 1L)
			{
				htmltext = "harne_q0103_01.htm";
				st.set("cond", "3");
				st.takeItems(969, 1L);
				st.giveItems(970, 1L);
			}
			else if(npcId == 30144 && st.getInt("cond") == 3 && st.getQuestItemsCount(970) >= 1L && st.getQuestItemsCount(1107) < 10L)
				htmltext = "harne_q0103_02.htm";
			else if(npcId == 30144 && st.getInt("cond") == 4 && st.getQuestItemsCount(970) == 1L && st.getQuestItemsCount(1107) >= 10L)
			{
				htmltext = "harne_q0103_03.htm";
				st.set("cond", "5");
				st.takeItems(970, 1L);
				st.takeItems(1107, 10L);
				st.giveItems(971, 1L);
			}
			else if(npcId == 30144 && st.getInt("cond") == 5 && st.getQuestItemsCount(971) == 1L)
				htmltext = "harne_q0103_04.htm";
			else if(npcId == 30132 && st.getInt("cond") == 5 && st.getQuestItemsCount(971) == 1L)
			{
				htmltext = "cecon_q0103_03.htm";
				st.set("cond", "6");
				st.takeItems(971, 1L);
				st.giveItems(972, 1L);
			}
			else if(npcId == 30132 && st.getInt("cond") == 6 && st.getQuestItemsCount(972) == 1L && st.getQuestItemsCount(973) == 0L && st.getQuestItemsCount(974) == 0L)
				htmltext = "cecon_q0103_04.htm";
			else if(npcId == 30132 && st.getInt("cond") == 7 && st.getQuestItemsCount(973) == 1L)
			{
				htmltext = "cecon_q0103_05.htm";
				st.set("cond", "8");
				st.takeItems(973, 1L);
				st.giveItems(974, 1L);
			}
			else if(npcId == 30132 && st.getInt("cond") == 8 && st.getQuestItemsCount(974) == 1L)
				htmltext = "cecon_q0103_06.htm";
			else if(npcId == 30307 && st.getInt("cond") == 8 && st.getQuestItemsCount(974) == 1L)
			{
				htmltext = "blacksmith_karoyd_q0103_07.htm";
				st.takeItems(974, 1L);
				st.giveItems(975, 1L);
				st.giveItems(57, 19799L, false);
				st.getPlayer().addExpAndSp(46663L, 3999L, false, false);
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
				st.exitCurrentQuest(false);
				st.playSound(Quest.SOUND_FINISH);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if((npcId == 20517 || npcId == 20518 || npcId == 20455) && st.getInt("cond") == 3)
		{
			if(st.getQuestItemsCount(970) == 1L && st.getQuestItemsCount(1107) < 10L && Rnd.chance(33))
			{
				st.giveItems(1107, 1L);
				if(st.getQuestItemsCount(1107) == 10L)
				{
					st.playSound(Quest.SOUND_MIDDLE);
					st.set("cond", "4");
				}
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if((npcId == 20015 || npcId == 20020) && st.getInt("cond") == 6 && st.getQuestItemsCount(972) == 1L && Rnd.chance(33))
		{
			st.giveItems(973, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			st.takeItems(972, 1L);
			st.set("cond", "7");
		}
		return null;
	}
}
