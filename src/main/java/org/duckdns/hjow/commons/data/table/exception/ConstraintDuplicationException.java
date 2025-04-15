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
package org.duckdns.hjow.commons.data.table.exception;

public class ConstraintDuplicationException extends ColumnDuplicationException {
    private static final long serialVersionUID = -8968556288735266490L;
    public ConstraintDuplicationException() {
        super("There is already another constraint with same name.");
    }
    public ConstraintDuplicationException(String consName) {
        super("There is already another constraint with the name '" + consName + "'.");
    }
}
