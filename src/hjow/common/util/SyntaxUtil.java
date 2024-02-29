/*
Copyright 2019 HJOW

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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>이 클래스에는 문자열에서 문법을 분석하는 데 사용되는 정적 메소드들이 있습니다.</p>
 * 
 * @author HJOW
 *
 */
public class SyntaxUtil {
	/** 문자열 내의 유효한 구분자들로 나눈 각각의 토큰을 반환합니다. */
	public static List<String> getTokens(String insides, char ... delims) {
		List<Integer> delimiterPoints = getDelimiterLocations(insides, delims);
		List<String> tokens = new ArrayList<String>();
		int befores = 0;
		for(int idx=0; idx<delimiterPoints.size(); idx++) {
			int after = delimiterPoints.get(idx).intValue();
			
			String contents = insides.substring(befores, after).trim();
			tokens.add(contents);
			
			befores = after;
		}
		String contents = insides.substring(befores).trim();
		tokens.add(contents);
		return tokens;
	}
	
	
	/** 문자열 내의 유효한 구분자들의 위치들을 반환합니다. */
	public static List<Integer> getDelimiterLocations(String insides, char ... delims) {
        List<Integer> delimiterPoints = new ArrayList<Integer>();
        int isSingleQuoteRange = 0;
        int isDoubleQuoteRange = 0;
        int isBigBracetRange = 0;
        int isMediumBracetRange = 0;
        char[] chars = insides.toCharArray();
        char beforeChar = ' ';
        StringBuilder logs = new StringBuilder("");
        for(int idx=0; idx<chars.length; idx++) {
            char charOne = chars[idx];
            logs.append(charOne);
            
            boolean needContinue = false;
            
            boolean isSinbleQuoteChanged  = false;
            boolean isDoubleQuoteChanged  = false;
            
            if(charOne != ' ') {
            	int a = 1;
            	a = a + 1;
            }
            // 따옴표 범위안에 있으면, 벗어날 때까지 무시
            if(isSingleQuoteRange >= 1) {
                if(charOne == '\'') {
                    if(beforeChar != '\\') {
                    	isSingleQuoteRange--;
                    	isSinbleQuoteChanged = true;
                    }
                }
                needContinue = true;
            }
            if(isDoubleQuoteRange >= 1) {
                if(charOne == '"') {
                    if(beforeChar != '\\') {
                    	isDoubleQuoteRange--;
                    	isDoubleQuoteChanged = true;
                    }
                }
                needContinue = true;
            }
            if(isBigBracetRange >= 1) {
                if(charOne == ']') {
                    isBigBracetRange--;
                    
                }
                needContinue = true;
            }
            if(isMediumBracetRange >= 1) {
                if(charOne == '}') {
                    isMediumBracetRange--;
                }
                needContinue = true;
            }
            
            // 따옴표 범위 바깥에 있으면...
            if(charOne == '\'' && beforeChar != '\\' && (! isSinbleQuoteChanged)) {
                isSingleQuoteRange++;
                beforeChar = charOne;
                needContinue = true;
            }
            if(charOne == '"' && beforeChar != '\\' && (! isDoubleQuoteChanged)) {
                isDoubleQuoteRange++;
                beforeChar = charOne;
                needContinue = true;
            }
            if(charOne == '[') {
                isBigBracetRange++;
                beforeChar = charOne;
                needContinue = true;
            }
            if(charOne == '{') {
                isMediumBracetRange++;
                beforeChar = charOne;
                needContinue = true;
            }
            
            if(needContinue) {
            	beforeChar = charOne;
            	continue;
            }
            
            // 이제 구분자 처리
            boolean isAccepted = false;
            for(char delimOne : delims) {
            	if(delimOne == charOne) {
            		isAccepted = true;
            		break;
            	}
            }
            if(isAccepted) {
                delimiterPoints.add(new Integer(idx));
            }
            
            beforeChar = charOne;
        }
        return delimiterPoints;
    }
	
	/**
	 * 한 줄로 통합된 문자열을 여러 줄로 나눕니다. (\n을 인식합니다.)
	 * 
	 * @param originals 한 줄로 통합된 문자열
	 * @return 여러 줄로 나뉜 문자열
	 */
	public static String lineMultilize(String originals) {
		if(originals == null) return null;
		StringBuilder res = new StringBuilder("");
		
		char[] chars = originals.toCharArray();
		originals = null;
		
		char current;
		int revSlashCount = 0;
		boolean quoteArea = false;
		for(int idx=0; idx<chars.length; idx++) {
			current = chars[idx];
			
			// System.out.println(idx + "\t" + current + "\t" + revSlashCount + "\t" + quoteArea + "\t" + res);
			
			if(current == '"') {
				for(int rdx=0; rdx<Math.ceil(revSlashCount / 2.0); rdx++) {
					res = res.append("\\");
				}
				if(revSlashCount >= 1 && revSlashCount % 2 == 0) {
					if(quoteArea) {
						res = res.append("\\");
					}
				}
				res = res.append("\"");
				quoteArea = (! quoteArea);
				revSlashCount = 0;
				continue;
			} else if(current == '\\') {
				revSlashCount++;
				
				if(revSlashCount >= 3) {
					res = res.append("\\");
					revSlashCount = 1;
				}
				
				continue;
			} else if(revSlashCount >= 1 && current != '\\') {
				if(current == 'n') {
					res = res.append("\n");
				} else if(current == 't') {
					res = res.append("\t");
				} else if(current == '"') {
					res = res.append("\\").append("\"");
				} else {
					for(int rdx=0; rdx<Math.ceil(revSlashCount / 2.0); rdx++) {
						res = res.append("\\");
					}
					res = res.append(current);
				}
				revSlashCount = 0;
				continue;
			}
			
			res = res.append(current);
		}
		
		return res.toString().trim();
	}
	
	/**
	 * 여러 줄 문자열을 한줄로 합칩니다. (줄 나눔 위치에는 \n 이 들어갑니다.)
	 * 
	 * @param originals 기존 문자열
	 * @return 한 줄로 변환된 문자열
	 */
	public static String lineSinglize(String originals) {
		if(originals == null) return null;
		StringBuilder res = new StringBuilder("");
		
		char[] chars = originals.toCharArray();
		originals = null;
		
		char current;
		int revSlashCount = 0;
		for(int idx=0; idx<chars.length; idx++) {
			current = chars[idx];
			
			if(current == '\\') {
				revSlashCount++;
				continue;
			} else {
				for(int rdx=0; rdx<revSlashCount; rdx++) {
					res = res.append("\\");
				}
				revSlashCount = 0;
				
				if(current == '\r') {
					continue;
				}
				
				if(current == '\n') {
					res = res.append("\\").append("n");
					continue;
				}
			}
			
			res = res.append(current);
		}
		
		return res.toString().trim();
	}
}
