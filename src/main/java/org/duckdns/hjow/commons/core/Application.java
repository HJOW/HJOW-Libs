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
package org.duckdns.hjow.commons.core;

import org.duckdns.hjow.commons.ui.SwingUI;

/**
 * 프로그램 실행을 시작할 때 이 클래스 내 run 메소드를 이용하세요.
 * 
 * 
 * @author HJOW
 *
 */
public class Application {
    /** 응용 프로그램을 실행합니다. */
    public static void run(String appShortName, String appName, String version, String[] args) throws InstantiationException, IllegalAccessException {
        Core.restartEnabled = true;
        Core.loadCore(appShortName, appName, version, args).prepareUI(SwingUI.class).show();
    }
    
    /** 응용 프로그램을 실행합니다. */
    public static void run(String appShortName, String appName, String version) throws InstantiationException, IllegalAccessException {
        Core.restartEnabled = true;
        Core.loadCore(appShortName, appName, version).prepareUI(SwingUI.class).show();
    }
    
    /** 응용 프로그램을 실행합니다. */
    public static void run(String appShortName, String appName, String[] args) throws InstantiationException, IllegalAccessException {
        Core.restartEnabled = true;
        Core.loadCore(appShortName, appName, args).prepareUI(SwingUI.class).show();
    }
    
    /** 응용 프로그램을 실행합니다. */
    public static void run(String appShortName, String appName) throws InstantiationException, IllegalAccessException {
        Core.restartEnabled = true;
        Core.loadCore(appShortName, appName).prepareUI(SwingUI.class).show();
    }
}
