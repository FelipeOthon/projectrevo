package quests;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _648_AnIceMerchantsDream extends Quest implements ScriptFile
{
	private static int Rafforty;
	private static int Ice_Shelf;
	private static int Silver_Hemocyte;
	private static int Silver_Ice_Crystal;
	private static int Black_Ice_Crystal;
	private static int Silver_Hemocyte_Chance;
	private static int Silver2Black_Chance;
	private static List<Integer> silver2black;

	public _648_AnIceMerchantsDream()
	{
		super(false);
		this.addStartNpc(_648_AnIceMerchantsDream.Rafforty);
		this.addStartNpc(_648_AnIceMerchantsDream.Ice_Shelf);
		for(int i = 22080; i <= 22098; ++i)
			if(i != 22095)
				this.addKillId(new int[] { i });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("32020-02.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("32020-09.htm") && _state == 2)
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		if(_state != 2)
			return event;
		final long Silver_Ice_Crystal_Count = st.getQuestItemsCount(_648_AnIceMerchantsDream.Silver_Ice_Crystal);
		final long Black_Ice_Crystal_Count = st.getQuestItemsCount(_648_AnIceMerchantsDream.Black_Ice_Crystal);
		if(event.equalsIgnoreCase("32020-07.htm"))
		{
			final long reward = Silver_Ice_Crystal_Count * 300L + Black_Ice_Crystal_Count * 1200L;
			if(reward <= 0L)
				return "32020-07a.htm";
			st.takeItems(_648_AnIceMerchantsDream.Silver_Ice_Crystal, -1L);
			st.takeItems(_648_AnIceMerchantsDream.Black_Ice_Crystal, -1L);
			st.giveItems(57, reward);
		}
		else if(event.equalsIgnoreCase("32023-04.htm"))
		{
			final int char_obj_id = st.getPlayer().getObjectId();
			synchronized (_648_AnIceMerchantsDream.silver2black)
			{
				if(_648_AnIceMerchantsDream.silver2black.contains(char_obj_id))
					return event;
				if(Silver_Ice_Crystal_Count <= 0L)
					return "cheat.htm";
				_648_AnIceMerchantsDream.silver2black.add(char_obj_id);
			}
			st.takeItems(_648_AnIceMerchantsDream.Silver_Ice_Crystal, 1L);
			st.playSound(Quest.SOUND_BROKEN_KEY);
		}
		else if(event.equalsIgnoreCase("32023-05.htm"))
		{
			final Integer char_obj_id2 = st.getPlayer().getObjectId();
			synchronized (_648_AnIceMerchantsDream.silver2black)
			{
				if(!_648_AnIceMerchantsDream.silver2black.contains(char_obj_id2))
					return "cheat.htm";
				while(_648_AnIceMerchantsDream.silver2black.contains(char_obj_id2))
					_648_AnIceMerchantsDream.silver2black.remove(char_obj_id2);
			}
			if(!Rnd.chance(_648_AnIceMerchantsDream.Silver2Black_Chance))
			{
				st.playSound(Quest.SOUND_ENCHANT_FAILED);
				return "32023-06.htm";
			}
			st.giveItems(_648_AnIceMerchantsDream.Black_Ice_Crystal, 1L);
			st.playSound(Quest.SOUND_ENCHANT_SUCESS);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int _state = st.getState();
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(_state == 1)
			if(npcId == _648_AnIceMerchantsDream.Rafforty)
			{
				if(st.getPlayer().getLevel() >= 53)
				{
					st.set("cond", "0");
					return "32020-01.htm";
				}
				st.exitCurrentQuest(true);
				return "32020-00.htm";
			}
			else if(npcId == _648_AnIceMerchantsDream.Ice_Shelf)
				return "32023-00.htm";
		if(_state != 2)
			return "noquest";
		final long Silver_Ice_Crystal_Count = st.getQuestItemsCount(_648_AnIceMerchantsDream.Silver_Ice_Crystal);
		if(npcId == _648_AnIceMerchantsDream.Ice_Shelf)
			return Silver_Ice_Crystal_Count > 0L ? "32023-02.htm" : "32023-01.htm";
		final long Black_Ice_Crystal_Count = st.getQuestItemsCount(_648_AnIceMerchantsDream.Black_Ice_Crystal);
		if(npcId == _648_AnIceMerchantsDream.Rafforty)
			if(cond == 1)
			{
				if(Silver_Ice_Crystal_Count <= 0L && Black_Ice_Crystal_Count <= 0L)
					return "32020-04.htm";
				final QuestState st_115 = st.getPlayer().getQuestState(115);
				if(st_115 != null && st_115.getState() == 3)
				{
					st.set("cond", "2");
					st.playSound(Quest.SOUND_MIDDLE);
					return "32020-10.htm";
				}
				return "32020-05.htm";
			}
			else if(cond == 2)
				return Silver_Ice_Crystal_Count > 0L || Black_Ice_Crystal_Count > 0L ? "32020-10.htm" : "32020-04a.htm";
		return "noquest";
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		final Player player = qs.getRandomPartyMember(2, Config.ALT_PARTY_DISTRIBUTION_RANGE);
		if(player != null)
		{
			final QuestState st = player.getQuestState(qs.getQuest().getId());
			if(st != null && Rnd.chance(npc.getNpcId() - 22050))
			{
				st.giveItems(_648_AnIceMerchantsDream.Silver_Ice_Crystal, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		if(qs.getState() == 2 && qs.getInt("cond") == 2 && Rnd.chance(_648_AnIceMerchantsDream.Silver_Hemocyte_Chance))
		{
			qs.giveItems(_648_AnIceMerchantsDream.Silver_Hemocyte, 1L);
			qs.playSound(Quest.SOUND_ITEMGET);
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
		_648_AnIceMerchantsDream.Rafforty = 32020;
		_648_AnIceMerchantsDream.Ice_Shelf = 32023;
		_648_AnIceMerchantsDream.Silver_Hemocyte = 8057;
		_648_AnIceMerchantsDream.Silver_Ice_Crystal = 8077;
		_648_AnIceMerchantsDream.Black_Ice_Crystal = 8078;
		_648_AnIceMerchantsDream.Silver_Hemocyte_Chance = 10;
		_648_AnIceMerchantsDream.Silver2Black_Chance = 30;
		_648_AnIceMerchantsDream.silver2black = new ArrayList<Integer>();
	}
}
