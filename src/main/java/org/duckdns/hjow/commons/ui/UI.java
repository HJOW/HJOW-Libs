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
package org.duckdns.hjow.commons.ui;

import java.awt.Window;

import org.duckdns.hjow.commons.core.Core;
import org.duckdns.hjow.commons.core.Releasable;
import org.duckdns.hjow.commons.module.Module;

public interface UI extends Releasable {
    /** UI 객체 초기화 시 호출됩니다. */
    public void init(Core core);
    
    /** UI를 화면에 띄웁니다. */
    public void show();
    
    /** 로그를 출력합니다. */
    public void log(String str);
    
    /** UI 컴포넌트에 해당 모듈을 추가합니다. */
    public void attach(Module m);
    /** UI 컴포넌트에서 해당 모듈을 제거합니다. */
    public void remove(Module m);
    /** 최상위 프레임 객체를 반환합니다. */
    public Window getFrame();
    
    /** 사용자에게 알림 메시지를 출력합니다. */
    public void alert(String contents);
    
    /** 사용자에게 예/아니오 를 물어봅니다. */
    public boolean askConfirm(String contents);
}
