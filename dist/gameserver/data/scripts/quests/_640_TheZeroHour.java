package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AddonsConfig;

public class _640_TheZeroHour extends Quest implements ScriptFile
{
	private static int KAHMAN;
	private static int FANG;
	private static int Enria;
	private static int Asofe;
	private static int Thons;
	private static int Varnish_of_Purity;
	private static int Synthetic_Cokes;
	private static int Compound_Braid;
	private static int Durable_Metal_Plate;
	private static int Mithril_Alloy;
	private static int Oriharukon;
	private static int DROP_CHANCE;
	private static int[] mobs;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _640_TheZeroHour()
	{
		super(true);
		this.addStartNpc(_640_TheZeroHour.KAHMAN);
		this.addKillId(_640_TheZeroHour.mobs);
		addQuestItem(new int[] { _640_TheZeroHour.FANG });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int cond = st.getInt("cond");
		String htmltext = event;
		if(event.equals("merc_kahmun_q0640_0103.htm") && cond == 0)
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		if(cond == 1)
		{
			if(event.equals("0"))
				if(st.getQuestItemsCount(_640_TheZeroHour.FANG) >= 12L)
				{
					htmltext = "merc_kahmun_q0640_0203.htm";
					st.takeItems(_640_TheZeroHour.FANG, 12L);
					st.giveItems(_640_TheZeroHour.Enria, (int) AddonsConfig.getQuestRewardRates(this));
				}
				else
					htmltext = "merc_kahmun_q0640_0201.htm";
			if(event.equals("1"))
				if(st.getQuestItemsCount(_640_TheZeroHour.FANG) >= 6L)
				{
					htmltext = "merc_kahmun_q0640_0203.htm";
					st.takeItems(_640_TheZeroHour.FANG, 6L);
					st.giveItems(_640_TheZeroHour.Asofe, (int) AddonsConfig.getQuestRewardRates(this));
				}
				else
					htmltext = "merc_kahmun_q0640_0201.htm";
			if(event.equals("2"))
				if(st.getQuestItemsCount(_640_TheZeroHour.FANG) >= 6L)
				{
					htmltext = "merc_kahmun_q0640_0203.htm";
					st.takeItems(_640_TheZeroHour.FANG, 6L);
					st.giveItems(_640_TheZeroHour.Thons, (int) AddonsConfig.getQuestRewardRates(this));
				}
				else
					htmltext = "merc_kahmun_q0640_0201.htm";
			if(event.equals("3"))
				if(st.getQuestItemsCount(_640_TheZeroHour.FANG) >= 81L)
				{
					htmltext = "merc_kahmun_q0640_0203.htm";
					st.takeItems(_640_TheZeroHour.FANG, 81L);
					st.giveItems(_640_TheZeroHour.Varnish_of_Purity, (int) (10.0f * AddonsConfig.getQuestRewardRates(this)));
				}
				else
					htmltext = "merc_kahmun_q0640_0201.htm";
			if(event.equals("4"))
				if(st.getQuestItemsCount(_640_TheZeroHour.FANG) >= 33L)
				{
					htmltext = "merc_kahmun_q0640_0203.htm";
					st.takeItems(_640_TheZeroHour.FANG, 33L);
					st.giveItems(_640_TheZeroHour.Synthetic_Cokes, (int) (5.0f * AddonsConfig.getQuestRewardRates(this)));
				}
				else
					htmltext = "merc_kahmun_q0640_0201.htm";
			if(event.equals("5"))
				if(st.getQuestItemsCount(_640_TheZeroHour.FANG) >= 30L)
				{
					htmltext = "merc_kahmun_q0640_0203.htm";
					st.takeItems(_640_TheZeroHour.FANG, 30L);
					st.giveItems(_640_TheZeroHour.Compound_Braid, (int) (10.0f * AddonsConfig.getQuestRewardRates(this)));
				}
				else
					htmltext = "merc_kahmun_q0640_0201.htm";
			if(event.equals("6"))
				if(st.getQuestItemsCount(_640_TheZeroHour.FANG) >= 150L)
				{
					htmltext = "merc_kahmun_q0640_0203.htm";
					st.takeItems(_640_TheZeroHour.FANG, 150L);
					st.giveItems(_640_TheZeroHour.Durable_Metal_Plate, (int) (10.0f * AddonsConfig.getQuestRewardRates(this)));
				}
				else
					htmltext = "merc_kahmun_q0640_0201.htm";
			if(event.equals("7"))
				if(st.getQuestItemsCount(_640_TheZeroHour.FANG) >= 131L)
				{
					htmltext = "merc_kahmun_q0640_0203.htm";
					st.takeItems(_640_TheZeroHour.FANG, 131L);
					st.giveItems(_640_TheZeroHour.Mithril_Alloy, (int) (10.0f * AddonsConfig.getQuestRewardRates(this)));
				}
				else
					htmltext = "merc_kahmun_q0640_0201.htm";
			if(event.equals("8"))
				if(st.getQuestItemsCount(_640_TheZeroHour.FANG) >= 123L)
				{
					htmltext = "merc_kahmun_q0640_0203.htm";
					st.takeItems(_640_TheZeroHour.FANG, 123L);
					st.giveItems(_640_TheZeroHour.Oriharukon, (int) (5.0f * AddonsConfig.getQuestRewardRates(this)));
				}
				else
					htmltext = "merc_kahmun_q0640_0201.htm";
		}
		if(event.equals("close"))
		{
			htmltext = "merc_kahmun_q0640_0205.htm";
			st.takeItems(_640_TheZeroHour.FANG, -1L);
			st.exitCurrentQuest(true);
		}
		if(event.equals("more"))
		{
			htmltext = "merc_kahmun_q0640_0101.htm";
			st.unset("cond");
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		final QuestState InSearchOfTheNest = st.getPlayer().getQuestState(109);
		if(npcId == _640_TheZeroHour.KAHMAN)
		{
			if(cond == 0)
				if(st.getPlayer().getLevel() >= 66)
				{
					if(InSearchOfTheNest != null && InSearchOfTheNest.isCompleted())
						htmltext = "merc_kahmun_q0640_0101.htm";
					else
						htmltext = "merc_kahmun_q0640_0104.htm";
				}
				else
					htmltext = "merc_kahmun_q0640_0102.htm";
			if(cond == 1)
				htmltext = "merc_kahmun_q0640_0105.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() == 2)
			st.rollAndGive(_640_TheZeroHour.FANG, 1, _640_TheZeroHour.DROP_CHANCE);
		return null;
	}

	static
	{
		_640_TheZeroHour.KAHMAN = 31554;
		_640_TheZeroHour.FANG = 8085;
		_640_TheZeroHour.Enria = 4042;
		_640_TheZeroHour.Asofe = 4043;
		_640_TheZeroHour.Thons = 4044;
		_640_TheZeroHour.Varnish_of_Purity = 1887;
		_640_TheZeroHour.Synthetic_Cokes = 1888;
		_640_TheZeroHour.Compound_Braid = 1889;
		_640_TheZeroHour.Durable_Metal_Plate = 5550;
		_640_TheZeroHour.Mithril_Alloy = 1890;
		_640_TheZeroHour.Oriharukon = 1893;
		_640_TheZeroHour.DROP_CHANCE = 50;
		_640_TheZeroHour.mobs = new int[] {
				22105,
				22106,
				22107,
				22108,
				22109,
				22110,
				22111,
				22115,
				22116,
				22117,
				22118,
				22119,
				22120,
				22121,
				22112,
				22113,
				22114 };
	}
}
