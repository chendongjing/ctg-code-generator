<#include "var.ftl"/><#t/>
<#-- ================================ -->
<#include "../Copyright.ftl"/>
package ${rootPackage};

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
/**
  *服务启动类
  * @author: ${custom.author}
  * @Description:
  * @Company:ctg.cn
  * @date:${date}
  */
@SpringBootApplication(scanBasePackages = {"cn.ctg.common.redis","${custom.rootPackage}.*"})
@EnableEurekaClient
@EnableApolloConfig
@EnableFeignClients
@EnableDiscoveryClient
@EnableSwagger2
@EnableTransactionManagement 
@MapperScan("${custom.rootPackage}.dao")
public class ${baseApplication} {

	public static void main(String[] args) {
		SpringApplication.run(${baseApplication}.class,args);
	}

}