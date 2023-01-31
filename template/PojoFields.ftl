<#-- ================================ -->
<#list allFields as f> 
    <#---->/**
     * <#if f.column.fullComment != ''>字段名称：${f.column.fullComment?html}</#if>
     * <#if f.column.comment != ''>${f.column.comment}</#if>:${f.column.name} ${f.column.dataTypeDesc?replace("\n", " ")}
     */<#---->
    <#if f.column.pkFlag>
    @TableId(value = "id" ,type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "ID")
    </#if>
    private ${f.dateType} ${f.name};
 
</#list>