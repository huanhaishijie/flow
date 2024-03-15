package com.sophony.flow.api.respDto;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * ProcessRespDto
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 12:58
 */

@Data
public class ProcessRespDto{

    String id;

    /**
     * 流程模板id
     */
    private String actId;


    /**
     * 任务状态
     */
    private String state;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime startTime;


    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime endTime;


}
