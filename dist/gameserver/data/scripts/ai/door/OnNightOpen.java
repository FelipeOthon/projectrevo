package ai.door;

import l2s.commons.lang.reference.HardReference;
import l2s.gameserver.GameTimeController;
import l2s.gameserver.ai.DoorAI;
import l2s.gameserver.listener.game.OnDayNightChangeListener;
import l2s.gameserver.listener.GameListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.DoorInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author team: Lucera 3
 */
public class OnNightOpen extends DoorAI
{
	private static class NightDoorOpenController implements OnDayNightChangeListener
	{
		private HardReference<? extends Creature> _actRef;

		public NightDoorOpenController(DoorInstance actor)
		{
			_actRef = actor.getRef();
		}

		@Override
		public void onDay()
		{
			//
		}

		@Override
		public void onNight()
		{
			final Creature creature = _actRef.get();
			if(creature != null && creature.isDoor())
			{
				((DoorInstance) creature).openMe();
				_log.info("Zaken door is opened for 5 min.");
			}
			else
			{
				_log.warn("Zaken door is null");
			}
		}
	}

	private static Logger _log = LoggerFactory.getLogger(OnNightOpen.class);

	public OnNightOpen(final DoorInstance actor)
	{
		super(actor);
		GameTimeController.getInstance().addListener(new NightDoorOpenController(actor));
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}
