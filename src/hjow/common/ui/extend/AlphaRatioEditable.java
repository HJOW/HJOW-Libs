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
package hjow.common.ui.extend;

public interface AlphaRatioEditable {
    /** 투명도를 변경합니다. 0 ~ 1 사이의 값을 지정할 수 있으며 숫자가 클 수록 선명합니다. */
    public void setAlphaRatio(float alphaRatio);
}
