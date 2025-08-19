package items;

import java.util.concurrent.ScheduledFuture;

import l2s.commons.lang.reference.HardReference;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.ExUseSharedGroupItem;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.AbnormalEffect;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.templates.item.WeaponTemplate;

public class BreakingArrow implements IItemHandler, ScriptFile
{
	private static final int[] ITEM_IDS;
	private ScheduledFuture<?> _blockTask;

	public BreakingArrow()
	{
		_blockTask = null;
	}

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = playable.getPlayer();
		if(player == null)
			return false;
		final WeaponTemplate weapon = player.getActiveWeaponItem();
		final int itemId = item.getItemId();
		if(weapon != null && weapon.getItemType() == WeaponTemplate.WeaponType.BOW)
		{
			final Skill sk = SkillTable.getInstance().getInfo(2234, 1);
			if(player.isSkillDisabled(sk))
			{
				player.sendPacket(new SystemMessage(48).addItemName(itemId));
				return false;
			}
			final GameObject target = player.getTarget();
			if(target == null || !target.isNpc() || ((NpcInstance) target).getNpcId() != 29045)
			{
				player.sendPacket(Msg.INCORRECT_TARGET);
				return false;
			}
			final NpcInstance frintezza = (NpcInstance) target;
			if(!player.isInRange(frintezza, 900L))
			{
				player.sendPacket(Msg.YOUR_TARGET_IS_OUT_OF_RANGE);
				return false;
			}
			if(!frintezza.isBlocked())
			{
				player.sendPacket(new SystemMessage(46).addItemName(itemId));
				player.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(player, frintezza, 2234, 1, 2000, 0L) });
				player.getInventory().destroyItem(item, 1L, false);
				player.sendPacket(new ExUseSharedGroupItem(itemId, 5, 30000, 30000));
				player.disableSkill(sk, 30000L);
				frintezza.startAbnormalEffect(AbnormalEffect.STUN);
				frintezza.startStunning();
				frintezza.block();
				if(_blockTask != null)
				{
					_blockTask.cancel(false);
					_blockTask = null;
				}
				_blockTask = ThreadPoolManager.getInstance().schedule(new UnBlockTask(frintezza), 15000L);
			}
			else
				player.sendMessage("Already stunned.");
		}
		else
			player.sendPacket(new SystemMessage(113).addItemName(itemId));
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return BreakingArrow.ITEM_IDS;
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
		ITEM_IDS = new int[] { 8192 };
	}

	public static class UnBlockTask implements Runnable
	{
		private final HardReference<NpcInstance> _npcRef;

		public UnBlockTask(NpcInstance npc)
		{
			_npcRef = npc.getRef();
		}

		@Override
		public void run()
		{
			final NpcInstance npc = _npcRef.get();
			if(npc == null)
				return;

			npc.unblock();
			npc.stopAbnormalEffect(AbnormalEffect.STUN);
			npc.stopStunning();
		}
	}
}
