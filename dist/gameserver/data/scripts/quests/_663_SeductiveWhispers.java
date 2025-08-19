package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _663_SeductiveWhispers extends Quest implements ScriptFile
{
	private static final int Wilbert = 30846;
	private static final int[] mobs;
	private static final short Spirit_Bead = 8766;
	private static final short Enchant_Weapon_D = 955;
	private static final short Enchant_Weapon_C = 951;
	private static final short Enchant_Weapon_B = 947;
	private static final short Enchant_Armor_B = 948;
	private static final short Enchant_Weapon_A = 729;
	private static final short Enchant_Armor_A = 730;
	private static final short[] Recipes_Weapon_B;
	private static final short[] Recipes_Weapon_B_100P;
	private static final short[] Ingredients_Weapon_B;
	private static final int drop_chance = 15;
	private static final int WinChance = 33;
	private static final LevelRewards[] rewards;
	private static String Dialog_WinLevel;
	private static String Dialog_WinGame;
	private static String Dialog_Rewards;

	public _663_SeductiveWhispers()
	{
		super(false);
		this.addStartNpc(30846);
		this.addKillId(_663_SeductiveWhispers.mobs);
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		final long Spirit_Bead_Count = st.getQuestItemsCount(8766);
		if(event.equalsIgnoreCase("30846_04.htm") && _state == 1)
		{
			st.set("cond", "1");
			st.set("round", "0");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else
		{
			if(event.equalsIgnoreCase("30846_07.htm") && _state == 2)
				return _663_SeductiveWhispers.Dialog_Rewards;
			if(event.equalsIgnoreCase("30846_09.htm") && _state == 2)
			{
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else if(event.equalsIgnoreCase("30846_08.htm") && _state == 2)
			{
				if(Spirit_Bead_Count < 1L)
					return "30846_11.htm";
				st.takeItems(8766, 1L);
				if(!Rnd.chance(33))
					return "30846_08a.htm";
			}
			else if(event.equalsIgnoreCase("30846_10.htm") && _state == 2)
			{
				st.set("round", "0");
				if(Spirit_Bead_Count < 50L)
					return "30846_11.htm";
			}
			else if(event.equalsIgnoreCase("30846_12.htm") && _state == 2)
			{
				final int round = st.getInt("round");
				if(round == 0)
				{
					if(Spirit_Bead_Count < 50L)
						return "30846_11.htm";
					st.takeItems(8766, 50L);
				}
				if(!Rnd.chance(33))
				{
					st.set("round", "0");
					return event;
				}
				final LevelRewards current_reward = _663_SeductiveWhispers.rewards[round];
				int next_round = round + 1;
				final boolean LastLevel = next_round == _663_SeductiveWhispers.rewards.length;
				String dialog = LastLevel ? _663_SeductiveWhispers.Dialog_WinGame : _663_SeductiveWhispers.Dialog_WinLevel;
				dialog = dialog.replaceFirst("%level%", String.valueOf(next_round));
				dialog = dialog.replaceFirst("%prize%", current_reward.toString());
				if(LastLevel)
				{
					next_round = 0;
					current_reward.giveRewards(st);
					st.playSound(Quest.SOUND_JACKPOT);
				}
				st.set("round", String.valueOf(next_round));
				return dialog;
			}
			else if(event.equalsIgnoreCase("30846_13.htm") && _state == 2)
			{
				final int round = st.getInt("round") - 1;
				st.set("round", "0");
				if(round < 0 || round >= _663_SeductiveWhispers.rewards.length)
					return "30846_13a.htm";
				_663_SeductiveWhispers.rewards[round].giveRewards(st);
			}
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(npc.getNpcId() != 30846)
			return "noquest";
		final int _state = st.getState();
		if(_state != 1)
			return "30846_03.htm";
		if(st.getPlayer().getLevel() < 50)
		{
			st.exitCurrentQuest(true);
			return "30846_00.htm";
		}
		st.set("cond", "0");
		return "30846_01.htm";
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() == 2)
		{
			final double rand = 15.0 * Experience.penaltyModifier(qs.calculateLevelDiffForDrop(npc.getLevel(), qs.getPlayer().getLevel()), 9.0) * npc.getTemplate().rateHp;
			qs.rollAndGive(8766, (int) (1.0f * Config.RATE_QUESTS_DROP), rand);
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
		mobs = new int[] {
				20674,
				20678,
				20954,
				20955,
				20956,
				20957,
				20958,
				20959,
				20960,
				20961,
				20962,
				20974,
				20975,
				20976,
				20996,
				20997,
				20998,
				20999,
				21001,
				21002,
				21006,
				21007,
				21008,
				21009,
				21010 };
		Recipes_Weapon_B = new short[] { 4963, 4966, 4967, 4968, 5001, 5003, 5004, 5005, 5006, 5007 };
		Recipes_Weapon_B_100P = new short[] { 4182, 4185, 4186, 4187, 4193, 4195, 4196, 4197, 4198, 4199 };
		Ingredients_Weapon_B = new short[] { 4101, 4107, 4108, 4109, 4115, 4117, 4118, 4119, 4120, 4121 };
		rewards = new LevelRewards[] {
				new LevelRewards("%n% adena").add(57, 40000),
				new LevelRewards("%n% adena").add(57, 80000),
				new LevelRewards("%n% adena, %n% D-grade Enchant Weapon Scroll(s)").add(57, 110000).add(955, 1),
				new LevelRewards("%n% adena, %n% C-grade Enchant Weapon Scroll(s)").add(57, 199000).add(951, 1),
				new LevelRewards("%n% adena, %n% recipe(s) for a B-grade Weapon").add(57, 388000).add(Config.ALT_100_RECIPES_B ? _663_SeductiveWhispers.Recipes_Weapon_B_100P : _663_SeductiveWhispers.Recipes_Weapon_B, 1),
				new LevelRewards("%n% adena, %n% essential ingredient(s) for a B-grade Weapon").add(57, 675000).add(_663_SeductiveWhispers.Ingredients_Weapon_B, 1),
				new LevelRewards("%n% adena, %n% B-grade Enchant Weapon Scroll(s), %n% B-grade Enchat Armor Scroll(s)").add(57, 1284000).add(947, 2).add(948, 2),
				new LevelRewards("%n% adena, %n% A-grade Enchant Weapon Scroll(s), %n% A-grade Enchat Armor Scroll(s)").add(57, 2384000).add(729, 1).add(730, 2) };
		_663_SeductiveWhispers.Dialog_WinLevel = "<font color=\"LEVEL\">Blacksmith Wilbert:</font><br><br>";
		_663_SeductiveWhispers.Dialog_WinGame = "<font color=\"LEVEL\">Blacksmith Wilbert:</font><br><br>";
		_663_SeductiveWhispers.Dialog_Rewards = "<font color=\"LEVEL\">Blacksmith Wilbert:</font><br><br>";
		_663_SeductiveWhispers.Dialog_WinLevel += "You won round %level%!<br>";
		_663_SeductiveWhispers.Dialog_WinLevel += "You can stop game now and take your prize:<br>";
		_663_SeductiveWhispers.Dialog_WinLevel += "<font color=\"LEVEL\">%prize%</font><br><br>";
		_663_SeductiveWhispers.Dialog_WinLevel += "<a action=\"bypass -h Quest _663_SeductiveWhispers 30846_12.htm\">Pull next card!</a><br>";
		_663_SeductiveWhispers.Dialog_WinLevel += "<a action=\"bypass -h Quest _663_SeductiveWhispers 30846_13.htm\">\"No, enough for me, end game and take my prize.\"</a>";
		_663_SeductiveWhispers.Dialog_WinGame += "Congratulations! You won round %n%!<br>";
		_663_SeductiveWhispers.Dialog_WinGame += "Game ends now and you get your prize:<br>";
		_663_SeductiveWhispers.Dialog_WinGame += "<font color=\"LEVEL\">%prize%</font><br><br>";
		_663_SeductiveWhispers.Dialog_WinGame += "<a action=\"bypass -h Quest _663_SeductiveWhispers 30846_03.htm\">Return</a>";
		_663_SeductiveWhispers.Dialog_Rewards += "If you win the game, the master running it owes you the appropriate amount. The higher the round, the bigger the payout. That's why the game anly allows you to win up to 8 round in a row. If -- and that's a big if -- you manage to win 8 straight times, the game will end.<br>";
		_663_SeductiveWhispers.Dialog_Rewards += "Keep in mind that <font color=\"LEVEL\">if you lose any of the rounds, you get nothing</font>. That's fair warning, my friend. Here's how the prize system works:<br>";
		for(int i = 0; i < _663_SeductiveWhispers.rewards.length; ++i)
		{
			_663_SeductiveWhispers.Dialog_Rewards = _663_SeductiveWhispers.Dialog_Rewards + "<font color=\"LEVEL\">" + String.valueOf(i + 1) + " winning round";
			if(i > 0)
				_663_SeductiveWhispers.Dialog_Rewards += "s";
			_663_SeductiveWhispers.Dialog_Rewards = _663_SeductiveWhispers.Dialog_Rewards + ": </font>" + _663_SeductiveWhispers.rewards[i].toString() + "<br>";
		}
		_663_SeductiveWhispers.Dialog_Rewards += "<br>My advice is to identify what you'd like to win and then to play for that prize. Any questions?<br>";
		_663_SeductiveWhispers.Dialog_Rewards += "<a action=\"bypass -h Quest _663_SeductiveWhispers 30846_03.htm\">Return</a>";
	}

	private static class LevelRewards
	{
		private final Map<short[], Integer> rewards;
		private String txt;

		public LevelRewards(final String _txt)
		{
			rewards = new HashMap<short[], Integer>();
			txt = _txt;
		}

		public LevelRewards add(final int item_id, final int count)
		{
			return this.add(new short[] { (short) item_id }, count);
		}

		public LevelRewards add(final short[] items_id, final int count)
		{
			final int cnt = (int) (count * Config.RATE_QUESTS_REWARD);
			txt = txt.replaceFirst("%n%", String.valueOf(cnt));
			rewards.put(items_id, cnt);
			return this;
		}

		public void giveRewards(final QuestState qs)
		{
			for(final short[] item_ids : rewards.keySet())
				qs.giveItems(item_ids[Rnd.get(item_ids.length)], rewards.get(item_ids), false);
		}

		@Override
		public String toString()
		{
			return txt;
		}
	}
}
