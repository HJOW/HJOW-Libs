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
package hjow.common.ui;

import java.util.Properties;

public interface PropertiesEditor {

    /** 컴포넌트에 입력된 Properties 를 읽어내 반환합니다. */
    public Properties getProp();
    
    /** 새로운 Properties 를 입력합니다. */
    public void setProp(Properties prop);

}
