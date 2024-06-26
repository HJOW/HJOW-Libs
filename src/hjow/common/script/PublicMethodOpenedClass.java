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
package hjow.common.script;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 이 클래스의 하위 클래스는 availables 메소드를 통해 사용 가능한 public 메소드를 항시 오픈하게 됩니다. */
public abstract class PublicMethodOpenedClass implements Serializable {
    private static final long serialVersionUID = 8642923854262326151L;

    /**
     * 사용 가능한 메소드 이름 리스트를 반환합니다.
     * 
     * @return 메소드 이름 리스트
     */
    public List<String> availables() {
        return getAvailableMethods(this);
    }
    
    @Deprecated
    public void isDeprecated() {
        
    }
    
    /**
     * 어떤 객체에 대해 사용 가능한 메소드 이름 리스트를 반환합니다.
     * 
     * @return 메소드 이름 리스트
     */
    public static List<String> getAvailableMethods(Object target) {
        Class<?> classObj = target.getClass();
        Method[] methods = classObj.getMethods();
        
        Set<String> resultSet = new HashSet<String>(); // 중복 제거를 위해 먼저 Set 으로 받음 (스크립트에서는 메소드 오버로딩이 안되므로)
        for(Method m : methods) {
            if(Modifier.isPublic(m.getModifiers())) {
                String methodName = m.getName();
                if(methodName.equals("getPrefixName")) continue;
                if(methodName.equals("getInitScript")) continue;
                if(methodName.equals("releaseResource")) continue;
                if(methodName.equals("getClass")) continue;
                if(methodName.equals("hashCode")) continue;
                if(methodName.equals("notify")) continue;
                if(methodName.equals("notifyAll")) continue;
                
                boolean isDeprecated = false;
                try {
                    Annotation[] annotations = m.getAnnotations();
                    if(annotations != null) {
                        for(Annotation a : annotations) {
                            Class<? extends Annotation> annotationClass = a.annotationType();
                            if(annotationClass != null) {
                                if(annotationClass.getName().equals("java.lang.Deprecated")) {
                                    isDeprecated = true;
                                    break;
                                }
                            }
                        }
                    }
                    
                } catch(AssertionError ignores) { }
                if(isDeprecated) continue;
                
                resultSet.add(methodName);
            }
        }
        List<String> results = new ArrayList<String>();
        results.addAll(resultSet);
        return results;
    }
}
