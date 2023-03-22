package com.sophony.flow.api.respDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

@ApiModel(value = "ActProcessTaskRespDto", description = "流程任务审核节点实例")
@Data
public class ActProcessTaskRespDto {

    @ApiModelProperty(name = "id", value = "id")
    String id;

    @ApiModelProperty(name = "taskNo", value = "任务编号")
    String taskNo;


    /**
     * 任务名称
     */
    @ApiModelProperty(name = "taskName", value = "任务名称")
    private String taskName;



    /**
     * 状态 run 进行中 back 退回  withdraw 撤回  interrupt 中断（ 同组标签，有一个审核节点回退，其它节点中断， 中断节点需记录是因哪个节点审核回退而导致中断的）
     */
    @ApiModelProperty(name = "state", value = "状态")
    private String state;


    /**
     * 审批人
     */
    @ApiModelProperty(name = "auditUser", value = "审批人")
    private String auditUser;

    /**
     * 批注
     */
    @ApiModelProperty(name = "content", value = "批注")
    private String content;


    /**
     * 开始时间
     */
    @ApiModelProperty(name = "startTime", value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime startTime;


    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime", value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime endTime;


}
