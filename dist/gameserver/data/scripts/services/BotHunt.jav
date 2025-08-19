package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2scripts.sguard.core.manager.session.GuardSession;

import l2s.gameserver.listener.actor.PlayerListenerList;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.scripts.ScriptFile;
import l2p.gameserver.listener.actor.OnMagicUseListener;

/**
 * @author sguard 2017
 */

public class BotHunt implements ScriptFile
{
	private final OnMagicUseListener _listener = new OnPlayerMagicSkillUseImpl();
	private static final Logger _log = LoggerFactory.getLogger(BotHunt.class);

	private class OnPlayerMagicSkillUseImpl implements OnMagicUseListener
	{
		@Override
		public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt)
		{
			if(actor == null || !actor.isPlayer())
				return;
			if(skill == null || !skill.isOffensive())
				return;
			if(target == null || target.isPlayable() && target != actor)
				return;
			if(alt)
				return;

			Player player = actor.getPlayer();
			GuardSession session = BotHuntDAO.validateActiveSeasons(player); //checking if more than 1 session is open
			if(session == null) //means the guy plays in 1 wnd and there's no need to proceed
				return;
			BotHuntDAO.proceed(player, session);
		}
	}

	@Override
	public void onLoad()
	{
		PlayerListenerList.addGlobal(_listener);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
