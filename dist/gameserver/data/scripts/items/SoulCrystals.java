package items;

import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.SetupGauge;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.Stats;
import l2s.gameserver.tables.SkillTable;

public class SoulCrystals implements IItemHandler, ScriptFile
{
	public static final int[] _itemIds;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = playable.getPlayer();
		final Skill sk = SkillTable.getInstance().getInfo(2096, 1);
		if(player.isSkillDisabled(sk))
		{
			player.sendPacket(new SystemMessage(48).addItemName(item.getItemId()));
			return false;
		}
		final GameObject target = player.getTarget();
		if(target == null || !target.isMonster())
		{
			player.sendPacket(new IBroadcastPacket[] { Msg.INCORRECT_TARGET, Msg.ActionFail });
			return false;
		}
		if(player.isImmobilized() || player.isCastingNow())
		{
			player.sendActionFailed();
			return false;
		}
		final int range = sk.getCastRange();
		if(range > 0)
		{
			if(!player.isInRange(target, range))
			{
				player.sendPacket(new IBroadcastPacket[] { Msg.YOUR_TARGET_IS_OUT_OF_RANGE, Msg.ActionFail });
				return false;
			}
			player.turn((Creature) target, range + 400);
		}
		final long reuse = Math.max(0L, (long) player.calcStat(Stats.PHYSIC_REUSE_RATE, sk.getReuseDelay() * 333L / Math.max(player.getPAtkSpd(), 1), (Creature) null, sk));
		player.disableSkill(sk, reuse);
		final int skillHitTime = sk.getHitTime();
		player.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(player, 2096, 1, skillHitTime, 0L) });
		player.sendPacket(new SetupGauge(0, skillHitTime));
		player._skillTask = ThreadPoolManager.getInstance().schedule(new CrystalFinalizer(player, (MonsterInstance) target), Math.max(skillHitTime, 50));
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return SoulCrystals._itemIds;
	}

	@Override
	public void onLoad()
	{
		ItemHandler.getInstance().registerItemHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		_itemIds = new int[] {
				4629,
				4640,
				4651,
				4630,
				4641,
				4652,
				4631,
				4642,
				4653,
				4632,
				4643,
				4654,
				4633,
				4644,
				4655,
				4634,
				4645,
				4656,
				4635,
				4646,
				4657,
				4636,
				4647,
				4658,
				4637,
				4648,
				4659,
				4638,
				4649,
				4660,
				4639,
				4650,
				4661,
				5577,
				5578,
				5579,
				5580,
				5581,
				5582,
				5908,
				5911,
				5914 };
	}

	static class CrystalFinalizer implements Runnable
	{
		private Player _activeChar;
		private MonsterInstance _target;

		CrystalFinalizer(final Player activeChar, final MonsterInstance target)
		{
			_activeChar = activeChar;
			_target = target;
		}

		@Override
		public void run()
		{
			_activeChar.sendActionFailed();
			_activeChar.clearCastVars();
			if(_activeChar.isDead() || _target == null || _target.isDead())
				return;
			_target.addAbsorber(_activeChar);
			if(_activeChar.isInRange(_target, Config.SC_AGGRO_RANGE) && GeoEngine.canSeeTarget(_activeChar, _target))
				_target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, _activeChar, null, 100);
		}
	}
}
