package ai;

import l2s.gameserver.GameTimeController;
import l2s.gameserver.ai.Mystic;
import l2s.gameserver.listener.game.OnDayNightChangeListener;
import l2s.gameserver.model.instances.NpcInstance;

public class NightAgressionMystic extends Mystic
{
	private class NightAgressionDayNightListener implements OnDayNightChangeListener
	{
		private NightAgressionDayNightListener()
		{
			if(GameTimeController.getInstance().isNowNight())
				onNight();
			else
				onDay();
		}

		@Override
		public void onNight()
		{
			NpcInstance actor = getActor();
			if(actor != null)
				actor.setAggroRange(-1);
		}

		@Override
		public void onDay()
		{
			NpcInstance actor = getActor();
			if(actor != null)
				actor.setAggroRange(0);
		}
	}

	public NightAgressionMystic(NpcInstance actor)
	{
		super(actor);
		GameTimeController.getInstance().addListener(new NightAgressionDayNightListener());
	}
}
