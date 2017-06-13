package ${basepackage};

import java.io.Serializable;
<#list imports as import>
import ${import};
</#list>

/**  
 * @description: ${description}
 * @author: ${author}
 * @email: ${email}
 * @time: ${time} 
 */
public class ${entity} implements Serializable{

	private static final long serialVersionUID = 1L;
	
	<#list properties as pro>
	/** ${pro.des} */
    private ${pro.proType} ${pro.proName};
    
	</#list>
	
    <#list properties as pro>
	public void set<@upperFc>${pro.proName}</@upperFc>(${pro.proType} ${pro.proName}){
		this.${pro.proName} = ${pro.proName};
	}
	
	public ${pro.proType} get<@upperFc>${pro.proName}</@upperFc>(){
		return this.${pro.proName};
	}
	
    </#list>
}