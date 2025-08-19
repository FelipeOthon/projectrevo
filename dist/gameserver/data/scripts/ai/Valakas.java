package ai;

import bosses.ValakasManager;
import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;

public class Valakas extends DefaultAI
{
	private final Skill lava_skin;
	private final Skill trample;
	private final Skill trample2;
	private final Skill breath_low;
	private final Skill breath_high;
	private final Skill tail_stompP;
	private final Skill tail_stompM;
	private final Skill animation;
	private final Skill shock_fly;
	private final Skill fear;
	private final Skill meteor_storm;
	private int _hpStage;
	private long _lava_skin_timer;
	private long defenceDownTimer;

	public Valakas(final NpcInstance actor)
	{
		super(actor);
		_hpStage = 0;
		_lava_skin_timer = 0L;
		defenceDownTimer = Long.MAX_VALUE;
		lava_skin = SkillTable.getInstance().getInfo(4680, 1);
		trample = SkillTable.getInstance().getInfo(4681, 1);
		trample2 = SkillTable.getInstance().getInfo(4682, 1);
		breath_low = SkillTable.getInstance().getInfo(4683, 1);
		breath_high = SkillTable.getInstance().getInfo(4684, 1);
		tail_stompP = SkillTable.getInstance().getInfo(4685, 1);
		tail_stompM = SkillTable.getInstance().getInfo(4686, 1);
		animation = SkillTable.getInstance().getInfo(4687, 1);
		shock_fly = SkillTable.getInstance().getInfo(4688, 1);
		fear = SkillTable.getInstance().getInfo(4689, 1);
		meteor_storm = SkillTable.getInstance().getInfo(4690, 1);
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		ValakasManager.setLastAttackTime();
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
			actor.altOnMagicUseTimer(actor, SkillTable.getInstance().getInfo(4691, 1));
			_hpStage = 1;
		}
		else if(_hpStage == 1 && chp < 80.0)
		{
			defenceDownTimer = System.currentTimeMillis();
			actor.altOnMagicUseTimer(actor, SkillTable.getInstance().getInfo(4691, 1));
			_hpStage = 2;
		}
		else if(_hpStage == 2 && chp < 50.0)
		{
			actor.altOnMagicUseTimer(actor, SkillTable.getInstance().getInfo(4691, 2));
			_hpStage = 3;
		}
		else if(_hpStage == 3 && chp < 30.0)
		{
			actor.altOnMagicUseTimer(actor, SkillTable.getInstance().getInfo(4691, 3));
			_hpStage = 4;
		}
		else if(_hpStage == 4 && chp < 10.0)
		{
			actor.altOnMagicUseTimer(actor, SkillTable.getInstance().getInfo(4691, 4));
			_hpStage = 5;
		}
		if(_hpStage >= 3 && _lava_skin_timer + 300000L < System.currentTimeMillis())
		{
			_lava_skin_timer = System.currentTimeMillis();
			if(Rnd.chance(60))
				actor.altOnMagicUseTimer(actor, lava_skin);
		}
		if(defenceDownTimer < System.currentTimeMillis())
		{
			defenceDownTimer = System.currentTimeMillis() + 120000L + Rnd.get(60) * 1000L;
			return chooseTaskAndTargets(fear, target, actor.getDistance(target));
		}
		final int chanceAttack = chp < 10.0 ? 10 : chp < 30.0 ? 20 : 50;
		if(Rnd.chance(chanceAttack))
			return chooseTaskAndTargets(Rnd.chance(60) ? trample : trample2, target, actor.getDistance(target));
		final int chanceSkill = chp < 10.0 ? 20 : chp < 25.0 ? 10 : chp < 50.0 ? 2 : 0;
		Skill r_skill;
		if(Rnd.chance(chanceSkill))
			r_skill = meteor_storm;
		else if(chp < 30.0 && chp > 15.0 && Rnd.chance(15))
			r_skill = animation;
		else if(Rnd.chance(chp > 10.0 ? 5 : 2))
			r_skill = fear;
		else if(chp < 50.0 && Rnd.chance(20))
			r_skill = shock_fly;
		else
			r_skill = random(Rnd.get(_hpStage > 1 ? 5 : 4));
		return chooseTaskAndTargets(r_skill, target, actor.getDistance(target));
	}

	private Skill random(final int n)
	{
		if(n == 0)
			return breath_low;
		if(n == 1)
			return breath_high;
		if(n == 2)
			return tail_stompP;
		if(n == 3)
			return tail_stompM;
		return shock_fly;
	}

	@Override
	protected boolean maybeMoveToHome()
	{
		final NpcInstance actor = getActor();
		if(actor != null && !ValakasManager.getZone().checkIfInZone(actor.getX(), actor.getY()))
			teleportHome();
		return false;
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}
