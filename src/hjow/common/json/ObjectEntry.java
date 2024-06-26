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
package hjow.common.json;

import java.io.Serializable;
import java.util.Map;

public class ObjectEntry<T, X> implements Map.Entry<T, X>, Serializable {
    private static final long serialVersionUID = -6822817560553527249L;
    protected T key;
    protected X value;
    
    @Override
    public T getKey() {
        return key;
    }

    @Override
    public X getValue() {
        return value;
    }

    @Override
    public X setValue(X value) {
        this.value = value;
        return value;
    }

    public void setKey(T k) {
        this.key = k;
    }
}
