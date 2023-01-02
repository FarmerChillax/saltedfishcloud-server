package com.xiaotao.saltedfishcloud.model.po;

import com.xiaotao.saltedfishcloud.constant.ComponentType;
import com.xiaotao.saltedfishcloud.model.template.AuditModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class DesktopComponentConfig extends AuditModel {
    /**
     * 组件标题
     */
    private String title;

    /**
     * 组件名称
     */
    private String name;

    /**
     * 组件参数json
     */
    private String params;

    /**
     * 备注
     */
    private String remark;

    /**
     * 组件类型
     * @see ComponentType
     */
    private String type;

    /**
     * 显示顺序
     */
    private Integer showOrder;

    /**
     * 布局占用的单位宽度
     */
    private Integer width;

    /**
     * 布局占用的单位高度
     */
    private Integer height;
}
