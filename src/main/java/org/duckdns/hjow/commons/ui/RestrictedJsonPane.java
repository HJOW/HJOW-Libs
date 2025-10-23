/*
Copyright 2025 HJOW

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

import java.awt.Component;

import org.duckdns.hjow.commons.exception.KnownRuntimeException;
import org.duckdns.hjow.commons.json.JsonObject;

/** JSON 으로 레이아웃을 구성합니다. getRootComponent 메소드로 컴포넌트를 받아 원하는 위치에 add해 사용합니다. 더 이상 이 객체를 쓰지 않을 때에는 releaseResource 를 호출해 주세요. 허용된 컴포넌트만 생성이 가능합니다. */
public class RestrictedJsonPane extends JsonPane {
	private static final long serialVersionUID = -1408294821115479601L;
	
	public RestrictedJsonPane() { super(); }
	public RestrictedJsonPane(String json) { super(json); }
	public RestrictedJsonPane(JsonObject obj) { super(obj); }

	@Override
    protected Component processObject(JsonObject obj) {
        throw new KnownRuntimeException("Unsupported object type.");
    }
}
