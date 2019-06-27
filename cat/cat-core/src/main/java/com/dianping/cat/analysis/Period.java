package com.dianping.cat.analysis;

import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.statistic.ServerStatisticManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class Period {

    private static final Logger logger = LoggerFactory.getLogger(Period.class);

    private long startTime;
    private long endTime;
    private Map<String, List<PeriodTask>> tasks;
    private ServerStatisticManager serverStatisticManager = ServerStatisticManager.getInstance();

    public Period(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

        tasks = new HashMap<>();
    }

    public boolean isIn(long timestamp) {
        return timestamp >= startTime && timestamp < endTime;
    }

    public void distribute(MessageTree tree) {
//        serverStatisticManager.addMessageTotal(tree.getDomain(), 1);
//        boolean success = true;
//        String domain = tree.getDomain();
//
//        for (Map.Entry<String, List<PeriodTask>> entry : tasks.entrySet()) {
//            List<PeriodTask> tasks = entry.getValue();
//            int length = tasks.size();
//            int index = 0;
//            boolean manyTasks = length > 1;
//
//            if (manyTasks) {
//                index = Math.abs(domain.hashCode()) % length;
//            }
//            PeriodTask task = tasks.get(index);
//            boolean enqueue = task.enqueue(tree);
//
//            if (!enqueue) {
//                if (manyTasks) {
//                    task = tasks.get((index + 1) % length);
//                    enqueue = task.enqueue(tree);
//
//                    if (!enqueue) {
//                        success = false;
//                    }
//                } else {
//                    success = false;
//                }
//            }
//        }
//        if (!success && !tree.isProcessLoss()) {
//            serverStatisticManager.addMessageTotalLoss(tree.getDomain(), 1);
//            tree.setProcessLoss(true);
//        }
    }

    public void finish() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = new Date(startTime);
        Date endDate = new Date(endTime - 1);

        logger.info(String.format("Finishing %s tasks in period [%s, %s]", tasks.size(), df.format(startDate), df.format(endDate)));
        try {
            for (Map.Entry<String, List<PeriodTask>> task : tasks.entrySet()) {
                for (PeriodTask tmp : task.getValue()) {
                    tmp.finish();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            logger.info(String.format("Finished %s tasks in period [%s, %s]", tasks.size(), df.format(startDate)), df.format(endDate));
        }
    }
}