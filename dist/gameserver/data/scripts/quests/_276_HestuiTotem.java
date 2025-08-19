package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;

public class _276_HestuiTotem extends Quest implements ScriptFile
{
	private static int Tanapi;
	private static int Kasha_Bear;
	private static int Kasha_Bear_Totem_Spirit;
	private static int Leather_Pants;
	private static int Totem_of_Hestui;
	private static int Kasha_Parasite;
	private static int Kasha_Crystal;

	public _276_HestuiTotem()
	{
		super(false);
		this.addStartNpc(_276_HestuiTotem.Tanapi);
		this.addKillId(new int[] { _276_HestuiTotem.Kasha_Bear });
		this.addKillId(new int[] { _276_HestuiTotem.Kasha_Bear_Totem_Spirit });
		addQuestItem(new int[] { _276_HestuiTotem.Kasha_Parasite });
		addQuestItem(new int[] { _276_HestuiTotem.Kasha_Crystal });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("seer_tanapi_q0276_03.htm") && st.getState() == 1 && st.getPlayer().getRace() == Race.orc && st.getPlayer().getLevel() >= 15)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _276_HestuiTotem.Tanapi)
			return htmltext;
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getRace() != Race.orc)
			{
				htmltext = "seer_tanapi_q0276_00.htm";
				st.exitCurrentQuest(true);
			}
			else if(st.getPlayer().getLevel() < 15)
			{
				htmltext = "seer_tanapi_q0276_01.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "seer_tanapi_q0276_02.htm";
				st.set("cond", "0");
			}
		}
		else if(_state == 2)
			if(st.getQuestItemsCount(_276_HestuiTotem.Kasha_Crystal) > 0L)
			{
				htmltext = "seer_tanapi_q0276_05.htm";
				st.takeItems(_276_HestuiTotem.Kasha_Parasite, -1L);
				st.takeItems(_276_HestuiTotem.Kasha_Crystal, -1L);
				st.giveItems(_276_HestuiTotem.Leather_Pants, 1L);
				st.giveItems(_276_HestuiTotem.Totem_of_Hestui, 1L);
				if(st.getRateQuestsReward() > 1.0f)
					st.giveItems(57, Math.round(ItemTable.getInstance().getTemplate(_276_HestuiTotem.Totem_of_Hestui).getReferencePrice() * (st.getRateQuestsReward() - 1.0f) / 2.0f), false);
				if(st.getPlayer().getClassId().getLevel() == 1 && !st.getPlayer().getVarBoolean("p1q4"))
				{
					st.getPlayer().setVar("p1q4", "1");
					st.getPlayer().sendPacket(new ExShowScreenMessage("Now go find the Newbie Guide.", 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
				}
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "seer_tanapi_q0276_04.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		if(npcId == _276_HestuiTotem.Kasha_Bear && qs.getQuestItemsCount(_276_HestuiTotem.Kasha_Crystal) == 0L)
		{
			if(qs.getQuestItemsCount(_276_HestuiTotem.Kasha_Parasite) < 50L)
			{
				qs.giveItems(_276_HestuiTotem.Kasha_Parasite, 1L);
				qs.playSound(Quest.SOUND_ITEMGET);
			}
			else
			{
				qs.takeItems(_276_HestuiTotem.Kasha_Parasite, -1L);
				qs.addSpawn(_276_HestuiTotem.Kasha_Bear_Totem_Spirit);
			}
		}
		else if(npcId == _276_HestuiTotem.Kasha_Bear_Totem_Spirit && qs.getQuestItemsCount(_276_HestuiTotem.Kasha_Crystal) == 0L)
		{
			qs.giveItems(_276_HestuiTotem.Kasha_Crystal, 1L);
			qs.playSound(Quest.SOUND_MIDDLE);
		}
		return null;
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		_276_HestuiTotem.Tanapi = 30571;
		_276_HestuiTotem.Kasha_Bear = 20479;
		_276_HestuiTotem.Kasha_Bear_Totem_Spirit = 27044;
		_276_HestuiTotem.Leather_Pants = 29;
		_276_HestuiTotem.Totem_of_Hestui = 1500;
		_276_HestuiTotem.Kasha_Parasite = 1480;
		_276_HestuiTotem.Kasha_Crystal = 1481;
	}
}
