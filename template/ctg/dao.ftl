<#include "var.ftl"/><#t/>
<#assign hasTimeDateType = 'false' />
<#-- ================================ -->
<#include "../Copyright.ftl"/>
package ${pojo.packageName};
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${entityClass};

/**
  * @Author: ${custom.author}
  * @Description: ${tableCommentVar}Mapper
  * @Company: ctg.cn
  * @Date: ${date}
  */
@Repository
public interface ${dao} extends BaseMapper<${pojo.name}>{

}