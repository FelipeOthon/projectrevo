package npc.model;

import java.util.concurrent.Future;

import bosses.FourSepulchersSpawn;
import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.NpcSay;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.skills.Env;
import l2s.gameserver.skills.effects.EffectTemplate;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.templates.npc.NpcTemplate;

public class SepulcherMonsterInstance extends MonsterInstance
{
	private static Skill _skill;
	public int mysteriousBoxId;
	public boolean noFollow;
	protected Future<?> _victimShout;
	protected Future<?> _victimSpawnKeyBoxTask;
	protected Future<?> _changeImmortalTask;
	protected Future<?> _onDeadEventTask;
	private static final int CHAPEL_KEY = 7260;

	public SepulcherMonsterInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
		mysteriousBoxId = 0;
		noFollow = false;
		_victimShout = null;
		_victimSpawnKeyBoxTask = null;
		_changeImmortalTask = null;
		_onDeadEventTask = null;
		if(SepulcherMonsterInstance._skill == null)
			SepulcherMonsterInstance._skill = SkillTable.getInstance().getInfo(4578, 1);
	}

	@Override
	public void onSpawn()
	{
		switch(getNpcId())
		{
			case 18150:
			case 18151:
			case 18152:
			case 18153:
			case 18154:
			case 18155:
			case 18156:
			case 18157:
			{
				if(_victimSpawnKeyBoxTask != null)
					_victimSpawnKeyBoxTask.cancel(false);
				_victimSpawnKeyBoxTask = ThreadPoolManager.getInstance().schedule(new VictimSpawnKeyBox(this), 300000L);
				if(_victimShout != null)
					_victimShout.cancel(false);
				_victimShout = ThreadPoolManager.getInstance().schedule(new VictimShout(this), 5000L);
				break;
			}
			case 18231:
			case 18232:
			case 18233:
			case 18234:
			case 18235:
			case 18236:
			case 18237:
			case 18238:
			case 18239:
			case 18240:
			{
				if(_changeImmortalTask != null)
					_changeImmortalTask.cancel(false);
				_changeImmortalTask = ThreadPoolManager.getInstance().schedule(new ChangeImmortal(this), 500L);
				break;
			}
		}
		super.onSpawn();
		noFollow = false;
	}

	@Override
	public void spawnMe()
	{
		super.spawnMe();
		getAI().setGlobalAggro(0L);
	}

	@Override
	public void onDeath(final Creature killer)
	{
		super.onDeath(killer);
		final Player player = killer.getPlayer();
		final String name = player != null ? player.getName() : "";
		switch(getNpcId())
		{
			case 18120:
			case 18121:
			case 18122:
			case 18123:
			case 18124:
			case 18125:
			case 18126:
			case 18127:
			case 18128:
			case 18129:
			case 18130:
			case 18131:
			case 18149:
			case 18158:
			case 18159:
			case 18160:
			case 18161:
			case 18162:
			case 18163:
			case 18164:
			case 18165:
			case 18183:
			case 18184:
			case 18212:
			case 18213:
			case 18214:
			case 18215:
			case 18216:
			case 18217:
			case 18218:
			case 18219:
			{
				if(_onDeadEventTask != null)
					_onDeadEventTask.cancel(false);
				_onDeadEventTask = ThreadPoolManager.getInstance().schedule(new OnDeadEvent(this, name), 3500L);
				break;
			}
			case 18150:
			case 18151:
			case 18152:
			case 18153:
			case 18154:
			case 18155:
			case 18156:
			case 18157:
			{
				if(_victimSpawnKeyBoxTask != null)
				{
					_victimSpawnKeyBoxTask.cancel(false);
					_victimSpawnKeyBoxTask = null;
				}
				if(_victimShout != null)
				{
					_victimShout.cancel(false);
					_victimShout = null;
				}
				if(_onDeadEventTask != null)
					_onDeadEventTask.cancel(false);
				_onDeadEventTask = ThreadPoolManager.getInstance().schedule(new OnDeadEvent(this, name), 3500L);
				break;
			}
			case 18141:
			case 18142:
			case 18143:
			case 18144:
			case 18145:
			case 18146:
			case 18147:
			case 18148:
			{
				if(FourSepulchersSpawn.isViscountMobsAnnihilated(mysteriousBoxId) && !hasPartyAKey(killer.getPlayer()))
				{
					if(_onDeadEventTask != null)
						_onDeadEventTask.cancel(false);
					_onDeadEventTask = ThreadPoolManager.getInstance().schedule(new OnDeadEvent(this, name), 3500L);
					break;
				}
				break;
			}
			case 18220:
			case 18221:
			case 18222:
			case 18223:
			case 18224:
			case 18225:
			case 18226:
			case 18227:
			case 18228:
			case 18229:
			case 18230:
			case 18231:
			case 18232:
			case 18233:
			case 18234:
			case 18235:
			case 18236:
			case 18237:
			case 18238:
			case 18239:
			case 18240:
			{
				if(FourSepulchersSpawn.isDukeMobsAnnihilated(mysteriousBoxId))
				{
					if(_onDeadEventTask != null)
						_onDeadEventTask.cancel(false);
					_onDeadEventTask = ThreadPoolManager.getInstance().schedule(new OnDeadEvent(this, name), 3500L);
					break;
				}
				break;
			}
		}
	}

	@Override
	public void deleteMe()
	{
		if(_victimSpawnKeyBoxTask != null)
		{
			_victimSpawnKeyBoxTask.cancel(false);
			_victimSpawnKeyBoxTask = null;
		}
		if(_onDeadEventTask != null)
		{
			_onDeadEventTask.cancel(false);
			_onDeadEventTask = null;
		}
		super.deleteMe();
	}

	@Override
	public void reduceCurrentHp(final double damage, final Creature attacker, final Skill skill, final int poleHitCount, final boolean crit, final boolean awake, final boolean standUp, final boolean directHp, final boolean canReflect, final boolean transferDamage, final boolean isDot, final boolean sendMessage)
	{
		super.reduceCurrentHp(damage, attacker, skill, poleHitCount, crit, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
		if(getNpcId() >= 18231 && getNpcId() <= 18240 && !isInvul() && !isDead() && !isDot && attacker != null && attacker != this && Rnd.chance(15))
			giveEff(this, Rnd.get(10000, 20000));
	}

	private static void giveEff(final SepulcherMonsterInstance mob, final int time)
	{
		for(final EffectTemplate et : SepulcherMonsterInstance._skill.getEffectTemplates())
		{
			final Env env = new Env(mob, mob, SepulcherMonsterInstance._skill);
			final Abnormal effect = et.getEffect(env);
			effect.setPeriod(time);
			mob.getAbnormalList().add(effect);
		}
	}

	private boolean hasPartyAKey(final Player player)
	{
		if(player.isInParty())
		{
			for(final Player m : player.getParty().getPartyMembers())
				if(Functions.getItemCount(m, 7260) > 0L)
				{
					player.sendMessage((player.getObjectId() == m.getObjectId() ? "You" : "Player " + m.getName()) + " already has the key!");
					return true;
				}
		}
		else if(Functions.getItemCount(player, 7260) > 0L)
		{
			player.sendMessage("You already has the key!");
			return true;
		}
		return false;
	}

	@Override
	public boolean canChampion()
	{
		return false;
	}

	private class VictimShout implements Runnable
	{
		private final SepulcherMonsterInstance _activeChar;

		public VictimShout(final SepulcherMonsterInstance activeChar)
		{
			_activeChar = activeChar;
		}

		@Override
		public void run()
		{
			if(_activeChar.isDead())
				return;
			if(!_activeChar.isVisible())
				return;
			SepulcherMonsterInstance.this.broadcastPacket(new L2GameServerPacket[] { new NpcSay(SepulcherMonsterInstance.this, 0, "forgive me!!") });
		}
	}

	private class VictimSpawnKeyBox implements Runnable
	{
		private final SepulcherMonsterInstance _activeChar;

		public VictimSpawnKeyBox(final SepulcherMonsterInstance activeChar)
		{
			_activeChar = activeChar;
		}

		@Override
		public void run()
		{
			if(_activeChar.isDead())
				return;
			if(!_activeChar.isVisible())
				return;
			FourSepulchersSpawn.spawnKeyBox(_activeChar, "VictimSpawnKeyBox");
			SepulcherMonsterInstance.this.broadcastPacket(new L2GameServerPacket[] {
					new NpcSay(SepulcherMonsterInstance.this, 0, "Many thanks for rescue me.") });
			if(_victimShout != null)
			{
				_victimShout.cancel(false);
				_victimShout = null;
			}
			_activeChar.noFollow = true;
			_activeChar.stopMove();
		}
	}

	private class OnDeadEvent implements Runnable
	{
		SepulcherMonsterInstance _activeChar;
		String _name;

		public OnDeadEvent(final SepulcherMonsterInstance activeChar, final String name)
		{
			_activeChar = activeChar;
			_name = name;
		}

		@Override
		public void run()
		{
			switch(_activeChar.getNpcId())
			{
				case 18120:
				case 18121:
				case 18122:
				case 18123:
				case 18124:
				case 18125:
				case 18126:
				case 18127:
				case 18128:
				case 18129:
				case 18130:
				case 18131:
				case 18149:
				case 18158:
				case 18159:
				case 18160:
				case 18161:
				case 18162:
				case 18163:
				case 18164:
				case 18165:
				case 18183:
				case 18184:
				case 18212:
				case 18213:
				case 18214:
				case 18215:
				case 18216:
				case 18217:
				case 18218:
				case 18219:
				{
					FourSepulchersSpawn.spawnKeyBox(_activeChar, _name);
					break;
				}
				case 18150:
				case 18151:
				case 18152:
				case 18153:
				case 18154:
				case 18155:
				case 18156:
				case 18157:
				{
					FourSepulchersSpawn.spawnExecutionerOfHalisha(_activeChar);
					break;
				}
				case 18141:
				case 18142:
				case 18143:
				case 18144:
				case 18145:
				case 18146:
				case 18147:
				case 18148:
				{
					FourSepulchersSpawn.spawnMonster(_activeChar.mysteriousBoxId, _name);
					break;
				}
				case 18220:
				case 18221:
				case 18222:
				case 18223:
				case 18224:
				case 18225:
				case 18226:
				case 18227:
				case 18228:
				case 18229:
				case 18230:
				case 18231:
				case 18232:
				case 18233:
				case 18234:
				case 18235:
				case 18236:
				case 18237:
				case 18238:
				case 18239:
				case 18240:
				{
					FourSepulchersSpawn.spawnArchonOfHalisha(_activeChar.mysteriousBoxId);
					break;
				}
			}
		}
	}

	private class ChangeImmortal implements Runnable
	{
		private final SepulcherMonsterInstance activeChar;

		public ChangeImmortal(final SepulcherMonsterInstance mob)
		{
			activeChar = mob;
		}

		@Override
		public void run()
		{
			giveEff(activeChar, 120000);
		}
	}
}
