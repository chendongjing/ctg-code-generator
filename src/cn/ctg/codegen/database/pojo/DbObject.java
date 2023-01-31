package cn.ctg.codegen.database.pojo;

import cn.ctg.codegen.util.Utils;

public class DbObject {
	private String catalog;
	private String schema = "";
	private String name = "";
	private String comment = "";
	private String fullComment = "";

	public DbObject() {
	}

	public DbObject(String catalog, String schema, String name) {
		this.setCatalog(catalog);
		this.setSchema(schema);
		this.setName(name);
	}

	public String getCatalog() {
		return this.catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return this.schema;
	}

	public void setSchema(String schema) {
		this.schema = Utils.strip(schema);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = Utils.strip(name);
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = Utils.strip(comment);
	}

	public String getFullComment() {
		return this.fullComment;
	}

	public void setFullComment(String fullComment) {
		this.fullComment = Utils.strip(fullComment);
		String[] start = new String[] { "(", "（", ";" };
		int startPos = -1;
		String[] var4 = start;
		int var5 = start.length;

		for (int var6 = 0; var6 < var5; ++var6) {
			String str = var4[var6];
			if ((startPos = this.fullComment.indexOf(str)) != -1) {
				break;
			}
		}

		if (startPos != -1) {
			this.setComment(this.fullComment.substring(0, startPos));
		} else {
			this.setComment(this.fullComment);
		}

	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n=========================================================\n");
		sb.append("  catalog:" + this.catalog);
		sb.append("\n  schema:" + this.schema);
		sb.append("\n  name:" + this.name);
		return sb.toString();
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (o == this) {
			return true;
		} else if (!(o instanceof DbObject)) {
			return false;
		} else {
			DbObject o2 = (DbObject) o;
			return o2.getSchema().equals(this.schema) && o2.getName().equals(this.name);
		}
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.schema.hashCode();
		result = 37 * result + this.name.hashCode();
		return result;
	}
}