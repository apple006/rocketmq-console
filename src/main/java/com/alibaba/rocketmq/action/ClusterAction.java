package com.alibaba.rocketmq.action;

import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.rocketmq.common.Table;
import com.alibaba.rocketmq.service.ClusterService;

import java.util.Iterator;
import java.util.Set;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-2-8
 */
@Controller
@RequestMapping("/cluster")
public class ClusterAction extends AbstractAction {

    @Autowired
    ClusterService clusterService;


    @Override
    protected String getFlag() {
        return "cluster_flag";
    }


    @Override
    protected String getName() {
        return "Cluster";
    }

    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String list(ModelMap map) {
        putPublicAttribute(map, "list");
        try {
            Set<Table> tables = clusterService.lists();
            Table[] tableShow = new Table[tables.size()];
            Iterator<Table> iterator = tables.iterator();
            int i = 0;
            while (iterator.hasNext()){
                Table table = iterator.next();
                tableShow[i] = table;
                i++;
            }
            putTable(map, tableShow);
        }
        catch (Throwable t) {
            putAlertMsg(t, map);
        }
        return TEMPLATE;
    }
    /*@RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String list(ModelMap map) {
        putPublicAttribute(map, "list");
        try {
            Table table = clusterService.list();
            putTable(map, table);
        }
        catch (Throwable t) {
            putAlertMsg(t, map);
        }
        return TEMPLATE;
    }*/


    @RequestMapping(value = "/demo.do", method = RequestMethod.GET)
    public String demo() {
        return "cluster/demo";
    }

}
