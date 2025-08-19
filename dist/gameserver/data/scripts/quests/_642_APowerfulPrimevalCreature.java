package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AddonsConfig;

public class _642_APowerfulPrimevalCreature extends Quest implements ScriptFile
{
	private static int Dinn;
	private static int Ancient_Egg;
	private static int[] Dino;
	private static int[] Rewards;
	private static short Dinosaur_Tissue;
	private static short Dinosaur_Egg;
	private static int Dinosaur_Tissue_Chance;

	public _642_APowerfulPrimevalCreature()
	{
		super(true);
		this.addStartNpc(_642_APowerfulPrimevalCreature.Dinn);
		this.addKillId(new int[] { _642_APowerfulPrimevalCreature.Ancient_Egg });
		for(final int dino_id : _642_APowerfulPrimevalCreature.Dino)
			this.addKillId(new int[] { dino_id });
		addQuestItem(new int[] { _642_APowerfulPrimevalCreature.Dinosaur_Tissue });
		addQuestItem(new int[] { _642_APowerfulPrimevalCreature.Dinosaur_Egg });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		final long Dinosaur_Tissue_Count = st.getQuestItemsCount(_642_APowerfulPrimevalCreature.Dinosaur_Tissue);
		if(event.equalsIgnoreCase("dindin_q0642_04.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("dindin_q0642_12.htm") && _state == 2)
		{
			if(Dinosaur_Tissue_Count == 0L)
				return "dindin_q0642_08a.htm";
			st.takeItems(_642_APowerfulPrimevalCreature.Dinosaur_Tissue, -1L);
			st.giveItems(57, Dinosaur_Tissue_Count * 3000L, false);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else
		{
			if(event.equalsIgnoreCase("dindin_q0642_10.htm") && _state == 2 && Config.ALT_100_RECIPES_A)
				return "dindin_q0642_10a.htm";
			if(event.equalsIgnoreCase("0"))
				return null;
			if(_state == 2)
				try
				{
					final int rew_id = Integer.valueOf(event);
					if(Dinosaur_Tissue_Count < 150L || st.getQuestItemsCount(_642_APowerfulPrimevalCreature.Dinosaur_Egg) == 0L)
						return "dindin_q0642_08a.htm";
					for(final int reward : _642_APowerfulPrimevalCreature.Rewards)
						if(reward == rew_id)
						{
							st.takeItems(_642_APowerfulPrimevalCreature.Dinosaur_Tissue, 150L);
							st.takeItems(_642_APowerfulPrimevalCreature.Dinosaur_Egg, 1L);
							st.giveItems(Config.ALT_100_RECIPES_A ? reward + 1 : reward, 1L, false);
							st.giveItems(57, 44000L, false);
							st.playSound(Quest.SOUND_MIDDLE);
							return "dindin_q0642_12.htm";
						}
					return null;
				}
				catch(Exception ex)
				{}
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(npc.getNpcId() != _642_APowerfulPrimevalCreature.Dinn)
			return "noquest";
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getLevel() < 75)
			{
				st.exitCurrentQuest(true);
				return "dindin_q0642_01a.htm";
			}
			st.set("cond", "0");
			return "dindin_q0642_01.htm";
		}
		else
		{
			if(_state != 2)
				return "noquest";
			final long Dinosaur_Tissue_Count = st.getQuestItemsCount(_642_APowerfulPrimevalCreature.Dinosaur_Tissue);
			if(Dinosaur_Tissue_Count == 0L)
				return "dindin_q0642_08a.htm";
			if(Dinosaur_Tissue_Count < 150L || st.getQuestItemsCount(_642_APowerfulPrimevalCreature.Dinosaur_Egg) == 0L)
				return "dindin_q0642_07.htm";
			return "dindin_q0642_07a.htm";
		}
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2 || st.getInt("cond") != 1)
			return null;
		if(npc.getNpcId() == _642_APowerfulPrimevalCreature.Ancient_Egg)
			st.rollAndGive(_642_APowerfulPrimevalCreature.Dinosaur_Egg, 1, AddonsConfig.getQuestDropRates(this));
		else
			st.rollAndGive(_642_APowerfulPrimevalCreature.Dinosaur_Tissue, 1, _642_APowerfulPrimevalCreature.Dinosaur_Tissue_Chance);
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
		_642_APowerfulPrimevalCreature.Dinn = 32105;
		_642_APowerfulPrimevalCreature.Ancient_Egg = 18344;
		_642_APowerfulPrimevalCreature.Dino = new int[] {
				22196,
				22197,
				22198,
				22199,
				22200,
				22201,
				22202,
				22203,
				22204,
				22205,
				22218,
				22219,
				22220,
				22223,
				22224,
				22225,
				22226,
				22227 };
		_642_APowerfulPrimevalCreature.Rewards = new int[] { 8690, 8692, 8694, 8696, 8698, 8700, 8702, 8704, 8706, 8708, 8710 };
		_642_APowerfulPrimevalCreature.Dinosaur_Tissue = 8774;
		_642_APowerfulPrimevalCreature.Dinosaur_Egg = 8775;
		_642_APowerfulPrimevalCreature.Dinosaur_Tissue_Chance = 33;
	}
}
