package l2s.authserver.network.gamecomm.gs2as;

import l2s.authserver.AuthBanManager;
import l2s.authserver.network.gamecomm.ReceivablePacket;
import l2s.commons.ban.BanBindType;

/**
 * @author Bonux (Head Developer L2-scripts.com)
 * 10.04.2019
 * Developed for L2-Scripts.com
 **/
public class UnbanRequest extends ReceivablePacket
{
	private BanBindType bindType;
	private String bindValue;

	@Override
	protected void readImpl()
	{
		bindType = BanBindType.VALUES[readC()];
		bindValue = readS();
	}

	@Override
	protected void runImpl()
	{
		AuthBanManager.getInstance().removeBan(bindType, bindValue);
	}
}