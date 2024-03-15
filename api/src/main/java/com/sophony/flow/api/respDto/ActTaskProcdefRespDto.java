package com.sophony.flow.api.respDto;


import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * ActTaskProcdefRespDto
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 20:12
 */
@Data
public class ActTaskProcdefRespDto {



    private String id;


    /**
     *流程模板名称
     */
    private String processfName;

    /**
     * 任务名称
     */
    private String taskName;


    /**
     * 任务编号
     */
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


    private List<ActTaskProcdefRespDto> backTaskDtos;

    /**
     * 流程模板id
     */
    private String processFid;


    /**
     * 上一级任务节点
     */
    private List<String> preTaskIds;


    /**
     * 下一级任务节点
     */
    private List<String> nextTaskIds;

    /**
     * 关联标签ids
     * tag_ids
     * @return
     */
    private List<String> tagIds;

    /**
     *执行中断tag
     */

    private List<String> interruptTag;


    public List<String> getTagIds() {
        if(CollectionUtils.isEmpty(tagIds)){
            return tagIds;
        }
        tagIds.sort(Comparator.reverseOrder());
        LinkedList<String> objects = new LinkedList<>();
        objects.addAll(tagIds);
        return objects;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public List<String> getInterruptTag() {
        if(CollectionUtils.isEmpty(interruptTag)){
            return interruptTag;
        }
        interruptTag.sort(Comparator.reverseOrder());
        LinkedList<String> objects = new LinkedList<>();
        objects.addAll(interruptTag);
        return objects;
    }

    public void setInterruptTag(List<String> interruptTag) {
        this.interruptTag = interruptTag;
    }

}
