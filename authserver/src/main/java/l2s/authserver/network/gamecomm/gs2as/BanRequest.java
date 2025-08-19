package l2s.authserver.network.gamecomm.gs2as;

import l2s.authserver.AuthBanManager;
import l2s.authserver.network.gamecomm.ReceivablePacket;
import l2s.commons.ban.BanBindType;

/**
 * @author Bonux (Head Developer L2-scripts.com)
 * 10.04.2019
 * Developed for L2-Scripts.com
 **/
public class BanRequest extends ReceivablePacket
{
	private BanBindType bindType;
	private String bindValue;
	private int endTime;
	private String reason;

	@Override
	protected void readImpl()
	{
		bindType = BanBindType.VALUES[readC()];
		bindValue = readS();
		endTime = readD();
		reason = readS();
	}

	@Override
	protected void runImpl()
	{
		AuthBanManager.getInstance().giveBan(bindType, bindValue, endTime, reason);
	}
}