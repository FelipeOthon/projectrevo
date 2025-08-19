package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _381_LetsBecomeARoyalMember extends Quest implements ScriptFile
{
	private static int KAILS_COIN;
	private static int COIN_ALBUM;
	private static int MEMBERSHIP_1;
	private static int CLOVER_COIN;
	private static int ROYAL_MEMBERSHIP;
	private static int SORINT;
	private static int SANDRA;
	private static int ANCIENT_GARGOYLE;
	private static int VEGUS;
	private static int GARGOYLE_CHANCE;
	private static int VEGUS_CHANCE;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _381_LetsBecomeARoyalMember()
	{
		super(false);
		this.addStartNpc(_381_LetsBecomeARoyalMember.SORINT);
		this.addTalkId(new int[] { _381_LetsBecomeARoyalMember.SANDRA });
		this.addKillId(new int[] { _381_LetsBecomeARoyalMember.ANCIENT_GARGOYLE });
		this.addKillId(new int[] { _381_LetsBecomeARoyalMember.VEGUS });
		addQuestItem(new int[] { _381_LetsBecomeARoyalMember.KAILS_COIN });
		addQuestItem(new int[] { _381_LetsBecomeARoyalMember.COIN_ALBUM });
		addQuestItem(new int[] { _381_LetsBecomeARoyalMember.CLOVER_COIN });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("warehouse_keeper_sorint_q0381_02.htm"))
		{
			if(st.getPlayer().getLevel() >= 55 && st.getQuestItemsCount(_381_LetsBecomeARoyalMember.MEMBERSHIP_1) > 0L)
			{
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
				htmltext = "warehouse_keeper_sorint_q0381_03.htm";
			}
			else
			{
				htmltext = "warehouse_keeper_sorint_q0381_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(event.equalsIgnoreCase("sandra_q0381_02.htm") && st.getInt("cond") == 1)
		{
			st.set("id", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		final int npcId = npc.getNpcId();
		final long album = st.getQuestItemsCount(_381_LetsBecomeARoyalMember.COIN_ALBUM);
		if(npcId == _381_LetsBecomeARoyalMember.SORINT)
		{
			if(cond == 0)
				htmltext = "warehouse_keeper_sorint_q0381_01.htm";
			else if(cond == 1)
			{
				final long coin = st.getQuestItemsCount(_381_LetsBecomeARoyalMember.KAILS_COIN);
				if(coin > 0L && album > 0L)
				{
					st.takeItems(_381_LetsBecomeARoyalMember.KAILS_COIN, -1L);
					st.takeItems(_381_LetsBecomeARoyalMember.COIN_ALBUM, -1L);
					st.giveItems(_381_LetsBecomeARoyalMember.ROYAL_MEMBERSHIP, 1L);
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(true);
					htmltext = "warehouse_keeper_sorint_q0381_06.htm";
				}
				else if(album == 0L)
					htmltext = "warehouse_keeper_sorint_q0381_05.htm";
				else if(coin == 0L)
					htmltext = "warehouse_keeper_sorint_q0381_04.htm";
			}
		}
		else
		{
			final long clover = st.getQuestItemsCount(_381_LetsBecomeARoyalMember.CLOVER_COIN);
			if(album > 0L)
				htmltext = "sandra_q0381_05.htm";
			else if(clover > 0L)
			{
				st.takeItems(_381_LetsBecomeARoyalMember.CLOVER_COIN, -1L);
				st.giveItems(_381_LetsBecomeARoyalMember.COIN_ALBUM, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
				htmltext = "sandra_q0381_04.htm";
			}
			else if(st.getInt("id") == 0)
				htmltext = "sandra_q0381_01.htm";
			else
				htmltext = "sandra_q0381_03.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		final long album = st.getQuestItemsCount(_381_LetsBecomeARoyalMember.COIN_ALBUM);
		final long coin = st.getQuestItemsCount(_381_LetsBecomeARoyalMember.KAILS_COIN);
		final long clover = st.getQuestItemsCount(_381_LetsBecomeARoyalMember.CLOVER_COIN);
		if(npcId == _381_LetsBecomeARoyalMember.ANCIENT_GARGOYLE && coin == 0L)
		{
			if(Rnd.chance(_381_LetsBecomeARoyalMember.GARGOYLE_CHANCE))
			{
				st.giveItems(_381_LetsBecomeARoyalMember.KAILS_COIN, 1L);
				if(album > 0L || clover > 0L)
					st.playSound(Quest.SOUND_MIDDLE);
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if(npcId == _381_LetsBecomeARoyalMember.VEGUS && clover + album == 0L && st.getInt("id") != 0 && Rnd.chance(_381_LetsBecomeARoyalMember.VEGUS_CHANCE))
		{
			st.giveItems(_381_LetsBecomeARoyalMember.CLOVER_COIN, 1L);
			if(coin > 0L)
				st.playSound(Quest.SOUND_MIDDLE);
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}

	static
	{
		_381_LetsBecomeARoyalMember.KAILS_COIN = 5899;
		_381_LetsBecomeARoyalMember.COIN_ALBUM = 5900;
		_381_LetsBecomeARoyalMember.MEMBERSHIP_1 = 3813;
		_381_LetsBecomeARoyalMember.CLOVER_COIN = 7569;
		_381_LetsBecomeARoyalMember.ROYAL_MEMBERSHIP = 5898;
		_381_LetsBecomeARoyalMember.SORINT = 30232;
		_381_LetsBecomeARoyalMember.SANDRA = 30090;
		_381_LetsBecomeARoyalMember.ANCIENT_GARGOYLE = 21018;
		_381_LetsBecomeARoyalMember.VEGUS = 27316;
		_381_LetsBecomeARoyalMember.GARGOYLE_CHANCE = 5;
		_381_LetsBecomeARoyalMember.VEGUS_CHANCE = 100;
	}
}
