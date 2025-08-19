package ai;

import bosses.AntharasManager;
import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;

public class Antharas extends DefaultAI
{
	private final Skill meteor;
	private final Skill shock;
	private final Skill shock_fly;
	private final Skill terror;
	private final Skill terror2;
	private final Skill curse;
	private final Skill mouth_odor_attack;
	private final Skill fossilization;
	private final Skill ordinary_attack;
	private final Skill a_d_ordinary_attack;
	private final Skill regen1;
	private final Skill regen2;
	private final Skill regen3;
	private int _hpStage;

	public Antharas(final NpcInstance actor)
	{
		super(actor);
		_hpStage = 0;
		terror2 = SkillTable.getInstance().getInfo(5092, 1);
		meteor = SkillTable.getInstance().getInfo(5093, 1);
		shock = SkillTable.getInstance().getInfo(4106, 1);
		shock_fly = SkillTable.getInstance().getInfo(4107, 1);
		terror = SkillTable.getInstance().getInfo(4108, 1);
		curse = SkillTable.getInstance().getInfo(4109, 1);
		mouth_odor_attack = SkillTable.getInstance().getInfo(4110, 1);
		fossilization = SkillTable.getInstance().getInfo(4111, 1);
		ordinary_attack = SkillTable.getInstance().getInfo(4112, 1);
		a_d_ordinary_attack = SkillTable.getInstance().getInfo(4113, 1);
		regen1 = SkillTable.getInstance().getInfo(4239, 1);
		regen2 = SkillTable.getInstance().getInfo(4240, 1);
		regen3 = SkillTable.getInstance().getInfo(4241, 1);
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		AntharasManager.setLastAttackTime();
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		final Creature target;
		if((target = prepareTarget()) == null)
			return false;
		final NpcInstance actor = getActor();
		if(actor.isDead())
			return false;
		final double chp = actor.getCurrentHpPercents();
		if(_hpStage == 0)
		{
			actor.altOnMagicUseTimer(actor, regen1);
			_hpStage = 1;
		}
		else if(_hpStage == 1 && chp < 75.0)
		{
			actor.altOnMagicUseTimer(actor, regen2);
			_hpStage = 2;
		}
		else if(_hpStage == 2 && chp < 50.0)
		{
			actor.altOnMagicUseTimer(actor, regen3);
			_hpStage = 3;
		}
		else if(_hpStage == 3 && chp < 30.0)
		{
			actor.altOnMagicUseTimer(actor, regen3);
			_hpStage = 4;
		}
		final int attack = actor.getNpcId() == 29068 ? 20 : actor.getNpcId() == 29067 ? 30 : 50;
		if(Rnd.chance(attack))
			return chooseTaskAndTargets(Rnd.chance(50) ? ordinary_attack : a_d_ordinary_attack, target, actor.getDistance(target));
		final Skill r_skill = actor.getNpcId() == 29068 ? strong(chp) : actor.getNpcId() == 29067 ? normal(chp) : weak(chp);
		return chooseTaskAndTargets(r_skill, target, actor.getDistance(target));
	}

	private Skill weak(final double chp)
	{
		if(chp < 20.0 && Rnd.chance(10))
			return mouth_odor_attack;
		if(chp < 50.0 && Rnd.chance(15))
			return fossilization;
		if(chp < 30.0 && Rnd.chance(20))
			return shock_fly;
		if(Rnd.chance(30))
			return shock;
		if(Rnd.chance(10))
			return terror;
		if(Rnd.chance(20))
			return curse;
		return Rnd.chance(50) ? ordinary_attack : a_d_ordinary_attack;
	}

	private Skill normal(final double chp)
	{
		if(chp < 20.0 && Rnd.chance(15))
			return meteor;
		if(chp < 30.0 && Rnd.chance(30))
			return mouth_odor_attack;
		if(chp < 75.0 && Rnd.chance(15))
			return fossilization;
		if(chp < 60.0 && Rnd.chance(20))
			return shock_fly;
		if(Rnd.chance(45))
			return shock;
		if(Rnd.chance(15))
			return terror;
		if(Rnd.chance(30))
			return curse;
		return Rnd.chance(50) ? ordinary_attack : a_d_ordinary_attack;
	}

	private Skill strong(final double chp)
	{
		if(chp < 25.0 && Rnd.chance(20))
			return meteor;
		if(chp < 50.0 && Rnd.chance(30))
			return mouth_odor_attack;
		if(chp < 75.0 && Rnd.chance(20))
			return fossilization;
		if(chp < 60.0 && Rnd.chance(25))
			return shock_fly;
		if(Rnd.chance(50))
			return shock;
		if(Rnd.chance(15))
			return terror;
		if(Rnd.chance(35))
			return curse;
		return Rnd.chance(50) ? ordinary_attack : a_d_ordinary_attack;
	}

	@Override
	protected boolean maybeMoveToHome()
	{
		final NpcInstance actor = getActor();
		if(actor != null && !AntharasManager.getZone().checkIfInZone(actor.getX(), actor.getY()))
			teleportHome();
		return false;
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}
