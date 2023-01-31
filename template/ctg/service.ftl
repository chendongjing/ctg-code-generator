<#include "var.ftl"/><#t/>
<#-- ================================ -->
<#include "../Copyright.ftl"/>
package ${pojo.packageName};

import cn.ctg.common.util.PageUtils;
import cn.ctg.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${entityClass};
import ${daoClass};
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
  
/**
  * @Author: ${custom.author}
  * @Description: ${tableCommentVar}Service
  * @Company: ctg.cn
  * @Date: ${date}
  */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ${pojo.fileName} extends ServiceImpl<${dao}, ${pojo.name}>{

    @Autowired
    private ${dao} ${daoVar};

    /**
     * 翻页查询
     * @param param
     * @return
     */
    public PageUtils queryPage(Map<String, Object> param) {

        IPage<${entity}> page = new Query<${entity}>().getPage(param);
        QueryWrapper<${entity}> wrapper = new QueryWrapper<${entity}>();
        wrapper.eq("state", "0");
        IPage<${pojo.name}> iPage = ${daoVar}.selectPage(page, wrapper);
        return new PageUtils(iPage);

    }

    /**
     *查询所有
     * searchAll
     */
    public List<${entity}> searchAll() {
        QueryWrapper<${entity}> wrapper = new QueryWrapper<${entity}>();
        wrapper.eq("state", "0");
        wrapper.orderByAsc("sort");
        List<${entity}> list = ${daoVar}.selectList(wrapper);
        return list;
    }
	
}