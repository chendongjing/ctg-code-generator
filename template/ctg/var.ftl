
<#-- ==============system name================== -->
<#assign system = custom.system>
<#assign baseApplication = custom.application+'Application'>
<#assign rootPackage = custom.rootPackage>

<#-- ==============class name================== -->
<#assign entity = pojo.name>
<#assign entityClass = custom.entityPackage + '.' + pojo.name>
<#assign entityVar = pojo.name?uncap_first>

<#assign dto = pojo.name+'DTO'>
<#assign constantClass = custom.constantPackage + '.' + pojo.name + 'Constant'>
<#assign constant = pojo.name + 'Constant'>

<#assign dao = pojo.name + 'Mapper'>
<#assign daoClass = custom.daoPackage + '.' + dao>
<#assign daoVar = dao?uncap_first>

<#assign service = pojo.name + 'Service'>
<#assign serviceClass = custom.servicePackage + '.' + service>
<#assign serviceImplClass = custom.servicePackage + '.' + service + 'Impl'>
<#assign serviceVar = service?uncap_first>

<#assign tableCommentVar = pojo.table.tableComment?replace("表", "")>

<#-- ==============data================== -->
<#assign allFields = pojo.fields>
<#assign allFieldNames = pojo.fieldNames>
<#assign allColumnNames = pojo.table.columnNames>
<#assign primaryKeyFields = pojo.primaryKeyFields>
<#assign foreignKeyFields = pojo.foreignKeyFields>
<#assign nonPrimaryKeyFields = pojo.nonPrimaryKeyFields>
<#assign nonKeyFields = pojo.nonKeyFields>
<#assign includeClasses = pojo.pojoImpClasses>
<#assign pk = primaryKeyFields[0]>

<#assign especialFields = ["createTime","updateTime"]>

<#-- ==============path================== -->

<#nt/>