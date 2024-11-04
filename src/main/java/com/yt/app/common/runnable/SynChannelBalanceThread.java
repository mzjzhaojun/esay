package com.yt.app.common.runnable;

import org.redisson.api.RLock;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.vo.SysTyBalance;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.PayUtil;
import com.yt.app.common.util.RedissonUtil;

public class SynChannelBalanceThread implements Runnable {

	private long id;

	public SynChannelBalanceThread(long _id) {
		id = _id;
	}

	@Override
	public void run() {
		TenantIdContext.removeFlag();
		ChannelMapper channelmapper = BeanContext.getApplicationContext().getBean(ChannelMapper.class);
		Channel cl = channelmapper.get(id);
		RLock lock = RedissonUtil.getLock(id);
		try {
			lock.lock();
			String balance = null;
			switch (cl.getName()) {
			case DictionaryResource.DFTXAISLE:
				SysTyBalance stb = PayUtil.SendTxSelectBalance(cl);
				cl.setRemotebalance(stb.getAvailableBalance());
				break;
			case DictionaryResource.HSAISLE:
				balance = PayUtil.SendHsGetBalance(cl);
				if (balance != null)
					cl.setRemotebalance(Double.valueOf(balance));
				break;
			case DictionaryResource.WDAISLE:
				balance = PayUtil.SendWdGetBalance(cl);
				if (balance != null)
					cl.setRemotebalance(Double.valueOf(balance));
				break;
			case DictionaryResource.RBLAISLE:
				balance = PayUtil.SendRblGetBalance(cl);
				if (balance != null)
					cl.setRemotebalance(Double.valueOf(balance));
				break;
			case DictionaryResource.GZAISLE:
				balance = PayUtil.SendGzGetBalance(cl);
				if (balance != null)
					cl.setRemotebalance(Double.valueOf(balance));
				break;
			case DictionaryResource.WJAISLE:
				balance = PayUtil.SendWjGetBalance(cl);
				if (balance != null)
					cl.setRemotebalance(Double.valueOf(balance));
				break;
			case DictionaryResource.FCAISLE:
				balance = PayUtil.SendFcGetBalance(cl);
				if (balance != null)
					cl.setRemotebalance(Double.valueOf(balance));
				break;
			case DictionaryResource.AKLAISLE:
				balance = PayUtil.SendAklGetBalance(cl);
				if (balance != null)
					cl.setRemotebalance(Double.valueOf(balance));
				break;
			}
			channelmapper.put(cl);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

}
