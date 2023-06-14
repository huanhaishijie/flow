package com.sophony.flow.utils;

import com.sophony.flow.absEo.BaseMappingEO;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ParseSql
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/7 13:42
 */
public class ParseSql {
    private BaseMappingEO baseMappingEO;

    private String[] columns;

    private String[] cols;

    private Map<String, List<Method>> methodMap = new LinkedHashMap<>();


    private List<Object> args = new LinkedList<>();


    public <T extends  BaseMappingEO>ParseSql(T t){
        this.baseMappingEO = t;
    }

    private Field[] getFields(){
        Field[] fields1 = this.baseMappingEO.getClass().getDeclaredFields();
        Class<BaseMappingEO> baseMappingEOClass = BaseMappingEO.class;
        Field[] fields2 = baseMappingEOClass.getDeclaredFields();
        Field[] fields = new Field[fields1.length + fields2.length - 1];
        int i = 0;
        for(Field f : fields1){
            fields[i] = f;
            i ++;
        }
        for(Field f : fields2){
            fields[i] = f;
            if(f.getType() == ParseSql.class){
                continue;
            }
            i ++;
        }
        return  fields;
    }

    private String[] getColumns(){
        if(Objects.isNull(columns)){
            Field[] fields = getFields();
            cols  = new String[fields.length];
            columns  = new String[fields.length];
            for (int i = 0; i < fields.length; i++ ){
                Field f = fields[i];
                cols[i] = f.getName();
                columns[i] = getColum(cols[i]);
            }
            List<Method> methods = Arrays.asList(this.baseMappingEO.getClass().getMethods());
            Map<String, List<Method>> collect = methods.stream().collect(Collectors.groupingBy(method -> method.getName()));
            methodMap.putAll(collect);
        }

        return columns;
    }


    public String getQuerySql() {
        String[] columns1 = getColumns();

        String sql = "select ";
        for(int i = 0; i < columns1.length; i ++){
            sql +=  columns1[i] + " "+ cols[i] +",";
        }
        if(sql.endsWith(",")){
            sql = sql.substring(0, sql.length() -1);
        }
        sql += " From  " + baseMappingEO.getTableName();

        return sql;
    }


    public String getInsertSql() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        baseMappingEO.generateId();
        getColumns();
        String sql = "insert into "+baseMappingEO.getTableName() + " (";
        String columnsStr = "";
        String values = " values(";
        args.clear();
        for(int i = 0; i < cols.length; i++){
            String col = cols[i];
            String column = columns[i];
            Object value = getValue(col);
            if(Objects.isNull(value)){
                continue;
            }
            args.add(value);
            columnsStr +=  column + ",";
            values += "?,";
        }
        if(columnsStr.endsWith(",")){
            columnsStr = columnsStr.substring(0, columnsStr.length() -1);
        }
        if(values.endsWith(",")){
            values = values.substring(0, values.length() -1);
        }
        sql += columnsStr + ") "+ values + " );";
        return sql;

    }

    public String getUpdateSql() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        getColumns();
        String sql = "update "+baseMappingEO.getTableName() + " set ";
        args.clear();
        for(int i = 0; i < cols.length; i++){
            String col = cols[i];
            String column = columns[i];
            Object value = getValue(col);
            if(Objects.isNull(value)){
                continue;
            }
            if(col.equals("id")){
                continue;
            }
            args.add(value);
            sql += column +"" + " = ?,";
        }
        if(sql.endsWith(",")){
            sql = sql.substring(0, sql.length() -1);
        }
        return sql;
    }

    String getColum(String col){
        char[] chars = col.toCharArray();
        StringBuffer res = new StringBuffer("");
        for(char c : chars){
            if(!Character.isLetter(c)){
                res.append(c);
                continue;
            }
            if(Character.isLowerCase(c)){
                res.append(c);
            }else {
                res.append("_");
                res.append(c += 32);
            }
        }
        return res.toString();
    }

    public Object getValue(String col) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<? extends BaseMappingEO> aClass = baseMappingEO.getClass();
        Method method = aClass.getMethod("get" + col.substring(0, 1).toUpperCase() + col.substring(1));
        Object res = method.invoke(baseMappingEO);
        return res;
    }


    public <T extends BaseMappingEO> T mapRow(ResultSet rs, int rowNum) throws SQLException, InvocationTargetException, IllegalAccessException {
        getColumns();
        BaseMappingEO instance;
        try {
            instance = this.baseMappingEO.getClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i < cols.length; i++){
            assignment(instance, cols[i], columns[i], rs);
        }

        return (T) instance;
    }

    private <T extends BaseMappingEO> void assignment(T instance, String col, String column, ResultSet rs) throws SQLException, InvocationTargetException, IllegalAccessException {
        List<Method> methods = methodMap.get("set" + col.substring(0, 1).toUpperCase() + col.substring(1));
        if(methods == null || methods.size() == 0){
            return;
        }
        Method method = methods.get(0);
        Class<?> parameterType = method.getParameterTypes()[0];
        try {
            String key = column;
            try {
                rs.findColumn(column);
            }catch (Exception e){
                key = col;
            }


            if (parameterType.equals(Date.class) && Objects.nonNull(rs.getDate(key))) {
                method.invoke(instance, rs.getDate(key));
            }
            if (parameterType.equals(String.class) && Objects.nonNull(rs.getString(key))) {
                method.invoke(instance, rs.getString(key));
            }
            if (parameterType.equals(Integer.class) && Objects.nonNull(rs.getInt(key))) {
                method.invoke(instance, rs.getInt(key));
            }
            if (parameterType.equals(Long.class) && Objects.nonNull(rs.getLong(key))) {
                method.invoke(instance, rs.getLong(key));
            }
            if (parameterType.equals(BigDecimal.class) && Objects.nonNull(rs.getBigDecimal(key))) {
                method.invoke(instance, rs.getBigDecimal(key));
            }
            if (parameterType.equals(LocalDateTime.class) && Objects.nonNull(rs.getTimestamp(key))) {
                method.invoke(instance, rs.getTimestamp(key).toLocalDateTime());
            }
            if (parameterType.equals(Double.class) && Objects.nonNull(rs.getDouble(key))) {
                method.invoke(instance, rs.getDouble(key));
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("字段赋值错误");
        }

    }

    public List<Object> getArgs(){
        return args;
    }


}
