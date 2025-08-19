package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _123_TheLeaderAndTheFollower extends Quest implements ScriptFile
{
	int NEWYEAR;
	int BRUIN_LIZARDMEN;
	int BRUIN_BLOOD;
	int PICOT_ARANEID;
	int PICOT_LEG;
	int D_CRY;
	int D_CRY_COUNT_HEAVY;
	int D_CRY_COUNT_LIGHT_MAGIC;
	int CLAN_OATH_HELM;
	int CLAN_OATH_ARMOR;
	int CLAN_OATH_GAUNTLETS;
	int CLAN_OATH_SABATON;
	int CLAN_OATH_BRIGANDINE;
	int CLAN_OATH_LEATHER_GLOVES;
	int CLAN_OATH_BOOTS;
	int CLAN_OATH_AKETON;
	int CLAN_OATH_PADDED_GLOVES;
	int CLAN_OATH_SANDALS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _123_TheLeaderAndTheFollower()
	{
		super(false);
		NEWYEAR = 31961;
		BRUIN_LIZARDMEN = 27321;
		BRUIN_BLOOD = 8549;
		PICOT_ARANEID = 27322;
		PICOT_LEG = 8550;
		D_CRY = 1458;
		D_CRY_COUNT_HEAVY = 721;
		D_CRY_COUNT_LIGHT_MAGIC = 604;
		CLAN_OATH_HELM = 7850;
		CLAN_OATH_ARMOR = 7851;
		CLAN_OATH_GAUNTLETS = 7852;
		CLAN_OATH_SABATON = 7853;
		CLAN_OATH_BRIGANDINE = 7854;
		CLAN_OATH_LEATHER_GLOVES = 7855;
		CLAN_OATH_BOOTS = 7856;
		CLAN_OATH_AKETON = 7857;
		CLAN_OATH_PADDED_GLOVES = 7858;
		CLAN_OATH_SANDALS = 7859;
		this.addStartNpc(NEWYEAR);
		this.addKillId(new int[] { BRUIN_LIZARDMEN });
		this.addKillId(new int[] { PICOT_ARANEID });
		addQuestItem(new int[] { BRUIN_BLOOD, PICOT_LEG });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("31961-03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("31961-05.htm"))
		{
			st.set("choose", "1");
			st.set("cond", "3");
		}
		else if(event.equals("31961-06.htm"))
		{
			st.set("choose", "2");
			st.set("cond", "4");
		}
		else if(event.equals("31961-07.htm"))
		{
			st.set("choose", "3");
			st.set("cond", "5");
		}
		else if(event.equals("31961-08.htm"))
		{
			final int choose = st.getInt("choose");
			int D_CRY_COUNT = D_CRY_COUNT_LIGHT_MAGIC;
			if(choose == 1)
				D_CRY_COUNT = D_CRY_COUNT_HEAVY;
			if(st.getQuestItemsCount(D_CRY) >= D_CRY_COUNT)
			{
				st.set("cond", "7");
				st.takeItems(D_CRY, D_CRY_COUNT);
			}
			else
				htmltext = "<html><body>771 D Cry!</body></html>";
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() < 19)
			{
				htmltext = "<html><body>Your level is too low</body></html>";
				return htmltext;
			}
			if(st.getPlayer().getClanId() == 0)
			{
				htmltext = "<html><body>You are not in clan</body></html>";
				return htmltext;
			}
			if(st.getPlayer().getSponsor() == 0)
			{
				htmltext = "<html><body>You have no sponsor</body></html>";
				return htmltext;
			}
			htmltext = "31961-00.htm";
		}
		else if(cond == 1)
			htmltext = "<html><body>Bring me 10 Bruin Lizardmen blood.</body></html>";
		else if(cond == 2)
		{
			st.takeItems(BRUIN_BLOOD, 10L);
			htmltext = "31961-04.htm";
		}
		else if(cond == 3)
			htmltext = "31961-05.htm";
		else if(cond == 4)
			htmltext = "31961-06.htm";
		else if(cond == 5)
			htmltext = "31961-07.htm";
		else if(cond == 7)
			htmltext = "<html><body>Bring me 8 Picot Legs.</body></html>";
		else if(cond == 8)
		{
			st.takeItems(PICOT_LEG, 8L);
			final int choose = st.getInt("choose");
			st.giveItems(CLAN_OATH_HELM, 1L);
			if(choose == 1)
			{
				st.giveItems(CLAN_OATH_ARMOR, 1L);
				st.giveItems(CLAN_OATH_GAUNTLETS, 1L);
				st.giveItems(CLAN_OATH_SABATON, 1L);
			}
			else if(choose == 2)
			{
				st.giveItems(CLAN_OATH_BRIGANDINE, 1L);
				st.giveItems(CLAN_OATH_LEATHER_GLOVES, 1L);
				st.giveItems(CLAN_OATH_BOOTS, 1L);
			}
			else if(choose == 3)
			{
				st.giveItems(CLAN_OATH_AKETON, 1L);
				st.giveItems(CLAN_OATH_PADDED_GLOVES, 1L);
				st.giveItems(CLAN_OATH_SANDALS, 1L);
			}
			st.set("cond", "0");
			st.playSound(Quest.SOUND_FINISH);
			htmltext = "<html><body>OK!</body></html>";
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == BRUIN_LIZARDMEN && st.getQuestItemsCount(BRUIN_BLOOD) < 10L && cond == 1 && Rnd.chance(50))
		{
			st.giveItems(BRUIN_BLOOD, 1L);
			if(st.getQuestItemsCount(BRUIN_BLOOD) == 10L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
		}
		else if(npcId == PICOT_ARANEID && st.getQuestItemsCount(PICOT_LEG) < 8L && cond == 7 && Rnd.chance(50))
		{
			st.giveItems(PICOT_LEG, 1L);
			if(st.getQuestItemsCount(PICOT_LEG) == 8L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "8");
			}
		}
		return null;
	}
}
