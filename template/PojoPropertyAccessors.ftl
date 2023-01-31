<#-- ================================ -->
<#list allFields as f><#if !especialFields?seq_contains(f.name)>

    <#--/**
     * <#if f.column.fullComment != ''>取得${f.column.comment?html}</#if>
     */-->
    <#if !f.column.pkFlag>
    public ${f.dateType} get${f.accessorName}() {
        return this.${f.name};
    }</#if>
<#if !f.column.pkFlag>

    <#--/**
     * <#if f.column.fullComment != ''>设置${f.column.comment?html}</#if>
     */-->
    public void set${f.accessorName}(${f.dateType} ${f.name}) {
        this.${f.name} = ${f.name};
    }
</#if></#if></#list>