package cn.ctg.codegen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import cn.ctg.codegen.export.ExportType;
import cn.ctg.codegen.export.data.TemplateInfo;

public class Run extends BaseRun {
	
	public void init() {
		this.setExportType(ExportType.DB);
		this.app.getTablesMap();
		List<TemplateInfo> templateInfos = this.app.getTemplateInfos();
		if (templateInfos != null && templateInfos.size() > 0) {
			List<Object> datas = new ArrayList<Object> ();
			Iterator<TemplateInfo> var3 = templateInfos.iterator();
			while (var3.hasNext()) {
				TemplateInfo ti = (TemplateInfo) var3.next();
				if (ti != null) {
					datas.add(ti);
				}
			}
			this.setDatas(datas);
		}
	}

	public static void main(String[] args) {
		Run dr = new Run();
		dr.start();
	}
}