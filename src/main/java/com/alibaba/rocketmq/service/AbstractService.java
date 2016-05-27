package com.alibaba.rocketmq.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.protocol.body.ClusterInfo;
import com.alibaba.rocketmq.remoting.exception.RemotingConnectException;
import com.alibaba.rocketmq.remoting.exception.RemotingSendRequestException;
import com.alibaba.rocketmq.remoting.exception.RemotingTimeoutException;
import io.netty.util.internal.ConcurrentSet;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.rocketmq.common.constant.PermName;
import com.alibaba.rocketmq.config.ConfigureInitializer;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.alibaba.rocketmq.tools.command.SubCommand;


public abstract class AbstractService {

    @Autowired
    ConfigureInitializer configureInitializer;


    protected DefaultMQAdminExt getDefaultMQAdminExt() {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
        defaultMQAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
        return defaultMQAdminExt;
    }

    protected void shutdownDefaultMQAdminExt(DefaultMQAdminExt defaultMQAdminExt) {
        defaultMQAdminExt.shutdown();
    }

    protected Set<DefaultMQAdminExt> getDefaultMQAdminExts(int nsrvCount) {
        Set<DefaultMQAdminExt> result = new ConcurrentSet<DefaultMQAdminExt>();
        for (int i = 0; i < nsrvCount; i++) {
            DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
            defaultMQAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()) + i);
            result.add(defaultMQAdminExt);
        }
        return result;
    }

    protected void startMQAdminExts(Set<DefaultMQAdminExt> exts) throws MQClientException {
        for (DefaultMQAdminExt one : exts){
            one.start();
        }
    }

    protected void shutdownDefaultMQAdminExts(Set<DefaultMQAdminExt> exts) {
        for (DefaultMQAdminExt one : exts){
            one.shutdown();
        }
    }

    /*protected Set<ClusterInfo> getClusters(Set<DefaultMQAdminExt> exts) throws InterruptedException, MQBrokerException, RemotingTimeoutException, RemotingSendRequestException, RemotingConnectException {
        Set<ClusterInfo> result = new ConcurrentSet<ClusterInfo>();
        for (DefaultMQAdminExt one : exts){
            result.add(one.examineBrokerClusterInfo());
        }
        return result;
    }

    protected Set<Set<Map.Entry<String, Set<String>>>> getClustersSet(Set<ClusterInfo> clusters) {
        Set<Set<Map.Entry<String, Set<String>>>> result = new ConcurrentSet<Set<Map.Entry<String, Set<String>>>>();
        for (ClusterInfo one : clusters){
            result.add(one.getClusterAddrTable().entrySet());
        }
        return result;
    }

    protected Map<DefaultMQAdminExt, ClusterInfo> getSrvClusters(Set<DefaultMQAdminExt> exts) throws InterruptedException, MQBrokerException, RemotingTimeoutException, RemotingSendRequestException, RemotingConnectException {
        Map<DefaultMQAdminExt, ClusterInfo> result = new ConcurrentHashMap<DefaultMQAdminExt, ClusterInfo>();
        for (DefaultMQAdminExt one : exts){
            result.put(one, one.examineBrokerClusterInfo());
        }
        return result;
    }

    protected int getClustersRowCount(Set<Set<Map.Entry<String, Set<String>>>> clustersSet){
        int count = 0;
        for (Set<Map.Entry<String, Set<String>>> one : clustersSet){
            count += one.size();
        }
        return count;
    }*/

    protected Collection<Option> getOptions(SubCommand subCommand) {
        Options options = new Options();
        subCommand.buildCommandlineOptions(options);
        @SuppressWarnings("unchecked")
        Collection<Option> col = options.getOptions();
        return col;
    }


    protected int translatePerm(String perm) {
        if (perm.toLowerCase().equals("r")) {
            return PermName.PERM_READ;
        }
        else if (perm.toLowerCase().equals("w")) {
            return PermName.PERM_WRITE;
        }
        else {
            return PermName.PERM_READ | PermName.PERM_WRITE;
        }
    }

}
