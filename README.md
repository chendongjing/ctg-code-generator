# ctg-code-generator
微服务代码逆向生成工程。
## 该工程的意义
- 该工程的主要好处是解决开发人员编写重复的代码， 
- 强制开发人员使用规范的编程模式和代码注解，  
- 提高代码的可维护性和阅读性，
- 降低了代码的不规范性和因个人编程缺陷引起的不必要风险，  
- 提高代码质量和开发效率。  

## 使用说明
- 1、修改dbConfig.xml文件配置数据源信息。  
- 2、修改project.xml文件配置微服务信息。  
	2.0、修改name为对应数据库，支持mysql、oracle、postgresql  
	2.1、修改schema为据库名称  
	2.2、修改table为表名，多个表以逗号分隔  
	2.3、修改version版本号  
	2.4、修改author为自己的OA名称  
	2.5、修改service为服务名称  
	2.6、修改module为模块名称  
	2.7、修改rootPackage为根包名称  
	2.8、修改application为启动类名称  
	2.9、其他内容无需修改  
- 3、运行Run.java，生成代码，路径为out目录下 。  
