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
package org.duckdns.hjow.commons.data;

/** 정수 (64비트) 로 사용됩니다. 기존 Wrapper 클래스와 달리 Mutable (기본적인 연산 시 새 인스턴스를 생성하지 않고 원래의 인스턴스가 변경)합니다. */
public class MutableInteger extends MutableNumber {
    private static final long serialVersionUID = -2753229728790562007L;
    protected long value = 0;
    public MutableInteger() {
        
    }
    public MutableInteger(long value) {
        this.value = value;
    }
    @Override
    public void increase() {
        value++;
    }
    public void add(Number numbers) {
        value += numbers.longValue();
    }
    public void subtract(Number numbers) {
        value -= numbers.longValue();
    }
    public void multiply(Number numbers) {
        value = value * numbers.longValue();
    }
    public void divide(Number numbers) {
        value = value / numbers.longValue();
    }
    public long getValue() {
        return value;
    }
    public void setValue(long value) {
        this.value = value;
    }
    @Override
    public Number getNumber() {
        return new Long(value);
    }
    @Override
    public void setNumber(Number value) {
        this.value = value.longValue();
    }
    @Override
    public boolean hasFloatingValue() {
        return false;
    }
}
