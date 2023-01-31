<#include "var.ftl"/><#t/>
<#-- ================================ -->
<#include "../Copyright.ftl"/>
package ${pojo.packageName};
  /**
	* @Author: ${custom.author}
	* @Description: ${tableCommentVar}Constant
	* @Company: ctg.cn
	* @Date: ${date}
	*/
 public class ${pojo.fileName} {
 
	public static String SUCCESS_CODE = "0";
	public static String FAILED_CODE = "-1";
	public static String QUERY_SUCCESS = "查询${tableCommentVar}成功";
	public static String QUERY_FAILED = "查询${tableCommentVar}失败";
	public static String QUERY_LIST_SUCCESS = "查询${tableCommentVar}列表成功";
	public static String QUERY_LIST_FAILED = "查询${tableCommentVar}列表失败";
	public static String QUERY_PAGE_SUCCESS = "分页查询${tableCommentVar}成功";
	public static String QUERY_PAGE_FAILED = "分页查询${tableCommentVar}失败";
	public static String QUERY_COUNT_SUCCESS = "查询数量成功";
	public static String QUERY_COUNT_FAILED = "查询数量失败";
	public static String SAVE_SUCCESS = "添加${tableCommentVar}成功";
	public static String SAVE_FAILED = "添加${tableCommentVar}失败";
	public static String UPDATE_SUCCESS = "修改${tableCommentVar}成功";
	public static String UPDATE_FAILED = "修改${tableCommentVar}失败";
	public static String DELETE_SUCCESS = "删除${tableCommentVar}成功";
	public static String DELETE_FAILED = "删除${tableCommentVar}失败";
	public static String NOT_EXIST = "该${tableCommentVar}不存在";

}