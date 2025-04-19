package com.lcx.campus.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lcx.campus.constant.UserConstants;
import com.lcx.campus.domain.Dept;
import com.lcx.campus.domain.Menu;
import com.lcx.campus.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类,给前端el-tree-select组件使用
 *
 * @author 刘传星
 * @since 2025-04-19
 */
@Data
public class TreeSelect implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 节点value
     */
    private Long value;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 节点禁用
     */
    private boolean disabled = false;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect() {
    }

    public TreeSelect(Dept dept) {
        this.value = dept.getDeptId();
        this.label = dept.getDeptName();
        this.disabled = StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus());
        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public TreeSelect(Menu menu) {
        this.value = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }
}
