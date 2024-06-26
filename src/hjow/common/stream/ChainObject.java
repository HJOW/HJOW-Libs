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

package hjow.common.stream;

import java.io.Closeable;
import java.util.List;

import hjow.common.core.Releasable;

public interface ChainObject extends Closeable, Releasable
{  
    /**
     * 탑재된 하위 스트림 객체들의 클래스명을 반환합니다.
     * 
     * @return 리스트
     */
    public List<String> getElementTypes();
}
