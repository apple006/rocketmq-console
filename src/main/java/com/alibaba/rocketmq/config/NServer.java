package com.alibaba.rocketmq.config;

import com.alibaba.rocketmq.common.MixAll;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import io.netty.util.internal.ConcurrentSet;

import java.util.Set;

/**
 * Created by ldb on 2016/5/27.
 */
public class NServer {
    private String namesrvAddr;
    private Set<String> addresses;
    private static class SingletonHolder {
        private static final NServer INSTANCE = new NServer();
    }

    private NServer() {
        namesrvAddr = System.getProperty(MixAll.NAMESRV_ADDR_PROPERTY);
        addresses = new ConcurrentSet<String>();
        setAddresses(namesrvAddr);
    }

    private void setAddresses(final String addrs){
        if (addrs == null || addrs.isEmpty()){
            return;
        }
        String[] splits = addrs.split(";");
        for (String one: splits) {
            addresses.add(one);
        }
    }

    private static final NServer getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static Set<String> getAddresses(){
        return getInstance().addresses;
    }
}
