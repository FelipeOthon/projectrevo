package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _118_ToLeadAndBeLed extends Quest implements ScriptFile
{
	private static int PINTER;
	private static int MAILLE_LIZARDMAN;
	private static int BLOOD_OF_MAILLE_LIZARDMAN;
	private static int KING_OF_THE_ARANEID;
	private static int KING_OF_THE_ARANEID_LEG;
	private static int D_CRY;
	private static int D_CRY_COUNT_HEAVY;
	private static int D_CRY_COUNT_LIGHT_MAGIC;
	private static int CLAN_OATH_HELM;
	private static int CLAN_OATH_ARMOR;
	private static int CLAN_OATH_GAUNTLETS;
	private static int CLAN_OATH_SABATON;
	private static int CLAN_OATH_BRIGANDINE;
	private static int CLAN_OATH_LEATHER_GLOVES;
	private static int CLAN_OATH_BOOTS;
	private static int CLAN_OATH_AKETON;
	private static int CLAN_OATH_PADDED_GLOVES;
	private static int CLAN_OATH_SANDALS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _118_ToLeadAndBeLed()
	{
		super(false);
		this.addStartNpc(_118_ToLeadAndBeLed.PINTER);
		this.addKillId(new int[] { _118_ToLeadAndBeLed.MAILLE_LIZARDMAN });
		this.addKillId(new int[] { _118_ToLeadAndBeLed.KING_OF_THE_ARANEID });
		addQuestItem(new int[] { _118_ToLeadAndBeLed.BLOOD_OF_MAILLE_LIZARDMAN, _118_ToLeadAndBeLed.KING_OF_THE_ARANEID_LEG });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("30298-02.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("30298-05a.htm"))
		{
			st.set("choose", "1");
			st.set("cond", "3");
		}
		else if(event.equals("30298-05b.htm"))
		{
			st.set("choose", "2");
			st.set("cond", "4");
		}
		else if(event.equals("30298-05c.htm"))
		{
			st.set("choose", "3");
			st.set("cond", "5");
		}
		else if(event.equals("30298-08.htm"))
		{
			final int choose = st.getInt("choose");
			final int need_dcry = choose == 1 ? _118_ToLeadAndBeLed.D_CRY_COUNT_HEAVY : _118_ToLeadAndBeLed.D_CRY_COUNT_LIGHT_MAGIC;
			if(st.getQuestItemsCount(_118_ToLeadAndBeLed.D_CRY) < need_dcry)
				return "30298-07.htm";
			st.set("cond", "7");
			st.takeItems(_118_ToLeadAndBeLed.D_CRY, need_dcry);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(npc.getNpcId() != _118_ToLeadAndBeLed.PINTER)
			return "noquest";
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getLevel() < 19)
			{
				st.exitCurrentQuest(true);
				return "30298-00.htm";
			}
			if(st.getPlayer().getClanId() == 0)
			{
				st.exitCurrentQuest(true);
				return "30298-00a.htm";
			}
			if(st.getPlayer().getSponsor() == 0)
			{
				st.exitCurrentQuest(true);
				return "30298-00b.htm";
			}
			st.set("cond", "0");
			return "30298-01.htm";
		}
		else
		{
			final int cond = st.getInt("cond");
			if(cond == 1 && _state == 2)
				return "30298-02a.htm";
			if(cond == 2 && _state == 2)
			{
				if(st.getQuestItemsCount(_118_ToLeadAndBeLed.BLOOD_OF_MAILLE_LIZARDMAN) < 10L)
				{
					st.set("cond", "1");
					return "30298-02a.htm";
				}
				st.takeItems(_118_ToLeadAndBeLed.BLOOD_OF_MAILLE_LIZARDMAN, -1L);
				return "30298-04.htm";
			}
			else
			{
				if(cond == 3 && _state == 2)
					return "30298-05a.htm";
				if(cond == 4 && _state == 2)
					return "30298-05b.htm";
				if(cond == 5 && _state == 2)
					return "30298-05c.htm";
				if(cond == 7 && _state == 2)
					return "30298-08a.htm";
				if(cond != 8 || _state != 2)
					return "noquest";
				if(st.getQuestItemsCount(_118_ToLeadAndBeLed.KING_OF_THE_ARANEID_LEG) < 8L)
				{
					st.set("cond", "7");
					return "30298-08a.htm";
				}
				st.takeItems(_118_ToLeadAndBeLed.KING_OF_THE_ARANEID_LEG, -1L);
				st.giveItems(_118_ToLeadAndBeLed.CLAN_OATH_HELM, 1L);
				final int choose = st.getInt("choose");
				if(choose == 1)
				{
					st.giveItems(_118_ToLeadAndBeLed.CLAN_OATH_ARMOR, 1L);
					st.giveItems(_118_ToLeadAndBeLed.CLAN_OATH_GAUNTLETS, 1L);
					st.giveItems(_118_ToLeadAndBeLed.CLAN_OATH_SABATON, 1L);
				}
				else if(choose == 2)
				{
					st.giveItems(_118_ToLeadAndBeLed.CLAN_OATH_BRIGANDINE, 1L);
					st.giveItems(_118_ToLeadAndBeLed.CLAN_OATH_LEATHER_GLOVES, 1L);
					st.giveItems(_118_ToLeadAndBeLed.CLAN_OATH_BOOTS, 1L);
				}
				else
				{
					st.giveItems(_118_ToLeadAndBeLed.CLAN_OATH_AKETON, 1L);
					st.giveItems(_118_ToLeadAndBeLed.CLAN_OATH_PADDED_GLOVES, 1L);
					st.giveItems(_118_ToLeadAndBeLed.CLAN_OATH_SANDALS, 1L);
				}
				st.unset("cond");
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
				return "30298-09.htm";
			}
		}
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == _118_ToLeadAndBeLed.MAILLE_LIZARDMAN && st.getQuestItemsCount(_118_ToLeadAndBeLed.BLOOD_OF_MAILLE_LIZARDMAN) < 10L && cond == 1 && Rnd.chance(50))
		{
			st.giveItems(_118_ToLeadAndBeLed.BLOOD_OF_MAILLE_LIZARDMAN, 1L);
			if(st.getQuestItemsCount(_118_ToLeadAndBeLed.BLOOD_OF_MAILLE_LIZARDMAN) == 10L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		else if(npcId == _118_ToLeadAndBeLed.KING_OF_THE_ARANEID && st.getQuestItemsCount(_118_ToLeadAndBeLed.KING_OF_THE_ARANEID_LEG) < 8L && cond == 7 && Rnd.chance(50))
		{
			st.giveItems(_118_ToLeadAndBeLed.KING_OF_THE_ARANEID_LEG, 1L);
			if(st.getQuestItemsCount(_118_ToLeadAndBeLed.KING_OF_THE_ARANEID_LEG) == 8L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "8");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}

	static
	{
		_118_ToLeadAndBeLed.PINTER = 30298;
		_118_ToLeadAndBeLed.MAILLE_LIZARDMAN = 20919;
		_118_ToLeadAndBeLed.BLOOD_OF_MAILLE_LIZARDMAN = 8062;
		_118_ToLeadAndBeLed.KING_OF_THE_ARANEID = 20927;
		_118_ToLeadAndBeLed.KING_OF_THE_ARANEID_LEG = 8063;
		_118_ToLeadAndBeLed.D_CRY = 1458;
		_118_ToLeadAndBeLed.D_CRY_COUNT_HEAVY = 721;
		_118_ToLeadAndBeLed.D_CRY_COUNT_LIGHT_MAGIC = 604;
		_118_ToLeadAndBeLed.CLAN_OATH_HELM = 7850;
		_118_ToLeadAndBeLed.CLAN_OATH_ARMOR = 7851;
		_118_ToLeadAndBeLed.CLAN_OATH_GAUNTLETS = 7852;
		_118_ToLeadAndBeLed.CLAN_OATH_SABATON = 7853;
		_118_ToLeadAndBeLed.CLAN_OATH_BRIGANDINE = 7854;
		_118_ToLeadAndBeLed.CLAN_OATH_LEATHER_GLOVES = 7855;
		_118_ToLeadAndBeLed.CLAN_OATH_BOOTS = 7856;
		_118_ToLeadAndBeLed.CLAN_OATH_AKETON = 7857;
		_118_ToLeadAndBeLed.CLAN_OATH_PADDED_GLOVES = 7858;
		_118_ToLeadAndBeLed.CLAN_OATH_SANDALS = 7859;
	}
}
