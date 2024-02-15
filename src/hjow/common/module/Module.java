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
package hjow.common.module;

import java.awt.Component;
import java.util.Map;

import hjow.common.core.Core;
import hjow.common.core.Releasable;
import hjow.common.ui.UI;

// TODO: Auto-generated Javadoc
/**
 * 이 클래스가 모듈임을 표시합니다.
 */
public interface Module extends Releasable, HasShortcut {
    
    /** 데스크톱 창 내부 대화상자 형식의 컴포넌트 */
    public static final int DESKTOP = 1;
    
    /** 외부 대화상자 형식의 컴포넌트 */
    public static final int DIALOG = 2;
    
    /** 출력되지 않는 모듈 */
    public static final int HIDDEN = 0;
    
    /** 메뉴 할당도 안되는 모듈 */
    public static final int NONE = -1;
    
    /**
     * 이 모듈의 짧은 이름을 반환합니다. 한 줄이며, 영어 소문자와 언더바만 허용됩니다.
     *
     * @return 모듈의 짧은 이름
     */
    public String getShortName();
    
    /**
     * 이 모듈의 풀네임을 반환합니다.
     *
     * @return 모듈의 이름
     */
    public String getName();
    
    /**
     * 이 모듈의 고유 ID값을 반환합니다. 다른 모듈과 이 값이 동일하면 같이 사용할 수 없습니다.
     *
     * @return 고유값
     */
    public long getId();
    
    /**
     * 이 모듈의 설명문을 반환합니다.
     *
     * @return 모듈의 설명
     */
    public String getDescription();
    
    /**
     * 이 모듈의 UI 타입을 반환합니다.
     *
     * @return 이 모듈의 UI 타입
     */
    public int getComponentType();
    
    /**
     * 이 모듈의 컴포넌트를 반환합니다.
     *
     * @return 컴포넌트
     */
    public Component getComponent();
    
    /** 모듈을 불러오면서 모듈 내에서 사용할 수 있는 저장 수단을 준비합니다. */
    public void prepareStorage(Map<String, String> storageObj);
    
    /** 모듈을 불러온 직후 호출됩니다. */
    public void initFirst();
    /** 모듈들을 다 불러온 후 호출됩니다. */
    public void initSecond(UI ui);
    /** 모듈 실행 시 호출됩니다. */
    public void run();
    /** initFirst 후 스크립트 엔진이 준비되면 호출됩니다. 쓰레드 기능을 사용하지 않으면 이 메소드에서 아무것도 하지 않는 것이 좋습니다. */
    public void initThread(Core core);
}
