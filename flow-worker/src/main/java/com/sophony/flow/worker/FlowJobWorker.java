package com.sophony.flow.worker;

import com.sophony.flow.worker.common.FLowBannerPrinter;
import com.sophony.flow.worker.common.FlowWorkConfig;

/**
 * FlowWorker
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/8 23:29
 */

public class FlowJobWorker {

    protected FlowWorkConfig flowWorkConfig;

    public FlowJobWorker(FlowWorkConfig flowWorkConfig){
        this.flowWorkConfig = flowWorkConfig;
    }

    public void init() throws Exception{
        FLowBannerPrinter.print();
    }


}
