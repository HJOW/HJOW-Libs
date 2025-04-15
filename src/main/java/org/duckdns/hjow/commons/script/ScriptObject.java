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
package org.duckdns.hjow.commons.script;

import org.duckdns.hjow.commons.core.Releasable;

/** 스크립트 엔진에서 사용할 수 있는 객체들의 기본 클래스입니다. */
public abstract class ScriptObject extends PublicMethodOpenedClass implements Releasable {
    private static final long serialVersionUID = 8029074938421244342L;

    @Override
    public void releaseResource() {
        
    }
    
    /**
     * 스크립트 객체의 저장 변수명에 영향을 주는 고유 이름을 반환합니다. 고유해야 하며, 영문자와 언더바(_) 만으로 구성되어야 합니다.
     */
    public abstract String getPrefixName();
    /**
     * 초기화에 쓰이는 스크립트 구문을 반환합니다. 불러올 때 호출되어 각 스크립트 엔진에서 실행됩니다.
     * 
     * @param accessKey : 매번 프로그램 실행 때마다 발급되는 랜덤 키 (스크립트 이용자가 스크립트를 통해 클래스에 직접 액세스할 수 없도록 함)
     * @return 스크립트 구문
     */
    public abstract String getInitScript(String accessKey);
}
