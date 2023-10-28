package com.zenith.spzx.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zenith.spzx.model.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Schema(description = "系统菜单实体类")
@Data
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

	@Schema(description = "父节点id")
	private Long parentId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SysMenu)) return false;
		return this.parentId.equals(((SysMenu) o).getParentId());
	}


	@Schema(description = "节点标题")
	private String title;

	@Schema(description = "组件名称")
	private String component;

	@Schema(description = "排序值")
	private Integer sortValue;

	@Schema(description = "状态(0:禁止,1:正常)")
	private Integer status;

	// 下级列表
	@Schema(description = "子节点")
	@TableField(exist = false)
	private List<SysMenu> children;

}
