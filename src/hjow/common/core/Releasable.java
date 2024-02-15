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
package hjow.common.core;

import java.io.Serializable;

/**
 * 사용 종료 전 반드시 release 해주어야 하는, 리소스 관리가 필요한 클래스임을 표시할 때 사용합니다.
 */
public interface Releasable extends Serializable {
    /** 이 인스턴스의 사용을 종료할 때 호출해 주세요. */
    public void releaseResource();
}
