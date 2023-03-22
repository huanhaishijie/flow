package com.sophony.flow.worker.common;

import lombok.extern.slf4j.Slf4j;

/**
 * FLowBannerPrinter
 *
 * @author yzm
 * @version 1.5.0
 * @description Banner
 * @date 2023/3/8 23:29
 */
@Slf4j
public final class FLowBannerPrinter {

    private static final String BANNER = "\n" +
            "___________.____    ________  __      __ \n" +
            "\\_   _____/|    |   \\_____  \\/  \\    /  \\\n" +
            " |    __)  |    |    /   |   \\   \\/\\/   /\n" +
            " |     \\   |    |___/    |    \\        / \n" +
            " \\___  /   |_______ \\_______  /\\__/\\  /  \n" +
            "     \\/            \\/       \\/      \\/   "+
            "\n" +
            "* author: yzm & yzm\n" +
            "* email: 320264603@qq.com\n" +
            "\n";

    public static void print() {
        log.info(BANNER);

        String version = "3.0.0";
        version = (version != null) ? " (v" + version + ")" : "";
        log.info(":: Flow Worker :: {}", version);
    }

}
