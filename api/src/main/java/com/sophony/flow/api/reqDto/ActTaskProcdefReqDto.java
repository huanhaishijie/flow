package com.sophony.flow.api.reqDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * ActTaskProcdefReqDto
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/9 19:54
 */
@ApiModel(value = "ActTaskProcdefReqDto", description = "任务模板Dto")
@Data
public class ActTaskProcdefReqDto {


    private String id;


    /**
     *流程模板名称
     */
    @ApiModelProperty(name = "processfName", value = "流程模板名称")
    private String processfName;

    /**
     * 任务名称
     */
    @ApiModelProperty(name = "taskName", value = "任务名称")
    private String taskName;


    /**
     * 任务编号
     */
    @ApiModelProperty(name = "taskNo", value = "任务编号")
    @NotBlank(message = "任务编号不能为空")
    private String taskNo;

    /**
     * 任务排序
     */
    @ApiModelProperty(name = "sort", value = "任务排序")
    private Integer sort;

    /**
     * 任务备注
     */
    @ApiModelProperty(name = "remark", value = "任务备注")
    private String remark;


    /**
     * 退回节点，可以多节点
     */

    @ApiModelProperty(name = "backTasks", value = "backTasks")
    private List<String> backTasks;

    /**
     * 流程模板id
     */
    @ApiModelProperty(name = "processFid", value = "流程模板id")
    private String processFid;


    /**
     * 上一级任务节点
     */
    @ApiModelProperty(name = "preTaskIds", value = "上一级任务节点")
    private List<String> preTaskIds;


//    /**
//     * 下一级任务节点
//     */
//    @ApiModelProperty(name = "nextTaskIds", value = "下一级任务节点")
//    private List<String> nextTaskIds;



    @ApiModelProperty(name = "cond", value = "条件")
    private String cond;



//
//    public List<String> getNextTaskIds() {
//        if(CollectionUtils.isEmpty(nextTaskIds)){
//            return nextTaskIds;
//        }
//        nextTaskIds.sort(Comparator.reverseOrder());
//        LinkedList<String> objects = new LinkedList<>();
//        objects.addAll(nextTaskIds);
//        return objects;
//    }
//
//    public void setNextTaskIds(List<String> nextTaskIds) {
//        this.nextTaskIds = nextTaskIds;
//    }

    public List<String> getPreTaskIds() {
        if(CollectionUtils.isEmpty(preTaskIds)){
            return preTaskIds;
        }
        preTaskIds.sort(Comparator.reverseOrder());
        LinkedList<String> objects = new LinkedList<>();
        objects.addAll(preTaskIds);
        return objects;
    }

    public void setPreTaskIds(List<String> preTaskIds) {
        this.preTaskIds = preTaskIds;
    }
}
