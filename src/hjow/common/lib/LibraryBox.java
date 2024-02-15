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
package hjow.common.lib;

import java.util.List;

import hjow.common.core.Core;
import hjow.common.core.Releasable;
import hjow.common.module.Module;
import hjow.common.script.ScriptObject;
import hjow.common.ui.UI;

/**
 * 
 * <pre>
 * hjow_lib_advanced 까지 포함시키면 이 인터페이스의 구현체로부터 추가 모듈과 추가 스크립트 객체를 불러옵니다. 구현체 클래스는 기본 생성자를 가져야 합니다.
 * hjow.common.lib.BundledBox 라는 이름이 클래스 패스에 존재하면 설정에 없더라도 자동으로 불러오려고 시도합니다.
 * </pre>
 * 
 * @author HJOW
 *
 */
public interface LibraryBox extends Releasable {
	/** 초기화 작업이 필요할 때 이 메소드를 오버라이드해 내용을 작성하세요. Core 객체는 가지고 있을 필요는 없습니다. */
	public void init(Core core);
	/** UI 초기화 시 호출됩니다. */
	public void onPrepareUI(Core core, UI ui);
	/** 스크립트 객체 리스트를 불러옵니다. */
	public List<ScriptObject> advancedObjects(Core core);
	/** 모듈 리스트를 불러옵니다. */
	public List<Module> advancedModules(Core core);
}
