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
package hjow.common.data.table;

import java.util.Map;

/** 행 별로 수행해야 할 작업이 있을 때 이 인터페이스와 메소드를 구현해 메소드 안에서 작업을 지정합니다. */
public interface RowIterateAction {
    /** 각 행 별로 작업을 수행합니다. */
    public void onEachRow(Map<String, Object> rowOne);
}
