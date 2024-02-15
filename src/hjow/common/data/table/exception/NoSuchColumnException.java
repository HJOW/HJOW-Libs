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
package hjow.common.data.table.exception;

public class NoSuchColumnException extends RuntimeException {
	private static final long serialVersionUID = -7454876708014809204L;
	public NoSuchColumnException() {
		super("There is no column to match.");
	}
	public NoSuchColumnException(String columnName) {
		super("There is no column to match the name '" + columnName + "'.");
	}
}
