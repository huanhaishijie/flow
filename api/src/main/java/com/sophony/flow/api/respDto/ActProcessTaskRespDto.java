package com.sophony.flow.api.respDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ActProcessTaskRespDto
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/22 17:30
 */

@Data
public class ActProcessTaskRespDto {

    String id;

    String taskNo;


    /**
     * 任务名称
     */
    private String taskName;



    /**
     * 状态 run 进行中 back 退回  withdraw 撤回  interrupt 中断（ 同组标签，有一个审核节点回退，其它节点中断， 中断节点需记录是因哪个节点审核回退而导致中断的）
     */
    private String state;


    /**
     * 审批人
     */
    private String auditUser;

    /**
     * 批注
     */
    private String content;


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
