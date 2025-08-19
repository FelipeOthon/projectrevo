package events.tournament;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;

public class Team
{
	private int _id;
	private int _leader;
	private int _category;
	private int _member1;
	private int _member2;
	private String _name;

	public String getName()
	{
		return _name;
	}

	public void setName(final String name)
	{
		_name = name;
	}

	public boolean addMember(final int member)
	{
		if(_member1 == 0)
		{
			_member1 = member;
			return true;
		}
		if(_member2 == 0)
		{
			_member2 = member;
			return true;
		}
		return false;
	}

	public boolean removeMember(final int member)
	{
		if(_member1 == member)
		{
			_member1 = 0;
			return true;
		}
		if(_member2 == member)
		{
			_member2 = 0;
			return true;
		}
		return false;
	}

	public int getLeader()
	{
		return _leader;
	}

	public void setLeader(final int leader)
	{
		_leader = leader;
	}

	public int[] getMembers()
	{
		if(_member1 != 0 && _member2 == 0)
			return new int[] { _leader, _member1 };
		if(_member1 == 0 && _member2 != 0)
			return new int[] { _leader, _member2 };
		if(_member1 != 0 && _member2 != 0)
			return new int[] { _leader, _member1, _member2 };
		return new int[] { _leader };
	}

	public List<Player> getOnlineMembers()
	{
		final List<Player> result = new ArrayList<Player>();
		for(final int obj_id : getMembers())
		{
			final Player obj = GameObjectsStorage.getPlayer(obj_id);
			if(obj != null)
				result.add(obj);
		}
		return result;
	}

	public int getCount()
	{
		int i = 1;
		if(_member1 != 0)
			++i;
		if(_member2 != 0)
			++i;
		return i;
	}

	public int getOnlineCount()
	{
		return getOnlineMembers().size();
	}

	public int getCategory()
	{
		return _category;
	}

	public void setCategory(final int category)
	{
		_category = category;
	}

	public int getId()
	{
		return _id;
	}

	public void setId(final int id)
	{
		_id = id;
	}
}
