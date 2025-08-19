package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.s2c.ItemList;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class _104_SpiritOfMirror extends Quest implements ScriptFile
{
	static final int GALLINS_OAK_WAND = 748;
	static final int WAND_SPIRITBOUND1 = 1135;
	static final int WAND_SPIRITBOUND2 = 1136;
	static final int WAND_SPIRITBOUND3 = 1137;
	static final int WAND_OF_ADEPT = 747;
	static final SystemMessage CACHE_SYSMSG_GALLINS_OAK_WAND;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _104_SpiritOfMirror()
	{
		super(0);
		this.addStartNpc(30017);
		this.addTalkId(new int[] { 30041, 30043, 30045 });
		this.addKillId(new int[] { 27003, 27004, 27005 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("gallin_q0104_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.giveItems(748, 3L);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30017)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getRace() != Race.human)
				{
					htmltext = "gallin_q0104_00.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					if(st.getPlayer().getLevel() >= 10)
					{
						htmltext = "gallin_q0104_02.htm";
						return htmltext;
					}
					htmltext = "gallin_q0104_06.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1 && st.getQuestItemsCount(748) >= 1L && (st.getQuestItemsCount(1135) == 0L || st.getQuestItemsCount(1136) == 0L || st.getQuestItemsCount(1137) == 0L))
				htmltext = "gallin_q0104_04.htm";
			else if(cond == 3 && st.getQuestItemsCount(1135) >= 1L && st.getQuestItemsCount(1136) >= 1L && st.getQuestItemsCount(1137) >= 1L)
			{
				st.takeAllItems(new int[] { 1135, 1136, 1137 });
				st.giveItems(747, 1L);
				st.giveItems(57, 16866L, false);
				st.getPlayer().addExpAndSp(39750L, 3407L, false, false);
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
				htmltext = "gallin_q0104_05.htm";
				st.exitCurrentQuest(false);
				st.playSound(Quest.SOUND_FINISH);
			}
		}
		else if((npcId == 30041 || npcId == 30043 || npcId == 30045) && cond == 1)
		{
			if(npcId == 30041 && st.getInt("id1") == 0)
				st.set("id1", "1");
			htmltext = "arnold_q0104_01.htm";
			if(npcId == 30043 && st.getInt("id2") == 0)
				st.set("id2", "1");
			htmltext = "johnson_q0104_01.htm";
			if(npcId == 30045 && st.getInt("id3") == 0)
				st.set("id3", "1");
			htmltext = "ken_q0104_01.htm";
			if(st.getInt("id1") + st.getInt("id2") + st.getInt("id3") == 3)
			{
				st.set("cond", "2");
				st.unset("id1");
				st.unset("id2");
				st.unset("id3");
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getInt("cond");
		final int npcId = npc.getNpcId();
		if((cond == 1 || cond == 2) && st.getPlayer().getActiveWeaponInstance() != null && st.getPlayer().getActiveWeaponInstance().getItemId() == 748)
		{
			final ItemInstance weapon = st.getPlayer().getActiveWeaponInstance();
			if(npcId == 27003 && st.getQuestItemsCount(1135) == 0L)
			{
				st.getPlayer().getInventory().destroyItem(weapon, 1L, false);
				st.giveItems(1135, 1L);
				st.getPlayer().sendPacket(new IBroadcastPacket[] { _104_SpiritOfMirror.CACHE_SYSMSG_GALLINS_OAK_WAND, new ItemList(st.getPlayer(), false) });
				final long Collect = st.getQuestItemsCount(1135) + st.getQuestItemsCount(1136) + st.getQuestItemsCount(1137);
				if(Collect == 3L)
				{
					st.set("cond", "3");
					st.playSound(Quest.SOUND_MIDDLE);
				}
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
			else if(npcId == 27004 && st.getQuestItemsCount(1136) == 0L)
			{
				st.getPlayer().getInventory().destroyItem(weapon, 1L, false);
				st.giveItems(1136, 1L);
				st.getPlayer().sendPacket(new IBroadcastPacket[] { _104_SpiritOfMirror.CACHE_SYSMSG_GALLINS_OAK_WAND, new ItemList(st.getPlayer(), false) });
				final long Collect = st.getQuestItemsCount(1135) + st.getQuestItemsCount(1136) + st.getQuestItemsCount(1137);
				if(Collect == 3L)
				{
					st.set("cond", "3");
					st.playSound(Quest.SOUND_MIDDLE);
				}
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
			else if(npcId == 27005 && st.getQuestItemsCount(1137) == 0L)
			{
				st.getPlayer().getInventory().destroyItem(weapon, 1L, false);
				st.giveItems(1137, 1L);
				st.getPlayer().sendPacket(new IBroadcastPacket[] { _104_SpiritOfMirror.CACHE_SYSMSG_GALLINS_OAK_WAND, new ItemList(st.getPlayer(), false) });
				final long Collect = st.getQuestItemsCount(1135) + st.getQuestItemsCount(1136) + st.getQuestItemsCount(1137);
				if(Collect == 3L)
				{
					st.set("cond", "3");
					st.playSound(Quest.SOUND_MIDDLE);
				}
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		return null;
	}

	static
	{
		CACHE_SYSMSG_GALLINS_OAK_WAND = SystemMessage.removeItems(748, 1L);
	}
}
