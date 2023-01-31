<#include "var.ftl"/><#t/>
<#-- ================================ -->
<#include "../Copyright.ftl"/>
package ${pojo.packageName};

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  * @Author: ${custom.author}
  * @Description: ${tableCommentVar}cache
  * @Company: ctg.cn
  * @Date: ${date}
  */
@Slf4j
@Component("d")
public class ${pojo.fileName} {
	
	/**
	 * 查询所有${tableCommentVar}数据缓存列表
	 * @param  
	 * @return
	 */
	@Cacheable(value = "${entity}", key = "searchAll")
    public List<${entity}> searchAll() {
	    return null ;
    }
 
}