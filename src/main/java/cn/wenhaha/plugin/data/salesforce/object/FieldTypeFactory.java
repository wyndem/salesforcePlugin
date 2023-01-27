package cn.wenhaha.plugin.data.salesforce.object;

import cn.wenhaha.datasource.FieldType;

/**
 * 字段
 * --------
 *
 * @author ：wyndem
 * @Date ：Created in 2022-07-20 22:37
 */
public class FieldTypeFactory {

    public static FieldType getType(String typeName){
        switch (typeName){
            case "string":
            case "textarea":
            case "reference":
                return  FieldType.String;
            case "int":  return  FieldType.Int;
            case "datetime":  return  FieldType.DateTime;
            case "date":  return  FieldType.Date;
            case "double":  return  FieldType.Double;
            case "location":  return  FieldType.Obj;
            default: return FieldType.Other;
        }
    }
}
