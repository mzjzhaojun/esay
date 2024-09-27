package com.yt.app.common.runnable;

import java.util.List;

import com.yt.app.api.v1.entity.Tronaddress;
import com.yt.app.api.v1.mapper.TronaddressMapper;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.util.TronUtil;

public class TronGetAddressThread4 implements Runnable {

	public TronGetAddressThread4() {
	}

	@Override
	public void run() {
		TenantIdContext.removeFlag();
		TronaddressMapper mapper = BeanContext.getApplicationContext().getBean(TronaddressMapper.class);
		while (true) {
			List<String> liststringcode = TronUtil.mnemoniCode();
			if (liststringcode != null) {
				List<String> listaddress = TronUtil.generateAddress(liststringcode);
				if (listaddress != null) {
					String address = listaddress.get(2);
					String endchar = address.substring(address.length() - 1);
					String startchar = address.substring(address.length() - 5, address.length() - 1);
					String sixchar = endchar + endchar + endchar + endchar;
					if (sixchar.equalsIgnoreCase(startchar)) {
						Tronaddress t = new Tronaddress();
						t.setAddress(address);
						t.setPrivatekey(listaddress.get(0));
						t.setHexaddress(listaddress.get(1));
						t.setMnemoniccode(liststringcode.toString());
						t.setRemark(4 + "");
						mapper.post(t);
					}
				}
			}
		}
	}

}