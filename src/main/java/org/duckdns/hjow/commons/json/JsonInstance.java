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
package org.duckdns.hjow.commons.json;

import org.duckdns.hjow.commons.xml.XMLSerializable;

public interface JsonInstance extends XMLSerializable, Iterable<Object> {
    /** JSON 형식의 문자열을 생성합니다. */
    public String toJSON();
    /** JSON 형식의 문자열을 생성합니다. */
    public String toJSON(boolean allowLineJump, boolean lookFine);
    /** 이 메소드는 직접 호출하지 마세요. */
    public String toJSON(String indent, boolean allowLineJumpString, boolean lookFine);
    /** 복제하여 새 객체를 만듭니다. */
    public JsonInstance cloneObject();
}
