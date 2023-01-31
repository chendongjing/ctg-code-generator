<#include "var.ftl"/><#t/>
<#assign hasTimeDateType = 'false' />
<#list allFields as f><#if !especialFields?seq_contains(f.name)><#if f.dateType == 'Date'><#assign hasTimeDateType = 'true' /></#if></#if></#list>
<#-- ================================ -->
<#include "../Copyright.ftl"/>
package ${pojo.packageName};

<#if hasTimeDateType == 'true'>
</#if>
<#if hasTimeDateType == 'true'>
</#if>
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
  
 /**
   * 数据库表名称：${pojo.table.name} (${pojo.table.tableComment})
   * @Author: ${custom.author}
   * @Description:
   * @Company: ctg.cn
   * @Date: ${date}
   */
@Data
@EqualsAndHashCode()
@TableName(value="${pojo.table.name}",schema = "public")
public class ${pojo.name} implements Serializable{

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;

	<#include "../PojoFields.ftl"/>
	<#include "../PojoToString.ftl"/>
	<#include "../PojoEqualsHashcode.ftl"/>
}