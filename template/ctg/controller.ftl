<#include "var.ftl"/><#t/>
<#-- ================================ -->
<#include "../Copyright.ftl"/>
package ${pojo.packageName};

import java.util.List;
import java.util.Map;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.ctg.common.util.PageUtils;
import cn.ctg.common.response.ResponseData;
import cn.ctg.common.util.CtgUtils;
import lombok.extern.slf4j.Slf4j;
import ${entityClass};
import ${serviceClass};
import ${constantClass};

/**
  * @Author: ${custom.author}
  * @Description: ${tableCommentVar}Controller
  * @Company: ctg.cn
  * @Date: ${date}
  */
@RestController
@RequestMapping("${entityVar}")
@Slf4j
public class ${pojo.fileName} {

    @Autowired
    private ${service} ${serviceVar};
    
	@ApiOperation(value = "分页查询${tableCommentVar}列表", notes = "分页查询${tableCommentVar}列表")
    @PostMapping("pageList")
    public ResponseData<PageUtils> queryPage(@RequestBody Map<String, Object> param) {
        PageUtils pageUtils = ${serviceVar}.queryPage(param);
        return ResponseData.success(pageUtils);
    }
	
	
	/**
	 * 查询所有${tableCommentVar}列表
	 * @param  
	 * @return
	 */
	@ApiOperation(value = "查询所有${tableCommentVar}列表", notes = "查询所有${tableCommentVar}列表")
    @PostMapping("searchAll")
    public ResponseData<List<${entity}>> searchAll() {
		List<${entity}> ${entityVar}List = ${serviceVar}.list();
    	if(!CtgUtils.isCollectionNull(${entityVar}List)) {
        	return  ResponseData.success(${entityVar}List);
    	}else {
    		log.info(${constant}.NOT_EXIST);
    		return  ResponseData.success(${entityVar}List);
    	}
    }
	
	/**
	 * 保存${tableCommentVar}
	 * @param ${entityVar}
	 * @return
	 */
	@ApiOperation(value = "保存${tableCommentVar}", notes = "保存${tableCommentVar}")
    @PostMapping("save")
    public ResponseData<String> save(@RequestBody ${entity} ${entityVar}) {
    	boolean res = ${serviceVar}.save(${entityVar});
    	if(res) {
        	return ResponseData.success(${constant}.SAVE_SUCCESS);
    	}else {
    		log.error(${constant}.SAVE_FAILED);
    		return ResponseData.error(${constant}.SAVE_FAILED);
    	}
    }
	
	/**
	 * 删除${tableCommentVar}
	 * @param ${entityVar}
	 * @return
	 */
	@ApiOperation(value = "删除${tableCommentVar}", notes = "删除${tableCommentVar}")
    @PostMapping("delete")
    public ResponseData<String> delete(@RequestBody ${entity} ${entityVar}) {
    	boolean res = ${serviceVar}.removeById(${entityVar});
    	if(res) {
        	return ResponseData.success(${constant}.DELETE_SUCCESS);
    	}else {
    		log.error(${constant}.DELETE_FAILED);
    		return ResponseData.error(${constant}.DELETE_FAILED);
    	}
    }
	
	/**
	 * 根据主键ID更新${tableCommentVar}
	 * @param ${entityVar}
	 * @return
	 */
	@ApiOperation(value = "根据主键ID更新${tableCommentVar}", notes = "根据主键ID更新${tableCommentVar}")
    @PostMapping("update")
    public ResponseData<Boolean> update(@RequestBody ${entity} ${entityVar}) {
		boolean res = ${serviceVar}.updateById(${entityVar});
    	if(res) {
        	return  ResponseData.success(true);
    	}else {
    		log.error(${constant}.UPDATE_FAILED);
    		return  ResponseData.error(${constant}.UPDATE_FAILED);
    	}
    }
	
	/**
	 * 批量删除${tableCommentVar}
	 * @param ${entityVar}List
	 * @return
	 */
	@ApiOperation(value = "批量删除${tableCommentVar}", notes = "批量删除${tableCommentVar}")
    @PostMapping("deleteList")
    public ResponseData<String> deleteList(@RequestBody List<${entity}> ${entityVar}List) {
    	boolean res = ${serviceVar}.removeByIds(${entityVar}List);
    	if(res) {
        	return ResponseData.success(${constant}.DELETE_SUCCESS);
    	}else {
    		log.error(${constant}.DELETE_FAILED);
    		return ResponseData.error(${constant}.DELETE_FAILED);
    	}
    }

	/**
	* 根据主键ID查找${tableCommentVar}
	*/
	@ApiOperation(value = "根据主键ID查找${tableCommentVar}", notes = "根据主键ID查找${tableCommentVar}")
	@PostMapping("searchById")
	public ResponseData<${entity}> searchById (@RequestBody ${entity} ${entityVar}) {
		${entity} ${entityVar}Res = ${serviceVar}.getById(${entityVar}.getId());
		if (ObjectUtil.isNotEmpty(${entityVar}Res)) {
			return ResponseData.success(${entityVar}Res);
		}else {
			log.error(${constant}.QUERY_FAILED);
			return ResponseData.error(${constant}.QUERY_FAILED);
		}
	}
 
}