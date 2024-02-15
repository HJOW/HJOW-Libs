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
package hjow.common.script.jdbc;

public class JDBCException extends RuntimeException {
    private static final long serialVersionUID = 3090977986949758157L;
    public JDBCException() {
        super();
    }
    public JDBCException(Throwable causes) {
        super(causes);
    }
}
