package npc.model;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.SpecialMonsterInstance;
import l2s.gameserver.templates.npc.NpcTemplate;

public class SquashInstance extends SpecialMonsterInstance
{
	private static final long serialVersionUID = 1L;
	public static final int Young_Squash = 12774;
	public static final int High_Quality_Squash = 12775;
	public static final int Low_Quality_Squash = 12776;
	public static final int Large_Young_Squash = 12777;
	public static final int High_Quality_Large_Squash = 12778;
	public static final int Low_Quality_Large_Squash = 12779;
	public static final int King_Squash = 13016;
	public static final int Emperor_Squash = 13017;
	private int _spawnerId;

	public SquashInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	public void setSpawner(final Player spawner)
	{
		if(spawner != null)
			_spawnerId = spawner.getObjectId();
	}

	public Player getSpawner()
	{
		return GameObjectsStorage.getPlayer(_spawnerId);
	}

	@Override
	public void reduceCurrentHp(double i, final Creature attacker, final Skill skill, final int poleHitCount, final boolean crit, final boolean awake, final boolean standUp, final boolean directHp, final boolean canReflect, final boolean transferDamage, final boolean isDot, final boolean sendMessage)
	{
		if(attacker.getActiveWeaponInstance() == null)
			return;
		final int weaponId = attacker.getActiveWeaponInstance().getItemId();
		if((getNpcId() == 12779 || getNpcId() == 12778 || getNpcId() == 13017) && weaponId != 4202 && weaponId != 5133 && weaponId != 5817 && weaponId != 7058 && weaponId != 8350)
			return;
		i = 1.0;
		super.reduceCurrentHp(i, attacker, skill, poleHitCount, crit, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}

	@Override
	public long getRegenTick()
	{
		return 0L;
	}

	@Override
	public synchronized void startRegeneration()
	{}

	@Override
	public boolean canChampion()
	{
		return false;
	}
}
