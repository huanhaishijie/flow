package com.sophony.flow.api.reqDto;


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
 * @version 1.5.0
 * @description
 * @date 2023/3/9 19:54
 */
@Data
public class ActTaskProcdefReqDto {


    private String id;


    /**
     *流程模板名称
     */
    @NotBlank(message = "流程模板名称不能为空")
    private String processfName;

    /**
     * 任务名称
     */
    private String taskName;


    /**
     * 任务编号
     */
    @NotBlank(message = "任务编号不能为空")
    private String taskNo;

    /**
     * 任务排序
     */
    private Integer sort;

    /**
     * 任务备注
     */
    private String remark;


    /**
     * 退回节点，可以多节点
     */

    private List<String> backTasks;

    /**
     * 流程模板id
     */
    @NotBlank(message = "流程模板id不能为空")
    private String processFid;


    /**
     * 上一级任务节点
     */
    private List<String> preTaskIds;


//    /**
//     * 下一级任务节点
//     */
//    @ApiModelProperty(name = "nextTaskIds", value = "下一级任务节点")
//    private List<String> nextTaskIds;




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

    public List<String> getBackTasks(){
        if(CollectionUtils.isEmpty(backTasks)){
            return backTasks;
        }
        backTasks.sort(Comparator.reverseOrder());
        LinkedList<String> objects = new LinkedList<>();
        objects.addAll(backTasks);
        return objects;
    }
}
