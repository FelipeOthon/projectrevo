package events.TheFallHarvest;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.base.ItemToDrop;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.reward.DropData;
import l2s.gameserver.network.l2.s2c.Die;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.NpcTable;
import npc.model.SquashInstance;

public class SquashAI extends Fighter
{
	protected static final DropData[] _dropList;
	public static final int Young_Squash = 12774;
	public static final int High_Quality_Squash = 12775;
	public static final int Low_Quality_Squash = 12776;
	public static final int Large_Young_Squash = 12777;
	public static final int High_Quality_Large_Squash = 12778;
	public static final int Low_Quality_Large_Squash = 12779;
	public static final int King_Squash = 13016;
	public static final int Emperor_Squash = 13017;
	public static final int Squash_Level_up = 4513;
	public static final int Squash_Poisoned = 4514;
	private static final String[] textOnSpawn;
	private static final String[] textOnAttack;
	private static final String[] textTooFast;
	private static final String[] textSuccess0;
	private static final String[] textFail0;
	private static final String[] textSuccess1;
	private static final String[] textFail1;
	private static final String[] textSuccess2;
	private static final String[] textFail2;
	private static final String[] textSuccess3;
	private static final String[] textFail3;
	private static final String[] textSuccess4;
	private static final String[] textFail4;
	private int _npcId;
	private int _nectar;
	private int _tryCount;
	private long _lastNectarUse;
	private long _timeToUnspawn;
	private ScheduledFuture<?> _polimorphTask;
	private static int NECTAR_REUSE;

	public SquashAI(final NpcInstance actor)
	{
		super(actor);
		_npcId = getActor().getNpcId();
		Functions.npcSayCustomMessage((NpcInstance) getActor(), SquashAI.textOnSpawn[Rnd.get(SquashAI.textOnSpawn.length)], new Object[0]);
		_timeToUnspawn = System.currentTimeMillis() + 120000L;
	}

	@Override
	protected boolean thinkActive()
	{
		if(System.currentTimeMillis() > _timeToUnspawn)
		{
			_timeToUnspawn = Long.MAX_VALUE;
			if(_polimorphTask != null)
			{
				_polimorphTask.cancel(true);
				_polimorphTask = null;
			}
			stopAITask();
			final SquashInstance actor = getActor();
			if(actor != null)
				actor.deleteMe();
		}
		return false;
	}

	@Override
	protected void onEvtSeeSpell(final Skill skill, final Creature caster)
	{
		final SquashInstance actor = getActor();
		if(actor == null || skill.getId() != 2005)
			return;
		switch(_tryCount)
		{
			case 0:
			{
				++_tryCount;
				_lastNectarUse = System.currentTimeMillis();
				if(Rnd.chance(50))
				{
					++_nectar;
					Functions.npcSay(actor, SquashAI.textSuccess0[Rnd.get(SquashAI.textSuccess0.length)]);
					actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4513, 1, SquashAI.NECTAR_REUSE, 0L) });
					break;
				}
				Functions.npcSay(actor, SquashAI.textFail0[Rnd.get(SquashAI.textFail0.length)]);
				actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4514, 1, SquashAI.NECTAR_REUSE, 0L) });
				break;
			}
			case 1:
			{
				if(System.currentTimeMillis() - _lastNectarUse < SquashAI.NECTAR_REUSE)
				{
					Functions.npcSay(actor, SquashAI.textTooFast[Rnd.get(SquashAI.textTooFast.length)]);
					return;
				}
				++_tryCount;
				_lastNectarUse = System.currentTimeMillis();
				if(Rnd.chance(50))
				{
					++_nectar;
					Functions.npcSay(actor, SquashAI.textSuccess1[Rnd.get(SquashAI.textSuccess1.length)]);
					actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4513, 1, SquashAI.NECTAR_REUSE, 0L) });
					break;
				}
				Functions.npcSay(actor, SquashAI.textFail1[Rnd.get(SquashAI.textFail1.length)]);
				actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4514, 1, SquashAI.NECTAR_REUSE, 0L) });
				break;
			}
			case 2:
			{
				if(System.currentTimeMillis() - _lastNectarUse < SquashAI.NECTAR_REUSE)
				{
					Functions.npcSay(actor, SquashAI.textTooFast[Rnd.get(SquashAI.textTooFast.length)]);
					return;
				}
				++_tryCount;
				_lastNectarUse = System.currentTimeMillis();
				if(Rnd.chance(50))
				{
					++_nectar;
					Functions.npcSay(actor, SquashAI.textSuccess2[Rnd.get(SquashAI.textSuccess2.length)]);
					actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4513, 1, SquashAI.NECTAR_REUSE, 0L) });
					break;
				}
				Functions.npcSay(actor, SquashAI.textFail2[Rnd.get(SquashAI.textFail2.length)]);
				actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4514, 1, SquashAI.NECTAR_REUSE, 0L) });
				break;
			}
			case 3:
			{
				if(System.currentTimeMillis() - _lastNectarUse < SquashAI.NECTAR_REUSE)
				{
					Functions.npcSay(actor, SquashAI.textTooFast[Rnd.get(SquashAI.textTooFast.length)]);
					return;
				}
				++_tryCount;
				_lastNectarUse = System.currentTimeMillis();
				if(Rnd.chance(50))
				{
					++_nectar;
					Functions.npcSay(actor, SquashAI.textSuccess3[Rnd.get(SquashAI.textSuccess3.length)]);
					actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4513, 1, SquashAI.NECTAR_REUSE, 0L) });
					break;
				}
				Functions.npcSay(actor, SquashAI.textFail3[Rnd.get(SquashAI.textFail3.length)]);
				actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4514, 1, SquashAI.NECTAR_REUSE, 0L) });
				break;
			}
			case 4:
			{
				if(System.currentTimeMillis() - _lastNectarUse < SquashAI.NECTAR_REUSE)
				{
					Functions.npcSay(actor, SquashAI.textTooFast[Rnd.get(SquashAI.textTooFast.length)]);
					return;
				}
				++_tryCount;
				_lastNectarUse = System.currentTimeMillis();
				if(Rnd.chance(50))
				{
					++_nectar;
					Functions.npcSay(actor, SquashAI.textSuccess4[Rnd.get(SquashAI.textSuccess4.length)]);
					actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4513, 1, SquashAI.NECTAR_REUSE, 0L) });
				}
				else
				{
					Functions.npcSay(actor, SquashAI.textFail4[Rnd.get(SquashAI.textFail4.length)]);
					actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4514, 1, SquashAI.NECTAR_REUSE, 0L) });
				}
				if(_npcId == 12774)
				{
					if(_nectar < 3)
						_npcId = 12776;
					else if(_nectar == 5)
						_npcId = 13016;
					else
						_npcId = 12775;
				}
				else if(_npcId == 12777)
					if(_nectar < 3)
						_npcId = 12779;
					else if(_nectar == 5)
						_npcId = 13017;
					else
						_npcId = 12778;
				_polimorphTask = ThreadPoolManager.getInstance().schedule(new PolimorphTask(), SquashAI.NECTAR_REUSE);
				break;
			}
		}
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final SquashInstance actor = getActor();
		if(actor != null && Rnd.chance(5))
			Functions.npcSay(actor, SquashAI.textOnAttack[Rnd.get(SquashAI.textOnAttack.length)]);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		_tryCount = -1;
		final SquashInstance actor = getActor();
		if(actor == null)
			return;
		double dropMod = 1.5;
		switch(_npcId)
		{
			case 12776:
			{
				dropMod *= 1.0;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			}
			case 12775:
			{
				dropMod *= 2.0;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			}
			case 13016:
			{
				dropMod *= 4.0;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			}
			case 12779:
			{
				dropMod *= 12.5;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			}
			case 12778:
			{
				dropMod *= 25.0;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			}
			case 13017:
			{
				dropMod *= 50.0;
				Functions.npcSay(actor, "The pampkin opens!!!");
				Functions.npcSay(actor, "ya yo! Opens! Good thing many...");
				break;
			}
			default:
			{
				dropMod *= 0.0;
				Functions.npcSay(actor, "Ouch, if I had died like this, you could obtain nothing!");
				Functions.npcSay(actor, "The news about my death shouldn't spread, oh!");
				break;
			}
		}
		actor.broadcastPacket(new L2GameServerPacket[] { new Die(actor) });
		this.setIntention(CtrlIntention.AI_INTENTION_IDLE);
		if(dropMod > 0.0)
		{
			if(_polimorphTask != null)
			{
				_polimorphTask.cancel(true);
				_polimorphTask = null;
				return;
			}
			for(final DropData d : SquashAI._dropList)
			{
				final List<ItemToDrop> itd = d.roll((Player) null, dropMod, false, false, false);
				for(final ItemToDrop i : itd)
					actor.dropItem(actor.getSpawner(), i.itemId, i.count);
			}
		}
	}

	@Override
	protected boolean randomAnimation()
	{
		return false;
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}

	@Override
	public SquashInstance getActor()
	{
		return (SquashInstance) super.getActor();
	}

	static
	{
		_dropList = new DropData[] {
				new DropData(1539, 1, 5, 15000.0, 1),
				new DropData(1374, 1, 3, 15000.0, 1),
				new DropData(4411, 1, 1, 5000.0, 1),
				new DropData(4412, 1, 1, 5000.0, 1),
				new DropData(4413, 1, 1, 5000.0, 1),
				new DropData(4414, 1, 1, 5000.0, 1),
				new DropData(4415, 1, 1, 5000.0, 1),
				new DropData(4416, 1, 1, 5000.0, 1),
				new DropData(4417, 1, 1, 5000.0, 1),
				new DropData(5010, 1, 1, 5000.0, 1),
				new DropData(1458, 10, 30, 13846.0, 1),
				new DropData(1459, 10, 30, 3000.0, 1),
				new DropData(1460, 10, 30, 1000.0, 1),
				new DropData(1461, 10, 30, 600.0, 1),
				new DropData(1462, 10, 30, 360.0, 1),
				new DropData(4161, 1, 1, 5000.0, 1),
				new DropData(4182, 1, 1, 5000.0, 1),
				new DropData(4174, 1, 1, 5000.0, 1),
				new DropData(4166, 1, 1, 5000.0, 1),
				new DropData(8660, 1, 1, 1000.0, 1),
				new DropData(8661, 1, 1, 1000.0, 1),
				new DropData(4393, 1, 1, 300.0, 1),
				new DropData(7836, 1, 1, 200.0, 1),
				new DropData(5590, 1, 1, 200.0, 1),
				new DropData(7058, 1, 1, 50.0, 1),
				new DropData(8350, 1, 1, 50.0, 1),
				new DropData(5133, 1, 1, 50.0, 1),
				new DropData(5817, 1, 1, 50.0, 1),
				new DropData(9140, 1, 1, 30.0, 1),
				new DropData(9146, 1, 3, 5000.0, 1),
				new DropData(9147, 1, 3, 5000.0, 1),
				new DropData(9148, 1, 3, 5000.0, 1),
				new DropData(9149, 1, 3, 5000.0, 1),
				new DropData(9150, 1, 3, 5000.0, 1),
				new DropData(9151, 1, 3, 5000.0, 1),
				new DropData(9152, 1, 3, 5000.0, 1),
				new DropData(9153, 1, 3, 5000.0, 1),
				new DropData(9154, 1, 3, 5000.0, 1),
				new DropData(9155, 1, 3, 5000.0, 1),
				new DropData(9156, 1, 3, 2000.0, 1),
				new DropData(9157, 1, 3, 1000.0, 1),
				new DropData(955, 1, 1, 400.0, 1),
				new DropData(956, 1, 1, 2000.0, 1),
				new DropData(951, 1, 1, 300.0, 1),
				new DropData(952, 1, 1, 1500.0, 1),
				new DropData(947, 1, 1, 200.0, 1),
				new DropData(948, 1, 1, 1000.0, 1),
				new DropData(729, 1, 1, 100.0, 1),
				new DropData(730, 1, 1, 500.0, 1),
				new DropData(959, 1, 1, 50.0, 1),
				new DropData(960, 1, 1, 300.0, 1) };
		textOnSpawn = new String[] {
				"scripts.events.TheFallHarvest.SquashAI.textOnSpawn.0",
				"scripts.events.TheFallHarvest.SquashAI.textOnSpawn.1",
				"scripts.events.TheFallHarvest.SquashAI.textOnSpawn.2" };
		textOnAttack = new String[] {
				"Bites rat-a-tat... to change... body...!",
				"Ha ha, grew up! Completely on all!",
				"Cannot to aim all? Had a look all to flow out...",
				"Is that also calculated hit? Look for person which has the strength!",
				"Don't waste your time!",
				"Ha, this sound is really pleasant to hear?",
				"I eat your attack to grow!",
				"Time to hit again! Come again!",
				"Only useful music can open big pumpkin... It can not be opened with weapon!" };
		textTooFast = new String[] {
				"heh heh,looks well hit!",
				"yo yo? Your skill is mediocre?",
				"Time to hit again! Come again!",
				"I eat your attack to grow!",
				"Make an effort... to get down like this, I walked...",
				"What is this kind of degree to want to open me? Really is indulges in fantasy!",
				"Good fighting method. Evidently flies away the fly also can overcome.",
				"Strives to excel strength oh! But waste your time..." };
		textSuccess0 = new String[] {
				"The lovely pumpkin young fruit start to glisten when taken to the threshing ground! From now on will be able to grow healthy and strong!",
				"Oh, Haven't seen for a long time?",
				"Suddenly, thought as soon as to see my beautiful appearance?",
				"Well! This is something! Is the nectar?",
				"Refuels! Drink 5 bottles to be able to grow into the big pumpkin oh!" };
		textFail0 = new String[] {
				"If I drink nectar, I can grow up faster!",
				"Come, believe me, sprinkle a nectar! I can certainly turn the big pumpkin!!!",
				"Take nectar to come, pumpkin nectar!" };
		textSuccess1 = new String[] {
				"Wish the big pumpkin!",
				"completely became the recreation area! Really good!",
				"Guessed I am mature or am rotten?",
				"Nectar is just the best! Ha! Ha! Ha!" };
		textFail1 = new String[] {
				"oh! Randomly missed! Too quickly sprinkles the nectar?",
				"If I die like this, you only could get young pumpkin...",
				"Cultivate a bit faster! The good speech becomes the big pumpkin, the young pumpkin is not good!",
				"The such small pumpkin you all must eat? Bring the nectar, I can be bigger!" };
		textSuccess2 = new String[] {
				"Young pumpkin wishing! Has how already grown up?",
				"Already grew up! Quickly sneaked off...",
				"Graciousness, is very good. Come again to see, now felt more and more well" };
		textFail2 = new String[] {
				"Hey! Was not there! Here is! Here! Not because I can not properly care? Small!",
				"Wow, stops? Like this got down to have to thank",
				"Hungry for a nectar oh...",
				"Do you want the big pumpkin? But I like young pumpkin..." };
		textSuccess3 = new String[] {
				"Big pumpkin wishing! Ask, to sober!",
				"Rumble rumble... it's really tasty! Hasn't it?",
				"Cultivating me just to eat? Good, is casual your... not to give the manna on the suicide!" };
		textFail3 = new String[] { "Isn't it the water you add? What flavor?", "Master, rescue my... I don't have the nectar flavor, I must die..." };
		textSuccess4 = new String[] {
				"is very good, does extremely well! Knew what next step should make?",
				"If you catch me, I give you 10 million adena!!! Agree?" };
		textFail4 = new String[] { "Hungry for a nectar oh...", "If I drink nectar, I can grow up faster!" };
		SquashAI.NECTAR_REUSE = 3000;
	}

	public class PolimorphTask implements Runnable
	{
		@Override
		public void run()
		{
			final SquashInstance actor = getActor();
			if(actor == null)
				return;
			Spawn spawn = null;
			try
			{
				spawn = new Spawn(NpcTable.getTemplate(_npcId));
				spawn.setLoc(actor.getLoc());
				final NpcInstance npc = spawn.doSpawn(true);
				npc.setAI(new SquashAI(npc));
				((SquashInstance) npc).setSpawner(actor.getSpawner());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			_timeToUnspawn = Long.MAX_VALUE;
			stopAITask();
			actor.deleteMe();
		}
	}
}
