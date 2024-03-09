/*
 
 Copyright 2015 HJOW

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 
 */

package hjow.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import hjow.common.json.JsonObject;

/**
 * <p>데이터 타입 변환에 관련된 여러 정적 메소드들이 있는 클래스입니다.</p>
 * 
 * @author HJOW
 *
 */
public class DataUtil
{
    /**
     * <p>객체가 비었는지 여부를 반환합니다. null 이면 true 가 반환되고, 빈 텍스트인 경우도 true 가 반환됩니다.</p>
     * 
     * @param ob : 검사할 텍스트
     * @return 비었는지의 여부
     */
    public static boolean isEmpty(Object ob)
    {
        if(ob == null) return true;
        if(ob instanceof String)
        {
            if(((String) ob).trim().equals("")) return true;
            else if(((String) ob).trim().equals("null")) return true;
            else return false;
        }
        else if(ob instanceof List<?>)
        {
            return ((List<?>) ob).isEmpty();
        }
        else if(ob instanceof Map<?, ?>)
        {
            return ((Map<?, ?>) ob).isEmpty();
        }
        else if(String.valueOf(ob).trim().equals("null")) return true;
        else if(String.valueOf(ob).trim().equals("")) return true;
        else return isEmpty(String.valueOf(ob));
    }
    
    /**
     * <p>객체들이 모두 비어 있는지 여부를 반환합니다. 모두 빈 객체여야 true 가 반환됩니다.</p>
     * 
     * @param obs : 객체들
     * @return 비어 있는지 여부
     */
    public static boolean isEmpties(Object ... obs)
    {
        for(Object o : obs)
        {
            if(isNotEmpty(o))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * <p>객체가 비었는지 여부를 반환합니다. null 이면 false 가 반환되고, 빈 텍스트인 경우도 false 가 반환됩니다.</p>
     * 
     * @param ob : 검사할 텍스트
     * @return 비었는지의 여부
     */
    public static boolean isNotEmpty(Object ob)
    {
        return ! isEmpty(ob);
    }
    
    /**
     * <p>객체들이 모두 비어 있지 않은지 여부를 반환합니다. 모두 비어 있지 않은 객체여야 true 가 반환됩니다.</p>
     * 
     * @param obs : 객체들
     * @return 비어 있지 않은지 여부
     */
    public static boolean isNotEmpties(Object ... obs)
    {
        for(Object o : obs)
        {
            if(isEmpty(o))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * <p>어떤 객체가 정수 값으로 변환이 가능한지 여부를 반환합니다.</p>
     * 
     * @param ob : 검사할 객체
     * @return : 정수 변환 가능 여부
     */
    public static boolean isInteger(Object ob)
    {
        if(ob instanceof BigInteger) return true;
        if(ob instanceof Integer) return true;
        if(ob instanceof Long) return true;
        if(ob instanceof String)
        {
            try
            {
                Long.parseLong((String) ob);
                return true;
            }
            catch(NumberFormatException e)
            {
                return false;
            }
        }
        else return false;
    }
    
    /**
     * <p>어떤 객체가 실수 값으로 변환이 가능한지 여부를 반환합니다.</p>
     * 
     * @param ob : 검사할 객체
     * @return : 실수 변환 가능 여부
     */
    public static boolean isFloat(Object ob)
    {
        if(ob instanceof BigDecimal) return true;
        if(ob instanceof Float) return true;
        if(ob instanceof Double) return true;
        if(ob instanceof String)
        {
            try
            {
                Double.parseDouble((String) ob);
                return true;
            }
            catch(NumberFormatException e)
            {
                return false;
            }
        }
        else return false;
    }
    
    /**
     * <p>어떤 객체가 정수, 혹은 실수 값인지 여부를 반환합니다.</p>
     * 
     * @param ob : 검사할 객체
     * @return 숫자 데이터 여부
     */
    public static boolean isNumber(Object ob)
    {
        return isNumber(ob) || isFloat(ob);
    }
    
    
    /**
     * <p>객체를 boolean 타입으로 반환합니다. 사용자가 y/n 입력을 한 경우 그에 해당하는 true, false 논리값으로 변환하는 데 사용됩니다.</p>
     * 
     * @param ob : 객체
     * @return 논리값
     * @throws InvalidInputException : 논리값과 전혀 상관이 없는 객체가 매개변수로 들어온 문제
     */
    public static boolean parseBoolean(Object ob)
    {
        if(ob == null) return false;
        if(isEmpty(ob)) return false;
        if(ob instanceof String)
        {
            String target = ((String) ob).trim();
            if(target.equalsIgnoreCase("t") || target.equalsIgnoreCase("true")
                    || target.equalsIgnoreCase("y") || target.equalsIgnoreCase("yes")) return true;
            else return false;
        }
        else if(ob instanceof Integer)
        {
            if(((Integer) ob).intValue() == 0) return false;
            else return true;
        }
        else if(ob instanceof BigInteger)
        {
            if(((Integer) ob).intValue() == 0) return false;
            else return true;
        }
        else if(ob instanceof Boolean)
        {
            return ((Boolean) ob).booleanValue();
        }
        throw new RuntimeException(String.valueOf(ob));
    }
    
    /**
     * <p>배열을 리스트 객체로 변환합니다.</p>
     * 
     * @param newList : 리스트 객체, 주로 새 객체를 만들어 넣습니다. 이전에 쓰던 객체를 넣으면 배열 내용이 객체에 추가됩니다.
     * @return 리스트 객체
     */
    public static <T> List<T> arrayToList(T[] obj, List<T> newList)
    {
        if(obj == null) return null;
        List<T> lists = newList;
        for(int i=0; i<obj.length; i++)
        {
            lists.add(obj[i]);
        }
        return lists;
    }
    
    /**
     * <p>리스트 둘을 병합한 새 리스트를 반환합니다. 이 과정에서 둘 사이에 중복 원소가 있는지 검사합니다.</p>
     * 
     * @param one : 리스트
     * @param two : 또 다른 리스트
     * @return 병합된 리스트
     */
    private static <T> List<T> mergeList(List<T> one, List<T> two)
    {
        if(one == null) return two;
        if(two == null) return one;
        if(one.size() <= 0) return two;
        if(two.size() <= 0) return one;
        
        List<T> newList = new Vector<T>();
        
        boolean isEquals = true;
        for(int i=0; i<one.size(); i++)
        {
            newList.add(one.get(i));
        }
        for(int i=0; i<two.size(); i++)
        {
            isEquals = true;
            for(int j=0; j<newList.size(); j++)
            {
                if(! (two.get(i).equals(newList.get(j))))
                {
                    isEquals = false;
                }
            }
            if(! isEquals)
            {
                newList.add(two.get(i));
            }
        }
        return newList;
    }
    
    /**
     * <p>여러 리스트들을 병합한 새 리스트를 반환합니다. 이 과정에서 둘 사이에 중복 원소가 있는지 검사합니다.</p>
     * <p>비교 과정에서 equals(others) 메소드를 사용하기 때문에, 비교해야 할 객체들은 이 메소드를 가지고 있어야 합니다.</p>
     * 
     * @param lists : 리스트들
     * @return 병합된 새 리스트
     */
	public static <T> List<T> merge(List<T> ... lists)
    {
        List<List<T>> targets = new Vector<List<T>>();
        for(List<T> othersList : lists)
        {
            targets.add(othersList);
        }
        
        List<T> results = new Vector<T>();
        for(int i=0; i<targets.size(); i++)
        {
            results = mergeList(results, targets.get(i));
        }
        
        return results;
    }
    
    /**
     * <p>따옴표 앞에 \ 기호를 붙입니다. (캐스팅)</p>
     * 
     * @param isDoubleQuote : 이 값이 true 이면 쌍따옴표 앞에만 \ 기호를 붙이고, 그 외에는 일반 따옴표 앞에만 \ 기호를 붙입니다.
     * @param target : 대상 텍스트
     * @return \ 처리된 텍스트
     */
    public static String castQuote(boolean isDoubleQuote, String target)
    {
    	String res = target;
    	// res = res.replace("\n", "\\" + "n");
    	// res = res.replace("\t", "\\" + "t");
    	res = res.replace("\\", "\\" + "\\");
        if(isDoubleQuote)
        {
        	res = res.replace("\"", "\\" + "\"");
        }
        else
        {
            res = res.replace("'", "\'");
        }
        return res;
    }
    
    /**
     * <p>\ 처리된 따옴표들을 찾아 원래의 따옴표로 돌려 놓습니다. (캐스팅 해제)</p>
     * 
     * @param isDoubleQuote : 이 값이 true 이면 \ 처리된 쌍따옴표들을 찾아 원래의 쌍따옴표로 바꾸고, false 이면 일반 따옴표들을 찾아 원래의 따옴표로 바꿉니다.
     * @param target : 대상 텍스트
     * @return \ 가 제거된 텍스트
     */
    public static String reCastQuote(boolean isDoubleQuote, String target)
    {
        char[] chars = target.toCharArray();
        StringBuffer results = new StringBuffer("");
        boolean useDefault = true;
        for(int i=0; i<chars.length; i++)
        {
            useDefault = true;
            if(isDoubleQuote)
            {
                if(i >= 3)
                {
                    useDefault = false;
                    if((chars[i - 2] == '\\') && chars[i - 1] == '\\' && chars[i] == '"')
                    {
                        results = results.append(String.valueOf('"'));
                    }
                    else if((chars[i - 2] != '\\') && chars[i - 1] == '\\' && chars[i] == '"')
                    {
                        results = results.append(String.valueOf('"'));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == '\\')
                    {
                        results = results.append(String.valueOf('\\'));
                    }
                    else if(chars[i] == '\\')
                    {
                        continue;
                    }
                    else
                    {
                    	if(chars[i] != '\\' && chars[i - 1] == '\\') results = results.append(String.valueOf('\\'));
                        useDefault = true;
                    }
                }
                else if(i >= 2)
                {
                    useDefault = false;
                    if(chars[i - 1] == '\\' && chars[i] == '"')
                    {
                        results = results.append(String.valueOf('"'));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == '\\')
                    {
                        results = results.append(String.valueOf('\\'));
                    }
                    else if(chars[i] == '\\')
                    {
                        continue;
                    }
                    else
                    {
                    	if(chars[i] != '\\' && chars[i - 1] == '\\') results = results.append(String.valueOf('\\'));
                    	useDefault = true;
                    }
                }
                else
                {
                    useDefault = true;
                }
            }
            else
            {
                if(i >= 3)
                {
                    useDefault = false;
                    if((chars[i - 2] == '\\') && chars[i - 1] == '\\' && chars[i] == '\'')
                    {
                        results = results.append(String.valueOf('\''));
                    }
                    else if((chars[i - 2] != '\\') && chars[i - 1] == '\\' && chars[i] == '\'')
                    {
                        results = results.append(String.valueOf('\''));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == '\\')
                    {
                        results = results.append(String.valueOf('\\'));
                    }
                    else if(chars[i] == '\\')
                    {
                        continue;
                    }
                    else
                    {
                    	if(chars[i] != '\\' && chars[i - 1] == '\\') results = results.append(String.valueOf('\\'));
                        useDefault = true;
                    }
                }
                else if(i >= 2)
                {
                    useDefault = false;
                    if(chars[i - 1] == '\\' && chars[i] == '\'')
                    {
                        results = results.append(String.valueOf('\''));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == '\\')
                    {
                        results = results.append(String.valueOf('\\'));
                    }
                    else if(chars[i] == '\\')
                    {
                        continue;
                    }
                    else
                    {
                    	if(chars[i] != '\\' && chars[i - 1] == '\\') results = results.append(String.valueOf('\\'));
                    	useDefault = true;
                    }
                }
                else
                {
                    useDefault = true;
                }
            }
            
            if(useDefault)
            {
                results = results.append(chars[i]);
            }
        }
        
        return results.toString();
    }
    
    /**
     * <p>텍스트 앞뒤에 따옴표를 붙입니다. 그 안의 내용은 캐스팅 처리합니다.</p>
     * 
     * @param contents : 대상 텍스트
     * @param needDouble : true 시 쌍따옴표 사용
     * @return 따옴표로 포장된 텍스트
     */
    public static String putQuote(String contents, boolean needDouble)
    {
        if(needDouble) return "\"" + castQuote(true, contents) + "\"";
        else return "'" + castQuote(false, contents) + "'";
    }
    
    /**
     * <p>텍스트 앞뒤에서 따옴표를 제거하고, 그 안의 내용을 캐스팅 해제합니다. 대상 텍스트가 따옴표로 시작하고 끝나야만 적용이 가능합니다.</p>
     * 
     * @param contents : 대상 텍스트
     * @return 따옴표 제거된 원본 텍스트
     */
    public static String removeQuote(String contents)
    {
        String target = contents.trim();
        if(target.startsWith("\"") && target.endsWith("\""))
        {
            target = target.substring(1, target.length() - 1);
            target = reCastQuote(true, target);
        }
        else if(target.startsWith("'") && target.endsWith("'"))
        {
            target = target.substring(1, target.length() - 1);
            target = reCastQuote(false, target);
        }
        return target;
    }
    
    /**
     * <p>줄 띄기, 탭, 따옴표 앞에 \를 붙입니다. 여러 줄 텍스트는 한 줄로 변환되고, 줄 띄는 자리에는 대신 \n이, 탭 자리에는 \t가 오게 됩니다.</p>
     * 
     * @param isDoubleQuote : 이 값이 true 이면 쌍따옴표 앞에만 \ 기호를 붙이고, 그 외에는 일반 따옴표 앞에만 \ 기호를 붙입니다.
     * @param target : 대상 텍스트
     * @return \ 처리된 텍스트
     */
    public static String castTotal(boolean isDoubleQuote, String target)
    {    
        return castQuote(isDoubleQuote, target.replace("\n", "\\n").replace("\t", "\\t"));
    }
    
    /**
     * <p>\n, \t, \" 를 찾아 \ 기호를 제거합니다. \n은 줄 띄기 기호로 변환되고, \t는 탭 공백으로 변환됩니다.</p>
     * 
     * @param isDoubleQuote : 이 값이 true 이면 \ 처리된 쌍따옴표들을 찾아 원래의 쌍따옴표로 바꾸고, false 이면 일반 따옴표들을 찾아 원래의 따옴표로 바꿉니다.
     * @param target : 대상 텍스트
     * @return \ 가 제거된 텍스트
     */
    public static String reCastTotal(boolean isDoubleQuote, String target)
    {
        // return reCastQuote(isDoubleQuote, target.replace("\\n", "\n").replace("\\t", "\t"));
        
        char[] chars = target.toCharArray();
        StringBuffer results = new StringBuffer("");
        boolean useDefault = true;
        for(int i=0; i<chars.length; i++)
        {
            useDefault = true;
            if(isDoubleQuote)
            {
                if(i >= 3)
                {
                    useDefault = false;
                    if((chars[i - 2] == '\\') && chars[i - 1] == '\\' && chars[i] == '"')
                    {
                        results = results.append(String.valueOf('"'));
                    }
                    else if((chars[i - 2] != '\\') && chars[i - 1] == '\\' && chars[i] == '"')
                    {
                        results = results.append(String.valueOf('"'));
                    }
                    else if((chars[i - 2] == '\\') && chars[i - 1] == '\\' && chars[i] == 'n')
                    {
                        results = results.append(String.valueOf('n'));
                    }
                    else if((chars[i - 2] != '\\') && chars[i - 1] == '\\' && chars[i] == 'n')
                    {
                        results = results.append(String.valueOf('\n'));
                    }
                    else if((chars[i - 2] == '\\') && chars[i - 1] == '\\' && chars[i] == 't')
                    {
                        results = results.append(String.valueOf('t'));
                    }
                    else if((chars[i - 2] != '\\') && chars[i - 1] == '\\' && chars[i] == 't')
                    {
                        results = results.append(String.valueOf('\t'));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == '\\')
                    {
                        results = results.append(String.valueOf('\\'));
                    }
                    else if(chars[i] == '\\')
                    {
                        continue;
                    }
                    else
                    {
                        useDefault = true;
                    }
                }
                else if(i >= 2)
                {
                    useDefault = false;
                    if(chars[i - 1] == '\\' && chars[i] == '"')
                    {
                        results = results.append(String.valueOf('"'));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == '\\')
                    {
                        results = results.append(String.valueOf('\\'));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == 'n')
                    {
                        results = results.append(String.valueOf('\n'));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == 't')
                    {
                        results = results.append(String.valueOf('\t'));
                    }
                    else if(chars[i] == '\\')
                    {
                        continue;
                    }
                    else useDefault = true;
                }
                else
                {
                    useDefault = true;
                }
            }
            else
            {
                if(i >= 3)
                {
                    useDefault = false;
                    if((chars[i - 2] == '\\') && chars[i - 1] == '\\' && chars[i] == '\'')
                    {
                        results = results.append(String.valueOf("\\'"));
                    }
                    else if((chars[i - 2] != '\\') && chars[i - 1] == '\\' && chars[i] == '\'')
                    {
                        results = results.append(String.valueOf('\''));
                    }
                    else if((chars[i - 2] == '\\') && chars[i - 1] == '\\' && chars[i] == 'n')
                    {
                        results = results.append(String.valueOf('n'));
                    }
                    else if((chars[i - 2] != '\\') && chars[i - 1] == '\\' && chars[i] == 'n')
                    {
                        results = results.append(String.valueOf('\n'));
                    }
                    else if((chars[i - 2] == '\\') && chars[i - 1] == '\\' && chars[i] == 't')
                    {
                        results = results.append(String.valueOf('t'));
                    }
                    else if((chars[i - 2] != '\\') && chars[i - 1] == '\\' && chars[i] == 't')
                    {
                        results = results.append(String.valueOf('\t'));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == '\\')
                    {
                        results = results.append(String.valueOf('\\'));
                    }
                    else if(chars[i] == '\\')
                    {
                        continue;
                    }
                    else
                    {
                        useDefault = true;
                    }
                }
                else if(i >= 2)
                {
                    useDefault = false;
                    if(chars[i - 1] == '\\' && chars[i] == '\'')
                    {
                        results = results.append(String.valueOf('\''));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == '\\')
                    {
                        results = results.append(String.valueOf('\\'));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == 'n')
                    {
                        results = results.append(String.valueOf('\n'));
                    }
                    else if(chars[i - 1] == '\\' && chars[i] == 't')
                    {
                        results = results.append(String.valueOf('\t'));
                    }
                    else if(chars[i] == '\\')
                    {
                        continue;
                    }
                    else useDefault = true;
                }
                else
                {
                    useDefault = true;
                }
            }
            
            if(useDefault)
            {
                results = results.append(chars[i]);
            }
        }
        return results.toString();
    }
    
    /**
     * <p>예외, 혹은 오류 객체의 내용을 텍스트로 반환합니다. 자바의 스택 추적 형식을 따릅니다.</p>
     * 
     * @see DataUtil.traceException
     * @param t : 예외, 혹은 오류 객체
     * @return 텍스트 내용
     */
    public static String stackTrace(Throwable t)
    {
        StringBuffer results = new StringBuffer("");        
        results = results.append(t.getClass().getName() + ": " + t.getMessage() + "\n");        
        StackTraceElement[] traces = t.getStackTrace();
        for(StackTraceElement e : traces)
        {
            results = results.append("\tat " +  String.valueOf(e) + "\n");
        }        
        return results.toString();
    }
    
    /**
     * 예외 객체의 스택 트레이스 메시지를 문자열로 반환합니다. 자바의 기본 스택 트레이스 출력을 그대로 문자열로 반환합니다.
     * 
     * @see DataUtil.stackTrace
     */
    public static String traceException(Throwable t) {
        StringWriter fakeWriter = new StringWriter();
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fakeWriter);
            t.printStackTrace(writer);
            writer.close();
            return fakeWriter.toString();
        } catch(Throwable ignores) {  } finally {
            try { writer.close(); } catch(Throwable ignoreTwo) {}
        }
        return null;
    }

    /**
     * <p>텍스트에서 유니코드 텍스트 파일임을 나타내는 특수 기호를 제거한 텍스트를 반환합니다.</p>
     * 
     * @param target : 원래의 텍스트
     * @return 유니코드 특수 기호가 제거된 텍스트
     */
    public static String remove65279(String target)
    {
        char[] targetChar = target.toCharArray();
        
        List<Character> resultChars = new Vector<Character>();
        for(int i=0; i<targetChar.length; i++)
        {
            if(((int) targetChar[i]) != 65279) resultChars.add(new Character(targetChar[i]));
        }
        
        char[] newChar = new char[resultChars.size()];
        for(int i=0; i<newChar.length; i++)
        {
            newChar[i] = resultChars.get(i);
        }
        return new String(newChar);
    }
    
    /**
     * <p>데이터 용량 단위를 텍스트로 변환합니다.</p>
     * 
     * @param values : 데이트 용량 값 (byte 단위)
     * @return 단위 적용한 텍스트
     */
    public static String toByteUnit(long values)
    {
        return toByteUnit(values, 3);
    }
    
    
    /**
     * <p>데이터 용량 단위를 텍스트로 변환합니다.</p>
     * 
     * @param values : 데이트 용량 값 (byte 단위)
     * @return 단위 적용한 텍스트
     */
    public static String toByteUnit(long values, int digit)
    {
    	if(digit <= 0) throw new RuntimeException("On 'toByteUnit', digit must bigger  than 1.");
    	if(digit >= 7) throw new RuntimeException("On 'toByteUnit', digit must smaller than 6.");
    	
        double calcs = 0.0;
        
        if(values == 0) return "0";
        if(values == 1) return "1 byte";
        if(values < 1024) return String.valueOf(values) + " bytes";
        
        String formats = "%." + digit + "f";
        
        calcs = values / 1024.0;
        if(calcs < 1024.0) return String.format(formats + " KB", calcs);
        
        calcs = calcs / 1024.0;
        if(calcs < 1024.0) return String.format(formats + " MB", calcs);
        
        calcs = calcs / 1024.0;
        if(calcs < 1024.0) return String.format(formats + " GB", calcs);
        
        calcs = calcs / 1024.0;
        if(calcs < 1024.0) return String.format(formats + " TB", calcs);
        
        calcs = calcs / 1024.0;
        if(calcs < 1024.0) return String.format(formats + " PB", calcs);
        
        calcs = calcs / 1024.0;
        if(calcs < 1024.0) return String.format(formats + " EB", calcs);
        
        calcs = calcs / 1024.0;
        if(calcs < 1024.0) return String.format(formats + " ZB", calcs);
        
        calcs = calcs / 1024.0;
        if(calcs < 1024.0) return String.format(formats + " YB", calcs);
        
        return String.format(formats + " YB", calcs);
    }
    
    /**
     * <p>바이트 배열 내용을 0으로 채웁니다.</p>
     * 
     * @param bytes : 바이트 배열
     */
    public static void emptyByteArray(byte[] bytes)
    {
        for(int i=0; i<bytes.length; i++)
        {
            bytes[i] = 0;
        }
    }
    
    /**
     * <p>오늘 날짜에 대한 Date 객체를 반환합니다.</p>
     * 
     * @return 날짜 객체
     */
    public static Date currentDate()
    {
        Date date = new Date(System.currentTimeMillis());
        return date;
    }
    
    /**
     * <p>두 날짜 객체에 대한 차이를 구합니다.</p>
     * 
     * @param one : 날짜 객체
     * @param two : 다른 날짜 객체
     * @return 날짜 뺄셈 결과
     */
    public static Date subtract(Date one, Date two)
    {        
        long oneLong = one.getTime();
        long twoLong = two.getTime();
        
        if(oneLong > twoLong) return new Date(oneLong - twoLong);
        else return new Date(twoLong - oneLong);
    }
    
    /**
     * <p>어떤 텍스트에 특정 텍스트가 몇 번 포함되어 있는지를 반환합니다. finds 가 빈 칸이면 -1 을 반환합니다.</p>
     * 
     * @param text : 텍스트
     * @param finds : 패턴
     * @return 패턴 포함 횟수
     */
    public static int has(String text, String finds)
    {
        int results = 0;
        String target = text;
        
        if(finds.equals("")) return -1;
        
        while(text.indexOf(finds) >= 0)
        {
            results = results + 1;
            target = target.replaceFirst(finds, "");
        }
        
        return results;
    }
    
    /**
     * <p>2분의 1 제곱 값을 구합니다.</p>
     * 
     * @param original : 원래의 값
     * @param scale : 소수 자리수
     * @return 2분의 1 제곱
     */
    public static BigDecimal sqrt(BigDecimal original, int scale)
    {
        BigDecimal temp = new BigDecimal(String.valueOf(original));
        
        BigDecimal results = new BigDecimal("1.0");
        results.setScale(scale + 2);
        
        int loops = 0;
        
        while(true)
        {
            if(loops >= 1)
            {
                temp = new BigDecimal(String.valueOf(results));
            }
            
            temp.setScale(scale + 2, BigDecimal.ROUND_FLOOR);
            results = original.divide(temp, scale + 2, BigDecimal.ROUND_FLOOR).add(temp).divide(new BigDecimal("2.0"), scale + 2, BigDecimal.ROUND_FLOOR);
            if(temp.equals(results)) break;
            
            loops++;
        }
        
        return results.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * XML 형식의 문자열에서 Properties 데이터를 추출합니다.
     */
    public static Properties fromXML(String xmlContents) {
        Properties prop = new Properties();
        
        try {
            ByteArrayInputStream fakeInputStream = new ByteArrayInputStream(xmlContents.getBytes("UTF-8"));
            prop.loadFromXML(fakeInputStream);
            fakeInputStream.close();
        } catch(Throwable t) {
            t.printStackTrace();
        }
        
        return prop;
    }
    
    /**
     * Properties 를 XML 형식의 문자열로 변환합니다.
     */
    public static String toXML(Properties prop) {
        ByteArrayOutputStream fakeStream = null;
        
        try {
            fakeStream = new ByteArrayOutputStream();
            prop.storeToXML(fakeStream, "");
            fakeStream.close();
            return new String(fakeStream.toByteArray(), "UTF-8");
        } catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
    
    /** JSON 형식의 문자열을 인식해 JSON 지원 객체로 변환합니다. 문자열 내용이 단순 리터럴 (숫자, 문자열 등등) 이면 그 리터럴 값이 반환됩니다. */
    public static Object parseJson(String jsonStr) {
    	return JsonObject.parseJson(jsonStr);
    }
    
    /** 문자열 하나의 블록을 나눕니다. 따옴표를 인식합니다. */
    public static List<String> parseBlocks(String str) {
    	if(str == null) return new ArrayList<String>();
    	List<String> res = new ArrayList<String>();
    	
    	int len = str.length();
    	int idx = 0;
    	char quote = ' ';
    	int  revSlice = 0;
    	StringBuilder collector = new StringBuilder("");
    	for(idx=0; idx<len; idx++) {
    		char current = str.charAt(idx);
    		
    		if(quote != ' ') {
				if(current == quote) {
					if(revSlice % 2 == 0) {
						quote = ' ';
						for(int qdx=0; qdx<revSlice / 2; qdx++) {
							collector = collector.append('\\');
						}
						revSlice = 0;
						collector = collector.append(current);
					} else {
						for(int qdx=0; qdx<revSlice / 2; qdx++) {
							collector = collector.append('\\');
						}
						revSlice = 0;
						collector = collector.append('\\');
						collector = collector.append(current);
					}
				} else if(current == '\\') {
					revSlice++;
					if(revSlice >= 2) {
						collector = collector.append('\\');
						revSlice -= 2;
					}
				} else {
					collector = collector.append(current);
				}
				continue;
			} else if(current == '\'' || current == '"') {
				if(revSlice % 2 == 1) {
					collector = collector.append('\\');
					collector = collector.append(current);
					continue;
				} else {
					quote = current;
					collector = collector.append(current);
					continue;
				}
			} else if(current != ' ' && current != '\t') {
				if(current == '\\') {
					revSlice++;
					if(revSlice >= 2) {
						collector = collector.append('\\');
						revSlice -= 2;
					}
					continue;
				} else {
					collector = collector.append(current);
					continue;
				}
			} else {
				res.add(collector.toString().trim());
				collector.setLength(0);
				continue;
			}
    	}
    	
    	if(collector.length() >= 1) {
    		res.add(collector.toString().trim());
			collector.setLength(0);
    	}
    	
    	for(idx=0; idx<res.size(); idx++) {
    		String s = res.get(idx);
    		if(s.startsWith("'")) s = DataUtil.removeQuote(s);
    		res.set(idx, s);
    	}
    	
    	return res;
    }
    
    /**
     * <pre>
     * 하나의 문자열로부터, 명령과 주 매개변수, 옵션을 추출합니다.
     *     하나의 Map 객체로 반환하며, 명령과 주 매개변수 또한 옵션으로 취급하여 Map 에 같이 담아 반환합니다.
     *     이 때, 키로 명령은 ORDER, 주 매개변수는 PARAMETER 를 사용합니다.
     *     
     * 예)
     *     // 입력
     *     cat text.xml -l 1024 --comment nothing
     *     
     *     // 결과
     *     ORDER     : cat
     *     PARAMETER : text.xml
     *     l         : 1024
     *     comment   : nothing
     * 
     * </pre>
     */
    public static Map<String, String> parseParameter(String str) {
    	if(str == null) return new HashMap<String, String>();
    	Map<String, String> res = new HashMap<String, String>();
    	if(str.contains("\n")) str = str.replace("\n", " ");
    	str = str.trim();
    	
    	String order  = null;
    	String mParam = null;
    	String pKey   = null;
    	
    	List<String> blocks = parseBlocks(str);
    	StringBuilder collector = new StringBuilder("");
    	for(String b : blocks) {
    		b = b.trim();
    		if(order == null) {
    			order = b;
    			continue;
    		} else {
    			if(b.startsWith("-")) {
    				if(pKey != null) {
    					res.put(pKey, collector.toString().trim());
    					pKey = null;
    					collector.setLength(0);
    				}
    				
    				if(b.startsWith("--")) {
    					pKey = b.substring(2);
    				} else {
    					pKey = b.substring(1);
    					if(pKey.length() >= 2) throw new RuntimeException("Wrong length of parameter block " + b);
    				}
    			} else if(pKey == null) {
    				if(mParam == null) mParam = "";
    				mParam += " " + b;
    				continue;
    			} else {
    				collector = collector.append(" ").append(b);
    			}
    		}
    	}
    	
    	if(pKey != null) {
			res.put(pKey, collector.toString().trim());
			pKey = null;
			collector.setLength(0);
		}
    	
    	if(mParam != null) mParam = mParam.trim();
    	
    	res.put("ORDER"    , order.trim());
    	res.put("PARAMETER", mParam);
    	
    	return res;
    }
}
